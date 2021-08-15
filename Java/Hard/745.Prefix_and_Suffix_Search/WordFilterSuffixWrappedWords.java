public class WordFilterSuffixWrappedWords {

    /**
     * Approach 2: Suffix wrapped words
     * Actually, we could change the word a little by adding substrings of the suffix ahead of the original word. For instance,
     * for word "apple", we add "#apple", "e#apple", "le#apple", "ple#apple", "pple#apple", and "apple#apple" into the trie.
     * During query, we search whether suffix#prefix exists in the trie, e.g. prefix = "ap", suffix = "le", we query "le#ap"
     * <p>
     * Time:
     * Constructor: O(N * L^2) since now we will have L(L + 1)/2 suffix + prefix combinations to be inserted into the trie
     * Query: O(L) now we only need to search the length of 2L at most
     * Space: O(N * L^2) for building the trie
     */
    private final TrieNode head;

    public WordFilterSuffixWrappedWords(String[] words) {
        head = new TrieNode();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            StringBuilder sb = new StringBuilder();
            sb.append('#').append(word);
            for (int j = word.length() - 1; j >= 0; j--) {
                sb.insert(0, word.charAt(j));
                insert(sb.toString(), i);
            }
        }
    }

    private void insert(String s, int index) {
        TrieNode ptr = head;
        for (char c : s.toCharArray()) {
            if (!ptr.containsKey(c)) ptr.put(c);
            ptr = ptr.get(c);
            ptr.setIndex(index);
        }
    }

    public int f(String prefix, String suffix) {
        String s = suffix + "#" + prefix;
        TrieNode ptr = head;
        for (char c : s.toCharArray()) {
            if (!ptr.containsKey(c)) return -1;
            ptr = ptr.get(c);
        }
        return ptr.getIndex();
    }

    private static class TrieNode {
        private TrieNode[] children;
        private int index;

        public TrieNode() {
            this.children = new TrieNode[128];
            this.index = -1;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean containsKey(char c) {
            return this.children[c] != null;
        }

        public void put(char c) {
            this.children[c] = new TrieNode();
        }

        public TrieNode get(char c) {
            return this.children[c];
        }
    }
}
