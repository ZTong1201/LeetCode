import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class groupAnagrams {

    /**
     * Given an array of strings, group anagrams together.
     *
     * Note:
     *
     * All inputs will be in lowercase.
     * The order of your output does not matter.
     *
     * Example:
     * Input: ["eat", "tea", "tan", "ate", "nat", "bat"],
     * Output:
     * [
     *   ["ate","eat","tea"],
     *   ["nat","tan"],
     *   ["bat"]
     * ]
     *
     * Approach 1: Sorting each string
     * 如果两个字符串是anagram，则他们sort之后的结果必为相同字符串。因此可以构建一个map，以sort之后的string作为key，对应的list of anagram作为value
     * 如果一个新的字符串sort之后在map中出现，就把该字符串加在对应的list里。否则构建一对新的pair。
     * 需要注意的是，string本身不可以被排序，需要将string转换成char array，排序后利用String.valueOf()将其还原回string即可。
     * 因为map中的value已经是list了，可以最后直接返回new ArrayList<>(map.values())即可
     *
     * Time: O(NLlogL), N是字符串的数量，L是最大字符串的长度，sort字符串需要O(LlogL)时间，同时要对N个字符串都做sort
     * Space: O(NL), 将所有字符串都储存在了map里
     */
    public List<List<String>> groupAnagramsSorting(String[] strs) {
        if(strs.length == 0) return new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        for(String s : strs) {
            char[] c = s.toCharArray();
            Arrays.sort(c);
            String key = String.valueOf(c);
            if(!map.containsKey(key)) map.put(key, new ArrayList<>());
            map.get(key).add(s);
        }
        return new ArrayList<>(map.values());
    }


    /**
     * Approach 2: Count Characters
     * 如果两个字符串为anagram，那么他们对应的字符计数应该完全相同。因为输入字符只有小写字母，所以可以用一个size为26的int数组来记录每个字符出现的次数。因为
     * map中只会存储数组的地址，因此无法直接判断map中是否包含某数组，可以考虑将整个数组变成一个string。例如"abb"的数组为[1, 2, 0, ... 0]，可以利用stringbuilder
     * 将其变为"120...0"，将这个string作为key映射到anagram的list上即可。无需排序
     *
     * Time: O(NL), 对于每个字符串都要遍历整个字符串记录字符次数，因此为O(NL)
     * Space: O(NL), 与排序算法类似，还是讲所有字符串记录进一个map
     */
    public List<List<String>> groupAnagramsCounting(String[] strs) {
        if(strs.length == 0) return new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        for(String s : strs) {
            int[] count = new int[26];
            for(int i = 0; i < s.length(); i++) {
                count[s.charAt(i) - 'a'] += 1;
            }
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < 26; i++) {
                sb.append(count[i]);
            }
            String key = sb.toString();
            if(!map.containsKey(key)) map.put(key, new ArrayList<>());
            map.get(key).add(s);
        }
        return new ArrayList<>(map.values());
    }

}
