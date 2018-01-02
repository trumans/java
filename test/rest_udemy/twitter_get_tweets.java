
package rest_udemy;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.Properties;
import org.testng.annotations.Test;
import support.common;

public class twitter_get_tweets {
    
    @Test
    public void twitterGetTweets() throws IOException {
  
        Properties env = common.load_properties();
                
        RestAssured.baseURI=env.getProperty("TWITTER_HOST");

        Response res = 
        given().
            auth().oauth(
                    env.getProperty("TWITTER_CONSUMER_KEY"),
                    env.getProperty("TWITTER_CONSUMER_SECRET"),
                    env.getProperty("TWITTER_ACCESS_TOKEN"),
                    env.getProperty("TWITTER_SECRET_TOKEN")).
            queryParam("count", "20").
        when().get("/home_timeline.json").
        then().assertThat().statusCode(200).
        extract().response();

        JsonPath json_response = common.response_to_json_path(res); 
        int c = json_response.get("$.size()");
        for (int i=0; i<c; i++) {
            System.out.println("");
            String path = String.format("text[%d]", i);
            String val = json_response.get(path).toString();
            System.out.println(val);
            path = String.format("id[%d]", i);
            val = json_response.get(path).toString();
            System.out.println(val);

        }    
    }
    
}
