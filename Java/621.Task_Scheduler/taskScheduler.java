import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class taskScheduler {

    /**
     *
     * Given a char array representing tasks CPU need to do.
     * It contains capital letters A to Z where different letters represent different tasks.
     * Tasks could be done without original order. Each task could be done in one interval.
     * For each interval, CPU could finish one task or just be idle.
     *
     * However, there is a non-negative cooling interval n that means between two same tasks,
     * there must be at least n intervals that CPU are doing different tasks or just be idle.
     *
     * You need to return the least number of intervals the CPU will take to finish all the given tasks.
     *
     *
     * We need to iterate over the task array and compute the number for each individual task and store them into an array
     * We then sort them by the frequency, remove tasks in descending order until the cooling time is over
     * When cooling time is over, we resort the array and keep removing tasks in descending order until all the tasks has been assigned
     *
     * Time: O(N) since we need firstly traverse the tasks array. For each sorting process, the runtime will be O(26*log(26)) which is
     *      a constant time
     * Space: O(1) since we only need a constant size of array (26)
     */
    public int leastInterval1(char[] tasks, int n) {
        int[] taskCount = new int[26];
        for(char task : tasks) taskCount[task - 'A'] += 1;
        Arrays.sort(taskCount);
        int intervals = 0;
        while(taskCount[25] > 0) {
            int i = 0; //record cooling time
            while(i <= n) {
                if(taskCount[25] == 0) break;
                if(i < 26 && taskCount[25 - i] > 0) taskCount[25 - i] -= 1;
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
     * largest number, it will only occupy largest number - 1 idle slots.
     *
     * Time: O(N) we still iterate ove the task array once, the initial sorting will cost O(26*log(26), which is a constant runtime
     * Space: O(1) since we require a fixed size of space
     */
    public int leastInterval2(char[] tasks, int n) {
        int[] taskCount = new int[26];
        for(char task : tasks) taskCount[task - 'A'] += 1; //record the number of each task and store them in an array
        Arrays.sort(taskCount); //initial sorting, the largest number will at the rightmost point
        int mostFrequent = taskCount[25];
        int idleSlots = n * (mostFrequent - 1);
        for(int i = 24; i >= 0 && taskCount[i] > 0; i--) {
            idleSlots -= Math.min(taskCount[i], mostFrequent - 1);
        }
        //Note that when cooling time n is 0, we will obtain negative number of idle slots
        //In this case, the least interval is simply the number of tasks
        return idleSlots > 0 ? idleSlots + tasks.length : tasks.length;
    }

    @Test
    public void leastIntervalTest() {
        char[] tasks1 = new char[]{'A', 'A', 'A', 'B', 'B', 'B'};
        assertEquals(8, leastInterval1(tasks1, 2));
        assertEquals(6, leastInterval1(tasks1, 0));
        assertEquals(104, leastInterval1(tasks1, 50));
        assertEquals(8, leastInterval2(tasks1, 2));
        assertEquals(6, leastInterval2(tasks1, 0));
        assertEquals(104, leastInterval2(tasks1, 50));
        char[] tasks2 = new char[]{'A', 'A', 'A', 'A', 'B', 'B', 'B'};
        assertEquals(10, leastInterval1(tasks2, 2));
        assertEquals(10, leastInterval2(tasks2, 2));
    }
}
