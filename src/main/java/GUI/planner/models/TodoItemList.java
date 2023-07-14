package GUI.planner.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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


    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static TodoItemList fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, TodoItemList.class);
    }
}