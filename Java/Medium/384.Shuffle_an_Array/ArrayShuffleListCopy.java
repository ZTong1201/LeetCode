import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArrayShuffleListCopy {

    /**
     * Given an integer array nums, design an algorithm to randomly shuffle the array. All permutations of the array should
     * be equally likely as a result of the shuffling.
     * <p>
     * Implement the Solution class:
     * <p>
     * Solution(int[] nums) Initializes the object with the integer array nums.
     * int[] reset() Resets the array to its original configuration and returns it.
     * int[] shuffle() Returns a random shuffling of the array.
     * <p>
     * Constraints:
     * <p>
     * 1 <= nums.length <= 200
     * -10^6 <= nums[i] <= 10^6
     * All the elements of nums are unique.
     * At most 5 * 10^4 calls in total will be made to reset and shuffle.
     * <p>
     * Approach 1: Copy array to list
     * In order to achieve the shuffle functionality, let's first assume the random number generator is actually "random".
     * Then what we need to do is that for each shuffle call, we want to randomly select an index to place a number. How
     * to achieve that? We can do it in the reverse way, i.e. we place a random element at a time from index 0 until index
     * n - 1. Essentially, before shuffling the array, we can copy the entire array into a list. Then starting from index
     * 0 until index n - 1, we randomly select an element (an index in the list actually) and assign it to array[i]. After
     * that, we remove that element from the list. In the end, all the indexes will be filled with numbers and the list
     * will become empty.
     * <p>
     * For reset, during initialization, we need clone the entire array and keep it somewhere to make sure it's not being
     * changed during the shuffle call. Then each time when reset is called, we need to assign the cloned original array
     * to the shuffled array, and re-clone the original array again. Why clone? Because we're actually manipulating the
     * reference to the array and each shuffle call will change the input array without clone.
     * <p>
     * Time:
     * Constructor(): O(n) since we need to clone the array
     * reset(): O(n) similarly, we need to clone the array
     * shuffle(): O(n^2), for each element in the list, we need to call the remove() method which will take O(n) in the worst
     * case, hence in total it will be O(n^2) in the worst case.
     * Space: O(n)
     */
    private int[] original;
    private int[] shuffled;
    private final Random random;

    public ArrayShuffleListCopy(int[] nums) {
        // must clone the original array to make sure it won't be changed
        original = nums.clone();
        shuffled = nums;
        random = new Random(1);
    }

    public int[] reset() {
        // during reset, we assign the cloned original array to shuffled
        // and re-clone the original again
        shuffled = original;
        original = original.clone();
        return shuffled;
    }

    public int[] shuffle() {
        // for shuffle, we first need to copy the current array into a list
        List<Integer> clonedList = new ArrayList<>();
        for (int num : shuffled) {
            clonedList.add(num);
        }

        // then starting from index 0 until index n - 1
        // we randomly select an element (an index) from the cloned list
        // put it into the desired index, and remove that element from the list
        for (int i = 0; i < shuffled.length; i++) {
            // get a random integer within the size of cloned list
            int removeIndex = random.nextInt(clonedList.size());
            // assign this random element to index i
            shuffled[i] = clonedList.get(removeIndex);
            // remove this element from the list
            clonedList.remove(removeIndex);
        }
        return shuffled;
    }
}
