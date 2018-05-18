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
        List<Syndrome> syndromes = structure.getDiagnosticOpinionPattern();
        /*List<Syndrome> syndromes = structure.getDiagnosticOpinionPattern();
        Set<Test> tests  = syndromes.get(0).getTestResults().keySet();
        StringBuilder builder = new StringBuilder("[");
        for(Test test: tests) {
            builder.append(test.getTestingUnit().toString() + ", ");
        }
        builder.append("]");
        System.out.println(builder.toString());
        builder = new StringBuilder("[");
        for(Test test: tests) {
            builder.append(test.getTestedUnit().toString() + ", ");
        }
        builder.append("]");
        System.out.println(builder.toString());
        System.out.println();
        for(Syndrome syndrome: syndromes) {
            System.out.println(syndrome.getTestResults().values().toString());
        }
        */
    }
}
