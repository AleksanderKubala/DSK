import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Integer[][] structureMap = new Integer[][] {
                {0, 1, 0, 0, 1},
                {1, 0, 1, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 1, 0, 1},
                {1, 0, 0, 1, 0}};

        DiagnosticStructure structure = new DiagnosticStructure(structureMap);
        structure.setDiagnosisParameter(2);
        structure.computeDiagnosticPattern();
        Syndrome syndrome = structure.getTestSyndrome(7);
        LGraph lGraph = structure.computeLGraph(syndrome);
        lGraph.label();
        Set<Integer> faultyNodes = lGraph.getFaultyNodes();
        System.out.println(faultyNodes.toString());
    }
}
