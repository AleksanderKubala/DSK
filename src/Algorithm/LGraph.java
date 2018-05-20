package Algorithm;

import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.EdmondsMaximumCardinalityMatching;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class LGraph {

    public DefaultUndirectedGraph<Node, Integer> graph;
    public Set<Integer> maximumMatchingEdges;

    public LGraph(List<Integer> nodes) {
        graph = new DefaultUndirectedGraph<>(null, new EdgeSupplier(), false);
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

    //procedura LABEL - wersja równoważna.
    public void label() {

        // wszystkie M-nienasycone wierzchołki ( nieincydentne z żadna krawędzią maksymalnego skojarzenia M)
        // oznaczane jako fault-free i dodawanie do listy wierzchołków do przejrzenia
        Set<Node> unsaturatedNodes = findUnsaturatedNodes(maximumMatchingEdges);
        for(Node node: unsaturatedNodes) {
            node.setLabel(NodeFault.FAULT_FREE);
        }

        List<Node> labeledNodes = new ArrayList<>(unsaturatedNodes);
        //dla każdego oznaczonego wierzchołka
        while(!labeledNodes.isEmpty()) {
            //usuwany z listy do sprawdzenia
            Node currentNode = labeledNodes.get(0);
            labeledNodes.remove(currentNode);
            // jeżeli wierzchołek oznaczony jako faulty-free - wszystkie przyległę i nieoznaczone wierzchołki oznaczane jako faulty
            if(currentNode.isFaultFree()) {
                Set<Node> adjacentNodes = getAdjacentNodes(currentNode);
                for(Node adjacentNode: adjacentNodes) {
                    if(!adjacentNode.isLabeled()) {
                        adjacentNode.setLabel(NodeFault.FAULTY);
                        labeledNodes.add(adjacentNode);
                    }
                }
            }
            //jeśli wierzchołek oznaczony jako faulty - oznaczenie wszystkich przyległych do niego wierzchołków
            // po krawędziach z maksymalnego skojarzenia jako fault-free
            if(currentNode.isFaulty()) {
                Set<Node> adjacentNodes = getAdjacentNodesByEdge(currentNode, maximumMatchingEdges);
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

    public Set<Integer> getGraphEdges() {
        return graph.edgeSet();
    }

    public void findMaximumCardinalityMatching() {
        MatchingAlgorithm.Matching<Node, Integer> matching = computeMaximumCardinalityMatching();
        maximumMatchingEdges = matching.getEdges();
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

    // wyszukiwanie M-nienasyconych wierzchołków - jeżeli wierzchołek nie jest końcem jakiejkolwiek
    // krawędzi ze skojarzenia M to jest M-nienasycony
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

    //algorytm z biblioteki jgrapht
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
