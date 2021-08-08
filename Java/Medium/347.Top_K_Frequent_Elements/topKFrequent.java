import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class topKFrequent {

    /**
     * Given a non-empty array of integers, return the k most frequent elements.
     * <p>
     * Note:
     * <p>
     * You may assume k is always valid, 1 ≤ k ≤ number of unique elements.
     * Your algorithm's time complexity must be better than O(n log n), where n is the array's size.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 10^5
     * k is in the range [1, the number of unique elements in the array].
     * It is guaranteed that the answer is unique.
     * <p>
     * Follow up: Your algorithm's time complexity must be better than O(n log n), where n is the array's size.
     * <p>
     * Approach 1: Min Heap
     * 若使time complexity小于O(nlogn)，需要建立一个size为k的heap，插入heap的runtime为O(logk)。先遍历整个array，将每个元素出现的次数记录在map里
     * 在将每个元素放进heap时，heap中元素的顺序按该元素出现的频率排布。当heap的size大于k时，就移除heap中最小的元素（意味着
     * 到目前为止，该元素出现的次数最少）。最后剩下的k个元素即为结果，若最终结果要求按次数从小到大排列，返回结果前将整个list reverse即可
     * <p>
     * Time: O(Nlogk) 遍历整个数组需要O(N), 将每个元素插入heap总共需要O(Nlogk)，最终移除元素需要O(klogk)。所以总的运行时间为O(Nlogk)
     * Space: O(N) 需要一个map记录每个元素出现的次数，虽然仍需要size为k的heap，但一般情况下k << N，所以总的space为O(N)
     */
    public int[] topKFrequentHeap(int[] nums, int k) {
        // initialize a hash map to record the frequency of each element
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        // create a new comparator
        Comparator<Integer> comparator = (Integer a, Integer b) -> {
            return freq.get(a) - freq.get(b);
        };
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(comparator);
        for (int key : freq.keySet()) {
            minHeap.add(key);
            // remove the element with the smallest frequency so far
            // when the heap size is larger than k
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        int[] res = new int[k];
        int i = 0;
        while (!minHeap.isEmpty()) {
            res[i++] = minHeap.poll();
        }
        return res;
    }

    /**
     * Approach 2: Quick Select
     * When the problem is trying to k-th smallest/largest/most frequent/least frequent values, we can always take advantage
     * of the quick selection algorithm to find the pivot index. For this specific problem statement, the law of comparison
     * would be the value with smaller frequency comes first. Hence, we need to find top (n - k) less frequent elements
     * and output from the end to the pivot position.
     * <p>
     * Time: O(n) on average, O(n^2) in the worst case
     * Space: O(n)
     */
    private Map<Integer, Integer> count;

    public int[] topKFrequentQuickSelect(int[] nums, int k) {
        count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        int[] unique = new int[count.size()];
        int i = 0;
        for (int num : count.keySet()) {
            unique[i++] = num;
        }

        quickSelect(unique, 0, unique.length - 1, unique.length - k);
        int[] res = new int[k];
        for (int m = unique.length - k, n = 0; m < unique.length && n < k; m++, n++) {
            res[n] = unique[m];
        }
        return res;
    }

    private void quickSelect(int[] unique, int start, int end, int k) {
        if (start == end) return;
        int pivot = partition(unique, start, end);
        if (pivot == k) return;
        else if (pivot > k) quickSelect(unique, start, pivot - 1, k);
        else quickSelect(unique, pivot + 1, end, k);
    }

    private int partition(int[] unique, int start, int end) {
        int mid = (end - start) / 2 + start;
        int pivotFrequency = count.get(unique[mid]);
        int correct_index = start;
        swap(unique, mid, end);

        for (int i = start; i < end; i++) {
            if (count.get(unique[i]) < pivotFrequency) {
                swap(unique, i, correct_index);
                correct_index++;
            }
        }

        swap(unique, correct_index, end);
        return correct_index;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    @Test
    public void topKFrequentTest() {
        /**
         * Example 1:
         * Input: nums = [1,1,1,2,2,3], k = 2
         * Output: [1,2]
         */
        int[] nums1 = new int[]{1, 1, 1, 2, 2, 3};
        int[] actualHeap1 = topKFrequentHeap(nums1, 2);
        int[] actualQuickSelect1 = topKFrequentQuickSelect(nums1, 2);
        int[] expected1 = new int[]{1, 2};
        Arrays.sort(actualHeap1);
        Arrays.sort(actualQuickSelect1);
        assertEquals(expected1.length, actualHeap1.length);
        assertEquals(expected1.length, actualQuickSelect1.length);
        for (int i = 0; i < expected1.length; i++) {
            assertEquals(expected1[i], actualHeap1[i]);
            assertEquals(expected1[i], actualQuickSelect1[i]);
        }
        /**
         * Example 2:
         * Input: nums = [1], k = 1
         * Output: [1]
         */
        int[] nums2 = new int[]{1};
        int[] actualHeap2 = topKFrequentHeap(nums2, 1);
        int[] actualQuickSelect2 = topKFrequentQuickSelect(nums2, 1);
        int[] expected2 = new int[]{1};
        assertEquals(expected2.length, actualHeap2.length);
        assertEquals(expected2.length, actualQuickSelect2.length);
        for (int i = 0; i < expected2.length; i++) {
            assertEquals(expected2[i], actualHeap2[i]);
            assertEquals(expected2[i], actualQuickSelect2[i]);
        }
    }
}
