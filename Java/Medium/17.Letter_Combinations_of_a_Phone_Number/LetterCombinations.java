import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LetterCombinations {

    /**
     * Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the
     * number could represent. Return the answer in any order.
     * <p>
     * A mapping of digit to letters (just like on the telephone buttons) is given below. Note that 1 does not
     * map to any letters.
     * 2 -> "abc"
     * 3 -> "def"
     * 4 -> "ghi"
     * 5 -> "jkl"
     * 6 -> "mno"
     * 7 -> "pqrs"
     * 8 -> "tuv"
     * 9 -> "wxyz"
     * <p>
     * Constraints:
     * <p>
     * 0 <= digits.length <= 4
     * digits[i] is a digit in the range ['2', '9'].
     * <p>
     * Approach: backtrack
     * Use DFS to do a comprehensive search and once one of the combinations has been found, add it to the result list
     * and backtrack to the previous state.
     * <p>
     * Time: O(N * 4^N) assume N is the length of digits string. For each character in the digits, it needs to search
     * all of the potential combinations. Since the maximum number of candidate letters could be 4, the total number
     * of combinations is (less than) 4^N for each letter, which gives N * 4^N in totality.
     * Space: O(N) call stack
     */

    private Map<Character, String> digitToChars;
    private List<String> res;

    public List<String> letterCombinations(String digits) {
        res = new ArrayList<>();
        if (digits.length() == 0) return res;
        initMap();
        backtrack(digits, 0, new StringBuilder());
        return res;
    }

    private void backtrack(String digits, int start, StringBuilder builder) {
        // base case
        if (digits.length() == builder.length()) {
            // add the correct combination to the list
            res.add(builder.toString());
            // backtrack
            return;
        }
        // get candidate letters
        String letters = digitToChars.get(digits.charAt(start));
        // loop through the candidate letters for a comprehensive search
        for (char letter : letters.toCharArray()) {
            // add current letter to the combination
            builder.append(letter);
            // DFS - jump to the next letter in the digits string
            backtrack(digits, start + 1, builder);
            // after backtrack, remove the previous letter and start the next round
            builder.deleteCharAt(builder.length() - 1);
        }
    }

    private void initMap() {
        digitToChars = Map.of('2', "abc", '3', "def", '4', "ghi",
                '5', "jkl", '6', "mno", '7', "pqrs",
                '8', "tuv", '9', "wxyz");
    }

    @Test
    public void letterCombinationsTest() {
        /**
         * Example 1:
         * Input: digits = "23"
         * Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
         */
        List<String> actual1 = letterCombinations("23");
        Set<String> expected1 = Set.of("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf");
        assertEquals(expected1.size(), actual1.size());
        for (String combination : actual1) {
            assertTrue(expected1.contains(combination));
        }
        /**
         * Example 2:
         * Input: digits = ""
         * Output: []
         */
        List<String> actual2 = letterCombinations("");
        assertEquals(0, actual2.size());
    }
}
