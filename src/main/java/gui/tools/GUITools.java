package gui.tools;

import tools.DataTools;

import javax.swing.*;

public class GUITools {
    public static void displayDialog(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message);
    }

    public static String prettyPrintDate(String date) {
        if (DataTools.isEmptyString(date)) {
            return "00 Month 0000";
        }

        int[] values = DataTools.stream(date.split("/"))
                .map(Object::toString)
                .mapToInt(Integer::parseInt)
                .toArray();

        String month = "";
        switch (values[1]) {
            case 1 -> month = "January";
            case 2 -> month = "February";
            case 3 -> month = "March";
            case 4 -> month = "April";
            case 5 -> month = "May";
            case 6 -> month = "June";
            case 7 -> month = "July";
            case 8 -> month = "August";
            case 9 -> month = "September";
            case 10 -> month = "October";
            case 11 -> month = "November";
            case 12 -> month = "December";
            default -> {}
        }

        return String.format("%d %s %d", values[2], month, values[0]);
    }
}
