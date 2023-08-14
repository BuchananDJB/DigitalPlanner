package gui.planner;

import gui.planner.components.preferences.PreferencesDialog;
import gui.planner.components.tabs.PlannerTabbedPane;
import tools.savemanager.SaveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class DigitalPlanner extends JFrame {

    public DigitalPlanner() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(e -> {
            SaveManager saveManager = new SaveManager();
            saveManager.saveAllData();
        });
        fileMenu.add(saveMenuItem);

        JMenuItem preferencesMenuItem = new JMenuItem("Preferences");
        preferencesMenuItem.addActionListener(e -> {
            PreferencesDialog preferencesDialog = new PreferencesDialog(this);
            preferencesDialog.setVisible(true);
            refreshUI();
        });
        fileMenu.add(preferencesMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> {
            WindowEvent windowClosing = new WindowEvent(DigitalPlanner.this, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(windowClosing);
        });

        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        PlannerTabbedPane plannerTabbedPane = new PlannerTabbedPane();
        this.getContentPane().add(plannerTabbedPane, BorderLayout.CENTER);
    }

    private void refreshUI() {
        revalidate();
        repaint();
    }

}
