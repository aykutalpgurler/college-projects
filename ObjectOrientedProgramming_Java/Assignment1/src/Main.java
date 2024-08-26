import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        writeToFile("output.txt", "", false, false); // Initialize empty output.txt

        String[] boardTxt = readFile(args[0], true, true);
        String[] moveTxt = readFile(args[1], true, true);

        String[][] boardData = initializeBoard(boardTxt);
        String[] moves = initializeMoves(moveTxt);

        int[] whitePosition = initialWhitePosition(boardData);
        int totalPoint = 0; // to keep track of total point.
        boolean gameOver = false; // to control if ball dropped to hole or not.

        writeToFile("output.txt", "Game board:", true, true);
        for (String[] row : boardData) {
            for (String character : row) {
                writeToFile("output.txt", character + " ", true, false); // write the current character to the file followed by a space
            }
            writeToFile("output.txt", "", true, true); // write a new line character to the file after each row is processed
        }

        writeToFile("output.txt", "", true, true);
        writeToFile("output.txt", "Your movement is:", true, true);

        for (String move : moves) {
            if (!gameOver) {
                writeToFile("output.txt", move + " ", true, false);
                switch (move) {
                    case "L":
                        int[] previousPosition = whitePosition.clone(); // Create a copy of the current position of the white piece
                        goLeft(whitePosition, boardData); // Move the white piece to the left
                        switch (boardData[whitePosition[0]][whitePosition[1]]) { // Check the new position of the white piece and update the board and score accordingly
                            case "R":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint += 10;
                                break;
                            case "Y":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint += 5;
                                break;
                            case "B":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint -= 5;
                                break;
                            case "H":
                                boardData[previousPosition[0]][previousPosition[1]] = " ";
                                boardData[whitePosition[0]][whitePosition[1]] = "H";
                                gameOver = true; // Set the game over flag to true if the white piece drops to hole.
                                break;
                            case "W":
                                // If the white piece hits to a wall, move it two more spaces to the right
                                goRight(whitePosition, boardData);
                                goRight(whitePosition, boardData);
                                switch (boardData[whitePosition[0]][whitePosition[1]]) {
                                    case "R":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint += 10;
                                        break;
                                    case "Y":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint += 5;
                                        break;
                                    case "B":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint -= 5;
                                        break;
                                    case "H":
                                        boardData[previousPosition[0]][previousPosition[1]] = " ";
                                        boardData[whitePosition[0]][whitePosition[1]] = "H";
                                        gameOver = true;
                                        break;
                                    default:
                                        boardData[previousPosition[0]][previousPosition[1]] = boardData[whitePosition[0]][whitePosition[1]];
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        break;
                                }
                                break;
                            default:
                                boardData[previousPosition[0]][previousPosition[1]] = boardData[whitePosition[0]][whitePosition[1]];
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                break;
                        }
                        break;
                    case "R":
                        previousPosition = whitePosition.clone();
                        goRight(whitePosition, boardData);
                        switch (boardData[whitePosition[0]][whitePosition[1]]) {
                            case "R":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint += 10;
                                break;
                            case "B":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint -= 5;
                                break;
                            case "Y":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint += 5;
                                break;
                            case "H":
                                boardData[previousPosition[0]][previousPosition[1]] = " ";
                                boardData[whitePosition[0]][whitePosition[1]] = "H";
                                gameOver = true;
                                break;
                            case "W":
                                goLeft(whitePosition, boardData);
                                goLeft(whitePosition, boardData);
                                switch (boardData[whitePosition[0]][whitePosition[1]]) {
                                    case "R":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint += 10;
                                        break;
                                    case "Y":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint += 5;
                                        break;
                                    case "B":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint -= 5;
                                        break;
                                    case "H":
                                        boardData[previousPosition[0]][previousPosition[1]] = " ";
                                        boardData[whitePosition[0]][whitePosition[1]] = "H";
                                        gameOver = true;
                                        break;
                                    default:
                                        boardData[previousPosition[0]][previousPosition[1]] = boardData[whitePosition[0]][whitePosition[1]];
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        break;
                                }
                                break;
                            default:
                                boardData[previousPosition[0]][previousPosition[1]] = boardData[whitePosition[0]][whitePosition[1]];
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                break;
                        }
                        break;
                    case "U":
                        previousPosition = whitePosition.clone();
                        goUp(whitePosition, boardData);
                        switch (boardData[whitePosition[0]][whitePosition[1]]) {
                            case "R":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint += 10;
                                break;
                            case "B":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint -= 5;
                                break;
                            case "Y":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint += 5;
                                break;
                            case "H":
                                boardData[previousPosition[0]][previousPosition[1]] = " ";
                                boardData[whitePosition[0]][whitePosition[1]] = "H";
                                gameOver = true;
                                break;
                            case "W":
                                goDown(whitePosition, boardData);
                                goDown(whitePosition, boardData);
                                switch (boardData[whitePosition[0]][whitePosition[1]]) {
                                    case "R":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint += 10;
                                        break;
                                    case "Y":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint += 5;
                                        break;
                                    case "B":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint -= 5;
                                        break;
                                    case "H":
                                        boardData[previousPosition[0]][previousPosition[1]] = " ";
                                        boardData[whitePosition[0]][whitePosition[1]] = "H";
                                        gameOver = true;
                                        break;
                                    default:
                                        boardData[previousPosition[0]][previousPosition[1]] = boardData[whitePosition[0]][whitePosition[1]];
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        break;
                                }
                                break;
                            default:
                                boardData[previousPosition[0]][previousPosition[1]] = boardData[whitePosition[0]][whitePosition[1]];
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                break;
                        }
                        break;
                    case "D":
                        previousPosition = whitePosition.clone();
                        goDown(whitePosition, boardData);
                        switch (boardData[whitePosition[0]][whitePosition[1]]) {
                            case "R":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint += 10;
                                break;
                            case "B":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint -= 5;
                                break;
                            case "Y":
                                boardData[previousPosition[0]][previousPosition[1]] = "X";
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                totalPoint += 5;
                                break;
                            case "H":
                                boardData[previousPosition[0]][previousPosition[1]] = " ";
                                boardData[whitePosition[0]][whitePosition[1]] = "H";
                                gameOver = true;
                                break;
                            case "W":
                                goUp(whitePosition, boardData);
                                goUp(whitePosition, boardData);
                                switch (boardData[whitePosition[0]][whitePosition[1]]) {
                                    case "R":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint += 10;
                                        break;
                                    case "Y":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint += 5;
                                        break;
                                    case "B":
                                        boardData[previousPosition[0]][previousPosition[1]] = "X";
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        totalPoint -= 5;
                                        break;
                                    case "H":
                                        boardData[previousPosition[0]][previousPosition[1]] = " ";
                                        boardData[whitePosition[0]][whitePosition[1]] = "H";
                                        gameOver = true;
                                        break;
                                    default:
                                        boardData[previousPosition[0]][previousPosition[1]] = boardData[whitePosition[0]][whitePosition[1]];
                                        boardData[whitePosition[0]][whitePosition[1]] = "*";
                                        break;
                                }
                                break;
                            default:
                                boardData[previousPosition[0]][previousPosition[1]] = boardData[whitePosition[0]][whitePosition[1]];
                                boardData[whitePosition[0]][whitePosition[1]] = "*";
                                break;
                        }
                        break;
                    default:
                        System.out.println("BAD");
                }
            } else {
                break;
            }
        }

        writeToFile("output.txt", "", true, true);
        writeToFile("output.txt", "", true, true);
        writeToFile("output.txt", "Your output is:", true, true);
        for (String[] row : boardData) {
            for (String character : row) {
                writeToFile("output.txt", character + " ", true, false);
            }
            writeToFile("output.txt", "", true, true);
        }

        writeToFile("output.txt", "", true, true);
        if (gameOver) {
            writeToFile("output.txt", "Game Over!", true, true);
        }
        writeToFile("output.txt", "Score: " + totalPoint, true, true);
    }

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

    public static void writeToFile(String path, String content, boolean append, boolean newLine) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path, append));
            ps.print(content + (newLine ? "\n" : ""));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) { //Flushes all the content and closes the stream if it has been successfully created.
                ps.flush();
                ps.close();
            }
        }
    }

    /**
     * This method takes in an array of strings representing the board data and returns a 2D array of strings
     * representing the board.
     *
     * @param data an array of strings representing the board data
     * @return a 2D array of strings representing the board
     */
    public static String[][] initializeBoard(String[] data) {

        // create a 2D array of strings to represent the board
        String[][] boardArr = new String[data.length][data[0].split(" ").length];

        // loop through each string in the input array and split it into individual elements
        for (int i = 0; i < data.length; i++) {
            boardArr[i] = data[i].split(" ");
        }

        return boardArr;
    }

    /**
     * This method takes in an array of strings representing the moves data and returns an array of strings
     * representing the moves.
     *
     * @param data an array of strings representing the moves data
     * @return an array of strings representing the moves
     */
    public static String[] initializeMoves(String[] data) {
        // split the first string in the input array into individual elements
        // to obtain the moves data and return the resulting array of strings
        return data[0].split(" ");
    }

    /**
     * This method takes in a 2D array of strings as input and returns an integer array
     * representing the row and column position of the first occurrence of the string "*".
     *
     * @param data a 2D array of strings representing the board state
     * @return an integer array representing the row and column position of the first occurrence of "*"
     */
    public static int[] initialWhitePosition(String[][] data) {

        int i = 0; // variable to keep track of the row index
        int j = 0; // variable to keep track of the column index

        for (String[] line : data) {
            j = 0; // reset column index for each row
            for (String position : line) {
                if (position.equals("*")) {
                    return new int[]{i, j}; // return the row and column index as an array
                }
                j++;
            }
            i++;
        }
        return new int[]{i, j};
    }

    /**
     * This method takes in an integer array representing a position and a 2D array of strings
     * representing a board, and updates the position to move left on the board.
     *
     * @param position an integer array representing the current position on the board
     * @param board a 2D array of strings representing the board
     */
    public static void goLeft(int[] position, String[][] board) {

        if (position[1] == 0) { // check if current column is already at the leftmost position
            position[1] = board[0].length - 1; // if so, wrap around to the rightmost position
        } else {
            position[1] = position[1] - 1; // if not, move left by decrementing the column index
        }
    }

    public static void goRight(int[] position, String[][] board) {

        if (position[1] == board[0].length - 1) {
            position[1] = 0;
        } else {
            position[1] = position[1] + 1;
        }
    }

    public static void goUp(int[] position, String[][] board) {

        if (position[0] == 0) {
            position[0] = board.length - 1;
        } else {
            position[0] = position[0] - 1;
        }
    }

    public static void goDown(int[] position, String[][] board) {

        if (position[0] == board.length - 1) {
            position[0] = 0;
        } else {
            position[0] = position[0] + 1;
        }
    }
}