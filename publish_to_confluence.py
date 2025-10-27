import json, os, sys, base64, requests
from pathlib import Path

BASE   = os.environ["CONFLUENCE_BASE"].rstrip("/")
EMAIL  = os.environ["CONFLUENCE_EMAIL"]
TOKEN  = os.environ["CONFLUENCE_TOKEN"]
PAGEID = os.environ["CONFLUENCE_PAGE_ID"]

AUTH = (EMAIL, TOKEN)
HEADERS = {"Accept": "application/json"}

def attach_or_update(page_id: str, file_path: Path):
    name = file_path.name
    url  = f"{BASE}/rest/api/content/{page_id}/child/attachment"
    # Check existing
    r = requests.get(url, params={"filename": name, "expand": "version"}, auth=AUTH, headers=HEADERS)
    r.raise_for_status()
    results = r.json().get("results", [])
    if results:
        attach_id = results[0]["id"]
        upload_url = f"{BASE}/rest/api/content/{page_id}/child/attachment/{attach_id}/data"
    else:
        upload_url = url

    files = {"file": (name, open(file_path, "rb"), "image/svg+xml")}
    r = requests.post(upload_url, auth=AUTH, files=files)
    r.raise_for_status()
    return r.json()["results"][0]["id"] if not results else attach_id

def get_page(page_id: str):
    url = f"{BASE}/rest/api/content/{page_id}"
    r = requests.get(url, params={"expand":"version,body.storage"}, auth=AUTH, headers=HEADERS)
    r.raise_for_status()
    return r.json()

def update_page_with_images(page_id: str, filenames):
    page = get_page(page_id)
    ver  = page["version"]["number"] + 1
    body = page["body"]["storage"]["value"]

    # ensure each image is referenced once in the page body (storage format)
    for name in filenames:
        macro = f'<ac:image><ri:attachment ri:filename="{name}"/></ac:image>'
        if name not in body:
            body += f"<p>{macro}</p>"

    url = f"{BASE}/rest/api/content/{page_id}"
    payload = {
        "id": page_id,
        "type": "page",
        "title": page["title"],
        "version": {"number": ver, "minorEdit": True},
        "body": {"storage": {"value": body, "representation": "storage"}}
    }
    r = requests.put(url, auth=AUTH, headers={"Content-Type":"application/json"}, data=json.dumps(payload))
    r.raise_for_status()

def main(out_dir):
    out = Path(out_dir)
    svgs = sorted(out.glob("*.svg"))
    if not svgs:
        print("No SVGs found in", out_dir)
        sys.exit(1)

    names = []
    for svg in svgs:
        print("Uploading", svg.name)
        attach_or_update(PAGEID, svg)
        names.append(svg.name)

    update_page_with_images(PAGEID, names)
    print("Done.")

if __name__ == "__main__":
    main(sys.argv[1] if len(sys.argv) > 1 else "out")
