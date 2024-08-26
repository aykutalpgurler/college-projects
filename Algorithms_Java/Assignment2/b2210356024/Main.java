import java.io.IOException;
import java.util.ArrayList;

/**
 * Main class
 */
// FREE CODE HERE
public class Main {
    public static void main(String[] args) throws IOException {

        /** MISSION POWER GRID OPTIMIZATION BELOW **/

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        // TODO: Your code goes here
        // You are expected to read the file given as the first command-line argument to read
        // the energy demands arriving per hour. Then, use this data to instantiate a
        // PowerGridOptimization object. You need to call getOptimalPowerGridSolutionDP() method
        // of your PowerGridOptimization object to get the solution, and finally print it to STDOUT.
        ArrayList<Integer> demandSchedule = FileInput.createDemandSchedule(FileInput.readFile(args[0], true, true));
        PowerGridOptimization powerGridOptimization = new PowerGridOptimization(demandSchedule);
        OptimalPowerGridSolution solution = powerGridOptimization.getOptimalPowerGridSolutionDP();
        int demandedGigaWatts = sum(demandSchedule);
        System.out.println("The total number of demanded gigawatts: " + demandedGigaWatts);
        System.out.println("Maximum number of satisfied gigawatts: " + solution.getmaxNumberOfSatisfiedDemands());
        System.out.println("Hours at which the battery bank should be discharged: " + solution.getHoursToDischargeBatteriesForMaxEfficiency().toString().substring(1, solution.getHoursToDischargeBatteriesForMaxEfficiency().toString().length() - 1));
        System.out.println("The number of unsatisfied gigawatts: " + (demandedGigaWatts - solution.getmaxNumberOfSatisfiedDemands()));
        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        /** MISSION ECO-MAINTENANCE BELOW **/

        System.out.println("##MISSION ECO-MAINTENANCE##");
        // TODO: Your code goes here
        // You are expected to read the file given as the second command-line argument to read
        // the number of available ESVs, the capacity of each available ESV, and the energy requirements
        // of the maintenance tasks. Then, use this data to instantiate an OptimalESVDeploymentGP object.
        // You need to call getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) method
        // of your OptimalESVDeploymentGP object to get the solution, and finally print it to STDOUT.
        String[][] ESVMaintenance = FileInput.create2DInputArray(FileInput.readFile(args[1], true, true), " ");
        ArrayList<Integer> energyDemands = FileInput.createEnergyDemands(ESVMaintenance[1]);
        OptimalESVDeploymentGP optimalESVDeploymentGP = new OptimalESVDeploymentGP(energyDemands);
        int maxNumberOfAvailableESVs = Integer.parseInt(ESVMaintenance[0][0]);
        int maxESVCapacity = Integer.parseInt(ESVMaintenance[0][1]);
        int requiredESVs = optimalESVDeploymentGP.getMinNumESVsToDeploy(maxNumberOfAvailableESVs, maxESVCapacity);
        if (requiredESVs != -1) {
            System.out.println("The minimum number of ESVs to deploy: " + requiredESVs);
            ArrayList<ArrayList<Integer>> ESVs = optimalESVDeploymentGP.getMaintenanceTasksAssignedToESVs();
            for (int i = 0; i < ESVs.size(); i++) {
                if (ESVs.get(i).isEmpty()) break;
                System.out.println("ESV " + (i + 1) + " tasks: " + ESVs.get(i));
            }
        } else {
            System.out.println("Warning: Mission Eco-Maintenance Failed.");
        }
        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }

    public static int sum(ArrayList<Integer> arr) {
        int sum = 0;
        for (int i = 0; i < arr.size(); i++) sum += arr.get(i);
        return sum;
    }
}
