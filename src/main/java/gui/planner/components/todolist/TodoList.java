package gui.planner.components.todolist;

import gui.planner.models.TodoItemList;
import tools.Constants;
import tools.DataTools;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TodoList extends JTabbedPane {

    private static final String GENERAL = "General";
    private static final String SOCIAL = "Social";
    private static final String COMPLETE = "Complete";
    private static final String INCOMPLETE = "Incomplete";
    private static final String TODO_LIST = "Todo List";

    private TodoListTable generalCompleteTodoListTable;
    private TodoListTable generalIncompleteTodoListTable;
    private TodoListTable socialCompleteTodoListTable;
    private TodoListTable socialIncompleteTodoListTable;

    private final String todoListDirectory;

    public TodoList(String todoListDirectory) {
        super();
        this.todoListDirectory = todoListDirectory;
        initializeTodoListTablesFromFiles();
        initializeAllPanes();
    }

    private void initializeAllPanes() {
        this.setBorder(BorderFactory.createTitledBorder(TODO_LIST));

        JPanel generalIncompletePanel = createPanel(generalIncompleteTodoListTable);
        JPanel generalCompletePanel = createPanel(generalCompleteTodoListTable);
        JSplitPane generalPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, generalIncompletePanel, generalCompletePanel);
        generalPane.setDividerLocation(150);

        JPanel socialIncompletePanel = createPanel(socialIncompleteTodoListTable);
        JPanel socialCompletePanel = createPanel(socialCompleteTodoListTable);
        JSplitPane socialPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, socialIncompletePanel, socialCompletePanel);
        socialPane.setDividerLocation(150);

        this.addTab(GENERAL, generalPane);
        this.addTab(SOCIAL, socialPane);
    }

    private void initializeTodoListTablesFromFiles() {
        TodoItemList generalCompleteList = createTodoItemList(todoListDirectory, Constants.GENERAL_TODO_COMPLETE);
        this.generalCompleteTodoListTable =
                new TodoListTable(GENERAL + "-" + COMPLETE, true, todoListDirectory, generalCompleteList);

        TodoItemList generalIncompleteList = createTodoItemList(todoListDirectory, Constants.GENERAL_TODO_INCOMPLETE);
        this.generalIncompleteTodoListTable =
                new TodoListTable(GENERAL + "-" + INCOMPLETE, false, todoListDirectory, generalIncompleteList);

        this.generalIncompleteTodoListTable.registerTaskStatusListener(this.generalCompleteTodoListTable);
        this.generalCompleteTodoListTable.registerTaskStatusListener(this.generalIncompleteTodoListTable);

        TodoItemList socialCompleteList = createTodoItemList(todoListDirectory, Constants.SOCIAL_TODO_COMPLETE);
        this.socialCompleteTodoListTable =
                new TodoListTable(SOCIAL + "-" + COMPLETE, true, todoListDirectory, socialCompleteList);

        TodoItemList socialIncompleteList = createTodoItemList(todoListDirectory, Constants.SOCIAL_TODO_INCOMPLETE);
        this.socialIncompleteTodoListTable =
                new TodoListTable(SOCIAL + "-" + INCOMPLETE, false, todoListDirectory, socialIncompleteList);

        this.socialIncompleteTodoListTable.registerTaskStatusListener(this.socialCompleteTodoListTable);
        this.socialCompleteTodoListTable.registerTaskStatusListener(this.socialIncompleteTodoListTable);
    }

    private TodoItemList createTodoItemList(String todoListFolderPath, String fileName) {
        String todoJson = DataTools.readFileAsString(todoListFolderPath + "/" + fileName);
        return DataTools.fromJson(todoJson, TodoItemList.class);
    }

    private JPanel createPanel(TodoListTable todoListTable) {
        JScrollPane scrollPane = new JScrollPane(todoListTable);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane);
        panel.setBorder(BorderFactory.createTitledBorder(todoListTable.getTitle()));

        return panel;
    }

    public List<TodoListTable> getAllTodoListTables() {
        return new ArrayList<>(List.of(
                generalIncompleteTodoListTable,
                generalCompleteTodoListTable,
                socialIncompleteTodoListTable,
                socialCompleteTodoListTable));
    }

}
