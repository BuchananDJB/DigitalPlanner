import com.formdev.flatlaf.FlatDarkLaf;
import gui.planner.DigitalPlanner;
import tools.savemanager.SaveManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    private static SaveManager saveManager;

    public static void main(String[] args) {
        saveManager = new SaveManager();
        try {
            initializeGUI();
            saveManager.startAutoSave();
        } catch (Exception e) {
            e.printStackTrace();
            saveAndShutdown();
        }
    }

    private static void initializeGUI() {
        // TODO: figure out how to make it purple
        FlatDarkLaf.setup();
        initializeJFrame();
    }

    private static void initializeJFrame() {
        DigitalPlanner digitalPlanner = new DigitalPlanner();
        digitalPlanner.setTitle("Digital Planner");
        digitalPlanner.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        digitalPlanner.setSize(1280, 720);
        digitalPlanner.setLocationRelativeTo(null);
        digitalPlanner.setVisible(true);

        digitalPlanner.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveAndShutdown();
                digitalPlanner.dispose();
            }
        });
    }

    private static void saveAndShutdown() {
        saveManager.saveAllData();
        saveManager.shutdown();
    }
}
