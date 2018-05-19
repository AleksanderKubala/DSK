package Algorithm;

public class Node {

    private Integer identifier;
    private NodeFault label;

    public Node(Integer identifier) {
        this.identifier = identifier;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public NodeFault getLabel() {
        return label;
    }

    public void setLabel(NodeFault label) {
        this.label = label;
    }

    public Boolean isLabeled() {
        if(label == null)
            return false;
        else
            return true;
    }

    public Boolean isFaulty() {
        return label.equals(NodeFault.FAULTY);
    }

    public Boolean isFaultFree() {
        return label.equals(NodeFault.FAULT_FREE);
    }

}
