import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TimeNeededToInformAllEmployees {

    /**
     * A company has n employees with a unique ID for each employee from 0 to n - 1. The head of the company is the one with
     * headID.
     * <p>
     * Each employee has one direct manager given in the manager array where manager[i] is the direct manager of the i-th
     * employee, manager[headID] = -1. Also, it is guaranteed that the subordination relationships have a tree structure.
     * <p>
     * The head of the company wants to inform all the company employees of an urgent piece of news. He will inform his
     * direct subordinates, and they will inform their subordinates, and so on until all employees know about the urgent news.
     * <p>
     * The i-th employee needs informTime[i] minutes to inform all of his direct subordinates (i.e., After informTime[i]
     * minutes, all his direct subordinates can start spreading the news).
     * <p>
     * Return the number of minutes needed to inform all the employees about the urgent news.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 10^5
     * 0 <= headID < n
     * manager.length == n
     * 0 <= manager[i] < n
     * manager[headID] == -1
     * informTime.length == n
     * 0 <= informTime[i] <= 1000
     * informTime[i] == 0 if employee i has no subordinates.
     * It is guaranteed that all the employees can be informed.
     * <p>
     * Approach 1: Top Down DFS
     * Since there might be multiple branches after a single node and their total inform times are different, the time required
     * to inform all the nodes below a single node will be determined the maximum branch. Hence, DFS is required to visit
     * the entire branch before switching to another. As a top-down approach, we could start from the head of employee and
     * recursively traverse until the deepest leave and return the result from there.
     * <p>
     * Time: O(n) we will visit each node once
     * Space: O(n)
     */
    public int numOfMinutesTopDown(int n, int headID, int[] manager, int[] informTime) {
        // build the tree structure first;
        ArrayList<Integer>[] subordinates = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            subordinates[i] = new ArrayList<>();
        }
        // add all direct subordinates to manager[i]
        for (int i = 0; i < n; i++) {
            if (manager[i] != -1) subordinates[manager[i]].add(i);
        }
        return dfsTopDown(headID, subordinates, informTime);
    }

    private int dfsTopDown(int employeeId, ArrayList<Integer>[] tree, int[] informTime) {
        // base case, return 0 if it's a leaf node
        if (tree[employeeId].isEmpty()) return 0;
        // need to find maximum inform time of all branches
        int maxTimeToInformAllSubordinates = 0;
        for (int subordinate : tree[employeeId]) {
            maxTimeToInformAllSubordinates = Math.max(maxTimeToInformAllSubordinates, dfsTopDown(subordinate, tree, informTime));
        }
        // the total time at current node would be curr time + max time in its child branch
        return informTime[employeeId] + maxTimeToInformAllSubordinates;
    }

    /**
     * Approach 2: Bottom Up DFS
     * We can also calculate it in the reverse way. Essentially, if we want to know how much time needed to inform a child
     * node, we can look the tree upward and ask his manager the time needed to inform him. We can make all the employees
     * on the path until the root node as visited. How to mark them as visited? Based on the problem statement, we can change
     * the manager to -1, which means it cannot be looked upward. The final result will be the largest path sum.
     * <p>
     * Time: O(n) still need to visit all nodes once
     * Space: O(n) the call stack will still take O(n) time
     */
    public int numOfMinutesBottomUp(int n, int headID, int[] manager, int[] informTime) {
        int res = 0;
        for (int i = 0; i < n; i++) {
            // for each employee - compute the largest path sum from root to itself
            res = Math.max(res, dfsBottomUp(i, manager, informTime));
        }
        return res;
    }

    private int dfsBottomUp(int employeeId, int[] manager, int[] informTime) {
        // only look upward if this branch has been visited
        if (manager[employeeId] != -1) {
            // we need to know how much time needed to inform his direct manager
            informTime[employeeId] += dfsBottomUp(manager[employeeId], manager, informTime);
            manager[employeeId] = -1;
        }
        // if the node has been visited - return it directly
        return informTime[employeeId];
    }

    @Test
    public void numOfMinutesTest() {
        /**
         * Example 1:
         * Input: n = 1, headID = 0, manager = [-1], informTime = [0]
         * Output: 0
         * Explanation: The head of the company is the only employee in the company.
         */
        assertEquals(0, numOfMinutesTopDown(1, 0, new int[]{-1}, new int[]{0}));
        assertEquals(0, numOfMinutesBottomUp(1, 0, new int[]{-1}, new int[]{0}));
        /**
         * Example 2:
         * Input: n = 6, headID = 2, manager = [2,2,-1,2,2,2], informTime = [0,0,1,0,0,0]
         * Output: 1
         * Explanation: The head of the company with id = 2 is the direct manager of all the employees in the company and
         * needs 1 minute to inform them all.
         */
        assertEquals(1, numOfMinutesTopDown(6, 2, new int[]{2, 2, -1, 2, 2, 2},
                new int[]{0, 0, 1, 0, 0, 0}));
        assertEquals(1, numOfMinutesBottomUp(6, 2, new int[]{2, 2, -1, 2, 2, 2},
                new int[]{0, 0, 1, 0, 0, 0}));
        /**
         * Example 3:
         * Input: n = 7, headID = 6, manager = [1,2,3,4,5,6,-1], informTime = [0,6,5,4,3,2,1]
         * Output: 21
         * Explanation: The head has id = 6. He will inform employee with id = 5 in 1 minute.
         * The employee with id = 5 will inform the employee with id = 4 in 2 minutes.
         * The employee with id = 4 will inform the employee with id = 3 in 3 minutes.
         * The employee with id = 3 will inform the employee with id = 2 in 4 minutes.
         * The employee with id = 2 will inform the employee with id = 1 in 5 minutes.
         * The employee with id = 1 will inform the employee with id = 0 in 6 minutes.
         * Needed time = 1 + 2 + 3 + 4 + 5 + 6 = 21.
         */
        assertEquals(21, numOfMinutesTopDown(7, 6, new int[]{1, 2, 3, 4, 5, 6, -1},
                new int[]{0, 6, 5, 4, 3, 2, 1}));
        assertEquals(21, numOfMinutesBottomUp(7, 6, new int[]{1, 2, 3, 4, 5, 6, -1},
                new int[]{0, 6, 5, 4, 3, 2, 1}));
        /**
         * Example 4:
         * Input: n = 15, headID = 0, manager = [-1,0,0,1,1,2,2,3,3,4,4,5,5,6,6], informTime = [1,1,1,1,1,1,1,0,0,0,0,0,0,0,0]
         * Output: 3
         * Explanation: The first minute the head will inform employees 1 and 2.
         * The second minute they will inform employees 3, 4, 5 and 6.
         * The third minute they will inform the rest of employees.
         */
        assertEquals(3, numOfMinutesTopDown(15, 0, new int[]{-1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6},
                new int[]{1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}));
        assertEquals(3, numOfMinutesBottomUp(15, 0, new int[]{-1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6},
                new int[]{1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}));
    }
}
