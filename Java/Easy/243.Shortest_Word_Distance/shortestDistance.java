import org.junit.Test;
import static org.junit.Assert.*;

public class shortestDistance {

    /**
     * Given a list of words and two words word1 and word2, return the shortest distance between these two words in the list.
     *
     * Note:
     * You may assume that word1 does not equal to word2, and word1 and word2 are both in the list.
     *
     * Approach 1: One-pass
     * 可以遍历一遍数组，并记录两个单词出现的index（初始化都为-1，表示还未在数组中找到该单词），在遍历过程中，遇到新的位置，就更新当前index，若两个单词
     * 都被找到，可以判断当前的距离是否为最小距离。可以将最小距离初始化为words.length，因为两单词距离最大为words.length - 1
     *
     * Time: O(n) simple one-pass
     * Space: O(1)
     */
    public int shortestDistance(String[] words, String word1, String word2) {
        int min = words.length;
        int index1 = -1, index2 = -1;
        for(int i = 0; i < words.length; i++) {
            if(words[i].equals(word1)) {
                index1 = i;
            } else if(words[i].equals(word2)) {
                index2 = i;
            }

            //若两单词都被找到，则可以更新最短距离
            if(index1 != -1 && index2 != -1) {
                min = Math.min(min, Math.abs(index2 - index1));
            }
        }
        return min;
    }

    @Test
    public void shortestDistanceTest() {
        /**
         * Assume that words = ["practice", "makes", "perfect", "coding", "makes"].
         * Example 1:
         * Input: word1 = “coding”, word2 = “practice”
         * Output: 3
         */
        String[] words = new String[]{"practice", "makes", "perfect", "coding", "makes"};
        assertEquals(3, shortestDistance(words, "coding", "practice"));
        /**
         * Example 2:
         * Input: word1 = "makes", word2 = "coding"
         * Output: 1
         */
        assertEquals(1, shortestDistance(words, "makes", "coding"));
    }
}
