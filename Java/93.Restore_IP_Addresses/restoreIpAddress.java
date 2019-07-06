import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class restoreIpAddress {

    /**
     * Given a string containing only digits, restore it by returning all possible valid IP address combinations.
     * (xxx.xx.xxx.xx) each xxx is a number within 0 to 255 (exclusive) without leading zeros
     *
     * If the length of string is less than 4 or larger than 12, there is no way to construct valid IP addresses, simply return an empty
     * list. For a valid length string, we will focus on where to put the dot. Actually, for a given position (xxx), we only have 3 choices
     * to put the first dot. Same explanation holds for the rest of two dots. Hence, we at most need to check 3x3x3 = 27 combinations.
     *
     * Then, we can use a backtrack algorithm. If the current stage cannot form a valid solution, we backtrack to the previous stage and
     * search again.
     *
     * Time: O(27) = O(1), as discussed before, we have at most 27 combinations to check
     * Space: O(19) = O(1) the actual number of final results depends on the input, the maximum will be 19 when input length is 8.
     */
    public List<String> restoreIpAddressesBacktrack1(String s) {
        List<String> res = new ArrayList<>();
        if(s == null || s.length() < 4 || s.length() > 12) return res;
        List<String> aList = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        backTrack(res, s, aList, current, 0);
        return res;
    }

    private void backTrack(List<String> res, String s, List<String> aList, StringBuilder current, int start) {
        if(aList.size() == 4) { //when we have 4 candidate numbers in the list, we can potentially form an IP address
            String address = String.join(".", aList);
            if(address.length() == s.length() + 3) { //a valid IP address should use all the numbers and contain 3 dots. Hence the length
                res.add(address);                    //should be s.length() + 3
            }
            return;
        }
        for(int i = start; i < s.length(); i++) {
            for(int j = i; j < Math.min(i + 3, s.length()); j++) { //need to check index boundary
                current.append(s.charAt(j));    //add a new number
                if(!valid(current)) return;     //check whether the current number is valid, if not, discard current stage
                else {
                    aList.add(current.toString());  //add current number to the list
                    current = new StringBuilder();  //reassign the current number to nothing
                    backTrack(res, s, aList, current, j + 1);   //recursively search further
                    current = new StringBuilder(aList.remove(aList.size() - 1)); //if invalid, backtrack to previous stage, remove
                    //the last number, and assign it to current for further searching.
                }
            }
        }
    }

    private boolean valid(StringBuilder curr) {
        if(curr.charAt(0) == '0') return curr.length() == 1; //"022" is not a valid number
        else return Integer.valueOf(curr.toString()) <= 255;
    }

    /**
     * A more concise backtrack approach
     */
    public List<String> restoreIpAddressesBacktrack2(String s) {
        List<String> res = new ArrayList<>();
        if(s == null || s.length() < 4 || s.length() > 12) return res;
        List<String> current = new ArrayList<>();
        backTrack2(res, 0, 0, s, current);
        return res;
    }

    private void backTrack2(List<String> res, int segement, int start, String s, List<String> curr) {
        if(segement == 4) {
            if(start == s.length()) res.add(String.join(".", curr));
        }
        for(int len = 1; len < 4 && start + len <= s.length(); len++) {
            String substr = s.substring(start, start + len);
            if(valid(substr)) {
                curr.add(substr);
                backTrack2(res, segement + 1, start + len, s, curr);
                curr.remove(curr.size() - 1);
            }
        }
    }

    private boolean valid(String curr) {
        if(curr.charAt(0) == '0') return curr.length() == 1; //"022" is not a valid number
        else return Integer.valueOf(curr) <= 255;
    }


    /**
     * Approach 2: Three loops
     * Since the result set is limited, we can actually use three different loops to focus on its own segment. In each segment,
     * we will only consider substrings with length 1, 2, 3. If every substring meets validation condition, we finally obtain
     * a valid IP address.
     *
     * Time: O(27) = O(1), as discussed before, we have at most 27 combinations to check
     * Space: O(19) = O(1) the actual number of final results depends on the input, the maximum will be 19 when input length is 8.
     */
    public List<String> restoreIpAddressesThreeLoops(String s) {
        List<String> res = new ArrayList<>();
        int length = s.length();
        if(length < 4 || length > 12) return res;
        for(int a = 1; a < 4 && a < length; a++) { //first loop, check first substring only
            String as = s.substring(0, a);
            if(!valid(as)) continue;    //if current substring is invalid, skip to next stage, since no way to construct IP address from here

            for(int b = a + 1; b < a + 4 && b < length; b++) { //second loop, check second substring only
                String bs = s.substring(a, b);
                if(!valid(bs)) continue;

                for(int c = b + 1; c < b + 4 && c < length; c++) {  //third loop, check third and forth substring.
                    //no need for another loop, since after we consider the third substring, the rest is just the final substring
                    String cs = s.substring(b, c);
                    if(!valid(cs)) continue;

                    String ds = s.substring(c);
                    if(!valid(ds)) continue;

                    //If we have four valid substrings, we can construct a valid IP address as well
                    //Instead of using String.join(), we simply concatenate these substrings for the sake of runtime simplicity
                    res.add(as + "." + bs + "." + cs + "." + ds);
                }
            }
        }
        return res;
    }

    @Test
    public void restoreIpAddressesBacktrack1Test() {
        /**
         * Example 1:
         * Input: "25525511135"
         * Output: ["255.255.11.135", "255.255.111.35"]
         */
        String s1 = "25525511135";
        Set<String> expected1 = new HashSet<>();
        expected1.add("255.255.11.135");
        expected1.add("255.255.111.35");
        Set<String> actual1 = new HashSet<>(restoreIpAddressesBacktrack1(s1));
        assertEquals(expected1.size(), actual1.size());
        for(String s : expected1) {
            assertTrue(actual1.contains(s));
        }
        /**
         * Input: "010010"
         * Output: ["0.10.0.10","0.100.1.0"]
         */
        String s2 = "010010";
        Set<String> expected2 = new HashSet<>();
        expected2.add("0.10.0.10");
        expected2.add("0.100.1.0");
        Set<String> actual2 = new HashSet<>(restoreIpAddressesBacktrack1(s2));
        assertEquals(expected2.size(), actual2.size());
        for(String s : expected2) {
            assertTrue(actual2.contains(s));
        }
    }


    @Test
    public void restoreIpAddressesBacktrack2Test() {
        /**
         * Example 1:
         * Input: "25525511135"
         * Output: ["255.255.11.135", "255.255.111.35"]
         */
        String s1 = "25525511135";
        Set<String> expected1 = new HashSet<>();
        expected1.add("255.255.11.135");
        expected1.add("255.255.111.35");
        Set<String> actual1 = new HashSet<>(restoreIpAddressesBacktrack2(s1));
        assertEquals(expected1.size(), actual1.size());
        for(String s : expected1) {
            assertTrue(actual1.contains(s));
        }
        /**
         * Input: "010010"
         * Output: ["0.10.0.10","0.100.1.0"]
         */
        String s2 = "010010";
        Set<String> expected2 = new HashSet<>();
        expected2.add("0.10.0.10");
        expected2.add("0.100.1.0");
        Set<String> actual2 = new HashSet<>(restoreIpAddressesBacktrack2(s2));
        assertEquals(expected2.size(), actual2.size());
        for(String s : expected2) {
            assertTrue(actual2.contains(s));
        }
    }

    @Test
    public void restoreIpAddressesThreeLoopsTest() {
        /**
         * Example 1:
         * Input: "25525511135"
         * Output: ["255.255.11.135", "255.255.111.35"]
         */
        String s1 = "25525511135";
        Set<String> expected1 = new HashSet<>();
        expected1.add("255.255.11.135");
        expected1.add("255.255.111.35");
        Set<String> actual1 = new HashSet<>(restoreIpAddressesThreeLoops(s1));
        assertEquals(expected1.size(), actual1.size());
        for(String s : expected1) {
            assertTrue(actual1.contains(s));
        }
        /**
         * Input: "010010"
         * Output: ["0.10.0.10","0.100.1.0"]
         */
        String s2 = "010010";
        Set<String> expected2 = new HashSet<>();
        expected2.add("0.10.0.10");
        expected2.add("0.100.1.0");
        Set<String> actual2 = new HashSet<>(restoreIpAddressesThreeLoops(s2));
        assertEquals(expected2.size(), actual2.size());
        for(String s : expected2) {
            assertTrue(actual2.contains(s));
        }
    }
}
