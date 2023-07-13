import GUI.DigitalPlanner;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        initializeGUI();
    }

    private static void initializeGUI() {
        FlatDarkLaf.setup();
        initializeJFrame();
    }

    private static void initializeJFrame() {
        JFrame plannerFrame = new DigitalPlanner();
        plannerFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        plannerFrame.setSize(1280, 720);
        plannerFrame.setLocationRelativeTo(null);
        plannerFrame.setVisible(true);
    }
}
