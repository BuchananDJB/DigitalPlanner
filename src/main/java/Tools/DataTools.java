package Tools;

import GUI.Tools.GUITools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
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
}
