import java.util.TreeMap;

public class SnapshotArray {

    /**
     * Implement a SnapshotArray that supports the following interface:
     * <p>
     * SnapshotArray(int length) initializes an array-like data structure with the given length.  Initially, each element
     * equals 0.
     * void set(index, val) sets the element at the given index to be equal to val.
     * int snap() takes a snapshot of the array and returns the snap_id: the total number of times we called snap() minus 1.
     * int get(index, snap_id) returns the value at the given index, at the time we took the snapshot with the given snap_id
     * <p>
     * Constraints:
     * <p>
     * 1 <= length <= 50000
     * At most 50000 calls will be made to set, snap, and get.
     * 0 <= index < length
     * 0 <= snap_id < (the total number of times we call snap())
     * 0 <= val <= 10^9
     * <p>
     * Approach: TreeMap (Binary Search)
     * Essentially, we keep track of the snap id and its corresponding value in a map at each index. However, we might take
     * tons of snapshots without resetting the elements, for instance, set(0, 5), snap(), snap(), snap(), snap(), snap().
     * The value for snap_id [0, 4] will all return 5, yet we don't necessarily need the all pairs, e.g. 1: 5, 2: 5, etc.
     * in the map. That is being said, we need to binary search the closet snapshot id in which we store a value. There is
     * an edge case when there is nothing in the array. According to the problem statement, the initial value will be 0
     * for all indexes, hence we can add a default pair {0: 0} into the map which indicates that the snap_id 0 has a value
     * 0 if we haven't set any values. The easiest way to search the lower bound of a value is to use TreeMap, since tree map
     * is well-balanced, the search time complexity will be O(logn)
     * <p>
     * Time:
     * Constructor: O(n) need to initialize a tree map for each index
     * set(): O(1)
     * snap(): O(1)
     * get(index, snap_id) O(logn) where n is the number of snapshots taken since we need to search for the nearest key
     * Space: O(S) where S is the number of set() calls. In the worst case, each set() call will create a new snapshot and we
     * have to add that key-value pair in one of our tree maps
     */
    private final TreeMap<Integer, Integer>[] arrayWithSnapId;
    private int snapId;

    public SnapshotArray(int length) {
        this.arrayWithSnapId = new TreeMap[length];
        for (int i = 0; i < length; i++) {
            arrayWithSnapId[i] = new TreeMap<>();
            // add a placeholder, if setter has not been called, return 0 as a default value
            arrayWithSnapId[i].put(0, 0);
        }
        this.snapId = 0;
    }

    public void set(int index, int val) {
        // attach the value with current snapshot version at desired index
        arrayWithSnapId[index].put(snapId, val);
    }

    public int snap() {
        // return the current snapshot id and increment it for next version
        return snapId++;
    }

    public int get(int index, int snap_id) {
        // there is a possibility that we haven't set any values for the given snap_id
        // binary search the closest snap_id in which it has a value (0 by default)
        return arrayWithSnapId[index].floorEntry(snap_id).getValue();
    }
}
