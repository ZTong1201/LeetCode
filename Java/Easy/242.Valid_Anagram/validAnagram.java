import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class validAnagram {

    /**
     * Given two strings s and t , write a function to determine if t is an anagram of s.
     *
     * Note:
     * You may assume the string contains only lowercase alphabets.
     *
     * Approach 1: hash table
     * Since we may assume thee string contains only lowercase alphabets, we can simply use an integer array as our hash table.
     * We iterate over the first string to record the counts of every characters. Then we decrement the count when we traverse the second
     * string. If at any time, the count is less than 0, which means we found a mismatch between two strings.
     *
     * Time: O(n), since we iterate over the string separately, each cost O(n) time.
     * Space: O(1), even if we assign extra space to hash table, it is a constant size O(26)
     *
     * Follow up: What if the inputs contain unicode characters? How would you adapt your solution to such case?
     *
     * If the string contains unicode characters, we can build a true hash map to store the count of each character.
     * In this case, the space complexity could be O(n), where n is the number of unique characters in the string.
     */
    public boolean isAnagramHashTable(String s, String t) {
        if(s.length() != t.length()) return false;
        int[] letters = new int[26];
        for(char c : s.toCharArray()) {
            letters[c - 'a'] += 1;
        }
        for(char c : t.toCharArray()) {
            letters[c - 'a'] -= 1;
            if(letters[c - 'a'] < 0) return false;
        }
        return true;
    }

    /**
     * Approach 2: Sorting
     * We can convert both strings into a char array and sort them, after sorting we check whether they are equal.
     * If two strings are truly anagrams, their corresponding sorted char arrays should be exactly the same.
     *
     * Time: O(NlogN) for typical sorting algorithm
     * Space: O(n):, we convert the strings to char arrays
     */
    public boolean isAnagramSorting(String s, String t) {
        char[] str1 = s.toCharArray();
        char[] str2 = t.toCharArray();
        Arrays.sort(str1);
        Arrays.sort(str2);
        return Arrays.equals(str1, str2);
    }


    @Test
    public void isAnagramHashTableTest() {
        /**
         * Example 1:
         * Input: s = "anagram", t = "nagaram"
         * Output: true
         */
        String s1 = "anagram";
        String t1 = "nagaram";
        assertTrue(isAnagramHashTable(s1, t1));
        /**
         * Example 1:
         * Input: s = "rat", t = "car"
         * Output: false
         */
        String s2 = "rat";
        String t2 = "car";
        assertFalse(isAnagramHashTable(s2, t2));
    }

    @Test
    public void isAnagramSortingTest() {
        /**
         * Example 1:
         * Input: s = "anagram", t = "nagaram"
         * Output: true
         */
        String s1 = "anagram";
        String t1 = "nagaram";
        assertTrue(isAnagramSorting(s1, t1));
        /**
         * Example 1:
         * Input: s = "rat", t = "car"
         * Output: false
         */
        String s2 = "rat";
        String t2 = "car";
        assertFalse(isAnagramSorting(s2, t2));
    }
}
