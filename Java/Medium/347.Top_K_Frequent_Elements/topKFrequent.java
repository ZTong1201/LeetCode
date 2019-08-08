import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class topKFrequent {

    /**
     * Given a non-empty array of integers, return the k most frequent elements.
     *
     * Note:
     *
     * You may assume k is always valid, 1 ≤ k ≤ number of unique elements.
     * Your algorithm's time complexity must be better than O(n log n), where n is the array's size.
     *
     * Approach 1: Min Heap
     * 若使time complexity小于O(nlogn)，需要建立一个size为k的heap，插入heap的runtime为O(logk)。先遍历整个array，将每个元素出现的次数记录在map里
     * 在将每个元素和对应出现频率放在一个数组中一起add进heap，heap中元素的顺序按该元素出现的频率排布。当heap的size大于k时，就移除heap中最小的元素（意味着
     * 到目前为止，该元素出现的次数最少）。最后剩下的k个元素即为结果，若最终结果要求按次数从小到大排列，返回结果前将整个list reverse即可
     *
     * Time: O(Nlogk) 遍历整个数组需要O(N), 将每个元素插入heap总共需要O(Nlogk)，最终移除元素需要O(klogk)。所以总的运行时间为O(Nlogk)
     * Space: O(N) 需要一个map记录每个元素出现的次数，虽然仍需要size为k的heap，但一般情况下k << N，所以总的space为O(N)
     */
    public List<Integer> topKFrequent(int[] nums, int k) {
        PriorityQueue<int[]> minPQ = new PriorityQueue<>((a, b) -> (a[1] - b[1]));
        Map<Integer, Integer> freq = new HashMap<>();
        for(int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        for(int key : freq.keySet()) {
            minPQ.add(new int[]{key, freq.get(key)});
            if(minPQ.size() > k) {
                minPQ.poll();
            }
        }
        List<Integer> res = new ArrayList<>();
        for(int i = 0; i < k; i++) {
            res.add(minPQ.poll()[0]);
        }
        Collections.reverse(res);
        return res;
    }

    @Test
    public void topKFrequentTest() {
        /**
         * Example 1:
         * Input: nums = [1,1,1,2,2,3], k = 2
         * Output: [1,2]
         */
        int[] nums1 = new int[]{1, 1, 1, 2, 2, 3};
        List<Integer> actual1 = topKFrequent(nums1, 2);
        List<Integer> expected1 = new ArrayList<>(Arrays.asList(1, 2));
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input: nums = [1], k = 1
         * Output: [1]
         */
        int[] nums2 = new int[]{1};
        List<Integer> actual2 = topKFrequent(nums2, 1);
        List<Integer> expected2 = new ArrayList<>(Arrays.asList(1));
        assertEquals(expected2.size(), actual2.size());
        for(int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
    }
}
