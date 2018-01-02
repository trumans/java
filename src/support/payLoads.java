package support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class payLoads {
    
    public static String postPlace(String format) throws IOException
    {
        if (format == "json") {
            return new String( 
                "{ \"location\": { \"lat\": -33.8669710, \"lng\": 151.1958750 }, " +
                "  \"accuracy\": 50, " +
                "  \"name\": \"Google Shoes!\", " +
                "  \"phone_number\": \"(02) 9374 4000\", " +
                "  \"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\", " +
                "  \"types\": [\"shoe_store\"], " +
                "  \"website\": \"http://www.google.com.au/\", " +
                "  \"language\": \"en-AU\" }"
        );
        } else if (format == "xml") {
            String payload_path = new String("./src/support/postPlace.xml");
            return new String(Files.readAllBytes(Paths.get(payload_path)));
        }
        else {
            return new String("");
        }
    }
    
    public static String deletePlace(String format, String place_id){
        
        if (format == "json") {
            return String.format("{ \"place_id\": \"%s\" }", place_id);
        } else if (format == "xml") {
        String p = 
            "<PlaceDeleteRequest> <place_id>%s</place_id> </PlaceDeleteRequest>";
        return String.format(p, place_id);
        }
        else {
            return new String("");
        }
 
    }
            
    
}
