package rest_udemy;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.Test;

public class add_delete_place {

    //@Test
    public static void main(String[] args)
    {
        RestAssured.baseURI = "https://maps.googleapis.com";

        String body_text = 
            "{ \"location\": { \"lat\": -33.8669710, \"lng\": 151.1958750 }, " +
            "  \"accuracy\": 50, " +
            "  \"name\": \"Google Shoes!\", " +
            "  \"phone_number\": \"(02) 9374 4000\", " +
            "  \"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\", " +
            "  \"types\": [\"shoe_store\"], " +
            "  \"website\": \"http://www.google.com.au/\", " +
            "  \"language\": \"en-AU\" }"; 
        
        Response res = 
        given().
            // POST query parameter requires explicit queryParam() method
            queryParam("key", "AIzaSyBgYWAODitmA2feE5pEJy7372CV2tiVfD0").
            body(body_text).
        when().post("maps/api/place/add/json").
        then().assertThat().
            statusCode(200).and().
            contentType(ContentType.JSON).and().
            body("status", equalTo("OK")).
        // extract response so it can be saved in a variable
        extract().response();
        
        // Get the place id
        String res_str = res.asString();
        System.out.println(res_str);
        JsonPath res_json = new JsonPath(res_str);
        String place_id = res_json.get("place_id");
        System.out.println(place_id);
        
        // Delete the place using place_id from add API response
        given().
            queryParam("key", "AIzaSyBgYWAODitmA2feE5pEJy7372CV2tiVfD0").
            body("{ \"place_id\": \"" + place_id + "\" }").
        when().post("/maps/api/place/delete/json").
        then().assertThat().
            statusCode(200).and().
            contentType(ContentType.JSON).and().
            body("status", equalTo("OK"));
    }
}
