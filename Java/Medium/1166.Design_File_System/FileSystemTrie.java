public class FileSystemTrie {

    /**
     * Approach 2: Trie
     * We can store the file paths into a trie to better improve the search performance.
     * <p>
     * Time:
     * createPath(): O(n * L), for each path, we need to go through the entire string to find parent path and check the existence
     * of parent path and the original path
     * get(): O(L)
     * <p>
     * Space: O(n * L) if there are n valid paths and on average the length is O(L)
     */
    private final TrieNode head;

    public FileSystemTrie() {
        head = new TrieNode();
    }

    public boolean createPath(String path, int value) {
        int lastIndex = path.lastIndexOf("/");
        // edge case 1: if it's an invalid path or a duplicate path - return false;
        if (lastIndex == -1 || findValue(path) != -1) return false;
        // edge case 2: "/a" is a valid path since parent "" is always valid
        if (lastIndex == 0) {
            insert(path, value);
            return true;
        }
        
        // find parent path
        String parentPath = path.substring(0, lastIndex);
        // edge case 3: if the parent path doesn't exist - return false
        if (findValue(parentPath) == -1) return false;

        // insert the key-value pair
        insert(path, value);
        return true;
    }

    public int get(String path) {
        return findValue(path);
    }

    private void insert(String path, int value) {
        TrieNode ptr = head;
        for (int i = 0; i < path.length(); i++) {
            char curr = path.charAt(i);
            if (!ptr.containsKey(curr)) ptr.put(curr);
            ptr = ptr.get(curr);
        }
        ptr.setValue(value);
    }

    private int findValue(String path) {
        TrieNode ptr = head;
        for (int i = 0; i < path.length(); i++) {
            char curr = path.charAt(i);
            if (!ptr.containsKey(curr)) return -1;
            ptr = ptr.get(curr);
        }
        return ptr.getValue();
    }

    private static class TrieNode {
        private final TrieNode[] children;
        private int value;

        public TrieNode() {
            this.children = new TrieNode[128];
            this.value = -1;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public boolean containsKey(char c) {
            return this.children[c] != null;
        }

        public TrieNode get(char c) {
            return this.children[c];
        }

        public void put(char c) {
            this.children[c] = new TrieNode();
        }
    }
}
