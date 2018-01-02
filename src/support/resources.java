package support;

public class resources {

    public static String addPlace(String format)
    {
        if (format == "json") {
            return new String("maps/api/place/add/json");
        } else if (format == "xml") {
            return new String("maps/api/place/add/xml");
        } else {
            return new String("");
        }
    } 
    
    public static String deletePlace(String format)
    {
        if (format == "json") {
            return new String("maps/api/place/delete/json");
        } else if (format == "xml") {
            return new String("maps/api/place/delete/xml");
        } else {
            return new String("");
        }
    }

}
