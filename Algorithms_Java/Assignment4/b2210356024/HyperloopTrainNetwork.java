import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class HyperloopTrainNetwork implements Serializable {
    static final long serialVersionUID = 11L;
    public double averageTrainSpeed;
    public final double averageWalkingSpeed = 1000 / 6.0;
    public int numTrainLines;
    public Station startPoint;
    public Station destinationPoint;
    public List<TrainLine> lines;

    public int getIntVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Integer.parseInt(m.group(1));
    }

    public String getStringVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\"([^\"]+)\"");
        Matcher m = p.matcher(fileContent);
        m.find();
        return m.group(1);
    }

    public Double getDoubleVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]*\\.?[0-9]+)");
        Matcher m = p.matcher(fileContent);
        m.find();
        return Double.parseDouble(m.group(1));
    }

    public Point getPointVar(String varName, String fileContent) {
        Pattern p = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*\\(([\\t ]*[0-9]+[\\t ]*)[\\t ]*,[\\t ]*([\\t ]*[0-9]+[\\t ]*)\\)");
        Matcher m = p.matcher(fileContent);
        m.find();
        int x = Integer.parseInt(m.group(1).trim());
        int y = Integer.parseInt(m.group(2).trim());
        return new Point(x, y);
    }

    public List<TrainLine> getTrainLines(String fileContent) {
        List<TrainLine> trainLines = new ArrayList<>();
        Pattern linePattern = Pattern.compile(
                "train_line_name[\\t ]*=[\\t ]*\"([^\"]+)\"[\\t ]*\\r?\\n" +
                        "train_line_stations[\\t ]*=[\\t ]*((\\([\\t ]*[0-9]+[\\t ]*,[\\t ]*[0-9]+[\\t ]*\\)[\\t ]*)+)"
        );
        Matcher lineMatcher = linePattern.matcher(fileContent);

        while (lineMatcher.find()) {
            String lineName = lineMatcher.group(1);
            String stationsString = lineMatcher.group(2);

            List<Station> stations = new ArrayList<>();
            Pattern stationPattern = Pattern.compile("\\(([\\t ]*[0-9]+[\\t ]*),([\\t ]*[0-9]+[\\t ]*)\\)");
            Matcher stationMatcher = stationPattern.matcher(stationsString);

            while (stationMatcher.find()) {
                int x = Integer.parseInt(stationMatcher.group(1).trim());
                int y = Integer.parseInt(stationMatcher.group(2).trim());
                stations.add(new Station(new Point(x, y), "Station at (" + x + ", " + y + ")"));
            }
            trainLines.add(new TrainLine(lineName, stations));
        }
        return trainLines;
    }



    public void readInput(String filename) {
        String fileContent = "";
        try {
            fileContent = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.numTrainLines = getIntVar("num_train_lines", fileContent);
        this.startPoint = new Station(getPointVar("starting_point", fileContent), "Start Point");
        this.destinationPoint = new Station(getPointVar("destination_point", fileContent), "Destination Point");
        this.averageTrainSpeed = getDoubleVar("average_train_speed", fileContent);
        this.lines = getTrainLines(fileContent);
//        System.out.println(numTrainLines);
//        System.out.println(startPoint);
//        System.out.println(destinationPoint);
//        System.out.println(averageTrainSpeed);
//        System.out.println(lines.get(0).trainLineStations.toString());
    }
}
