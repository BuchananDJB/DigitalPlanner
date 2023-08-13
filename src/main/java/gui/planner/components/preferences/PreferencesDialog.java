package gui.planner.components.preferences;

import gui.GUIInitializer;
import tools.Constants;
import tools.DataTools;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PreferencesDialog extends JDialog {

    private String[] themes = {"ArcDarkOrange", "CyanLight", "Dark", "DarkPurple",
            "GradiantoMidnightBlue", "LightFlat", "MaterialPalenight", "SolarizedLight"};
    private JComboBox<String> themeComboBox;

    public PreferencesDialog(JFrame parentFrame) {
        super(parentFrame, "Preferences", true);

        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel themeLabel = new JLabel("Theme:");
        themeComboBox = new JComboBox<>(themes);
        themeComboBox.addActionListener(e -> applyTheme());

        // TODO: Determine why this JComboBox is not updating the displayed value as expected
//        GUIInitializer.getCurrentTheme().ifPresent(theme -> {
//            themeComboBox.setSelectedItem(theme);
//            themeComboBox.revalidate();
//            themeComboBox.repaint();
//        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        contentPanel.add(themeLabel);
        contentPanel.add(themeComboBox);
        contentPanel.add(closeButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(parentFrame);
    }

    private void applyTheme() {
        String selectedTheme = (String) themeComboBox.getSelectedItem();
        if (selectedTheme != null) {
            GUIInitializer.applyTheme(selectedTheme);

            List<String> preferences = DataTools.readFileAsListOfStrings(Constants.PREFERENCES_PATH);
            for (int i = 0; i < preferences.size(); i++) {
                String preference = preferences.get(i);
                if (preference.startsWith("theme:")) {
                    preferences.set(i, "theme:" + selectedTheme);
                    break;
                }
            }
            DataTools.writeStringsToFile(preferences, Constants.PREFERENCES_PATH);
        }
    }

}
