import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ParallelCourses {

    /**
     * You are given an integer n, which indicates that there are n courses labeled from 1 to n. You are also given an array
     * relations where relations[i] = [prevCoursei, nextCoursei], representing a prerequisite relationship between course
     * prevCoursei and course nextCoursei: course prevCoursei has to be taken before course nextCoursei.
     * <p>
     * In one semester, you can take any number of courses as long as you have taken all the prerequisites in the previous
     * semester for the courses you are taking.
     * <p>
     * Return the minimum number of semesters needed to take all courses. If there is no way to take all the courses, return -1.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 5000
     * 1 <= relations.length <= 5000
     * relations[i].length == 2
     * 1 <= prevCoursei, nextCoursei <= n
     * prevCoursei != nextCoursei
     * All the pairs [prevCoursei, nextCoursei] are unique.
     * <p>
     * Approach: Topological Sorting (BFS)
     * This problem is essentially a topological sorting question, and it can be done by DFS or BFS. BFS is more straightforward
     * here because we need to return the minimum semesters needed. It's basically asking how many layers in the graph after
     * it's topologically sorted, and we can compute that number on the fly since we keep removing nodes with 0 in degrees from
     * the graph and these nodes are the courses should've been taken in the same semester. DFS also works, the problem will be
     * translated into finding the longest path in the graph. It can be done after the graph is sorted or calculated on the fly
     * as well. If there is a cycle in the graph, then return -1
     * <p>
     * Time: O(N + E) we need to visit every node and edge once in order to sort the graph
     * Space: O(N + E) need to convert the input array into a graph, which takes O(E) space, and we also need to keep track of
     * visited vertexes, which takes O(N)
     */
    public int minimumSemestersBFS(int n, int[][] relations) {
        // need one more space since courses are 1-indexed
        ArrayList<Integer>[] graph = new ArrayList[n + 1];
        // also need to keep track of the in degree of each node
        int[] inDegrees = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] relation : relations) {
            graph[relation[0]].add(relation[1]);
            inDegrees[relation[1]]++;
        }

        // need a queue to run BFS,
        // add all nodes with 0 in degree into the queue as starting points
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            if (inDegrees[i] == 0) queue.add(i);
        }
        // also need a set to keep track of visited nodes
        Set<Integer> visited = new HashSet<>();
        int numOfSemesters = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int curr = queue.poll();

                if (!visited.contains(curr)) {
                    visited.add(curr);

                    // search all neighbors
                    for (int neighbor : graph[curr]) {
                        // decrement in degrees - pretend to remove current node from the graph
                        inDegrees[neighbor]--;

                        // if there is a new node with 0 in degree, add it into the queue
                        if (inDegrees[neighbor] == 0) queue.add(neighbor);
                    }
                }
            }
            // increment the number of semesters after current layer is done
            numOfSemesters++;
        }
        // we can only finish all courses when the visited set equals to n
        // otherwise, there must have been a cycle in the graph, return -1
        return visited.size() == n ? numOfSemesters : -1;
    }

    /**
     * Approach 2: DFS
     * This can be translated into a DFS approach too. Essentially, we need an integer array to keep track of the max path
     * length until node i. We can first mark visit[i] as -1 which means it's under visit, and if we reach a visiting node,
     * then there is a cycle, we can directly return -1. Otherwise, it will be eventually assigned to the correct value.
     * <p>
     * Time: O(N + E) still need to go through all nodes and edges to get the correct result
     * Space: O(N + E) similarly, we need O(E) to build the graph, O(N) for the visit 1-D array and the call stack will take
     * up to O(N) space as well
     */
    public int minimumSemestersDFS(int n, int[][] relations) {
        // build the graph, similar to BFS approach
        ArrayList<Integer>[] graph = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] relation : relations) {
            graph[relation[0]].add(relation[1]);
        }

        int maxLength = 0;
        // need a 1-D array to keep track of the longest path until each node
        // visit[i] = -1 means there is a cycle detected
        int[] visit = new int[n + 1];
        // starting from each node - sort the entire graph
        for (int i = 1; i <= n; i++) {
            int length = findLongestPathAndDetectCycle(i, visit, graph);
            // if there is a cycle, return -1
            if (length == -1) return -1;
            // otherwise, update the max length
            maxLength = Math.max(maxLength, length);
        }
        return maxLength;
    }

    private int findLongestPathAndDetectCycle(int curr, int[] visit, ArrayList<Integer>[] graph) {
        if (visit[curr] != 0) return visit[curr];
        // mark the current node as visiting
        visit[curr] = -1;

        // we need at least one semester to take current course
        int maxLength = 1;
        // search all neighbors
        for (int neighbor : graph[curr]) {
            // execute DFS and record the longest path on the way
            int length = findLongestPathAndDetectCycle(neighbor, visit, graph);
            // if we reach a visiting node, then there is a cycle detected, return -1
            if (length == -1) return -1;
            // otherwise, update the max length
            maxLength = Math.max(maxLength, length + 1);
        }
        // after DFS is done - assign the max lenght to current node
        visit[curr] = maxLength;
        return maxLength;
    }

    @Test
    public void minimumSemestersTest() {
        /**
         * Example 1:
         * Input: n = 3, relations = [[1,3],[2,3]]
         * Output: 2
         * Explanation: The figure above represents the given graph.
         * In the first semester, you can take courses 1 and 2.
         * In the second semester, you can take course 3.
         */
        assertEquals(2, minimumSemestersBFS(3, new int[][]{{1, 3}, {2, 3}}));
        assertEquals(2, minimumSemestersDFS(3, new int[][]{{1, 3}, {2, 3}}));
        /**
         * Example 2:
         * Input: n = 3, relations = [[1,2],[2,3],[3,1]]
         * Output: -1
         * Explanation: No course can be studied because they are prerequisites of each other.
         */
        assertEquals(-1, minimumSemestersBFS(3, new int[][]{{1, 2}, {2, 3}, {3, 1}}));
        assertEquals(-1, minimumSemestersDFS(3, new int[][]{{1, 2}, {2, 3}, {3, 1}}));
    }
}
