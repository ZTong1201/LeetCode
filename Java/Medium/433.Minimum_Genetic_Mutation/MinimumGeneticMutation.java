import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MinimumGeneticMutation {

    /**
     * A gene string can be represented by an 8-character long string, with choices from 'A', 'C', 'G', and 'T'.
     * <p>
     * Suppose we need to investigate a mutation from a gene string start to a gene string end where one mutation is defined
     * as one single character changed in the gene string.
     * <p>
     * For example, "AACCGGTT" --> "AACCGGTA" is one mutation.
     * There is also a gene bank bank that records all the valid gene mutations. A gene must be in bank to make it a valid
     * gene string.
     * <p>
     * Given the two gene strings start and end and the gene bank bank, return the minimum number of mutations needed to
     * mutate from start to end. If there is no such a mutation, return -1.
     * <p>
     * Note that the starting point is assumed to be valid, so it might not be included in the bank.
     * <p>
     * Constraints:
     * <p>
     * start.length == 8
     * end.length == 8
     * 0 <= bank.length <= 10
     * bank[i].length == 8
     * start, end, and bank[i] consist of only the characters ['A', 'C', 'G', 'T'].
     * <p>
     * Approach: BFS
     * The problem is basically finding the shortest path between two nodes in the graph, if there is no valid path, we return
     * -1 in the end. Therefore, we can start from the start gene string and each time we make one mutation at a position and
     * put those candidates into the queue. Notice that the next gene strings need to be placed in the gene bank, hence it will
     * significantly reduce the search space in practice. If at any time, we reach the end sequence, we can return the shortest
     * path so far since it's guaranteed to be the minimum. Otherwise, -1 will be returned eventually.
     * <p>
     * Time: O(4^8) at each position, we have 4 choices to be searched, hence in total, the search space will be 4^8
     * Space: O(4^8)
     */
    public int minMutation(String start, String end, String[] bank) {
        Set<String> validGenes = new HashSet<>(Arrays.asList((bank)));
        // based on the problem statement, we cannot reach the end mutation, if it doesn't exist in the bank at all
        if (!validGenes.contains(end)) return -1;

        // use queue to run BFS
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        // also need a set to keep track of visited nodes to avoid duplicate visit
        Set<String> visited = new HashSet<>();

        int minSteps = 0;
        char[] choices = new char[]{'A', 'C', 'G', 'T'};

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String currGeneString = queue.poll();

                if (!visited.contains(currGeneString)) {
                    visited.add(currGeneString);

                    // return the minimum steps if the end mutation is reached
                    if (currGeneString.equals(end)) return minSteps;

                    // convert string into a char array
                    char[] strArray = currGeneString.toCharArray();
                    for (int j = 0; j < strArray.length; j++) {
                        char oldChar = strArray[j];

                        for (char newChar : choices) {
                            // change the current gene to get a mutation
                            strArray[j] = newChar;
                            String newGeneString = new String(strArray);

                            // if the new mutation exists in the gene bank, and it's not visited
                            // we can add it in the queue for further search
                            if (validGenes.contains(newGeneString) && !visited.contains(newGeneString)) {
                                queue.add(newGeneString);
                            }
                        }

                        // change the character back to the original to not affect the subsequent mutation
                        strArray[j] = oldChar;
                    }
                }
            }
            minSteps++;
        }
        return -1;
    }

    @Test
    public void minMutationTest() {
        /**
         * Example 1:
         * Input: start = "AACCGGTT", end = "AACCGGTA", bank = ["AACCGGTA"]
         * Output: 1
         */
        assertEquals(1, minMutation("AACCGGTT", "AACCGGTA", new String[]{"AACCGGTA"}));
        /**
         * Example 2:
         * Input: start = "AACCGGTT", end = "AAACGGTA", bank = ["AACCGGTA","AACCGCTA","AAACGGTA"]
         * Output: 2
         */
        assertEquals(2, minMutation("AACCGGTT", "AAACGGTA", new String[]{"AACCGGTA", "AACCGCTA", "AAACGGTA"}));
        /**
         * Example 3:
         * Input: start = "AAAAACCC", end = "AACCCCCC", bank = ["AAAACCCC","AAACCCCC","AACCCCCC"]
         * Output: 3
         */
        assertEquals(3, minMutation("AAAAACCC", "AACCCCCC", new String[]{"AAAACCCC", "AAACCCCC", "AACCCCCC"}));
    }
}
