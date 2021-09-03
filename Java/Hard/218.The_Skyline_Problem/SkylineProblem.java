import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class SkylineProblem {

    /**
     * A city's skyline is the outer contour of the silhouette formed by all the buildings in that city when viewed from a
     * distance. Given the locations and heights of all the buildings, return the skyline formed by these buildings
     * collectively.
     * <p>
     * The geometric information of each building is given in the array buildings where buildings[i] =
     * [left(i), right(i), height(i)]:
     * <p>
     * left(i) is the x coordinate of the left edge of the ith building.
     * right(i) is the x coordinate of the right edge of the ith building.
     * height(i) is the height of the ith building.
     * You may assume all buildings are perfect rectangles grounded on an absolutely flat surface at height 0.
     * <p>
     * The skyline should be represented as a list of "key points" sorted by their x-coordinate in the form
     * [[x1,y1],[x2,y2],...]. Each key point is the left endpoint of some horizontal segment in the skyline except the
     * last point in the list, which always has a y-coordinate 0 and is used to mark the skyline's termination where the
     * rightmost building ends. Any ground between the leftmost and rightmost buildings should be part of the skyline's
     * contour.
     * <p>
     * Note: There must be no consecutive horizontal lines of equal height in the output skyline. For instance,
     * [...,[2 3],[4 5],[7 5],[11 5],[12 7],...] is not acceptable; the three lines of height 5 should be merged into one in
     * the final output as such: [...,[2 3],[4 5],[12 7],...]
     * <p>
     * Constraints:
     * <p>
     * 1 <= buildings.length <= 10^4
     * 0 <= left(i) < right(i) <= 2^31 - 1
     * 1 <= height(i) <= 2^31 - 1
     * buildings is sorted by left(i) in non-decreasing order.
     * <p>
     * Approach: Sweep Line
     * 因为每一个building都保证是长方形，因此可以用扫描线遍历每一个building的左端点和右端点来判断当前点位是否为skyline的一部分。根据扫进和扫出
     * 的状态，将每一个building分成两个event，即进入（entering）和离开（leaving）。对于进入event，若当前building的高度比之前遇到的所有高度
     * 都高，那么当前点必定是skyline的一部分。同时对于离开event，将当前高度移除，那么移除后的最高点（若没有其他高度，则当前最大高度为0）
     * 必定是另一个skyline key point。
     * 伪代码如下：
     * if (entering):
     * if (height > maxHeight) res.add([x, height])
     * heightSeen.add(height)
     * else if (leaving):
     * heightSeen.remove(height)
     * if (height > maxHeight) res.add([x, maxHeight])
     * 因此需要一个数据结构使得add，remove，getMax都在合理运行时间内。最优解为TreeMap（balanced BST）。
     * Why map?
     * 因为同一高度可能出现多次，需要记录该高度出现的频数，在remove时，先将频数减1，若频数最终减小到0，则该高度可以安全移除。
     * <p>
     * edge case：
     * building间可能存在overlap，e.g. [0, 2, 3], [2, 5, 3]，则只有[0, 3]和[5, 0]需要记录
     * 或者building的宽度一致但高度不一致[0, 3, 3], [0, 3, 5], [0, 3, 7]则只有[0, 7]和[3, 0]需要记录
     * 规律：对于进入event，当x相同时，需要按照高度从大到小排序（即更高的building会先进入扫描，因为只有最高的building才是skyline的一部分）。
     * 相反地，对于离开event，需要按高度从小到大排序（即更矮的building先进入扫描，在离开时，最矮的building才是skyline的一部分）。
     * 技巧：对于离开event，可以将高度即为负值，负值indicate离开event的同时-3 > -5保证高的building会在矮的building之前扫描
     * <p>
     * Time: O(nlogn) 对于n个building，则有2n个扫描线，每次扫描都可能会用到add/remove和getMax，TreeMap都在O(logn)时间
     */
    private TreeMap<Integer, Integer> heightCount;

    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> skyline = new ArrayList<>();
        // 用treeMap记录所有高度出现的频数，同时高度本身按从小到大排序
        heightCount = new TreeMap<>();
        // 记录每一个building的扫描线[left/right, height]
        List<int[]> boundaryAndHeight = new ArrayList<>();
        for (int[] building : buildings) {
            boundaryAndHeight.add(new int[]{building[0], building[2]});
            // -height代表该event为离开，同时可以进行一致的排序
            boundaryAndHeight.add(new int[]{building[1], -building[2]});
        }
        Collections.sort(boundaryAndHeight, (a, b) -> {
            // 若扫描线相同，则按高度从大到小遍历（对于离开event，则为从小到大）
            if (a[0] == b[0]) return b[1] - a[1];
            else return a[0] - b[0];
        });

        // 对所有扫描线进行遍历
        for (int[] pair : boundaryAndHeight) {
            boolean entering = pair[1] > 0;
            int height = Math.abs(pair[1]), x = pair[0];

            // 将当前点加入skyline，如果当前为更大高度且为进入event
            if (entering) {
                if (height > maxHeight()) skyline.add(List.of(x, height));
                // 将当前高度加入map
                heightCount.put(height, heightCount.getOrDefault(height, 0) + 1);
            } else {
                // 若为离开event，先将当前高度移除（将出现频数减1）
                heightCount.put(height, heightCount.get(height) - 1);
                // 若频数为1，则将该高度彻底移除
                if (heightCount.get(height) == 0) {
                    heightCount.remove(height);
                }
                // 若被移除的高度大于移除后的最大高度，则新的最大高度所在的点也是skyline的一部分
                if (height > maxHeight()) skyline.add(List.of(x, maxHeight()));
            }
        }
        return skyline;
    }

    // helper function - 返回当前最大高度，若map为空，则最大高度为0
    private int maxHeight() {
        if (heightCount.isEmpty()) return 0;
        return heightCount.lastKey();
    }

    @Test
    public void getSkylineTest() {
        /**
         * Example 1:
         * Input: buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
         * Output: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
         */
        List<List<Integer>> expected1 = List.of(List.of(2, 10), List.of(3, 15), List.of(7, 12), List.of(12, 0),
                List.of(15, 10), List.of(20, 8), List.of(24, 0));
        List<List<Integer>> actual1 = getSkyline(new int[][]{{2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}});
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input: buildings = [[0,2,3],[2,5,3]]
         * Output: [[0,3],[5,0]]
         */
        List<List<Integer>> expected2 = List.of(List.of(0, 3), List.of(5, 0));
        List<List<Integer>> actual2 = getSkyline(new int[][]{{0, 2, 3}, {2, 5, 3}});
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }
}
