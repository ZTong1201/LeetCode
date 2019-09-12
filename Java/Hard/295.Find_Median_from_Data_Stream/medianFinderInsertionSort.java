import java.util.*;

public class medianFinderInsertionSort {

    /**
     * Median is the middle value in an ordered integer list. If the size of the list is even, there is no middle value. So the median
     * is the mean of the two middle value.
     *
     * For example,
     * [2,3,4], the median is 3
     *
     * [2,3], the median is (2 + 3) / 2 = 2.5
     *
     * Design a data structure that supports the following two operations:
     *
     * void addNum(int num) - Add a integer number from the data stream to the data structure.
     * double findMedian() - Return the median of all elements so far.
     *
     * Follow up:
     *
     * If all integer numbers from the stream are between 0 and 100, how would you optimize it?
     * If 99% of all integer numbers from the stream are between 0 and 100, how would you optimize it?
     *
     * Approach 1: Insertion Sort
     * 最简单的解法为，将stream中的元素记录下来，然后每一次都进行一次排序，然后根据数字总数决定中位数。但每一次都进行排序显然非常耗时，主要到其实在加入新元素num
     * 时，之前的列表已经是排好序的。对于一个almost sorted的列表来说，insertion sort是最快的。即在原始列表中找到新加入元素num所应该插入的位置，然后将num插入
     * 到正确的位置即可。因为原始列表是sorted，因此可以采用binary search找到正确位置。
     * 但将num插入到列表任意位置，需要O(n)时间
     *
     * Time: O(n + logn) = O(n) 虽然binary search只需要O(logn)时间，但将num插入到列表中需要O(n)时间
     * Space: O(n) 需要将数据流的值全部记录在列表中
     */

    List<Integer> nums;

    /** initialize your data structure here. */
    public medianFinderInsertionSort() {
        this.nums = new ArrayList<>();
    }

    public void addNum(int num) {
        if (nums.isEmpty()) {
            nums.add(num);
        } else {
            int index = binarySearch(num);
            nums.add(index, num);
        }
    }

    public double findMedian() {
        int n = nums.size();
        if (n % 2 == 0) {
            return 0.5 * (nums.get(n / 2) + nums.get(n / 2 - 1));
        }
        return (double) nums.get(n / 2);
    }

    //在已经排好序的数组中找到val的正确插入位置
    private int binarySearch(int val) {
        int left = 0, right = nums.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums.get(mid) > val) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
