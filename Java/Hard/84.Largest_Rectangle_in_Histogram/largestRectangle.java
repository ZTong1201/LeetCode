import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class largestRectangle {

    /**
     * Given n non-negative integers representing the histogram's bar height where the width of each bar is 1, find the area of largest
     * rectangle in the histogram.
     *
     * Approach 1: Better Brute Force
     * Brute Force解法为，对于O(n^2)的pair，找到给定的范围内最小的高度，用最小高度乘上range的长度，即为当前的面积。对于一个range，寻找最大长度需要O(n)时间，
     * 所以brute force的runtime是O(n^3)。
     *
     * 而对Brute force进行改进就是在不断扩大范围的同时，动态地更新当前的最小高度即可。可以将找寻最小高度的时间降为O(1).
     *
     * Time: O(n^2) 对于O(n^2)个pair，找最小高度为O(1)，所以总的运行时间降为O(n^2)
     * Space: O(1)
     */
    public int largestRectangleAreaBetterBruteForce(int[] heights) {
        int maxArea = 0;
        for(int i = 0; i < heights.length; i++) {
            //将最小高度初始化为MAX_VALUE并动态地更新以当前位置为起点的最小高度
            int minHeight = Integer.MAX_VALUE;
            for(int j = i; j < heights.length; j++) {
                minHeight = Math.min(minHeight, heights[j]);
                maxArea = Math.max(maxArea, minHeight * (j - i + 1));
            }
        }
        return maxArea;
    }

    /**
     * Approach 2: Divide and Conquer
     * 对于某个位置，其所能形成的最大矩形面积为下面三者其一
     * 1.以当前bar为高度，乘上当前range的宽度。
     * 2.当前bar左边的bars所能形成的最大面积（subproblems）
     * 3.当前bar右边的bars所能形成的最大面积（subproblems）
     * 因此可以使用divide and conquer来不断把问题缩小。在当前range内找到最小高度乘上当前range的宽度，得到1的值，然后再递归调用左半边和右半边，直到所考虑
     * 的bar只有一个。
     *
     * Time: O(nlogn) 不断把问题一分为二，树的高度为logn，同时每一层需要找到最小高度，需要O(n)，总共为O(nlogn)。但在worst case，即输入高度已经排好序了，
     *       那么每次划分问题只能划分成1和n - 1，因此树的高度为O(N)，总的运行时间为O(n^2)
     * Space: O(N)， worst case递归深度为O(N)
     */
    public int largestRectangleDivideAndConquer(int[] heights) {
        return calculateArea(heights, 0, heights.length - 1);
    }

    private int calculateArea(int[] heights, int start, int end) {
        //base case，如果所在range没有bar，返回面积0
        if(start > end) {
            return 0;
        }
        //需要循环当前range，找到最小高度对应的index，然后一分为二
        int minIndex = start;
        for(int i = start; i <= end; i++) {
            if(heights[minIndex] > heights[i]) {
                minIndex = i;
            }
        }
        return Math.max(heights[minIndex] * (end - start + 1),
                Math.max(calculateArea(heights, start, minIndex - 1), calculateArea(heights, minIndex + 1, end)));
    }

    /**
     * Approach 3: Using Stack
     * 可以构建一个单调栈，栈中元素记录的是heights中的index，若新的height大于栈顶元素，说明有可能会获得更大的面积，因此持续压栈。当新高度小于栈顶元素时，需要
     * 将前面记录的值进行更新，即不断地将栈顶元素顶出，然后计算，当前两个index的range内所能形成的面积，高度即为挤出的栈顶元素，即heights[stack.top()]，而
     * 宽度为当前index和新栈顶index的差值-1，即i - heights[stack.top() - 1] - 1。持续更新直到遍历完所有高度。
     * 此时若栈中还有元素，则继续出栈，考虑当前高度所能构成的最大面积。此时因已遍历完数组，说明当前栈顶元素之后的所有高度均大于它，因此可以直接把heights的右边界
     * 作为其宽度的右边界。
     * 为保证数组中第一个元素也可以被正确计算，需要在一开始在栈中压入-1，意味着0之前的index
     *
     * Time: O(N) 每个元素之后被压栈和出栈一次
     * Space: O(N) 用stack来记录元素，若worst case，输入数组为升序，则需要将所有元素压栈
     */
    public int largestRectangleAreaStack(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        int maxArea = 0;
        for(int i = 0; i < heights.length; i++) {
            //若遇到小于栈顶元素的index，则需要将之前记录的高度进行结算
            while(stack.peek() != -1 && heights[stack.peek()] >= heights[i]) {
                maxArea = Math.max(maxArea, heights[stack.pop()] * (i - stack.peek() - 1));
            }
            //无论如何，都需要将当前index压栈
            stack.push(i);
        }
        //遍历完整个数组后，若栈中还有元素，则需要继续以数组长度为右边界，计算每个高度能形成的最大面积
        while(stack.peek() != -1) {
            maxArea = Math.max(maxArea, heights[stack.pop()] * (heights.length - stack.peek() - 1));
        }
        return maxArea;
    }


    @Test
    public void largestRectangleAreaTest() {
        /**
         * Example 1:
         * Input: [2,1,5,6,2,3]
         * Output: 10
         */
        int[] heights1 = new int[]{2, 1, 5, 6, 2, 3};
        assertEquals(10, largestRectangleAreaBetterBruteForce(heights1));
        assertEquals(10, largestRectangleDivideAndConquer(heights1));
        /**
         * Example 2:
         * Input: [2,1,5,6,2,3]
         * Output: 10
         */
        int[] heights2 = new int[]{1, 1};
        assertEquals(2, largestRectangleAreaBetterBruteForce(heights2));
        assertEquals(2, largestRectangleDivideAndConquer(heights2));
    }
}
