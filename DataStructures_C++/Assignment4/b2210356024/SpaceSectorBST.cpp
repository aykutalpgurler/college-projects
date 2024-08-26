#include "SpaceSectorBST.h"

using namespace std;

SpaceSectorBST::SpaceSectorBST() : root(nullptr) {}

SpaceSectorBST::~SpaceSectorBST() {
    // Free any dynamically allocated memory in this class.
    deleteTree(root);
}

// Helper function for recursive deletion
void SpaceSectorBST::deleteTree(Sector* pSector) {
    if (pSector != nullptr) {
        // Recursively delete left and right subtrees
        deleteTree(pSector->left);
        deleteTree(pSector->right);

        // Delete the current node
        delete pSector;
    }
}

void SpaceSectorBST::readSectorsFromFile(const std::string& filename) {
    // TODO: read the sectors from the input file and insert them into the BST sector map
    // according to the given comparison critera based on the sector coordinates.
    ifstream file(filename);

    if (!file.is_open()) {
        cerr << "Error: Unable to open file " << filename << endl;
        return;
    }

    string line;
    getline(file, line); // Ignore the header line

    while (getline(file, line)) {
        istringstream iss(line);
        string x_str, y_str, z_str;

        getline(iss, x_str, ',');
        getline(iss, y_str, ',');
        getline(iss, z_str, ',');

        int x = stoi(x_str);
        int y = stoi(y_str);
        int z = stoi(z_str);

        insertSectorByCoordinates(x, y, z);
    }

    file.close();
}

void SpaceSectorBST::insertSectorByCoordinates(int x, int y, int z) {
    // Instantiate and insert a new sector into the space sector BST map according to the 
    // coordinates-based comparison criteria.
    // Call the helper function to perform recursive insertion
    root = insertSectorRecursive(root, x, y, z);
}

// Helper function for recursive insertion
Sector* SpaceSectorBST::insertSectorRecursive(Sector* pSector, int x, int y, int z) {
    // Base case: If the tree is empty, create a new node
    if (pSector == nullptr) {
        return new Sector(x, y, z);
    }

    // Compare coordinates for insertion
    if (x < pSector->x || (x == pSector->x && y < pSector->y) || (x == pSector->x && y == pSector->y && z < pSector->z)) {
        // Insert to the left
        pSector->left = insertSectorRecursive(pSector->left, x, y, z);
    } else {
        // Insert to the right
        pSector->right = insertSectorRecursive(pSector->right, x, y, z);
    }

    return pSector;
}

void SpaceSectorBST::deleteSector(const std::string& sector_code) {
    // Call the helper function to perform recursive deletion
    Sector* sector = getSector(sector_code);
    if (sector != nullptr) root = deleteSectorRecursive(root, sector_code);
}

// Helper function for recursive deletion
Sector* SpaceSectorBST::deleteSectorRecursive(Sector* pSector, const std::string& sector_code) {
    // Base case: If the tree is empty
    Sector* targetSector = getSector(sector_code);

    if (pSector == nullptr) {
        return nullptr;
    }

    // Recursive search for the node to delete
    if (targetSector->x < pSector->x || (targetSector->x == pSector->x && targetSector->y < pSector->y) || (targetSector->x == pSector->x && targetSector->y == pSector->y && targetSector->z < pSector->z)) {
        // The node to be deleted is in the left subtree
        pSector->left = deleteSectorRecursive(pSector->left, sector_code);
    } else if (targetSector->x > pSector->x || (targetSector->x == pSector->x && targetSector->y > pSector->y) || (targetSector->x == pSector->x && targetSector->y == pSector->y && targetSector->z > pSector->z)) {
        // The node to be deleted is in the right subtree
        pSector->right = deleteSectorRecursive(pSector->right, sector_code);
    } else {
        // Node with matching sector_code found, perform deletion

        // Case 1: Node with no children (leaf node)
        if (pSector->left == nullptr && pSector->right == nullptr) {
            delete pSector;
            return nullptr;
        }

        // Case 2: Node with one child
        if (pSector->left == nullptr) {
            Sector* temp = pSector->right;
            delete pSector;
            return temp;
        } else if (pSector->right == nullptr) {
            Sector* temp = pSector->left;
            delete pSector;
            return temp;
        }

        // Case 3: Node with two children
        // Find the in-order successor (smallest node in the right subtree)
        Sector* successor = findMinNode(pSector->right);

        // Copy the data of the in-order successor to the current node
        pSector->sector_code = successor->sector_code;
        pSector->distance_from_earth = successor->distance_from_earth;
        pSector->x = successor->x;
        pSector->y = successor->y;
        pSector->z = successor->z;

        // Recursively delete the in-order successor
        pSector->right = deleteSectorRecursive(pSector->right, successor->sector_code);
    }

    return pSector;
}

// Helper function to find the minimum node in a subtree
Sector* SpaceSectorBST::findMinNode(Sector* pSector) {
    while (pSector->left != nullptr) {
        pSector = pSector->left;
    }
    return pSector;
}


// Helper function for inorder traversal
void SpaceSectorBST::inorderTraversal(Sector* pSector) {
    if (pSector != nullptr) {
        inorderTraversal(pSector->left);
        std::cout << pSector->sector_code << std::endl;
        inorderTraversal(pSector->right);
    }
}

// Helper function for preorder traversal
void SpaceSectorBST::preorderTraversal(Sector* pSector) {
    if (pSector != nullptr) {
        std::cout << pSector->sector_code << std::endl;
        preorderTraversal(pSector->left);
        preorderTraversal(pSector->right);
    }
}

// Helper function for postorder traversal
void SpaceSectorBST::postorderTraversal(Sector* pSector) {
    if (pSector != nullptr) {
        postorderTraversal(pSector->left);
        postorderTraversal(pSector->right);
        std::cout << pSector->sector_code << std::endl;
    }
}

// Public member function for inorder traversal
void SpaceSectorBST::displaySectorsInOrder() {
    std::cout << "Space sectors inorder traversal:" << std::endl;
    inorderTraversal(root);
    std::cout << std::endl;
}

// Public member function for preorder traversal
void SpaceSectorBST::displaySectorsPreOrder() {
    std::cout << "Space sectors preorder traversal:" << std::endl;
    preorderTraversal(root);
    std::cout << std::endl;
}

// Public member function for postorder traversal
void SpaceSectorBST::displaySectorsPostOrder() {
    std::cout << "Space sectors postorder traversal:" << std::endl;
    postorderTraversal(root);
    std::cout << std::endl;
}

std::vector<Sector*> SpaceSectorBST::getStellarPath(const std::string& sector_code) {
    std::vector<Sector*> path;

    // Find the starting sector (Earth)
    Sector* startSector = getSector("0SSS");

    // Find the destination sector
    Sector* destinationSector = getSector(sector_code);

    // Clear the visited set before starting a new path search
    visitedSectors.clear();

    // Check if both sectors exist in the tree
    if (startSector != nullptr && destinationSector != nullptr) {
        // Perform a depth-first search to find the path
        findPathDFS(startSector, destinationSector, path);
    } else {
        std::cout << "A path to Dr. Elara could not be found." << std::endl;
    }

    return path;
}

void SpaceSectorBST::findPathDFS(Sector* currentSector, Sector* destinationSector, std::vector<Sector*>& path) {
    // Mark the current sector as visited
    visitedSectors.insert(currentSector);

    // Add the current sector to the path
    path.push_back(currentSector);

    // Check if the current sector is the destination
    if (currentSector == destinationSector) {
        return;
    }

    // Recursively search in the left subtree
    if (currentSector->left != nullptr && visitedSectors.find(currentSector->left) == visitedSectors.end()) {
        findPathDFS(currentSector->left, destinationSector, path);
        if (path.back() == destinationSector) {
            return;
        }
    }

    // Recursively search in the right subtree
    if (currentSector->right != nullptr && visitedSectors.find(currentSector->right) == visitedSectors.end()) {
        findPathDFS(currentSector->right, destinationSector, path);
        if (path.back() == destinationSector) {
            return;
        }
    }

    // Backtrack if the destination sector is not found in the current subtree
    path.pop_back();
}


// Modify the printStellarPath function to print the path in the required format
void SpaceSectorBST::printStellarPath(const std::vector<Sector*>& path) {
    if (!path.empty()) std::cout << "The stellar path to Dr. Elara: ";

    for (size_t i = 0; i < path.size(); ++i) {
        std::cout << path[i]->sector_code;
        if (i < path.size() - 1) {
            std::cout << "->";
        }
    }

    std::cout << std::endl;
}

Sector* SpaceSectorBST::getSector(const std::string& sector_code) {
    return getSectorRecursive(root, sector_code);
}

// Helper function for recursive sector retrieval
Sector* SpaceSectorBST::getSectorRecursive(Sector* pSector, const std::string& sector_code) {
    // Base case: If the tree is empty or the sector is not found
    if (pSector == nullptr || pSector->sector_code == sector_code) {
        return pSector;
    }

    // Search in the left and right subtrees
    Sector* leftResult = getSectorRecursive(pSector->left, sector_code);
    Sector* rightResult = getSectorRecursive(pSector->right, sector_code);

    // Return the result from the subtree where the sector was found
    return (leftResult != nullptr) ? leftResult : rightResult;
}

#include <iomanip> // for std::setw

// Add this function definition to SpaceSectorBST.cpp
void SpaceSectorBST::printTreeStructure() {
    printTreeStructure(root, 0);
}

// Helper function for recursive printing of tree structure
void SpaceSectorBST::printTreeStructure(Sector* node, int indent) {
    if (node != nullptr) {
        // Print right subtree
        printTreeStructure(node->right, indent + 4);

        // Print current node
        std::cout << std::setw(indent) << " ";
        std::cout << node->sector_code << std::endl;

        // Print left subtree
        printTreeStructure(node->left, indent + 4);
    }
}