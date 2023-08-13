package gui.planner.components.calendar;

import tools.Constants;
import tools.DataTools;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class CalendarPanel extends JPanel {

    private final Calendar calendar;
    private final JLabel monthYearLabel;
    private final JButton prevButton;
    private final JButton nextButton;

    private DefaultTableModel tableModel;
    private int currentMonth;
    private int currentYear;

    private final Calendar selectedDate;
    private final Set<DateSelectionListener> dateSelectionListeners;

    public CalendarPanel() {
        this(300, 200);
    }

    public CalendarPanel(int width, int height) {
        this.dateSelectionListeners = new HashSet<>();
        this.setSize(width, height);
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        this.calendar = new GregorianCalendar();

        monthYearLabel = new JLabel();
        monthYearLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthYearLabel.addMouseListener(setupMonthYearLabelListener());

        prevButton = createPreviousButton();
        nextButton = createNextButton();

        JPanel panel = initializeUpperPanel();
        JScrollPane scrollPane = initializeScrollPane();

        this.add(panel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        this.selectedDate = (Calendar) calendar.clone();

        this.updateMonth();
    }

    public void registerDateSelectionListener(DateSelectionListener listener) {
        dateSelectionListeners.add(listener);
    }

    private MouseListener setupMonthYearLabelListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2) {
                    goToToday();
                }
            }
        };
    }

    public void goToToday() {
        Calendar today = Calendar.getInstance();
        calendar.setTime(today.getTime());
        updateMonth();
        selectedDate.setTime(today.getTime());
        notifyDateSelectionListener();
    }

    private JButton createNextButton() {
        JButton nextButton = new JButton("->");
        addMonthButtonListener(nextButton, 1);
        return nextButton;
    }

    private JButton createPreviousButton() {
        JButton prevButton = new JButton("<-");
        addMonthButtonListener(prevButton, -1);
        return prevButton;
    }

    private void addMonthButtonListener(JButton button, int amount) {
        button.addActionListener(listener -> {
            calendar.add(Calendar.MONTH, amount);
            selectedDate.setTime(calendar.getTime());
            updateMonth();
        });
    }

    private JPanel initializeUpperPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(prevButton, BorderLayout.WEST);
        panel.add(monthYearLabel, BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.EAST);
        return panel;
    }

    private JScrollPane initializeScrollPane() {
        String[] columns = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value instanceof Integer) {
                    Calendar cellDate = (Calendar) selectedDate.clone();
                    cellDate.set(Calendar.DAY_OF_MONTH, (int) value);

                    if (hasDataForDate(cellDate)) {
                        Font originalFont = component.getFont();
                        Font boldFont = originalFont.deriveFont(originalFont.getStyle() | Font.BOLD);
                        component.setFont(boldFont);
                    }
                }

                if (isSelected) {
                    if (row == table.getSelectedRow() && column == table.getSelectedColumn()) {
                        component.setBackground(table.getSelectionBackground());
                        component.setForeground(table.getSelectionForeground());
                    } else {
                        component.setBackground(table.getBackground());
                        component.setForeground(table.getForeground());
                    }
                } else {
                    component.setBackground(table.getBackground());
                    component.setForeground(table.getForeground());
                }

                ((DefaultTableCellRenderer) component).setHorizontalAlignment(SwingConstants.CENTER);

                return component;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (row >= 0 && column >= 0) {
                    int day = (int) table.getValueAt(row, column);
                    selectedDate.set(Calendar.DAY_OF_MONTH, day);
                    selectedDate.set(Calendar.MONTH, currentMonth);
                    selectedDate.set(Calendar.YEAR, currentYear);
                    notifyDateSelectionListener();
                }
            }
        });

        return new JScrollPane(table);
    }

    private void updateMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        int year = calendar.get(Calendar.YEAR);
        monthYearLabel.setText(month + " " + year);

        int startDay = calendar.get(Calendar.DAY_OF_WEEK);
        int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

        tableModel.setRowCount(0);
        tableModel.setRowCount(weeks);

        int i = startDay - 1;
        for (int day = 1; day <= numberOfDays; ++day) {
            tableModel.setValueAt(day, i / 7, i % 7);
            ++i;
        }

        highlightCurrentDate();
        boldDatesInMonth();
    }

    private void highlightCurrentDate() {
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        if (currentYear == Calendar.getInstance().get(Calendar.YEAR) && currentMonth == Calendar.getInstance().get(Calendar.MONTH)) {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Object value = tableModel.getValueAt(row, col);
                    if (value instanceof Integer && (int) value == today) {
                        JTable table = (JTable) ((JScrollPane) getComponent(1)).getViewport().getView();
                        table.changeSelection(row, col, false, false);
                        return;
                    }
                }
            }
        }
    }

    private void boldDatesInMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= numberOfDays; ++day) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
            styleDate((Calendar) calendar.clone(), hasDataForDate(calendar)); // Bold
        }
    }

    private boolean hasDataForDate(Calendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = dateFormat.format(date.getTime());

        String dateDirectory = Constants.DAILY_INFO_DIRECTORY + dateString;
        List<String> dateTabNames = DataTools.listSubdirectories(dateDirectory);
        for (String tabName : dateTabNames) {
            if (!DataTools.directoryAndFilesAreEmpty(dateDirectory + "/" + tabName)) {
                return true;
            }
        }
        return false;
    }

    private void styleDate(Calendar date, boolean bold) {
        int row = date.get(Calendar.WEEK_OF_MONTH) - 1;
        int col = date.get(Calendar.DAY_OF_WEEK) - 1;

        JTable table = (JTable) ((JScrollPane) getComponent(1)).getViewport().getView();
        Component cellRenderer = table.getDefaultRenderer(Object.class).getTableCellRendererComponent(table, null, false, false, row, col);

        if (cellRenderer instanceof JLabel label) {
            Font originalFont = label.getFont();
            int style = originalFont.getStyle();
            style = bold ? (style | Font.BOLD) : (style & ~Font.BOLD);
            Font newFont = originalFont.deriveFont(style);
            label.setFont(newFont);
        }
    }

    private void notifyDateSelectionListener() {
        for (DateSelectionListener listener : dateSelectionListeners) {
            listener.onDateSelected(selectedDate);
        }
    }
}