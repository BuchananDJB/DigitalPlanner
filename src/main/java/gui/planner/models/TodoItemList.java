package gui.planner.models;

import java.util.List;

public class TodoItemList {
    private List<TodoItem> todoItems;

    public TodoItemList(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

    public List<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void setTodoItems(List<TodoItem> todoItems) {
        this.todoItems = todoItems;
    }

}