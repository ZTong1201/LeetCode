import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertArrayEquals;

public class courseScheduleII {

    /**
     * There are a total of n courses you have to take, labeled from 0 to n-1.
     * Some courses may have prerequisites, for example to take course 0 you have to first take course 1, which is expressed as a pair: [0,1]
     * Given the total number of courses and a list of prerequisite pairs, return the ordering of courses you should take to finish
     * all courses.
     * <p>
     * There may be multiple correct orders, you just need to return one of them. If it is impossible to finish all courses,
     * return an empty array.
     * Note:
     * <p>
     * The input prerequisites is a graph represented by a list of edges, not adjacency matrices.
     * You may assume that there are no duplicate edges in the input prerequisites.
     * <p>
     * Approach 1: Topological sorting (DFS)
     * 此题和207类似，唯一区别是需返回拓扑排序后结果。仍然可以用DFS来进行拓扑排序，注意的是最终返回的顺序即为访问顺序的倒序。同时若给定的图中有环，则需要返回一个
     * 空的数组。只需对207做如下改动即可
     * <p>
     * 1.若判断出图中有环，不在返回true，而是返回一个空数组
     * 2.若在判断过程中未找到环，则在该节点访问结束后（即将visit[i]标记为2后），将当前节点插入链表
     * 3.有向图遍历结束后，将链表顺序反转，得到正确的拓扑排序
     * <p>
     * Time: O(V + E) 仍需要访问所有的节点和边来进行拓扑排序
     * Space: O(V + E) 需要一个array of arraylist来存储所有的边，需要O(E)，同时需要一个数组记录所有节点状态，O(V)，以及一个额外的链表记录排序后的节点顺序，
     * 需要O(V)
     */
    public int[] findOrderDFS(int numCourses, int[][] prerequisites) {
        ArrayList<Integer>[] edges = new ArrayList[numCourses];
        for (int i = 0; i < numCourses; i++) {
            edges[i] = new ArrayList<>();
        }
        for (int[] edge : prerequisites) {
            edges[edge[1]].add(edge[0]);
        }
        int[] visit = new int[numCourses];
        LinkedList<Integer> res = new LinkedList<>();

        for (int i = 0; i < numCourses; i++) {
            //若图中有环，则之间返回空数组
            if (hasCycle(i, edges, visit, res)) return new int[0];
        }
        //若无环，则将链表中的元素放入return数组中
        int[] order = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            //将链表反转得到正确的拓扑顺序
            order[i] = res.removeLast();
        }
        return order;
    }

    private boolean hasCycle(int curr, ArrayList<Integer>[] edges, int[] visit, LinkedList<Integer> res) {
        if (visit[curr] == 1) return true;
        if (visit[curr] == 2) return false;

        visit[curr] = 1;
        for (int neighbor : edges[curr]) {
            if (hasCycle(neighbor, edges, visit, res)) return true;
        }
        visit[curr] = 2;
        //若无环，则需要在当前节点访问结束之后，将当前节点放入链表
        //本质上在做postorder DFS， 即最后完成的课程在链表的最前面
        //在返回最终结果时，需将链表反转
        res.add(curr);
        return false;
    }

    @Test
    public void findOrderDFSTest() {
        /**
         * Example 1:
         * Input: 2, [[1,0]]
         * Output: [0,1]
         * Explanation: There are a total of 2 courses to take. To take course 1 you should have finished
         *              course 0. So the correct course order is [0,1] .
         */
        int[][] prerequisites1 = new int[][]{{1, 0}};
        int[] expected1 = findOrderDFS(2, prerequisites1);
        int[] actual1 = new int[]{0, 1};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: 4, [[1,0],[2,0],[3,1],[3,2]]
         * Output: [0,1,2,3] or [0,2,1,3]
         * Explanation: There are a total of 4 courses to take. To take course 3 you should have finished both
         *              courses 1 and 2. Both courses 1 and 2 should be taken after you finished course 0.
         *              So one correct course order is [0,1,2,3]. Another correct ordering is [0,2,1,3] .
         */
        int[][] prerequisites2 = new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        int[] expected2 = findOrderDFS(4, prerequisites2);
        int[] actual2 = new int[]{0, 2, 1, 3};
        assertArrayEquals(expected2, actual2);
    }

    /**
     * Approach 2: Breadth-First Search (BFS)
     * <p>
     * 可以利用BFS（queue）来实现拓扑排序。基本思想为，统计每个节点的入度（即有多少个节点有边指向该节点），入度为0的节点则应该在排序后的最前面，入度同为0的节点
     * 则可以以任意顺序进入最终结果。
     * <p>
     * 所以，要实现BFS，首先需要一个array来记录所有节点的入度，以及每个一个邻接的map来记录起始节点和终止节点的list，同时用queue来记录待访问节点
     * 算法大致如下
     * 1.遍历所有的边，记录入度和邻接map
     * 2.遍历入度array，将所有入度为0的节点加入queue
     * 3.从queue取出一个元素（入度为0后才能加入queue），将其加入最终结果
     * 4.在邻接map中找到该图的所有相邻节点，并将其相邻节点的入度减1（相当于将当前节点移除出图）
     * 5.若任意时刻，某个节点的入度为0，则将其加入queue中
     * 6.重复3-5直到queue为空
     * <p>
     * Time: O(V + E), 遍历了所有节点两遍，第一遍记录入度，第二遍进行排序，同时遍历了每条边各一遍来建立邻接map
     * Space: O(V + E), 若最终结果不算space，则需要一个array来记录节点入度，同时需要一个邻接map来记录每一条边
     */
    public int[] findOrderBFS(int numCourses, int[][] prerequisites) {
        ArrayList<Integer>[] edges = new ArrayList[numCourses];
        int[] inDegree = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            edges[i] = new ArrayList<>();
        }
        for (int[] edge : prerequisites) {
            //邻接map要以起点为key，终点为value
            edges[edge[1]].add(edge[0]);
            //入度array记录的是终点的入度
            inDegree[edge[0]] += 1;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) queue.add(i);
        }

        int i = 0;
        int[] order = new int[numCourses];
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            //因为入度为0后才能进入queue，因此queue中元素可直接加入最终结果
            order[i++] = curr;

            //若当前节点有相邻节点，则将其相邻节点入度减1
            for (int neighbor : edges[curr]) {
                inDegree[neighbor] -= 1;

                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }
        //只有所有的节点都可能被排序才返回结果
        if (i == numCourses) {
            return order;
        }
        //否则返回空array
        return new int[0];
    }


    @Test
    public void findOrderBFSTest() {
        /**
         * Example 1:
         * Input: 2, [[1,0]]
         * Output: [0,1]
         * Explanation: There are a total of 2 courses to take. To take course 1 you should have finished
         *              course 0. So the correct course order is [0,1] .
         */
        int[][] prerequisites1 = new int[][]{{1, 0}};
        int[] expected1 = findOrderBFS(2, prerequisites1);
        int[] actual1 = new int[]{0, 1};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: 4, [[1,0],[2,0],[3,1],[3,2]]
         * Output: [0,1,2,3] or [0,2,1,3]
         * Explanation: There are a total of 4 courses to take. To take course 3 you should have finished both
         *              courses 1 and 2. Both courses 1 and 2 should be taken after you finished course 0.
         *              So one correct course order is [0,1,2,3]. Another correct ordering is [0,2,1,3] .
         */
        int[][] prerequisites2 = new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        int[] expected2 = findOrderBFS(4, prerequisites2);
        int[] actual2 = new int[]{0, 1, 2, 3};
        assertArrayEquals(expected2, actual2);
    }
}
