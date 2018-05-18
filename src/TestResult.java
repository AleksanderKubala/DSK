public class TestResult {

    private Integer result;

    public TestResult(Integer result) {
        this.result = result;
    }

    public Integer getResult() {
        return result;
    }

    @Override
    public String toString() {
        if(result == null)
            return "x";
        else
            return result.toString();
    }
}
