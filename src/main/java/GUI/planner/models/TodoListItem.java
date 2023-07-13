package GUI.planner.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TodoListItem {
    private boolean isComplete;
    private String description;
    private TaskPriority priority;

    public TodoListItem() {}

    public TodoListItem(boolean isComplete, String description, TaskPriority priority) {
        this.isComplete = isComplete;
        this.description = description;
        this.priority = priority;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static TodoListItem fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, TodoListItem.class);
    }
}
