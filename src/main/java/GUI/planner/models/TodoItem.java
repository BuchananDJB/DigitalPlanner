package GUI.planner.models;

public class TodoItem {
    private boolean isComplete;
    private String description;
    private TaskPriority priority;

    public TodoItem() {}

    public TodoItem(boolean isComplete, String description, TaskPriority priority) {
        this.isComplete = isComplete;
        this.description = description;
        this.priority = priority;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }
}
