package gui.planner.components.dailyinfo;

import gui.tools.GUITools;

import javax.swing.*;
import java.awt.*;

public class DailyInfo extends JPanel {
    private final DailyInfoTabbedPane dailyInfoTabbedPane;

    public DailyInfo(String plannerTabTitle, String pathFormattedDate) {
        this.setBorder(BorderFactory.createTitledBorder(GUITools.prettyPrintDate(pathFormattedDate)));

        this.dailyInfoTabbedPane = new DailyInfoTabbedPane(plannerTabTitle, pathFormattedDate);
        this.setLayout(new BorderLayout());
        this.add(dailyInfoTabbedPane, BorderLayout.CENTER);
    }

    public void unregisterAllSaveItems() {
        dailyInfoTabbedPane.unregisterAllSaveItems();
    }
}