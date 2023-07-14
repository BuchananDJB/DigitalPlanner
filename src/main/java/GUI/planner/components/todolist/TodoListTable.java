package GUI.planner.components.todolist;

import GUI.planner.models.TaskPriority;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class TodoListTable extends JTable {

    private final String title;
    private final String[] columnNames = {"Done", "Description", "Priority"};

    public TodoListTable(String title) {
        super();

        this.title = title;
        setModel(createTableModel());

        JComboBox<TaskPriority> priorityComboBox = new JComboBox<>(TaskPriority.values());
        this.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(priorityComboBox));

        adjustColumnWidths();
        enableColumnSorting();
        TableColumn descriptionColumn = getColumnModel().getColumn(1);
        descriptionColumn.setCellRenderer(new DescriptionCellRenderer());

        addEmptyRow();
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
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
        model.addRow(new Object[model.getColumnCount()]);
    }

    private boolean isRowEditable(int row, int column) {
        DefaultTableModel model = (DefaultTableModel) getModel();

        // Check if the previous row is fully filled
        if (row > 0) {
            int columnCount = model.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                Object value = model.getValueAt(row - 1, i);
                if (value == null || value.toString().isEmpty()) {
                    return false;
                }
            }
        }

        // Enable editing for "Done" column if Description and Priority columns are filled
        if (column == 0) {
            Object description = model.getValueAt(row, 1);
            Object priority = model.getValueAt(row, 2);
            return description != null && !description.toString().isEmpty() &&
                    priority != null && !priority.toString().isEmpty();
        }

        return true;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        super.setValueAt(aValue, row, column);

        // Add empty row and enable editing for "Done" column
        if (column == 2 && row == getRowCount() - 1) {
            addEmptyRow();
            TableColumn doneColumn = getColumnModel().getColumn(0);
            doneColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        }
    }

    public String getTitle() {
        return title;
    }

    // TODO: Create method to retrieve all data from the table as a TodoItemList (List<TodoItem>)
}
