import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AutocompleteSystemTest {

    @Test
    public void autocompleteSystemTest() {
        /**
         * Example:
         * Input
         * ["AutocompleteSystem", "input", "input", "input", "input"]
         * [[["i love you", "island", "iroman", "i love leetcode"], [5, 3, 2, 2]], ["i"], [" "], ["a"], ["#"]]
         * Output
         * [null, ["i love you", "island", "i love leetcode"], ["i love you", "i love leetcode"], [], []]
         *
         * Explanation
         * AutocompleteSystem obj = new AutocompleteSystem(["i love you", "island", "iroman", "i love leetcode"], [5, 3, 2, 2]);
         * obj.input("i"); // return ["i love you", "island", "i love leetcode"]. There are four sentences that have prefix
         * "i". Among them, "ironman" and "i love leetcode" have same hot degree. Since ' ' has ASCII code 32 and 'r' has
         * ASCII code 114, "i love leetcode" should be in front of "ironman". Also we only need to output top 3 hot sentences,
         * so "ironman" will be ignored.
         * obj.input(" "); // return ["i love you", "i love leetcode"]. There are only two sentences that have prefix "i ".
         * obj.input("a"); // return []. There are no sentences that have prefix "i a".
         * obj.input("#"); // return []. The user finished the input, the sentence "i a" should be saved as a historical
         * sentence in system. And the following input will be counted as a new search.
         */
        AutocompleteSystem autocompleteSystem = new AutocompleteSystem(
                new String[]{"i love you", "island", "iroman", "i love leetcode"},
                new int[]{5, 3, 2, 2});
        assertEquals(List.of("i love you", "island", "i love leetcode"), autocompleteSystem.input('i'));
        assertEquals(List.of("i love you", "i love leetcode"), autocompleteSystem.input(' '));
        assertTrue(autocompleteSystem.input('a').isEmpty());
        assertTrue(autocompleteSystem.input('#').isEmpty());
    }
}
