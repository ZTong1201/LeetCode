import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class WordFilterTwoTries {

    /**
     * Design a special dictionary with some words that searchs the words in it by a prefix and a suffix.
     * <p>
     * Implement the WordFilter class:
     * <p>
     * WordFilter(string[] words) Initializes the object with the words in the dictionary.
     * f(string prefix, string suffix) Returns the index of the word in the dictionary, which has the prefix and the
     * suffix. If there is more than one valid index, return the largest of them. If there is no such word in the dictionary,
     * return -1.
     * <p>
     * Constraints:
     * <p>
     * 1 <= words.length <= 15000
     * 1 <= words[i].length <= 10
     * 1 <= prefix.length, suffix.length <= 10
     * words[i], prefix and suffix consist of lower-case English letters only.
     * At most 15000 calls will be made to the function f.
     * <p>
     * Approach 1: Prefix and suffix tries + set intersection
     * Construct two tries, one is the original order and the other is in the reverse order (treat the suffix as prefix).
     * Search a set of indexes for prefix/suffix and find the largest intersected index in both sets
     * <p>
     * Time:
     * Constructor: O(N * L) where L is the average length of the words.
     * Query: O(N + L) We need O(L) time to find the set of indexes and in the worst case the set contains all the indexes,
     * and we need to traverse every one of them to find the answer.
     * Space: O(N * L) for both prefix and suffix tries
     */
    private final TrieNode prefixTrie;
    private final TrieNode suffixTrie;

    public WordFilterTwoTries(String[] words) {
        prefixTrie = new TrieNode();
        suffixTrie = new TrieNode();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            insert(word, i, prefixTrie);
            insert(reverseString(word), i, suffixTrie);
        }
    }

    private void insert(String s, int index, TrieNode head) {
        TrieNode ptr = head;
        for (char c : s.toCharArray()) {
            if (!ptr.containsKey(c)) ptr.put(c);
            ptr = ptr.get(c);
        }
        ptr.setIndex(index);
    }

    public int f(String prefix, String suffix) {
        TrieNode p1 = search(prefix, prefixTrie);
        TrieNode p2 = search(reverseString(suffix), suffixTrie);
        Set<Integer> prefixIndex = searchIndex(p1), suffixIndex = searchIndex(p2);
        int res = -1;
        for (int index : suffixIndex) {
            if (prefixIndex.contains(index)) res = Math.max(res, index);
        }
        return res;
    }

    private Set<Integer> searchIndex(TrieNode head) {
        Set<Integer> res = new HashSet<>();
        Stack<TrieNode> stack = new Stack<>();
        stack.push(head);

        while (!stack.isEmpty()) {
            TrieNode curr = stack.pop();
            if (curr != null) {
                res.add(curr.getIndex());
                for (TrieNode child : curr.getChildren()) {
                    if (child != null) stack.push(child);
                }
            }
        }
        return res;
    }

    private String reverseString(String s) {
        StringBuilder reversed = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            reversed.append(s.charAt(i));
        }
        return reversed.toString();
    }

    private TrieNode search(String substring, TrieNode head) {
        TrieNode ptr = head;
        for (char c : substring.toCharArray()) {
            if (!ptr.containsKey(c)) return null;
            ptr = ptr.get(c);
        }
        return ptr;
    }

    private static class TrieNode {
        private final TrieNode[] children;
        private int index;

        public TrieNode() {
            this.children = new TrieNode[26];
            this.index = -1;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean containsKey(char c) {
            return this.children[c - 'a'] != null;
        }

        public void put(char c) {
            this.children[c - 'a'] = new TrieNode();
        }

        public TrieNode get(char c) {
            return this.children[c - 'a'];
        }

        public TrieNode[] getChildren() {
            return this.children;
        }
    }
}
