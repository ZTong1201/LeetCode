import org.junit.Test;
import static org.junit.Assert.*;

public class findCelebrityTest {

    @Test
    public void findCelebrityTest() {
        /**
         * Example 1:
         *
         *       1 <--- 0
         *       ^    /
         *       |   /
         *       |  /
         *       2
         * Input: graph = [
         *   [1,1,0],
         *   [0,1,0],
         *   [1,1,1]
         * ]
         * Output: 1
         * Explanation: There are three persons labeled with 0, 1 and 2. graph[i][j] = 1 means person i knows person j, otherwise
         * graph[i][j] = 0 means person i does not know person j. The celebrity is the person labeled as 1 because both 0 and 2 know
         * him but 1 does not know anybody.
         */
        int[][] graph1 = new int[][]{{1, 1, 0}, {0, 1, 0}, {1, 1, 1}};
        findCelebrity solution1 = new findCelebrity();
        solution1.relations = graph1;
        assertEquals(1, solution1.findCelebrity(3));
        /**
         * Example 2:
         *   0 ---> 2
         *   ^    /
         *   |   /
         *   |  /
         *   1
         * Input: graph = [
         *   [1,0,1],
         *   [1,1,0],
         *   [0,1,1]
         * ]
         * Output: -1
         * Explanation: There is no celebrity.
         */
        int[][] graph2 = new int[][]{{1, 0, 1}, {1, 1, 0}, {0, 1, 1}};
        findCelebrity solution2 = new findCelebrity();
        solution2.relations = graph2;
        assertEquals(-1, solution2.findCelebrity(3));
    }

}
