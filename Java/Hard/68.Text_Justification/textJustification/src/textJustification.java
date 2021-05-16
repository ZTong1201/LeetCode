import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class textJustification {

    /**
     * Given an array of words and a width maxWidth, format the text such that each line has exactly maxWidth characters and is fully
     * (left and right) justified.
     *
     * You should pack your words in a greedy approach; that is, pack as many words as you can in each line. Pad extra spaces ' ' when
     * necessary so that each line has exactly maxWidth characters.
     *
     * Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line do not divide evenly
     * between words, the empty slots on the left will be assigned more spaces than the slots on the right.
     *
     * For the last line of text, it should be left justified and no extra space is inserted between words.
     *
     * Note:
     * A word is defined as a character sequence consisting of non-space characters only.
     * Each word's length is guaranteed to be greater than 0 and not exceed maxWidth.
     * The input array words contains at least one word.
     */
    public List<String> fullJustify(String[] words, int maxWidth) {

    }

    @Test
    public void fullJustifyTest() {
        /**
         * Example 1:
         * Input:
         * words = ["This", "is", "an", "example", "of", "text", "justification."]
         * maxWidth = 16
         * Output:
         * [
         *    "This    is    an",
         *    "example  of text",
         *    "justification.  "
         * ]
         */
        String[] words1 = new String[]{"This", "is", "an", "example", "of", "text", "justification."};
        List<String> actual1 = fullJustify(words1, 16);
        List<String> expected1 = new ArrayList<>(Arrays.asList("This    is    an", "example  of text", "justification.  "));
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input:
         * words = ["What","must","be","acknowledgment","shall","be"]
         * maxWidth = 16
         * Output:
         * [
         *   "What   must   be",
         *   "acknowledgment  ",
         *   "shall be        "
         * ]
         * Explanation: Note that the last line is "shall be    " instead of "shall     be",
         *              because the last line must be left-justified instead of fully-justified.
         *              Note that the second line is also left-justified becase it contains only one word.
         */
        String[] words2 = new String[]{"What", "must", "be", "acknowledgment", "shall", "be"};
        List<String> actual2 = fullJustify(words2, 16);
        List<String> expected2 = new ArrayList<>(Arrays.asList("What   must   be", "acknowledgment  ", "shall be        "));
        assertEquals(expected2.size(), actual2.size());
        for(int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
        /**
         * Example 3:
         * Input:
         * words = ["Science","is","what","we","understand","well","enough","to","explain",
         *          "to","a","computer.","Art","is","everything","else","we","do"]
         * maxWidth = 20
         * Output:
         * [
         *   "Science  is  what we",
         *   "understand      well",
         *   "enough to explain to",
         *   "a  computer.  Art is",
         *   "everything  else  we",
         *   "do                  "
         * ]
         */
        String[] words3 = new String[]{"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain",
                "to", "a", "computer.", "Art", "is", "everything", "else", "we", "do"};
        List<String> actual3 = fullJustify(words3, 20);
        List<String> expected3 = new ArrayList<>(Arrays.asList("Science  is  what we", "understand      well", "enough to explain to",
                "a  computer.  Art is", "everything  else  we", "do                  "));
        assertEquals(expected3.size(), actual3.size());
        for(int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i), actual3.get(i));
        }
    }
}
