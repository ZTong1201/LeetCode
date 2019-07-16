import org.junit.Test;
import static org.junit.Assert.*;

public class islandPerimeter {

    /**
     * You are given a map in form of a two-dimensional integer grid where 1 represents land and 0 represents water.
     *
     * Grid cells are connected horizontally/vertically (not diagonally).
     * The grid is completely surrounded by water, and there is exactly one island (i.e., one or more connected land cells).
     *
     * The island doesn't have "lakes" (water inside that isn't connected to the water around the island).
     * One cell is a square with side length 1. The grid is rectangular, width and height don't exceed 100.
     * Determine the perimeter of the island.
     *
     * Approach 1: Four Way Search
     * If one cell equals to 1 (i.e. is an island), we can naively treat the perimeter of that cell is 4. (no surrounding cells). Then we
     * simply search the top, bottom, left, right of that cell, if there is a cell nearby, we subtract the perimeter by 1. Note that if the
     * nearby cell is out of the bound, we should not decrement the perimeter. We go through the whole grid, and accumulate the perimeter.
     *
     * Time: O(4mn) = O(mn), since in the worst case, for each cell we need to search 4 ways nearby.
     * Space: O(1) require constant space to search 4 ways
     */
    public int islandPerimeterFourWaySearch(int[][] grid) {
        int perimeter = 0;
        if(grid.length == 0) return perimeter;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == 1) {
                    perimeter += neighbors(grid, i, j);
                }
            }
        }
        return perimeter;
    }

    private int neighbors(int[][] grid, int i, int j) {
        int perimeter = 4;
        int[] dr = new int[]{0, 1, 0, -1};
        int[] dc = new int[]{1, 0, -1, 0};
        for(int k = 0; k < 4; k++) {
            int r = i + dr[k];
            int c = j + dc[k];
            if(r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] == 0) continue;
            else perimeter -= 1;
        }
        return perimeter;
    }

    /**
     * Approach 2: Simple search
     * We don't actually need to search four ways for each cell. For boundary cells, we can always simply increment the perimeter, since
     * we know that there is at least one direction without nearby cells.
     *
     * Time: O(mn), we still need to visit all the cells
     * Space: O(1) no extra space required
     */
    public int islandPerimeterSimpleSearch(int[][] grid) {
        int perimeter = 0;
        if(grid.length == 0) return perimeter;
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == 1) {
                    if(i == 0) perimeter++;
                    else if(grid[i - 1][j] == 0) perimeter++;

                    if(j == 0) perimeter++;
                    else if(grid[i][j - 1] == 0) perimeter++;

                    if(i == grid.length - 1) perimeter++;
                    else if(grid[i + 1][j] == 0) perimeter++;

                    if(j == grid[0].length - 1) perimeter++;
                    else if(grid[i][j + 1] == 0) perimeter++;
                }
            }
        }
        return perimeter;
    }

    @Test
    public void islandPerimeterTest() {
        /**
         * Example:
         * Input:
         * [[0,1,0,0],
         *  [1,1,1,0],
         *  [0,1,0,0],
         *  [1,1,0,0]]
         *
         * Output: 16
         */
        int[][] grid = new int[][]{{0, 1, 0, 0}, {1, 1, 1, 0}, {0, 1, 0, 0}, {1, 1, 0, 0}};
        assertEquals(16, islandPerimeterFourWaySearch(grid));
        assertEquals(16, islandPerimeterSimpleSearch(grid));
    }
}
