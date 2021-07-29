import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LetterCasePermutation {

    /**
     * Given a string s, we can transform every letter individually to be lowercase or uppercase to create another string.
     * <p>
     * Return a list of all possible strings we could create. You can return the output in any order.
     * <p>
     * Constraints:
     * <p>
     * s will be a string with length between 1 and 12.
     * s will consist only of letters or digits.
     * <p>
     * Approach 1: BFS - iteration
     * For BFS - we traverse each character in the string. If it's a digit, simply appending it to all existing substrings.
     * If it's a letter - we need to first append the letter AS IS to all existing substrings. Meanwhile, for subsequent
     * letters we need to double the size of search space by adding new strings with appending the opposite case at the end
     * of each existing substrings. For example, "1ab2"
     * [""] -> ["1"] -> ["1a", "1A"] -> ["1ab", "1Ab", "1aB", "1AB"] -> ["1ab2", "1Ab2", "1aB2", "1AB2"]
     * <p>
     * Time: O(N * 2^N) in the worst case, the string is of all letters, we have two options (lower or upper case) at each position,
     * hence it gives 2^N total permutations. For each permutation, we need to copy the previous substrings which may have up
     * to length of N.
     * Space: O(2^N)
     */
    public List<String> letterCasePermutationBFSIterative(String s) {
        // use string builder to store substring permutations for better performance
        List<StringBuilder> permutations = new ArrayList<>();
        // add an empty substring as a starting point
        permutations.add(new StringBuilder());
        for (char c : s.toCharArray()) {
            int size = permutations.size();
            // first need to know the size of current level (breadth)
            for (int i = 0; i < size; i++) {
                // if it's a letter
                if (Character.isLetter(c)) {
                    // appending the opposite case to the end of all existing substrings
                    // and put them into new substrings (doubling the search space)
                    StringBuilder sb = new StringBuilder(permutations.get(i));
                    if (Character.isLowerCase(c)) {
                        sb.append(Character.toUpperCase(c));
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                    permutations.add(sb);
                }
                // appending the original letter or a digit to existing substrings
                permutations.get(i).append(c);
            }
        }

        // final result
        List<String> res = new ArrayList<>();
        for (StringBuilder sb : permutations) {
            res.add(sb.toString());
        }
        return res;
    }

    /**
     * Approach 2: DFS - recursion
     * The above process can be converted to recursion - except we only add to the result list after constructing a valid string.
     * It becomes to a DFS instead of BFS.
     * <p>
     * Time: O(N * 2^N)
     * Space: O(2^N)
     */
    public List<String> letterCasePermutationBFSRecursive(String s) {
        List<String> res = new ArrayList<>();
        backtrack(res, s, new StringBuilder(), 0);
        return res;
    }

    private void backtrack(List<String> res, String s, StringBuilder substring, int index) {
        if (index == s.length()) {
            res.add(substring.toString());
            return;
        }
        // get current character
        char c = s.charAt(index);
        // if it's a letter
        if (Character.isLetter(c)) {
            // we can first change the case and keep searching
            char newChar = Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c);
            substring.append(newChar);
            backtrack(res, s, substring, index + 1);
            substring.deleteCharAt(substring.length() - 1);
        }
        // otherwise, the character is a digit,
        // or we're done with searching after changing the case - use the original character to resume
        substring.append(c);
        backtrack(res, s, substring, index + 1);
        substring.deleteCharAt(substring.length() - 1);
    }

    @Test
    public void letterCasePermutationTest() {
        /**
         * Example 1:
         * Input: s = "a1b2"
         * Output: ["a1b2","a1B2","A1b2","A1B2"]
         */
        Set<String> expected1 = Set.of("a1b2", "a1B2", "A1b2", "A1B2");
        List<String> actualBFSIterative1 = letterCasePermutationBFSIterative("a1b2");
        assertEquals(expected1.size(), actualBFSIterative1.size());
        for (String s : actualBFSIterative1) {
            assertTrue(expected1.contains(s));
        }
        List<String> actualBFSRecursive1 = letterCasePermutationBFSRecursive("a1b2");
        assertEquals(expected1.size(), actualBFSRecursive1.size());
        for (String s : actualBFSRecursive1) {
            assertTrue(expected1.contains(s));
        }
        /**
         * Example 2:
         * Input: s = "3z4"
         * Output: ["3z4","3Z4"]
         */
        Set<String> expected2 = Set.of("3z4", "3Z4");
        List<String> actualBFSIterative2 = letterCasePermutationBFSIterative("3z4");
        assertEquals(expected2.size(), actualBFSIterative2.size());
        for (String s : actualBFSIterative2) {
            assertTrue(expected2.contains(s));
        }
        List<String> actualBFSRecursive2 = letterCasePermutationBFSRecursive("a1b2");
        assertEquals(expected1.size(), actualBFSRecursive2.size());
        for (String s : actualBFSRecursive2) {
            assertTrue(expected1.contains(s));
        }
        /**
         * Example 3:
         * Input: s = "12345"
         * Output: ["12345"]
         */
        Set<String> expected3 = Set.of("12345");
        List<String> actualBFSIterative3 = letterCasePermutationBFSIterative("12345");
        assertEquals(expected3.size(), actualBFSIterative3.size());
        for (String s : actualBFSIterative3) {
            assertTrue(expected3.contains(s));
        }
        List<String> actualBFSRecursive3 = letterCasePermutationBFSRecursive("a1b2");
        assertEquals(expected1.size(), actualBFSRecursive3.size());
        for (String s : actualBFSRecursive3) {
            assertTrue(expected1.contains(s));
        }
        /**
         * Example 4:
         * Input: s = "0"
         * Output: ["0"]
         */
        Set<String> expected4 = Set.of("0");
        List<String> actualBFSIterative4 = letterCasePermutationBFSIterative("0");
        assertEquals(expected4.size(), actualBFSIterative4.size());
        for (String s : actualBFSIterative4) {
            assertTrue(expected4.contains(s));
        }
        List<String> actualBFSRecursive4 = letterCasePermutationBFSRecursive("a1b2");
        assertEquals(expected1.size(), actualBFSRecursive4.size());
        for (String s : actualBFSRecursive4) {
            assertTrue(expected1.contains(s));
        }
    }
}
