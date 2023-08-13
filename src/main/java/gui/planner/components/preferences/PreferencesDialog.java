package gui.planner.components.preferences;

import gui.GUIInitializer;
import tools.Constants;
import tools.DataTools;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PreferencesDialog extends JDialog {

    private final String[] themes = {"ArcDarkOrange", "CyanLight", "Dark", "DarkPurple",
            "GradiantoMidnightBlue", "LightFlat", "MaterialPalenight", "SolarizedLight"};
    private final JComboBox<String> themeComboBox;

    public PreferencesDialog(JFrame parentFrame) {
        super(parentFrame, "Preferences", true);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);

        JLabel themeLabel = new JLabel("Theme:");
        contentPanel.add(themeLabel, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        themeComboBox = new JComboBox<>(themes);
        themeComboBox.addActionListener(e -> applyTheme());
        contentPanel.add(themeComboBox, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 10, 10, 10);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        contentPanel.add(closeButton, gbc);

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