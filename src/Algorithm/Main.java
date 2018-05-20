package Algorithm;

import java.util.Set;

public class Main {

    public static void main(String[] args) {

        int[][] structureMap = new int[][] {
                {0, 1, 1, 0, 0},
                {0, 0, 1, 1, 0},
                {0, 0, 0, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 0, 0, 0}};

        DiagnosticStructure structure = new DiagnosticStructure(structureMap);
        structure.setDiagnosisParameter(2);
        structure.computeDiagnosticPattern();
        Syndrome syndrome = structure.getTestSyndrome(15);
        Set<Test> tests = syndrome.getTests();
        for(Test test: tests) {
            System.out.println(test.getTestingUnit() + "->" + test.getTestedUnit() + " || " + syndrome.getTestValue(test).getResult());
        }
        LGraph lGraph = structure.computeLGraph(syndrome);
        int[][] adjacencyMatrix = lGraph.getAdjacencyMatrix();
        for(int i = 0; i < adjacencyMatrix.length; i++) {
            for(int j = 0; j < adjacencyMatrix.length; j++) {
                System.out.print(adjacencyMatrix[i][j]);
            }
            System.out.println();
        }
        lGraph.label();
        Set<Integer> faultyNodes = lGraph.getFaultyNodes();
        System.out.println(faultyNodes.toString());
    }
}
