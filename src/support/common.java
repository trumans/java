
package support;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class common {

    public static Properties load_properties() throws FileNotFoundException, IOException {
        Properties env = new Properties();
        FileInputStream fis=new FileInputStream("./src/support/env.properties");
        env.load(fis);
        return env;
    }
    public static JsonPath response_to_json_path(Response response) {
        String res_str = response.asString();
        JsonPath json_response = new JsonPath(res_str);
        return json_response;
    }
}
