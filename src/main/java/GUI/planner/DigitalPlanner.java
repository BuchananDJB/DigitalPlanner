package GUI.planner;

import GUI.Tools.GUITools;
import GUI.planner.components.calendar.CalendarPanel;
import GUI.planner.components.calendar.DateSelectionListener;
import GUI.planner.components.dailyinfo.DailyInfo;
import GUI.planner.components.todolist.TodoList;
import Tools.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DigitalPlanner extends JFrame implements DateSelectionListener {

    private final TodoList generalTodoList;
    private final JTextArea generalNotesTextArea;
    private CalendarPanel calendarPanel;

    public DigitalPlanner() {
        initializeCalendarPanel();

        this.generalTodoList = new TodoList();

        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, calendarPanel, generalTodoList);
        leftSplitPane.setDividerLocation(200);

        DailyInfo dailyInfoPane = new DailyInfo();

        JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, dailyInfoPane);
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

    private String readFileAsString(String filePath) {
        String fileContents = "";
        try {
            fileContents = Files.readString(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            GUITools.displayDialog(e.getMessage());
        }
        return fileContents;
    }

    @Override
    public void onDateSelected(Calendar selectedDate) {
        // TODO date-selection: change the view on the right side with a new JTabbedPane
        //  associated with data correlating with the selectedDate
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = dateFormat.format(selectedDate.getTime());

        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH) + 1;
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        GUITools.displayDialog(dateString);
    }
}
