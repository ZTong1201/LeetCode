import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WordLadderII {

    /**
     * A transformation sequence from word beginWord to word endWord using a dictionary wordList is a sequence of words
     * beginWord -> s1 -> s2 -> ... -> sk such that:
     * <p>
     * Every adjacent pair of words differs by a single letter.
     * Every si for 1 <= i <= k is in wordList. Note that beginWord does not need to be in wordList.
     * sk == endWord
     * Given two words, beginWord and endWord, and a dictionary wordList, return all the shortest transformation sequences
     * from beginWord to endWord, or an empty list if no such sequence exists. Each sequence should be returned as a list
     * of the words [beginWord, s1, s2, ..., sk].
     * <p>
     * Constraints:
     * <p>
     * 1 <= beginWord.length <= 5
     * endWord.length == beginWord.length
     * 1 <= wordList.length <= 1000
     * wordList[i].length == beginWord.length
     * beginWord, endWord, and wordList[i] consist of lowercase English letters.
     * beginWord != endWord
     * All the words in wordList are unique.
     * <p>
     * Approach: BFS
     * Similar to LeetCode 127: https://leetcode.com/problems/word-ladder/, the key is about how to build the graph and
     * search in an efficient way. The easiest way is for each word, we replace each position as a star which indicates
     * one character change. For example, "hit" will be changed to "*it", "h*t", and "hi*", the list of words under the same
     * pattern will only have one character difference. After building the graph, we can search from the beginning word
     * using BFS. Therefore, when the ending word is hit, it's guaranteed that the sequence is of the shortest length.
     * <p>
     * Time: O(NL^2) where n is the number of words in the input list, L is the average length of word. For building graph,
     * we will enumerate L options for each word, which takes O(NL) time. And for each pattern, we need to build a new string
     * of L length, which takes O(L) time, the total runtime for graph build will be O(NL^2). During BFS, at the beginning word,
     * we have L options for example, "hit" -> ["*it", "h*t", "hi*"] 3 options, after picking up a pattern and select a word,
     * it will have (L - 1) options, we don't to jump back to where we came from to avoid cycle. This will end up being as
     * O(L^2) pairs. And in the worst case, we need to do BFS on each word in the list.
     * Space: O(NL)
     */
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        // build the graph for search
        Map<String, List<String>> graph = new HashMap<>();
        buildGraph(graph, wordList);

        List<List<String>> res = new ArrayList<>();
        // use a visited set to avoid cycle
        Set<String> visited = new HashSet<>();
        // add begin word in the queue as a starting point
        Queue<String> wordQueue = new LinkedList<>();
        wordQueue.add(beginWord);
        // also need a queue to store each sequence
        Queue<List<String>> sequenceQueue = new LinkedList<>();
        sequenceQueue.add(new ArrayList<>(List.of(beginWord)));
        int minSequenceLength = Integer.MAX_VALUE;

        while (!wordQueue.isEmpty()) {
            int size = wordQueue.size();

            for (int i = 0; i < size; i++) {
                String currWord = wordQueue.poll();
                List<String> currSequence = sequenceQueue.poll();

                // if we hit the end word, add the smallest length of sequence into the result list only
                if (currWord.equals(endWord) && currSequence.size() <= minSequenceLength) {
                    minSequenceLength = currSequence.size();
                    res.add(currSequence);
                } else {
                    // otherwise, add current word in the set to avoid revisit
                    visited.add(currWord);

                    // search the neighbor which has one character difference
                    for (int j = 0; j < currWord.length(); j++) {
                        String pattern = currWord.substring(0, j) + "*" + currWord.substring(j + 1);

                        for (String neighbor : graph.getOrDefault(pattern, new ArrayList<>())) {
                            // only add non-visited neighbors into the queue to avoid cycle
                            if (!visited.contains(neighbor)) {
                                wordQueue.add(neighbor);
                                // make a deep copy of current sequence
                                List<String> nextSequence = new ArrayList<>(currSequence);
                                // add the next word in the sequence
                                nextSequence.add(neighbor);
                                // push it into the queue
                                sequenceQueue.add(nextSequence);
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    private void buildGraph(Map<String, List<String>> graph, List<String> wordList) {
        for (String word : wordList) {
            for (int i = 0; i < word.length(); i++) {
                // change each word to L patterns, where L is the length of the word
                String pattern = word.substring(0, i) + "*" + word.substring(i + 1);
                graph.putIfAbsent(pattern, new ArrayList<>());
                graph.get(pattern).add(word);
            }
        }
    }

    @Test
    public void findLaddersTest() {
        /**
         * Example 1:
         * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
         * Output: [["hit","hot","dot","dog","cog"],["hit","hot","lot","log","cog"]]
         * Explanation: There are 2 shortest transformation sequences:
         * "hit" -> "hot" -> "dot" -> "dog" -> "cog"
         * "hit" -> "hot" -> "lot" -> "log" -> "cog"
         */
        List<List<String>> expected1 = List.of(List.of("hit", "hot", "dot", "dog", "cog"),
                List.of("hit", "hot", "lot", "log", "cog"));
        List<List<String>> actual1 = findLadders("hit", "cog",
                List.of("hot", "dot", "dog", "lot", "log", "cog"));
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i).size(), actual1.get(i).size());
            for (int j = 0; j < expected1.get(i).size(); j++) {
                assertEquals(expected1.get(i).get(j), actual1.get(i).get(j));
            }
        }
        /**
         * Example 2:
         * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
         * Output: []
         * Explanation: The endWord "cog" is not in wordList, therefore there is no valid transformation sequence.
         */
        List<List<String>> actual2 = findLadders("hit", "cog",
                List.of("hot", "dot", "dog", "lot", "log"));
        assertTrue(actual2.isEmpty());
        /**
         * Example 3:
         * Input: beginWord = "red", endWord = "tax", wordList = ["ted","tex","red","tax","tad","den","rex","pee"]
         * Output: [["red","ted","tad","tax"],["red","ted","tex","tax"],["red","rex","tex","tax"]]
         */
        List<List<String>> expected3 = List.of(List.of("red", "ted", "tad", "tax"),
                List.of("red", "ted", "tex", "tax"), List.of("red", "rex", "tex", "tax"));
        List<List<String>> actual3 = findLadders("red", "tax",
                List.of("ted", "tex", "red", "tax", "tad", "den", "rex", "pee"));
        assertEquals(expected3.size(), actual3.size());
        for (int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i).size(), actual3.get(i).size());
            for (int j = 0; j < expected3.get(i).size(); j++) {
                assertEquals(expected3.get(i).get(j), actual3.get(i).get(j));
            }
        }
    }
}
