#include "SpaceSectorLLRBT.h"

using namespace std;

SpaceSectorLLRBT::SpaceSectorLLRBT() : root(nullptr) {}

void SpaceSectorLLRBT::readSectorsFromFile(const std::string& filename) {
    // TODO: read the sectors from the input file and insert them into the LLRBT sector map
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

// Remember to handle memory deallocation properly in the destructor.
SpaceSectorLLRBT::~SpaceSectorLLRBT() {
    // TODO: Free any dynamically allocated memory in this class.
    deleteTree(root);
}

void SpaceSectorLLRBT::deleteTree(Sector* node) {
    if (node == nullptr) {
        return;
    }

    // Recursively delete the left and right subtrees
    deleteTree(node->left);
    deleteTree(node->right);

    // Delete the current node
    delete node;
}

void SpaceSectorLLRBT::insertSectorByCoordinates(int x, int y, int z) {
    // TODO: Instantiate and insert a new sector into the space sector LLRBT map 
    // according to the coordinates-based comparison criteria.
    root = insertSectorByCoordinatesRecursive(root, x, y, z);
    root->color = BLACK; // Ensure root is always black
}

Sector* SpaceSectorLLRBT::insertSectorByCoordinatesRecursive(Sector* node, int x, int y, int z) {
    if (node == nullptr) {
        return new Sector(x, y, z);
    }

    // Perform standard BST insertion
    if (x < node->x || (x == node->x && y < node->y) || (x == node->x && y == node->y && z < node->z)) {
        node->left = insertSectorByCoordinatesRecursive(node->left, x, y, z);
    } else if (x > node->x || (x == node->x && y > node->y) || (x == node->x && y == node->y && z > node->z)) {
        node->right = insertSectorByCoordinatesRecursive(node->right, x, y, z);
    } else {
        // Duplicate values are not allowed in LLRBT
        return node;
    }

    // Fix LLRBT violations
    if (isRed(node->right) && !isRed(node->left)) {
        node = rotateLeft(node);
    }
    if (isRed(node->left) && isRed(node->left->left)) {
        node = rotateRight(node);
    }
    if (isRed(node->left) && isRed(node->right)) {
        flipColors(node);
    }

    return node;
}

// Helper function to check if a node is red (or nullptr, treated as black)
bool SpaceSectorLLRBT::isRed(Sector* node) {
    return node != nullptr && node->color == RED;
}

// Helper function for LLRBT left rotation
Sector* SpaceSectorLLRBT::rotateLeft(Sector* node) {
    Sector* newRoot = node->right;
    node->right = newRoot->left;
    newRoot->left = node;
    newRoot->color = node->color;
    node->color = RED; // The newly rotated node becomes red
    return newRoot;
}

// Helper function for LLRBT right rotation
Sector* SpaceSectorLLRBT::rotateRight(Sector* node) {
    Sector* newRoot = node->left;
    node->left = newRoot->right;
    newRoot->right = node;
    newRoot->color = node->color;
    node->color = RED; // The newly rotated node becomes red
    return newRoot;
}

// Helper function for LLRBT color flip
void SpaceSectorLLRBT::flipColors(Sector* node) {
    node->color = RED;
    node->left->color = BLACK;
    node->right->color = BLACK;
}

// Helper function for in-order traversal
void SpaceSectorLLRBT::inOrderTraversalHelper(Sector* node) {
    if (node == nullptr) {
        return;
    }

    inOrderTraversalHelper(node->left);

    if (node->color == BLACK) {
        std::cout << "BLACK sector: ";
    } else {
        std::cout << "RED sector: ";
    }
    std::cout << node->sector_code << std::endl;

    inOrderTraversalHelper(node->right);
}

// Helper function for pre-order traversal
void SpaceSectorLLRBT::preOrderTraversalHelper(Sector* node) {
    if (node == nullptr) {
        return;
    }

    if (node->color == BLACK) {
        std::cout << "BLACK sector: ";
    } else {
        std::cout << "RED sector: ";
    }
    std::cout << node->sector_code << std::endl;

    preOrderTraversalHelper(node->left);
    preOrderTraversalHelper(node->right);
}

// Helper function for post-order traversal
void SpaceSectorLLRBT::postOrderTraversalHelper(Sector* node) {
    if (node == nullptr) {
        return;
    }

    postOrderTraversalHelper(node->left);
    postOrderTraversalHelper(node->right);

    if (node->color == BLACK) {
        std::cout << "BLACK sector: ";
    } else {
        std::cout << "RED sector: ";
    }
    std::cout << node->sector_code << std::endl;
}

void SpaceSectorLLRBT::displaySectorsInOrder() {
    std::cout << "Space sectors inorder traversal:" << std::endl;
    inOrderTraversalHelper(root);
    std::cout << std::endl;
}

void SpaceSectorLLRBT::displaySectorsPreOrder() {
    std::cout << "Space sectors preorder traversal:" << std::endl;
    preOrderTraversalHelper(root);
    std::cout << std::endl;
}

void SpaceSectorLLRBT::displaySectorsPostOrder() {
    std::cout << "Space sectors postorder traversal:" << std::endl;
    postOrderTraversalHelper(root);
    std::cout << std::endl;
}


std::vector<Sector*> SpaceSectorLLRBT::getStellarPath(const std::string& target_sector_code) {
    std::vector<Sector*> path;
    Sector* targetSector = findSectorByCoordinates(target_sector_code, root);

    if (targetSector != nullptr) {
        getPathToTarget(root, targetSector, path);
    }

    return path;
}

void SpaceSectorLLRBT::printStellarPath(const std::vector<Sector*>& path) {
    if (path.empty()) {
        std::cout << "A path to Dr. Elara could not be found." << std::endl;
        return;
    }

    std::cout << "The stellar path to Dr. Elara: ";
    for (size_t i = 0; i < path.size(); ++i) {
        std::cout << path[i]->sector_code;

        if (i < path.size() - 1) {
            std::cout << "->";
        }
    }
    std::cout << std::endl;
}

Sector* SpaceSectorLLRBT::findSectorByCoordinates(const std::string& target_sector_code, Sector* node) {
    if (node == nullptr || node->sector_code == target_sector_code) {
        return node;
    }

    Sector* leftResult = findSectorByCoordinates(target_sector_code, node->left);
    Sector* rightResult = findSectorByCoordinates(target_sector_code, node->right);

    return (leftResult != nullptr) ? leftResult : rightResult;
}

void SpaceSectorLLRBT::getPathToTarget(Sector* current, Sector* target, std::vector<Sector*>& path) {
    if (current != nullptr) {
        path.push_back(current);

        if (current == target) {
            // Target found, stop recursion
            return;
        }

        // Recursively search in left or right subtree based on coordinates comparison
        if (compareCoordinates(current, target) > 0) {
            getPathToTarget(current->left, target, path);
        } else {
            getPathToTarget(current->right, target, path);
        }
    }
}

int SpaceSectorLLRBT::compareCoordinates(const Sector* sector1, const Sector* sector2) {
    // Compare X coordinates
    if (sector1->x != sector2->x) {
        return sector1->x - sector2->x;
    }

    // X coordinates are equal, compare Y coordinates
    if (sector1->y != sector2->y) {
        return sector1->y - sector2->y;
    }

    // X and Y coordinates are equal, compare Z coordinates
    return sector1->z - sector2->z;
}