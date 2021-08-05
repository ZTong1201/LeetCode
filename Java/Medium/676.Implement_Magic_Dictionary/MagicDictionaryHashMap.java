import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicDictionaryHashMap {

    /**
     * Approach 2: Hash Map
     * We can create a hash map between the length of word and a list of words. For a given search word, we can get the list
     * of words with the same length and count the number of mismatches at the same index. If the count is not equal to 1 for
     * every candidate word, then return false.
     * <p>
     * Time: O(N * L) in the worst case, all the words (search word and the words in the dictionary) have the same length,
     * and we need cross-check all N candidates
     * Space: O(N)
     */
    private final Map<Integer, List<String>> map;

    public MagicDictionaryHashMap() {
        map = new HashMap<>();
    }

    public void buildDict(String[] dictionary) {
        for (String word : dictionary) {
            int length = word.length();
            map.putIfAbsent(length, new ArrayList<>());
            map.get(length).add(word);
        }
    }

    public boolean search(String searchWord) {
        int length = searchWord.length();
        // return false if there is no candidate to be searched
        if (!map.containsKey(length)) return false;
        // search all candidates
        for (String word : map.get(length)) {
            // count the number of mismatches
            int count = 0;
            for (int i = 0; i < length; i++) {
                if (word.charAt(i) != searchWord.charAt(i)) count++;
                // early termination if count > 1
                if (count > 1) break;
            }
            // return true if there is only one mismatch
            if (count == 1) return true;
        }
        return false;
    }
}
