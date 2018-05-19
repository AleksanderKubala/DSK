package Gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private int height = 600;
    private int width = 800;

    private int minNodes = 3;
    private int maxNodes = 7;

    private StructureSettingsPanel structureSettingsPanel;
    private JPanel messagePanel;
    private JPanel buttonPanel;
    private JPanel structurePanel;
    private JPanel lGraphPanel;

    private Container pane;

    public MainWindow() {
        pane = getContentPane();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pane.setLayout(new FlowLayout());
        setPreferredSize(new Dimension(800, 600));
        structureSettingsPanel = new StructureSettingsPanel();
        add(structureSettingsPanel);
        setVisible(true);
    }

    private void prepareMessagePanel() {

    }

    private void prepareButtonPanel() {

    }

    private void prepareStructurePanel() {

    }

    private void prepareLGraphPanel() {

    }
}
