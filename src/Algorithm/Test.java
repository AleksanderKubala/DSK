package Algorithm;

import java.util.Comparator;

public class Test implements Comparable<Test>{

    private Integer testingUnit;
    private Integer testedUnit;

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

    public Boolean isIncident(Integer node){
        return ((testingUnit.equals(node)) || (testedUnit.equals(node)));
    }

    public Boolean isTestingUnit(Integer node) {
        return testingUnit.equals(node);
    }

    public Boolean isTestedUnit(Integer node) {
        return testedUnit.equals(node);
    }

    public Integer getOtherEnd(Integer node) {
        if(isTestedUnit(node)) {
            return testingUnit;
        } else if( isTestingUnit(node)) {
            return testedUnit;
        } else {
            return null;
        }
    }

    public Integer getTestingUnit() {
        return testingUnit;
    }

    public Integer getTestedUnit() {
        return testedUnit;
    }

    @Override
    public int compareTo(Test o) {
        return Comparator.comparingInt(Test::getTestingUnit)
                .thenComparingInt(Test::getTestedUnit)
                .compare(this, o);
    }

}
