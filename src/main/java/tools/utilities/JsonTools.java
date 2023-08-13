package tools.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonTools {

    public static <T> String toJson(T object) {
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String jsonString, Class<T> tClass) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, tClass);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

}
