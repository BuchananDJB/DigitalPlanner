package gui.planner.components.todolist;

import gui.planner.models.TodoItem;

public interface TaskStatusListener {
    void toggleTaskStatus(TodoItem todoItem);
}
