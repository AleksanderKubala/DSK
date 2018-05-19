package Gui;

import Algorithm.DiagnosticStructure;
import Algorithm.LGraph;
import Utils.Misc;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionsPanel extends JPanel {

    private JButton generate;
    private JButton createLGraph;
    private JButton findMatching;
    private JButton label;

    private JPanel buttonPanel;
    private JPanel messagePanel;
    private JPanel resultsPanel;

    private JLabel message;
    private JLabel result;

    private StructureSettingsPanel settingsPanel;

    private DiagnosticStructure diagnosticStructure;
    private LGraph lGraph;

    public OptionsPanel(StructureSettingsPanel settingsPanel) {
        super();
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.settingsPanel = settingsPanel;

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        generate = new JButton("Generate");
        generate.addActionListener(new GenerateActionListener());
        generate.setVisible(true);
        buttonPanel.add(generate);

        createLGraph = new JButton("Create L-Graph");
        createLGraph.setVisible(true);
        createLGraph.setEnabled(false);
        buttonPanel.add(createLGraph);

        findMatching = new JButton("Find Matching");
        findMatching.setVisible(true);
        findMatching.setEnabled(false);
        buttonPanel.add(findMatching);

        label = new JButton("Label");
        label.setVisible(true);
        label.setEnabled(false);
        buttonPanel.add(label);

        buttonPanel.setVisible(true);
        add(buttonPanel);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        message = new JLabel("----");
        message.setVisible(true);
        messagePanel.add(message);

        messagePanel.setVisible(true);
        add(messagePanel);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new FlowLayout());
        JLabel faultyUnits = new JLabel("Faulty units: ");
        faultyUnits.setVisible(true);
        resultsPanel.add(faultyUnits);

        result = new JLabel("----");
        result.setVisible(true);
        resultsPanel.add(result);

        resultsPanel.setVisible(true);
        add(resultsPanel);

        setVisible(true);
    }

    private Boolean checkNecessaryCondition(int[][] adjacencyMatrix, Integer diagnosisParameter) {

        Boolean conditionMet = true;
        int checkedColumn = 0;
        for(int i = 0; (i < adjacencyMatrix.length) && (conditionMet); i++) {
            checkedColumn = i;
            int columnSum = 0;
            for(int j = 0; (j < adjacencyMatrix.length); j++) {
                columnSum += adjacencyMatrix[j][i];
            }
            if(columnSum < diagnosisParameter)
                conditionMet = false;
        }
        if(!conditionMet) {
            message.setText("<html><font color='red'>Necessary condition not met. Check node " + checkedColumn + "</font></html>");
            message.repaint();
        }
        return conditionMet;
    }


    private Boolean checkSufficientCondition(int[][] adjacencyMatrix, Integer diagnosisParameter) {
        Boolean conditionMet = true;
        List<Integer> nodes = new ArrayList<>();
        Set<Integer> checkedSubset = new HashSet<>();
        for(int i = 0; i < adjacencyMatrix.length; i++) {
            nodes.add(i);
        }

        for(int p = 0; (p < diagnosisParameter) && (conditionMet); p++) {
            List<Set<Integer>> subsets = new ArrayList<>();
            int subsetSize = (adjacencyMatrix.length - (2*diagnosisParameter) + p);
            Misc.getSubsets(nodes, subsetSize, 0, new HashSet<>(), subsets);
            for(int i = 0; (i < subsets.size()) && (conditionMet); i++) {
                checkedSubset = subsets.get(i);
                int uniqueChildrenCount = 0;
                List<Integer> checkedNodes = new ArrayList<>();
                for(Integer node: checkedSubset) {
                    for(int j = 0; j < adjacencyMatrix.length; j++) {
                        Integer checkedNode = adjacencyMatrix[node][j];
                        if(checkedNode != 0) {
                            if(!checkedNodes.contains(checkedNode)) {
                                uniqueChildrenCount++;
                                checkedNodes.add(checkedNode);
                            }
                        }
                    }
                }
                if(uniqueChildrenCount <= p) {
                    conditionMet = false;
                }
            }
        }
        if(!conditionMet) {
            message.setText("<html><font color='red'>Sufficient condition not met. Check nodes: " + checkedSubset.toString() + "</font></html>");
            message.repaint();
        }

        return conditionMet;
    }

    private class GenerateActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == generate) {
                int[][] adjacencyMatrix = settingsPanel.getAdjacencyMatrix();
                Integer diagnosisParameter = settingsPanel.getTParameterValue();
                Boolean conditionsMet = checkNecessaryCondition(adjacencyMatrix, diagnosisParameter);
                if(conditionsMet) {
                    conditionsMet = checkSufficientCondition(adjacencyMatrix, diagnosisParameter);
                } else {
                    return;
                }
                if(conditionsMet) {
                    message.setText("<html><font color='green'>Conditions met.</font></html>");
                    message.repaint();
                    //diagnosticStructure = new DiagnosticStructure(adjacencyMatrix);
                }
            }
        }
    }

}
