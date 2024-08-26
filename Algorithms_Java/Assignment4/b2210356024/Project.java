import java.io.Serializable;
import java.util.*;

public class Project implements Serializable {
    static final long serialVersionUID = 33L;
    private final String name;
    private final List<Task> tasks;

    private List<Task> topologicalSort; // no need to use stack
    private boolean[] marked; // marked list for dfs

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Project(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    /**
     * @return the total duration of the project in days
     */
    public int getProjectDuration() {
        int projectDuration = 0;

        // TODO: YOUR CODE HERE
        int[] schedule = getEarliestSchedule();
        projectDuration = tasks.get(schedule.length-1).getDuration() + schedule[schedule.length-1];


        return projectDuration;
    }

    /**
     * Schedule all tasks within this project such that they will be completed as early as possible.
     *
     * @return An integer array consisting of the earliest start days for each task.
     */
    public int[] getEarliestSchedule() {

        // TODO: YOUR CODE HERE

        // Generating topological sort.
        topologicalSort = new ArrayList<>(tasks.size());
        marked = new boolean[tasks.size()];
        for (int t = 0; t < tasks.size(); t++) if (!marked[t]) dfs(tasks, t);

        int[] earliestStartTimes = new int[tasks.size()];
        earliestStartTimes[0] = 0;
        for (int i = 1; i < topologicalSort.size(); i++) {
            Task task = topologicalSort.get(i);
            if(task.getDependencies().isEmpty()) {
                earliestStartTimes[task.getTaskID()] = 0;
            } else {
                // for loop to find the biggest start time dependent
                int indexOfBiggest = 0;
//                for (int j = i - 1; j >= 0; j--) {
//                    if(task.getDependencies().contains(j) && earliestStartTimes[j] + tasks.get(j).getDuration() > earliestStartTimes[indexOfBiggest] + tasks.get(indexOfBiggest).getDuration()) {
//                        indexOfBiggest = j;
//                    }
//                }
                for (int j : task.getDependencies()) {
                    if (earliestStartTimes[j] + tasks.get(j).getDuration() > earliestStartTimes[indexOfBiggest] + tasks.get(indexOfBiggest).getDuration()) indexOfBiggest = j;
                }
                earliestStartTimes[task.getTaskID()] = earliestStartTimes[indexOfBiggest] + tasks.get(indexOfBiggest).getDuration();
            }
        }
//        System.out.println(Arrays.toString(earliestStartTimes));
        return earliestStartTimes;
    }

    private void dfs(List<Task> tasks, int task) {
        marked[task] = true;
        for (int t : tasks.get(task).getDependencies()) if (!marked[t]) dfs(tasks, t);
        topologicalSort.add(tasks.get(task));
    }

    public static void printlnDash(int limit, char symbol) {
        for (int i = 0; i < limit; i++) System.out.print(symbol);
        System.out.println();
    }

    /**
     * Some free code here. YAAAY! 
     */
    public void printSchedule(int[] schedule) {
        int limit = 65;
        char symbol = '-';
        printlnDash(limit, symbol);
        System.out.println(String.format("Project name: %s", name));
        printlnDash(limit, symbol);

        // Print header
        System.out.println(String.format("%-10s%-45s%-7s%-5s","Task ID","Description","Start","End"));
        printlnDash(limit, symbol);
        for (int i = 0; i < schedule.length; i++) {
            Task t = tasks.get(i);
            System.out.println(String.format("%-10d%-45s%-7d%-5d", i, t.getDescription(), schedule[i], schedule[i]+t.getDuration()));
        }
        printlnDash(limit, symbol);
        System.out.println(String.format("Project will be completed in %d days.", getProjectDuration()));
        printlnDash(limit, symbol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;

        int equal = 0;

        for (Task otherTask : ((Project) o).tasks) {
            if (tasks.stream().anyMatch(t -> t.equals(otherTask))) {
                equal++;
            }
        }

        return name.equals(project.name) && equal == tasks.size();
    }

}
