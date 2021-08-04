import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ReplaceWords {

    /**
     * In English, we have a concept called root, which can be followed by some other word to form another longer word -
     * let's call this word successor. For example, when the root "an" is followed by the successor word "other", we can
     * form a new word "another".
     * <p>
     * Given a dictionary consisting of many roots and a sentence consisting of words separated by spaces, replace all the
     * successors in the sentence with the root forming it. If a successor can be replaced by more than one root, replace it
     * with the root that has the shortest length.
     * <p>
     * Return the sentence after the replacement.
     * <p>
     * Constraints:
     * <p>
     * 1 <= dictionary.length <= 1000
     * 1 <= dictionary[i].length <= 100
     * dictionary[i] consists of only lower-case letters.
     * 1 <= sentence.length <= 10^6
     * sentence consists of only lower-case letters and spaces.
     * The number of words in sentence is in the range [1, 1000]
     * The length of each word in sentence is in the range [1, 1000]
     * Each two consecutive words in sentence will be separated by exactly one space.
     * sentence does not have leading or trailing spaces.
     * <p>
     * Approach 1: Prefix Hash
     * We can translate the prefix dictionary list into a hash set, for a given word in the sentence, we can enumerate all
     * possible prefix and check the existence in the set. If it exists, then we can replace the original word with the root.
     * <p>
     * Time: O(sum(wi^2)) where wi is the length of the i-th word in the sentence, for each word, we enumerate all the possible
     * prefixes from length 1, 2, ... until wi, each time we need to convert the string builder into a string to see whether it
     * exists in the hash set. It takes O(wi^2) for each word.
     * Space: O(M + N) M is the number of roots in the dictionary, we need a string array to store all the words in the sentence
     */
    public String replaceWordsHashSet(List<String> dictionary, String sentence) {
        Set<String> roots = new HashSet<>(dictionary);
        String[] words = sentence.split(" ");

        for (int i = 0; i < words.length; i++) {
            // use string builder to enumerate all prefixes
            StringBuilder sb = new StringBuilder();
            for (char c : words[i].toCharArray()) {
                sb.append(c);
                if (roots.contains(sb.toString())) {
                    // replace the word with the root
                    words[i] = sb.toString();
                    break;
                }
            }
        }
        return String.join(" ", words);
    }

    /**
     * Approach 2: Trie
     * <p>
     * We can take advantage of the trie structure to speed up the search. Using trie, we can search the character in each word
     * on the fly, if the prefix doesn't exist in the trie - use the original word. Otherwise we replace the word with the
     * first full word in the trie.
     * <p>
     * <p>
     * Time: O(N) in the worst case we need to traverse the entire string, the search in trie takes constant time
     * Space: O(M + N) We need to convert the dictionary list into a trie - also need a string array to store all the words
     */
    private TrieNode head;

    public String replaceWordsTrie(List<String> dictionary, String sentence) {
        head = new TrieNode();
        for (String root : dictionary) {
            // construct the trie with all roots
            insert(root);
        }

        String[] words = sentence.split(" ");
        for (int i = 0; i < words.length; i++) {
            // initiate a string builder in case the word needs to be replaced
            StringBuilder sb = new StringBuilder();
            // assign a pointer to search in trie
            TrieNode ptr = head;
            for (char c : words[i].toCharArray()) {
                // if the prefix of current word doesn't exist in the trie
                // or the shortest root has been found - jump out of the loop
                if (!ptr.containsKey(c) || ptr.isRoot()) break;
                // otherwise, keep searching and append the character into the builder
                ptr = ptr.get(c);
                sb.append(c);
            }
            // replace the current word if a root has been found
            if (ptr.isRoot()) {
                words[i] = sb.toString();
            }
        }
        return String.join(" ", words);
    }

    private void insert(String s) {
        TrieNode ptr = head;
        for (char c : s.toCharArray()) {
            if (!ptr.containsKey(c)) {
                ptr.put(c, new TrieNode());
            }
            ptr = ptr.get(c);
        }
        ptr.setAsRoot();
    }

    private static class TrieNode {
        private boolean isRoot;
        private TrieNode[] children;

        public TrieNode() {
            this.isRoot = false;
            this.children = new TrieNode[26];
        }

        public TrieNode get(char c) {
            return this.children[c - 'a'];
        }

        public boolean containsKey(char c) {
            return this.children[c - 'a'] != null;
        }

        public void put(char c, TrieNode children) {
            this.children[c - 'a'] = children;
        }

        public void setAsRoot() {
            this.isRoot = true;
        }

        public boolean isRoot() {
            return this.isRoot;
        }
    }


    @Test
    public void replaceWordsTest() {
        /**
         * Example 1:
         * Input: dictionary = ["cat","bat","rat"], sentence = "the cattle was rattled by the battery"
         * Output: "the cat was rat by the bat"
         */
        List<String> dictionary1 = List.of("cat", "bat", "rat");
        String actualHashSet1 = replaceWordsHashSet(dictionary1, "the cattle was rattled by the battery");
        String actualTrie1 = replaceWordsTrie(dictionary1, "the cattle was rattled by the battery");
        String expected1 = "the cat was rat by the bat";
        assertEquals(expected1, actualHashSet1);
        assertEquals(expected1, actualTrie1);
        /**
         * Example 2:
         * Input: dictionary = ["a","b","c"], sentence = "aadsfasf absbs bbab cadsfafs"
         * Output: "a a b c"
         */
        List<String> dictionary2 = List.of("a", "b", "c");
        String actualHashSet2 = replaceWordsHashSet(dictionary2, "aadsfasf absbs bbab cadsfafs");
        String actualTrie2 = replaceWordsTrie(dictionary2, "aadsfasf absbs bbab cadsfafs");
        String expected2 = "a a b c";
        assertEquals(expected2, actualHashSet2);
        assertEquals(expected2, actualTrie2);
        /**
         * Example 3:
         * Input: dictionary = ["a", "aa", "aaa", "aaaa"], sentence = "a aa a aaaa aaa aaa aaa aaaaaa bbb baba ababa"
         * Output: "a a a a a a a a bbb baba a"
         */
        List<String> dictionary3 = List.of("a", "aa", "aaa", "aaaa");
        String actualHashSet3 = replaceWordsHashSet(dictionary3, "a aa a aaaa aaa aaa aaa aaaaaa bbb baba ababa");
        String actualTrie3 = replaceWordsTrie(dictionary3, "a aa a aaaa aaa aaa aaa aaaaaa bbb baba ababa");
        String expected3 = "a a a a a a a a bbb baba a";
        assertEquals(expected3, actualHashSet3);
        assertEquals(expected3, actualTrie3);
        /**
         * Example 4:
         * Input: dictionary = ["catt","cat","bat","rat"], sentence = "the cattle was rattled by the battery"
         * Output: "the cat was rat by the bat"
         */
        List<String> dictionary4 = List.of("catt", "cat", "bat", "rat");
        String actualHashSet4 = replaceWordsHashSet(dictionary4, "the cattle was rattled by the battery");
        String actualTrie4 = replaceWordsTrie(dictionary4, "the cattle was rattled by the battery");
        String expected4 = "the cat was rat by the bat";
        assertEquals(expected4, actualHashSet4);
        assertEquals(expected4, actualTrie4);
        /**
         * Example 5:
         * Input: dictionary = ["ac","ab"], sentence = "it is abnormal that this solution is accepted"
         * Output: "it is ab that this solution is ac"
         */
        List<String> dictionary5 = List.of("ac", "ab");
        String actualHashSet5 = replaceWordsHashSet(dictionary5, "it is abnormal that this solution is accepted");
        String actualTrie5 = replaceWordsTrie(dictionary5, "it is abnormal that this solution is accepted");
        String expected5 = "it is ab that this solution is ac";
        assertEquals(expected5, actualHashSet5);
        assertEquals(expected5, actualTrie5);
    }
}
