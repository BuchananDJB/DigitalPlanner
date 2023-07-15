package Tools;

import GUI.Tools.GUITools;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class DataTools {
    public static Stream<?> stream(Collection<?> collection) {
        return collection != null ? collection.stream() : Stream.empty();
    }

    public static Stream<?> stream(Object[] array) {
        return array != null ? Arrays.stream(array) : Stream.empty();
    }

    public static boolean isEmptyString(String string) {
        return string == null || string.isEmpty();
    }

    public static String readFileAsString(String filePath) {
        String fileContents = "";
        try {
            fileContents = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            GUITools.displayDialog(e.getMessage());
        }
        return fileContents;
    }

    public static void writeStringToFile(String contents, String filePath) {
        try {
            Files.write(Path.of(filePath), contents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            GUITools.displayDialog(e.getMessage());
        }
    }

    public static <T> String toJson(T object) {
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String jsonString, Class<T> tClass) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, tClass);
    }

    public static <T> List<T> fromJson(String jsonString, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, type);
    }
}
