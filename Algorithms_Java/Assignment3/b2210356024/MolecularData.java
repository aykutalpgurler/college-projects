import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    // Method to identify molecular structures
// Return the list of different molecular structures identified from the input data
    // Method to identify molecular structures
// Return the list of different molecular structures identified from the input data, sorted by molecule IDs in ascending order
    // Method to identify molecular structures
// Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        ArrayList<MolecularStructure> structures = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for (Molecule molecule : molecules) {
            if (!visited.contains(molecule.getId())) {
                MolecularStructure structure = new MolecularStructure();
                identifyMolecularStructureDFS(molecule, visited, structure);
                Collections.sort(structure.getMolecules());
                structures.add(structure);
            }
        }
        return structures;
    }

// Rest of the class remains the same


    private void identifyMolecularStructureDFS(Molecule molecule, Set<String> visited, MolecularStructure structure) {
        visited.add(molecule.getId());
        structure.addMolecule(molecule);
        for (String bond : molecule.getBonds()) {
            Molecule nextMolecule = molecules.stream().filter(m -> m.getId().equals(bond)).findFirst().orElse(null);
            if (nextMolecule != null && !visited.contains(nextMolecule.getId())) {
                identifyMolecularStructureDFS(nextMolecule, visited, structure);
            }
        }
        for (Molecule m : molecules) {
            if (!visited.contains(m.getId()) && m.getBonds().contains(molecule.getId())) {
                identifyMolecularStructureDFS(m, visited, structure);
            }
        }
    }




    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        System.out.println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");
        for (int i = 0; i < molecularStructures.size(); i++) {
            System.out.println("Molecules in Molecular Structure " + (i + 1) + ": " + molecularStructures.get(i).getMolecules());
        }
    }


    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targetStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>(targetStructures);
        anomalyList.removeAll(sourceStructures);
        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {
        System.out.println("Molecular structures unique to Vitales individuals:");
        for (int i = 0; i < molecularStructures.size(); i++) {
            System.out.println(molecularStructures.get(i));
        }
    }
}
