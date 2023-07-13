package GUI.planner;

import GUI.Tools.GUITools;
import GUI.planner.components.CalendarPanel;
import Tools.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DigitalPlanner extends JFrame {
/*
Calendar
	click on a date, populate right side with relevant info
		JTabbedPane (be able to delete tab, or close and reopen)
			date-specific to-do list (both complete and incomplete on the same list)
			notes
	if a date has a to-do list item, indicate that on calendar (probably bold text)

General to-do list
    JCheckBox: Toggle between completed and uncompleted (be able to move specific tasks between complete/incomplete)
    JTabbedPane: Two tabs (One for general, one for social media)
        JTable (3 columns: checkbox, description, priority)
        Not associated with a specific date

General Notes
	JTextArea
	At the bottom

Development notes
	Data organization:
		File for general notes
		Folder for general to-do
			File to incomplete to-do
			File for complete to-do
		Folder for social media to-do
			File to incomplete to-do
			File for complete to-do
		Folder for each date
			Folder for each tab
				file for to-do (complete and incomplete in same file)
				file for notes
*/
    private final CalendarPanel calendarPanel;
    private final JTextArea generalNotesTextArea;

    public DigitalPlanner() {
        this.calendarPanel = new CalendarPanel();
        this.calendarPanel.setBorder(BorderFactory.createTitledBorder("Calendar"));

        JTable generalTodoListTablePlaceholder = new JTable(4, 3);
        JTable socialTodoListTablePlaceholder = new JTable(4, 3);

        JTabbedPane generalTodoList = new JTabbedPane();
        generalTodoList.setBorder(BorderFactory.createTitledBorder("Todo List"));
        generalTodoList.addTab("General", generalTodoListTablePlaceholder);
        generalTodoList.addTab("Social", socialTodoListTablePlaceholder);

        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, calendarPanel, generalTodoList);
        leftSplitPane.setDividerLocation(200);

        JTabbedPane dailyInfoTabbedPane = new JTabbedPane();
        dailyInfoTabbedPane.setBorder(BorderFactory.createTitledBorder("Daily Info"));

        JTextArea dailyNotesTextArea = new JTextArea();
        dailyNotesTextArea.setBorder(BorderFactory.createTitledBorder("Notes"));

        JTable dailyTodoListTablePlaceholder = new JTable(4, 3);
        dailyTodoListTablePlaceholder.setBorder(BorderFactory.createTitledBorder("Todo List"));

        JSplitPane dailyInfoSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dailyTodoListTablePlaceholder, dailyNotesTextArea);
        dailyInfoSplitPane.setDividerLocation(275);
        dailyInfoTabbedPane.addTab("General", dailyInfoSplitPane);
        JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplitPane, dailyInfoTabbedPane);
        upperSplitPane.setDividerLocation(275);

        this.generalNotesTextArea = initializeGeneralNotesTextArea();

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplitPane, generalNotesTextArea);
        mainSplitPane.setDividerLocation(500);
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
