package Gui;

import Algorithm.*;
import Utils.Misc;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;
import org.jgrapht.ext.JGraphXAdapter;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.BoxView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class OptionsPanel extends JPanel {

    private JButton generate;
    private JButton createLGraph;
    private JButton findMatching;
    private JButton label;

    private JPanel buttonPanel;
    //private JPanel messagePanel;
    private JPanel resultsPanel;
    private JPanel syndromePanel;

    private JComboBox<String> syndromes;

    private JLabel testingUnits;
    private JLabel testedUnits;
    private JLabel message;
    private JLabel result;

    private StructureSettingsPanel settingsPanel;
    private MessagePanel messagePanel;

    private DiagnosticStructure diagnosticStructure;
    private LGraph lGraph;

    public OptionsPanel(StructureSettingsPanel settingsPanel, MessagePanel messagePanel) {
        super();
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.settingsPanel = settingsPanel;
        this.messagePanel = messagePanel;

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        generate = new JButton("Generate");
        generate.addActionListener(new GenerateActionListener());
        generate.setVisible(true);
        generate.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(generate);

        createLGraph = new JButton("Create L-Graph");
        createLGraph.addActionListener(new LGraphActionListener());
        createLGraph.setVisible(true);
        createLGraph.setEnabled(false);
        createLGraph.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(createLGraph);

        findMatching = new JButton("Find Matching");
        findMatching.addActionListener(new MatchingActionListener());
        findMatching.setVisible(true);
        findMatching.setEnabled(false);
        findMatching.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(findMatching);

        label = new JButton("Label");
        label.addActionListener(new LabelActionListener());
        label.setVisible(true);
        label.setEnabled(false);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(label);

        buttonPanel.setVisible(true);
        add(buttonPanel);

        /*
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        message = new JLabel("----");
        message.setVisible(true);
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        messagePanel.add(message);


        messagePanel.setVisible(true);
        add(messagePanel);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel faultyUnits = new JLabel("Faulty units: ");
        faultyUnits.setVisible(true);
        faultyUnits.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsPanel.add(faultyUnits);

        result = new JLabel("----");
        result.setVisible(true);
        result.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultsPanel.add(result);

        resultsPanel.setVisible(true);
        add(resultsPanel);*/

        JPanel syndromesDescPanel = new JPanel();
        syndromesDescPanel.setLayout(new BoxLayout(syndromesDescPanel, BoxLayout.Y_AXIS));
        syndromesDescPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel syndromeBoxLabel = new JLabel("Syndromes: ");
        syndromeBoxLabel.setVisible(true);
        syndromeBoxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        syndromesDescPanel.add(syndromeBoxLabel);

        testingUnits = new JLabel("----");
        testingUnits.setVisible(true);
        testingUnits.setAlignmentX(Component.CENTER_ALIGNMENT);
        syndromesDescPanel.add(testingUnits);

        testedUnits = new JLabel("----");
        testedUnits.setVisible(true);
        testedUnits.setAlignmentX(Component.CENTER_ALIGNMENT);
        syndromesDescPanel.add(testedUnits);

        syndromesDescPanel.setVisible(true);
        add(syndromesDescPanel);

        syndromePanel = new JPanel();
        syndromePanel.setLayout(new BoxLayout(syndromePanel, BoxLayout.Y_AXIS));
        syndromePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
        dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        syndromes = new JComboBox<>();
        syndromes.addItem("----");
        syndromes.setVisible(true);
        syndromes.setAlignmentX(Component.CENTER_ALIGNMENT);
        syndromes.setRenderer(dlcr);
        syndromePanel.add(syndromes);

        syndromePanel.setVisible(true);
        add(syndromePanel);

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
            messagePanel.messageNewLine("Necessary condition not met. Check node " + checkedColumn + ".");
            /*
            message.setText("Necessary condition not met. Check node " + checkedColumn);
            message.repaint();*/
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
                    for(int child = 0; child < adjacencyMatrix.length; child++) {
                        Integer childValue = adjacencyMatrix[node][child];
                        if(childValue != 0) {
                            if(!checkedNodes.contains(child)) {
                                uniqueChildrenCount++;
                                checkedNodes.add(child);
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
            messagePanel.messageNewLine("Sufficient condition not met. Check nodes: " + checkedSubset.toString() + ".");
           /* message.setText("Sufficient condition not met. Check nodes: " + checkedSubset.toString() + "");
            message.repaint();*/
        }

        return conditionMet;
    }

    private void updateSyndromesBox() {
        syndromes.removeAllItems();
        List<Syndrome> syndromeList = diagnosticStructure.getDiagnosticOpinionPattern();
        StringBuilder testingUnitsString = new StringBuilder();
        StringBuilder testedUnitsString = new StringBuilder();
        for(Syndrome syndrome: syndromeList) {
            StringBuilder builder = new StringBuilder();
            Collection<TestResult> testResults = syndrome.getTestResults().values();
            for(TestResult testResult: testResults) {
                builder.append(testResult.toString());
            }
            syndromes.addItem(builder.toString());
        }
        if(!syndromeList.isEmpty()) {
            Syndrome syndrome = syndromeList.get(0);
            Set<Test> tests = syndrome.getTests();
            for(Test test: tests) {
                testingUnitsString.append(test.getTestingUnit());
                testedUnitsString.append(test.getTestedUnit());
            }
        }
        testingUnits.setText(testingUnitsString.toString());
        testedUnits.setText(testedUnitsString.toString());
        testingUnits.repaint();
        testedUnits.repaint();
        syndromes.repaint();
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
                }
                if(conditionsMet) {
                    messagePanel.message("Necessary and sufficient conditions met. Generating diagnostic structure... ");
                    /*
                    message.setText("Conditions met");
                    message.repaint();
                    */
                    diagnosticStructure = new DiagnosticStructure(adjacencyMatrix);
                    diagnosticStructure.setDiagnosisParameter(diagnosisParameter);
                    diagnosticStructure.computeDiagnosticPattern();
                    updateSyndromesBox();
                    messagePanel.messageNewLine("Done.");
                    messagePanel.messageNewLine("Generated structure contains following tests: ");
                    Set<Test> tests = diagnosticStructure.getStructureGraph().edgeSet();
                    for(Test test: tests) {
                        messagePanel.messageNewLine(test.getTestingUnit() + " -> " + test.getTestedUnit());
                    }
                    createLGraph.setEnabled(true);
                }
                if(!conditionsMet) {
                    testingUnits.setText("----");
                    testedUnits.setText("----");
                    syndromes.removeAllItems();
                    syndromes.addItem("----");
                    createLGraph.setEnabled(false);
                    findMatching.setEnabled(false);
                    label.setEnabled(false);
                    testingUnits.repaint();
                    testedUnits.repaint();
                    syndromes.repaint();
                }
            }
        }
    }

    private class LGraphActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == createLGraph) {
                Syndrome syndrome = diagnosticStructure.getTestSyndrome(syndromes.getSelectedIndex());
                messagePanel.messageNewLine("Selected syndrome: " + syndromes.getItemAt(syndromes.getSelectedIndex()));
                StringBuilder builder = new StringBuilder();
                for(TestResult testResult: syndrome.getTestResults().values()) {
                    builder.append(testResult.toString());
                }
                messagePanel.messageNewLine("Selected syndrome realization: " + builder.toString());
                messagePanel.message("Generating L-Graph for given syndrome realization... ");
                lGraph = diagnosticStructure.computeLGraph(syndrome);
                messagePanel.messageNewLine("Done.");
                messagePanel.messageNewLine("Generated L-Graph conatins following edges: ");
                Set<Integer> edges = lGraph.getGraphEdges();
                for(Integer edge: edges) {
                    messagePanel.messageNewLine(lGraph.graph.getEdgeSource(edge).getIdentifier()
                            + " - "
                            + lGraph.graph.getEdgeTarget(edge).getIdentifier());
                }
                findMatching.setEnabled(true);
            }
        }
    }

    private class MatchingActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == findMatching) {
                messagePanel.message("Finding maximum cardinality matching in L-Graph... ");
                lGraph.findMaximumCardinalityMatching();
                messagePanel.messageNewLine("Done.");
                messagePanel.messageNewLine("Edges belonging to found maximum matching are: ");
                for(Integer edge: lGraph.maximumMatchingEdges) {
                    messagePanel.messageNewLine(lGraph.graph.getEdgeSource(edge).getIdentifier()
                            + " - "
                            + lGraph.graph.getEdgeTarget(edge).getIdentifier());
                }
                label.setEnabled(true);
            }
        }
    }

    private class LabelActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == label) {
                messagePanel.message("Executing LABEL procedure... ");
                lGraph.label();
                messagePanel.messageNewLine("Done.");
                messagePanel.messageNewLine("Results: ");
                Set<Node> nodes = lGraph.graph.vertexSet();
                StringBuilder builder = new StringBuilder();
                for(Node node: nodes) {
                    builder.append("Node " + node.getIdentifier() + ": ");
                    if(node.isFaulty()) {
                        builder.append("faulty");
                    } else {
                        builder.append("fault-free");
                    }
                    messagePanel.messageNewLine(builder.toString());
                    builder.delete(0, builder.length());
                }
            }
        }
    }
}
