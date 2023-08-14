package gui.planner.components.dailyinfo;

import gui.planner.components.notes.NotesScrollPane;
import gui.planner.components.todolist.TodoList;

import javax.swing.*;

public class DailyInfoSplitPane extends JSplitPane {
    private final NotesScrollPane dailyNotesScrollPane;
    private final TodoList dailyTodoList;
    private final String directoryPath;

    public DailyInfoSplitPane(String directoryPath) {
        super();
        this.directoryPath = directoryPath;
        this.dailyNotesScrollPane = new NotesScrollPane(directoryPath);
        dailyNotesScrollPane.setBorder(BorderFactory.createTitledBorder("Notes"));

        this.dailyTodoList = new TodoList(directoryPath);

        this.setOrientation(HORIZONTAL_SPLIT);
        this.setLeftComponent(dailyTodoList);
        this.setRightComponent(dailyNotesScrollPane);

        this.setDividerLocation(350);
    }

    public NotesScrollPane getDailyNotesScrollPane() {
        return dailyNotesScrollPane;
    }

    public TodoList getDailyTodoList() {
        return dailyTodoList;
    }
}
