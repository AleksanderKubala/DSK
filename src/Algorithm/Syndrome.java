package Algorithm;

import java.util.*;
import java.util.stream.Collectors;

public class Syndrome {

    private static Integer globalId = 1;

    private Map<Test, TestResult> testResults;
    private Integer id;

    public Syndrome() {
        testResults = new HashMap<>();
        id = globalId;
        globalId++;
    }

    public void addTestValue(Test test, TestResult value) {
        testResults.put(test, value);
    }

    public TestResult getTestValue(Test test) {
        return testResults.get(test);
    }

    public Map<Test, TestResult> getTestResults() {
        return testResults;
    }

    // tworzenie realizacji syndromu - zastąpienie wartości x losowymi 0 lub 1
    public Syndrome getSyndromeRealization() {
       Map<Test, TestResult> testResultsRealization = (Map<Test, TestResult>)((HashMap<Test, TestResult>)testResults).clone();
       Syndrome realization = new Syndrome();
       realization.testResults = testResultsRealization;
       realization.id = this.id;
       realization.evaluate();

       return realization;
    }

    public Set<Test> getTests() {
        return testResults.keySet();
    }

    // wartościowanie oceny x
    private void evaluate() {
        Random random = new Random();
        Collection<TestResult> results = testResults.values();
        for(TestResult result: results) {
            if(result.getResult() == null) {
                //result.setResult(TestResult.OK);

                if(random.nextDouble() < 0.5) {
                    result.setResult(Algorithm.TestResult.OK);
                } else {
                    result.setResult(Algorithm.TestResult.FAULTY);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;
        if(getClass() != o.getClass()) {
            return false;
        }

        Syndrome other = (Syndrome) o;
        return this.id.equals(other.id);
    }

    public void sortTests() {
        testResults = testResults.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
