import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class trapRain {

    /**
     * Given n non-negative integers representing an elevation map where the width of each bar is 1,
     * compute how much water it is able to trap after raining.
     * <p>
     * Approach 1: Brute Force
     * <p>
     * Just do as the problem state. For a given element, if it can trap some water at current location. It must be the smallest height
     * of its left and right boundary minus its height.
     * Hence, our algorithm looks like this:
     * <p>
     * 1. Initialize maxLeft and maxRight to 0, res = 0
     * 2. For each element, move from current element to the front of the array and find the maxLeft
     * 3. For each element, move from current element to the end of the array and find the maxRight
     * 4. res += min(maxLeft, maxRight) - height[i]
     * <p>
     * The idea behind the scenes is that if current spot can trap water, its left and right boundaries must be taller than itself. And the
     * amount of water will be the height difference (which is minimum of boundary minus itself) times the bar width (which is 1). If
     * there is no taller wall at the left or the right (or both) side, the minimum of boundary is just itself, minus itself will get 0
     * indicating it cannot trap water at current location.
     * <p>
     * Time: O(n^2) since for each element, we traverse the whole array once
     * Space: O(1) only assign constant size of variables
     */
    public int trapBruteForce(int[] height) {
        int res = 0;
        for (int i = 0; i < height.length; i++) {
            int maxLeft = 0, maxRight = 0;
            for (int lo = 0; lo <= i; lo++) {
                maxLeft = Math.max(height[lo], maxLeft);
            }
            for (int hi = i; hi < height.length; hi++) {
                maxRight = Math.max(height[hi], maxRight);
            }
            res += Math.min(maxLeft, maxRight) - height[i];
        }
        return res;
    }

    /**
     * Approach 2: Dynamic Programming
     * <p>
     * In approach 1, we actually find the left boundary and the right boundary for each element, which gives us O(n^2) runtime. However,
     * we can do better by recording the left and right boundary by one-pass for each using DP.
     * <p>
     * 1. The first forward pass, we record the maximum left boundary by computing max(height[i], left[i - 1])
     * 2. The second backward pass, we record the maximum right boundary by computing max(height[i], right[i + 1])
     * 3. Then, final forward pass to compute the minimum of (left, right) minus current height and add it to final result.
     * <p>
     * <p>
     * Time: O(n), each loop we just do single one-pass on the whole array
     * Space: O(n), we build two more arrays with the size of input to store left and right boundaries
     */
    public int trapDP(int[] height) {
        int length = height.length;
        int res = 0;
        int[] leftMax = new int[length];
        int[] rightMax = new int[length];
        //first forward pass
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                leftMax[i] = height[i];
            } else {
                leftMax[i] = Math.max(leftMax[i - 1], height[i]);
            }
        }

        //second backward pass
        for (int i = length - 1; i >= 0; i--) {
            if (i == length - 1) {
                rightMax[i] = height[i];
            } else {
                rightMax[i] = Math.max(rightMax[i + 1], height[i]);
            }
        }

        //final forward path to compute trapped water
        for (int i = 0; i < length; i++) {
            res += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        return res;
    }

    /**
     * Approach 3: Stack
     * <p>
     * We can compute the amount of trapped water by single one-pass. In order to trap some water, we need a container with two boundaries
     * hence, we can use a stack to store index. If at any time, the current height is taller than the height at the top of the stack,
     * there is a possibility to container some water (we have a right boundary now). We simply pop the top element first
     * (it is actually the bottom level of the container) and search for the proper left boundary. If there is no such left boundary, we
     * contain nothing. Otherwise, the amount of water equals to current container height times the distance between two boundaries.
     * We do this until the right boundary reach the end of array and stack is empty.
     * <p>
     * 1. Use stack to store indices of the array
     * 2. Iterate the array
     * while stack is not empty and height[current] > height[top]:
     * 1. pop the current element (it is the bottom of the container)
     * 2. search for the left boundary, the distance between two boundaries is current - stack.peek() - 1
     * 3. the height of container is min(height[current], height[stack.peek()]) - height[top]
     * 4. update the result += height * distance
     * 3. Add current index to the stack
     * 4. Move current to the next position
     * <p>
     * Time: O(n) single one-pass
     * Space: O(n), in the worst case, we have a stairs-like or flat structure, we need to add all indices in the stack
     */
    public int trapStack(int[] height) {
        int res = 0, current = 0;
        Stack<Integer> stack = new Stack<>();
        while (current < height.length) {
            while (!stack.isEmpty() && height[current] > height[stack.peek()]) {
                int top = stack.pop();
                //if after popping, stack is empty
                //which means we cannot find a desired left boundary
                if (stack.isEmpty()) {
                    break;
                }
                int distance = current - stack.peek() - 1;
                int boundedHeight = Math.min(height[current], height[stack.peek()]) - height[top];
                res += distance * boundedHeight;
            }
            //add current index to the stack
            //move the current to the next position
            stack.push(current++);
        }
        return res;
    }

    /**
     * Approach 4: Two Pointers (BEST!!)
     * <p>
     * Based on approach 2, we notice that at each index, if leftMax < rightMax, the amount of trapped water is determined by leftMax,
     * otherwise, it is determined by rightMax. Hence, we use dynamic programming to store the leftMax and rightMax at each index. However,
     * we can now use two variables to store them, and assign two pointers to iterate over the array to update left and right max and add
     * trapped water to the final result
     * <p>
     * Time: O(n) single one-pass
     * Space: O(1) only assign a constant size of variables
     */
    public int trapTwoPointers(int[] height) {
        int res = 0;
        int left = 0, right = height.length - 1;  // two pointers
        int leftMax = 0, rightMax = 0;  // two variables to store left and right max

        while (left < right) {
            if (height[left] < height[right]) {
                // if left boundary is smaller, it will determine the amount of trapped water
                // update the left maximum height if applicable
                leftMax = Math.max(leftMax, height[left]);
                // trap some water at this index
                res += leftMax - height[left];
                // move the left pointer
                left += 1;
            } else {
                // right boundary is smaller, it will determine the trapped water
                // update the right maximum height if applicable
                rightMax = Math.max(rightMax, height[right]);
                // trap some water at this index
                res += rightMax - height[right];
                right -= 1;
            }
        }
        return res;
    }

    @Test
    public void trapTest() {
        /**
         * Example:
         * Input: [0,1,0,2,1,0,1,3,2,1,2,1]
         * Output: 6
         */
        int[] height = new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        assertEquals(6, trapBruteForce(height));
        assertEquals(6, trapDP(height));
        assertEquals(6, trapStack(height));
        assertEquals(6, trapTwoPointers(height));
    }
}
