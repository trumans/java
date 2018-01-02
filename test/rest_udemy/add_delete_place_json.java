package rest_udemy;

import support.resources;
import support.payLoads;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.Test;

public class add_delete_place_json {

    @Test
    public void addDeletePlaceViaJson() throws FileNotFoundException, IOException
    {
        // get values external configuration file
        Properties env = new Properties();
        FileInputStream fis=new FileInputStream("./src/support/env.properties");
        env.load(fis);
        
        // value is pulled from env.properties file
        RestAssured.baseURI = env.getProperty("HOST");

        Response res = 
        given().
            // POST query parameter requires explicit queryParam() method
            queryParam("key", env.getProperty("APP_KEY")).
            body(payLoads.postPlace("json")).
        when().post(resources.addPlace("json")).
        then().assertThat().
            statusCode(200).and().
            contentType(ContentType.JSON).and().
            body("status", equalTo("OK")).
        // extract response so it can be saved in a variable
        extract().response();
        
        String res_str = res.asString();
        //System.out.println(res_str);
        JsonPath res_json = new JsonPath(res_str);
        
        // Delete the place using place_id from add API response
        String place_id = res_json.get("place_id");
        //System.out.println(place_id);
        given().
            queryParam("key", env.getProperty("APP_KEY")).
            body(payLoads.deletePlace("json", place_id)).
        when().post(resources.deletePlace("json")).
        then().assertThat().
            statusCode(200).and().
            contentType(ContentType.JSON).and().
            body("status", equalTo("OK"));
        
        // Repeat deletion and expect not_found response
        given().
            queryParam("key", env.getProperty("APP_KEY")).
            body(payLoads.deletePlace("json", place_id)).
        when().post(resources.deletePlace("json")).
        then().assertThat().
            statusCode(200).and().
            contentType(ContentType.JSON).and().
            body("status", equalTo("NOT_FOUND"));
    }
}
