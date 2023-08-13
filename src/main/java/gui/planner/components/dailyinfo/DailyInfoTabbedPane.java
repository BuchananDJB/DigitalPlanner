package gui.planner.components.dailyinfo;

import gui.planner.components.notes.NotesScrollPane;
import gui.planner.components.todolist.TodoList;
import gui.planner.components.todolist.TodoListTable;
import tools.Constants;
import tools.savemanager.SaveManager;
import tools.utilities.FileTools;
import tools.utilities.StringTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyInfoTabbedPane extends JTabbedPane {

    private final Map<String, DailyInfoSplitPane> closedTabs;
    private final String pathFormattedDate;
    private final int addTabButtonIndex = 0;

    public DailyInfoTabbedPane(String pathFormattedDate) {
        this.closedTabs = new HashMap<>();
        this.pathFormattedDate = pathFormattedDate;

        addTabMouseListener();
        this.addTab("+", null);
        this.setTabComponentAt(0, createAddTabButton());

        initializeTabsFromDirectories();
    }

    private void addTabMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = DailyInfoTabbedPane.this.indexAtLocation(e.getX(), e.getY());
                    JPopupMenu popupMenu = createPopupMenu(index);
                    popupMenu.show(DailyInfoTabbedPane.this, e.getX(), e.getY());

                }
            }
        });
    }

    private JPopupMenu createPopupMenu(int index) {
        JPopupMenu popupMenu = new JPopupMenu();

        if (index != -1 && index != addTabButtonIndex) {
            JMenuItem closeTabMenuItem = new JMenuItem("Close Tab");
            closeTabMenuItem.addActionListener(e -> closeTab(index));
            popupMenu.add(closeTabMenuItem);

            JMenuItem deleteTabMenuItem = new JMenuItem("Delete Tab");
            deleteTabMenuItem.addActionListener(e -> deleteTab(index));
            popupMenu.add(deleteTabMenuItem);
        }

        List<String> closedTabNames = new ArrayList<>(closedTabs.keySet());
        if (!closedTabNames.isEmpty()) {
            JMenu reopenMenu = new JMenu("Reopen Tab");

            for (String tabName : closedTabNames) {
                JMenuItem reopenMenuItem = new JMenuItem(tabName);
                reopenMenuItem.addActionListener(e -> reopenTab(tabName));
                reopenMenu.add(reopenMenuItem);
            }

            popupMenu.add(reopenMenu);
        }

        return popupMenu;
    }

    private JButton createAddTabButton() {
        JButton addButton = new JButton("+");
        addButton.setFocusable(false);
        addButton.setPreferredSize(new Dimension(20, 20));
        addButton.addActionListener(e -> {
            String tabName = JOptionPane.showInputDialog(this, "Enter tab name:");
            if (!StringTools.isNullEmptyBlankString(tabName)) {
                addNewTab(tabName);
            }
        });
        return addButton;
    }

    private void initializeTabsFromDirectories() {
        Path path = Path.of(Constants.DAILY_INFO_DIRECTORY + pathFormattedDate);
        FileTools.createDirectory(path.toString());
        try {
            List<String> directories = Files.list(path).map(Path::toString).toList();
            directories.forEach(directory -> {
                String[] splitDirectory = directory.split("\\\\");
                String tabName = splitDirectory[splitDirectory.length - 1];
                addNewTab(tabName);
            });

            if (directories.isEmpty()) {
                addNewTab("General");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewTab(String title) {
        FileTools.createDirectory(Constants.DAILY_INFO_DIRECTORY + pathFormattedDate + "/" + title);
        DailyInfoSplitPane dailyInfoSplitPane =
                new DailyInfoSplitPane(Constants.DAILY_INFO_DIRECTORY + pathFormattedDate + "/" + title);
        this.addTab(title, dailyInfoSplitPane);
        this.setSelectedIndex(this.getTabCount() - 1);
    }

    private void closeTab(int index) {
        if (index >= 0 && index < getTabCount()) {
            String tabName = getTitleAt(index);
            DailyInfoSplitPane tabComponent = (DailyInfoSplitPane) getComponentAt(index);
            closedTabs.putIfAbsent(tabName, tabComponent);
            removeTabAt(index);
        }
    }

    private void reopenTab(String tabName) {
        DailyInfoSplitPane dailyInfoSplitPane = closedTabs.remove(tabName);
        this.addTab(tabName, dailyInfoSplitPane);
        this.setSelectedIndex(this.getTabCount() - 1);
    }

    private void deleteTab(int index) {
        if (index >= 0 && index < getTabCount()) {
            String tabName = getTitleAt(index);
            int choice = showConfirmationDialog(tabName);
            if (choice == JOptionPane.YES_OPTION) {
                FileTools.deleteDirectoryAndAllContents(Constants.DAILY_INFO_DIRECTORY + pathFormattedDate + "/" + tabName);
                unregisterSaveItems(index);
                removeTabAt(index);
            }
        }
    }

    private void unregisterSaveItems(int index) {
        SaveManager saveManager = new SaveManager();

        DailyInfoSplitPane dailyInfoSplitPane = (DailyInfoSplitPane) getComponentAt(index);
        NotesScrollPane notesScrollPane = dailyInfoSplitPane.getDailyNotesTextArea();
        saveManager.unregisterSaveItem(notesScrollPane);

        TodoList todoList = dailyInfoSplitPane.getDailyTodoList();
        List<TodoListTable> todoListTables = todoList.getAllTodoListTables();
        todoListTables.forEach(saveManager::unregisterSaveItem);
    }

    private int showConfirmationDialog(String tabName) {
        String message = "Are you sure you want to delete the " + tabName + " tab?";
        return JOptionPane.showConfirmDialog(this, message, "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    }

    @Override
    public boolean isEnabledAt(int index) {
        if (index == addTabButtonIndex) {
            return false;
        }
        return super.isEnabledAt(index);
    }
}
