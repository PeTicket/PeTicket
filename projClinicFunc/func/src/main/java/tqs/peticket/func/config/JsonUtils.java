package tqs.peticket.func.config;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonUtils {
    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static JSONObject responseToJson(String response) throws ParseException {
        return (JSONObject) new JSONParser().parse(response);
    }

    public static JSONArray responseToJsonArray(String response) throws ParseException {
        return (JSONArray) new JSONParser().parse(response);
    }
}