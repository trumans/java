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
import io.restassured.path.xml.XmlPath;
import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.Test;


public class add_delete_place_xml {

    @Test
    public void addDeletePlaceViaXml() throws FileNotFoundException, IOException
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
            body(payLoads.postPlace("xml")).
        when().post(resources.addPlace("xml")).
        then().assertThat().
            statusCode(200).and().
            contentType(ContentType.XML).and().
            body("PlaceAddResponse.status", equalTo("OK")).
        // extract response so it can be saved in a variable
        extract().response();
        
        String res_str = res.asString();
        //System.out.println(res_str);
        XmlPath res_xml = new XmlPath(res_str);

        // Delete the place using place_id from add API response
        String place_id = res_xml.get("PlaceAddResponse.place_id");
        //System.out.println(place_id);
        given().
            queryParam("key", env.getProperty("APP_KEY")).
            body(payLoads.deletePlace("xml", place_id)).
        when().post(resources.deletePlace("xml")).
        then().assertThat().
            statusCode(200).and().
            contentType(ContentType.XML).and().
            body("PlaceDeleteResponse.status", equalTo("OK"));
        
        // Repeat deletion and expect not_found response
        given().
            queryParam("key", env.getProperty("APP_KEY")).
            body(payLoads.deletePlace("xml", place_id)).
        when().post(resources.deletePlace("xml")).
        then().assertThat().
            statusCode(200).and().
            contentType(ContentType.XML).and().
            body("PlaceDeleteResponse.status", equalTo("NOT_FOUND"));
    }
}
