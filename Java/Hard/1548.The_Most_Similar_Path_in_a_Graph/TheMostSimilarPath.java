import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TheMostSimilarPath {

    /**
     * We have n cities and m bi-directional roads where roads[i] = [ai, bi] connects city ai with city bi. Each city has a
     * name consisting of exactly 3 upper-case English letters given in the string array names. Starting at any city x, you
     * can reach any city y where y != x (i.e. the cities and the roads are forming an undirected connected graph).
     * <p>
     * You will be given a string array targetPath. You should find a path in the graph of the same length and with the
     * minimum edit distance to targetPath.
     * <p>
     * You need to return the order of the nodes in the path with the minimum edit distance, The path should be of the same
     * length of targetPath and should be valid (i.e. there should be a direct road between ans[i] and ans[i + 1]). If there
     * are multiple answers return any one of them.
     * <p>
     * The edit distance is defined as follows:
     * def editDistance(targetPath, myPath) {
     * dis := 0
     * a := targetPath.length
     * b := myPath.length
     * if a != b {
     * return 1000000000
     * }
     * for (i := 0; i < a; i += 1) {
     * if targetPath[i] != myPath[i] {
     * dist += 1
     * }
     * }
     * return dist
     * }
     * <p>
     * Constraints:
     * <p>
     * 2 <= n <= 100
     * m == roads.length
     * n - 1 <= m <= (n * (n - 1) / 2)
     * 0 <= ai, bi <= n - 1
     * ai != bi
     * The graph is guaranteed to be connected and each pair of nodes may have at most one direct road.
     * names.length == n
     * names[i].length == 3
     * names[i] consists of upper-case English letters.
     * There can be two cities with the same name.
     * 1 <= targetPath.length <= 100
     * targetPath[i].length == 3
     * targetPath[i] consists of upper-case English letters.
     * <p>
     * Approach: DP + Path Construction
     * Denote dp[i][j] as the maxScore (maximum matched cities until position j on target path) until node i.
     * For example dp[2][3] means the maximum score we can obtain until node 2 (which has length 3). How to gain score?
     * if names[i] == targetPath[j] for given (i, j) then we can add one to that position. However, it would only give
     * the largest score we can get (also targetPath.length - maxScore = min edit distance). We need a way to somewhat
     * construct the path eventually. We also need a prev[i][j] array which stores the previous node that can give that
     * maximum score. When the dp is done, we can construct the path starting from the destination node (which has the
     * maximum score) until the starting node.
     * <p>
     * Time: O(mn^2) where n is the number of cities, m is the length of target path. We need traverse the entire 2-D array
     * to compute the maxScore & prev node. At a given cell (i, j) we need to traverse all its possible neighbors to get
     * the maximum score. If all nodes are connected with each other in the graph, then we need O(n) as well at each cell,
     * which ends up being O(mn^2)
     * Space: O(mn) need two 2-D arrays for DP + an array of list to construct the graph
     */
    public List<Integer> mostSimilar(int n, int[][] roads, String[] names, String[] targetPath) {
        ArrayList<Integer>[] edges = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<>();
        }
        // add bi-directional edges into the graph
        for (int[] road : roads) {
            edges[road[0]].add(road[1]);
            edges[road[1]].add(road[0]);
        }

        int pathLength = targetPath.length;
        int[][] maxScore = new int[pathLength][n];
        int[][] prevNode = new int[pathLength][n];

        // initialization - we can gain 1 score if the city name equals to the first city name on the path
        for (int i = 0; i < n; i++) {
            maxScore[0][i] = names[i].equals(targetPath[0]) ? 1 : 0;
        }

        // transition - compute the max score we can get until node i + update previous node on the path
        for (int i = 1; i < pathLength; i++) {
            for (int j = 0; j < n; j++) {
                // initialize max score as -1
                maxScore[i][j] = -1;
                // still, we can get 1 score if the cities are the same
                int score = names[j].equals(targetPath[i]) ? 1 : 0;
                // find max score from all the neighbors
                for (int neighbor : edges[j]) {
                    // if we can gain a larger score from its neighbor - update it
                    if (score + maxScore[i - 1][neighbor] > maxScore[i][j]) {
                        maxScore[i][j] = score + maxScore[i - 1][neighbor];
                        prevNode[i][j] = neighbor;
                    }
                }
            }
        }

        // we need to find the destination node which gives the largest score (== min edit distance)
        int currMax = -1, nodeOnPath = 0;
        for (int i = 0; i < n; i++) {
            if (maxScore[pathLength - 1][i] > currMax) {
                currMax = maxScore[pathLength - 1][i];
                nodeOnPath = i;
            }
        }

        LinkedList<Integer> mostSimilarPath = new LinkedList<>();
        // add the destination node
        mostSimilarPath.addFirst(nodeOnPath);

        // and construct the entire path in a reverse way
        for (int i = pathLength - 1; i > 0; i--) {
            // keep adding previous node on the path
            mostSimilarPath.addFirst(prevNode[i][nodeOnPath]);
            nodeOnPath = prevNode[i][nodeOnPath];
        }
        return mostSimilarPath;
    }

    @Test
    public void mostSimilarTest() {
        /**
         * Example 1:
         * Input: n = 5, roads = [[0,2],[0,3],[1,2],[1,3],[1,4],[2,4]], names = ["ATL","PEK","LAX","DXB","HND"],
         * targetPath = ["ATL","DXB","HND","LAX"]
         * Output: [0,2,4,2]
         * Explanation: [0,2,4,2], [0,3,0,2] and [0,3,1,2] are accepted answers.
         * [0,2,4,2] is equivalent to ["ATL","LAX","HND","LAX"] which has edit distance = 1 with targetPath.
         * [0,3,0,2] is equivalent to ["ATL","DXB","ATL","LAX"] which has edit distance = 1 with targetPath.
         * [0,3,1,2] is equivalent to ["ATL","DXB","PEK","LAX"] which has edit distance = 1 with targetPath.
         */
        int[][] roads1 = new int[][]{{0, 2}, {0, 3}, {1, 2}, {1, 3}, {1, 4}, {2, 4}};
        String[] names1 = new String[]{"ATL", "PEK", "LAX", "DXB", "HND"};
        String[] targetPath1 = new String[]{"ATL", "DXB", "HND", "LAX"};
        assertEquals(List.of(0, 3, 0, 2), mostSimilar(5, roads1, names1, targetPath1));
        /**
         * Example 2:
         * Input: n = 4, roads = [[1,0],[2,0],[3,0],[2,1],[3,1],[3,2]], names = ["ATL","PEK","LAX","DXB"],
         * targetPath = ["ABC","DEF","GHI","JKL","MNO","PQR","STU","VWX"]
         * Output: [0,1,0,1,0,1,0,1]
         * Explanation: Any path in this graph has edit distance = 8 with targetPath.
         */
        int[][] roads2 = new int[][]{{1, 0}, {2, 0}, {3, 0}, {2, 1}, {3, 1}, {3, 2}};
        String[] names2 = new String[]{"ATL", "PEK", "LAX", "DXB"};
        String[] targetPath2 = new String[]{"ABC", "DEF", "GHI", "JKL", "MNO", "PQR", "STU", "VWX"};
        assertEquals(List.of(1, 0, 1, 0, 1, 0, 1, 0), mostSimilar(4, roads2, names2, targetPath2));
        /**
         * Example 3:
         * Input: n = 6, roads = [[0,1],[1,2],[2,3],[3,4],[4,5]], names = ["ATL","PEK","LAX","ATL","DXB","HND"],
         * targetPath = ["ATL","DXB","HND","DXB","ATL","LAX","PEK"]
         * Output: [3,4,5,4,3,2,1]
         * Explanation: [3,4,5,4,3,2,1] is the only path with edit distance = 0 with targetPath.
         * It's equivalent to ["ATL","DXB","HND","DXB","ATL","LAX","PEK"]
         */
        int[][] roads3 = new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
        String[] names3 = new String[]{"ATL", "PEK", "LAX", "ATL", "DXB", "HND"};
        String[] targetPath3 = new String[]{"ATL", "DXB", "HND", "DXB", "ATL", "LAX", "PEK"};
        assertEquals(List.of(3, 4, 5, 4, 3, 2, 1), mostSimilar(6, roads3, names3, targetPath3));
    }
}
