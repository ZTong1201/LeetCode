import org.junit.Test;
import static org.junit.Assert.*;

public class longestCommonPrefix {

    /**
     * Write a function to find the longest common prefix string amongst an array of strings.
     *
     * If there is no common prefix, return an empty string "".
     *
     * Approach 1: Horizontal Scanning
     * The simplest way is to set the first string as the candidate prefix, and search in the rest of array. If any string
     * is not start with this prefix, we trim one letter at the end of the previous the candidate prefix. If the prefix becomes empty
     * during searching, we directly return an empty string. Otherwise, we return the candidate prefix we find.
     *
     * Time: O(S), where S is the total length of n input strings. Since we need to search for every letter in every string to find
     *       the prefix;
     * Space: O(1), only requires a constant space to store prefix to be returned
     */
    public String longestCommonPrefixHorizontalScan(String[] strs) {
        int length = strs.length;
        if(length == 0) return "";
        String prefix = strs[0];    //the initial candidate prefix
        for(int i = 1; i < length; i++) {  //search the rest of strings
            while(!strs[i].startsWith(prefix)) {  //if any string is not start with current prefix, can also check strs[i].indexOf(prefix) != 0
                //we remove one letter at the end as the new prefix
                prefix = prefix.substring(0, prefix.length() - 1);
                if(prefix.isEmpty()) return "";  //if at any time, the prefix contains nothing, return an empty string
            }
        }
        return prefix;
    }

    /**
     * Approach 2: Vertical Scanning
     * If there is a very short string at the end of the array, the horizontal scanning could be slow since we need to search
     * for the entire array for each prefix as we reduce the length of candidate prefix. Therefore, we can use a vertical scanning by
     * comparing the character at each index for every string. If at any time, we find a mismatch, we stop searching
     *
     * Time: O(n*minLen), where n is the length of the array and minLen is the minimum length of input strings. However, in the worst case,
     *       if we have n strings with the same length, say m, the time complexity would be O(n*m) = O(S) as in the approach 1
     * Space: O(1) no extra space required
     */
    public String longestCommonPrefixVerticalScan(String[] strs) {
        int length = strs.length;
        if(length == 0) return "";
        for(int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);    //search for one character at a time
            for(int j = 1; j < length; j++) {
                //if we reach the minimum length of all the input strings
                //or we find a mismatch of character, we are done searching
                if(i == strs[j].length() || strs[j].charAt(i) != c) {
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }

    /**
     * Approach 3: Divide and Conquer
     * We can use divide and conquer to solve this problem as well. We keep dividing the input array into subarrays. Find common
     * prefix between two strings and merge them together and update the prefix until we merge back to the entire array.
     *
     * Time: O(mlogN), where m is the minimum length of all the input arrays, N is the length of array. When merging the result, we always
     *       need O(m) time, and using divide and conquer, the recursion tree is of height logN.
     * Space: O(logN), the implicit call stack requires O(logN), which equals to the height of recursion tree
     */
    public String longestCommonPrefixDivideAndConquer(String[] strs) {
        int length = strs.length;
        if(length == 0) return "";
        return helper(strs, 0, length - 1);
    }

    private String helper(String[] strs, int left, int right) {
        if(left == right) return strs[left];
        int mid = left + (right - left) / 2;
        String lcpLeft = helper(strs, left, mid);
        String lcpRight = helper(strs, mid + 1, right);
        return commonPrefix(lcpLeft, lcpRight);
    }

    private String commonPrefix(String left, String right) {
        int minLen = Math.min(left.length(), right.length());
        for(int i = 0; i < minLen; i++) {
            if(left.charAt(i) != right.charAt(i)) return left.substring(0, i);
        }
        return left.substring(0, minLen);
    }


    /**
     * Approach 4: Binary Search
     * We can also use binary search to find the longest common prefix. The search space is from 0 to minLen of input strings.
     * At each iteration, we will discard a half of problem because we know that there is no correct answer in that half. The iteration
     * rule is
     * 1. If the current left half is a common prefix, we search the right half to find a longer common prefix
     * 2. Otherwise, we search the left half to find a smaller one.
     *
     * Time: O(Slogm), where m is the minimum length of input array, we only do the binary search for the first element between index 0 to
     *       m - 1. The search space is m, since we do binary search, we need approximately logm iterations. For each iteration, we at most
     *       make m * N comparisons, denote m * N = S, we have time complexity O(Slogm)
     * Space: O(1) no extra space required.
     */

    public String longestCommonPrefixBinarySearch(String[] strs) {
        if(strs.length == 0) return "";
        int minLen = Integer.MAX_VALUE;
        for(String s : strs) minLen = Math.min(minLen, s.length());
        int index = findIndex(strs, 0, minLen - 1);
        return strs[0].substring(0, index);
    }

    private int findIndex(String[] strs, int low, int high) {
        while(low <= high) {
            int mid = low + (high - low) / 2;
            if(isCommonPrefix(strs, mid)) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    private boolean isCommonPrefix(String[] strs, int middle) {
        String prefix = strs[0].substring(0, middle + 1);
        for(int i = 1; i < strs.length; i++) {
            if(!strs[i].startsWith(prefix)) return false;
        }
        return true;
    }

    @Test
    public void longestCommonPrefixHorizontalScanTest() {
        /**
         * Example 1:
         * Input: ["flower","flow","flight"]
         * Output: "fl"
         */
        String[] strs1 = new String[]{"flower", "flow", "flight"};
        assertEquals("fl", longestCommonPrefixHorizontalScan(strs1));
        /**
         * Example 2:
         * Input: ["dog","racecar","car"]
         * Output: ""
         * Explanation: There is no common prefix among the input strings.
         */
        String[] strs2 = new String[]{"dog", "racecar", "car"};
        assertEquals("", longestCommonPrefixHorizontalScan(strs2));
    }

    @Test
    public void longestCommonPrefixVerticalScanTest() {
        /**
         * Example 1:
         * Input: ["flower","flow","flight"]
         * Output: "fl"
         */
        String[] strs1 = new String[]{"flower", "flow", "flight"};
        assertEquals("fl", longestCommonPrefixVerticalScan(strs1));
        /**
         * Example 2:
         * Input: ["dog","racecar","car"]
         * Output: ""
         * Explanation: There is no common prefix among the input strings.
         */
        String[] strs2 = new String[]{"dog", "racecar", "car"};
        assertEquals("", longestCommonPrefixVerticalScan(strs2));
    }

    @Test
    public void longestCommonDivideAndConquerTest() {
        /**
         * Example 1:
         * Input: ["flower","flow","flight"]
         * Output: "fl"
         */
        String[] strs1 = new String[]{"flower", "flow", "flight"};
        assertEquals("fl", longestCommonPrefixDivideAndConquer(strs1));
        /**
         * Example 2:
         * Input: ["dog","racecar","car"]
         * Output: ""
         * Explanation: There is no common prefix among the input strings.
         */
        String[] strs2 = new String[]{"dog", "racecar", "car"};
        assertEquals("", longestCommonPrefixDivideAndConquer(strs2));
    }

    @Test
    public void longestCommonBinarySearchTest() {
        /**
         * Example 1:
         * Input: ["flower","flow","flight"]
         * Output: "fl"
         */
        String[] strs1 = new String[]{"flower", "flow", "flight"};
        assertEquals("fl", longestCommonPrefixBinarySearch(strs1));
        /**
         * Example 2:
         * Input: ["dog","racecar","car"]
         * Output: ""
         * Explanation: There is no common prefix among the input strings.
         */
        String[] strs2 = new String[]{"dog", "racecar", "car"};
        assertEquals("", longestCommonPrefixBinarySearch(strs2));
    }
}
