package getRequest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
//import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;

public class GetData {

  private static final String PROJECT_BASE_PATH = "projectBasePath";
/*  @Test
    public void testResponseCode() {
       
             String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6MSwiaWF0IjoxNTQ4MzIxMTc1fQ.iYnWTDb2uhFjjJhVPcSoeIbxX_yIYYJ1VRyyBXOPz7IguCvrqkPbkw6qW5fgL361olnJbqIj2b1oGlCmn8bGNw";
             Response response =   RestAssured.given().auth().oauth2(token).get("http://10.80.40.104:8888/codeinsight/api/projects/39/users?roleId=REVIEWER");
             int code = response.getStatusCode(); 
             System.out.println("Status code is "+code);
             Assert.assertEquals(code,200);
    }*/
    
    private static final String BASE_URI = "baseUri";
    private static final String TOKEN = "token";
    Properties restProperty = new Properties();
    
    @BeforeTest
    public void getData() {
        try {
            FileReader reader=new FileReader("rest.properties");
            restProperty.load(reader);
        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test(priority=2)
    public void testResponseRecord() {
        System.out.println("-----"+restProperty.getProperty(BASE_URI));
        given().header("Content-Type","application/json")
        .auth().oauth2(restProperty.getProperty(TOKEN))
        .baseUri(restProperty.getProperty(BASE_URI))
        .basePath(restProperty.getProperty(PROJECT_BASE_PATH))
        .pathParam("projectId",39).queryParam("roleId", "REVIEWER")
        .when().get("/{projectId}/users")
        .then().assertThat().statusCode(200).and().body("size()", is(1));
        
      /*  RestAssured.given().auth().oauth2(restProperty.getProperty("token"))
        .get("http://10.80.40.104:8888/codeinsight/api/projects/39/users?roleId=REVIEWER").then().assertThat()
        .body("size()", is(1));*/
    }
    
    @Test(priority=1)
    public void testResponseCode() {
        
        given().header("Content-Type","application/json")
        .auth().oauth2(restProperty.getProperty(TOKEN))
        .baseUri(restProperty.getProperty(BASE_URI))
        .basePath(restProperty.getProperty(PROJECT_BASE_PATH))
        .pathParam("projectId",39).queryParam("roleId", "REVIEWER")
        .when().get("/{projectId}/users")
        .then().assertThat().statusCode(200);
        
        
        /*RestAssured.baseURI="http://10.80.40.104:8888/codeinsight/api";
       
        given().header("Content-Type","application/json").auth().oauth2(token).param("projectId","39").queryParam("roleId", "REVIEWER");
        
        when().get("/projects/projectId/users").then().assertThat().statusCode(200);*/
    }
    
}
