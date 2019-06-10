package postRequest;

import org.json.simple.JSONObject;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import junit.framework.Assert;

public class PostData {
    
    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaWF0IjoxNTQ4MzIxMTc1fQ.iYnWTDb2uhFjjJhVPcSoeIbxX_yIYYJ1VRyyBXOPz7IguCvrqkPbkw6qW5fgL361olnJbqIj2b1oGlCmn8bGNw"; 

    @Test
    public void testPost() {

        /* Response response =  RestAssured.given().auth().oauth2(token).param("securityContactId", "bumrah")
        .param("legalContactId", "virat").contentType("application/json").post("http://10.80.40.104:8888/codeinsight/api/projects/39/users");*/
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("securityContactId", "yogesh");
        jsonObject.put("legalContactId", "yogesh");

        Response response =  RestAssured.given().auth().oauth2(token).accept(ContentType.JSON).contentType(ContentType.JSON).body(jsonObject, ObjectMapperType.JACKSON_2).post("http://10.80.40.104:8888/codeinsight/api/projects/39/users");

        int code = response.getStatusCode(); 
        System.out.println("Status code is "+code);
        Assert.assertEquals(code,200);

        }
}
