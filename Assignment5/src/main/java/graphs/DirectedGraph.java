package graphs;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DirectedGraph<V extends Identifiable, E> {

    private final Map<String,V> vertices = new HashMap<>();
    private final Map<V,Map<V,E>> edges = new HashMap<>();

    /** representation invariants:
        1.  the vertices map stores all vertices by their identifying id (which prevents duplicates)
        2.  the edges map stores all directed outgoing edges by their from-vertex and then in the nested map by their to-vertex
        3.  there can only be two directed edges between any two given vertices v1 and v2:
            one from v1 to v2 in edges.get(v1).get(v2)
            one from v2 to v1 in edges.get(v2).get(v1)
        4.  every vertex instance in the key-sets of edges shall also occur in the vertices map and visa versa
     **/

    public DirectedGraph() { }

    public Collection<V> getVertices() {
        return vertices.values();
    }

    /**
     * finds the vertex in the graph identified by the given id
     * @param id
     * @return  the vertex that matches the given id
     *          null if none of the vertices matches the id
     */
    public V getVertexById(String id) {
        return vertices.get(id);
    }

    /**
     * retrieves the collection of neighbour vertices that can be reached directly
     * via an out-going directed edge from 'fromVertex'
     * @param fromVertex
     * @return  null if fromVertex cannot be found in the graph
     *          an empty collection if fromVertex has no neighbours
     */
    public Collection<V> getNeighbours(V fromVertex) {
        if (fromVertex == null) return null;

        // TODO retrieve the collection of neighbour vertices of fromVertex out of the edges data structure

        return null;
    }
    public Collection<V> getNeighbours(String fromVertexId) {
        return this.getNeighbours(this.getVertexById(fromVertexId));
    }

    /**
     * retrieves the collection of edges
     * which connects the 'fromVertex' with its neighbours
     * (only the out-going edges directed from 'fromVertex' towards a neighbour shall be included
     * @param fromVertex
     * @return  null if fromVertex cannot be found in the graph
     *          an empty collection if fromVertex has no out-going edges
     */
    public Collection<E> getEdges(V fromVertex) {
        if (fromVertex == null) return null;

        // TODO retrieve the collection of out-going edges which connect fromVertex with a neighbour in the edges data structure

        return null;
    }
    public Collection<E> getEdges(String fromId) {
        return this.getEdges(this.getVertexById(fromId));
    }

    /**
     * Adds newVertex to the graph, if not yet present and in a way that maintains the representation invariants.
     * If a duplicate of newVertex (with the same id) already exists in the graph,
     *      nothing will be added, and the existing duplicate will be kept and returned.
     * @param newVertex
     * @return  the duplicate of newVertex with the same id that already exists in the graph,
     *          or newVertex itself if it has been added.
     */
    public V addOrGetVertex(V newVertex) {
        // TODO add and return the newVertex, or return the existing duplicate vertex with the same Id
        //  pay attention to sustaining representation invariant items 1. and 4.


        // a proper vertex shall be returned at all times
        return null;
    }




    /**
     * Adds a new, directed edge 'newEdge'
     * from vertex 'fromVertex' to vertex 'toVertex'
     * No change shall be made if a directed edge already exists between these vertices
     * @param fromVertex    the start vertex of the directed edge
     * @param toVertex      the target vertex of the directed edge
     * @param newEdge       the instance with edge information
     * @return  whether the edge has been added successfully
     */
    public boolean addEdge(V fromVertex, V toVertex, E newEdge) {
        // TODO add (directed) newEdge to the graph between fromVertex and toVertex


        return false;
    }

    /**
     * Adds a new, directed edge 'newEdge'
     * from vertex with id=fromId to vertex with id=toId
     * No change shall be made if a directed edge already exists between these vertices
     * @param fromId    the id of the start vertex of the outgoing edge
     * @param toId      the id of the target vertex of the directed edge
     * @param newEdge   the instance with edge information
     * @return  whether the edge has been added successfully
     */
    public boolean addEdge(String fromId, String toId, E newEdge) {
        // TODO add (directed) newEdge to the graph between fromId and toId


        return false;
    }

    /**
     * Adds two directed edges: one from v1 to v2 and one from v2 to v1
     * both with the same edge information
     * @param v1
     * @param v2
     * @param newEdge
     * @return  whether both edges have been added
     */
    public boolean addConnection(V v1, V v2, E newEdge) {
        return this.addEdge(v1, v2, newEdge) && this.addEdge(v2, v1, newEdge);
    }

    /**
     * Adds two directed edges: one from id1 to id2 and one from id2 to id1
     * both with the same edge information
     * @param id1
     * @param id2
     * @param newEdge
     * @return  whether both edges have been added
     */
    public boolean addConnection(String id1, String id2, E newEdge) {
        return this.addEdge(id1, id2, newEdge) && this.addEdge(id2, id1, newEdge);
    }

    /**
     * retrieves the directed edge between 'fromVertex' and 'toVertex' from the graph, if any
     * @param fromVertex    the start vertex of the designated edge
     * @param toVertex      the end vertex of the designated edge
     * @return      the designated directed edge that has been registered in the graph
     *              returns null if no connection has been set up between these vertices in the specified direction
     */
    public E getEdge(V fromVertex, V toVertex) {
        if (fromVertex == null || toVertex == null) return null;
        // TODO retrieve the directed edge between vertices fromVertex and toVertex from the graph

        return null;
    }

    public E getEdge(String fromId, String toId) {
        return this.getEdge(this.vertices.get(fromId), this.vertices.get(toId));
    }

    /**
     * @return  the total number of vertices in the graph
     */
    public int getNumVertices() {
        return vertices.size();
    }

    /**
     * calculates and returns the total number of directed edges in the graph data structure
     * @return  the total number of edges in the graph
     */
    public int getNumEdges() {
        // TODO calculate and return the total number of directed edges in the graph


        return 0;
    }

    /**
     * Remove vertices without any connection from the graph
     */
    public void removeUnconnectedVertices() {
        this.edges.entrySet().removeIf(e -> e.getValue().size() == 0);
        this.vertices.entrySet().removeIf(e -> !this.edges.containsKey(e.getValue()));
    }

    /**
     * represents a path of connected vertices and edges in the graph
     */
    public class DGPath {
        private Deque<V> vertices = new LinkedList<>();
        private double totalWeight = 0.0;
        private Set<V> visited = new HashSet<>();

        /**
         * representation invariants:
         * 1. vertices contains a sequence of vertices that are connected in the graph by a directed edge,
         *    i.e. FOR ALL i: 0 < i < vertices.length: this.getEdge(vertices[i-1],vertices[i]) will provide edge information of the connection
         * 2. a path with one vertex has no edges
         * 3. a path without vertices is empty
         * totalWeight is a helper attribute to capture additional info from searches, not a fundamental property of a path
         * visited is a helper set to be able to track visited vertices in searches, not a fundamental property of a path
         **/

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(
                    String.format("Weight=%f Length=%d visited=%d (",
                            this.totalWeight, this.vertices.size(), this.visited.size()));
            String separator = "";
            for (V v : this.vertices) {
                sb.append(separator + v.getId());
                separator = ", ";
            }
            sb.append(")");
            return sb.toString();
        }

        public Queue<V> getVertices() {
            return this.vertices;
        }

        public double getTotalWeight() {
            return this.totalWeight;
        }

        public Set<V> getVisited() { return this.visited; }
    }

    /**
     * Uses a depth-first search algorithm to find a path from the start vertex to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.visited
     * @param startId
     * @param targetId
     * @return  the path from start to target
     *          returns null if either start or target cannot be matched with a vertex in the graph
     *                          or no path can be found from start to target
     */
    public DGPath depthFirstSearch(String startId, String targetId) {

        V start = getVertexById(startId);
        V target = getVertexById(targetId);
        if (start == null || target == null) return null;

        DGPath path = new DGPath();

        // TODO calculate the path from start to target by recursive depth-first-search

        return null;
    }


    /**
     * Uses a breadth-first search algorithm to find a path from the start vertex to the target vertex in the graph
     * All vertices that are being visited by the search should also be registered in path.visited
     * @param startId
     * @param targetId
     * @return  the path from start to target
     *          returns null if either start or target cannot be matched with a vertex in the graph
     *                          or no path can be found from start to target
     */
    public DGPath breadthFirstSearch(String startId, String targetId) {

        V start = getVertexById(startId);
        V target = getVertexById(targetId);
        if (start == null || target == null) return null;

        // initialise the result path of the search
        DGPath path = new DGPath();
        path.visited.add(start);

        // easy target
        if (start.equals(target)) {
            path.vertices.add(target);
            return path;
        }

        // TODO calculate the path from start to target by breadth-first-search


        return null;
    }

    // helper class to register the state of a vertex in dijkstra shortest path algorithm
    // your may change this class or delete it altogether follow a different approach in your implementation
    private class DSPNode implements Comparable<DSPNode> {
        protected V vertex;                // the graph vertex that is concerned with this DSPNode
        protected V fromVertex = null;     // the parent's node vertex that has an edge towards this node's vertex
        protected boolean marked = false;  // indicates DSP processing has been marked complete for this vertex
        protected double weightSumTo = Double.MAX_VALUE;   // sum of weights of current shortest path to this node's vertex

        private DSPNode(V vertex) {
            this.vertex = vertex;
        }

        // comparable interface helps to find a node with the shortest current path, sofar
        @Override
        public int compareTo(DSPNode dspv) {
            return Double.compare(weightSumTo, dspv.weightSumTo);
        }
    }

    /**
     * Calculates the edge-weighted shortest path from start to target
     * according to Dijkstra's algorithm of a minimum spanning tree
     * @param startId       id of the start vertex of the search
     * @param targetId      id of the target vertex of the search
     * @param weightMapper    provides a function, by which the weight of an edge can be retrieved or calculated
     * @return  the shortest path from start to target
     *          returns null if either start or target cannot be matched with a vertex in the graph
     *                          or no path can be found from start to target
     */
    public DGPath dijkstraShortestPath(String startId, String targetId,
                                       Function<E,Double> weightMapper) {

        V start = getVertexById(startId);
        V target = getVertexById(targetId);
        if (start == null || target == null) return null;

        // initialise the result path of the search
        DGPath path = new DGPath();
        path.visited.add(start);

        // easy target
        if (start.equals(target)) {
            path.vertices.add(start);
            return path;
        }

        // keep track of the DSP status of all visited nodes
        // you may choose a different approach of tracking progress of the algorithm, if you wish
        Map<V, DSPNode> progressData = new HashMap<>();

        // initialise the progress of the start node
        DSPNode nextDspNode = new DSPNode(start);
        nextDspNode.weightSumTo = 0.0;
        progressData.put(start, nextDspNode);

        while (nextDspNode != null) {

            // TODO continue Dijkstra's algorithm to process nextDspNode
            //  mark nodes as you complete their processing
            //  register all visited vertices while going for statistical purposes
            //  if you hit the target: complete the path and bail out !!!






            // TODO find the next nearest node that is not marked yet

        }

        // no path found, graph was not connected ???
        return null;
    }


    @Override
    public String toString() {
        return this.getVertices().stream()
                .map(v -> v.toString() + ": " +
                        this.edges.get(v).entrySet().stream()
                                .map(e -> e.getKey().toString() + "(" + e.getValue().toString() + ")")
                                .collect(Collectors.joining(",","[","]"))
                        )
                .collect(Collectors.joining(",\n  ","{ ","\n}"));
    }
}
