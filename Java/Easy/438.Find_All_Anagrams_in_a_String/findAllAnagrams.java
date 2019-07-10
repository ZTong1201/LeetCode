import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class findAllAnagrams {

    /**
     * Given a string s and a non-empty string p, find all the start indices of p's anagrams in s.
     *
     * Strings consists of lowercase English letters only and the length of both strings s and p will not be larger than 20,100.
     *
     * The order of output does not matter.
     *
     * Approach: HashMap + Sliding Window
     * The easiest way to check whether two strings are anagrams, we can use a hash map. Since strings consist of lowercase English
     * letter only, we could use an integer array as a hash map.
     *
     * In order to accelerate the searching process, we could use a sliding window. If any value int the map is less than 0, which means
     * we have run out of this letter, hence there is no way to form an anagram from current starting point. We could close the window
     * by incrementing the starting point, and add letters back. If all letters are used and the size of current window equals to the
     * length of String p, we add the starting point of window to the output list. When the window slides to the end of the String, we're done
     *
     * Time: O(2N) in the worst case since each letter might be visited twice by the start and the end pointer of the window
     * Space: O(1), we need a constant size of integer array as the hash map
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        int sLength = s.length(), pLength = p.length();
        int[] map = new int[26];
        for(int i = 0; i < pLength; i++) map[p.charAt(i) - 'a'] += 1;

        int start = 0;    //the starting point of current window
        for(int end = 0; end < sLength; end++) { //we increment the end point of the window until reaching the end of the string
            char endChar = s.charAt(end);    //character at the end of the current window
            map[endChar - 'a'] -= 1;         //when we used one letter, we decrement the count in the hash map

            while(map[endChar - 'a'] < 0) {   //if we have used up any letter, we narrow the window from the start
                char startChar = s.charAt(start);
                map[startChar - 'a'] += 1;
                start += 1;
            }

            if(end - start + 1 == pLength) {  //if at any time, the size of current window equals to the length of string p, we find an anagram
                //add the starting point to the output list
                res.add(start);
                char startChar = s.charAt(start);
                //add the letter back to the hash map
                map[startChar - 'a'] += 1;
                start += 1;
            }
        }
        return res;
    }


    @Test
    public void findAnagramsTest() {
        /**
         * Example 1:
         * Input:
         * s: "cbaebabacd" p: "abc"
         *
         * Output:
         * [0, 6]
         *
         * Explanation:
         * The substring with start index = 0 is "cba", which is an anagram of "abc".
         * The substring with start index = 6 is "bac", which is an anagram of "abc".
         */
        String s1 = "cbaebabacd";
        String p1 = "abc";
        List<Integer> list1 = findAnagrams(s1, p1);
        int[] expected1 = new int[]{0, 6};
        int[] actual1 = listToArray(list1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input:
         * s: "abab" p: "ab"
         *
         * Output:
         * [0, 1, 2]
         *
         * Explanation:
         * The substring with start index = 0 is "ab", which is an anagram of "ab".
         * The substring with start index = 1 is "ba", which is an anagram of "ab".
         * The substring with start index = 2 is "ab", which is an anagram of "ab".
         */
        String s2 = "abab";
        String p2 = "ab";
        List<Integer> list2 = findAnagrams(s2, p2);
        int[] expected2 = new int[]{0, 1, 2};
        int[] actual2 = listToArray(list2);
        assertArrayEquals(expected2, actual2);
    }

    private int[] listToArray(List<Integer> aList) {
        int[] res = new int[aList.size()];
        for(int i = 0; i < res.length; i++) {
            res[i] = aList.get(i);
        }
        return res;
    }
}
