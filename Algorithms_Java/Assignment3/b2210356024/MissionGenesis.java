import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {
        try {
            File file = new File(filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList humanMoleculeList = doc.getElementsByTagName("HumanMolecularData").item(0).getChildNodes();
            NodeList vitalesMoleculeList = doc.getElementsByTagName("VitalesMolecularData").item(0).getChildNodes();

            List<Molecule> humanMolecules = parseMolecules(humanMoleculeList);
            List<Molecule> vitalesMolecules = parseMolecules(vitalesMoleculeList);

            molecularDataHuman = new MolecularData(humanMolecules);
            molecularDataVitales = new MolecularData(vitalesMolecules);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to parse a list of molecules from a NodeList
    private List<Molecule> parseMolecules(NodeList nodeList) {
        List<Molecule> molecules = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i) instanceof Element) {
                Element moleculeElement = (Element) nodeList.item(i);
                String id = moleculeElement.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(moleculeElement.getElementsByTagName("BondStrength").item(0).getTextContent());
                NodeList bondsList = moleculeElement.getElementsByTagName("Bonds").item(0).getChildNodes();
                List<String> bonds = new ArrayList<>();
                for (int j = 0; j < bondsList.getLength(); j++) {
                    if (bondsList.item(j) instanceof Element) {
                        Element bondElement = (Element) bondsList.item(j);
                        String bondId = bondElement.getTextContent();
                        bonds.add(bondId);
                    }
                }
                Molecule molecule = new Molecule(id, bondStrength, bonds);
                molecules.add(molecule);
            }
        }
//        System.out.println(molecules);
        return molecules;
    }
}
