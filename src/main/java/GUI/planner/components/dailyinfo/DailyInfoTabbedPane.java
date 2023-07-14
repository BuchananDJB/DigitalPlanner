package GUI.planner.components.dailyinfo;

import Tools.DataTools;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DailyInfoTabbedPane extends JTabbedPane {

    private final List<DailyInfoSplitPane> dailyInfoSplitPanes;
    private int addTabButtonIndex = 0;

    public DailyInfoTabbedPane() {
        this.dailyInfoSplitPanes = new ArrayList<>();
        this.addTab("+", null);
        this.setTabComponentAt(0, createAddTabButton());

        addNewTab("General");
    }

    public void addNewTab(String title) {
        DailyInfoSplitPane dailyInfoSplitPane = new DailyInfoSplitPane();
        dailyInfoSplitPanes.add(dailyInfoSplitPane);
        this.addTab(title, dailyInfoSplitPane);
        this.setSelectedIndex(this.getTabCount() - 1);
    }

    private JButton createAddTabButton() {
        JButton addButton = new JButton("+");
        addButton.setFocusable(false);
        addButton.setPreferredSize(new Dimension(20, 20));
        addButton.addActionListener(e -> {
            String tabName = JOptionPane.showInputDialog(this, "Enter tab name:");
            if (!DataTools.isEmptyString(tabName)) {
                addNewTab(tabName);
            }
        });
        return addButton;
    }

    @Override
    public boolean isEnabledAt(int index) {
        if (index == addTabButtonIndex) {
            return false;
        }
        return super.isEnabledAt(index);
    }
}
