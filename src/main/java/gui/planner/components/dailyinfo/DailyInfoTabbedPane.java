package gui.planner.components.dailyinfo;

import gui.planner.components.notes.NotesScrollPane;
import gui.planner.components.tabs.CustomTabbedPane;
import gui.planner.components.todolist.TodoList;
import gui.planner.components.todolist.TodoListTable;
import tools.Constants;
import tools.savemanager.SaveManager;
import tools.utilities.FileTools;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

public class DailyInfoTabbedPane extends CustomTabbedPane {

    private final String pathFormattedDate;
    private final String plannerTabTitle;

    public DailyInfoTabbedPane(String plannerTabTitle, String pathFormattedDate) {
        this.closedTabs = new HashMap<>();
        this.pathFormattedDate = pathFormattedDate;
        this.plannerTabTitle = plannerTabTitle;

        addTabMouseListener();
        this.addTab("+", null);
        this.setTabComponentAt(0, createAddTabButton());

        initializeTabsFromDirectories(Constants.TABVIEWS_DIRECTORY + plannerTabTitle + "/" + Constants.DAILY_INFO + pathFormattedDate);
    }

    @Override
    public void addNewTab(String title) {
        FileTools.createDirectory(Constants.TABVIEWS_DIRECTORY + plannerTabTitle + "/" +
                Constants.DAILY_INFO + pathFormattedDate + "/" + title);
        DailyInfoSplitPane dailyInfoSplitPane =
                new DailyInfoSplitPane(Constants.TABVIEWS_DIRECTORY + plannerTabTitle + "/" +
                        Constants.DAILY_INFO + pathFormattedDate + "/" + title);
        this.addTab(title, dailyInfoSplitPane);
        this.setSelectedIndex(this.getTabCount() - 1);
    }

    @Override
    protected void deleteTab(int index) {
        if (index >= 0 && index < getTabCount()) {
            String tabName = getTitleAt(index);
            int choice = showConfirmationDialog(tabName);
            if (choice == JOptionPane.YES_OPTION) {
                FileTools.deleteDirectoryAndAllContents(Constants.TABVIEWS_DIRECTORY + plannerTabTitle + "/" +
                        Constants.DAILY_INFO + pathFormattedDate + "/" + tabName);
                unregisterSaveItems(index);
                removeTabAt(index);
            }
        }
    }

    public void unregisterAllSaveItems() {
        for (int i = 0; i < getTabCount(); ++i) {
            if (getComponentAt(i) instanceof DailyInfoSplitPane) {
                unregisterSaveItems(i);
            }
        }
    }

    private void unregisterSaveItems(int index) {
        SaveManager saveManager = new SaveManager();

        DailyInfoSplitPane dailyInfoSplitPane = (DailyInfoSplitPane) getComponentAt(index);
        NotesScrollPane notesScrollPane = dailyInfoSplitPane.getDailyNotesScrollPane();
        saveManager.unregisterSaveItem(notesScrollPane);

        TodoList todoList = dailyInfoSplitPane.getDailyTodoList();
        List<TodoListTable> todoListTables = todoList.getAllTodoListTables();
        todoListTables.forEach(saveManager::unregisterSaveItem);
    }

}
