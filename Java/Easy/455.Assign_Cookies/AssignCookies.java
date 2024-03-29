import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class AssignCookies {

    /**
     * Assume you are an awesome parent and want to give your children some cookies. But, you should give each child
     * at most one cookie.
     * <p>
     * Each child i has a greed factor g[i], which is the minimum size of a cookie that the child will be content with;
     * and each cookie j has a size s[j]. If s[j] >= g[i], we can assign the cookie j to the child i, and the child i
     * will be content. Your goal is to maximize the number of your content children and output the maximum number.
     * <p>
     * Approach: Greedy
     * Sort both the child and cookie array, try to make the children with less greedy factor happy, which will maximum
     * the total number of happy children.
     * <p>
     * Time: O(max(SlogS, GlogG)) The sorting algorithm will dominate the time performance
     * Space: O(1)
     */
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int child = 0;
        for (int cookie = 0; cookie < s.length && child < g.length; cookie++) {
            if (s[cookie] >= g[child]) {
                child++;
            }
        }
        return child;
    }

    @Test
    public void findContentChildrenTest() {
        /**
         * Example 1:
         * Input: g = [1,2,3], s = [1,1]
         * Output: 1
         * Explanation: You have 3 children and 2 cookies. The greed factors of 3 children are 1, 2, 3.
         * And even though you have 2 cookies, since their size is both 1, you could only make the child whose
         * greed factor is 1 content.
         * You need to output 1.
         */
        assertEquals(1, findContentChildren(new int[]{1, 2, 3}, new int[]{1, 1}));
        /**
         * Example 2:
         * Input: g = [1,2], s = [1,2,3]
         * Output: 2
         * Explanation: You have 2 children and 3 cookies. The greed factors of 2 children are 1, 2.
         * You have 3 cookies and their sizes are big enough to gratify all of the children,
         * You need to output 2.
         */
        assertEquals(2, findContentChildren(new int[]{1, 2}, new int[]{1, 2, 3}));
    }
}
