import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class mostCommonWord {

    /**
     * Given a paragraph and a list of banned words, return the most frequent word that is not in the list of banned words.
     * It is guaranteed there is at least one word that isn't banned, and that the answer is unique.
     *
     * Words in the list of banned words are given in lowercase, and free of punctuation.
     * Words in the paragraph are not case sensitive.  The answer is in lowercase.
     *
     * Note:
     *
     * 1 <= paragraph.length <= 1000.
     * 0 <= banned.length <= 100.
     * 1 <= banned[i].length <= 10.
     * The answer is unique, and written in lowercase (even if its occurrences in paragraph may have uppercase symbols,
     * and even if it is a proper noun.)
     *
     * paragraph only consists of letters, spaces, or the punctuation symbols !?',;.
     *
     * There are no hyphens or hyphenated words.
     *
     * Words only consist of letters, never apostrophes or other punctuation symbols.
     *
     * Time: O(N), where N is the length of paragraph string
     * Space: O(N)
     */
    public String mostCommonWord(String paragraph, String[] banned) {
        String[] words = paragraph.replaceAll("\\p{Punct}", "").toLowerCase().split(" ");
        Set<String> bannedSet = new HashSet<>();
        Map<String, Integer> wordCount = new HashMap<>();
        for(String bannedWord : banned) bannedSet.add(bannedWord);

        int maxCount = 0;
        String res = "";
        for(String word : words) {
            if(!word.equals("") && !bannedSet.contains(word)) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                if(wordCount.get(word) > maxCount) {
                    maxCount = wordCount.get(word);
                    res = word;
                }
            }
        }
        return res;
    }

    /**
     * Input:
     * paragraph = "Bob hit a ball, the hit BALL flew far after it was hit."
     * banned = ["hit"]
     * Output: "ball"
     */
    @Test
    public void mostCommonWordTest() {
        String paragraph1 = "Bob hit a ball, the hit BALL flew far after it was hit.";
        String[] banned1 = new String[]{"hit"};
        assertEquals("ball", mostCommonWord(paragraph1,  banned1));
        String paragraph2 = "Bob, hIt. baLl";
        String[] banned2 = new String[]{"hit", "bob"};
        assertEquals("ball", mostCommonWord(paragraph2,  banned2));
    }
}
