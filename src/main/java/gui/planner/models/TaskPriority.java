package gui.planner.models;

public enum TaskPriority {
    NONE(" "),
    LOW("!"),
    MEDIUM("!!"),
    HIGH("!!!");

    private final String priorityString;

    TaskPriority(String priorityString) {
        this.priorityString = priorityString;
    }

    @Override
    public String toString() {
        return priorityString;
    }
}
