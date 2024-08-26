import java.util.ArrayList;
import java.util.Collections;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */
public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour){
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }
    /**
     *     Function to implement the given dynamic programming algorithm
     *     SOL(0) <- 0
     *     HOURS(0) <- [ ]
     *     For{j <- 1...N}
     *         SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     *         HOURS(j) <- [HOURS(i), j]
     *     EndFor
     *
     * @return OptimalPowerGridSolution
     */
    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP(){
        // TODO: YOUR CODE HERE

        int N = amountOfEnergyDemandsArrivingPerHour.size();

        int[] solution = new int[N + 1];
        solution[0] = 0;

        ArrayList<ArrayList<Integer>> hours = new ArrayList<>(N + 1);
        hours.add(0, new ArrayList<>());

        for (int j = 1; j <= N; j++) {
            int i;

            ArrayList<Integer> solutions = new ArrayList<>();
            for (i = 0; i < j; i++) {
                solutions.add(i, solution[i] + Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), ((j - i) * (j - i))));
            }
            int max = Collections.max(solutions);

            solution[j] = max;
            int maxIdx = solutions.indexOf(max);
            ArrayList<Integer> optimalHours = new ArrayList<>(hours.get(maxIdx));
            optimalHours.add(j);
            hours.add(optimalHours);
        }

        OptimalPowerGridSolution opgs = new OptimalPowerGridSolution(solution[N], hours.get(N));
        return opgs;
    }
}
