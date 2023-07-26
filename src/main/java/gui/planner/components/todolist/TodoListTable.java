package gui.planner.components.todolist;

import gui.planner.models.TaskPriority;
import gui.planner.models.TodoItem;
import gui.planner.models.TodoItemList;
import tools.DataTools;
import tools.savemanager.SaveItem;
import tools.savemanager.SaveManager;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TodoListTable extends JTable implements SaveItem, TaskStatusListener {

    private final String[] columnNames = {"Done", "Description", "Priority"};
    private final String title;
    private final String directoryPath;
    private final boolean isCompleteTable;

    private boolean initialSetupComplete = false;
    private boolean tableChanged = false;
    private TaskStatusListener taskStatusListener;

    public TodoListTable(String title, boolean isCompleteTable, String directoryPath, TodoItemList todoItemList) {
        this(title, isCompleteTable, directoryPath);
        populateTable(todoItemList);
        if (getRowCount() == 0) {
            addEmptyRow();
        }
    }

    public TodoListTable(String title, boolean isCompleteTable, String directoryPath) {
        super();

        this.title = title;
        this.directoryPath = directoryPath;
        this.isCompleteTable = isCompleteTable;
        setModel(createTableModel());

        JComboBox<TaskPriority> priorityComboBox = new JComboBox<>(TaskPriority.values());
        this.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(priorityComboBox));

        adjustColumnWidths();
        enableColumnSorting();

        TableColumn descriptionColumn = getColumnModel().getColumn(1);
        descriptionColumn.setCellRenderer(new DescriptionCellRenderer());

        setupMouseListener();

        getModel().addTableModelListener(e -> {
            if (!initialSetupComplete) {
                return;
            }

            if (!tableChanged) {
                handleTableChange();
                tableChanged = true;
            }
        });

    }

    private void handleTableChange() {
        SaveManager saveManager = new SaveManager();
        saveManager.registerSaveItem(this);
    }

    private void populateTable(TodoItemList todoItemList) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setRowCount(0);

        if (todoItemList != null) {
            for (TodoItem todoItem : todoItemList.getTodoItems()) {
                addRowWithData(todoItem);
            }
        }

        initialSetupComplete = true;
    }

    private void addRowWithData(TodoItem todoItem) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        Object[] rowData = new Object[model.getColumnCount()];
        rowData[0] = todoItem.isDone();
        rowData[1] = todoItem.getDescription();
        rowData[2] = todoItem.getPriority();
        model.addRow(rowData);
    }

    private static class DescriptionCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 1 && renderer instanceof JComponent component) {
                String description = (String) value;
                component.setToolTipText(description);
            }

            return renderer;
        }
    }
    private DefaultTableModel createTableModel() {
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                } else if (columnIndex == 1) {
                    return String.class;
                }
                return TaskPriority.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return isRowEditable(row, column);
            }
        };

        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

        return tableModel;
    }

    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // TODO: have right clicks highlight the clicked row, then have delete item functionality
                    //  delete all selected items, not just the clicked item
                    createRightClickMenu(e).show(TodoListTable.this, e.getX(), e.getY());
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickedRow = rowAtPoint(e.getPoint());
                int clickedColumn = columnAtPoint(e.getPoint());

                // Check if the click is on the "Done" column (column 0)
                if (clickedRow >= 0 && clickedColumn == 0) {
                    // Get the row data from the clicked row
                    boolean isDone = (boolean) getValueAt(clickedRow, 0);
                    String description = (String) getValueAt(clickedRow, 1);
                    TaskPriority taskPriority = (TaskPriority) getValueAt(clickedRow, 2);
                    TodoItem todoItem = new TodoItem(isDone, description, taskPriority);

                    // TODO: Don't move empty rows to the other table
                    if (taskStatusListener != null) {
                        taskStatusListener.toggleTaskStatus(todoItem);
                        ((DefaultTableModel) getModel()).removeRow(clickedRow);

                        // Ensure the incomplete table always has at least one row
                        if (!isCompleteTable && getRowCount() == 0) {
                            addEmptyRow();
                        }
                    }
                }
            }
        });
    }

    private JPopupMenu createRightClickMenu(MouseEvent event) {
        JPopupMenu popupMenu = new JPopupMenu();
        int clickedRow = rowAtPoint(event.getPoint());

        JMenuItem addTodoItem = new JMenuItem("Add Todo Item");
        addTodoItem.addActionListener(e -> addEmptyRow());
        popupMenu.add(addTodoItem);

        JMenuItem deleteTodoItem = new JMenuItem("Delete Todo Item");
        deleteTodoItem.addActionListener(e -> {
            if (clickedRow != -1) {
                ((DefaultTableModel) getModel()).removeRow(clickedRow);
                if (!isCompleteTable && getRowCount() == 0) {
                    addEmptyRow();
                }
            }
        });
        popupMenu.add(deleteTodoItem);

        if (clickedRow != -1 && isRowEditable(clickedRow, 0)) {
            Object value = getValueAt(clickedRow, 0);
            if (value instanceof Boolean isDone) {
                JMenuItem markItem = isDone
                        ? new JMenuItem("Mark Item as Not Done")
                        : new JMenuItem("Mark Item as Done");
                markItem.addActionListener(e -> setValueAt(!isDone, clickedRow, 0));
                popupMenu.add(markItem);
            }

        }

        return popupMenu;
    }

    private void adjustColumnWidths() {
        TableColumnModel columnModel = getColumnModel();

        TableColumn doneColumn = columnModel.getColumn(0);
        doneColumn.setPreferredWidth(70);

        TableColumn descriptionColumn = columnModel.getColumn(1);
        descriptionColumn.setPreferredWidth(300);

        TableColumn priorityColumn = columnModel.getColumn(2);
        priorityColumn.setPreferredWidth(70);
    }

    private void enableColumnSorting() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) getModel());
        setRowSorter(sorter);
    }

    private void addEmptyRow() {
        DefaultTableModel model = (DefaultTableModel) getModel();
        Object[] rowData = new Object[model.getColumnCount()];
        rowData[0] = false;
        model.addRow(rowData);
    }

    private boolean isRowEditable(int row, int column) {
        DefaultTableModel model = (DefaultTableModel) getModel();

        // Enable editing for "Done" column if Description column is filled
        if (column == 0) {
            Object description = model.getValueAt(row, 1);
            return description != null && !description.toString().isEmpty();
        }

        return true;
    }

    public String getTitle() {
        return title;
    }

    public TodoItemList getTodoItemList() {
        List<TodoItem> todoItemList = new ArrayList<>();

        DefaultTableModel model = (DefaultTableModel) getModel();
        int rowCount = model.getRowCount();
        for (int row = 0; row < rowCount; row++) {
            boolean isDone = (boolean) model.getValueAt(row, 0);
            String description = Optional.ofNullable((String) model.getValueAt(row, 1)).orElse("");
            TaskPriority priority = Optional.ofNullable((TaskPriority) model.getValueAt(row, 2)).orElse(TaskPriority.NONE);
            TodoItem todoItem = new TodoItem(isDone, description, priority);
            todoItemList.add(todoItem);
        }

        return new TodoItemList(todoItemList);
    }

    @Override
    public void toggleTaskStatus(TodoItem todoItem) {
        addRowWithData(todoItem);
    }

    public void registerTaskStatusListener(TaskStatusListener taskStatusListener) {
        this.taskStatusListener = taskStatusListener;
    }

    @Override
    public void saveData() {
        TodoItemList todoItemList = getTodoItemList();
        String todoJson = DataTools.toJson(todoItemList);
        DataTools.writeStringToFile(todoJson, directoryPath + "/" + title + ".json");
    }
}
