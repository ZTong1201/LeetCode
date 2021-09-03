import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class textJustification {

    /**
     * Given an array of words and a width maxWidth, format the text such that each line has exactly maxWidth characters and is fully
     * (left and right) justified.
     * <p>
     * You should pack your words in a greedy approach; that is, pack as many words as you can in each line. Pad extra spaces ' ' when
     * necessary so that each line has exactly maxWidth characters.
     * <p>
     * Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line do not divide evenly
     * between words, the empty slots on the left will be assigned more spaces than the slots on the right.
     * <p>
     * For the last line of text, it should be left justified and no extra space is inserted between words.
     * <p>
     * Note:
     * A word is defined as a character sequence consisting of non-space characters only.
     * Each word's length is guaranteed to be greater than 0 and not exceed maxWidth.
     * The input array words contains at least one word.
     * <p>
     * Constraints:
     * <p>
     * 1 <= words.length <= 300
     * 1 <= words[i].length <= 20
     * words[i] consists of only English letters and symbols.
     * 1 <= maxWidth <= 100
     * words[i].length <= maxWidth
     * <p>
     * Approach: String manipulation
     * We have no fancy ways to resolve this problem, but have to deal with as many corner cases as possible. There are
     * the following 3 corner cases need to be handled
     * 1. The whitespaces cannot be evenly distributed - the text needs to be left justified, i.e. we may have more spaces
     * in the first few words and fewer spaces later on.
     * 2. There is only one word in the single line - need to add extra spaces to span the width
     * 3. It's the last line - everything needs to be left justified.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> res = new ArrayList<>();
        // keep track of current width line
        int index = 0, currWidth = 0;

        while (index < words.length) {
            // the words need to be justified in the current line
            List<String> wordsInLine = new ArrayList<>();
            // greedily include as many as words as possible
            while (index < words.length && currWidth + words[index].length() <= maxWidth) {
                // plus one because there is at least one space between two words
                currWidth += words[index].length() + 1;
                wordsInLine.add(words[index]);
                index++;
            }

            // always minus one since there is no space after the last word of the line
            currWidth--;
            // corner case 1: fully left justify the text if it's the last line
            if (index == words.length) {
                // there is only one space between two words in the last line
                String lastLine = String.join(" ", wordsInLine);
                // plus extra spaces to span the width
                lastLine += " ".repeat(maxWidth - currWidth);
                res.add(lastLine);
            } else {
                StringBuilder currLine = new StringBuilder();
                // get the width difference to further justify
                int widthDiff = maxWidth - currWidth;

                // corner case 2: if there is only one word in the line
                if (wordsInLine.size() == 1) {
                    // add that word and span the width with spaces
                    currLine.append(wordsInLine.get(0)).append(" ".repeat(widthDiff));
                } else {
                    // otherwise, the line needs to be left justified
                    currLine = leftJustify(wordsInLine, widthDiff);
                }
                res.add(currLine.toString());
            }
            // always reset the current width before starting the new line
            currWidth = 0;
        }
        return res;
    }

    private StringBuilder leftJustify(List<String> wordsInLine, int widthDiff) {
        StringBuilder currLine = new StringBuilder();
        // get the number of intervals to be justified
        int numOfIntervals = wordsInLine.size() - 1;
        int wordIndex = 0;
        // try to distribute spaces evenly first
        int extraSpace = widthDiff / numOfIntervals;
        // and there are some leftovers need to be distributed by left justification
        int leftJustificationNeeded = widthDiff % numOfIntervals;
        // complete left justification first
        while (leftJustificationNeeded != 0) {
            // why extra space + 2? because there is always one whitespace between two words
            // and the first few words need one more extra space to span the width
            currLine.append(wordsInLine.get(wordIndex)).append(" ".repeat(extraSpace + 2));
            leftJustificationNeeded--;
            wordIndex++;
        }

        // after left justification - we can append the rest of the word until the end
        while (wordIndex < wordsInLine.size()) {
            // corner case 3: if it's the end word of the current line
            // not appending any spaces afterwards
            if (wordIndex == wordsInLine.size() - 1) {
                currLine.append(wordsInLine.get(wordIndex));
            } else {
                // otherwise, append extra space + 1 whitespaces after appending the word
                // why? because we don't need one more extra space for the left justification
                // only need the spaces which are evenly distributed + the one between any two words
                currLine.append(wordsInLine.get(wordIndex)).append(" ".repeat(extraSpace + 1));
            }
            wordIndex++;
        }
        return currLine;
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
        for (int i = 0; i < expected1.size(); i++) {
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
        for (int i = 0; i < expected2.size(); i++) {
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
        for (int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i), actual3.get(i));
        }
    }
}
