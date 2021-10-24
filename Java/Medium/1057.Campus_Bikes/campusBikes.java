import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import static org.junit.Assert.assertArrayEquals;

public class campusBikes {

    /**
     * On a campus represented as a 2D grid, there are N workers and M bikes, with N <= M. Each worker and bike is a 2D coordinate on
     * this grid.
     * <p>
     * Our goal is to assign a bike to each worker. Among the available bikes and workers, we choose the (worker, bike) pair with the
     * shortest Manhattan distance between each other, and assign the bike to that worker. (If there are multiple (worker, bike) pairs
     * with the same shortest Manhattan distance, we choose the pair with the smallest worker index; if there are multiple ways to do that,
     * we choose the pair with the smallest bike index). We repeat this process until there are no available workers.
     * <p>
     * The Manhattan distance between two points p1 and p2 is Manhattan(p1, p2) = |p1.x - p2.x| + |p1.y - p2.y|.
     * <p>
     * Return a vector ans of length N, where ans[i] is the index (0-indexed) of the bike that the i-th worker is assigned to.
     * <p>
     * Note:
     * <p>
     * 0 <= workers[i][j], bikes[i][j] < 1000
     * All worker and bike locations are distinct.
     * 1 <= workers.length <= bikes.length <= 1000
     * <p>
     * Approach 1: Compare Sort
     * 本质上，因为总要先assign距离最短的（worker，bike）pair，因此需要找到所有的worker和bike的组合，然后对所有pair进行排序，排序规则如下：
     * 1.若距离不等，按距离从小到大排序
     * 2.若距离相等，按worker的index从小到大排序
     * 3.若前两者都相等，最后按bike的index从小到大排序
     * <p>
     * 因此只要按照上述规则对所有pair进行排序，然后通过greedy思想，从小到大遍历所有的pair，若当前worker和bike都未被分配，则将二者连接起来即可
     * <p>
     * Time: O(mn*log(mn)) 对于m个worker和n个bike，总共有mn个pair，对所有pair进行排序需要O(mn*log(mn))时间
     * Space: O(mn) 需要将所有的pair都记录下来然后排序
     */
    public int[] assignBikesCompareSort(int[][] workers, int[][] bikes) {
        PriorityQueue<Pair> pairs = new PriorityQueue<>((a, b) -> {
            if (a.distance == b.distance) {
                if (a.workerIndex == b.workerIndex) return Integer.compare(a.bikeIndex, b.bikeIndex);
                return Integer.compare(a.workerIndex, b.workerIndex);
            }
            return Integer.compare(a.distance, b.distance);
        });

        //遍历所有pair，计算距离，将（worker index，bike index，dist）的pair放入priority queue中
        for (int i = 0; i < workers.length; i++) {
            for (int j = 0; j < bikes.length; j++) {
                pairs.add(new Pair(workers[i], bikes[j], i, j));
            }
        }

        //然后遍历所有pair，只有当worker和bike都未被分配时，才将二者连接起来
        //可以直接用数组来进行映射
        int[] workerAssignment = new int[workers.length], bikeAssignment = new int[bikes.length];
        //初始化为-1，意味着未被分配
        Arrays.fill(workerAssignment, -1);
        Arrays.fill(bikeAssignment, -1);
        //记录被分配的worker的个数，若所有worker都被分配完毕，不需要再进行计算
        int assignmentCount = 0;

        while (!pairs.isEmpty()) {
            Pair curr = pairs.poll();
            int workerIndex = curr.workerIndex, bikeIndex = curr.bikeIndex;

            if (workerAssignment[workerIndex] == -1 && bikeAssignment[bikeIndex] == -1) {
                workerAssignment[workerIndex] = bikeIndex;
                bikeAssignment[bikeIndex] = workerIndex;
                assignmentCount++;
                if (assignmentCount == workers.length) {
                    break;
                }
            }
        }
        return workerAssignment;
    }

    private static class Pair {
        int workerIndex;
        int bikeIndex;
        int distance;

        public Pair(int[] worker, int[] bike, int workerIndex, int bikeIndex) {
            this.workerIndex = workerIndex;
            this.bikeIndex = bikeIndex;
            this.distance = Math.abs(worker[0] - bike[0]) + Math.abs(worker[1] - bike[1]);
        }
    }

    /**
     * Approach 2: Counting Sort
     * 对于compare sort而言，O(nlogn)已经是极限，因此考虑使用counting sort缩短排序时间，注意到worker和bike的坐标都是[0, 1000)的数字，因此对于grid中
     * 任意两点，其最大曼哈顿距离为2000（即(0,0)到(1000, 1000)），因此可以对每一个dist，与（worker index，bike index）相对应起来，因为是先固定worker
     * 的index，再找所有的bike index的组合，因此每个dist下对应的pair已经是排好序的。最后只需要从小到大遍历所有的距离可能值，若图中的点能得到该距离，再
     * 继续遍历该距离下的所有pair，找到未被分配的worker和bike，然后进行分配即可。
     * <p>
     * Time: O(mn) 因为只需要使用counting sort，只需要遍历所有的pair，将其和对应的距离映射起来，之后再遍历一遍所有的pair进行分配即可
     * Space: O(mn) 需要经所有的pair和对应的距离映射起来
     */
    public int[] assignBikesCountingSort(int[][] workers, int[][] bikes) {
        //距离最大值为2000，因此需要2001个位置，每个位置记录能得到该距离的所有可能组合
        List<IndexPair>[] buckets = new ArrayList[2001];
        for (int i = 0; i < 2001; i++) {
            buckets[i] = new ArrayList<>();
        }

        // add all pairs into the desired distance bucket list
        for (int i = 0; i < workers.length; i++) {
            for (int j = 0; j < bikes.length; j++) {
                int dist = Math.abs(workers[i][0] - bikes[j][0]) + Math.abs(workers[i][1] - bikes[j][1]);
                buckets[dist].add(new IndexPair(i, j));
            }
        }

        int[] workerAssignment = new int[workers.length], bikeAssignment = new int[bikes.length];
        Arrays.fill(workerAssignment, -1);
        Arrays.fill(bikeAssignment, -1);
        int assignmentCount = 0;

        for (int i = 1; i <= 2000; i++) {
            //如果组成的pair能得到当前距离，则需要遍历对应的所有pair
            for (IndexPair curr : buckets[i]) {
                int workerIndex = curr.workerIndex, bikeIndex = curr.bikeIndex;

                if (workerAssignment[workerIndex] == -1 && bikeAssignment[bikeIndex] == -1) {
                    workerAssignment[workerIndex] = bikeIndex;
                    bikeAssignment[bikeIndex] = workerIndex;
                    assignmentCount++;

                    if (assignmentCount == workers.length) break;
                }
            }
        }
        return workerAssignment;
    }

    private static class IndexPair {
        int workerIndex;
        int bikeIndex;

        public IndexPair(int workerIndex, int bikeIndex) {
            this.workerIndex = workerIndex;
            this.bikeIndex = bikeIndex;
        }
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
