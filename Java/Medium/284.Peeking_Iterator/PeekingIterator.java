import java.util.Iterator;

public class PeekingIterator implements Iterator<Integer> {

    /**
     * Design an iterator that supports the peek operation on a list in addition to the hasNext and the next operations.
     * <p>
     * Implement the PeekingIterator class:
     * <p>
     * PeekingIterator(int[] nums) Initializes the object with the given integer array nums.
     * int next() Returns the next element in the array and moves the pointer to the next element.
     * bool hasNext() Returns true if there are still elements in the array.
     * int peek() Returns the next element in the array without moving the pointer.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 1000
     * 1 <= nums[i] <= 1000
     * All the calls to next and peek are valid.
     * At most 1000 calls will be made to next, hasNext, and peek.
     * <p>
     * Approach: Peek variable
     * Basically, we also need to keep track of the actual peek variable in the iterator since peek() method call doesn't move
     * the pointer to the next element. However, when dealing with the iterator, we can never go back when move to the next
     * element. That's why the peek variable comes into play. Since next and peek calls are guaranteed to be valid, we don't
     * need to worry about exception handling.
     * <p>
     * Time: O(1)
     * Space: O(1)
     */
    private final Iterator<Integer> iterator;
    private Integer peekValue;

    public PeekingIterator(Iterator<Integer> iterator) {
        this.iterator = iterator;
        // even though we don't have the test case for this edge scenario
        // however, it's always good to check whether the input iterator is already empty
        this.peekValue = (this.iterator.hasNext()) ? this.iterator.next() : null;
    }

    // Returns the next element in the iteration without advancing the iterator.
    public Integer peek() {
        // since all peek() calls are valid, we can always return peekValue
        return peekValue;
    }

    // hasNext() and next() should behave the same as in the Iterator interface.
    // Override them if needed.
    @Override
    public Integer next() {
        // here, the value to be returned is the peek value, but we need to move the pointer afterwards
        Integer returnValue = peekValue;
        // update the new peek value if any
        peekValue = (iterator.hasNext()) ? iterator.next() : null;
        return returnValue;
    }

    @Override
    public boolean hasNext() {
        // since the peek variable is pointing to the next value
        // we should have next element(s) if the peek value is not null
        return peekValue != null;
    }
}
