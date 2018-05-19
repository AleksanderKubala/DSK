package Algorithm;

public class TestResult {

    public static final Integer FAULTY = 1;
    public static final Integer OK = 0;

    private Integer result;

    public TestResult(Integer result) {
        this.result = result;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public String toString() {
        if(result == null)
            return "x";
        else
            return result.toString();
    }
}
