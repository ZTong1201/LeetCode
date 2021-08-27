import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SentenceScreenFitting {

    /**
     * Given a rows x cols screen and a sentence represented as a list of strings, return the number of times the given
     * sentence can be fitted on the screen.
     * <p>
     * The order of words in the sentence must remain unchanged, and a word cannot be split into two lines. A single space
     * must separate two consecutive words in a line.
     * <p>
     * Constraints:
     * <p>
     * 1 <= sentence.length <= 100
     * 1 <= sentence[i].length <= 10
     * sentence[i] consists of lowercase English letters.
     * 1 <= rows, cols <= 2 * 10^4
     * <p>
     * Approach: DP
     * Basically, we can solve this problem in a brute force way - iterate over each row, check whether it's possible to
     * append a string starting from index i. If yes, then move to next string to see whether we're able to append more.
     * Otherwise, jump to a new row and restart. This works fine when rows and cols are not that big. However, we may run
     * into duplicate scenarios when rows and cols are big while the number of words in sentence array is relatively small.
     * We can actually list all possible combinations with sentence[i] as the start word of the row (hence we have n possible
     * starting words, where n is the length of sentence array) to keep track of
     * 1. how many sentences can be completed in that row if starting with sentence[i]. Here completion means we reach the
     * sentence[n - 1]
     * 2. which index will be the starting word of next row
     * Once the pre-computation is done, we can iterate over each row to add the number sentences can be completed in that
     * row. The starting word of the first row would apparently be sentence[0], then we use the pre-computed information
     * to get what will be the index of starting word of next row, so on and so forth.
     * <p>
     * Time: O(R + N * C) for the pre-computation step, the loop stops until it exceeds cols for each starting word.
     * And finally, we iterate over each row to get pre-computed info
     * Space: O(N)
     */
    public int wordsTyping(String[] sentence, int rows, int cols) {
        int length = sentence.length;
        int[] wordLength = new int[length];
        for (int i = 0; i < length; i++) {
            wordLength[i] = sentence[i].length();
        }

        // the starting word index of next row if the starting word index is i
        int[] nextWordIndex = new int[length];
        // the number of sentences complete if the starting word index is i
        int[] sentenceComplete = new int[length];

        // pre-compute for all start index from 0 to n - 1
        for (int startIndex = 0; startIndex < length; startIndex++) {
            int currIndex = startIndex, totalSentences = 0, currCol = 0;

            // if we can append current word to this row without exceeding cols
            // append it and move to next word
            while (currCol + wordLength[currIndex] <= cols) {
                // plus one because we need a space
                currCol += wordLength[currIndex] + 1;
                // move to next index
                currIndex++;

                // if we already looped over the sentence array - restart;
                if (currIndex == length) {
                    currIndex = 0;
                    // also - we just completed a sentence
                    totalSentences++;
                }
            }

            // record the pre-computed information
            nextWordIndex[startIndex] = currIndex;
            sentenceComplete[startIndex] = totalSentences;
        }

        // iterate over all rows and calculate the total count
        // the start index of the first row would be 0
        int count = 0, startIndex = 0;
        for (int i = 0; i < rows; i++) {
            // add the number of sentences completed with the row begins with startIndex
            count += sentenceComplete[startIndex];
            // the next row will start with nextWordIndex[startIndex], jump to it
            startIndex = nextWordIndex[startIndex];
        }
        return count;
    }

    @Test
    public void wordsTypingTest() {
        /**
         * Example 1:
         * Input: sentence = ["hello","world"], rows = 2, cols = 8
         * Output: 1
         * Explanation:
         * hello---
         * world---
         * The character '-' signifies an empty space on the screen.
         */
        assertEquals(1, wordsTyping(new String[]{"hello", "world"}, 2, 8));
        /**
         * Example 2:
         * Input: sentence = ["a", "bcd", "e"], rows = 3, cols = 6
         * Output: 2
         * Explanation:
         * a-bcd-
         * e-a---
         * bcd-e-
         * The character '-' signifies an empty space on the screen.
         */
        assertEquals(2, wordsTyping(new String[]{"a", "bcd", "e"}, 3, 6));
        /**
         * Example 3:
         * Input: sentence = ["i","had","apple","pie"], rows = 4, cols = 5
         * Output: 1
         * Explanation:
         * i-had
         * apple
         * pie-i
         * had--
         * The character '-' signifies an empty space on the screen.
         */
        assertEquals(1, wordsTyping(new String[]{"i", "had", "apple", "pie"}, 4, 5));
    }
}
