package gui.planner.components.tabs;

import gui.planner.components.calendar.CalendarPanel;
import gui.planner.components.calendar.DateSelectionListener;
import gui.planner.components.dailyinfo.DailyInfo;
import gui.planner.components.notes.NotesScrollPane;
import gui.planner.components.todolist.TodoList;
import tools.Constants;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PlannerTabView extends JPanel implements DateSelectionListener {

    private final JSplitPane upperSplitPane;
    private final TodoList generalTodoList;
    private final NotesScrollPane generalNotesScrollPane;
    private final Map<String, DailyInfo> dailyInfoMapByDate;
    private final String tabTitle;

    private DailyInfo currentDailyInfoPane;
    private CalendarPanel calendarPanel;

    public PlannerTabView(String tabTitle) {
        this.dailyInfoMapByDate = new HashMap<>();
        this.tabTitle = tabTitle;

        initializeCalendarPanel();
        this.generalTodoList = new TodoList(Constants.TABVIEWS_DIRECTORY + tabTitle);
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, calendarPanel, generalTodoList);
        leftSplitPane.setDividerLocation(200);

        String todaysDateString = getTodaysDateString();
        DailyInfo dailyInfo = new DailyInfo(tabTitle, todaysDateString);
        dailyInfoMapByDate.putIfAbsent(todaysDateString, dailyInfo);

        this.currentDailyInfoPane = dailyInfo;
        this.upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, currentDailyInfoPane);
        upperSplitPane.setDividerLocation(300);

        this.generalNotesScrollPane = createGeneralNotesTextArea();
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplitPane, generalNotesScrollPane);

        mainSplitPane.setDividerLocation(550);
        setLayout(new BorderLayout());
        add(mainSplitPane, BorderLayout.CENTER);

        refreshUI();
    }

    private void initializeCalendarPanel() {
        this.calendarPanel = new CalendarPanel(tabTitle);
        this.calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
        this.calendarPanel.registerDateSelectionListener(this);
    }

    private String getTodaysDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private NotesScrollPane createGeneralNotesTextArea() {
        NotesScrollPane notesScrollPane = new NotesScrollPane(Constants.TABVIEWS_DIRECTORY + tabTitle);
        notesScrollPane.setBorder(BorderFactory.createTitledBorder("General Notes"));
        return notesScrollPane;
    }

    public void unregisterAllSaveItems() {
        generalTodoList.unregisterAllTodoListSaveItems();
        generalNotesScrollPane.unregisterTextSaveItem();
        dailyInfoMapByDate.values().forEach(DailyInfo::unregisterAllSaveItems);
    }

    private void refreshUI() {
        revalidate();
        repaint();
    }

    @Override
    public void onDateSelected(Calendar selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = dateFormat.format(selectedDate.getTime());

        DailyInfo newDailyInfoPane = dailyInfoMapByDate.getOrDefault(dateString, new DailyInfo(tabTitle, dateString));
        dailyInfoMapByDate.putIfAbsent(dateString, newDailyInfoPane);
        upperSplitPane.remove(currentDailyInfoPane);
        upperSplitPane.add(newDailyInfoPane);

        int dividerLocation = upperSplitPane.getDividerLocation();
        upperSplitPane.setDividerLocation(dividerLocation);
        this.currentDailyInfoPane = newDailyInfoPane;

        refreshUI();
    }

}
