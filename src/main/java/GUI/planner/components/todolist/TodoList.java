package GUI.planner.components.todolist;

import javax.swing.*;
import java.awt.*;

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

    public TodoList() {
        super();

        initializeTodoListTables();
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

    private void initializeTodoListTables() {
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
}
