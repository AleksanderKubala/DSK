package Algorithm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.MatrixExporter;

public class Main {

    public static void main(String[] args) {

        Integer[][] structureMap = new Integer[][] {
                {0, 1, 1, 0, 0},
                {0, 0, 1, 1, 0},
                {0, 0, 0, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 0, 0, 0}};

        DiagnosticStructure structure = new DiagnosticStructure(structureMap);
        structure.setDiagnosisParameter(2);
        structure.computeDiagnosticPattern();
        Syndrome syndrome = structure.getTestSyndrome(12);
        Set<Test> tests = syndrome.getTests();
        for(Test test: tests) {
            System.out.println(test.getTestingUnit() + "->" + test.getTestedUnit() + " || " + syndrome.getTestValue(test).getResult());
        }
        //System.out.println(syndrome.getTestResults().values().toString());
        /*
        StringBuilder builder = new StringBuilder("[");
        for(Algorithm.Test test: syndrome.getTests()) {
            builder.append(test.getTestingUnit() + ", ");
        }
        System.out.println(builder.toString());
        builder = new StringBuilder("[");
        for(Algorithm.Test test: syndrome.getTests()) {
            builder.append(test.getTestedUnit() + ", ");
        }
        System.out.println(builder.toString());
        */
        LGraph lGraph = structure.computeLGraph(syndrome);
        Path path = Paths.get("matrix.txt");
        try {
            Writer writer = new BufferedWriter(Files.newBufferedWriter(path));
            MatrixExporter<Node, Integer> exporter = new MatrixExporter<>();
            exporter.exportGraph(lGraph.graph, writer);
        } catch (IOException | ExportException e) {
            e.printStackTrace();
        }
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
