public class WordDictionary {

    /**
     * Design a data structure that supports the following two operations:
     * <p>
     * void addWord(word)
     * bool search(word)
     * search(word) can search a literal word or a regular expression string containing only letters a-z or .. A . means it can
     * represent any one letter.
     * <p>
     * Approach: Trie
     * 此题本质上和208一样，需要用一个trie结构来进行词的搜索。不同的一点是，因为输入字符串中可能有'.'，可以代表任何字符。因此若遇到其他字符时，仍可以用和208
     * 一样的方法持续搜索，直到遇到null返回false，或者找到该单词，返回true。当遇到'.'时，需要遍历当前这一次所有可能的节点，然后继续顺序搜索。若某一条分支
     * 的末尾不是null，同时也是一个单词，那么这条分支可以直接return true。反之则需要继续遍历下一条可能的分支。
     * <p>
     * Time: O(n*L)，其中n为单词个数，L为单词平均长度，在最坏情况下，需要遍历trie内的所有节点才能找到含'.'的字符
     * Space: O(n*L) 需要将所有的字符构造一个节点
     */
    private static class TrieNode {

        private boolean isWord;
        private TrieNode[] children;

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
            return children[c - 'a'] != null;
        }

        public void put(char c, TrieNode node) {
            children[c - 'a'] = node;
        }

        public TrieNode get(char c) {
            return children[c - 'a'];
        }

        public TrieNode[] getChildren() {
            return children;
        }
    }

    private final TrieNode head;

    /**
     * Initialize your data structure here.
     */
    public WordDictionary() {
        this.head = new TrieNode();
    }

    /**
     * Adds a word into the data structure.
     */
    public void addWord(String word) {
        TrieNode ptr = head;
        for (char c : word.toCharArray()) {
            if (!ptr.containsKey(c)) {
                ptr.put(c, new TrieNode());
            }
            ptr = ptr.get(c);
        }
        //插入单词后，记得将最后一个字符节点记为word
        ptr.setAsWord();
    }

    /**
     * Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter.
     */
    public boolean search(String word) {
        return searchInTrie(word, head);
    }

    //需要一个helper函数在trie进行搜索
    private boolean searchInTrie(String word, TrieNode head) {
        TrieNode ptr = head;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            // search all the 26 candidates recursively when the character is '.'
            if (c == '.') {
                for (TrieNode child : ptr.getChildren()) {
                    // if we can find a word in the trie to match up with the wildcard '.'
                    // then return true
                    if (child != null && searchInTrie(word.substring(i + 1), child)) return true;
                }
                // otherwise, return false if all 26 options have been comprehensively visited
                return false;
            } else {
                // if the character is not '.', check whether it exists in the trie
                // if not, return false directly
                if (!ptr.containsKey(c)) return false;
                // otherwise, move to the next node
                ptr = ptr.get(c);
            }
        }
        // check if the final node is actually a word
        return ptr.isWord();
    }
}
