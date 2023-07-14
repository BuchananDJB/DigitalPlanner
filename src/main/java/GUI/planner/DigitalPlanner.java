package GUI.planner;

import GUI.planner.components.calendar.CalendarPanel;
import GUI.planner.components.calendar.DateSelectionListener;
import GUI.planner.components.dailyinfo.DailyInfo;
import GUI.planner.components.todolist.TodoList;
import Tools.Constants;
import Tools.DataTools;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static Tools.DataTools.readFileAsString;

public class DigitalPlanner extends JFrame implements DateSelectionListener {

    private final TodoList generalTodoList;
    private final JTextArea generalNotesTextArea;
    private DailyInfo dailyInfoPane;
    private CalendarPanel calendarPanel;

    private JSplitPane upperSplitPane;

    public DigitalPlanner() {
        initializeCalendarPanel();
        this.generalTodoList = new TodoList();
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, calendarPanel, generalTodoList);
        leftSplitPane.setDividerLocation(200);

        this.dailyInfoPane = new DailyInfo();
        this.upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, dailyInfoPane);
        upperSplitPane.setDividerLocation(300);

        this.generalNotesTextArea = createGeneralNotesTextArea();
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplitPane, generalNotesTextArea);
        mainSplitPane.setDividerLocation(550);
        this.getContentPane().add(BorderLayout.CENTER, mainSplitPane);
    }

    private void initializeCalendarPanel() {
        this.calendarPanel = new CalendarPanel();
        this.calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));
        this.calendarPanel.registerDateSelectionListener(this);
    }

    private JTextArea createGeneralNotesTextArea() {
        String notesFileContents = readFileAsString(Constants.GENERAL_NOTES_PATH);
        JTextArea notesTextArea = new JTextArea(notesFileContents);
        notesTextArea.setBorder(BorderFactory.createTitledBorder("General Notes"));
        return notesTextArea;
    }

    @Override
    public void onDateSelected(Calendar selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = dateFormat.format(selectedDate.getTime());

        int dividerLocation = upperSplitPane.getDividerLocation();
        DailyInfo newDailyInfoPane = new DailyInfo(dateString);
        // TODO: read in existing data into newDailyInfoPane
        //  make sure to store references to these to load them in again
        //  if the same date is clicked again
        upperSplitPane.remove(dailyInfoPane);
        upperSplitPane.add(newDailyInfoPane);
        upperSplitPane.setDividerLocation(dividerLocation);
        this.dailyInfoPane = newDailyInfoPane;

        revalidate();
        repaint();

        /*int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH) + 1;
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);*/
    }

    public void saveAllData() {
        DataTools.writeStringToFile(generalNotesTextArea.getText(), Constants.GENERAL_NOTES_PATH);
    }
}
