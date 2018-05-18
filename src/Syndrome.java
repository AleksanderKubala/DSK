import java.util.*;
import java.util.stream.Collectors;

public class Syndrome {

    private Map<Test, TestResult> testResults;

    public Syndrome() {
        testResults = new HashMap<>();
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

    public void sortTests() {
        testResults = testResults.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
