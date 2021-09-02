import org.junit.Test;

import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;

import static org.junit.Assert.assertArrayEquals;

public class maxSlidingWindow {

    /**
     * Given an array nums, there is a sliding window of size k which is moving from the very left of the array to the very right.
     * You can only see the k numbers in the window. Each time the sliding window moves right by one position.
     * Return the max sliding window.
     * <p>
     * Note:
     * You may assume k is always valid, 1 ≤ k ≤ input array's size for non-empty array.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 10^5
     * -10^4 <= nums[i] <= 10^4
     * 1 <= k <= nums.length
     * <p>
     * Approach 1: Using Max Heap
     * 因为题目想要得到每个window里的最大值，可以构建一个size为k的maximum heap，这样可以保证迅速地得到最大值。在移动window的过程中，将上一个window的左边界
     * 元素从heap中移除，然后再将新的右边界元素放入即可。
     * <p>
     * Time: O(nk) 因为不一定每次都会移除最大值，所以移除的runtime不一定是O(logk)，对于移除其他元素，时间复杂度可能为O(k)，因此对于n个元素，需要移除n - k + 1
     * 次，因此总的时间为O(nk)
     * Space: O(k)，只需要一个size为k的heap即可
     */
    public int[] maxSlidingWindowMaxHeap(int[] nums, int k) {
        //edge case，若输入数组为空，或者k为1，可以直接返回该数组
        if (nums.length == 0 || k == 1) return nums;
        //最后结果数组的大小为n - k + 1
        int[] res = new int[nums.length - k + 1];
        //构建一个max heap
        PriorityQueue<Integer> maxPQ = new PriorityQueue<>((a, b) -> {
            return b - a;
        });
        //分配两个指针移动window
        int left = 0, right = 0;
        while (right < nums.length) {
            //如果heap的size已经为k，说明需要向前移动当前window，因此需要移除当前的左边界元素，并将左边界前移一位
            //移动之前，要把当前的最大值记录在结果中
            if (maxPQ.size() == k) {
                res[left] = maxPQ.peek();
                maxPQ.remove(nums[left]);
                left++;
            }
            //在window不断扩大或者右移的过程中，要将右边界元素不断加入heap中，然后移动右指针
            maxPQ.add(nums[right]);
            right++;
        }
        //最后一位的最大值没有被分配，记得在最后加上
        res[left] = maxPQ.peek();
        return res;
    }

    /**
     * Follow up:
     * Could you solve it in linear time?
     * <p>
     * Approach 2: Monotonic Queue
     * 此题的数据结构需要两个主要功能
     * 1.找到当前window中的最大值
     * 2.当移动window时，需要将最先加进来的元素移除
     * 第一种功能可以用单调栈结构解决，即若当前加入的元素比栈顶元素大，则不断移除栈顶元素。但栈的LIFO的性质使得无法将最先入栈的元素移除。因此考虑用单调deque，
     * 因为deque两头都可以以O(1)时间移除元素，所以可以同时满足上述两个功能。
     * <p>
     * 所以只要用单调deque维持递增序列即可，然后当遍历的window size大于k后，开始将最大的元素（即deque的第一个元素）放入最终结果，同时当左边界移动时，需要将
     * deque的第一个元素从deque中移除。
     * <p>
     * Time: O(N) 每个元素只有最多两种操作，加入deque或者从deque中移除。因此总的时间为O(N)
     * Space: O(k) deque的元素最多为k，因为无论是保持单调结构还是将左边界移除，都保证了deque的size小于等于k
     */
    public int[] maxSlidingWindowDeque(int[] nums, int k) {
        if (nums.length == 0 || k == 1) return nums;
        int[] res = new int[nums.length - k + 1];
        //构建deque，记录每个元素的index
        Deque<Integer> monoQueue = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            //需要保证当前单调队列的元素在size为k的window内，当前index为i，那么window的left bound为i - k + 1;
            //从队列顶端移除所有小于left bound的元素
            while (!monoQueue.isEmpty() && monoQueue.peekFirst() <= i - k) {
                monoQueue.pollFirst();
            }
            //为了维持单调结构，若新元素大于此时deque顶元素，则将deque顶元素不断移除
            //栈顶所对应的index永远是当前window的最大值所在index
            while (!monoQueue.isEmpty() && nums[monoQueue.peekLast()] <= nums[i]) {
                monoQueue.pollLast();
            }
            //无论如何，当前index都要入deque
            monoQueue.addLast(i);

            //如果此时的window大小已经大于k，可以将deque顶端（即当前window最大值）放入最终结果
            if (i - k + 1 >= 0) {
                res[i - k + 1] = nums[monoQueue.peekFirst()];
            }
        }
        return res;
    }

    /**
     * Approach 3: Dynamic Programming
     * 首先对于输入的数组，可以将其按k分成几个block，注意当n % k != 0时，最后一个block的元素个数会小于k。例如，对于给定数组[1,3,-1,-3,5,3,6,7]，可以将其
     * 划分为[1,3-1], [-3,5,3], [6,7]。那么对于给定的window range为[i, j]，其最大值只有两种可能
     * 1.当前block内的最大值（例如给定range[0,2]， 最大值即为3）
     * 2.或者该最大值要跨越不同block（例如给定range[2,4]，最大值为5）
     * 第一种情况很好算，只需要将每个block的开始值初始化为第一个元素，之后的每个位置可以利用dp计算在当前block内到该位置的最大值。
     * 对于第二种情况，需要额外的一个数组，从右往左逆序地计算从每个block end到该位置的最大值。
     * <p>
     * 对于给定的range[i, j]，假设其跨越两个block，其block的边界index为b，right[i]记录的是range[i, b]内的最大值，而left[j]记录的则是[b, j]的最大值，因此
     * [i, j]的最大值就是right[i]和left[j]的最大值。
     * 所以只需要先遍历一遍数组，计算每个block内从左至右和从右至左的最大值，然后再从0循环到n - k + 1，计算每个range内的最大值
     * <p>
     * <p>
     * Time: O(n) 只需要遍历整个数组两次
     * Space: O(n) 需要两个数组记录从右至左和从左至右的block最大值
     */
    public int[] maxSlidingWindowDP(int[] nums, int k) {
        if (nums.length == 0 || k == 1) return nums;
        int n = nums.length;
        int[] res = new int[n - k + 1];
        int[] left = new int[n];
        //从左至右的第一个block要先将最大值初始化为该block的第一个元素
        left[0] = nums[0];
        int[] right = new int[n];
        //同理，从右至左的第一个block先将最大值初始化为该block的最后一个元素
        right[n - 1] = nums[n - 1];
        for (int i = 1; i < n; i++) {
            //若当前位置为block的开始index（即index是k的倍数，进行初始化)
            if (i % k == 0) {
                left[i] = nums[i];
            } else {
                //否则更新从左至右该block的最大值
                left[i] = Math.max(left[i - 1], nums[i]);
            }

            //根据当前的i，可以对称的找到末端的index j，注意要跳过数组的最后一个元素
            int j = n - i - 1;
            //同样，若当前index是该block的终止位置（即从右至左时的开始位置），进行初始化
            if ((j + 1) % k == 0) {
                right[j] = nums[j];
            } else {
                //反之，则更新当前block从右至左的最大值
                right[j] = Math.max(right[j + 1], nums[j]);
            }
        }
        //最后再过从0循环到n - k + 1，更新每个range的最大值
        for (int i = 0; i < n - k + 1; i++) {
            res[i] = Math.max(right[i], left[i + k - 1]);
        }
        return res;
    }


    @Test
    public void maxSlidingWindowTest() {
        /**
         * Example 1:
         * Input: nums = [1,3,-1,-3,5,3,6,7], and k = 3
         * Output: [3,3,5,5,6,7]
         * Explanation:
         *
         * Window position                Max
         * ---------------               -----
         * [1  3  -1] -3  5  3  6  7       3
         *  1 [3  -1  -3] 5  3  6  7       3
         *  1  3 [-1  -3  5] 3  6  7       5
         *  1  3  -1 [-3  5  3] 6  7       5
         *  1  3  -1  -3 [5  3  6] 7       6
         *  1  3  -1  -3  5 [3  6  7]      7
         */
        int[] nums1 = new int[]{1, 3, -1, -3, 5, 3, 6, 7};
        int[] expected1 = new int[]{3, 3, 5, 5, 6, 7};
        int[] actual1Heap = maxSlidingWindowMaxHeap(nums1, 3);
        int[] actual1Deque = maxSlidingWindowDeque(nums1, 3);
        int[] actual1DP = maxSlidingWindowDP(nums1, 3);
        assertArrayEquals(expected1, actual1Heap);
        assertArrayEquals(expected1, actual1Deque);
        assertArrayEquals(expected1, actual1DP);
        /**
         * Example 2:
         * Input: nums = [9,11], and k = 2;
         * Output: [11]
         */
        int[] nums2 = new int[]{9, 11};
        int[] expected2 = new int[]{11};
        int[] actual2Heap = maxSlidingWindowMaxHeap(nums2, 2);
        int[] actual2Deque = maxSlidingWindowDeque(nums2, 2);
        int[] actual2DP = maxSlidingWindowDP(nums2, 2);
        assertArrayEquals(expected2, actual2Heap);
        assertArrayEquals(expected2, actual2Deque);
        assertArrayEquals(expected2, actual2DP);
        /**
         * Example 3:
         * Input: nums = [7,2,4], and k = 2;
         * Output: [7,4]
         */
        int[] nums3 = new int[]{7, 2, 4};
        int[] expected3 = new int[]{7, 4};
        int[] actual3Heap = maxSlidingWindowMaxHeap(nums3, 2);
        int[] actual3Deque = maxSlidingWindowDeque(nums3, 2);
        int[] actual3DP = maxSlidingWindowDP(nums3, 2);
        assertArrayEquals(expected3, actual3Heap);
        assertArrayEquals(expected3, actual3Deque);
        assertArrayEquals(expected3, actual3DP);
        /**
         * Example 4:
         * Input: nums = [1,3,1,2,0,5], k = 3
         * Output: [3,3,2,5]
         */
        int[] nums4 = new int[]{1, 3, 1, 2, 0, 5};
        int[] expected4 = new int[]{3, 3, 2, 5};
        int[] actual4Heap = maxSlidingWindowMaxHeap(nums4, 3);
        int[] actual4Deque = maxSlidingWindowDeque(nums4, 3);
        int[] actual4DP = maxSlidingWindowDP(nums4, 3);
        assertArrayEquals(expected4, actual4Heap);
        assertArrayEquals(expected4, actual4Deque);
        assertArrayEquals(expected4, actual4DP);
    }
}
