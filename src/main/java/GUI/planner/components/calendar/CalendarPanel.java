package GUI.planner.components.calendar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarPanel extends JPanel {

    private final Calendar calendar;
    private final JLabel monthYearLabel;
    private final JButton prevButton;
    private final JButton nextButton;

    private DefaultTableModel tableModel;
    private int calendarMonthOffset;

    public CalendarPanel() {
        this(300, 200);
    }

    public CalendarPanel(int width, int height) {
        this.setSize(width, height);
        this.setLayout(new BorderLayout());
        this.setVisible(true);

        this.calendar = new GregorianCalendar();
        this.calendarMonthOffset = 0;

        monthYearLabel = new JLabel();
        monthYearLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthYearLabel.addMouseListener(setupMonthYearLabelListener());

        prevButton = createPreviousButton();
        nextButton = createNextButton();

        JPanel panel = initializeUpperPanel();
        JScrollPane scrollPane = initializeScrollPane();

        this.add(panel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        this.updateMonth();
    }

    private MouseListener setupMonthYearLabelListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2) {
                    goToCurrentMonth();
                }
            }
        };
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
            calendarMonthOffset += amount;
            calendar.add(Calendar.MONTH, amount);
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
        String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        return new JScrollPane(table);
    }

    public void goToCurrentMonth() {
        calendar.add(Calendar.MONTH, -calendarMonthOffset);
        updateMonth();
        calendarMonthOffset = 0;
    }

    private void updateMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        int year = calendar.get(Calendar.YEAR);
        monthYearLabel.setText(month + " " + year);

        int startDay = calendar.get(Calendar.DAY_OF_WEEK);
        int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);

        tableModel.setRowCount(0);
        tableModel.setRowCount(weeks);

        int i = startDay - 1;
        for(int day = 1; day <= numberOfDays; ++day) {
            tableModel.setValueAt(day, i / 7 , i % 7 );
            ++i;
        }

    }

}