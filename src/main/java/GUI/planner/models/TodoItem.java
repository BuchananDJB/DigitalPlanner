package GUI.planner.models;

public class TodoItem {
    private boolean isDone;
    private String description;
    private TaskPriority priority;

    public TodoItem() {}

    public TodoItem(boolean isDone, String description, TaskPriority priority) {
        this.isDone = isDone;
        this.description = description;
        this.priority = priority;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
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
