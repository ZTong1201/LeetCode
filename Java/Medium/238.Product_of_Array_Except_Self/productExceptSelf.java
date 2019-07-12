import org.junit.Test;
import static org.junit.Assert.*;

public class productExceptSelf {

    /**
     * Given an array nums of n integers where n > 1,
     * return an array output such that output[i] is equal to the product of all the elements of nums except nums[i].
     *
     * Note: Please solve it without division and in O(n).
     *
     * Follow up:
     * Could you solve it with constant space complexity? (
     * The output array does not count as extra space for the purpose of space complexity analysis.)
     *
     * Approach 1: Left and Right product arrays
     * Since division is not allowed in this problem, we need to figure out another way to compute the product except itself.
     * Instead of using division, if we want to compute prod[i] except nums[i], we can compute the left product before index i, and
     * the right product after index i. The prod[i] will be simply equal to left product * right product.
     * In order to compute left product, we need iterate from the start of the array, and to compute right product, we need start
     * from the end of the array.
     *
     * The MOST IMPORTANT part for this algorithm is to initialize each array with a value of 1. Since there is nothing beyond the
     * array boundary, the left and right product will be simply 1.
     *
     * Time: O(3N) = O(N), we iterate over the length of array three times for computing left product, right product and product except self
     * Space: O(2N) = O(N), the output array doesn't account for space complexity. We need two more arrays to compute left and right product
     */
    public int[] productExceptSelfTwoArrays(int[] nums) {
        int length = nums.length;
        //we need three new arrays with the same length of input array to store left product, right product, and product except itself
        int[] left = new int[length];
        int[] right = new int[length];
        int[] res = new int[length];
        //compute left product before index i (0 -> (i - 1))
        left[0] = 1;
        for(int i = 1; i < length; i++) {
            left[i] = left[i - 1] * nums[i - 1];
        }
        //compute right product after index i ((i - 1) -> (length - 1))
        right[length - 1] = 1;
        for(int i = length - 2; i >= 0; i--) {
            right[i] = right[i + 1] * nums[i + 1];
        }
        //compute product except self
        for(int i = 0; i < length; i++) {
            res[i] = left[i] * right[i];
        }
        return res;
    }

    /**
     * Approach 2: Without Extra space
     * If the output array doesn't account for space complexity, we can actually do it in O(1) space. We simply assign an empty
     * result array, and compute the left product as we did in approach 1. When computing the right product, we don't actually need another
     * array. We can simply use an integer value to record the right product. Still, we initialize the right product as value of 1, and
     * keep updating it by multiply nums[i] to it to obtain right product after index i
     *
     * Time: O(2N) = O(N), we now traverse the entire array twice to compute left product first, and compute right product and update the
     *       product except itself simultaneously
     * Space: O(1), only require an integer value to keep track of right product when we iterate over the array backwardly.
     */
    public int[] productExceptSelfWithoutSpace(int[] nums) {
        int length = nums.length;
        //the first pass will fill the left product into the result array as we did in approach 1
        int[] res = new int[length];
        res[0] = 1;
        for(int i = 1; i < length; i++) {
            res[i] = res[i - 1] * nums[i - 1];
        }

        //assign an integer value to keep track of right product, and update the result array
        //by res[i](left product) * right (right product)
        int right = 1;
        for(int i = length - 1; i >= 0; i--) {
            res[i] = res[i] * right;
            right *= nums[i];
        }
        return res;
    }


    @Test
    public void productExceptSelfTwoArraysTest() {
        /**
         * Example 1:
         * Input:  [1,2,3,4]
         * Output: [24,12,8,6]
         */
        int[] nums = new int[]{1, 2, 3, 4};
        int[] expected = new int[] {24, 12, 8, 6};
        int[] actual = productExceptSelfTwoArrays(nums);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void productExceptSelfWithoutSpaceTest() {
        /**
         * Example 1:
         * Input:  [1,2,3,4]
         * Output: [24,12,8,6]
         */
        int[] nums = new int[]{1, 2, 3, 4};
        int[] expected = new int[] {24, 12, 8, 6};
        int[] actual = productExceptSelfTwoArrays(nums);
        assertArrayEquals(expected, actual);
    }
}
