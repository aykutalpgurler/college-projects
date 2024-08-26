#ifndef SPACESECTORLLRBT_H
#define SPACESECTORLLRBT_H

#include "Sector.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>

class SpaceSectorLLRBT {
public:
    Sector* root;
    SpaceSectorLLRBT();
    ~SpaceSectorLLRBT();
    void readSectorsFromFile(const std::string& filename);
    void insertSectorByCoordinates(int x, int y, int z);
    void displaySectorsInOrder();
    void displaySectorsPreOrder();
    void displaySectorsPostOrder();
    std::vector<Sector*> getStellarPath(const std::string& sector_code);
    void printStellarPath(const std::vector<Sector*>& path);

    Sector* insertSectorByCoordinatesRecursive(Sector* node, int x, int y, int z);
    Sector* rotateLeft(Sector* node);
    Sector* rotateRight(Sector* node);
    void flipColors(Sector* node);
    bool isRed(Sector* node);
    void postOrderTraversalHelper(Sector* node);
    void preOrderTraversalHelper(Sector* node);
    void inOrderTraversalHelper(Sector* node);
    void deleteTree(Sector* node);
    int compareCoordinates(const Sector* sector1, const Sector* sector2);
    void getPathToTarget(Sector* current, Sector* target, std::vector<Sector*>& path);
    Sector* findSectorByCoordinates(const std::string& target_sector_code, Sector* node);





    };

#endif // SPACESECTORLLRBT_H
