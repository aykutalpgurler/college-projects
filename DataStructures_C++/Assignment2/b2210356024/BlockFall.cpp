#include "BlockFall.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>



BlockFall::BlockFall(string grid_file_name, string blocks_file_name, bool gravity_mode_on, const string &leaderboard_file_name, const string &player_name) 
: gravity_mode_on(gravity_mode_on), leaderboard_file_name(leaderboard_file_name), player_name(player_name) {

    initialize_grid(grid_file_name);
    read_blocks(blocks_file_name);
    leaderboard.read_from_file(leaderboard_file_name);

}



void BlockFall::read_blocks(const string &input_file) {

    // TODO: Read the blocks from the input file and initialize "initial_block" and "active_rotation" member variables
    // TODO: For every block, generate its rotations and properly implement the multilevel linked list structure
    //       that represents the game blocks, as explained in the PA instructions.
    // TODO: Initialize the "power_up" member variable as the last block from the input file (do not add it to the linked list!)
    
    all_blocks = createAllBlocks(input_file);
    power_up = all_blocks.back();

    game_blocks.resize(all_blocks.size() - 1); // game blocks's size is -1 of all_blocks
    for(int i = 0; i < all_blocks.size() - 1; i++) {
        game_blocks.at(i) = all_blocks.at(i);
    }

    initial_block = new Block();
    initial_block->shape = game_blocks.at(0);

    Block *current_block = initial_block;
    active_rotation = initial_block;

    for (int i = 1; i < game_blocks.size(); i++) {
        Block *new_block = new Block();
        new_block->shape = game_blocks.at(i);
        current_block->next_block = new_block;
        current_block = new_block;
    }

    // Make sure to set the next_block of the last block to nullptr to indicate the end of the list
    current_block->next_block = nullptr;
    
    // Create rotations and link them with blocks
    Block* iterator = initial_block;

    while (iterator != nullptr) {
        if(iterator->next_block == nullptr) { // last element
            linkRotations(iterator, nullptr);
        } else
        linkRotations(iterator, iterator->next_block);

        iterator = iterator->next_block; // Move to the next block in the linked list
    }

}

// Function to create a rotation of a block
Block* BlockFall::createRotation(const Block* original, int degrees) {
    Block* rotation = new Block(*original);

    int rows = rotation->shape.size();
    int cols = rotation->shape[0].size();

    // Placeholder logic for rotations
    if (degrees == 90 || degrees == 270) {
        vector<vector<bool>> rotatedShape(cols, vector<bool>(rows, false));

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (degrees == 90)
                    rotatedShape[j][rows - i - 1] = rotation->shape[i][j];
                else  // degrees == 270
                    rotatedShape[cols - j - 1][i] = rotation->shape[i][j];
            }
        }

        rotation->shape = rotatedShape;
    } else if (degrees == 180) {
        vector<vector<bool>> rotatedShape(rows, vector<bool>(cols, false));

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                rotatedShape[i][j] = rotation->shape[rows - i - 1][cols - j - 1];
            }
        }

        rotation->shape = rotatedShape;
    }

    return rotation;
}


// Function to link rotations in both directions (clockwise and counterclockwise) and with the next block
void BlockFall::linkRotations(Block* block, Block* nextBlock) {
    block_90 = createRotation(block, 90);
    block_180 = createRotation(block, 180);
    block_270 = createRotation(block, 270);
    // Link clockwise rotations
    if(block == block_90) { // 1 distinct shape
        block->right_rotation = block;
        block->left_rotation = block;
    } else if(block != block_90 && block == block_180 && block_90 == block_270) { // 2 distinct shape
        block->right_rotation = block_90;
        block->right_rotation->left_rotation = block;
        block->right_rotation->right_rotation = block;
        block->left_rotation = block -> right_rotation;
        block->right_rotation->next_block = nextBlock;
    } else { // 4 distinct shape
        block->right_rotation = block_90;
        block->right_rotation->left_rotation = block;
        block->right_rotation->next_block = nextBlock;

        block->right_rotation->right_rotation = block_180;
        block->right_rotation->right_rotation->left_rotation = block->right_rotation;
        block->right_rotation->right_rotation->next_block = nextBlock;

        block->right_rotation->right_rotation->right_rotation = block_270;
        block->right_rotation->right_rotation->right_rotation->left_rotation = block->right_rotation->right_rotation;
        block->right_rotation->right_rotation->right_rotation->next_block = nextBlock;

        block->left_rotation = block->right_rotation->right_rotation->right_rotation;
        block->right_rotation->right_rotation->right_rotation->right_rotation = block;
    }
}


void BlockFall::initialize_grid(const string &input_file) {
    
    // TODO: Initialize "rows" and "cols" member variables
    // TODO: Initialize "grid" member variable using the command-line argument 1 in main
    
    ifstream file(input_file);

    if (!file.is_open()) {
        cerr << "Error opening file: " << input_file << endl;
        exit(EXIT_FAILURE);
    }

    string line;
    vector<int> row;

    // Read each line from the file
    while (getline(file, line)) {
        istringstream iss(line);
        int value;

        // Read each integer from the line and add it to the row vector
        while (iss >> value) {
            row.push_back(value);
        }

        // Add the row vector to the grid
        grid.push_back(row);

        // Clear the row vector for the next iteration
        row.clear();
    }

    // Close the file
    file.close();

    // Set the rows and columns based on the size of the grid
    rows = static_cast<int>(grid.size());
    cols = (rows > 0) ? static_cast<int>(grid[0].size()) : 0;
}

BlockFall::~BlockFall() {
    // Free dynamically allocated memory used for storing game blocks
    Block* current_block = initial_block;
    Block* next_block;

    block_90 = createRotation(current_block, 90);
    block_180 = createRotation(current_block, 180);
    block_270 = createRotation(current_block, 270);
    
    while (current_block != nullptr) {
        // Free memory for each rotation of the block
        if(current_block == block_90) { // 1 distinct shape
            // Do nothing
        } else if(current_block != block_90 && current_block == block_180 && block_90 == block_270) { // 2 distinct shape
            delete current_block->right_rotation;
        } else { // 4 distinct shape
            delete current_block->right_rotation->right_rotation->right_rotation;
            delete current_block->right_rotation->right_rotation;
            delete current_block->right_rotation;
        }

        next_block = current_block->next_block; // Save the next block
        delete current_block; // Free memory for the current block
        current_block = next_block; // Move to the next block
    }
    delete block_90;
    delete block_180;
    delete block_270;
}



std::vector<std::vector<std::vector<bool>>> BlockFall::createAllBlocks(const std::string& filename) {
    
    std::ifstream file(filename);
    if (!file.is_open()) {
        std::cerr << "Error opening file " << filename << std::endl;
        return {};
    }

    std::vector<std::vector<std::vector<bool>>> allBlocks;
    std::string line;
    std::vector<std::vector<bool>> currentBlock;

    while (std::getline(file, line)) {
        // Trim leading and trailing whitespaces
        line = trim(line);

        if (line.empty()) {
            // Empty or whitespace line indicates the end of a block
            if (!currentBlock.empty()) {
                allBlocks.push_back(currentBlock);
                currentBlock.clear();
            }
        } else {
            // Process the line and convert it to a boolean vector
            std::vector<bool> row;
            for (char c : line) {
                if (c == '0') {
                    row.push_back(false);
                } else if (c == '1') {
                    row.push_back(true);
                }
                // Ignore other characters
            }
            currentBlock.push_back(row);
        }
    }

    // Check if there's an unprocessed block at the end of the file
    if (!currentBlock.empty()) {
        allBlocks.push_back(currentBlock);
    }

    return allBlocks;
}
    
std::string BlockFall::trim(const std::string& str) {
    size_t first = str.find_first_not_of(" \t\n\r\f\v");
    if (first == std::string::npos) {
        return "";
    }
    size_t last = str.find_last_not_of(" \t\n\r\f\v");
    return str.substr(first, last - first + 1);
}
    






