package gui.planner;

import gui.planner.components.calendar.CalendarPanel;
import gui.planner.components.calendar.DateSelectionListener;
import gui.planner.components.dailyinfo.DailyInfo;
import gui.planner.components.notes.NotesTextArea;
import gui.planner.components.todolist.TodoList;
import tools.Constants;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DigitalPlanner extends JFrame implements DateSelectionListener {

    private final TodoList generalTodoList;
    private final NotesTextArea generalNotesTextArea;
    private DailyInfo currentDailyInfoPane;
    private CalendarPanel calendarPanel;
    private final Map<String, DailyInfo> dailyInfoMapByDate;

    private final JSplitPane upperSplitPane;

    public DigitalPlanner() {
        this.dailyInfoMapByDate = new HashMap<>();
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

        validate();
        repaint();
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

        revalidate();
        repaint();
    }

    private String getTodaysDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }

}
