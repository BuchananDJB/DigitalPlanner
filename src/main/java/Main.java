import gui.GUIInitializer;
import tools.Constants;
import tools.DataTools;
import tools.savemanager.SaveManager;

public class Main {

    private static SaveManager saveManager;

    public static void main(String[] args) {
        saveManager = new SaveManager();
        GUIInitializer guiInitializer = new GUIInitializer(Main::saveAndShutdown);

        try {
            guiInitializer.initializeGUI();
            saveManager.startAutoSave();
        } catch (Exception e) {
            e.printStackTrace();
            saveAndShutdown();
        }
    }

    private static void saveAndShutdown() {
        saveManager.saveAllData();
        saveManager.shutdown();
        DataTools.deleteEmptySubdirectories(Constants.DAILY_INFO_DIRECTORY);
    }
}
