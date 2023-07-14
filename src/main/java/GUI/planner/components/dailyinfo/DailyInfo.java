package GUI.planner.components.dailyinfo;

import GUI.Tools.GUITools;
import Tools.DataTools;

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createAddTabButton());
        this.add(buttonPanel, BorderLayout.NORTH);
    }

    private JButton createAddTabButton() {
        JButton addTabButton = new JButton("+");
        addTabButton.setFocusable(false);
        addTabButton.setPreferredSize(new Dimension(20, 20));
        addTabButton.addActionListener(e -> {
            String tabName = JOptionPane.showInputDialog(this, "Enter tab name:");
            if (!DataTools.isEmptyString(tabName)) {
                dailyInfoTabbedPane.addNewTab(tabName);
            }
        });
        return addTabButton;
    }
}