import java.util.Stack;

public class MapSum {

    /**
     * Design a map that allows you to do the following:
     * <p>
     * Maps a string key to a given value.
     * Returns the sum of the values that have a key with a prefix equal to a given string.
     * Implement the MapSum class:
     * <p>
     * MapSum() Initializes the MapSum object.
     * void insert(String key, int val) Inserts the key-val pair into the map. If the key already existed, the original
     * key-value pair will be overridden to the new one.
     * int sum(string prefix) Returns the sum of all the pairs' value whose key starts with the prefix.
     * <p>
     * Constraints:
     * <p>
     * 1 <= key.length, prefix.length <= 50
     * key and prefix consist of only lowercase English letters.
     * 1 <= val <= 1000
     * At most 50 calls will be made to insert and sum.
     * <p>
     * Approach: Trie
     * <p>
     * Time: O(L) for insertion, O(N) for search where N is the number of nodes in the trie
     * Space: O(N)
     */
    private final TrieNode head;

    public MapSum() {
        head = new TrieNode();
    }

    public void insert(String key, int val) {
        TrieNode ptr = head;
        for (char c : key.toCharArray()) {
            if (!ptr.containsKey(c)) ptr.put(c, new TrieNode());
            ptr = ptr.get(c);
        }
        ptr.setValue(val);
    }

    public int sum(String prefix) {
        TrieNode ptr = head;
        int res = 0;
        // first search for the prefix in the trie
        for (char c : prefix.toCharArray()) {
            // the prefix doesn't even exist in the trie, return 0
            if (!ptr.containsKey(c)) return res;
            ptr = ptr.get(c);
        }
        // now use current trie node as a starting point to sum all the values
        Stack<TrieNode> stack = new Stack<>();
        stack.push(ptr);
        while (!stack.isEmpty()) {
            TrieNode curr = stack.pop();
            if (curr != null) {
                // sum the value of the node
                res += curr.getValue();
                // append the children into the stack
                for (TrieNode child : curr.getChildren()) {
                    if (child != null) stack.push(child);
                }
            }
        }
        return res;
    }

    private static class TrieNode {
        private int value;
        private TrieNode[] children;

        public TrieNode() {
            this.value = 0;
            this.children = new TrieNode[26];
        }

        public TrieNode[] getChildren() {
            return this.children;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int val) {
            this.value = val;
        }

        public boolean containsKey(char c) {
            return this.children[c - 'a'] != null;
        }

        public void put(char c, TrieNode children) {
            this.children[c - 'a'] = children;
        }

        public TrieNode get(char c) {
            return this.children[c - 'a'];
        }
    }
}
