package GUI.planner.components.dailyinfo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DailyInfoTabbedPane extends JTabbedPane {

    private final List<DailyInfoSplitPane> dailyInfoSplitPanes;

    public DailyInfoTabbedPane() {
        this.dailyInfoSplitPanes = new ArrayList<>();
        addNewTab("General");
    }

    public void addNewTab(String title) {
        DailyInfoSplitPane dailyInfoSplitPane = new DailyInfoSplitPane();
        dailyInfoSplitPanes.add(dailyInfoSplitPane);
        this.addTab(title, dailyInfoSplitPane);
    }

}
