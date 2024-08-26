import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();

        List<Molecule> selectedMolecules = new ArrayList<>();
        for(int i = 0; i < humanStructures.size(); i++) {
            Molecule min = humanStructures.get(i).getMolecules().get(0);
            for (int j = 1; j < humanStructures.get(i).getMolecules().size(); j++) {
                if (humanStructures.get(i).getMolecules().get(j).getBondStrength() < min.getBondStrength()) {
                    min = humanStructures.get(i).getMolecules().get(j);
                }
            }
            selectedMolecules.add(min);
        }
        System.out.println("Typical human molecules selected for synthesis: " + selectedMolecules);

        for(int i = 0; i < diffStructures.size(); i++) {
            Molecule min = diffStructures.get(i).getMolecules().get(0);
            for (int j = 1; j < diffStructures.get(i).getMolecules().size(); j++) {
                if (diffStructures.get(i).getMolecules().get(j).getBondStrength() < min.getBondStrength()) {
                    min = diffStructures.get(i).getMolecules().get(j);
                }
            }
            selectedMolecules.add(min);
        }
        System.out.println("Vitales molecules selected for synthesis: " + selectedMolecules.subList(humanStructures.size(), selectedMolecules.size()));
        List<Molecule> serumMolecules = new ArrayList<>(selectedMolecules);
        Set<Molecule> visited = new HashSet<>();
        visited.add(selectedMolecules.get(0));
        while (visited.size() < serumMolecules.size()) {
            double minWeight = Double.MAX_VALUE;
            Molecule from = null;
            Molecule to = null;
            for (Molecule molecule : visited) {
                for (Molecule other : serumMolecules) {
                    if (!visited.contains(other)) {
                        double weight = (molecule.getBondStrength() + other.getBondStrength()) / 2.0;
                        if (weight < minWeight) {
                            minWeight = weight;
                            from = molecule;
                            to = other;
                        }
                    }
                }
            }
            if (from != null && to != null) {
                serum.add(new Bond(from, to, minWeight));
                visited.add(to);
            }
        }
        return serum;
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {

        /* YOUR CODE HERE */
        System.out.println("Synthesizing the serum...");
        double totalBondStrength = 0;
        for (Bond bond : serum) {
            System.out.printf("Forming a bond between %s - %s with strength %.2f\n", bond.getFrom().getId(), bond.getTo().getId(), bond.getWeight());
            totalBondStrength += bond.getWeight();
        }
        System.out.printf("The total serum bond strength is %.2f\n", totalBondStrength);
    }
}
