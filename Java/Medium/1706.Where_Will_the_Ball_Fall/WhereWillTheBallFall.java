import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertArrayEquals;

public class WhereWillTheBallFall {

    /**
     * You have a 2-D grid of size m x n representing a box, and you have n balls. The box is open on the top and bottom sides.
     * <p>
     * Each cell in the box has a diagonal board spanning two corners of the cell that can redirect a ball to the right or to the left.
     * <p>
     * A board that redirects the ball to the right spans the top-left corner to the bottom-right corner and is represented in the grid as 1.
     * A board that redirects the ball to the left spans the top-right corner to the bottom-left corner and is represented in the grid as -1.
     * We drop one ball at the top of each column of the box. Each ball can get stuck in the box or fall out of the bottom.
     * A ball gets stuck if it hits a "V" shaped pattern between two boards or if a board redirects the ball into either wall of the box.
     * <p>
     * Return an array answer of size n where answer[i] is the column that the ball falls out of at the bottom after dropping
     * the ball from the ith column at the top, or -1 if the ball gets stuck in the box.
     * <p>
     * Constraints:
     * <p>
     * m == grid.length
     * n == grid[i].length
     * 1 <= m, n <= 100
     * grid[i][j] is 1 or -1.
     * <p>
     * Approach: DFS
     * Basically, we can simulate the ball movement in the grid. A ball will have 3 states at the current cell,
     * row, col, and the direction the ball is currently facing. Note that the direction is actually changed after hitting
     * the board. For example, if the ball facing down hits a 1 board (top-left to bottom-right), the direction will be
     * changed to facing right (updated to facing left if hitting a -1 board). Hence, let's encode 4 directions in this way
     * 0 - bottom, 1 - left, 2 - up, 3- right (clockwise starting from facing down). The initial ball state will be
     * (0, col, 0) (always starting at the first row and facing downward). Use DFS to see where the ball will ultimately go.
     * Note that, if the ball move outside the top, left, right boundary of the grid, or it gets back to a visited cell,
     * that means the ball is either stuck or hits the wall. Only when the ball touch the bottom line (grid.length), the
     * corresponding column will be the result.
     * <p>
     * Time: O(mn^2) we need to simulate n balls, and for each ball, we might have to visit the entire grid to get the answer
     * Space: O(mn) need a 2-D array to record visited cells
     */
    public int[] findBallDFS(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] res = new int[n];

        for (int i = 0; i < n; i++) {
            boolean[][] visited = new boolean[m][n];
            // simulate movement for each ball
            // direction is always 0 since the ball is initially facing down
            res[i] = dfs(grid, i, visited);
        }
        return res;
    }

    private int dfs(int[][] grid, int col, boolean[][] visited) {
        int m = grid.length, n = grid[0].length;
        Stack<int[]> stack = new Stack<>();
        // triplet (row, col, direction)
        stack.push(new int[]{0, col, 0});

        // run DFS
        while (!stack.isEmpty()) {
            int[] curr = stack.pop();
            int currRow = curr[0], currCol = curr[1], currDirection = curr[2];
            // mark current cell as visited
            visited[currRow][currCol] = true;
            // update the direction after hitting the board;
            int newDirection = changeDirection(grid[currRow][currCol], currDirection);

            // get the next cell coordinates based on the new direction
            int nextRow = currRow, nextCol = currCol;
            switch (newDirection) {
                case 0: {
                    // move down
                    nextRow++;
                    break;
                }
                case 1: {
                    // move left
                    nextCol--;
                    break;
                }
                case 2: {
                    // move up
                    nextRow--;
                    break;
                }
                case 3: {
                    // move right
                    nextCol++;
                    break;
                }
            }
            // if hits the bottom, the ball successfully fall - return the current column
            if (nextRow == m) return currCol;
            // if hits the up, left, or right boundary or a visited cell, the ball either gets stuck or hits the wall
            if (nextRow < 0 || nextCol < 0 || nextCol >= n || visited[nextRow][nextCol]) break;
            // otherwise, add next cell into the stack for further search
            stack.push(new int[]{nextRow, nextCol, newDirection});
        }
        // the ball cannot fall, return -1
        return -1;
    }

    private int changeDirection(int board, int direction) {
        int newDirection = 0;
        // 0 - down, 1 - left, 2 - up, 3 - right
        if (board == 1) {
            switch (direction) {
                case 0: {
                    // down -> right
                    newDirection = 3;
                    break;
                }
                case 1: {
                    // left -> up
                    newDirection = 2;
                    break;
                }
                case 2: {
                    // up -> left
                    newDirection = 1;
                    break;
                }
                case 3: {
                    // right -> down
                    newDirection = 0;
                    break;
                }
            }
        } else {
            switch (direction) {
                case 0: {
                    // down -> left;
                    newDirection = 1;
                    break;
                }
                case 1: {
                    // left -> down
                    newDirection = 0;
                    break;
                }
                case 2: {
                    // up -> right
                    newDirection = 3;
                    break;
                }
                case 3: {
                    // right -> up
                    newDirection = 2;
                    break;
                }
            }
        }
        return newDirection;
    }

    /**
     * Approach 2: Quicker Search
     * Actually, we can enhance the search performance by always jumping to the next row in the grid.
     * How?
     * 1. If the current board is 1, then we can move to its bottom-right neighbor if and only if its right neighbor
     * also has a board 1.
     * 2. If the current board is -1, then we can move to the bottom-left neighbor if and only if its left neighbor
     * also has a board -1.
     * If other scenarios, we must have encountered a "V" shape and the ball will get stuck. Therefore, for each ball
     * (i.e. for each column), we only need to search all the rows to see whether the ball is able to reach the bottom line.
     * <p>
     * Time: O(mn) for each ball (n) we will visit at most m rows (i.e. reach the end)
     * Space: O(1) if the output array is not taken into account
     */
    public int[] findBallRowReduction(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] res = new int[n];

        for (int i = 0; i < n; i++) {
            int currRow = 0, currCol = i;
            // keep searching until reaching the bottom line
            while (currRow < m) {
                if (grid[currRow][currCol] == 1 && currCol + 1 < n && grid[currRow][currCol + 1] == 1) {
                    // if the current cell and its right neighbor both have board 1
                    // then we can slide to the bottom-right cell
                    currRow++;
                    currCol++;
                } else if (grid[currRow][currCol] == -1 && currCol - 1 >= 0 && grid[currRow][currCol - 1] == -1) {
                    // or if the current cell and its left neighbor both have board -1
                    // then we can slide to the bottom-left cell
                    currRow++;
                    currCol--;
                } else {
                    // otherwise, the ball gets stuck, stop the iteration
                    break;
                }
            }
            // update the final column if the ball drops out of the grid, otherwise assign -1
            res[i] = currRow == m ? currCol : -1;
        }
        return res;
    }

    @Test
    public void findBallTest() {
        /**
         * Example 1:
         * Input: grid = [
         * [1,1,1,-1,-1]
         * [1,1,1,-1,-1],
         * [-1,-1,-1,1,1],
         * [1,1,1,1,-1],
         * [-1,-1,-1,-1,-1]]
         * Output: [1,-1,-1,-1,-1]
         * Explanation:
         * Ball b0 is dropped at column 0 and falls out of the box at column 1.
         * Ball b1 is dropped at column 1 and will get stuck in the box between column 2 and 3 and row 1.
         * Ball b2 is dropped at column 2 and will get stuck on the box between column 2 and 3 and row 0.
         * Ball b3 is dropped at column 3 and will get stuck on the box between column 2 and 3 and row 0.
         * Ball b4 is dropped at column 4 and will get stuck on the box between column 2 and 3 and row 1.
         */
        int[] expected1 = new int[]{1, -1, -1, -1, -1};
        int[] actualDFS1 = findBallDFS(new int[][]{
                {1, 1, 1, -1, -1},
                {1, 1, 1, -1, -1},
                {-1, -1, -1, 1, 1},
                {1, 1, 1, 1, -1},
                {-1, -1, -1, -1, -1}});
        int[] actualRowReduction1 = findBallRowReduction(new int[][]{
                {1, 1, 1, -1, -1},
                {1, 1, 1, -1, -1},
                {-1, -1, -1, 1, 1},
                {1, 1, 1, 1, -1},
                {-1, -1, -1, -1, -1}});
        assertArrayEquals(expected1, actualDFS1);
        assertArrayEquals(expected1, actualRowReduction1);
        /**
         * Example 2:
         * Input: grid = [[-1]]
         * Output: [-1]
         * Explanation: The ball gets stuck against the left wall.
         */
        int[] expected2 = new int[]{-1};
        int[] actualDFS2 = findBallDFS(new int[][]{{-1}});
        int[] actualRowReduction2 = findBallRowReduction(new int[][]{{-1}});
        assertArrayEquals(expected2, actualDFS2);
        assertArrayEquals(expected2, actualRowReduction2);
        /**
         * Example 3:
         * Input: grid = [[1,1,1,1,1,1],[-1,-1,-1,-1,-1,-1],[1,1,1,1,1,1],[-1,-1,-1,-1,-1,-1]]
         * Output: [0,1,2,3,4,-1]
         */
        int[] expected3 = new int[]{0, 1, 2, 3, 4, -1};
        int[] actualDFS3 = findBallDFS(new int[][]{
                {1, 1, 1, 1, 1, 1},
                {-1, -1, -1, -1, -1, -1},
                {1, 1, 1, 1, 1, 1},
                {-1, -1, -1, -1, -1, -1}});
        int[] actualRowReduction3 = findBallRowReduction(new int[][]{
                {1, 1, 1, 1, 1, 1},
                {-1, -1, -1, -1, -1, -1},
                {1, 1, 1, 1, 1, 1},
                {-1, -1, -1, -1, -1, -1}});
        assertArrayEquals(expected3, actualDFS3);
        assertArrayEquals(expected3, actualRowReduction3);
    }
}
