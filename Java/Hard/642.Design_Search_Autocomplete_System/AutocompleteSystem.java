import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class AutocompleteSystem {

    /**
     * Design a search autocomplete system for a search engine. Users may input a sentence (at least one word and end with a
     * special character '#').
     * <p>
     * You are given a string array sentences and an integer array times both of length n where sentences[i] is a previously
     * typed sentence and times[i] is the corresponding number of times the sentence was typed. For each input character
     * except '#', return the top 3 historical hot sentences that have the same prefix as the part of the sentence already
     * typed.
     * <p>
     * Here are the specific rules:
     * <p>
     * The hot degree for a sentence is defined as the number of times a user typed the exactly same sentence before.
     * The returned top 3 hot sentences should be sorted by hot degree (The first is the hottest one). If several sentences
     * have the same hot degree, use ASCII-code order (smaller one appears first).
     * If less than 3 hot sentences exist, return as many as you can.
     * When the input is a special character, it means the sentence ends, and in this case, you need to return an empty list.
     * Implement the AutocompleteSystem class:
     * <p>
     * AutocompleteSystem(String[] sentences, int[] times) Initializes the object with the sentences and times arrays.
     * List<String> input(char c) This indicates that the user typed the character c.
     * Returns an empty array [] if c == '#' and stores the inputted sentence in the system.
     * Returns the top 3 historical hot sentences that have the same prefix as the part of the sentence already typed.
     * If there are fewer than 3 matches, return them all.
     * <p>
     * Constraints:
     * <p>
     * n == sentences.length
     * n == times.length
     * 1 <= n <= 100
     * 1 <= sentences[i].length <= 100
     * 1 <= times[i] <= 50
     * c is a lowercase English letter, a hash '#', or space ' '.
     * Each tested sentence will be a sequence of characters c that end with the character '#'.
     * Each tested sentence will have a length in the range [1, 200].
     * The words in each input sentence are separated by single spaces.
     * At most 5000 calls will be made to input.
     * <p>
     * Approach: Trie + Hash Map
     * To have a better performance on searching by prefix, we should consider the trie data structure. We also need to keep
     * track of the frequency of each sentence, and note that the frequency can be updated while the user is searching. The
     * best data structure is a hash map to store a key-value pair.
     * <p>
     * Time: O(m * n * L + (m * n)logn) where n is the total number of unique sentences in the trie and L is the average of length of sentences,
     * therefore, in the worst case, we will have n * L total nodes in the trie. m is the number of input calls. In the
     * worst case, we need to go through the entire trie. Afterwards, we need to sort the suggestion list. In the worst case,
     * all sentences will be present in the result list, sorting list with n sentences takes O(nlogn) time, in the worst case,
     * we need O(m * nlogn)
     * Space: O(n * L) we have n * L nodes in the trie, we also need a map to store n key-value pairs, which gives n * L + n,
     * hence in total it will be O(n * L)
     */
    private final TrieNode trie;
    private final Map<String, Integer> sentenceFrequency;
    private final StringBuilder userInput;

    public AutocompleteSystem(String[] sentences, int[] times) {
        this.trie = new TrieNode();
        this.sentenceFrequency = new HashMap<>();
        this.userInput = new StringBuilder();
        // insert each sentence into the trie and store the frequency of each sentence
        for (int i = 0; i < sentences.length; i++) {
            insertSentence(sentences[i]);
            // use getOrDefault in case there is duplicate in the sentences array
            this.sentenceFrequency.put(sentences[i], this.sentenceFrequency.getOrDefault(sentences[i], 0) + times[i]);
        }
    }

    public List<String> input(char c) {
        // if the input char is '#', it's the end of the input sentence
        // we should always return an empty list in this case,
        // and insert this new sentence into the trie and the map
        if (c == '#') {
            String typedString = userInput.toString();
            sentenceFrequency.put(typedString, sentenceFrequency.getOrDefault(typedString, 0) + 1);
            insertSentence(typedString);
            // reset the string builder for next search
            userInput.setLength(0);
            return new ArrayList<>();
        }

        // append the current character to the prefix
        userInput.append(c);
        // otherwise, we need to search all sentences based on the current prefix
        List<String> suggestions = startsWith(userInput.toString());
        // then sort the suggestion list by the frequency then by the string order
        suggestions.sort((a, b) -> {
            if (sentenceFrequency.get(a) == sentenceFrequency.get(b)) {
                return a.compareTo(b);
            }
            return Integer.compare(sentenceFrequency.get(b), sentenceFrequency.get(a));
        });

        // chop off the rest of the list and return top 3 when there are more than 3 suggestions,
        // otherwise, return all of them
        return suggestions.size() > 3 ? suggestions.subList(0, 3) : suggestions;
    }

    private List<String> startsWith(String prefix) {
        TrieNode ptr = trie;
        // first - check whether all characters in the prefix exist in the trie

        for (int i = 0; i < prefix.length(); i++) {
            char curr = prefix.charAt(i);
            // if there is no sentence starts with the current prefix, return an empty list
            if (!ptr.containsKey(curr)) return new ArrayList<>();
            ptr = ptr.get(curr);
        }

        List<String> res = new ArrayList<>();
        // run DFS to search all eligible sentences
        Stack<TrieNode> stack = new Stack<>();
        stack.push(ptr);

        while (!stack.isEmpty()) {
            TrieNode curr = stack.pop();

            // if we find a sentence, add it to the result list
            if (curr.getSentence() != null) res.add(curr.getSentence());

            for (TrieNode child : curr.getChildren()) {
                if (child != null) {
                    stack.push(child);
                }
            }
        }
        return res;
    }

    private void insertSentence(String sentence) {
        TrieNode ptr = trie;
        for (int i = 0; i < sentence.length(); i++) {
            char curr = sentence.charAt(i);

            if (!ptr.containsKey(curr)) ptr.put(curr);
            ptr = ptr.get(curr);
        }
        ptr.setSentence(sentence);
    }

    private static class TrieNode {
        private final TrieNode[] children;
        private String sentence;

        public TrieNode() {
            // since " " will be included, use an array of size 128
            this.children = new TrieNode[128];
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getSentence() {
            return this.sentence;
        }

        public void put(char c) {
            this.children[c] = new TrieNode();
        }

        public boolean containsKey(char c) {
            return this.children[c] != null;
        }

        public TrieNode get(char c) {
            return this.children[c];
        }

        public TrieNode[] getChildren() {
            return this.children;
        }
    }
}
