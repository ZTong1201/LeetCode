import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ImageOverlap {

    /**
     * You are given two images, img1 and img2, represented as binary, square matrices of size n x n. A binary matrix has
     * only 0s and 1s as values.
     * <p>
     * We translate one image however we choose by sliding all the 1 bits left, right, up, and/or down any number of units.
     * We then place it on top of the other image. We can then calculate the overlap by counting the number of positions
     * that have a 1 in both images.
     * <p>
     * Note also that a translation does not include any kind of rotation. Any 1 bits that are translated outside of the
     * matrix borders are erased.
     * <p>
     * Return the largest possible overlap.
     * <p>
     * Constraints:
     * <p>
     * n == img1.length == img1[i].length
     * n == img2.length == img2[i].length
     * 1 <= n <= 30
     * img1[i][j] is either 0 or 1.
     * img2[i][j] is either 0 or 1.
     * <p>
     * Approach 1: Shift and count (brute force)
     * Basically, we will simulate all possible shifts and count the number of ones in the overlapping zone. In order to better
     * handle the shift, we notice that shifting A to the upper side is equivalent to shifting B to the bottom side.
     * Therefore, for each shift unit on the x- or y-axis, we can move up A and fix B as well as move up B and fix A. By doing
     * so, we handle both moving A to the up and to the bottom. Other than that, we also need to handle the left and right
     * shifting at the same to find the maximum overlap based on the current shift units.
     * <p>
     * Time: O(n^4) along each direction, we have (n - 1) units to move, and we need to handle 2 * (n - 1) options since
     * there are two directions along one axis. Therefore, we have O(n^2) pairs of shifting, we also need to count the number
     * of overlapping ones, which takes up to O(n^2) to traverse the overlapping zone.
     * Space: O(1)
     */
    public int largestOverlapShift(int[][] img1, int[][] img2) {
        int n = img1.length;
        int maxOverlap = 0;

        for (int yShift = 0; yShift < n; yShift++) {
            for (int xShift = 0; xShift < n; xShift++) {
                // we will handle moving img1 to the up-left & up-right in the helper function
                maxOverlap = Math.max(maxOverlap, shiftAndCount(img1, img2, xShift, yShift));
                // moving img2 to the up-left & up-right is equivalent to moving img1 to the bottom-left & bottom-right
                maxOverlap = Math.max(maxOverlap, shiftAndCount(img2, img1, xShift, yShift));
            }
        }
        return maxOverlap;
    }

    private int shiftAndCount(int[][] img1, int[][] img2, int xShift, int yShift) {
        // fix img2 and move img1
        int rowInImg2 = 0;
        int leftShiftOverlap = 0, rightShiftOverlap = 0;

        for (int rowInImg1 = yShift; rowInImg1 < img1.length; rowInImg1++) {
            int colInImg2 = 0;

            for (int colInImg1 = xShift; colInImg1 < img1[0].length; colInImg1++) {
                // shift img1 to the right
                if (img1[rowInImg1][colInImg1] == 1 && img1[rowInImg1][colInImg1] == img2[rowInImg2][colInImg2]) {
                    rightShiftOverlap++;
                }
                // or, we shift img2 to the right, which is equivalent to shifting img1 to the left
                if (img1[rowInImg1][colInImg2] == 1 && img1[rowInImg1][colInImg2] == img2[rowInImg2][colInImg1]) {
                    leftShiftOverlap++;
                }
            }
        }
        return Math.max(leftShiftOverlap, rightShiftOverlap);
    }

    /**
     * Approach 2: Linear Transformation
     * Approach 1 simulates the shift operation on the entire matrix, which means it also shift lots of cells with 0. We don't
     * care about the zero cells, and it will bring a lot of overhead when the image matrix is sparse.
     * Note that a shift operation can be denoted as a linear transformation using a vector. Essentially, we can keep track of
     * the (x, y) coordinates of all the cells with 1 in both image 1 and image 2. Then we enumerate all the pairs to compute
     * the transformation vector in order to make them overlap. We can use a hash map to count the number of same vectors, and
     * the maximum overlap with be the vector with the highest frequency.
     * <p>
     * Time: O(n^4) for n^2 cells, we may have O(n^2) one's in one image, and we need to compute the transformation between
     * all pairs
     * Space: O(n^2) need two lists to store all cells with value 1
     */
    public int largestOverlapLinearTransformation(int[][] img1, int[][] img2) {
        // put the (i, j) coordinate of cells with value 1 into a list
        List<int[]> onesInImg1 = new ArrayList<>(), onesInImg2 = new ArrayList<>();
        countOnes(img1, onesInImg1);
        countOnes(img2, onesInImg2);

        int maxOverlap = 0;
        // use a hash map count the frequency of transformations
        Map<String, Integer> transformationCount = new HashMap<>();

        for (int[] pos1 : onesInImg1) {
            for (int[] pos2 : onesInImg2) {
                // use a string to serialize each transformation vector
                String transformation = (pos1[0] - pos2[0]) + "/" + (pos1[1] - pos2[1]);
                transformationCount.put(transformation, transformationCount.getOrDefault(transformation, 0) + 1);
                maxOverlap = Math.max(maxOverlap, transformationCount.get(transformation));
            }
        }
        return maxOverlap;
    }

    private void countOnes(int[][] img, List<int[]> cells) {
        for (int i = 0; i < img.length; i++) {
            for (int j = 0; j < img[0].length; j++) {
                if (img[i][j] == 1) {
                    cells.add(new int[]{i, j});
                }
            }
        }
    }

    @Test
    public void largestOverlapTest() {
        /**
         * Example 1:
         * Input: img1 = [[1,1,0],[0,1,0],[0,1,0]], img2 = [[0,0,0],[0,1,1],[0,0,1]]
         * Output: 3
         */
        assertEquals(3, largestOverlapShift(new int[][]{{1, 1, 0}, {0, 1, 0}, {0, 1, 0}},
                new int[][]{{0, 0, 0}, {0, 1, 1}, {0, 0, 1}}));
        assertEquals(3, largestOverlapLinearTransformation(new int[][]{{1, 1, 0}, {0, 1, 0}, {0, 1, 0}},
                new int[][]{{0, 0, 0}, {0, 1, 1}, {0, 0, 1}}));
        /**
         * Example 2:
         * Input: img1 = [[0,0,0,0,1],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0]]
         * img2 = [[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[1,0,0,0,0]]
         * Output: 1
         */
        assertEquals(1, largestOverlapShift(new int[][]{{0, 0, 0, 0, 1}, {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}},
                new int[][]{{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0}, {1, 0, 0, 0, 0}}));
        assertEquals(1, largestOverlapLinearTransformation(new int[][]{{0, 0, 0, 0, 1}, {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}},
                new int[][]{{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0}, {1, 0, 0, 0, 0}}));
    }
}
