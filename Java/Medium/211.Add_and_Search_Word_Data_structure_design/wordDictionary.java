public class wordDictionary {

    /**
     * Design a data structure that supports the following two operations:
     *
     * void addWord(word)
     * bool search(word)
     * search(word) can search a literal word or a regular expression string containing only letters a-z or .. A . means it can
     * represent any one letter.
     *
     * Approach: Trie
     * 此题本质上和208一样，需要用一个trie结构来进行词的搜索。不同的一点是，因为输入字符串中可能有'.'，可以代表任何字符。因此若遇到其他字符时，仍可以用和208
     * 一样的方法持续搜索，直到遇到null返回false，或者找到该单词，返回true。当遇到'.'时，需要遍历当前这一次所有可能的节点，然后继续顺序搜索。若某一条分支
     * 的末尾不是null，同时也是一个单词，那么这条分支可以直接return true。反之则需要继续遍历下一条可能的分支。
     *
     * Time: O(n*L)，其中n为单词个数，L为单词平均长度，在最坏情况下，需要遍历trie内的所有节点才能找到含'.'的字符
     * Space: O(n*L) 需要将所有的字符构造一个节点
     */
    private class TrieNode {
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
    }

    private TrieNode root;

    /** Initialize your data structure here. */
    public wordDictionary() {
        this.root = new TrieNode();
    }

    /** Adds a word into the data structure. */
    public void addWord(String word) {
        TrieNode ptr = root;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!ptr.containsKey(c)) {
                ptr.put(c, new TrieNode());
            }
            ptr = ptr.get(c);
        }
        //插入单词后，记得将最后一个字符节点记为word
        ptr.setAsWord();
    }

    /** Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter. */
    public boolean search(String word) {
        return find(word, 0, root);
    }

    //需要一个helper函数在trie进行搜索
    private boolean find(String word, int index, TrieNode node) {
        //base case，若对当前单词遍历至结尾，只需判断当前节点是否为单词即可
        if(index == word.length()) {
            return node.isWord();
        }

        char c = word.charAt(index);
        //如果当前字符不是'.'，可以像208一样，继续向前搜索
        if(c != '.') {
            //若当前字符不在trie中，可以直接返回false
            if(!node.containsKey(c)) return false;
            //否则则向前搜索一位，index要加1，同时trie的节点也要移动到下一个
            return find(word, index + 1, node.get(c));
        }
        //如果当前字符为'.'，那么最多则有26个分支需要查看
        for(int i = 0; i < 26; i++) {
            char curr = (char) (i + 'a');
            //如果当前节点的孩子节点中有当前字符，则继续搜索
            if(node.containsKey(curr)) {
                //如果继续搜索当前分支能找到一个单词，可以直接return true
                if(find(word, index + 1, node.get(curr))) {
                    return true;
                }
            }
        }
        //最后，若未找到该单词，或该字符串不是单词，返回false
        return false;
    }
}
