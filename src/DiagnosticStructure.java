import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiagnosticStructure {

    private static Integer FAULTY = 1;
    private static Integer OK = 0;

    private SimpleDirectedWeightedGraph<Integer, Test> structureGraph;
    private List<Syndrome> diagnosticOpinionPattern;
    private Integer diagnosisParameter;

    public DiagnosticStructure() {
        structureGraph = new SimpleDirectedWeightedGraph<>(null, null);
        diagnosticOpinionPattern = new ArrayList<>();
    }

    public DiagnosticStructure(Integer[][] adjacencyMatrix) {
        this();
        if(adjacencyMatrix != null) {
            fillGraph(adjacencyMatrix);
        }
    }

    public void fillGraph(Integer[][] adjacencyMatrix) {
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
        for(int i = 1; i <= diagnosisParameter; i++) {
            getSubsets(new ArrayList<>(structureGraph.vertexSet()), i, 0, new HashSet<>(), faultyUnitsSets);
        }
        for(Set<Integer> faultyUnitsSet: faultyUnitsSets) {
            Syndrome syndrome = computeSyndrome(faultyUnitsSet);
            diagnosticOpinionPattern.add(syndrome);
            syndrome.sortTests();
        }
    }

    private Syndrome computeSyndrome(Set<Integer> faultyUnits) {
        Syndrome syndrome = new Syndrome();
        Set<Test> tests = structureGraph.edgeSet();
        Boolean faultyTestingUnit = false;
        Boolean faultyTestedUnit = false;

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
                return new TestResult(FAULTY);
            } else {
                return new TestResult(OK);
            }
        }
    }

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

    public Integer getDiagnosisParameter() {
        return diagnosisParameter;
    }

    public void setDiagnosisParameter(Integer diagnosisParameter) {
        this.diagnosisParameter = diagnosisParameter;
    }

    public List<Syndrome> getDiagnosticOpinionPattern() {
        return diagnosticOpinionPattern;
    }
}
