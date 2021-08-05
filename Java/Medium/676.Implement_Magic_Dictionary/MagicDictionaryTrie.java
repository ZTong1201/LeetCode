public class MagicDictionaryTrie {

    /**
     * Design a data structure that is initialized with a list of different words. Provided a string, you should determine
     * if you can change exactly one character in this string to match any word in the data structure.
     * <p>
     * Implement the MagicDictionary class:
     * <p>
     * MagicDictionary() Initializes the object.
     * void buildDict(String[] dictionary) Sets the data structure with an array of distinct strings dictionary.
     * bool search(String searchWord) Returns true if you can change exactly one character in searchWord to match any string
     * in the data structure, otherwise returns false.
     * <p>
     * Constraints:
     * <p>
     * 1 <= dictionary.length <= 100
     * 1 <= dictionary[i].length <= 100
     * dictionary[i] consists of only lower-case English letters.
     * All the strings in dictionary are distinct.
     * 1 <= searchWord.length <= 100
     * searchWord consists of only lower-case English letters.
     * buildDict will be called only once before search.
     * At most 100 calls will be made to search.
     * <p>
     * Approach 1: Trie
     * <p>
     * Time: O(L * 25^L) where L is the length of word length. Search a word of length L in trie takes O(L) time, for a given
     * word, there are 25 options (the original char doesn't count) at each position, so there will be 25^L new words to be
     * searched.
     * Space: O(N * M) N is the number of words in the array, and M is the average length of words
     */
    private final TrieNode head;

    public MagicDictionaryTrie() {
        head = new TrieNode();
    }

    public void buildDict(String[] dictionary) {
        for (String word : dictionary) {
            TrieNode ptr = head;
            for (char c : word.toCharArray()) {
                if (!ptr.containsKey(c)) {
                    ptr.put(c, new TrieNode());
                }
                ptr = ptr.get(c);
            }
            ptr.setAsWord();
        }
    }

    public boolean search(String searchWord) {
        // convert input string into char array to mutate at each index
        char[] word = searchWord.toCharArray();
        for (int i = 0; i < word.length; i++) {
            // at each index, there are 25 options (excluding the original char) to be searched
            for (char c = 'a'; c <= 'z'; c++) {
                // skip the original char
                if (word[i] == c) continue;
                char temp = word[i];
                // modify the word by one character
                word[i] = c;
                // check the existence of modified word
                if (containsWord(word)) return true;
                // change the word back
                word[i] = temp;
            }
        }
        return false;
    }

    /**
     * Helper method to check whether the given (modified) word exists in the trie
     *
     * @param word a word string in character array format
     * @return true if the word exists
     */
    private boolean containsWord(char[] word) {
        TrieNode ptr = head;
        for (char c : word) {
            // if the character doesn't exist - return false
            if (!ptr.containsKey(c)) return false;
            ptr = ptr.get(c);
        }
        // found all character - need to finally check whether it's a word
        return ptr.isWord();
    }

    private static class TrieNode {
        private TrieNode[] children;
        private boolean isWord;

        public TrieNode() {
            this.isWord = false;
            this.children = new TrieNode[26];
        }

        public boolean isWord() {
            return this.isWord;
        }

        public void setAsWord() {
            this.isWord = true;
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
