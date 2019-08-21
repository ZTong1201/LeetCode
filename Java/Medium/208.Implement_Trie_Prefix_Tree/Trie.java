import java.util.*;

public class Trie {

    /**
     * Implement a trie with insert, search, and startsWith methods.
     *
     * Note:
     *
     * You may assume that all inputs are consist of lowercase letters a-z.
     * All inputs are guaranteed to be non-empty strings.
     *
     * Trie结果需要构建一个新的class来表示trie中的节点，当前节点记录两个信息。
     * 1.到该字符为止，之前的string是否为一个单词
     * 2.其之后的孩子节点。因为保证了输入是a-z，因此可以用一个size为26的TrieNode数组，即每个index分别代表a-z，若该位置不为null，而为一个新的TrieNode，
     * 说明在之后还有后续字母。
     *
     * insert的时候只需要从树的头部开始，若当前字母不在当前树的level，就构建一个新的节点。若该节点已存在，则遍历到该节点，继续往下建立。
     * search和startsWith类似，可以创建一个helper函数来判断某给定字符串是否在trie中。即按照字符串的顺序对trie进行遍历，若某一时刻节点为null，说明该字符
     * 不在trie中，返回null即可。若所有字符均存在，则一直遍历到该节点，返回当前节点。
     * 对于search，若最后返回的节点不为null，同时该节点是一个单词，返回true，反之返回false
     * 对于startsWith，只要最后节点不为null即可。
     *
     * Time: O(L)，无论是search还是insert，都需要按顺序遍历整个字符串
     * Space: O(n*L) 假设单词的平均长度为L，插入n个单词总共需要n*L个节点
     */
    private class TrieNode {
        private boolean isWord;
        private TrieNode[] children;  //每个trie节点的孩子仍然为trie节点

        public TrieNode() {
            this.isWord = false;   //绝大多数节点不是单词，因此默认该节点isWord为false
            this.children = new TrieNode[26];  //因为输入字符只是a-z，每个trie节点最多有26个孩子节点
        }

        //判断当前节点是否是word
        public boolean isWord() {
            return isWord;
        }

        //将当前节点标记为word
        public void setAsWord() {
            isWord = true;
        }

        //判断当前节点的孩子节点是否包含字符c
        public boolean containsKey(char c) {
            return children[c - 'a'] != null;
        }

        //在当前节点之后插入字符c
        public void put(char c, TrieNode node) {
            children[c - 'a'] = node;
        }

        //在当前节点的孩子节点中返回字符c的节点，若字符c不存在，返回的是null
        public TrieNode get(char c) {
            return children[c - 'a'];
        }
    }

    private TrieNode root;

    /** Initialize your data structure here. */
    public Trie() {
        //要在整个trie的最上方建一个dummy节点
        this.root = new TrieNode();
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        TrieNode ptr = root;
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            //按顺序将每个字符插入trie中，若某字符已存在，则不用新建节点
            if(!ptr.containsKey(c)) {
                ptr.put(c, new TrieNode());
            }
            //然后将指针遍历到下一个节点
            ptr = ptr.get(c);
        }
        //在最后一个字符插入结束后，要将当前节点记录为单词
        ptr.setAsWord();
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        //该单词在trie中的条件是，find最后返回的节点不是null，同时最后一个节点isWord为true
        TrieNode node = find(word);
        return node != null && node.isWord();
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        //判断某prefix的单词是否在trie中，可以直接判断最后返回节点是否为null
        return find(prefix) != null;
    }

    //helper函数，用来返回输入字符串在trie的最后一个节点
    //若该字符串在trie中存在，则返回的是最后一个字符所在节点，若不存在，返回的是null
    private TrieNode find(String prefix) {
        TrieNode ptr = root;
        for(int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if(!ptr.containsKey(c)) {
                return null;
            }
            ptr = ptr.get(c);
        }
        return ptr;
    }
}
