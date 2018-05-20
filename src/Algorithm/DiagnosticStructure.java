package Algorithm;

import Utils.Misc;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.*;

public class DiagnosticStructure {



    private SimpleDirectedWeightedGraph<Integer, Test> structureGraph;
    private List<Syndrome> diagnosticOpinionPattern;
    private Integer diagnosisParameter;

    public DiagnosticStructure() {
        structureGraph = new SimpleDirectedWeightedGraph<>(null, null);
        diagnosticOpinionPattern = new ArrayList<>();
    }

    public DiagnosticStructure(int[][] adjacencyMatrix) {
        this();
        if(adjacencyMatrix != null) {
            fillGraph(adjacencyMatrix);
        }
    }

    public void fillGraph(int[][] adjacencyMatrix) {
        int nodeCount = adjacencyMatrix.length;
        for(int i = 0; i < nodeCount; i++) {
            structureGraph.addVertex(i);
        }
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for(int j = 0; j < adjacencyMatrix.length; j++) {
                if(adjacencyMatrix[i][j] != 0) {
                    structureGraph.addEdge(i, j, new Test(i, j));
                }
            }
        }
    }

    public void computeDiagnosticPattern() {
        List<Set<Integer>> faultyUnitsSets = new ArrayList<>();
        for(int i = 0; i <= diagnosisParameter; i++) {
            Misc.getSubsets(new ArrayList<>(structureGraph.vertexSet()), i, 0, new HashSet<>(), faultyUnitsSets);
        }
        for(Set<Integer> faultyUnitsSet: faultyUnitsSets) {
            Syndrome syndrome = computeSyndrome(faultyUnitsSet);
            diagnosticOpinionPattern.add(syndrome);
            syndrome.sortTests();
            System.out.print(syndrome.getTestResults().values().toString());
            System.out.println(faultyUnitsSet.toString());
        }
    }

    public Syndrome getTestSyndrome(Integer index) {
        Syndrome syndrome = diagnosticOpinionPattern.get(index);
        Syndrome realization = syndrome.getSyndromeRealization();
        for(Test test: realization.getTests()) {
            structureGraph.setEdgeWeight(test, realization.getTestValue(test).getResult());
        }
        return realization;
    }

    private Syndrome computeSyndrome(Set<Integer> faultyUnits) {
        Syndrome syndrome = new Syndrome();
        Set<Test> tests = structureGraph.edgeSet();
        Boolean faultyTestingUnit;
        Boolean faultyTestedUnit;

        for(Test test: tests) {
            faultyTestingUnit = faultyUnits.contains(test.getTestingUnit());
            faultyTestedUnit = faultyUnits.contains(test.getTestedUnit());
            syndrome.addTestValue(test, pmcModelTest(faultyTestingUnit, faultyTestedUnit));
        }
        return syndrome;
    }

    private TestResult pmcModelTest(Boolean faultyTestingUnit, Boolean faultyTestedUnit) {
        if(faultyTestingUnit) {
            return new TestResult(null);
        } else {
            if(faultyTestedUnit) {
                return new TestResult(TestResult.FAULTY);
            } else {
                return new TestResult(TestResult.OK);
            }
        }
    }

    private TestResult bgmModelTest(Boolean faultyTestingUnit, Boolean faultyTestesUnit) {
        if(faultyTestesUnit)
            return new TestResult(TestResult.FAULTY);
        else {
            if(faultyTestingUnit) {
                return new TestResult(null);
            } else {
                return new TestResult(TestResult.OK);
            }
        }
    }

    public LGraph computeLGraph(Syndrome syndrome) {
        if(!diagnosticOpinionPattern.contains(syndrome)){
            throw new IllegalArgumentException("Passed syndrome is invalid for this diagnostic structure");
        }
        Map<Integer, Set<Integer>> zeroDescendantsSets = computeZeroDescendatns();
        Map<Integer, Set<Integer>> deltaSets = computeDeltaSets();
        Map<Integer, Set<Integer>> zeroAncestorsSets = computeZeroAncestors();

        Map<Integer, Set<Integer>> deltaZeroDescendatnsSets = new HashMap<>();
        Map<Integer, Set<Integer>> deltaZeroDescendantsAncestorsSets = new HashMap<>();
        Map<Integer, Set<Integer>> lSets = new HashMap<>();

        for(Integer node: zeroDescendantsSets.keySet()) {
            Set<Integer> deltaZeroDescendatns = new HashSet<>();
            for(Integer zeroDescendant: zeroDescendantsSets.get(node)) {
                deltaZeroDescendatns.addAll(deltaSets.get(zeroDescendant));
            }
            deltaZeroDescendatnsSets.put(node, deltaZeroDescendatns);
        }


        for(Integer node: deltaZeroDescendatnsSets.keySet()) {
            Set<Integer> deltaZeroDescendantsAncestors = new HashSet<>();
            for(Integer deltaZeroDescendant: deltaZeroDescendatnsSets.get(node)) {
                deltaZeroDescendantsAncestors.addAll(zeroAncestorsSets.get(deltaZeroDescendant));
            }
            deltaZeroDescendantsAncestorsSets.put(node, deltaZeroDescendantsAncestors);
        }

        for(Integer node: deltaZeroDescendatnsSets.keySet()) {
            Set<Integer> lSet = new HashSet<>();
            lSet.addAll(deltaZeroDescendatnsSets.get(node));
            lSet.addAll(deltaZeroDescendantsAncestorsSets.get(node));
            lSets.put(node, lSet);
        }

        LGraph lGraph = new LGraph(new ArrayList<>(structureGraph.vertexSet()));

        for(Integer node: zeroDescendantsSets.keySet()) {
            for(Integer lNode: lSets.get(node)) {
                lGraph.addEdge(lGraph.findNodeById(lNode), lGraph.findNodeById(node));
            }
        }

        /*
        Set<Integer> nodes = structureGraph.vertexSet();
        Set<Test> tests = syndrome.getTests();
        LGraph lGraph = new LGraph(new ArrayList<>(nodes));

        for(Integer node: nodes) {
            Node source = lGraph.findNodeById(node);
            for(Test test: tests) {
                if(test.isIncident(node)) {
                    if(syndrome.getTestValue(test).getResult().equals(TestResult.FAULTY)) {
                        Node target = lGraph.findNodeById(test.getOtherEnd(node));
                        lGraph.addEdge(source, target);
                    }
                }
            }
        }

        return lGraph;
        */
        return lGraph;
    }

    private Map<Integer, Set<Integer>> computeZeroDescendatns() {
        Map<Integer, Set<Integer>> zeroDescendantsSets = new HashMap<>();
        Set<Integer> nodes = structureGraph.vertexSet();

        AllDirectedPaths<Integer, Test> alg = new AllDirectedPaths<>(structureGraph);

        for(Integer source: nodes) {
            Set<Integer> zeroDescendants = new HashSet<>();
            for(Integer target: nodes) {
                if(!source.equals(target)) {
                    List<GraphPath<Integer, Test>> paths = alg.getAllPaths(source, target, true, structureGraph.edgeSet().size());
                    for(GraphPath<Integer, Test> path: paths) {
                        double weightSum = 0.0;
                        for(Test test: path.getEdgeList()) {
                            weightSum += structureGraph.getEdgeWeight(test);
                            if(weightSum > 0.0)
                                break;
                        }
                        if(weightSum == 0.0) {
                            zeroDescendants.add(target);
                            break;
                        }
                    }
                }
            }
            zeroDescendants.add(source);
            zeroDescendantsSets.put(source, zeroDescendants);
        }

        return zeroDescendantsSets;
    }

    private Map<Integer, Set<Integer>> computeZeroAncestors() {
        Map<Integer, Set<Integer>> zeroAncestorsSets = new HashMap<>();
        AllDirectedPaths<Integer, Test> alg = new AllDirectedPaths<>(structureGraph);
        Set<Integer> nodes = structureGraph.vertexSet();

        for(Integer target: nodes) {
            Set<Integer> zeroAncestors = new HashSet<>();
            for(Integer source: nodes) {
                if(!source.equals(target)) {
                    List<GraphPath<Integer, Test>> paths = alg.getAllPaths(source, target, true, structureGraph.edgeSet().size());
                    for(GraphPath<Integer, Test> path: paths) {
                        double weightSum = 0.0;
                        for(Test test: path.getEdgeList()) {
                            weightSum += structureGraph.getEdgeWeight(test);
                            if(weightSum > 0.0) {
                                break;
                            }
                        }
                        if(weightSum == 0.0) {
                            zeroAncestors.add(source);
                            break;
                        }
                    }
                }
            }
            zeroAncestorsSets.put(target, zeroAncestors);
        }
        return zeroAncestorsSets;
    }

    private Map<Integer, Set<Integer>> computeDeltaSets() {
        Map<Integer, Set<Integer>> deltaSets = new HashMap<>();
        Set<Integer> nodes = structureGraph.vertexSet();
        Set<Test> tests = structureGraph.edgeSet();
        for(Integer node: nodes) {
            Set<Integer> deltaSet = new HashSet<>();
            for(Test test: tests) {
                if(test.isIncident(node)) {
                    if(structureGraph.getEdgeWeight(test) == 1.0) {
                        deltaSet.add(test.getOtherEnd(node));
                    }
                }
            }
            deltaSets.put(node, deltaSet);
        }
        return deltaSets;
    }

    /*
    private void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current, List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) return;
        Integer x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }
    */

    public void setDiagnosisParameter(Integer diagnosisParameter) {
        this.diagnosisParameter = diagnosisParameter;
    }

    public List<Syndrome> getDiagnosticOpinionPattern() {
        return diagnosticOpinionPattern;
    }

    public SimpleDirectedWeightedGraph<Integer, Test> getStructureGraph() {
        return structureGraph;
    }
}
