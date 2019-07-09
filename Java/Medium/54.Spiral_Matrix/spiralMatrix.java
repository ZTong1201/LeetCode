import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;


public class spiralMatrix {

    /**
     * Given a matrix of m x n elements (m rows, n columns), return all elements of the matrix in spiral order.
     *
     * Approach 1: Layer-by-Layer
     * For a given matrix, say
     * [ [1, 2, 3],
     *   [4, 5, 6],
     *   [7, 8, 9]]
     * We can strip the entire matrix into several layers, each layer has the same distance to the boundary of matrix.
     * For a given layer, once we know the top-left corner and the bottom-right corner, the layer is fixed.
     * We can then traverse from the left to right, top to bottom, right to left, and finally bottom to top, and add elements in the final
     * list. Since the distance between layers is horizontally and vertically. We can update the top-left and bottom-right after finishing
     * each layer. The only thing we need to care about is when the last layer is a single row or a single column. We don't want to
     * repeat visiting each element. In such cases, we only need to traverse left to right, and top to bottom, then we are done.
     *
     * Time: O(m*n), we visited each entry one time
     * Space: O(m*n), we need a final result list to store all the elements
     */
    public List<Integer> spiralOrderLayer(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        if(matrix.length == 0) return res;       //care for the corner case
        int r1 = 0, c1 = 0;
        int r2 = matrix.length - 1, c2 = matrix[0].length - 1;
        while(r1 <= r2 && c1 <= c2) {
            for(int c = c1; c <= c2; c++) res.add(matrix[r1][c]);
            for(int r = r1 + 1; r <= r2; r++) res.add(matrix[r][c2]);
            if(r1 < r2 && c1 < c2) {
                for(int c = c2 - 1; c > c1; c--) res.add(matrix[r2][c]);
                for(int r = r2; r > r1; r--) res.add(matrix[r][c1]);
            }
            r1 += 1;
            c1 += 1;
            r2 -= 1;
            c2 -= 1;
        }
        return res;
    }

    /**
     * Approach 2: Simulation
     * We can simulate the whole process to solve the problem. The result is in a clockwise order, to achieve this, our direction will
     * be in the following order: left -> right, top -> bottom, right -> left, bottom -> top, left -> right, ...
     * Assume the row number of matrix is R, the column number is C, we need to visited all the R*C cells from the very beginning.
     * If the next point is in the matrix boundary and hasn't been seen, we move to this cell. Otherwise, we will make the next clockwise
     * turn, and go through along this direction.
     *
     * Time: O(m*n)
     * Space: O(m*n) the final result list and the 2-d boolean array both require O(m*n) space
     */
    public List<Integer> spiralOrderSimulation(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        if(matrix.length == 0) return res;
        int R = matrix.length, C = matrix[0].length;
        //The order does matter, since we need to do a clockwise turn
        int[] dr = new int[]{0, 1, 0, -1};
        int[] dc = new int[]{1, 0, -1, 0};
        boolean[][] visited = new boolean[R][C];
        int r = 0, c = 0, dir = 0;   //dir records the current direction
        for(int i = 0; i < R * C; i++) {  //visit all the cells
            //add current entry to the list
            res.add(matrix[r][c]);
            //record it as seen
            visited[r][c] = true;
            int newR = r + dr[dir];
            int newC = c + dc[dir];
            if(newR >= 0 && newR < R && newC >= 0 && newC < C && !visited[newR][newC]) {
                //next point on this position is in the matrix boundary and has not seen before
                //we found a new point
                r = newR;
                c = newC;
            } else {
                //otherwise, we need to make the next clockwise turn
                dir = (dir + 1) % 4;   //new direction
                r += dr[dir];
                c += dc[dir];
            }
        }
        return res;
    }


    @Test
    public void spiralOrderLayerTest() {
        /**
         * Example 1:
         * Input:
         * [
         *  [ 1, 2, 3 ],
         *  [ 4, 5, 6 ],
         *  [ 7, 8, 9 ]
         * ]
         * Output: [1,2,3,6,9,8,7,4,5]
         */
        int[][] matrix1 = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[] actual1 = listToArray(spiralOrderLayer(matrix1));
        int[] expected1 = new int[]{1, 2, 3, 6, 9, 8, 7, 4, 5};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input:
         * [
         *   [1, 2, 3, 4],
         *   [5, 6, 7, 8],
         *   [9,10,11,12]
         * ]
         * Output: [1,2,3,4,8,12,11,10,9,5,6,7]
         */
        int[][] matrix2 = new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        int[] actual2 = listToArray(spiralOrderLayer(matrix2));
        int[] expected2 = new int[]{1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7};
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input:
         * [
         *   [7],
         *   [6],
         *   [9]
         * ]
         * Output: [7,6,9]
         */
        int[][] matrix3 = new int[][]{{7}, {6}, {9}};
        int[] actual3 = listToArray(spiralOrderLayer(matrix3));
        int[] expected3 = new int[]{7, 6, 9};
        assertArrayEquals(expected3, actual3);
    }

    @Test
    public void spiralOrderSimulationTest() {
        /**
         * Example 1:
         * Input:
         * [
         *  [ 1, 2, 3 ],
         *  [ 4, 5, 6 ],
         *  [ 7, 8, 9 ]
         * ]
         * Output: [1,2,3,6,9,8,7,4,5]
         */
        int[][] matrix1 = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[] actual1 = listToArray(spiralOrderSimulation(matrix1));
        int[] expected1 = new int[]{1, 2, 3, 6, 9, 8, 7, 4, 5};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input:
         * [
         *   [1, 2, 3, 4],
         *   [5, 6, 7, 8],
         *   [9,10,11,12]
         * ]
         * Output: [1,2,3,4,8,12,11,10,9,5,6,7]
         */
        int[][] matrix2 = new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        int[] actual2 = listToArray(spiralOrderSimulation(matrix2));
        int[] expected2 = new int[]{1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7};
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input:
         * [
         *   [7],
         *   [6],
         *   [9]
         * ]
         * Output: [7,6,9]
         */
        int[][] matrix3 = new int[][]{{7}, {6}, {9}};
        int[] actual3 = listToArray(spiralOrderSimulation(matrix3));
        int[] expected3 = new int[]{7, 6, 9};
        assertArrayEquals(expected3, actual3);
    }

    private int[] listToArray(List<Integer> aList) {
        int[] res = new int[aList.size()];
        for(int i = 0; i < res.length; i++) {
            res[i] = aList.get(i);
        }
        return res;
    }
}
