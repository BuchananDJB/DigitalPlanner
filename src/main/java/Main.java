import GUI.planner.DigitalPlanner;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        initializeGUI();
    }

    private static void initializeGUI() {
        FlatDarkLaf.setup();
        initializeJFrame();
    }

    private static void initializeJFrame() {
        DigitalPlanner digitalPlanner = new DigitalPlanner();
        digitalPlanner.setTitle("Digital Planner");
        digitalPlanner.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        digitalPlanner.setSize(1280, 720);
        digitalPlanner.setLocationRelativeTo(null);
        digitalPlanner.setVisible(true);

        digitalPlanner.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                digitalPlanner.saveAllData();
                digitalPlanner.dispose();
            }
        });
    }
}
