package GUI.planner;

import GUI.Tools.GUITools;
import GUI.planner.components.calendar.CalendarPanel;
import GUI.planner.components.todolist.TodoList;
import Tools.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DigitalPlanner extends JFrame {

    private final CalendarPanel calendarPanel;
    private final TodoList generalTodoList;
    private final JTextArea generalNotesTextArea;

    public DigitalPlanner() {
        this.calendarPanel = new CalendarPanel();
        this.calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));

        this.generalTodoList = new TodoList();

        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, calendarPanel, generalTodoList);
        leftSplitPane.setDividerLocation(200);

        JTabbedPane dailyInfoTabbedPane = new JTabbedPane();
        dailyInfoTabbedPane.setBorder(BorderFactory.createTitledBorder("Daily Info"));

        JTextArea dailyNotesTextArea = new JTextArea();
        dailyNotesTextArea.setBorder(BorderFactory.createTitledBorder("Notes"));

        TodoList dailyTodoList = new TodoList();
        JSplitPane dailyInfoSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dailyTodoList, dailyNotesTextArea);
        dailyInfoSplitPane.setDividerLocation(350);
        dailyInfoTabbedPane.addTab("General", dailyInfoSplitPane);

        JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, dailyInfoTabbedPane);
        upperSplitPane.setDividerLocation(300);

        this.generalNotesTextArea = initializeGeneralNotesTextArea();

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplitPane, generalNotesTextArea);
        mainSplitPane.setDividerLocation(550);
        this.getContentPane().add(BorderLayout.CENTER, mainSplitPane);
    }

    private JTextArea initializeGeneralNotesTextArea() {
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
}
