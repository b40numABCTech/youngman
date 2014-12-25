package no.api.youngman.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.ResponseEntity;

public final class JsonUtil {
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
}
