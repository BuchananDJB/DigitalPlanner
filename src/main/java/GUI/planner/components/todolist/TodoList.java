package GUI.planner.components.todolist;

import GUI.planner.models.TodoItem;
import GUI.planner.models.TodoItemList;
import Tools.Constants;
import Tools.DataTools;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;
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

    public TodoList(String todoListFolderPath) {
        super();
        initializeTodoListTablesFromFiles(todoListFolderPath);
        initializeAllPanes();
    }

    public TodoList() {
        super();
        initializeNewTodoListTables();
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

    private void initializeTodoListTablesFromFiles(String todoListFolderPath) {
        TypeToken<List<TodoItem>> listTypeToken = new TypeToken<>() {};
        Type listType = listTypeToken.getType();

        String generalCompleteJson = DataTools.readFileAsString(todoListFolderPath + Constants.GENERAL_TODO_COMPLETE);
        List<TodoItem> generalComplete = DataTools.fromJson(generalCompleteJson, listType);
        TodoItemList generalCompleteList = new TodoItemList(generalComplete);
        this.generalCompleteTodoListTable = new TodoListTable(COMPLETE, generalCompleteList);

        String generalIncompleteJson = DataTools.readFileAsString(todoListFolderPath + Constants.GENERAL_TODO_INCOMPLETE);
        List<TodoItem> generalIncomplete = DataTools.fromJson(generalIncompleteJson, listType);
        TodoItemList generalIncompleteList = new TodoItemList(generalIncomplete);
        this.generalIncompleteTodoListTable = new TodoListTable(INCOMPLETE, generalIncompleteList);

        String socialCompleteJson = DataTools.readFileAsString(todoListFolderPath + Constants.SOCIAL_TODO_COMPLETE);
        List<TodoItem> socialComplete = DataTools.fromJson(socialCompleteJson, listType);
        TodoItemList socialCompleteList = new TodoItemList(socialComplete);
        this.socialCompleteTodoListTable = new TodoListTable(COMPLETE, socialCompleteList);

        String socialIncompleteJson = DataTools.readFileAsString(todoListFolderPath + Constants.SOCIAL_TODO_INCOMPLETE);
        List<TodoItem> socialIncomplete = DataTools.fromJson(socialIncompleteJson, listType);
        TodoItemList socialIncompleteList = new TodoItemList(socialIncomplete);
        this.socialIncompleteTodoListTable = new TodoListTable(INCOMPLETE, socialIncompleteList);
    }

    private void initializeNewTodoListTables() {
        this.generalCompleteTodoListTable = new TodoListTable(COMPLETE);
        this.generalIncompleteTodoListTable = new TodoListTable(INCOMPLETE);
        this.socialCompleteTodoListTable = new TodoListTable(COMPLETE);
        this.socialIncompleteTodoListTable = new TodoListTable(INCOMPLETE);
    }

    private JPanel createPanel(TodoListTable todoListTable) {
        JScrollPane scrollPane = new JScrollPane(todoListTable);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane);
        panel.setBorder(BorderFactory.createTitledBorder(todoListTable.getTitle()));

        return panel;
    }

    public void saveTodoList() {

    }
}
