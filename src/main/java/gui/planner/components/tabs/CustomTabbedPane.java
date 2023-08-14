package gui.planner.components.tabs;

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
import java.util.List;
import java.util.Map;

public abstract class CustomTabbedPane extends JTabbedPane {

    protected final int addTabButtonIndex = 0;
    protected Map<String, Component> closedTabs;

    protected void addTabMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int index = CustomTabbedPane.this.indexAtLocation(e.getX(), e.getY());
                    JPopupMenu popupMenu = createPopupMenu(index);
                    popupMenu.show(CustomTabbedPane.this, e.getX(), e.getY());
                }
            }
        });
    }

    protected JPopupMenu createPopupMenu(int index) {
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

    protected JButton createAddTabButton() {
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

    protected void initializeTabsFromDirectories(String directoryString) {
        Path path = Path.of(directoryString);
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

    protected void reopenTab(String tabName) {
        this.addTab(tabName, closedTabs.remove(tabName));
        this.setSelectedIndex(this.getTabCount() - 1);
    }

    protected void closeTab(int index) {
        if (index >= 0 && index < getTabCount()) {
            String tabName = getTitleAt(index);
            closedTabs.putIfAbsent(tabName, getComponentAt(index));
            removeTabAt(index);
        }
    }

    protected int showConfirmationDialog(String tabName) {
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

    protected abstract void addNewTab(String title);

    protected abstract void deleteTab(int index);

}
