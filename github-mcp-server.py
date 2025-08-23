#!/usr/bin/env python

import os
from mcp.server.fastmcp import FastMCP

# Create a FastMCP instance with only the 'name' argument
server = FastMCP(
    name="github-md-server",
)

# Tool: list markdown files
@server.tool(
    name="list_markdown_files",
    description="List all markdown files in the .github folder",
)
def list_markdown_files() -> list[str]:
    """Lists all markdown files in the .github folder."""
    github_folder = os.path.join(os.getcwd(), ".github")
    return [f for f in os.listdir(github_folder) if f.endswith(".md")]


# Tool: read markdown file
@server.tool(
    name="read_markdown_file",
    description="Read the content of a markdown file from the .github folder",
)
def read_markdown_file(filename: str) -> str:
    """Reads the content of a markdown file."""
    github_folder = os.path.join(os.getcwd(), ".github")
    file_path = os.path.join(github_folder, filename)

    if not os.path.exists(file_path):
        raise FileNotFoundError("File not found")

    with open(file_path, "r", encoding="utf-8") as file:
        return file.read()


if __name__ == "__main__":
    # Start the server using standard I/O for communication.
    server.run(transport="stdio")