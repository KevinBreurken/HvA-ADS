package route_planner;

public class RoutePlannerMain {

    public static void main(String[] args) {
        System.out.println("Welcome to the HvA RoutePlanner");

        // load the small map from csv files
        RoadMap.reSeedRandomizer(20211220L);
        RoadMap roadMap0 = new RoadMap("Junctions0.csv", "Roads0.csv");
        System.out.println(roadMap0);

        // provide a map into the target classpath
        roadMap0.svgDrawMap("RoadmapAMS.svg", null);

        // Run various types of searches
        doPathSearches(roadMap0, "Oostzaan", "Ouder-Amstel");

        // load the complete map from csv files
        RoadMap.reSeedRandomizer(20211220L);
        RoadMap roadMap = new RoadMap("Junctions.csv", "Roads.csv");

        // provide a map into the target classpath
        roadMap.svgDrawMap("RoadmapNL.svg", null);

        // Run various types of searches
        final String FROM_ID = "Amsterdam";
        //final String TO_ID = "Staphorst";
        final String TO_ID = "Meppel";
        doPathSearches(roadMap, FROM_ID, TO_ID);

        // now we have an accident between Diemen and Weesp...
        // TODO change the roadMap such that max average speed from Diemen to Weesp is only 5 km/h


        // find the fastest route avoiding the accident
        RoadMap.DGPath path =
                roadMap.dijkstraShortestPath(FROM_ID, TO_ID,
                        // TODO provide an edgeWeightCalculator that yields the expected travel time for the road

                        null
                );
        System.out.println("Dijkstra-accident-Weesp: " + path);
        roadMap.svgDrawMap(String.format("DSPACC-%s-%s.svg", FROM_ID, TO_ID), path);
    }

    private static void doPathSearches(RoadMap roadMap, String fromId, String toId) {
        System.out.printf("\nResults from path searches from %s to %s:\n", fromId, toId);
        RoadMap.DGPath path;

        // find the routes by depth-first-search
        path = roadMap.depthFirstSearch(fromId, toId);
        System.out.println("Depth-first-search: " + path);
        roadMap.svgDrawMap(String.format("DFS-%s-%s.svg", fromId, toId), path);
        path = roadMap.depthFirstSearch(toId, fromId);
        System.out.println("Depth-first-search return: " + path);

        // find the routes by breadth-first-search with minimum number of hops
        path = roadMap.breadthFirstSearch(fromId, toId);
        System.out.println("Breadth-first-search: " + path);
        roadMap.svgDrawMap(String.format("BFS-%s-%s.svg", fromId, toId), path);
        path = roadMap.breadthFirstSearch(toId, fromId);
        System.out.println("Breadth-first-search return: " + path);

        // find the routes by dijkstra-Shortest-Path with minimum total length
        path = roadMap.dijkstraShortestPath(fromId, toId,
                // TODO provide an edgeWeightCalculator that yields the travel distance for the road

                null
        );
        System.out.println("Dijkstra-Shortest-Path: " + path);
        roadMap.svgDrawMap(String.format("DSP-%s-%s.svg", fromId, toId), path);
        path = roadMap.dijkstraShortestPath(toId, fromId,
                // TODO provide the same edgeWeightCalculator as above

                null
        );
        System.out.println("Dijkstra-Shortest-Path return: " + path);

        // find the routes by dijkstra-Shortest-Path with minimum total travel time
        path = roadMap.dijkstraShortestPath(fromId, toId,
                // TODO provide an edgeWeightCalculator that yields the expected travel time for the road

                null
        );
        System.out.println("Dijkstra-Fastest-Route: " + path);
        roadMap.svgDrawMap(String.format("DFR-%s-%s.svg", fromId, toId), path);

        System.out.println();
    }
}
