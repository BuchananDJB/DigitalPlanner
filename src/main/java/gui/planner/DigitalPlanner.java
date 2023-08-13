package gui.planner;

import gui.planner.components.calendar.CalendarPanel;
import gui.planner.components.calendar.DateSelectionListener;
import gui.planner.components.dailyinfo.DailyInfo;
import gui.planner.components.notes.NotesTextArea;
import gui.planner.components.preferences.PreferencesDialog;
import gui.planner.components.todolist.TodoList;
import tools.Constants;
import tools.savemanager.SaveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DigitalPlanner extends JFrame implements DateSelectionListener {

    private final TodoList generalTodoList;
    private final NotesTextArea generalNotesTextArea;
    private final JSplitPane upperSplitPane;
    private final Map<String, DailyInfo> dailyInfoMapByDate;

    private DailyInfo currentDailyInfoPane;
    private CalendarPanel calendarPanel;


    public DigitalPlanner() {
        this.dailyInfoMapByDate = new HashMap<>();

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

        initializeCalendarPanel();
        this.generalTodoList = new TodoList(Constants.PLANNER_ROOT_DIRECTORY);
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, calendarPanel, generalTodoList);
        leftSplitPane.setDividerLocation(200);

        String todaysDateString = getTodaysDateString();
        DailyInfo dailyInfo = new DailyInfo(todaysDateString);
        dailyInfoMapByDate.putIfAbsent(todaysDateString, dailyInfo);
        this.currentDailyInfoPane = dailyInfo;
        this.upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, currentDailyInfoPane);
        upperSplitPane.setDividerLocation(300);

        this.generalNotesTextArea = createGeneralNotesTextArea();
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplitPane, generalNotesTextArea);

        mainSplitPane.setDividerLocation(550);
        this.getContentPane().add(BorderLayout.CENTER, mainSplitPane);

        refreshUI();
    }

    private void initializeCalendarPanel() {
        this.calendarPanel = new CalendarPanel();
        this.calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
        this.calendarPanel.registerDateSelectionListener(this);
    }

    private NotesTextArea createGeneralNotesTextArea() {
        NotesTextArea notesTextArea = new NotesTextArea(Constants.PLANNER_ROOT_DIRECTORY);
        notesTextArea.setBorder(BorderFactory.createTitledBorder("General Notes"));
        return notesTextArea;
    }

    @Override
    public void onDateSelected(Calendar selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = dateFormat.format(selectedDate.getTime());

        DailyInfo newDailyInfoPane = dailyInfoMapByDate.getOrDefault(dateString, new DailyInfo(dateString));
        dailyInfoMapByDate.putIfAbsent(dateString, newDailyInfoPane);
        upperSplitPane.remove(currentDailyInfoPane);
        upperSplitPane.add(newDailyInfoPane);

        int dividerLocation = upperSplitPane.getDividerLocation();
        upperSplitPane.setDividerLocation(dividerLocation);
        this.currentDailyInfoPane = newDailyInfoPane;

        refreshUI();
    }

    private void refreshUI() {
        revalidate();
        repaint();
    }

    private String getTodaysDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}
