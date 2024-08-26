import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UrbanInfrastructureDevelopment implements Serializable {
    static final long serialVersionUID = 88L;

    /**
     * Given a list of Project objects, prints the schedule of each of them.
     * Uses getEarliestSchedule() and printSchedule() methods of the current project to print its schedule.
     * @param projectList a list of Project objects
     */
    public void printSchedule(List<Project> projectList) {
        // TODO: YOUR CODE HERE
        for (Project project : projectList) {
            project.printSchedule(project.getEarliestSchedule());
        }
    }

    /**
     * TODO: Parse the input XML file and return a list of Project objects
     *
     * @param filename the input XML file
     * @return a list of Project objects
     */
    public List<Project> readXML(String filename) {
        List<Project> projectList = new ArrayList<>();
        try {
            File inputFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList projectNodes = doc.getElementsByTagName("Project");
            for (int i = 0; i < projectNodes.getLength(); i++) {
                Element projectElement = (Element) projectNodes.item(i);
                String projectName = projectElement.getElementsByTagName("Name").item(0).getTextContent();
                List<Task> tasks = new ArrayList<>();
                NodeList taskNodes = projectElement.getElementsByTagName("Task");
                for (int j = 0; j < taskNodes.getLength(); j++) {
                    Element taskElement = (Element) taskNodes.item(j);
                    int taskID = Integer.parseInt(taskElement.getElementsByTagName("TaskID").item(0).getTextContent());
                    String description = taskElement.getElementsByTagName("Description").item(0).getTextContent();
                    int duration = Integer.parseInt(taskElement.getElementsByTagName("Duration").item(0).getTextContent());
                    List<Integer> dependencies = new ArrayList<>();
                    NodeList dependencyNodes = taskElement.getElementsByTagName("DependsOnTaskID");
                    for (int k = 0; k < dependencyNodes.getLength(); k++) {
                        Element dependencyElement = (Element) dependencyNodes.item(k);
                        dependencies.add(Integer.parseInt(dependencyElement.getTextContent()));
                    }
                    Task task = new Task(taskID, description, duration, dependencies);
                    tasks.add(task);
                }
                Project project = new Project(projectName, tasks);
                projectList.add(project);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectList;
    }
}
