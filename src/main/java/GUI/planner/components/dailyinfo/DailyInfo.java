package GUI.planner.components.dailyinfo;

import GUI.Tools.GUITools;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DailyInfo extends JPanel {
    private final String date;
    private final DailyInfoTabbedPane dailyInfoTabbedPane;

    public DailyInfo() {
        this(new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()));
    }

    public DailyInfo(String date) {
        this.date = date;
        this.setBorder(BorderFactory.createTitledBorder(GUITools.prettyPrintDate(date)));

        this.dailyInfoTabbedPane = new DailyInfoTabbedPane();
        this.setLayout(new BorderLayout());
        this.add(dailyInfoTabbedPane, BorderLayout.CENTER);
    }
}