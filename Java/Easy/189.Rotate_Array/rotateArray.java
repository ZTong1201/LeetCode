import org.junit.Test;
import static org.junit.Assert.*;

public class rotateArray {

    /**
     * Given an array, rotate the array to the right by k steps, where k is non-negative.
     * Note:
     *
     * Try to come up as many solutions as you can, there are at least 3 different ways to solve this problem.
     * Could you do it in-place with O(1) extra space?
     *
     * Appraoch 1: Brute Force
     * 每次rotate的时候从后往前赋值直到index为1，rotate之前只需要记住最后一个元素，再把最后一个元素赋给index 0即可
     *
     * Time: O(kN), 每次rotate都要赋值N次
     * Space: O(1)
     */
    public void rotateBruteForce(int[] nums, int k) {
        int length = nums.length;
        for(int i = 0; i < k; i++) {
            int last = nums[length - 1];
            for(int j = length - 1; j >= 1; j--) {
                nums[j] = nums[j - 1];
            }
            nums[0] = last;
        }
    }

    @Test
    public void rotateBruteForceTest() {
        /**
         * Example 1:
         * Input: [1,2,3,4,5,6,7] and k = 3
         * Output: [5,6,7,1,2,3,4]
         * Explanation:
         * rotate 1 steps to the right: [7,1,2,3,4,5,6]
         * rotate 2 steps to the right: [6,7,1,2,3,4,5]
         * rotate 3 steps to the right: [5,6,7,1,2,3,4]
         */
        int[] nums1 = new int[]{1, 2, 3, 4, 5, 6, 7};
        rotateBruteForce(nums1, 3);
        int[] expected1 = new int[]{5, 6, 7, 1, 2, 3, 4};
        assertArrayEquals(expected1, nums1);
        /**
         * Example 2:
         * Input: [-1,-100,3,99] and k = 2
         * Output: [3,99,-1,-100]
         * Explanation:
         * rotate 1 steps to the right: [99,-1,-100,3]
         * rotate 2 steps to the right: [3,99,-1,-100]
         */
        int[] nums2 = new int[]{-1, -100, 3, 99};
        rotateBruteForce(nums2, 2);
        int[] expected2 = new int[]{3, 99, -1, -100};
        assertArrayEquals(expected2, nums2);
        /**
         * Example 3:
         * Input: [-1] and k = 2
         * Output: [-1]
         */
        int[] nums3 = new int[]{-1};
        rotateBruteForce(nums3, 2);
        int[] expected3 = new int[]{-1};
        assertArrayEquals(expected3, nums3);
        /**
         * Example 4:
         * Input: [2, 1] and k = 3
         * Output: [1, 2]
         * Explanation:
         * rotate 1 steps to the right: [1, 2]
         * rotate 2 steps to the right: [2, 1]
         * rotate 3 steps to the right: [1, 2]
         */
        int[] nums4 = new int[]{2, 1};
        rotateBruteForce(nums4, 3);
        int[] expected4 = new int[]{1, 2};
        assertArrayEquals(expected4, nums4);
    }

    /**
     * Approach 2: Extra Array
     * 可以把rotate之后的结果存放在另一个数组里，再将整个数组copy给原数组。
     * 值得注意的是，在向➡右rotate k次之后，原来在index i位置的元素的新位置为(i + k) % (nums.length)
     *
     * Time: O(N)
     * Space: O(N)
     */
    public void rotateExtraArray(int[] nums, int k) {
        int[] res = new int[nums.length];
        for(int i = 0; i < nums.length; i++) {
            res[(i + k) % nums.length] = nums[i];
        }
        System.arraycopy(res, 0, nums, 0, nums.length);
    }

    @Test
    public void rotateExtraArrayTest() {
        /**
         * Example 1:
         * Input: [1,2,3,4,5,6,7] and k = 3
         * Output: [5,6,7,1,2,3,4]
         * Explanation:
         * rotate 1 steps to the right: [7,1,2,3,4,5,6]
         * rotate 2 steps to the right: [6,7,1,2,3,4,5]
         * rotate 3 steps to the right: [5,6,7,1,2,3,4]
         */
        int[] nums1 = new int[]{1, 2, 3, 4, 5, 6, 7};
        rotateExtraArray(nums1, 3);
        int[] expected1 = new int[]{5, 6, 7, 1, 2, 3, 4};
        assertArrayEquals(expected1, nums1);
        /**
         * Example 2:
         * Input: [-1,-100,3,99] and k = 2
         * Output: [3,99,-1,-100]
         * Explanation:
         * rotate 1 steps to the right: [99,-1,-100,3]
         * rotate 2 steps to the right: [3,99,-1,-100]
         */
        int[] nums2 = new int[]{-1, -100, 3, 99};
        rotateExtraArray(nums2, 2);
        int[] expected2 = new int[]{3, 99, -1, -100};
        assertArrayEquals(expected2, nums2);
        /**
         * Example 3:
         * Input: [-1] and k = 2
         * Output: [-1]
         */
        int[] nums3 = new int[]{-1};
        rotateExtraArray(nums3, 2);
        int[] expected3 = new int[]{-1};
        assertArrayEquals(expected3, nums3);
        /**
         * Example 4:
         * Input: [2, 1] and k = 3
         * Output: [1, 2]
         * Explanation:
         * rotate 1 steps to the right: [1, 2]
         * rotate 2 steps to the right: [2, 1]
         * rotate 3 steps to the right: [1, 2]
         */
        int[] nums4 = new int[]{2, 1};
        rotateExtraArray(nums4, 3);
        int[] expected4 = new int[]{1, 2};
        assertArrayEquals(expected4, nums4);
    }

    /**
     * Approach 3: Cyclic Replacement (BEST!!)
     * 可以对应每一个元素，计算它在rotate之后的index，然后记录该index现在的值，然后继续查看现在的值应该在的index，直到遍历所有的元素。本质上，就是将原来
     * 在index i的元素移动到i + k，若i + k溢出边界，就移动到(i + k) % nums.length。所以当index又重新回到此次替换开始的index时，就跳出循环，继续查看
     * 他的下一个元素。在循环的过程中，用一个count来记录已经替换过的元素个数，当所有元素均被替换之后，就不再循环。
     *
     * Time: O(N)    每个元素都只换位一次
     * Space: O(1)   只需要constant space即可
     */
    public void rotateCyclicReplacement(int[] nums, int k) {
        //用count来记录是否所有元素都已换位
        int count = 0;
        //换位从index 0开始，跳出循环的条件为所有元素都已被换位,即count = nums.length - 1
        for(int start = 0; count < nums.length; start++) {
            //循环从当前位置开始， 但记录此次换位开始的位置，
            //若再次回到开始位置，就进行下一次循环，替换它的下一个元素的位置(即start++)
            int current = start;
            //记录下当前元素，在合适位置替换
            int prev = nums[current];
            //循环跳出条件比较特别，是当current第二次回到start位置的时候，跳出循环
            //因此用do while代替while，因为do while先执行代码，后判断条件。
            do {
                //计算当前元素该放置的位置
                int next = (current + k) % nums.length;
                //记录该位置当前的元素
                int temp = nums[next];
                //该位置应放置之前记录的元素
                nums[next] = prev;
                //更新所要继续放置的元素
                prev = temp;
                //index的位置也要移动到当前位置
                current = next;
                //因为已经移动过一个元素，所以计数加1
                count++;

            } while(start != current); //当current第二次回到start时，停止替换

        }
    }

    @Test
    public void rotateCyclicReplacementTest() {
        /**
         * Example 1:
         * Input: [1,2,3,4,5,6,7] and k = 3
         * Output: [5,6,7,1,2,3,4]
         * Explanation:
         * rotate 1 steps to the right: [7,1,2,3,4,5,6]
         * rotate 2 steps to the right: [6,7,1,2,3,4,5]
         * rotate 3 steps to the right: [5,6,7,1,2,3,4]
         */
        int[] nums1 = new int[]{1, 2, 3, 4, 5, 6, 7};
        rotateCyclicReplacement(nums1, 3);
        int[] expected1 = new int[]{5, 6, 7, 1, 2, 3, 4};
        assertArrayEquals(expected1, nums1);
        /**
         * Example 2:
         * Input: [-1,-100,3,99] and k = 2
         * Output: [3,99,-1,-100]
         * Explanation:
         * rotate 1 steps to the right: [99,-1,-100,3]
         * rotate 2 steps to the right: [3,99,-1,-100]
         */
        int[] nums2 = new int[]{-1, -100, 3, 99};
        rotateCyclicReplacement(nums2, 2);
        int[] expected2 = new int[]{3, 99, -1, -100};
        assertArrayEquals(expected2, nums2);
        /**
         * Example 3:
         * Input: [-1] and k = 2
         * Output: [-1]
         */
        int[] nums3 = new int[]{-1};
        rotateCyclicReplacement(nums3, 2);
        int[] expected3 = new int[]{-1};
        assertArrayEquals(expected3, nums3);
        /**
         * Example 4:
         * Input: [2, 1] and k = 3
         * Output: [1, 2]
         * Explanation:
         * rotate 1 steps to the right: [1, 2]
         * rotate 2 steps to the right: [2, 1]
         * rotate 3 steps to the right: [1, 2]
         */
        int[] nums4 = new int[]{2, 1};
        rotateCyclicReplacement(nums4, 3);
        int[] expected4 = new int[]{1, 2};
        assertArrayEquals(expected4, nums4);
    }

    /**
     * Approach 4: Reverse
     * 本质上，rotate之后，就是把后k个元素拿到前面，前n - k个元素放在后面。只需将原数组整个reverse，即将元素由前变后，在分别reverse前k个和后n - k个元素
     * 即可。值得注意的是，当k大于n时，将原数组rotate k次，和将原数组rotate (k % length)次相同，所以reverse前将k对length取余，将其变为0，length之间
     * 的数，才能reverse
     *
     * Time: O(N) 三次reverse分别需要O(N), O(k), O(N - k)的runtime，所以overall是O(N)
     * Space: O(1)
     */
    public void rotateReverse(int[] nums, int k) {
        k = k % nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    private void reverse(int[] nums, int left, int right) {
        while(left < right) {
            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;
            left++;
            right--;
        }
    }

    @Test
    public void rotateReversetTest() {
        /**
         * Example 1:
         * Input: [1,2,3,4,5,6,7] and k = 3
         * Output: [5,6,7,1,2,3,4]
         * Explanation:
         * rotate 1 steps to the right: [7,1,2,3,4,5,6]
         * rotate 2 steps to the right: [6,7,1,2,3,4,5]
         * rotate 3 steps to the right: [5,6,7,1,2,3,4]
         */
        int[] nums1 = new int[]{1, 2, 3, 4, 5, 6, 7};
        rotateReverse(nums1, 3);
        int[] expected1 = new int[]{5, 6, 7, 1, 2, 3, 4};
        assertArrayEquals(expected1, nums1);
        /**
         * Example 2:
         * Input: [-1,-100,3,99] and k = 2
         * Output: [3,99,-1,-100]
         * Explanation:
         * rotate 1 steps to the right: [99,-1,-100,3]
         * rotate 2 steps to the right: [3,99,-1,-100]
         */
        int[] nums2 = new int[]{-1, -100, 3, 99};
        rotateReverse(nums2, 2);
        int[] expected2 = new int[]{3, 99, -1, -100};
        assertArrayEquals(expected2, nums2);
        /**
         * Example 3:
         * Input: [-1] and k = 2
         * Output: [-1]
         */
        int[] nums3 = new int[]{-1};
        rotateReverse(nums3, 2);
        int[] expected3 = new int[]{-1};
        assertArrayEquals(expected3, nums3);
        /**
         * Example 4:
         * Input: [2, 1] and k = 3
         * Output: [1, 2]
         * Explanation:
         * rotate 1 steps to the right: [1, 2]
         * rotate 2 steps to the right: [2, 1]
         * rotate 3 steps to the right: [1, 2]
         */
        int[] nums4 = new int[]{2, 1};
        rotateReverse(nums4, 3);
        int[] expected4 = new int[]{1, 2};
        assertArrayEquals(expected4, nums4);
    }
}
