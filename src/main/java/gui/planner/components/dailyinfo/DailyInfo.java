package gui.planner.components.dailyinfo;

import gui.tools.GUITools;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DailyInfo extends JPanel {
    private final String pathFormattedDate;
    private final DailyInfoTabbedPane dailyInfoTabbedPane;

    public DailyInfo() {
        this(new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()));
    }

    public DailyInfo(String pathFormattedDate) {
        this.pathFormattedDate = pathFormattedDate;
        this.setBorder(BorderFactory.createTitledBorder(GUITools.prettyPrintDate(pathFormattedDate)));

        this.dailyInfoTabbedPane = new DailyInfoTabbedPane(pathFormattedDate);
        this.setLayout(new BorderLayout());
        this.add(dailyInfoTabbedPane, BorderLayout.CENTER);
    }
}