package graphs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectedGraphTest {

    Country nl, be, de, lux, fr, uk, ro, hu;
    DirectedGraph<Country, Integer> europe = new DirectedGraph<>();
    DirectedGraph<Country, Integer> africa = new DirectedGraph<>();

    @BeforeEach
    void setUp() {
        nl = this.europe.addOrGetVertex(new Country("NL"));
        be = this.europe.addOrGetVertex(new Country("BE"));
        this.europe.addConnection("BE","NL", 100);
        de = this.europe.addOrGetVertex(new Country("DE"));
        this.europe.addConnection("NL","DE", 200);
        this.europe.addConnection("BE","DE", 30);
        lux = this.europe.addOrGetVertex(new Country("LUX"));
        this.europe.addConnection("LUX","BE", 60);
        this.europe.addConnection("LUX","DE", 50);
        fr = this.europe.addOrGetVertex(new Country("FR"));
        this.europe.addConnection("FR","LUX", 30);
        this.europe.addConnection("FR","BE", 110);
        this.europe.addConnection("FR","DE", 50);

        uk = this.europe.addOrGetVertex(new Country("UK"));
        this.europe.addConnection("UK","BE", 70);
        this.europe.addConnection("UK","FR", 150);
        this.europe.addConnection("UK","NL", 250);

        ro = this.europe.addOrGetVertex(new Country("RO"));
        hu = this.europe.addOrGetVertex(new Country("HU"));
        this.europe.addConnection("RO","HU", 250);
    }

    @AfterEach
    void checkRepresentationInvariants() {
        assertEquals(8, europe.getNumVertices());
        assertEquals(24, europe.getNumEdges());
        for (Country from: europe.getVertices()) {
            for (Country to: europe.getNeighbours(from)) {
                assertSame(europe.getEdge(from,to), europe.getEdge(to,from),
                        "Border between two countries should be the same object instance");
            }
        }
    }

    @Test
    void checkGetVertexById() {
        assertEquals(nl, europe.getVertexById("NL"));
        assertEquals(be, europe.getVertexById("BE"));
        assertNull(europe.getVertexById("XX"));
        assertNull(africa.getVertexById("XX"));
    }

    @Test
    void checkAddOrGetVertex() {
        int oldNumV = europe.getNumVertices();
        int oldNumE = europe.getNumEdges();
        assertSame(nl, europe.addOrGetVertex(new Country("NL")));
        assertSame(lux, europe.addOrGetVertex(new Country("LUX")));

        assertEquals(oldNumV, europe.getNumVertices());
        assertEquals(oldNumE, europe.getNumEdges());
    }

    @Test
    void checkAddEdge() {
        assertEquals(0, africa.getNumVertices());
        assertEquals(0, africa.getNumEdges());

        assertTrue(africa.addEdge(new Country("MO"), new Country("AL"), 200));
        assertEquals(2, africa.getNumVertices());
        assertEquals(1, africa.getNumEdges());
        assertFalse(africa.addEdge(new Country("MO"), new Country("AL"), 300));
        assertFalse(africa.addEdge("MO", "AL", 300) );
        assertFalse(africa.addEdge("MO", "XX", 300) );
        assertFalse(africa.addEdge("XX", "AL", 300) );

        assertTrue(africa.addEdge("AL", "MO", 200));
        assertFalse(africa.addEdge("AL", "MO", 200));
        assertEquals(2, africa.getNumVertices());
        assertEquals(2, africa.getNumEdges());
    }

    @Test
    void checkBorderLength() {
        assertEquals(550, europe.getEdges("NL").stream().reduce(Integer::sum).orElse(0));
        assertEquals(370, europe.getEdges("BE").stream().reduce(Integer::sum).orElse(0));
    }

    @Test
    void checkDFSearch() {
        DirectedGraph<Country, Integer>.DGPath path = europe.depthFirstSearch("UK","LUX");
        assertNotNull(path);
        assertSame(europe.getVertexById("UK"), path.getVertices().peek());
        assertTrue(path.getVertices().size() >= 3);
        assertTrue(path.getVisited().size() >= path.getVertices().size());
    }

    @Test
    void checkDFSearchStartIsTarget() {
        DirectedGraph<Country, Integer>.DGPath path = europe.depthFirstSearch("HU","HU");
        assertNotNull(path);
        assertSame(europe.getVertexById("HU"), path.getVertices().peek());
        assertEquals(1, path.getVertices().size());
        assertEquals(1, path.getVisited().size());
    }

    @Test
    void checkDFSearchUnconnected() {
        DirectedGraph<Country, Integer>.DGPath path = europe.depthFirstSearch("UK","HU");
        assertNull(path);
    }

    @Test
    void checkBFSearch() {
        DirectedGraph<Country, Integer>.DGPath path = europe.breadthFirstSearch("UK","LUX");
        assertNotNull(path);
        assertSame(europe.getVertexById("UK"), path.getVertices().peek());
        assertEquals(3, path.getVertices().size());
        assertTrue(path.getVisited().size() >= path.getVertices().size());
    }

    @Test
    void checkBFSearchStartIsTarget() {
        DirectedGraph<Country, Integer>.DGPath path = europe.breadthFirstSearch("HU","HU");
        assertNotNull(path);
        assertSame(europe.getVertexById("HU"), path.getVertices().peek());
        assertEquals(1, path.getVertices().size());
        assertEquals(1, path.getVisited().size());
    }

    @Test
    void checkBFSearchUnconnected() {
        DirectedGraph<Country, Integer>.DGPath path = europe.breadthFirstSearch("UK","HU");
        assertNull(path);
    }

    @Test
    void checkDSPSearch() {
        DirectedGraph<Country, Integer>.DGPath path = europe.dijkstraShortestPath("UK", "LUX", b -> 2.0);
        assertNotNull(path);
        assertSame(europe.getVertexById("UK"), path.getVertices().peek());
        assertEquals(4.0, path.getTotalWeight(), 0.0001);
        assertEquals(path.getTotalWeight(), 2.0 * (path.getVertices().size()-1), 0.0001);
        assertTrue(path.getVisited().size() >= path.getVertices().size());
    }

    @Test
    void checkDSPSearchStartIsTarget() {
        DirectedGraph<Country, Integer>.DGPath path = europe.dijkstraShortestPath("HU", "HU", b -> 2.0);
        assertNotNull(path);
        assertEquals(europe.getVertexById("HU"), path.getVertices().peek());
        assertEquals(0.0, path.getTotalWeight(), 0.0001);
        assertEquals(1, path.getVertices().size());
        assertEquals(1, path.getVisited().size());
    }

    @Test
    void checkDSPSearchUnconnected() {
        DirectedGraph<Country, Integer>.DGPath path = europe.dijkstraShortestPath("UK", "HU", b -> 2.0);
        assertNull(path);
    }
}