import org.junit.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.junit.Assert.assertArrayEquals;

public class SingleThreadedCPU {

    /**
     * You are given n tasks labeled from 0 to n - 1 represented by a 2D integer array tasks, where tasks[i] =
     * [enqueueTimei, processingTimei] means that the ith task will be available to process at enqueueTimei and will take
     * processingTimei to finish processing.
     * <p>
     * You have a single-threaded CPU that can process at most one task at a time and will act in the following way:
     * <p>
     * If the CPU is idle and there are no available tasks to process, the CPU remains idle.
     * If the CPU is idle and there are available tasks, the CPU will choose the one with the shortest processing time.
     * If multiple tasks have the same shortest processing time, it will choose the task with the smallest index.
     * Once a task is started, the CPU will process the entire task without stopping.
     * The CPU can finish a task then start a new one instantly.
     * Return the order in which the CPU will process the tasks.
     * <p>
     * Constraints:
     * <p>
     * tasks.length == n
     * 1 <= n <= 10^5
     * 1 <= enqueueTimei, processingTimei <= 10^9
     * <p>
     * Approach: Sort + Heap
     * In order to simulate the process, we need to first sort all the tasks by their start time. Why? Cuz we need to know
     * which tasks become available at certain time. Therefore, we can copy the entire array to a separate array to by
     * carrying over the indexes and sort it by enqueue time. Then there will be two options:
     * 1. If there is no task available at current time, directly jump to the next enqueue when there are new tasks become
     * available
     * 2. There are some tasks in the queue, we need to process the one with the shortest processing time and the smallest
     * index. This can be done by using the priority queue.
     * We keep the above steps until all tasks have been executed.
     * <p>
     * Time: O(nlogn) the sorting algorithm will take O(nlogn), then need add some tasks into the queue, which in the worst
     * case will take O(n) time. Removing and adding in heap will take O(logn) time, hence in total we will also have O(nlogn) time
     * Space: O(n) we need to copy the entire array + adding tasks into the heap
     */
    public int[] getOrder(int[][] tasks) {
        int n = tasks.length;
        // need to copy the entire tasks array by carrying over their indexes
        int[][] taskWithIndex = new int[n][3];
        // we have the triplet for [index, enqueue time, process time]
        for (int i = 0; i < n; i++) {
            taskWithIndex[i][0] = i;
            taskWithIndex[i][1] = tasks[i][0];
            taskWithIndex[i][2] = tasks[i][1];
        }
        // sort the array by the enqueue time
        Arrays.sort(taskWithIndex, (a, b) -> (Integer.compare(a[1], b[1])));

        // instantiate a min heap to sort tasks by processing time then by index
        PriorityQueue<int[]> taskQueue = new PriorityQueue<>((a, b) -> {
            if (a[2] == b[2]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[2], b[2]);
        });

        int[] res = new int[n];
        // starting from 0 second to process tasks
        int currTime = 0, resIndex = 0, taskIndex = 0;

        // will finish when all tasks have been processed
        while (resIndex < n) {
            // if there are any new tasks become available at current time, add them into the queue
            while (taskIndex < n && taskWithIndex[taskIndex][1] <= currTime) {
                taskQueue.add(taskWithIndex[taskIndex]);
                taskIndex++;
            }

            // scenario 1: if there is no available tasks, update the current time
            if (taskQueue.isEmpty()) {
                currTime = taskWithIndex[taskIndex][1];
                continue;
            }

            // otherwise, pop the task with the smallest processing time and the smallest index
            int[] currTask = taskQueue.poll();
            res[resIndex++] = currTask[0];
            // update the currTime to the time when this task is complete
            currTime += currTask[2];
        }
        return res;
    }

    @Test
    public void getOrderTest() {
        /**
         * Example 1:
         * Input: tasks = [[1,2],[2,4],[3,2],[4,1]]
         * Output: [0,2,3,1]
         * Explanation: The events go as follows:
         * - At time = 1, task 0 is available to process. Available tasks = {0}.
         * - Also at time = 1, the idle CPU starts processing task 0. Available tasks = {}.
         * - At time = 2, task 1 is available to process. Available tasks = {1}.
         * - At time = 3, task 2 is available to process. Available tasks = {1, 2}.
         * - Also at time = 3, the CPU finishes task 0 and starts processing task 2 as it is the shortest. Available tasks = {1}.
         * - At time = 4, task 3 is available to process. Available tasks = {1, 3}.
         * - At time = 5, the CPU finishes task 2 and starts processing task 3 as it is the shortest. Available tasks = {1}.
         * - At time = 6, the CPU finishes task 3 and starts processing task 1. Available tasks = {}.
         * - At time = 10, the CPU finishes task 1 and becomes idle.
         */
        int[] expected1 = new int[]{0, 2, 3, 1};
        int[] actual1 = getOrder(new int[][]{{1, 2}, {2, 4}, {3, 2}, {4, 1}});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: tasks = [[7,10],[7,12],[7,5],[7,4],[7,2]]
         * Output: [4,3,2,0,1]
         * Explanation: The events go as follows:
         * - At time = 7, all the tasks become available. Available tasks = {0,1,2,3,4}.
         * - Also at time = 7, the idle CPU starts processing task 4. Available tasks = {0,1,2,3}.
         * - At time = 9, the CPU finishes task 4 and starts processing task 3. Available tasks = {0,1,2}.
         * - At time = 13, the CPU finishes task 3 and starts processing task 2. Available tasks = {0,1}.
         * - At time = 18, the CPU finishes task 2 and starts processing task 0. Available tasks = {1}.
         * - At time = 28, the CPU finishes task 0 and starts processing task 1. Available tasks = {}.
         * - At time = 40, the CPU finishes task 1 and becomes idle.
         */
        int[] expected2 = new int[]{4, 3, 2, 0, 1};
        int[] actual2 = getOrder(new int[][]{{7, 10}, {7, 12}, {7, 5}, {7, 4}, {7, 2}});
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: tasks = [[19,13],[16,9],[21,10],[32,25],[37,4],[49,24],[2,15],[38,41],
         * [37,34],[33,6],[45,4],[18,18],[46,39],[12,24]]
         * Output: [6,1,2,9,4,10,0,11,5,13,3,8,12,7]
         */
        int[] expected3 = new int[]{6, 1, 2, 9, 4, 10, 0, 11, 5, 13, 3, 8, 12, 7};
        int[] actual3 = getOrder(new int[][]{{19, 13}, {16, 9}, {21, 10}, {32, 25}, {37, 4}, {49, 24}, {2, 15},
                {38, 41}, {37, 34}, {33, 6}, {45, 4}, {18, 18}, {46, 39}, {12, 24}});
        assertArrayEquals(expected3, actual3);
    }
}
