import java.util.Comparator;

public class Test implements Comparable<Test>{

    private Integer testingUnit;
    private Integer testedUnit;
    private TestResult testValue;

    public Test(Integer testingUnit, Integer testedUnit) {
        this.testingUnit = testingUnit;
        this.testedUnit = testedUnit;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        }
        if(getClass() != o.getClass()) {
            return false;
        }

        Test other = (Test)o;

        return (testingUnit.equals(other.testingUnit)
                && testedUnit.equals(other.testedUnit));
    }

    public Integer getTestingUnit() {
        return testingUnit;
    }

    public Integer getTestedUnit() {
        return testedUnit;
    }

    public TestResult getTestValue() {
        return testValue;
    }

    public void setTestValue(TestResult testValue) {
        this.testValue = testValue;
    }

    @Override
    public int compareTo(Test o) {
        return Comparator.comparingInt(Test::getTestingUnit)
                .thenComparingInt(Test::getTestedUnit)
                .compare(this, o);
    }

}
