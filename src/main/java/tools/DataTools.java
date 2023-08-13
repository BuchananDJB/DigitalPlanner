package tools;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import gui.tools.GUITools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class DataTools {
    public static <T> Stream<T> stream(Collection<T> collection) {
        return collection != null ? collection.stream() : Stream.empty();
    }

    public static Stream<?> stream(Object[] array) {
        return array != null ? Arrays.stream(array) : Stream.empty();
    }

    public static boolean isNullEmptyBlankString(String string) {
        return string == null || string.isEmpty() || string.isBlank();
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

    public static List<String> readFileAsListOfStrings(String filePath) {
        List<String> fileContents = new ArrayList<>();
        try {
            if (!Files.exists(Path.of(filePath))) {
                Files.createFile(Path.of(filePath));
            }
            fileContents = Files.readAllLines(Path.of(filePath));
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

    public static void writeStringsToFile(List<String> lines, String filePath) {
        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                Files.createFile(path);
            }

            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
            GUITools.displayDialog("Error saving data to: " + filePath);
        }
    }

    public static void deleteFile(String filePath) {
        try {
            Path path = Path.of(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            GUITools.displayDialog("Error deleting file: " + filePath);
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

    public static void deleteEmptySubdirectories(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            GUITools.displayDialog("Error clearing empty directories.");
            return;
        }

        deleteEmptySubdirectoriesRecursive(directory);
    }

    private static void deleteEmptySubdirectoriesRecursive(File directory) {
        File[] subdirectories = directory.listFiles(File::isDirectory);

        if (subdirectories == null) {
            return;
        }

        for (File subdirectory : subdirectories) {
            deleteEmptySubdirectoriesRecursive(subdirectory);
            if (isEmptyDirectory(subdirectory)) {
                subdirectory.delete();
            }
        }
    }

    private static boolean isEmptyDirectory(File directory) {
        File[] files = directory.listFiles();
        return files == null || files.length == 0;
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
}
