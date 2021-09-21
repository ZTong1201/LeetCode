import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;

public class VowelSpellchecker {

    /**
     * Given a wordlist, we want to implement a spellchecker that converts a query word into a correct word.
     * <p>
     * For a given query word, the spell checker handles two categories of spelling mistakes:
     * <p>
     * Capitalization: If the query matches a word in the wordlist (case-insensitive), then the query word is returned with
     * the same case as the case in the wordlist.
     * Example: wordlist = ["yellow"], query = "YellOw": correct = "yellow"
     * Example: wordlist = ["Yellow"], query = "yellow": correct = "Yellow"
     * Example: wordlist = ["yellow"], query = "yellow": correct = "yellow"
     * <p>
     * Vowel Errors: If after replacing the vowels ('a', 'e', 'i', 'o', 'u') of the query word with any vowel individually,
     * it matches a word in the wordlist (case-insensitive), then the query word is returned with the same case as the match
     * in the wordlist.
     * Example: wordlist = ["YellOw"], query = "yollow": correct = "YellOw"
     * Example: wordlist = ["YellOw"], query = "yeellow": correct = "" (no match)
     * Example: wordlist = ["YellOw"], query = "yllw": correct = "" (no match)
     * <p>
     * In addition, the spell checker operates under the following precedence rules:
     * When the query exactly matches a word in the wordlist (case-sensitive), you should return the same word back.
     * When the query matches a word up to capitalization, you should return the first such match in the wordlist.
     * When the query matches a word up to vowel errors, you should return the first such match in the wordlist.
     * If the query has no matches in the wordlist, you should return the empty string.
     * <p>
     * Given some queries, return a list of words answer, where answer[i] is the correct word for query = queries[i].
     * <p>
     * Constraints:
     * <p>
     * 1 <= wordlist.length, queries.length <= 5000
     * 1 <= wordlist[i].length, queries[i].length <= 7
     * wordlist[i] and queries[i] consist only of only English letters.
     * <p>
     * Approach: Hash Table
     * Basically, we can translate the spell checker rules as follows:
     * 1. Put all the original words into a set, if there is an exact match, return it
     * 2. Convert input word into lowercase, and add a mapping between the lowercase word to the original word. Use
     * putIfAbsent() API we guarantee the first such word will be returned
     * 3. After converting the input word into all lowercase, add a mapping between the word with vowels masked out and
     * the original word. For example, "Kite" -> "k*t*". For the query word, we will do the same processing, and if there
     * is a match, return it. Still putIfAbsent() guarantees the first such word will be returned
     * <p>
     * Time: O(L * (M + N)) = O(M + N) where M is the size of wordlist, and N is the size of query array. L is the average
     * length of all words. For each word in the wordlist, we need to go through the entire word to change to lowercase, mask
     * out vowels. So it would take O(L * M), similar runtime applies to the query, which is O(L * N). However, since the
     * input string length is at most 7, it will be constant in practice. O(M + N) in total.
     * Space: O(M)
     */
    private Set<Character> vowelSet;
    private Set<String> exactMatch;
    private Map<String, String> capitalization, changeVowels;

    public String[] spellchecker(String[] wordlist, String[] queries) {
        vowelSet = Set.of('a', 'e', 'i', 'o', 'u');
        exactMatch = new HashSet<>();
        capitalization = new HashMap<>();
        changeVowels = new HashMap<>();

        // preprocess each word to add the desired mapping
        for (String word : wordlist) {
            preprocess(word);
        }

        int n = queries.length;
        String[] res = new String[n];
        for (int i = 0; i < n; i++) {
            res[i] = findMatch(queries[i]);
        }
        return res;
    }

    private String findMatch(String query) {
        // if an exact match is found - return it
        if (exactMatch.contains(query)) {
            return query;
        }

        // change the query to lowercase
        String lowercaseQuery = query.toLowerCase();
        // try to find a match in the map
        if (capitalization.containsKey(lowercaseQuery)) {
            return capitalization.get(lowercaseQuery);
        }

        // if a match hasn't been found so far - mask out vowels
        String queryWithVowelsReplaced = maskVowels(lowercaseQuery);
        // find a match
        if (changeVowels.containsKey(queryWithVowelsReplaced)) {
            return changeVowels.get(queryWithVowelsReplaced);
        }

        // if a match cannot be found after checking all three rules - return an empty string
        return "";
    }

    private void preprocess(String word) {
        // add word as is for exact match
        exactMatch.add(word);

        // change the word to lowercase
        String lowercaseWord = word.toLowerCase();
        // use putIfAbsent to make sure only the first one is stored
        capitalization.putIfAbsent(lowercaseWord, word);

        // mask vowels out in the lowercase word
        String wordWithVowelsMasked = maskVowels(lowercaseWord);
        // add the mapping
        changeVowels.putIfAbsent(wordWithVowelsMasked, word);
    }

    private String maskVowels(String word) {
        StringBuilder newWord = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char curr = word.charAt(i);
            // mask out vowels by replacing any vowels with '*'
            newWord.append(vowelSet.contains(curr) ? '*' : curr);
        }
        return newWord.toString();
    }

    @Test
    public void spellcheckerTest() {
        /**
         * Example 1:
         * Input: wordlist = ["KiTe","kite","hare","Hare"],
         * queries = ["kite","Kite","KiTe","Hare","HARE","Hear","hear","keti","keet","keto"]
         * Output: ["kite","KiTe","KiTe","Hare","hare","","","KiTe","","KiTe"]
         */
        String[] wordlist1 = new String[]{"KiTe", "kite", "hare", "Hare"};
        String[] queries1 = new String[]{"kite", "Kite", "KiTe", "Hare", "HARE", "Hear", "hear", "keti", "keet", "keto"};
        String[] actual1 = spellchecker(wordlist1, queries1);
        String[] expected1 = new String[]{"kite", "KiTe", "KiTe", "Hare", "hare", "", "", "KiTe", "", "KiTe"};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: wordlist = ["yellow"], queries = ["YellOw"]
         * Output: ["yellow"]
         */
        String[] wordlist2 = new String[]{"yellow"};
        String[] queries2 = new String[]{"YellOw"};
        String[] actual2 = spellchecker(wordlist2, queries2);
        String[] expected2 = new String[]{"yellow"};
        assertArrayEquals(expected2, actual2);
    }
}
