import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class taskScheduler {

    /**
     * Given a characters array tasks, representing the tasks a CPU needs to do, where each letter represents a different
     * task. Tasks could be done in any order. Each task is done in one unit of time. For each unit of time, the CPU
     * could complete either one task or just be idle.
     * <p>
     * However, there is a non-negative integer n that represents the cooldown period between two same tasks (the same
     * letter in the array), that is that there must be at least n units of time between any two same tasks.
     * <p>
     * Return the least number of units of times that the CPU will take to finish all the given tasks.
     * <p>
     * Constraints:
     * <p>
     * 1 <= task.length <= 10^4
     * tasks[i] is upper-case English letter.
     * The integer n is in the range [0, 100].
     * <p>
     * <p>
     * We need to iterate over the task array and compute the number for each individual task and store them into an array
     * We then sort them by the frequency, remove tasks in descending order until the cooling time is over
     * When cooling time is over, we resort the array and keep removing tasks in descending order until all the tasks has been assigned
     * <p>
     * Time: O(N) since we need firstly traverse the tasks array. For each sorting process, the runtime will be O(26*log(26)) which is
     * a constant time
     * Space: O(1) since we only need a constant size of array (26)
     */
    public int leastIntervalSort(char[] tasks, int n) {
        int[] taskCount = new int[26];
        for (char task : tasks) taskCount[task - 'A'] += 1;
        Arrays.sort(taskCount);
        int intervals = 0;
        while (taskCount[25] > 0) {
            int i = 0; //record cooling time
            while (i <= n) {
                if (taskCount[25] == 0) break;
                if (i < 26 && taskCount[25 - i] > 0) taskCount[25 - i] -= 1;
                i += 1;
                intervals += 1;
            }
            Arrays.sort(taskCount);
        }
        return intervals;
    }

    /**
     * We can actually compute the number of idle slots for given tasks, since the least interval is simply
     * the number of idle slots + the number of tasks. Note that if cooling time n is 0, the result is just the number of tasks.
     * The maximum number of idle slots equals to the cooling time n times the largest number of task frequency - 1
     * we then start from the second most frequent task (the number could be the same with the most frequent task), to make tasks
     * occupy idle slots to reduce the number of idle slots. We should be careful that when the number of task equals to the
     * largest number, it will only occupy the largest number - 1 idle slots.
     * <p>
     * Time: O(N) we still iterate ove the task array once, the initial sorting will cost O(26*log(26), which is a constant runtime
     * Space: O(1) since we require a fixed size of space
     */
    public int leastIntervalGreedy(char[] tasks, int n) {
        int[] taskCount = new int[26];
        for (char task : tasks) taskCount[task - 'A'] += 1; //record the number of each task and store them in an array
        Arrays.sort(taskCount); //initial sorting, the largest number will at the rightmost point
        int maxFrequency = taskCount[25];
        int idleSlots = n * (maxFrequency - 1);
        for (int i = 24; i >= 0 && taskCount[i] > 0; i--) {
            idleSlots -= Math.min(taskCount[i], maxFrequency - 1);
        }
        //Note that when cooling time n is 0, we will obtain negative number of idle slots
        //In this case, the least interval is simply the number of tasks
        return idleSlots > 0 ? idleSlots + tasks.length : tasks.length;
    }

    @Test
    public void leastIntervalTest() {
        /**
         * Example 1:
         * Input: tasks = ["A","A","A","B","B","B"], n = 2
         * Output: 8
         * Explanation:
         * A -> B -> idle -> A -> B -> idle -> A -> B
         * There is at least 2 units of time between any two same tasks.
         */
        assertEquals(8, leastIntervalGreedy(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2));
        assertEquals(8, leastIntervalSort(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2));
        /**
         * Example 2:
         * Input: tasks = ["A","A","A","B","B","B"], n = 0
         * Output: 6
         * Explanation: On this case any permutation of size 6 would work since n = 0.
         * ["A","A","A","B","B","B"]
         * ["A","B","A","B","A","B"]
         * ["B","B","B","A","A","A"]
         * ...
         * And so on.
         */
        assertEquals(6, leastIntervalGreedy(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 0));
        assertEquals(6, leastIntervalSort(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 0));
        /**
         * Example 3:
         * Input: tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
         * Output: 16
         * Explanation:
         * One possible solution is
         * A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> idle -> idle -> A -> idle -> idle -> A
         */
        assertEquals(16, leastIntervalGreedy(new char[]{'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G'}, 2));
        assertEquals(16, leastIntervalSort(new char[]{'A', 'A', 'A', 'A', 'A', 'A', 'B', 'C', 'D', 'E', 'F', 'G'}, 2));
    }
}
