import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class goatLatin {

    /**
     * A sentence S is given, composed of words separated by spaces. Each word consists of lowercase and uppercase letters only.
     *
     * We would like to convert the sentence to "Goat Latin" (a made-up language similar to Pig Latin.)
     *
     * The rules of Goat Latin are as follows:
     *
     * If a word begins with a vowel (a, e, i, o, or u), append "ma" to the end of the word.
     * For example, the word 'apple' becomes 'applema'.
     *
     * If a word begins with a consonant (i.e. not a vowel), remove the first letter and append it to the end, then add "ma".
     * For example, the word "goat" becomes "oatgma".
     *
     * Add one letter 'a' to the end of each word per its word index in the sentence, starting with 1.
     * For example, the first word gets "a" added to the end, the second word gets "aa" added to the end and so on.
     * Return the final sentence representing the conversion from S to Goat Latin.
     *
     * Notes:
     *
     * S contains only uppercase, lowercase and spaces. Exactly one space between each word.
     * 1 <= S.length <= 150.
     *
     * Time: O(N) where N is the length of String, we need to traverse every word and manipulate it
     * Space: O(N^2) since we need to add extra "a"s at the end of each word
     */
    public String toGoatLatin(String S) {
        String[] words = S.split(" ");
        Set<Character> vowels = new HashSet<>();
        addVowels(vowels);
        StringBuilder res = new StringBuilder();
        //use a string builder to contain a's, and increment one more a for each loop
        StringBuilder as = new StringBuilder("a");
        for(int i = 0; i < words.length; i++) {
            char first = words[i].charAt(0);
            if(vowels.contains(first)) {
                res.append(words[i]);
                res.append("ma");
                res.append(as);
            } else {
                res.append(words[i].substring(1));
                res.append(first);
                res.append("ma");
                res.append(as);
            }
            as.append("a");
            res.append(" ");
        }
        //we have one more space, use substring to cut off the last element
        return res.toString().substring(0, res.length() - 1);
    }

    private void addVowels(Set<Character> vowels) {
        for(char c : new char[]{'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'}) {
            vowels.add(c);
        }
    }

    @Test
    public void toGoatLatinTest() {
        /**
         * Example 1:
         * Input: "I speak Goat Latin"
         * Output: "Imaa peaksmaaa oatGmaaaa atinLmaaaaa"
         */
        String actual1 = toGoatLatin("I speak Goat Latin");
        String expected1 = "Imaa peaksmaaa oatGmaaaa atinLmaaaaa";
        assertEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: "The quick brown fox jumped over the lazy dog"
         * Output: "heTmaa uickqmaaa rownbmaaaa oxfmaaaaa umpedjmaaaaaa overmaaaaaaa hetmaaaaaaaa azylmaaaaaaaaa ogdmaaaaaaaaaa"
         */
        String actual2 = toGoatLatin("The quick brown fox jumped over the lazy dog");
        String expected2 = "heTmaa uickqmaaa rownbmaaaa oxfmaaaaa umpedjmaaaaaa overmaaaaaaa hetmaaaaaaaa azylmaaaaaaaaa ogdmaaaaaaaaaa";
        assertEquals(expected2, actual2);
    }
}
