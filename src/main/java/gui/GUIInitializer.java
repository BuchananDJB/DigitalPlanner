package gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightIJTheme;
import gui.planner.DigitalPlanner;
import gui.tools.GUITools;
import tools.Constants;
import tools.DataTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;

public class GUIInitializer {

    private final WindowCloseCallback windowCloseCallback;

    public GUIInitializer(WindowCloseCallback windowCloseCallback) {
        this.windowCloseCallback = windowCloseCallback;
    }

    public void initializeGUI() {
        setupFlatLaf();
        modifyUIDefaults();

        initializePlannerFrame();
        //initializeTestFrame();
    }

    private void setupFlatLaf() {
        Optional<String> themeNameOpt = getCurrentTheme();
        if (themeNameOpt.isPresent()) {
            String themeName = themeNameOpt.get().split(":")[1];
            applyTheme(themeName);
        } else {
            FlatDarkLaf.setup();
        }
    }

    public static Optional<String> getCurrentTheme() {
        List<String> preferences = DataTools.readFileAsListOfStrings(Constants.PREFERENCES_PATH);
        return DataTools.stream(preferences).filter(item -> item.startsWith("theme")).findFirst();
    }

    public static void applyTheme(String themeName) {
        switch (themeName) {
            case "ArcDarkOrange" -> FlatArcDarkOrangeIJTheme.setup();
            case "CyanLight" -> FlatCyanLightIJTheme.setup();
            case "DarkPurple" -> FlatDarkPurpleIJTheme.setup();
            case "GradiantoMidnightBlue" -> FlatGradiantoMidnightBlueIJTheme.setup();
            case "LightFlat" -> FlatLightFlatIJTheme.setup();
            case "MaterialPalenight" -> FlatMaterialPalenightIJTheme.setup();
            case "SolarizedLight" -> FlatSolarizedLightIJTheme.setup();
            default -> FlatDarkLaf.setup();
        }
        SwingUtilities.invokeLater(GUIInitializer::updateUIDefaults);
    }

    private static void updateUIDefaults() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals("FlatLaf")) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    GUITools.displayDialog("Error updating Laf.");
                }
            }
        }

        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }
    }

    private void modifyUIDefaults() {
        UIManager.put("TabbedPane.showTabSeparators", true);

        UIManager.put("ScrollBar.thumbArc", 999 );
        UIManager.put("ScrollBar.thumbInsets", new Insets( 2, 2, 2, 2 ));
    }

    private void initializePlannerFrame() {
        DigitalPlanner digitalPlanner = new DigitalPlanner();
        digitalPlanner.setTitle("Digital Planner");
        digitalPlanner.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        digitalPlanner.setSize(1280, 720);
        digitalPlanner.setLocationRelativeTo(null);
        digitalPlanner.setVisible(true);

        digitalPlanner.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowCloseCallback.onWindowClose();
                digitalPlanner.dispose();
            }
        });
    }

    private void initializeTestFrame() {
        JFrame testFrame = new JFrame();
        testFrame.setTitle("Test Frame");
        testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        testFrame.setSize(1280, 720);

        testFrame.add(new JTextArea());

        testFrame.setLocationRelativeTo(null);
        testFrame.setVisible(true);
    }

    public interface WindowCloseCallback {
        void onWindowClose();
    }
}
