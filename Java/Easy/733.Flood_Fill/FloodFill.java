import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertArrayEquals;

public class FloodFill {

    /**
     * An image is represented by an m x n integer grid image where image[i][j] represents the pixel value of the image.
     * <p>
     * You are also given three integers sr, sc, and newColor. You should perform a flood fill on the image starting from
     * the pixel image[sr][sc].
     * <p>
     * To perform a flood fill, consider the starting pixel, plus any pixels connected 4-directionally to the starting
     * pixel of the same color as the starting pixel, plus any pixels connected 4-directionally to those pixels
     * (also with the same color), and so on. Replace the color of all of the aforementioned pixels with newColor.
     * <p>
     * Return the modified image after performing the flood fill.
     * <p>
     * Constraints:
     * <p>
     * m == image.length
     * n == image[i].length
     * 1 <= m, n <= 50
     * 0 <= image[i][j], newColor < 216
     * 0 <= sr < m
     * 0 <= sc < n
     * <p>
     * Approach 1: DFS - recursion
     * <p>
     * Time: O(m * n)
     * Space: O(m * n)
     */
    public int[][] floodFillDFS(int[][] image, int sr, int sc, int newColor) {
        int oldColor = image[sr][sc];
        if (oldColor != newColor) dfs(image, sr, sc, oldColor, newColor);
        return image;
    }

    private void dfs(int[][] image, int i, int j, int oldColor, int newColor) {
        if (i < 0 || j < 0 || i >= image.length || j >= image[0].length || image[i][j] != oldColor) return;
        image[i][j] = newColor;
        dfs(image, i + 1, j, oldColor, newColor);
        dfs(image, i - 1, j, oldColor, newColor);
        dfs(image, i, j + 1, oldColor, newColor);
        dfs(image, i, j - 1, oldColor, newColor);
    }

    /**
     * Approach 2: BFS - queue & iteration
     * <p>
     * Time: O(m * n)
     * Space: O(min(m, n)) in the worst case, all the diagonal elements will be stored in the queue. It wouldn't exceed
     * min(m, n)
     */
    public int[][] floodFillBFS(int[][] image, int sr, int sc, int newColor) {
        bfs(image, sr, sc, newColor);
        return image;
    }

    private void bfs(int[][] image, int sr, int sc, int newColor) {
        int oldColor = image[sr][sc];
        if (oldColor == newColor) return;

        int m = image.length;
        int n = image[0].length;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(sr * n + sc);

        while (!queue.isEmpty()) {
            int id = queue.poll();
            int row = id / n;
            int col = id % n;
            image[row][col] = newColor;

            if (row - 1 >= 0 && image[row - 1][col] == oldColor) {
                queue.add((row - 1) * n + col);
            }
            if (row + 1 < m && image[row + 1][col] == oldColor) {
                queue.add((row + 1) * n + col);
            }
            if (col - 1 >= 0 && image[row][col - 1] == oldColor) {
                queue.add(row * n + col - 1);
            }
            if (col + 1 < n && image[row][col + 1] == oldColor) {
                queue.add(row * n + col + 1);
            }
        }
    }


    @Test
    public void floodFillDFSTest() {
        /**
         * Example 1:
         * Input: image = [[1,1,1],[1,1,0],[1,0,1]], sr = 1, sc = 1, newColor = 2
         * Output: [[2,2,2],[2,2,0],[2,0,1]]
         * Explanation: From the center of the image with position (sr, sc) = (1, 1) (i.e., the red pixel), all pixels
         * connected by a path of the same color as the starting pixel (i.e., the blue pixels) are colored with the new color.
         * Note the bottom corner is not colored 2, because it is not 4-directionally connected to the starting pixel.
         */
        int[][] image1 = new int[][]{{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        int[][] expected1 = new int[][]{{2, 2, 2}, {2, 2, 0}, {2, 0, 1}};
        int[][] actual1 = floodFillDFS(image1, 1, 1, 2);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: image = [[0,0,0],[0,0,0]], sr = 0, sc = 0, newColor = 2
         * Output: [[2,2,2],[2,2,2]]
         */
        int[][] image2 = new int[][]{{0, 0, 0}, {0, 0, 0}};
        int[][] expected2 = new int[][]{{2, 2, 2}, {2, 2, 2}};
        int[][] actual2 = floodFillDFS(image2, 0, 0, 2);
        assertArrayEquals(expected2, actual2);
    }

    @Test
    public void floodFillBFSTest() {
        /**
         * Example 1:
         * Input: image = [[1,1,1],[1,1,0],[1,0,1]], sr = 1, sc = 1, newColor = 2
         * Output: [[2,2,2],[2,2,0],[2,0,1]]
         * Explanation: From the center of the image with position (sr, sc) = (1, 1) (i.e., the red pixel), all pixels
         * connected by a path of the same color as the starting pixel (i.e., the blue pixels) are colored with the new color.
         * Note the bottom corner is not colored 2, because it is not 4-directionally connected to the starting pixel.
         */
        int[][] image1 = new int[][]{{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        int[][] expected1 = new int[][]{{2, 2, 2}, {2, 2, 0}, {2, 0, 1}};
        int[][] actual1 = floodFillBFS(image1, 1, 1, 2);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: image = [[0,0,0],[0,0,0]], sr = 0, sc = 0, newColor = 2
         * Output: [[2,2,2],[2,2,2]]
         */
        int[][] image2 = new int[][]{{0, 0, 0}, {0, 0, 0}};
        int[][] expected2 = new int[][]{{2, 2, 2}, {2, 2, 2}};
        int[][] actual2 = floodFillBFS(image2, 0, 0, 2);
        assertArrayEquals(expected2, actual2);
    }
}
