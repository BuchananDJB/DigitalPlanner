package tools;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import gui.tools.GUITools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
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
            if (!Files.exists(Path.of(filePath))) {
                Files.createFile(Path.of(filePath));
            }
            fileContents = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            GUITools.displayDialog("An error occurred while reading contents of file: " + filePath);
        }
        return fileContents;
    }

    public static void writeStringToFile(String contents, String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            Files.write(path, contents.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            GUITools.displayDialog("An error occurred while writing to file: " + filePath);
        }
    }

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

    public static void createDirectory(String directoryString) {
        Path directoryPath = Path.of(directoryString);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                GUITools.displayDialog("Error occurred while creating folder: " + directoryString);
                e.printStackTrace();
            }
        }
    }

    public static void deleteDirectoryAndAllContents(String directoryPath) {
            Path directory = Paths.get(directoryPath);
            if (Files.exists(directory)) {
                try (Stream<Path> walk = Files.walk(directory)) {
                    walk.sorted(Comparator.reverseOrder())
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
