#ifndef SPACESECTORBST_H
#define SPACESECTORBST_H

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <unordered_set>

#include "Sector.h"

class SpaceSectorBST {

public:
    Sector *root;
    SpaceSectorBST();
    ~SpaceSectorBST();
    void readSectorsFromFile(const std::string& filename);
    void insertSectorByCoordinates(int x, int y, int z);
    void deleteSector(const std::string& sector_code);
    void displaySectorsInOrder();
    void displaySectorsPreOrder();
    void displaySectorsPostOrder();
    std::vector<Sector*> getStellarPath(const std::string& sector_code);
    void printStellarPath(const std::vector<Sector*>& path);

    Sector* insertSectorRecursive(Sector* pSector, int x, int y, int z);
    Sector* deleteSectorRecursive(Sector* pSector, const std::string& sector_code);
    Sector* findMinNode(Sector* pSector);
    void inorderTraversal(Sector* pSector);
    void preorderTraversal(Sector* pSector);
    void postorderTraversal(Sector* pSector);
    void printTreeStructure();
    void printTreeStructure(Sector* node, int indent);
    Sector* getSectorRecursive(Sector* pSector, const std::string& sector_code);
    Sector* getSector(const std::string& sector_code);
    void findPathDFS(Sector* currentSector, Sector* destinationSector, std::vector<Sector*>& path);
    void deleteTree(Sector* pSector);

        private:
    std::unordered_set<Sector*> visitedSectors; // Keep track of visited sectors

};

#endif // SPACESECTORBST_H
