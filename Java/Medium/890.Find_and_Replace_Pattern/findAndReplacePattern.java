import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class findAndReplacePattern {

    /**
     * You have a list of words and a pattern, and you want to know which words in words matches the pattern.
     *
     * A word matches the pattern if there exists a permutation of letters p so that after replacing every letter x in the pattern with
     * p(x), we get the desired word.
     *
     * (Recall that a permutation of letters is a bijection from letters to letters: every letter maps to another letter, and no two
     * letters map to the same letter.)
     *
     * Return a list of the words in words that match the given pattern.
     * You may return the answer in any order.
     *
     * Note:
     *
     * 1 <= words.length <= 50
     * 1 <= pattern.length = words[i].length <= 20
     *
     * Approach 1: Two Hash Maps
     * 此题和205题isomorphic string一样，即判断输入的所有字符串与pattern是否为isomorphic，本质上需要建立一个双向映射，从word的每一个字符映射向pattern的
     * 每一个字符，因为每个字符只能有一个映射，所以若某字符的映射已被记录，且与当前的映射不相同，说明该word和pattern不是isomorphic
     *
     * Time: O(N * K), N是字符串个数，K是pattern长度，最坏情况下，对于每个字符串都需要遍历到最后一个字符。
     * Space: O(K) 对于每一个字符串，需要两个map，建立字符之间的映射，最坏情况下，每个字符都是unique的，且需要遍历至最后，因此需要将K个字符都放入map中
     */
    public List<String> findAndReplacePatternTwoMaps(String[] words, String pattern) {
        List<String> res = new ArrayList<>();
        for (String word : words) {
            if (isMatchTwoMaps(word, pattern)) {
                res.add(word);
            }
        }
        return res;
    }

    private boolean isMatchTwoMaps(String word, String pattern) {
        Map<Character, Character> map1 = new HashMap<>();
        Map<Character, Character> map2 = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            char c1 = word.charAt(i);
            char c2 = pattern.charAt(i);
            //若当前不存在不存在，将双向映射加入两个map中
            if (!map1.containsKey(c1)) {
                map1.put(c1, c2);
            }
            if (!map2.containsKey(c2)) {
                map2.put(c2, c1);
            }
            //若之前所记录的字符映射，与当前映射不符，说明不匹配，返回false
            if (map1.get(c1) != c2 || map2.get(c2) != c1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Approach 2: One Map
     * 构建映射其实不需要两个map，根据题意，直到同一个字符只会有一个映射存在，因此可以只记录word到pattern的映射，同时记录pattern中出现过的字符。两种情况下，
     * 不满足唯一映射的条件：
     * 1. map未记录word中某字符的映射，但其映射对象（pattern中字符）已被记录，所以word中有两个字符映射到pattern中同一个字符，返回false
     * 2. map中记录了word某字符的映射，但其映射与当前映射不符，意味着pattern中有两个字符映射到word中同一字符，返回false
     *
     * Time: O(N * K)
     * Space: O(K)
     */
    public List<String> findAndReplacePatternOneMaps(String[] words, String pattern) {
        List<String> res = new ArrayList<>();
        for (String word : words) {
            if (isMatchOneMap(word, pattern)) {
                res.add(word);
            }
        }
        return res;
    }

    private boolean isMatchOneMap(String word, String pattern) {
        Map<Character, Character> map = new HashMap<>();
        Set<Character> seen = new HashSet<>();
        for (int i = 0; i < word.length(); i++) {
            char c1 = word.charAt(i);
            char c2 = pattern.charAt(i);
            if ((!map.containsKey(c1) && seen.contains(c2)) || (map.containsKey(c1) && map.get(c1) != c2)) {
                return false;
            }
            map.put(c1, c2);
            seen.add(c2);
        }
        return true;
    }

    @Test
    public void findAndReplacePatternTest() {
        /**
         * Example:
         * Input: words = ["abc","deq","mee","aqq","dkd","ccc"], pattern = "abb"
         * Output: ["mee","aqq"]
         * Explanation: "mee" matches the pattern because there is a permutation {a -> m, b -> e, ...}.
         * "ccc" does not match the pattern because {a -> c, b -> c, ...} is not a permutation,
         * since a and b map to the same letter.
         */
        String[] words = new String[]{"abc", "deq", "mee", "aqq", "dkd", "ccc"};
        Set<String> actual1 = new HashSet<>(findAndReplacePatternTwoMaps(words, "abb"));
        Set<String> expected = new HashSet<>(Arrays.asList("mee", "aqq"));
        assertEquals(expected.size(), actual1.size());
        for(String s : expected) {
            assertTrue(actual1.contains(s));
        }
        Set<String> actual2 = new HashSet<>(findAndReplacePatternOneMaps(words, "abb"));
        assertEquals(expected.size(), actual2.size());
        for(String s : expected) {
            assertTrue(actual2.contains(s));
        }
    }
}
