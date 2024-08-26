import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileInput {
    /**
     * Reads the file at the given path and returns contents of it in a string array.
     *
     * @param path              Path to the file that is going to be read.
     * @param discardEmptyLines If true, discards empty lines with respect to trim; else, it takes all the lines from the file.
     * @param trim              Trim status; if true, trims (strip in Python) each line; else, it leaves each line as-is.
     * @return Contents of the file as a string array, returns null if there is not such a file or this program does not have sufficient permissions to read that file.
     */
    public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path)); //Gets the content of file to the list.
            if (discardEmptyLines) { //Removes the lines that are empty with respect to trim.
                lines.removeIf(line -> line.trim().equals(""));
            }
            if (trim) { //Trims each line.
                lines.replaceAll(String::trim);
            }
            return lines.toArray(new String[0]);
        } catch (IOException e) { //Returns null if there is no such a file.
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a 2D array of strings from an array of strings using the specified splitter to split each string into elements.
     *
     * @param data     The array of strings to convert to a 2D array.
     * @param splitter The splitter to use to split each string into elements.
     * @return A 2D array of strings representing the input array, with each string split into elements using the splitter.
     */
    public static String[][] create2DInputArray(String[] data, String splitter) {
        // create a 2D array of strings to represent the command lines.
        String[][] inputArray = new String[data.length][data[0].split(splitter).length];
        // loop through each string in the input array and split it into individual elements
        for (int i = 0; i < data.length; i++) {
            inputArray[i] = data[i].split(splitter);
        }
        return inputArray;
    }

    public static ArrayList<Integer> createDemandSchedule(String[] data) {
        String line = data[0];
        String[] demands = line.split(" ");
        ArrayList<Integer> demandSchedule = new ArrayList<>();
        for (String demand : demands) {
            demandSchedule.add(Integer.parseInt(demand));
        }
        return demandSchedule;
    }

    public static ArrayList<Integer> createEnergyDemands(String[] data) {
        ArrayList<Integer> demandSchedule = new ArrayList<>();
        for (String demand : data) {
            demandSchedule.add(Integer.parseInt(demand));
        }
        return demandSchedule;
    }
}