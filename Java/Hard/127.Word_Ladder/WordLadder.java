import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class WordLadder {

    /**
     * Given two words (beginWord and endWord), and a dictionary's word list, find the length of shortest transformation sequence from beginWord to endWord, such that:
     * <p>
     * Only one letter can be changed at a time.
     * Each transformed word must exist in the word list. Note that beginWord is not a transformed word.
     * Note:
     * <p>
     * Return 0 if there is no such transformation sequence.
     * All words have the same length.
     * All words contain only lowercase alphabetic characters.
     * You may assume no duplicates in the word list.
     * You may assume beginWord and endWord are non-empty and are not the same.
     * <p>
     * Approach 1: Breadth-first search
     * The word ladder problem is a typical BFS problem. We can treat each word as a node in a graph, and if the word can be changed to
     * each other by only modifying one letter, there is an edge between two words/nodes. Hence, it is an undirected and unweighted
     * graph. We are supposed to find the shortest path between two nodes. The main question here is how to construct the graph.
     * The naive approach is that given a word, we search the whole wordList, if a word in the wordList is only letter different with
     * the given word, we treat it as a candidate node, and keep searching its next node until we find the endWord or nothing. However,
     * when we start search again, it is very painful to re-traverse the whole word list. Therefore, we might just slightly revise
     * our original word list by take "*" as a wildcard and construct new "words" whose size equals to the length of input word. For example,
     * if we have the word "hot", then we can find "hot" as "*ot", "h*t" and "ho*". We need a hash map to map between such wildcard words
     * with the actual word. In order to fully implement the BFS algorithm, we need a queue to store all the adjacent words, and the
     * another queue to store the corresponding distance from the beginning.
     * <p>
     * Meanwhile, in order to avoid cycles, i.e. revisit a node, we need a hash set to record the nodes seen so far.
     * <p>
     * Time: O(L*N) where L is the length of input word, N is the length of word list. The initial modification requires O(L*N) time.
     * The BFS requires at most O(n), a.k.a. search all the words in the list to find the answer
     * Space: O(L*N), the hash map to store wildcard words requires O(L*N), the visited hash set and two queues requires O(N) in the worst
     * case
     */
    public int ladderLengthBFS(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;   //if end word is not in the word list, simply return 0
        // we need a hash map to map from wildcard words to all the corresponding words in the list.
        Map<String, List<String>> graph = new HashMap<>();
        // all the words have the same length
        int length = beginWord.length();
        for (String word : wordList) {
            for (int i = 0; i < length; i++) {
                // construct the wildcard word, # words = length of input word
                String newWord = word.substring(0, i) + '*' + word.substring(i + 1);
                graph.putIfAbsent(newWord, new ArrayList<>());
                graph.get(newWord).add(word);
            }
        }

        // a queue to store current node
        Queue<String> queue = new LinkedList<>();
        // add begin word in the queue
        queue.add(beginWord);
        int res = 0;
        // a hash set to record visited nodes to avoid cycles
        Set<String> seen = new HashSet<>();
        while (!queue.isEmpty()) {
            // the current word for searching
            String curr = queue.remove();
            // increment the number of changes to make the end word
            res++;
            // return the result if we hit the end word
            if (curr.equals(endWord)) return res;
            // avoid revisiting nodes
            if (!seen.contains(curr)) {
                seen.add(curr);
                for (int i = 0; i < length; i++) {
                    // find all the wildcard words for current word
                    String newWord = curr.substring(0, i) + "*" + curr.substring(i + 1);
                    // find all the words in the list match the wildcard pattern
                    for (String neighbor : graph.getOrDefault(newWord, new ArrayList<>())) {
                        // otherwise, we have visited a new node, we add it to the queue for further searching
                        queue.add(neighbor);
                    }
                }
            }
        }
        return 0;
    }

    /**
     * Approach 2: Bidirectional Breadth-First Search
     * The graph can be huge if we have too many nodes, the search process could be slow using BFS. We can actually do two BFSs
     * both from the beginning and the end. It is the so-called bidirectional BFS. We will start BFS from both sides, and
     * squeeze the graph to each other, we simply reduce half of the search time. However, by doing so, the termination condition
     * should be changed a little bit to fit this algorithm. We need two visited hash sets to store nodes seen so far from each
     * end. As long as we find a node that has already been visited from a different set, we are done BFS, and the final result is
     * the sum of distance from each origin.
     * <p>
     * Time: O(L*N), the initial modification will not change for this problem. However, we reduce the search time in half.
     * Space: O(L*N), all the wildcard words requires O(L*N) space, and we now need 4 queues and two hash sets which will require
     * O(N) space in the worst case.
     */
    public int ladderLengthBidirectional(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;
        Map<String, List<String>> allCombos = new HashMap<>();
        int length = beginWord.length();
        for (String word : wordList) {
            for (int i = 0; i < length; i++) {
                String wildcardWord = word.substring(0, i) + "*" + word.substring(i + 1);
                List<String> aList = allCombos.getOrDefault(wildcardWord, new ArrayList<>());
                aList.add(word);
                allCombos.put(wildcardWord, aList);
            }
        }

        Queue<String> wordQueue_begin = new LinkedList<>();
        Queue<String> wordQueue_end = new LinkedList<>();
        Queue<Integer> countQueue_begin = new LinkedList<>();
        Queue<Integer> countQueue_end = new LinkedList<>();
        wordQueue_begin.add(beginWord);
        wordQueue_end.add(endWord);
        countQueue_begin.add(1);
        countQueue_end.add(1);
        Map<String, Integer> visited_begin = new HashMap<>();
        Map<String, Integer> visited_end = new HashMap<>();
        visited_begin.put(beginWord, 1);
        visited_end.put(endWord, 1);

        int ans = 0;
        while (!wordQueue_begin.isEmpty() && !wordQueue_end.isEmpty()) {
            //BFS from the front
            ans = visitNode(wordQueue_begin, countQueue_begin, visited_begin, visited_end, allCombos);
            if (ans > 0) return ans;

            //BFS from the end
            ans = visitNode(wordQueue_end, countQueue_end, visited_end, visited_begin, allCombos);
            if (ans > 0) return ans;
        }
        return 0;
    }

    private int visitNode(Queue<String> wordQueue, Queue<Integer> countQueue, Map<String, Integer> visied1, Map<String, Integer> visited2,
                          Map<String, List<String>> allCombos) {
        String word = wordQueue.remove();
        int count = countQueue.remove();
        for (int i = 0; i < word.length(); i++) {
            String wildcardWord = word.substring(0, i) + "*" + word.substring(i + 1);

            for (String adjacentWord : allCombos.getOrDefault(wildcardWord, new ArrayList<>())) {
                if (visited2.containsKey(adjacentWord)) return count + visited2.get(adjacentWord);

                if (!visied1.containsKey(adjacentWord)) {
                    visied1.put(adjacentWord, count + 1);
                    wordQueue.add(adjacentWord);
                    countQueue.add(count + 1);
                }
            }
        }
        return -1;
    }

    @Test
    public void ladderLengthBidirectionalTest() {
        /**
         * Input:
         * beginWord = "hit",
         * endWord = "cog",
         * wordList = ["hot","dot","dog","lot","log","cog"]
         *
         * Output: 5
         *
         * Explanation: As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
         * return its length 5.
         */
        List<String> wordList1 = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"));
        String beginWord1 = "hit";
        String endWord1 = "cog";
        assertEquals(5, ladderLengthBidirectional(beginWord1, endWord1, wordList1));
        /**
         * Input:
         * beginWord = "hit",
         * endWord = "gut",
         * wordList = ["hot","dot","dog","lot","log","gut"]
         *
         * Output: 5
         *
         * Explanation: Even if "gut" in the word list, there is no way to transform from "hit" to "gut" with the words in the word list.
         */
        List<String> wordList2 = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log", "gut"));
        String beginWord2 = "hit";
        String endWord2 = "gut";
        assertEquals(0, ladderLengthBidirectional(beginWord2, endWord2, wordList2));
    }

    @Test
    public void ladderLengthBFSTest() {
        /**
         * Input:
         * beginWord = "hit",
         * endWord = "cog",
         * wordList = ["hot","dot","dog","lot","log","cog"]
         *
         * Output: 5
         *
         * Explanation: As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
         * return its length 5.
         */
        List<String> wordList1 = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"));
        String beginWord1 = "hit";
        String endWord1 = "cog";
        assertEquals(5, ladderLengthBFS(beginWord1, endWord1, wordList1));
        /**
         * Input:
         * beginWord = "hit",
         * endWord = "gut",
         * wordList = ["hot","dot","dog","lot","log","gut"]
         *
         * Output: 5
         *
         * Explanation: Even if "gut" in the word list, there is no way to transform from "hit" to "gut" with the words in the word list.
         */
        List<String> wordList2 = new ArrayList<>(Arrays.asList("hot", "dot", "dog", "lot", "log", "gut"));
        String beginWord2 = "hit";
        String endWord2 = "gut";
        assertEquals(0, ladderLengthBFS(beginWord2, endWord2, wordList2));
    }
}
