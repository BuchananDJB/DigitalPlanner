package GUI;

import GUI.components.CalendarPanel;

import javax.swing.*;
import java.awt.*;

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

        JTextArea generalNotesTextArea = new JTextArea();
        generalNotesTextArea.setBorder(BorderFactory.createTitledBorder("General Notes"));

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplitPane, generalNotesTextArea);
        mainSplitPane.setDividerLocation(500);
        this.getContentPane().add(BorderLayout.CENTER, mainSplitPane);
    }
}
