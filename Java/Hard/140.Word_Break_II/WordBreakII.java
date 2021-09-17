import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WordBreakII {

    /**
     * Given a string s and a dictionary of strings wordDict, add spaces in s to construct a sentence where each word is a
     * valid dictionary word. Return all such possible sentences in any order.
     * <p>
     * Note that the same word in the dictionary may be reused multiple times in the segmentation.
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 20
     * 1 <= wordDict.length <= 1000
     * 1 <= wordDict[i].length <= 10
     * s and wordDict[i] consist of only lowercase English letters.
     * All the strings of wordDict are unique.
     * <p>
     * Approach 1: BFS
     * Similar to LeetCode 139: https://leetcode.com/problems/word-break/
     * The naive approach would be using BFS to enumerate all the possible answers. Previously on 139, we only need to
     * return whether we can break the word. Now since we will return all the sentences, we also need to keep track of the
     * current set of words chosen. When the BFS hits the end of the input string and all the words can be found in the input
     * word dict. Then we can add that result into the final list, otherwise, that subset of words will be discarded eventually.
     * <p>
     * Time: O(n^2) we might have O(n^2) pairs to be searched
     * Space: O(n^2)
     */
    public List<String> wordBreakBFS(String s, List<String> wordDict) {
        List<String> res = new ArrayList<>();
        Set<String> wordSet = new HashSet<>(wordDict);
        Queue<Integer> index = new LinkedList<>();
        index.add(0);
        // need another queue to keep track of the current subset of words chosen
        Queue<List<String>> sentence = new LinkedList<>();
        sentence.add(new ArrayList<>());

        while (!index.isEmpty()) {
            int start = index.poll();
            List<String> curr = sentence.poll();

            for (int end = start + 1; end <= s.length(); end++) {
                // if the current substring is in to word dict
                if (wordSet.contains(s.substring(start, end))) {
                    // add the end index into the queue for further search
                    index.add(end);
                    // also make a deep copy of the current list
                    List<String> newList = new ArrayList<>(curr);
                    // and add the found word into that list
                    newList.add(s.substring(start, end));
                    if (end == s.length()) {
                        // if we can find all words in the word dict
                        // add the sentence into the result list
                        res.add(String.join(" ", newList));
                    }
                    // also, we need to add the current subset into the queue for further search
                    sentence.add(newList);
                }
            }
        }
        return res;
    }

    @Test
    public void wordBreakTest() {
        /**
         * Example 1:
         * Input: s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]
         * Output: ["cats and dog","cat sand dog"]
         */
        Set<String> expected1 = Set.of("cats and dog", "cat sand dog");
        Set<String> actual1 = new HashSet<>(wordBreakBFS("catsanddog", List.of("cat", "cats", "and", "sand", "dog")));
        assertEquals(expected1.size(), actual1.size());
        for (String word : actual1) {
            assertTrue(expected1.contains(word));
        }
        /**
         * Example 2:
         * Input: s = "pineapplepenapple", wordDict = ["apple","pen","applepen","pine","pineapple"]
         * Output: ["pine apple pen apple","pineapple pen apple","pine applepen apple"]
         * Explanation: Note that you are allowed to reuse a dictionary word.
         */
        Set<String> expected2 = Set.of("pine apple pen apple", "pineapple pen apple", "pine applepen apple");
        Set<String> actual2 = new HashSet<>(wordBreakBFS("pineapplepenapple", List.of("apple", "pen", "applepen", "pine", "pineapple")));
        assertEquals(expected2.size(), actual2.size());
        for (String word : actual2) {
            assertTrue(expected2.contains(word));
        }
        /**
         * Example 3:
         * Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
         * Output: []
         */
        List<String> actual3 = wordBreakBFS("catsandog", List.of("cats", "dog", "sand", "and", "cat"));
        assertTrue(actual3.isEmpty());
    }
}
