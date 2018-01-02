
package rest_udemy;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.Properties;
import org.testng.annotations.Test;
import support.common;

public class twitter_add_delete_tweet {
    
    @Test 
    public void twitterAddDeleteTweet() throws IOException {
        
        Properties env = common.load_properties();
                
        RestAssured.baseURI=env.getProperty("TWITTER_HOST");

        // Create using query param
        Response res = 
        given().
            auth().oauth(
                    env.getProperty("TWITTER_CONSUMER_KEY"),
                    env.getProperty("TWITTER_CONSUMER_SECRET"),
                    env.getProperty("TWITTER_ACCESS_TOKEN"),
                    env.getProperty("TWITTER_SECRET_TOKEN")).
            queryParam("status", "a tweet by automation by query param11").
        when().post("/update.json").
        then().assertThat().statusCode(200).
        extract().response();
        
        JsonPath json_response = common.response_to_json_path(res); 
        String t = json_response.get("text");
        System.out.println(t);
        String tweet_id = json_response.get("id_str");
        System.out.println(tweet_id);
        
        // Delete tweet
        res = 
        given().
            auth().oauth(
                    env.getProperty("TWITTER_CONSUMER_KEY"),
                    env.getProperty("TWITTER_CONSUMER_SECRET"),
                    env.getProperty("TWITTER_ACCESS_TOKEN"),
                    env.getProperty("TWITTER_SECRET_TOKEN")).
            pathParams("tweetId", tweet_id).
        when().post("/destroy/{tweetId}.json").
        then().assertThat().statusCode(200).
        extract().response();
        
        String res_str = res.asString();
        System.out.println("");
        System.out.println(res_str);

        // Confirm tweet is deleted
        res = 
        given().
            auth().oauth(
                    env.getProperty("TWITTER_CONSUMER_KEY"),
                    env.getProperty("TWITTER_CONSUMER_SECRET"),
                    env.getProperty("TWITTER_ACCESS_TOKEN"),
                    env.getProperty("TWITTER_SECRET_TOKEN")).
            pathParams("tweetId", tweet_id).
        when().get("/show/{tweetId}.json").
        then().assertThat().statusCode(404).
        extract().response();
        
    }
    
}
