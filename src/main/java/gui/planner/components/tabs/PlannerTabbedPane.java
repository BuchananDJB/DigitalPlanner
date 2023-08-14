package gui.planner.components.tabs;

import tools.Constants;
import tools.utilities.FileTools;

import javax.swing.*;
import java.util.HashMap;

public class PlannerTabbedPane extends CustomTabbedPane {

    public PlannerTabbedPane() {
        this.closedTabs = new HashMap<>();

        addTabMouseListener();
        this.addTab("+", null);
        this.setTabComponentAt(0, createAddTabButton());

        initializeTabsFromDirectories(Constants.TABVIEWS_DIRECTORY);
    }

    public void addNewTab(String title) {
        FileTools.createDirectory(Constants.TABVIEWS_DIRECTORY + title);
        PlannerTabView dailyInfoTabbedPane = new PlannerTabView(title);
        this.addTab(title, dailyInfoTabbedPane);
        this.setSelectedIndex(this.getTabCount() - 1);
    }

    @Override
    protected void deleteTab(int index) {
        if (index >= 0 && index < getTabCount()) {
            String tabName = getTitleAt(index);
            int choice = showConfirmationDialog(tabName);
            if (choice == JOptionPane.YES_OPTION) {
                FileTools.deleteDirectoryAndAllContents(Constants.TABVIEWS_DIRECTORY + "/" + tabName);
                unregisterSaveItems(index);
                removeTabAt(index);
            }
        }
    }

    private void unregisterSaveItems(int index) {
        if (getComponentAt(index) instanceof PlannerTabView tabView) {
            tabView.unregisterAllSaveItems();
        }
    }

}
