package no.api.youngman.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;


public final class JsonUtil {

    private static Logger log = LoggerFactory.getLogger(JsonUtil.class.getName());

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");

    private JsonUtil() {

    }

    public static JsonArray transformToArray(ResponseEntity<String> responseEntity) {

        String jsonString = responseEntity.getBody();
        JsonParser parser = new JsonParser();
        JsonElement elements = parser.parse(jsonString);
        if(elements.isJsonArray()) {
            return elements.getAsJsonArray();
        } else {
            return null;
        }
    }

    public static String jsonObjectLoader(JsonObject jsonObject,String member) {
        JsonElement element = jsonObject.get(member);
        if(element != null && element.isJsonPrimitive()) {
            return element.getAsString();
        }else{
            return null;
        }
    }

    public static Long jsonObjectLoaderLong(JsonObject jsonObject,String member) {
        JsonElement element = jsonObject.get(member);
        if(element != null && element.isJsonPrimitive()) {
            return element.getAsLong();
        }else{
            return null;
        }
    }

    public static DateTime jsonObjectLoaderDateTime(JsonObject jsonObject, String member) {
        JsonElement element = jsonObject.get(member);
        if(element != null && element.isJsonPrimitive()) {
            try {
                return formatter.parseDateTime(element.getAsString());
            } catch (IllegalArgumentException ex) {
                log.error("error cann't parse json",ex);
                return null;
            }
        }else{
            return null;
        }
    }

}
