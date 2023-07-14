package GUI.planner.components.dailyinfo;

import GUI.planner.components.todolist.TodoList;

import javax.swing.*;

public class DailyInfoSplitPane extends JSplitPane {
    private final JTextArea dailyNotesTextArea;
    private final TodoList dailyTodoList;

    public DailyInfoSplitPane() {
        super();
        this.dailyNotesTextArea = new JTextArea();
        dailyNotesTextArea.setBorder(BorderFactory.createTitledBorder("Notes"));

        this.dailyTodoList = new TodoList();

        this.setOrientation(HORIZONTAL_SPLIT);
        this.setLeftComponent(dailyTodoList);
        this.setRightComponent(dailyNotesTextArea);

        this.setDividerLocation(350);
    }
}
