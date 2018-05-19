package Algorithm;

import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.EdmondsMaximumCardinalityMatching;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class LGraph {

    public SimpleGraph<Node, Integer> graph;

    public LGraph(List<Integer> nodes) {
        graph = new SimpleGraph<>(null, new EdgeSupplier(), false);
        for(Integer node: nodes) {
            graph.addVertex(new Node(node));
        }
    }

    public Node findNodeById(Integer id) {
        Set<Node> nodes = graph.vertexSet();
        for(Node node: nodes) {
            if(node.getIdentifier().equals(id))
                return node;
        }
        return null;
    }

    public void addEdge(Node source, Node target) {
        graph.addEdge(source, target);
    }

    public void label() {
        MatchingAlgorithm.Matching<Node, Integer> matching = computeMaximumCardinalityMatching();
        Set<Integer> matchingEdges = matching.getEdges();
        Set<Node> unsaturatedNodes = findUnsaturatedNodes(matchingEdges);
        for(Node node: unsaturatedNodes) {
            node.setLabel(NodeFault.FAULT_FREE);
        }
        List<Node> labeledNodes = new ArrayList<>(unsaturatedNodes);
        while(!labeledNodes.isEmpty()) {
            Node currentNode = labeledNodes.get(0);
            labeledNodes.remove(currentNode);
            if(currentNode.isFaultFree()) {
                Set<Node> adjacentNodes = getAdjacentNodes(currentNode);
                for(Node adjacentNode: adjacentNodes) {
                    if(!adjacentNode.isLabeled()) {
                        adjacentNode.setLabel(NodeFault.FAULTY);
                        labeledNodes.add(adjacentNode);
                    }
                }
            }
            if(currentNode.isFaulty()) {
                Set<Node> adjacentNodes = getAdjacentNodesByEdge(currentNode, matchingEdges);
                for(Node adjacentNode: adjacentNodes) {
                    if(!adjacentNode.isLabeled()) {
                        adjacentNode.setLabel(NodeFault.FAULT_FREE);
                        labeledNodes.add(adjacentNode);
                    }
                }
            }
        }
    }

    public Set<Integer> getFaultyNodes() {
        Set<Node> nodes = graph.vertexSet();
        Set<Integer> faultyNodes = new HashSet<>();
        for(Node node: nodes) {
            if(node.isFaulty())
                faultyNodes.add(node.getIdentifier());
        }
        return faultyNodes;
    }

    public int[][] getAdjacencyMatrix() {
        int size = graph.vertexSet().size();
        int[][] adjacencyMatrix = new int[size][size];
        Set<Integer> edges = graph.edgeSet();
        for(Integer edge: edges) {
            Node source = graph.getEdgeSource(edge);
            Node target = graph.getEdgeTarget(edge);
            adjacencyMatrix[source.getIdentifier()][target.getIdentifier()] = 1;
            adjacencyMatrix[target.getIdentifier()][source.getIdentifier()] = 1;
        }
        return adjacencyMatrix;
    }

    private Set<Node> getAdjacentNodesByEdge(Node node, Set<Integer> edges) {
        Set<Integer> outgoingEdges = graph.outgoingEdgesOf(node);
        List<Integer> interestingEdges = new ArrayList<>();
        Set<Node> adjacentNodes = new HashSet<>();
        for(Integer outgoingEdge: outgoingEdges) {
            if(edges.contains(outgoingEdge)) {
                interestingEdges.add(outgoingEdge);
            }
        }
        for(Integer edge: interestingEdges) {
            adjacentNodes.add(graph.getEdgeTarget(edge));
            adjacentNodes.add(graph.getEdgeSource(edge));
        }
        adjacentNodes.remove(node);
        return adjacentNodes;
    }

    private Set<Node> getAdjacentNodes(Node node) {
        Set<Integer> outgoingEdges = graph.outgoingEdgesOf(node);
        Set<Node> adjacentNodes = new HashSet<>();
        for(Integer edge: outgoingEdges) {
            adjacentNodes.add(graph.getEdgeSource(edge));
            adjacentNodes.add(graph.getEdgeTarget(edge));
        }
        adjacentNodes.remove(node);
        return adjacentNodes;
    }

    private Set<Node> findUnsaturatedNodes(Set<Integer> matching) {
        Set<Node> unsaturatedVertices = new HashSet<>(graph.vertexSet());
        Set<Node> checkedNodes = new HashSet<>();
        for(Integer edge: matching) {
            Node target = graph.getEdgeTarget(edge);
            Node source = graph.getEdgeSource(edge);
            if(!checkedNodes.contains(target)) {
                checkedNodes.add(target);
                if(unsaturatedVertices.contains(target)) {
                    unsaturatedVertices.remove(target);
                }
            }
            if(!checkedNodes.contains(source)) {
                checkedNodes.add(source);
                if(unsaturatedVertices.contains(source)) {
                    unsaturatedVertices.remove(source);
                }
            }
        }
        return unsaturatedVertices;
    }

    private MatchingAlgorithm.Matching<Node, Integer> computeMaximumCardinalityMatching() {
        MatchingAlgorithm<Node, Integer> matchingAlgorithm = new EdmondsMaximumCardinalityMatching<>(graph);
        return matchingAlgorithm.getMatching();
    }

    private class EdgeSupplier implements Supplier<Integer> {

        private Integer globalId = 0;

        @Override
        public Integer get() {
            Integer id = globalId;
            globalId++;
            return id;
        }
    }
}
