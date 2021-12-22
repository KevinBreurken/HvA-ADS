package route_planner;

import graphs.DirectedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class RoadMap extends DirectedGraph<Junction, Road> {

    public RoadMap(String junctionsResource, String roadsResource) {
        System.out.printf("\nImporting junctions and roads from %s and %s...\n",
                junctionsResource, roadsResource);;
        int nJunctions = this.importJunctions(junctionsResource);
        int nRoads = this.importRoads(roadsResource);
        this.removeUnconnectedVertices();
        System.out.printf("%d junctions and %d bi-directional roads have been imported.\n",
                nJunctions, nRoads);
        System.out.printf("%d junctions and %d one-way roads have been stored into the graph.\n",
                this.getNumVertices(), this.getNumEdges());
    }

    /**
     * the randomizor is used to generate missing authentic data about the fysical length of roads.
     * Repreducible results of calculations can be obtained by fixing the seed of the randomizer
     */
    public static Random randomizer = new Random();
    public static void reSeedRandomizer(long seed) {
        randomizer = new Random(seed);
    }

    private static final String DELIMITER = ";";

    /**
     * imports a list of junctions from a resource file in the project.
     * imports name, x-coordinate, y-coordinate and population at the junction
     * uses the Dutch RD-coordinate system measured in km (see https://nl.wikipedia.org/wiki/Rijksdriehoeksco%C3%B6rdinaten)
     * @param resourceName
     * @return
     */
    public int importJunctions(String resourceName) {
        if (resourceName == null) return 0;
        int numLoaded = 0;

        Scanner scanner = new Scanner(
                RoadMap.class.getClassLoader().getResourceAsStream(resourceName));

        scanner.useDelimiter(DELIMITER);
        scanner.useLocale(Locale.ENGLISH);

        // skip header line
        String header = scanner.nextLine();
        // System.out.println(header);

        while (scanner.hasNext()) {
            Junction junction = new Junction();
            scanner.nextInt(); // skip code
            junction.setName(scanner.next().trim());
            junction.setLocationX(scanner.nextDouble());
            junction.setLocationY(scanner.nextDouble());
            scanner.next(); // skip province
            junction.setPopulation(scanner.nextInt());

            // add the junction to the DirectedGraph data structure
            this.addOrGetVertex(junction);
            numLoaded++;
            scanner.nextLine();
        }

        return numLoaded;
    }

    /**
     * imports a list of road segments from a resource file in the project.
     * imports name, maxSpeed and junction names that are to be connected by road segments.
     * creates two road segments for each (bi-directional) junction pair
     * (The resource files do not provide specific directional information about the roads.
     * Junction names must be configured into the DirectedGraph before roads can be loaded.
     * @param resourceName
     * @return
     */
    public int importRoads(String resourceName) {
        if (resourceName == null) return 0;
        int numLoaded = 0;

        Scanner scanner = new Scanner(
                RoadMap.class.getClassLoader().getResourceAsStream(resourceName));

        scanner.useDelimiter(DELIMITER);
        scanner.useLocale(Locale.ENGLISH);

        // skip header line
        String header = scanner.nextLine();
        // System.out.println(header);

        while (scanner.hasNext()) {
            // road name
            String name = scanner.next().trim();
            int speedlimit = scanner.nextInt();

            // first junction name: the start of the road
            String jName = scanner.next().trim();
            // retrieve the associated junction from the DirectedGraph data structure
            Junction prevJunction = getVertexById(jName);

            // process the list of junctions connected by this road
            while (scanner.hasNext() && prevJunction != null) {
                // next junction
                jName = scanner.next().trim();
                if (jName == null || jName.isEmpty()) {
                    break;
                }
                Junction nextJunction = getVertexById(jName);

                // configure two road segments for this connection, in opposite directions
                if (nextJunction != null) {
                    // add the road segments to the DirectedGraph data structures
                    double distanceMultiplier = 1.05 + 0.1 * randomizer.nextDouble();
                    Road road = new Road(name, distanceMultiplier * prevJunction.getDistance(nextJunction), speedlimit);
                    addEdge(prevJunction, nextJunction, road);
                    addEdge(nextJunction, prevJunction, new Road(road));
                    numLoaded++;
                    prevJunction = nextJunction;
                }
            }
            scanner.nextLine();
        }

        return numLoaded;
    }

    /**
     * produces an .svg file in the target classpath folder, which depicts the roadMap and the optional path
     * .svg files can be viewed with a regular browser
     * @param resourceName  name of the file to be generated
     * @param path          optional search path with visited vertices to be coloured into the map
     */
    public void svgDrawMap(String resourceName, DGPath path) {
        try {
            //Path resources = Paths.get(this.getClass().getResource("/").getPath());
            //String svgPath = resources.toAbsolutePath() + "/" + resourceName;
            String svgPath = new File(getClass().getResource("/").getPath()).getAbsolutePath() +
                    "/" + resourceName;
            PrintStream svgWriter = new PrintStream(svgPath);

            // header for an .svg file
            svgWriter.println("<?xml version='1.0' standalone='no'?>");
            // configure the viewBox to match the coordinate ranges of the Dutch RD-coordinate system
            svgWriter.printf("<svg width='20cm' height='30cm' viewBox='%d %d %d %d' preserveAspectRatio='xMidYMin'\n",
                    0, -625, 300, 300);
            svgWriter.println("     version='1.1' xmlns='http://www.w3.org/2000/svg'>");

            // first draw all road segments with their default colour
            for (Junction junction: getVertices()) {
                this.svgDrawRoads(svgWriter, junction);
            }

            // on top of that, draw all junctions with a colour depending on their status in the optional path
            for (Junction junction: getVertices()) {
                junction.svgDraw(svgWriter,
                        path != null && path.getVisited().contains(junction) ? "yellowgreen" : "black");
            }

            // on top of that, highlight the path, if any has been provided
            if (path != null) {
                Junction from = null;
                for (Junction neighbour: path.getVertices()) {
                    neighbour.svgDrawRoad(svgWriter, from, 1.25, "lime");
                    from = neighbour;
                }
            }

            svgWriter.println("</svg>");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * draws all road segments starting from the junction with their default colour
     * @param svgWriter
     */
    private void svgDrawRoads(PrintStream svgWriter, Junction from) {
        for (Junction neighbour : this.getNeighbours(from)) {
            Road road = this.getEdge(from, neighbour);
            String colour = (road.getMaxSpeed() >= 100 ? "darkorange" :
                    road.getMaxSpeed() >= 80 ? "gold" : "lightskyblue");
            double width = 0.2 + road.getMaxSpeed() * 0.008;
            neighbour.svgDrawRoad(svgWriter, from, width, colour);
        }
    }

    @Override
    public String toString() {
        return "Roadmap lay-out:\n" + super.toString();
    }
}
