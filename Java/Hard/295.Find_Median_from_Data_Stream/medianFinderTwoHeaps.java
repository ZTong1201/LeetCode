import java.util.*;

public class medianFinderTwoHeaps {

    /**
     * Approach 2: Two Heaps
     * 中位数的本质是，列表中有一半的元素大于中位数，同时有一半的元素小于中位数。因此可以考虑使用两个heap，一个min heap记录列表中较小的前半部分元素，和一个max
     * heap记录较大的另一半元素。根据中位数定义，min heap的size只能等于max heap的size，或者比max heap的size大1，即
     * 1. 若总共有2n + 1个元素，那么min heap中有n + 1个元素，而max heap中有n个元素，此时中位数是min heap中最大的元素
     * 2. 若总共有2n个元素，那么min heap和max heap各有n个元素，此时中位数是min heap的最大元素与max heap最小元素的平均数
     * 由此可以看出，计算中位数只需要O(1)时间，该方法的重点是如何加入新元素
     * 加入新元素时，首先将其放入min heap中，然后将min heap中的最大元素移除，放入max heap中，执行这步操作后，两个heap的size可能不再满足上述条件，此时需要再将
     * max heap中最小元素移除，再放回min heap中。保证两个heap的size大小关系。
     *
     * Time: O(5 * logn) 每次加入新元素时，需要3次insertion操作和2次deletion操作，每次操作都是O(logn)时间，同时算中位数只需要O(1)时间
     * Space: O(n) 需要将所有元素分成两部分放入两个heap
     */
    PriorityQueue<Integer> minPQ;
    PriorityQueue<Integer> maxPQ;

    /** initialize your data structure here. */
    public medianFinderTwoHeaps() {
        minPQ = new PriorityQueue<>();
        maxPQ = new PriorityQueue<>((a, b) -> {return b - a;});
    }

    public void addNum(int num) {
        //先将元素放入min heap
        minPQ.add(num);
        //然后将min heap中的最大值移除，放入max heap中
        maxPQ.add(minPQ.poll());
        //若此时min heap的size小于max heap的size，不满足条件，需要将max heap的最小元素移除，再放回min heap中
        if (minPQ.size() < maxPQ.size()) {
            minPQ.add(maxPQ.poll());
        }
    }

    public double findMedian() {
        //最后只需要根据min heap和max heap的size关系，确定中位数即可
        if (minPQ.size() > maxPQ.size()) {
            return (double) minPQ.peek();
        }
        return 0.5 * (minPQ.peek() + maxPQ.peek());
    }
}
