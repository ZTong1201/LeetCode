import org.junit.Test;

import java.util.PriorityQueue;

import static org.junit.Assert.assertArrayEquals;

public class ProcessTasksUsingServers {

    /**
     * You are given two 0-indexed integer arrays servers and tasks of lengths n and m respectively. servers[i] is the weight
     * of the ith server, and tasks[j] is the time needed to process the jth task in seconds.
     * <p>
     * Tasks are assigned to the servers using a task queue. Initially, all servers are free, and the queue is empty.
     * <p>
     * At second j, the jth task is inserted into the queue (starting with the 0th task being inserted at second 0). As long
     * as there are free servers and the queue is not empty, the task in the front of the queue will be assigned to a free
     * server with the smallest weight, and in case of a tie, it is assigned to a free server with the smallest index.
     * <p>
     * If there are no free servers and the queue is not empty, we wait until a server becomes free and immediately assign
     * the next task. If multiple servers become free at the same time, then multiple tasks from the queue will be assigned
     * in order of insertion following the weight and index priorities above.
     * <p>
     * A server that is assigned task j at second t will be free again at second t + tasks[j].
     * <p>
     * Build an array ans of length m, where ans[j] is the index of the server the jth task will be assigned to.
     * <p>
     * Return the array ans.
     * <p>
     * Constraints:
     * <p>
     * servers.length == n
     * tasks.length == m
     * 1 <= n, m <= 2 * 10^5
     * 1 <= servers[i], tasks[j] <= 2 * 10^5
     * <p>
     * Approach: Heap (Priority Queue)
     * Basically, we need to keep two mutually exclusive sets of servers, one is for all free servers, which will be sorted by
     * the weight, and if there was a tie, they will be sorted by the index. The other is for used servers, it will first
     * be sorted by the time it can be freed, then following the same rules as the free servers do. The best way to sort
     * these servers is to use a priority queue (heap). When a task comes in (the index i will be the current time in second),
     * 1. we first check whether we can free any used servers at current second.
     * 2. Then if there are any free servers available - we assign the smallest weighted server with the smallest index
     * 3. If there are no free servers available right now, which means we will eventually assign a used server who ends
     * the first to this task. Therefore, we update the ending time for this server and add it back to the used server queue.
     * <p>
     * Time: O(nlogn + mlogn) initially we need to add all servers into the free server queue, which takes O(nlogn) time. Then
     * for each task we need to free some used servers and assign an appropriate server. It will take O(mlogn) time in the
     * worst case.
     * Space: O(n)
     */
    public int[] assignTasks(int[] servers, int[] tasks) {
        // sort free servers by weight then by index
        PriorityQueue<int[]> freeServers = new PriorityQueue<>((a, b) -> {
            if (a[0] == b[0]) return a[1] - b[1];
            return a[0] - b[0];
        });
        // sort used servers by the ending time, then by weight and index
        PriorityQueue<int[]> usedServerQueue = new PriorityQueue<>((a, b) -> {
            if (a[2] == b[2]) {
                if (a[0] == b[0]) return a[1] - b[1];
                return a[0] - b[0];
            }
            return a[2] - b[2];
        });
        // add all servers into the free server queue with 0 ending time
        // since currently all servers are available at second 0
        for (int i = 0; i < servers.length; i++) {
            freeServers.add(new int[]{servers[i], i, 0});
        }

        int n = tasks.length;
        int[] res = new int[n];

        // try to assign a correct server to the current task
        for (int i = 0; i < n; i++) {
            // first check whether there are any used servers can be freed at second i
            while (!usedServerQueue.isEmpty() && usedServerQueue.peek()[2] <= i) {
                // move those servers to free servers queue
                freeServers.add(usedServerQueue.poll());
            }

            // try to find the first free server
            int[] firstFreeServer;
            // if there is currently no free server
            // assign the server which will end first to the current task
            if (freeServers.isEmpty()) {
                firstFreeServer = usedServerQueue.poll();
                // update the ending time
                firstFreeServer[2] += tasks[i];
            } else {
                // otherwise, we can get a free server for the current task
                firstFreeServer = freeServers.poll();
                // again, the ending time will be updated
                firstFreeServer[2] = i + tasks[i];
            }
            // assign the index to the result list
            res[i] = firstFreeServer[1];
            // add assigned server to the used queue
            usedServerQueue.add(firstFreeServer);
        }
        return res;
    }

    @Test
    public void assignTasksTest() {
        /**
         * Example 1:
         * Input: servers = [3,3,2], tasks = [1,2,3,2,1,2]
         * Output: [2,2,0,2,1,2]
         * Explanation: Events in chronological order go as follows:
         * - At second 0, task 0 is added and processed using server 2 until second 1.
         * - At second 1, server 2 becomes free. Task 1 is added and processed using server 2 until second 3.
         * - At second 2, task 2 is added and processed using server 0 until second 5.
         * - At second 3, server 2 becomes free. Task 3 is added and processed using server 2 until second 5.
         * - At second 4, task 4 is added and processed using server 1 until second 5.
         * - At second 5, all servers become free. Task 5 is added and processed using server 2 until second 7.
         */
        int[] expected1 = new int[]{2, 2, 0, 2, 1, 2};
        int[] actual1 = assignTasks(new int[]{3, 3, 2}, new int[]{1, 2, 3, 2, 1, 2});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: servers = [5,1,4,3,2], tasks = [2,1,2,4,5,2,1]
         * Output: [1,4,1,4,1,3,2]
         * Explanation: Events in chronological order go as follows:
         * - At second 0, task 0 is added and processed using server 1 until second 2.
         * - At second 1, task 1 is added and processed using server 4 until second 2.
         * - At second 2, servers 1 and 4 become free. Task 2 is added and processed using server 1 until second 4.
         * - At second 3, task 3 is added and processed using server 4 until second 7.
         * - At second 4, server 1 becomes free. Task 4 is added and processed using server 1 until second 9.
         * - At second 5, task 5 is added and processed using server 3 until second 7.
         * - At second 6, task 6 is added and processed using server 2 until second 7.
         */
        int[] expected2 = new int[]{1, 4, 1, 4, 1, 3, 2};
        int[] actual2 = assignTasks(new int[]{5, 1, 4, 3, 2}, new int[]{2, 1, 2, 4, 5, 2, 1});
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: servers = [10,63,95,16,85,57,83,95,6,29,71], tasks = [70,31,83,15,32,67,98,65,56,48,38,90,5]
         * Output: [8,0,3,9,5,1,10,6,4,2,7,9,0]
         */
        int[] expected3 = new int[]{8, 0, 3, 9, 5, 1, 10, 6, 4, 2, 7, 9, 0};
        int[] actual3 = assignTasks(new int[]{10, 63, 95, 16, 85, 57, 83, 95, 6, 29, 71},
                new int[]{70, 31, 83, 15, 32, 67, 98, 65, 56, 48, 38, 90, 5});
        assertArrayEquals(expected3, actual3);
    }
}
