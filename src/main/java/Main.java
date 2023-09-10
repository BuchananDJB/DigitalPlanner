import gui.GUIInitializer;
import tools.Constants;
import tools.savemanager.SaveManager;
import tools.utilities.FileTools;

import java.io.File;

public class Main {

    private static SaveManager saveManager;

    public static void main(String[] args) {
        saveManager = new SaveManager();
        setup();
        GUIInitializer guiInitializer = new GUIInitializer(Main::saveAndShutdown);
        try {
            guiInitializer.initializeGUI();
            saveManager.startDefaultAutoSave();
        } catch (Exception e) {
            e.printStackTrace();
            saveAndShutdown();
        }
    }

    private static void setup() {
        FileTools.createDirectory(Constants.TABVIEWS_DIRECTORY);
        File preferencesFile = new File(Constants.PREFERENCES_FILE_PATH);
        if (!preferencesFile.isFile()) {
            FileTools.writeStringToFile("theme:FlatDark", Constants.PREFERENCES_FILE_PATH);
        }
    }

    private static void saveAndShutdown() {
        saveManager.saveAllData();
        saveManager.shutdown();
        FileTools.deleteEmptySubdirectoriesAndFiles(Constants.TABVIEWS_DIRECTORY);
    }
}
