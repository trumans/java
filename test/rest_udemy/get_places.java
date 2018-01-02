package rest_udemy;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import org.testng.annotations.Test;

public class get_places {

    @Test
    public void getPlaces() 
    {
        RestAssured.baseURI = "https://maps.googleapis.com";

        float expect_lat = -33.8f;
        float expect_lng = 151.2f;
        float margin = 0.1f;
        Response raw_response = 
        given().
            param("location", "-33.8670522,151.1957362").
            param("radius", "100").
            param("key", "AIzaSyBgYWAODitmA2feE5pEJy7372CV2tiVfD0").
        when().get("/maps/api/place/nearbysearch/json").
        then().assertThat().
            // assert response status code is 200 and content type is json
            statusCode(200).and().
            contentType(ContentType.JSON).and().
            header("server", "pablo").and().
            // assert the latitude and longitude of first result in response
            // Note: body assertions require comparison method (i.e. equalTo, lessThan)
            body("results[0].geometry.location.lat", lessThan(expect_lat)).and().
            body("results[0].geometry.location.lat", greaterThan(expect_lat-margin)).and().
            body("results[0].geometry.location.lng", greaterThan(expect_lng)).and().
            body("results[0].geometry.location.lng", lessThan(expect_lng+margin)).
            log().body().
        extract().response();
        
        String res_str = raw_response.asString();
        //System.out.println(res_str);
        JsonPath json_response = new JsonPath(res_str);
        
        int c = json_response.get("results.size()");
        for (int i=0; i<c; i++) {
            String name_path = String.format("results[%d].name", i);
            String name = json_response.get(name_path);
            System.out.println(name);
        }
    }
}
