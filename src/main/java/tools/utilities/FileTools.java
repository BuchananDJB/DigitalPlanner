package tools.utilities;

import gui.tools.GUITools;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class FileTools {

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

    public static void deleteEmptySubdirectoriesAndFiles(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            GUITools.displayDialog("Error clearing empty directories and files.");
            return;
        }

        deleteEmptySubdirectoriesAndFilesRecursive(directory);
    }

    private static void deleteEmptySubdirectoriesAndFilesRecursive(File directory) {
        File[] subdirectoriesAndFiles = directory.listFiles();

        if (subdirectoriesAndFiles == null) {
            return;
        }

        for (File item : subdirectoriesAndFiles) {
            if (item.isDirectory()) {
                deleteEmptySubdirectoriesAndFilesRecursive(item);
                if (isEmptyDirectory(item)) {
                    item.delete();
                }
            } else if (item.isFile() && item.length() == 0) {
                item.delete();
            }
        }
    }

    private static boolean isEmptyDirectory(File directory) {
        File[] files = directory.listFiles();
        return files == null || files.length == 0;
    }

    public static List<String> listSubdirectories(String directoryPath) {
        List<String> subdirectories = new ArrayList<>();

        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] subdirectoryFiles = directory.listFiles(File::isDirectory);
            if (subdirectoryFiles != null) {
                for (File subdirectory : subdirectoryFiles) {
                    subdirectories.add(subdirectory.getName());
                }
            }
        }
        return subdirectories;
    }

    public static boolean directoryAndFilesAreEmpty(String directoryPath) {
        Path path = Paths.get(directoryPath);

        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return true;
        }

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            for (Path file : directoryStream) {
                if (!Files.isDirectory(file) && Files.size(file) > 0) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            GUITools.displayDialog("Error occurred while parsing directories and files.");
        }
        return true;
    }

}
