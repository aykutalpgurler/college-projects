#include "GameController.h"

#include <iostream>
#include <fstream>
#include <string>

bool GameController::play(BlockFall& game, const string& commands_file){

    // TODO: Implement the gameplay here while reading the commands from the input file given as the 3rd command-line
    //       argument. The return value represents if the gameplay was successful or not: false if game over,
    //       true otherwise.

    std::ifstream file(commands_file);
    
    std::string command;
    while(std::getline(file, command)) {
    
        // New block is precluded from entering the grid due to potential collisions with existing blocks
        if(game.active_rotation->top_leftmost_x_index == 0 /*check if block is at the beginning*/ &&
            is_collision_at_beginning(game.active_rotation->shape, game.grid) /*check if blocks are collided*/&&
            !game.active_rotation->has_entered) { 
            // GAME OVER !
            // TODO: Print operations!
            std::cout << "GAME OVER!" << std::endl;
            std::cout << "Next block that couldn't fit:" << std::endl;
            print_blocks_shape(game.active_rotation->shape);
            std::cout << "Final grid and score:" << std::endl << std::endl;
            std::cout << "Score: " << game.current_score << std::endl;
            print_grid(game.grid, game.rows, game.cols);
            game.leaderboard.print_leaderboard();
            return false;
        } else {
            place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index /* 0 */, game.active_rotation->top_leftmost_y_index);
            game.active_rotation->has_entered = true;
        }
        
        if(command == "PRINT_GRID") {
//            std::cout << "pg" << std::endl;
            std::cout << "Score: " << game.current_score << std::endl;

            print_grid(game.grid, game.rows, game.cols);

        } else if(command =="ROTATE_RIGHT") {
//            std::cout << "rr" << std::endl;
            
//            int temp_index = game.active_rotation->top_leftmost_index;
//            game.active_rotation = game.active_rotation->right_rotation;
//            game.active_rotation->top_leftmost_index = temp_index;
            
            clear_previous_position(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);

            if (!is_collision(game.active_rotation->right_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, 0, game.active_rotation->top_leftmost_y_index, 0)) {
                
                // Rotate the block to the right
                int temp_index = game.active_rotation->top_leftmost_x_index;
                game.active_rotation = game.active_rotation->right_rotation;
                game.active_rotation->top_leftmost_x_index = temp_index;
                
                // Place the block in the new position
                place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
                
            } else {
                
                place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
            }
            
            game.active_rotation->has_entered = true;
            
        } else if(command == "ROTATE_LEFT") {
//            std::cout << "rl" << std::endl;
            
            clear_previous_position(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);

            if (!is_collision(game.active_rotation->left_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, 0, game.active_rotation->top_leftmost_y_index, 0)) {
                
                // Rotate the block to the left
                int temp_index = game.active_rotation->top_leftmost_x_index;
                game.active_rotation = game.active_rotation->left_rotation;
                game.active_rotation->top_leftmost_x_index = temp_index;
            
                // Place the block in the new position
                place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
                
            } else {
                
                place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
            }
            
            game.active_rotation->has_entered = true;
            
        } else if(command == "MOVE_RIGHT") {
//            std::cout << "mr" << std::endl;

            clear_previous_position(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);

            if (!is_collision(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, 1, game.active_rotation->top_leftmost_y_index, 0)) {
                
                // Move the block to the right
                game.active_rotation->top_leftmost_x_index++;
                
                // Place the block in the new position
                place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
            } else {
                place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
            }
            // Else, continue

        } else if(command == "MOVE_LEFT") {
//            std::cout << "ml" << std::endl;
            
            clear_previous_position(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);

            if (!is_collision(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, -1, game.active_rotation->top_leftmost_y_index, 0)) {
                
                // Move the block to the left
                game.active_rotation->top_leftmost_x_index--;
                
                // Place the block in the new position
                place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
            } else {
                // Place the block in the new position
                place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
            }
            // Else, continue
            
        } else if(command == "DROP") {
//            std::cout << "d" << std::endl;

            if(!game.gravity_mode_on) { // Without gravity
                // Clear the previous position before dropping
                int fall_distance = 0;
                clear_previous_position(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);

                // Drop the block until it encounters an occupied cell or reaches the bottom
                while (!is_collision(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, 0, game.active_rotation->top_leftmost_y_index, 1)) {
                    game.active_rotation->top_leftmost_y_index++;
                    fall_distance++;
                }
                
                game.current_score += fall_distance * count_occupied_cells(game.active_rotation->shape);
                
                // Place the block in the new position
                place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
                
//                game.active_rotation = game.active_rotation->next_block;
            } else { // With Gravity
                // First drop block normally to just calculate fall_distance and score.
//                int fall_distance = 0;
//                clear_previous_position(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
//
//                // Drop the block until it encounters an occupied cell or reaches the bottom
//                while (!is_collision(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, 0, game.active_rotation->top_leftmost_y_index, 1)) {
//                    game.active_rotation->top_leftmost_y_index++;
//                    fall_distance++;
//                }
//                
//                game.current_score += fall_distance * count_occupied_cells(game.active_rotation->shape);
                
                int total_score = calculate_fall_distance(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index) * count_occupied_cells(game.active_rotation->shape);
                
                clear_previous_position(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
//                int min_fall_distance = game.rows; // Initialize with the maximum possible value

                // Iterate through each cell in the block
                for (int i = 0; i < game.active_rotation->shape.size(); ++i) {
                    for (int j = 0; j < game.active_rotation->shape[i].size(); ++j) {
                        if (game.active_rotation->shape[i][j]) { // Cell is part of the block

                            // Drop the cell until it encounters an occupied cell or reaches the bottom
                            int fall_distance = 0;
                            while (game.active_rotation->top_leftmost_y_index + i + fall_distance + 1 < game.rows &&
                                   game.grid[game.active_rotation->top_leftmost_y_index + i + fall_distance + 1][game.active_rotation->top_leftmost_x_index + j] == 0) {
                                fall_distance++;
                            }

                            // Update the score based on the falling distance
//                            min_fall_distance = std::min(min_fall_distance, fall_distance);

                            // Place the cell in the new position
                            game.grid[game.active_rotation->top_leftmost_y_index + i + fall_distance][game.active_rotation->top_leftmost_x_index + j] = 1;
                        }
                    }
                }

                // Update the total score
                game.current_score += total_score;

//                // Move to the next block
//                game.active_rotation = game.active_rotation->next_block;
            }
            
            // TODO: Implement:
            //       1.Power-up detection
            int powerUpBonus = detectPowerUp(game.rows, game.cols, game.grid, game.power_up);
            if (powerUpBonus > 0) {
                std::cout << "Before clearing:" << std::endl;
                print_grid(game.grid, game.rows, game.cols);
                game.current_score += powerUpBonus;
                clearGrid(game.grid); // Clear the grid after power-up detection
            }
            
            // TODO: Implement:
            //       2. Clear rows.
            
            if(checkCompletedRows(game.rows, game.cols, game.grid)) {
                std::cout << "Before clearing:" << std::endl;
                print_grid(game.grid, game.rows, game.cols);
                int clearRowBonus = detectCompletedRows(game.rows, game.cols, game.grid);
                game.current_score += clearRowBonus;
            }
            
            // check if blocks are finished
            if(game.active_rotation->next_block != nullptr) {
                // Move to the next block
                game.active_rotation = game.active_rotation->next_block;
            } else {
                std::cout << "YOU WIN!" << std::endl;
                std::cout << "No more blocks." << std::endl;
                std::cout << "Final grid and score:" << std::endl << std::endl;
                std::cout << "Score: " << game.current_score << std::endl;
                print_grid(game.grid, game.rows, game.cols);
                game.leaderboard.print_leaderboard();
                return true;
            }
            
        } else if(command == "GRAVITY_SWITCH") {
//            std::cout << "gs" << std::endl;
            // Apply gravity to cells in the grid (excluding active block)
            clear_previous_position(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
            apply_gravity_to_grid(game);

            game.gravity_mode_on = !game.gravity_mode_on;
            
            if(checkCompletedRows(game.rows, game.cols, game.grid)) {
                int clearRowBonus = detectCompletedRows(game.rows, game.cols, game.grid);
                game.current_score += clearRowBonus;
            }
            
            place_block_in_grid(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
            
//            if(checkCompletedRows(game.rows, game.cols, game.grid)) {
//                std::cout << "Before clearing:" << std::endl;
//                print_grid(game.grid, game.rows, game.cols);
//                int clearRowBonus = detectCompletedRowsAfterGravitySwitch(game.rows, game.cols, game.grid, game.active_rotation->shape, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
//                game.current_score += clearRowBonus;
//            }
            
        } else {
            std::cout << "Unknown command: " << command << std::endl;
        }
          
    }
    
    clear_previous_position(game.active_rotation->shape, game.grid, game.active_rotation->top_leftmost_x_index, game.active_rotation->top_leftmost_y_index);
    std::cout << "GAME FINISHED!" << std::endl;
    std::cout << "No more commands." << std::endl;
    std::cout << "Final grid and score:" << std::endl << std::endl;
    std::cout << "Score: " << game.current_score << std::endl;
    print_grid(game.grid, game.rows, game.cols);
    game.leaderboard.print_leaderboard();

    return true;

}

void GameController::print_grid(vector<vector<int>> grid, int rows, int cols) {
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
            if (grid[i][j] == 1) {
                std::cout << occupiedCellChar;
            } else {
                std::cout << unoccupiedCellChar;
            }
        }
        std::cout << std::endl;
    }
    std::cout << std::endl;
}

bool GameController::is_collision_at_beginning(const std::vector<std::vector<bool>>& shape, const std::vector<std::vector<int>>& grid) {
    // Assuming the shape vector represents the block's shape, with true indicating occupied cells.

    // Get the dimensions of the shape and the grid
    int shapeRows = shape.size();
    int shapeCols = shape[0].size();
//    int gridRows = grid.size();
//    int gridCols = grid[0].size();

    // Check if the shape collides with the occupied cells in the grid
    for (int i = 0; i < shapeRows; ++i) {
        for (int j = 0; j < shapeCols; ++j) {
            if (shape[i][j]) {
                // Calculate the corresponding position in the grid
                int gridRow = i; // Adjust as needed based on the block's position in the game
                int gridCol = j; // No adjustment for leftmost index since it is not used at the beginning

                // Check if the cell is already occupied in the grid
                if (grid[gridRow][gridCol] != 0) {
                    // Collision with an occupied cell in the grid
                    return true;
                }
            }
        }
    }

    // No collision detected
    return false;
}

#include <vector>

bool GameController::is_collision(const std::vector<std::vector<bool>>& shape, const std::vector<std::vector<int>>& grid, int top_leftmost_x_index, int offsetX, int top_leftmost_y_index, int offsetY) {
    // Get the dimensions of the block's shape
    int shapeRows = shape.size();
    int shapeCols = shape[0].size();

    // Calculate the new top-left indices after applying the offset
    int newTopLeftIndexX = top_leftmost_x_index + offsetX;
    int newTopLeftIndexY = top_leftmost_y_index + offsetY;

    // Check if the block would go out of bounds on the left or right
    if (newTopLeftIndexX < 0 || newTopLeftIndexX + shapeCols > grid[0].size()) {
        return true;  // Collision with grid boundary in the horizontal direction
    }

    // Check if the block would go out of bounds on the top or bottom
    if (newTopLeftIndexY < 0 || newTopLeftIndexY + shapeRows > grid.size()) {
        return true;  // Collision with grid boundary in the vertical direction
    }

    // Check for collisions with existing blocks in the grid
    for (int i = 0; i < shapeRows; ++i) {
        for (int j = 0; j < shapeCols; ++j) {
            if (shape[i][j]) {  // Check only the filled cells of the block's shape
                int gridRow = newTopLeftIndexY + i;
                int gridCol = newTopLeftIndexX + j;

                // Check if the cell in the grid is occupied
                if (grid[gridRow][gridCol] != 0) {
                    return true;  // Collision with an existing block
                }
            }
        }
    }

    // No collision detected
    return false;
}


// Function to place the active block into the grid
void GameController::place_block_in_grid(const vector<vector<bool>>& shape, vector<vector<int>>& grid, int top_leftmost_x_index, int top_leftmost_y_index) {
    for (size_t i = 0; i < shape.size(); ++i) {
        for (size_t j = 0; j < shape[i].size(); ++j) {
            if (shape[i][j]) {
                // If the cell in the shape is true, update the corresponding cell in the grid
                grid[i + top_leftmost_y_index][j + top_leftmost_x_index] = 1;
            }
        }
    }
}

// Function to clear the previous position of the active block in the grid
void GameController::clear_previous_position(const vector<vector<bool>>& shape, vector<vector<int>>& grid, int top_leftmost_x_index, int top_leftmost_y_index) {
    for (size_t i = 0; i < shape.size(); ++i) {
        for (size_t j = 0; j < shape[i].size(); ++j) {
            if (shape[i][j]) {
                // If the cell in the shape is true, clear the corresponding cell in the grid
                grid[i + top_leftmost_y_index][j + top_leftmost_x_index] = 0;
            }
        }
    }
}

int GameController::count_occupied_cells(const vector<vector<bool>>& shape) {
    int count = 0;
    for (const auto& row : shape) {
        for (bool cell : row) {
            if (cell) {
                count++;
            }
        }
    }
    return count;
}

void GameController::apply_gravity_to_grid(BlockFall& game) {
    // Iterate through each cell in the grid
    for (int i = game.rows - 1; i >= 0; --i) {
        for (int j = 0; j < game.cols; ++j) {
            // Skip the cells in the active block
            if (is_cell_in_active_block(game.active_rotation->shape, j - game.active_rotation->top_leftmost_x_index, i - game.active_rotation->top_leftmost_y_index)) {
                continue;
            }

            // Drop the cell until it encounters an occupied cell or reaches the bottom
            int fall_distance = 0;
            while (i + fall_distance + 1 < game.rows && game.grid[i + fall_distance + 1][j] == 0) {
                fall_distance++;
            }

            // Place the cell in the new position
            game.grid[i + fall_distance][j] = game.grid[i][j];

            // Clear the previous position
            if (fall_distance > 0) {
                game.grid[i][j] = 0;
            }
        }
    }
}

bool GameController::is_cell_in_active_block(const vector<vector<bool>>& shape, int relativeX, int relativeY) {
    // Check if the relative coordinates (relativeX, relativeY) are within the bounds of the active block's shape
    return relativeY >= 0 && relativeY < static_cast<int>(shape.size()) &&
           relativeX >= 0 && relativeX < static_cast<int>(shape[0].size()) &&
           shape[relativeY][relativeX];
}

int GameController::detectPowerUp(int rows, int cols, const vector<vector<int>>& grid, const vector<vector<bool>>& powerUp) {
    int occupiedCellCount = 0;

    // Iterate through the grid to find the power-up shape
    for (int i = 0; i <= rows - powerUp.size(); ++i) {
        for (int j = 0; j <= cols - powerUp[0].size(); ++j) {
            bool foundPowerUp = true;

            // Check if the power-up shape matches the grid at the current position
            for (size_t pi = 0; pi < powerUp.size(); ++pi) {
                for (size_t pj = 0; pj < powerUp[pi].size(); ++pj) {
                    if ((powerUp[pi][pj] && grid[i + pi][j + pj] != 1) || (!powerUp[pi][pj] && grid[i + pi][j + pj] != 0)) {
                        foundPowerUp = false;
                        break;
                    }
                }
                if (!foundPowerUp) {
                    break;
                }
            }

            // If the power-up shape is found, clear the grid and return the score
            if (foundPowerUp) {
                occupiedCellCount = count_occupied_cells(grid);  // Count occupied cells in the entire grid
                return occupiedCellCount + 1000;  // Return score
            }
        }
    }

    // Power-up not detected
    return 0;
}

void GameController::clearGrid(std::vector<std::vector<int>>& grid) {
    // Set every cell in the grid to 0
    for (auto& row : grid) {
        for (int& cell : row) {
            cell = 0;
        }
    }
}

int GameController::count_occupied_cells(const vector<vector<int>>& grid) {
    int count = 0;
    for (const auto& row : grid) {
        for (int cell : row) {
            if (cell == 1) {
                count++;
            }
        }
    }
    return count;
}

int GameController::detectCompletedRows(int rows, int cols, vector<vector<int>>& grid) {
    // detect completed rows.
    // shift downwards remaining unfilled rows
    // return bonus point
    // bonus point equal one additional point per cleared row, multiplied by the total number of columns in the grid.
    
    int bonusPoints = 0;

    // Iterate through each row
    for (int i = 0; i < rows; ++i) {
        bool completedRow = true;

        // Check if the row is completed
        for (int j = 0; j < cols; ++j) {
            if (grid[i][j] != 1) {
                completedRow = false;
                break;
            }
        }

        // If the row is completed, clear it and shift down the remaining rows
        if (completedRow) {
            // Increment bonus points
            bonusPoints += cols;

            // Shift down remaining rows
            for (int k = i; k > 0; --k) {
                for (int j = 0; j < cols; ++j) {
                    grid[k][j] = grid[k - 1][j];
                }
            }

            // Clear the top row
            for (int j = 0; j < cols; ++j) {
                grid[0][j] = 0;
            }
        }
    }

    return bonusPoints;
}

bool GameController::checkCompletedRows(int rows, int cols, const vector<vector<int>>& grid) {
    // Return true if there is at least 1 completed rows.
        for (int i = 0; i < rows; ++i) {
        bool completedRow = true;
        for (int j = 0; j < cols; ++j) {
            if (grid[i][j] != 1) {
                completedRow = false;
                break;
            }
        }
        if (completedRow) {
            return true;
        }
    }
    return false;
}

int GameController::calculate_fall_distance(vector<vector<bool>> shape, vector<vector<int>> grid, int top_leftmost_x_index, int top_leftmost_y_index) {
    int fall_distance = 0;
    clear_previous_position(shape, grid, top_leftmost_x_index, top_leftmost_y_index);

    // Drop the block until it encounters an occupied cell or reaches the bottom
    while (!is_collision(shape, grid, top_leftmost_x_index, 0, top_leftmost_y_index, 1)) {
        top_leftmost_y_index++;
        fall_distance++;
    }

    // Place the block in the new position
    place_block_in_grid(shape, grid, top_leftmost_x_index, top_leftmost_y_index);

    return fall_distance;
}

void GameController::print_blocks_shape(vector<vector<bool>>& shape) {
    for (size_t i = 0; i < shape.size(); ++i) {
        for (size_t j = 0; j < shape[i].size(); ++j) {
            if (shape[i][j]) {
                std::cout << occupiedCellChar;
            } else {
                std::cout << unoccupiedCellChar;
            }
        }
        std::cout << std::endl;
    }
    std::cout << std::endl;
}

//int GameController::detectCompletedRowsAfterGravitySwitch(int rows, int cols, vector<vector<int>>& grid,vector<vector<bool>>& blockShape, int blockX, int blockY) {
//    int bonusPoints = 0;
//
//    // Iterate through each row
//    for (int i = 0; i < rows; ++i) {
//        bool completedRow = true;
//
//        // Check if the row is completed, excluding cells covered by the blockShape
//        for (int j = 0; j < cols; ++j) {
//            // Check if the indices are within bounds
//            int blockShapeRow = i - blockY;
//            int blockShapeCol = j - blockX;
//            if (blockShapeRow >= 0 && blockShapeRow < blockShape.size() &&
//                blockShapeCol >= 0 && blockShapeCol < blockShape[0].size() &&
//                blockShape[blockShapeRow][blockShapeCol]) {
//                // If it is part of the shape, don't check for completion
//                continue;
//            }
//
//            // Check if the cell in the grid is filled
//            if (grid[i][j] != 1) {
//                completedRow = false;
//                break;
//            }
//        }
//
//        // If the row is completed, clear it (except for cells covered by blockShape) and shift down the remaining rows
//        if (completedRow) {
//            // Increment bonus points
//            bonusPoints += cols;
//
//            // Shift down remaining rows
//            for (int k = i; k > 0; --k) {
//                for (int j = 0; j < cols; ++j) {
//                    // Skip cells covered by the blockShape
//                    int blockShapeRow = k - blockY;
//                    int blockShapeCol = j - blockX;
//                    if (!blockShapeRow >= 0 || blockShapeRow >= blockShape.size() ||
//                        !blockShapeCol >= 0 || blockShapeCol >= blockShape[0].size() ||
//                        !blockShape[blockShapeRow][blockShapeCol]) {
//                        grid[k][j] = grid[k - 1][j];
//                    }
//                }
//            }
//
//            // Clear the top row (except for cells covered by blockShape)
//            for (int j = 0; j < cols; ++j) {
//                int blockShapeRow = 0 - blockY;
//                int blockShapeCol = j - blockX;
//                if (!(blockShapeRow >= 0 && blockShapeRow < blockShape.size() &&
//                      blockShapeCol >= 0 && blockShapeCol < blockShape[0].size() &&
//                      blockShape[blockShapeRow][blockShapeCol])) {
//                    grid[0][j] = 0;
//                }
//            }
//        }
//    }
//
//    return bonusPoints;
//}
