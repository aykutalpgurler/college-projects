import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;

    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        Map<String, Station> descToStation = createDescToStationMap(network);
        Set<Station> allStations = new HashSet<>(descToStation.values());
        Map<Station, List<RouteDirection>> graph = createGraph(network, allStations);

        Map<Station, Double> distances = new HashMap<>();
        Map<Station, RouteDirection> prev = new HashMap<>();
        dijkstra(network, graph, descToStation, distances, prev);

        return buildRouteDirections(network, descToStation, prev);
    }

    private Map<String, Station> createDescToStationMap(HyperloopTrainNetwork network) {
        Map<String, Station> descToStation = new HashMap<>();
        network.lines.forEach(line -> line.trainLineStations.forEach(station -> descToStation.put(station.description, station)));
        descToStation.put(network.startPoint.description, network.startPoint);
        descToStation.put(network.destinationPoint.description, network.destinationPoint);
        return descToStation;
    }

    private Map<Station, List<RouteDirection>> createGraph(HyperloopTrainNetwork network, Set<Station> allStations) {
        Map<Station, List<RouteDirection>> graph = new HashMap<>();
        network.lines.forEach(line -> {
            for (int i = 0; i < line.trainLineStations.size() - 1; i++) {
                Station current = line.trainLineStations.get(i);
                Station next = line.trainLineStations.get(i + 1);
                addRouteDirection(graph, current, next, network.averageTrainSpeed, true);
                addRouteDirection(graph, next, current, network.averageTrainSpeed, true);
            }
        });
        allStations.forEach(from -> allStations.forEach(to -> {
            if (!from.equals(to)) {
                addRouteDirection(graph, from, to, network.averageWalkingSpeed, false);
            }
        }));
        return graph;
    }

    private void addRouteDirection(Map<Station, List<RouteDirection>> graph, Station from, Station to, double speed, boolean isTrainRide) {
        graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new RouteDirection(from.description, to.description,
                calculateTime(from.coordinates, to.coordinates, speed), isTrainRide));
    }

    private void dijkstra(HyperloopTrainNetwork network, Map<Station, List<RouteDirection>> graph, Map<String, Station> descToStation, Map<Station, Double> distances, Map<Station, RouteDirection> prev) {
        PriorityQueue<Station> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        Station start = network.startPoint;
        distances.put(start, 0.0);
        pq.add(start);
        while (!pq.isEmpty()) {
            Station current = pq.poll();
            if (current.equals(network.destinationPoint)) {
                break;
            }
            List<RouteDirection> directions = graph.getOrDefault(current, new ArrayList<>());
            directions.forEach(direction -> {
                Station neighbor = descToStation.get(direction.endStationName);
                double newDist = distances.get(current) + direction.duration;
                if (newDist < distances.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    distances.put(neighbor, newDist);
                    prev.put(neighbor, direction);
                    pq.add(neighbor);
                }
            });
        }
    }

    private List<RouteDirection> buildRouteDirections(HyperloopTrainNetwork network, Map<String, Station> descToStation, Map<Station, RouteDirection> prev) {
        List<RouteDirection> routeDirections = new ArrayList<>();
        Station step = network.destinationPoint;
        while (prev.containsKey(step)) {
            routeDirections.add(prev.get(step));
            step = descToStation.get(prev.get(step).startStationName);
        }
        Collections.reverse(routeDirections);
        return routeDirections;
    }

    private double calculateTime(Point a, Point b, double speed) {
        double distance = Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
        return (distance / speed);
    }

    public void printRouteDirections(List<RouteDirection> directions) {
        if (directions.isEmpty()) {
            System.out.println("No available route.");
            return;
        }

        double totalTime = directions.stream()
                .mapToDouble(direction -> direction.trainRide ? direction.duration * 60 / 1000 : direction.duration)
                .sum();

        System.out.printf("The fastest route takes %.0f minute(s).\n", totalTime);
        System.out.println("Directions");
        System.out.println("----------");

        int step = 1;
        for (RouteDirection direction : directions) {
            String mode = direction.trainRide ? "Get on the train" : "Walk";
            double duration = direction.trainRide ? direction.duration * 60 / 1000 : direction.duration;
            System.out.printf("%d. %s from \"%s\" to \"%s\" for %.2f minutes.\n", step++, mode, direction.startStationName, direction.endStationName, duration);
        }
    }
}
