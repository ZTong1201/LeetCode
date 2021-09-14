import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlphabetBoardPath {

    /**
     * On an alphabet board, we start at position (0, 0), corresponding to character board[0][0].
     * <p>
     * Here, board = ["abcde", "fghij", "klmno", "pqrst", "uvwxy", "z"], as shown in the diagram below.
     * <p>
     * We may make the following moves:
     * <p>
     * 'U' moves our position up one row, if the position exists on the board;
     * 'D' moves our position down one row, if the position exists on the board;
     * 'L' moves our position left one column, if the position exists on the board;
     * 'R' moves our position right one column, if the position exists on the board;
     * '!' adds the character board[r][c] at our current position (r, c) to the answer.
     * (Here, the only positions that exist on the board are positions with letters on them.)
     * <p>
     * Return a sequence of moves that makes our answer equal to target in the minimum number of moves.
     * You may return any path that does so.
     * <p>
     * Constraints:
     * <p>
     * 1 <= target.length <= 100
     * target consists only of English lowercase letters.
     * <p>
     * Approach: Index Manipulation
     * The idea is to basically translate a 2-D index (i, j) into a 1-D index by using the formula i * width + j. In this
     * particular question, the width is 5. However, this should work for any width. If all 26 letters can fit perfectly
     * in the grid - no corner case at all. Yet, for this question (or for any question the final row has a different length)
     * we need to handle an extra corner case. That is
     * 1. If from other row to the final row, we also need L -> D
     * 2. Or from the final row to other rows, always need to U -> R
     * to make sure we're always inside the grid. Therefore, we just need to add steps in LDUR order or URLD order.
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public String alphabetBoardPath(String target) {
        // in this question, the width is 5
        int width = 5;
        StringBuilder finalPath = new StringBuilder();
        // the starting point is (0, 0) which is index 0 in 1-D
        int prevId = 0;

        for (int i = 0; i < target.length(); i++) {
            char curr = target.charAt(i);
            // get 1-D id of current char
            int currId = curr - 'a';
            // convert it to 2-D coordinate
            int currRow = currId / width, currCol = currId % width;
            // convert previous ID to coordinate as well
            int prevRow = prevId / width, prevCol = prevId % width;

            // add movements in the LDUR order
            // note that we might not necessarily need to move on each direction if the difference is negative
            finalPath.append("L".repeat(Math.max(0, prevCol - currCol)));
            finalPath.append("D".repeat(Math.max(0, currRow - prevRow)));
            finalPath.append("U".repeat(Math.max(0, prevRow - currRow)));
            finalPath.append("R".repeat(Math.max(0, currCol - prevCol)));
            // need to append "!" to add current character
            finalPath.append("!");

            // update previous ID for next round
            prevId = currId;
        }
        return finalPath.toString();
    }

    @Test
    public void alphabetBoardPathTest() {
        /**
         * Example 1:
         * Input: target = "leet"
         * Output: "DDR!UURRR!!DDD!"
         */
        assertEquals("DDR!UURRR!!DDD!", alphabetBoardPath("leet"));
        /**
         * Example 2:
         * Input: target = "code"
         * Output: "RR!DDRR!LUU!R!" (or "RR!DDRR!UUL!R!")
         */
        assertEquals("RR!DDRR!LUU!R!", alphabetBoardPath("code"));
    }
}
