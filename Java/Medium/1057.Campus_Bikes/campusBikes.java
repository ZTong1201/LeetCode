import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class campusBikes {

    /**
     * On a campus represented as a 2D grid, there are N workers and M bikes, with N <= M. Each worker and bike is a 2D coordinate on
     * this grid.
     *
     * Our goal is to assign a bike to each worker. Among the available bikes and workers, we choose the (worker, bike) pair with the
     * shortest Manhattan distance between each other, and assign the bike to that worker. (If there are multiple (worker, bike) pairs
     * with the same shortest Manhattan distance, we choose the pair with the smallest worker index; if there are multiple ways to do that,
     * we choose the pair with the smallest bike index). We repeat this process until there are no available workers.
     *
     * The Manhattan distance between two points p1 and p2 is Manhattan(p1, p2) = |p1.x - p2.x| + |p1.y - p2.y|.
     *
     * Return a vector ans of length N, where ans[i] is the index (0-indexed) of the bike that the i-th worker is assigned to.
     *
     * Note:
     *
     * 0 <= workers[i][j], bikes[i][j] < 1000
     * All worker and bike locations are distinct.
     * 1 <= workers.length <= bikes.length <= 1000
     *
     * Approach 1: Compare Sort
     * 本质上，因为总要先assign距离最短的（worker，bike）pair，因此需要找到所有的worker和bike的组合，然后对所有pair进行排序，排序规则如下：
     * 1.若距离不等，按距离从小到大排序
     * 2.若距离相等，按worker的index从小到大排序
     * 3.若前两者都相等，最后按bike的index从小到大排序
     *
     * 因此只要按照上述规则对所有pair进行排序，然后通过greedy思想，从小到大遍历所有的pair，若当前worker和bike都未被分配，则将二者连接起来即可
     *
     * Time: O(mn*log(mn)) 对于m个worker和n个bike，总共有mn个pair，对所有pair进行排序需要O(mn*log(mn))时间
     * Space: O(mn) 需要将所有的pair都记录下来然后排序
     */
    public int[] assignBikesCompareSort(int[][] workers, int[][] bikes) {
        //将上述排序规则写成comparator
        Comparator<int[]> pairComparator = (a, b) -> {
            if(a[2] != b[2]) {
                return a[2] - b[2];
            } else if(a[0] != b[0]) {
                return a[0] - b[0];
            } else {
                return a[1] - b[1];
            }
        };
        List<int[]> pairs = new ArrayList<>();
        //遍历所有pair，计算距离，将（worker index，bike index，dist）的pair放入list中
        for(int i = 0; i < workers.length; i++) {
            for(int j = 0; j < bikes.length; j++) {
                int dist = Math.abs(workers[i][0] - bikes[j][0]) + Math.abs(workers[i][1] - bikes[j][1]);
                pairs.add(new int[]{i, j, dist});
            }
        }
        //对所有pair进行排序
        Collections.sort(pairs, pairComparator);
        //然后遍历所有pair，只有当worker和bike都未被分配时，才将二者连接起来
        //可以直接用数组来进行映射
        int[] wo = new int[workers.length];
        int[] bi = new int[bikes.length];
        //初始化为-1，意味着未被分配
        Arrays.fill(wo, -1);
        Arrays.fill(bi, -1);
        //记录被分配的worker的个数，若所有worker都被分配完毕，不需要再进行计算
        int count = 0;
        for(int[] pair : pairs) {
            if(wo[pair[0]] == -1 && bi[pair[1]] == -1) {
                wo[pair[0]] = pair[1];
                bi[pair[1]] = pair[0];
                count++;
                if(count == workers.length) {
                    break;
                }
            }
        }
        return wo;
    }

    /**
     * Approach 2: Counting Sort
     * 对于compare sort而言，O(nlogn)已经是极限，因此考虑使用counting sort缩短排序时间，注意到worker和bike的坐标都是[0, 1000)的数字，因此对于grid中
     * 任意两点，其最大曼哈顿距离为2000（即(0,0)到(1000, 1000)），因此可以对每一个dist，与（worker index，bike index）相对应起来，因为是先固定worker
     * 的index，再找所有的bike index的组合，因此每个dist下对应的pair已经是排好序的。最后只需要从小到大遍历所有的距离可能值，若图中的点能得到该距离，再
     * 继续遍历该距离下的所有pair，找到未被分配的worker和bike，然后进行分配即可。
     *
     * Time: O(mn) 因为只需要使用counting sort，只需要遍历所有的pair，将其和对应的距离映射起来，之后再遍历一遍所有的pair进行分配即可
     * Space: O(mn) 需要经所有的pair和对应的距离映射起来
     */
    public int[] assignBikesCountingSort(int[][] workers, int[][] bikes) {
        //距离最大值为2000，因此需要2001个位置，每个位置记录能得到该距离的所有可能组合
        List<int[]>[] dists = new ArrayList[2001];
        for(int i = 0; i < workers.length; i++) {
            for(int j = 0; j < bikes.length; j++) {
                int dist = Math.abs(workers[i][0] - bikes[j][0]) + Math.abs(workers[i][1] - bikes[j][1]);
                if(dists[dist] == null) {
                    dists[dist] = new ArrayList<>();
                }
                dists[dist].add(new int[]{i, j});
            }
        }
        int[] wo = new int[workers.length];
        int[] bi = new int[bikes.length];
        Arrays.fill(wo, -1);
        Arrays.fill(bi, -1);
        int count = 0;
        for(int i = 1; i <= 2000 && count < workers.length; i++) {
            //如果该距离所对应的位置为null，说明数组中组成的pair不能得到该距离，直接跳过，继续判断下一个距离
            if(dists[i] == null) continue;
            //如果组成的pair能得到当前距离，则需要遍历对应的所有pair
            for(int[] pair : dists[i]) {
                if(wo[pair[0]] == -1 && bi[pair[1]] == -1) {
                    wo[pair[0]] = pair[1];
                    bi[pair[1]] = pair[0];
                    count++;
                }
            }
        }
        return wo;
    }

    @Test
    public void assignBikesTest() {
        /**
         * Example 1:
         * Input: workers = [[0,0],[2,1]], bikes = [[1,2],[3,3]]
         * Output: [1,0]
         * Explanation:
         * Worker 1 grabs Bike 0 as they are closest (without ties), and Worker 0 is assigned Bike 1. So the output is [1, 0].
         */
        int[][] workers1 = new int[][]{{0, 0}, {2, 1}};
        int[][] bikes1 = new int[][]{{1, 2}, {3, 3}};
        int[] expected1 = new int[]{1, 0};
        int[] actual1 = assignBikesCompareSort(workers1, bikes1);
        int[] actual11 = assignBikesCountingSort(workers1, bikes1);
        assertArrayEquals(expected1, actual1);
        assertArrayEquals(expected1, actual11);
        /**
         * Example 2:
         * Input: workers = [[0,0],[1,1],[2,0]], bikes = [[1,0],[2,2],[2,1]]
         * Output: [0,2,1]
         * Explanation:
         * Worker 0 grabs Bike 0 at first. Worker 1 and Worker 2 share the same distance to Bike 2, thus Worker 1 is assigned to Bike 2,
         * and Worker 2 will take Bike 1. So the output is [0,2,1].
         */
        int[][] workers2 = new int[][]{{0, 0}, {1, 1}, {2, 0}};
        int[][] bikes2 = new int[][]{{1, 0}, {2, 2}, {2, 1}};
        int[] expected2 = new int[]{0, 2, 1};
        int[] actual2 = assignBikesCompareSort(workers2, bikes2);
        int[] actual22 = assignBikesCountingSort(workers2, bikes2);
        assertArrayEquals(expected2, actual2);
        assertArrayEquals(expected2, actual22);
    }
}
