package Gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private int height = 600;
    private int width = 800;

    private int minNodes = 3;
    private int maxNodes = 7;

    private StructureSettingsPanel structureSettingsPanel;
    private OptionsPanel optionsPanel;
    private JPanel operationalPanel;
    private JPanel graphPanel;
    private JPanel structurePanel;
    private JPanel lGraphPanel;

    private Container pane;

    public MainWindow() {
        pane = getContentPane();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pane.setLayout(new GridLayout(2, 2, 5, 5));
        setPreferredSize(new Dimension(800, 600));

        operationalPanel = new JPanel();
        operationalPanel.setLayout(new BoxLayout(operationalPanel, BoxLayout.X_AXIS));

        structureSettingsPanel = new StructureSettingsPanel();
        operationalPanel.add(structureSettingsPanel);

        optionsPanel = new OptionsPanel(structureSettingsPanel);
        operationalPanel.add(optionsPanel);

        operationalPanel.setVisible(true);
        add(operationalPanel);

        setVisible(true);
    }


    private void prepareStructurePanel() {

    }

    private void prepareLGraphPanel() {

    }
}
