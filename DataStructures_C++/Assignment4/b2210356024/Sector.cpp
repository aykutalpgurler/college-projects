#include "Sector.h"
#include <cmath> // For std::sqrt

// Constructor implementation

Sector::Sector(int x, int y, int z) : x(x), y(y), z(z), left(nullptr), right(nullptr), parent(nullptr), color(RED) {
    // TODO: Calculate the distance to the Earth, and generate the sector code

    // Calculate the distance to the Earth using the Euclidean distance formula
    distance_from_earth = std::sqrt(std::pow(x, 2) + std::pow(y, 2) + std::pow(z, 2));

    // Generate the sector code
    int distance_component = static_cast<int>(distance_from_earth); // Truncate to integer
    sector_code = std::to_string(distance_component);

    // Append coordinate components to the sector code using conditional operators
    sector_code += (x == 0) ? "S" : (x > 0) ? "R" : "L";
    sector_code += (y == 0) ? "S" : (y > 0) ? "U" : "D";
    sector_code += (z == 0) ? "S" : (z > 0) ? "F" : "B";
}

Sector::~Sector() {
    // TODO: Free any dynamically allocated memory if necessary
    // No dynamically allocated memory to free in this example

}

Sector& Sector::operator=(const Sector& other) {
    // TODO: Overload the assignment operator
    if (this != &other) { // Check for self-assignment
        x = other.x;
        y = other.y;
        z = other.z;
        distance_from_earth = other.distance_from_earth;
        sector_code = other.sector_code;
        // Note: The color, left, right, and parent members are not copied, as this might interfere with the Red-Black Tree structure.
    }
    return *this;
}

bool Sector::operator==(const Sector& other) const {
    return (x == other.x && y == other.y && z == other.z);
}

bool Sector::operator!=(const Sector& other) const {
    return !(*this == other);
}