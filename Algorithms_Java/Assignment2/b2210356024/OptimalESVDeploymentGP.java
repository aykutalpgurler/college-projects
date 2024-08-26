import java.util.*;

/**
 * This class accomplishes Mission Eco-Maintenance
 */
public class OptimalESVDeploymentGP
{
    private ArrayList<Integer> maintenanceTaskEnergyDemands;

    /*
     * Should include tasks assigned to ESVs.
     * For the sample input:
     * 8 100
     * 20 50 40 70 10 30 80 100 10
     *
     * The list should look like this:
     * [[100], [80, 20], [70, 30], [50, 40, 10], [10]]
     *
     * It is expected to be filled after getMinNumESVsToDeploy() is called.
     */
    private ArrayList<ArrayList<Integer>> maintenanceTasksAssignedToESVs = new ArrayList<>();

    ArrayList<ArrayList<Integer>> getMaintenanceTasksAssignedToESVs() {
        return maintenanceTasksAssignedToESVs;
    }

    public OptimalESVDeploymentGP(ArrayList<Integer> maintenanceTaskEnergyDemands) {
        this.maintenanceTaskEnergyDemands = maintenanceTaskEnergyDemands;
    }

    public ArrayList<Integer> getMaintenanceTaskEnergyDemands() {
        return maintenanceTaskEnergyDemands;
    }

    /**
     *
     * @param maxNumberOfAvailableESVs the maximum number of available ESVs to be deployed
     * @param maxESVCapacity the maximum capacity of ESVs
     * @return the minimum number of ESVs required using first fit approach over reversely sorted items.
     * Must return -1 if all tasks can't be satisfied by the available ESVs
     */
    public int getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) {
        maintenanceTaskEnergyDemands.sort(Comparator.reverseOrder());
        int[] maxCapacities = new int[maxNumberOfAvailableESVs];
        Arrays.fill(maxCapacities, maxESVCapacity);
        for (int i = 0; i < maxNumberOfAvailableESVs; i++) {
            maintenanceTasksAssignedToESVs.add(new ArrayList<>());
        }
        for (int i = 0; i < maintenanceTaskEnergyDemands.size(); i++) {
            for (int j = 0; j < maxNumberOfAvailableESVs; j++) {
                if (maxCapacities[j] >= maintenanceTaskEnergyDemands.get(i)) {
                    maxCapacities[j] -= maintenanceTaskEnergyDemands.get(i);
                    maintenanceTasksAssignedToESVs.get(j).add(maintenanceTaskEnergyDemands.get(i));
                    break;
                }
                if (j == 0 && i == 0) {
                    return -1;
                }
                if (j == maxNumberOfAvailableESVs - 1) {
                    return -1;
                }
            }
        }
        int usedCount = 0;
        for (ArrayList<Integer> currentList : maintenanceTasksAssignedToESVs) {
            if (currentList.size() != 0) {
                usedCount++;
            }
            else {
                break;
            }
        }
        if (usedCount == 0) {
            return -1;
        }
        return usedCount;
    }
}
