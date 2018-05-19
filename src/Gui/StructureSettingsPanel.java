package Gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StructureSettingsPanel extends JPanel{

    private Integer minStructureDegree = 3;
    private Integer maxstructureDegree = 9;

    private JPanel structureDetailsPanel;
    private JPanel matrixDetailsPanel;

    private JComboBox<Integer> structureDegree;
    private JComboBox<Integer> diagnosisParameter;

    private List<List<JCheckBox>> structureAdjacencyMatrix;

    public StructureSettingsPanel() {
        super();
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        structureDetailsPanel = new JPanel();
        JLabel structureDegreeLabel = new JLabel("Structure degree: ");
        structureDegreeLabel.setVisible(true);
        structureDetailsPanel.add(structureDegreeLabel);

        structureDegree = new JComboBox<>();
        for(int i = minStructureDegree; i <= maxstructureDegree; i++) {
            structureDegree.addItem(i);
        }
        structureDegree.setSelectedIndex(0);
        structureDegree.setVisible(true);
        structureDetailsPanel.add(structureDegree);
        int currentStructureDegree = structureDegree.getItemAt(structureDegree.getSelectedIndex());
        int maxTParameter = Math.floorDiv(currentStructureDegree - 1, 2);


       JLabel tParameterLabel = new JLabel("T-Parameter: ");
       tParameterLabel.setVisible(true);
       structureDetailsPanel.add(tParameterLabel);

        diagnosisParameter = new JComboBox<>();
        for(int i = 1; i <= maxTParameter; i++) {
            diagnosisParameter.addItem(i);
        }
        diagnosisParameter.setSelectedIndex(0);
        diagnosisParameter.setVisible(true);


        structureDetailsPanel.add(diagnosisParameter);
        structureDetailsPanel.setVisible(true);

        matrixDetailsPanel = new JPanel();
        matrixDetailsPanel.setLayout(new BoxLayout(matrixDetailsPanel, BoxLayout.Y_AXIS));

        JPanel matrixLabelPanel = new JPanel();
        matrixLabelPanel.setLayout(new BoxLayout(matrixLabelPanel, BoxLayout.X_AXIS));

        JLabel adjacencyMatrixLabel = new JLabel("Adjacency Matrix: ");
        adjacencyMatrixLabel.setVisible(true);
        matrixLabelPanel.add(adjacencyMatrixLabel);
        matrixLabelPanel.setVisible(true);
        matrixDetailsPanel.add(matrixLabelPanel);

        JPanel matrixValuesPanel = new JPanel();
        matrixValuesPanel.setLayout(new BoxLayout(matrixValuesPanel, BoxLayout.Y_AXIS));

        structureAdjacencyMatrix = new ArrayList<>();
        for(int i = 0; i < currentStructureDegree; i++) {
            List<JCheckBox> matrixRow = new ArrayList<>();
            JPanel matrixRowPanel = new JPanel();
            for(int j = 0; j < currentStructureDegree; j++) {
                JCheckBox test = new JCheckBox();
                test.setSelected(false);
                if(i == j) {
                    test.setEnabled(false);
                }
                matrixRowPanel.add(test);
                test.setVisible(true);
                matrixRow.add(test);
            }
            matrixValuesPanel.add(matrixRowPanel);
            matrixRowPanel.setVisible(true);
            structureAdjacencyMatrix.add(matrixRow);
        }
        matrixValuesPanel.setVisible(true);
        matrixDetailsPanel.add(matrixValuesPanel);
        matrixDetailsPanel.setVisible(true);

        add(structureDetailsPanel);
        add(matrixDetailsPanel);
        setVisible(true);
    }
}
