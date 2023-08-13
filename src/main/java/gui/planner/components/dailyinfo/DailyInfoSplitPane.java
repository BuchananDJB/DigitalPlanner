package gui.planner.components.dailyinfo;

import gui.planner.components.notes.NotesTextArea;
import gui.planner.components.todolist.TodoList;

import javax.swing.*;

public class DailyInfoSplitPane extends JSplitPane {
    private final NotesTextArea dailyNotesTextArea;
    private final TodoList dailyTodoList;
    private final String directoryPath;

    public DailyInfoSplitPane(String directoryPath) {
        super();
        this.directoryPath = directoryPath;
        this.dailyNotesTextArea = new NotesTextArea(directoryPath);
        dailyNotesTextArea.setBorder(BorderFactory.createTitledBorder("Notes"));

        this.dailyTodoList = new TodoList(directoryPath);

        this.setOrientation(HORIZONTAL_SPLIT);
        this.setLeftComponent(dailyTodoList);
        this.setRightComponent(dailyNotesTextArea);

        this.setDividerLocation(350);
    }

    public NotesTextArea getDailyNotesTextArea() {
        return dailyNotesTextArea;
    }

    public TodoList getDailyTodoList() {
        return dailyTodoList;
    }
}
