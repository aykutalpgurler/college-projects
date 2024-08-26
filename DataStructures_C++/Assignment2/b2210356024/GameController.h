#ifndef PA2_GAMECONTROLLER_H

#define PA2_GAMECONTROLLER_H



#include "BlockFall.h"



using namespace std;



class GameController {

public:

    bool play(BlockFall &game, const string &commands_file); // Function that implements the gameplay
    void print_grid(vector<vector<int>> grid, int row, int col);
    bool is_collision_at_beginning(const std::vector<std::vector<bool>>& shape, const std::vector<std::vector<int>>& grid);
    bool is_collision(const std::vector<std::vector<bool>>& shape, const std::vector<std::vector<int>>& grid, int top_leftmost_x_index, int offsetX, int top_leftmost_y_index, int offsetY);
    void place_block_in_grid(const vector<vector<bool>>& shape, vector<vector<int>>& grid, int top_leftmost_x_index, int top_leftmost_y_index);
    void clear_previous_position(const vector<vector<bool>>& shape, vector<vector<int>>& grid, int top_leftmost_x_index, int top_leftmost_y_index);
    int count_occupied_cells(const vector<vector<bool>>& shape);
    void apply_gravity_to_grid(BlockFall& game);
    bool is_cell_in_active_block(const std::vector<std::vector<bool>>& shape, int relativeX, int relativeY);
    int detectPowerUp(int rows, int cols, const vector<vector<int>>& grid, const vector<vector<bool>>& powerUp); 
    void clearGrid(std::vector<std::vector<int>>& grid);
    int count_occupied_cells(const vector<vector<int>>& grid);
    int detectCompletedRows(int rows, int cols, vector<vector<int>>& grid);
    bool checkCompletedRows(int rows, int cols, const vector<vector<int>>& grid);
    int calculate_fall_distance(vector<vector<bool>> shape, vector<vector<int>> grid, int top_leftmost_x_index, int top_leftmost_y_index);
    void print_blocks_shape(vector<vector<bool>>& shape);
//    int detectCompletedRowsAfterGravitySwitch(int rows, int cols, vector<vector<int>>& grid,vector<vector<bool>>& blockShape, int blockX, int blockY);






};





#endif //PA2_GAMECONTROLLER_H

