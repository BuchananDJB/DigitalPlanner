package GUI.planner.components.dailyinfo;

import Tools.DataTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DailyInfoTabbedPane extends JTabbedPane {

    private final List<DailyInfoSplitPane> dailyInfoSplitPanes;
    private int addTabButtonIndex = 0;

    public DailyInfoTabbedPane() {
        this.dailyInfoSplitPanes = new ArrayList<>();
        addTabMouseListener();
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

    private void addTabMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = DailyInfoTabbedPane.this.indexAtLocation(e.getX(), e.getY());
                    if (index >= 0 && index != addTabButtonIndex) {
                        JPopupMenu popupMenu = createPopupMenu(index);
                        popupMenu.show(DailyInfoTabbedPane.this, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private JPopupMenu createPopupMenu(int index) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem closeTabMenuItem = new JMenuItem("Close Tab");
        JMenuItem deleteTabMenuItem = new JMenuItem("Delete Tab");

        closeTabMenuItem.addActionListener(e -> closeTab(index));
        deleteTabMenuItem.addActionListener(e -> deleteTab(index));

        popupMenu.add(closeTabMenuItem);
        popupMenu.add(deleteTabMenuItem);

        return popupMenu;
    }

    private void closeTab(int index) {
        if (index >= 0 && index < getTabCount()) {
            removeTabAt(index);
        }
    }

    private void deleteTab(int index) {
        if (index >= 0 && index < getTabCount()) {
            String tabName = getTitleAt(index);
            int choice = showConfirmationDialog(tabName);
            if (choice == JOptionPane.YES_OPTION) {
                removeTabAt(index);
                dailyInfoSplitPanes.remove(index - 1);
                saveDailyInfo();
            }
        }
    }

    private int showConfirmationDialog(String tabName) {
        String message = "Are you sure you want to delete the " + tabName + " tab?";
        return JOptionPane.showConfirmDialog(this, message, "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    }

    private void saveDailyInfo() {
        // TODO: Save the daily information associated with the deleted tab
    }

    @Override
    public boolean isEnabledAt(int index) {
        if (index == addTabButtonIndex) {
            return false;
        }
        return super.isEnabledAt(index);
    }
}
