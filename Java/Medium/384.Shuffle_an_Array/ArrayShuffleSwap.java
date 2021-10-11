import java.util.Random;

public class ArrayShuffleSwap {

    /**
     * Approach 2: Fisher-Yates algorithm with swap
     * Basically, the time complexity is not optimal because we need to remove elements at certain index which can take
     * O(n) time in the worst case. Therefore, we should consider shuffling the array inplace to avoid extra space
     * and runtime. Indeed, the essence of the shuffle algorithm is to either randomly pick up an element or randomly
     * pick an index to place. In the first algorithm, we're fixing the order of the index where we place elements and
     * randomly select numbers. Now in approach 2, we could fix the order of the number and then randomly select an index
     * to put it.
     * Essentially, we traverse the array from the beginning to the end (0 -> n - 1)
     * For each element at index i, we randomly select an index from [i, n - 1] (note that each element can be swapped with
     * itself) and do the swap.
     * Why this works? We want to actually make sure P(a random element picked at index k) = 1/n because each number will have
     * an equal chance to be placed at an index. Then
     * P(a random element picked at index k) = 1 / (n - k) * P(this random element is not selected from first k - 1 operations)
     * = 1 / (n - k) * [(n - 1) / n * (n - 2) / (n - 1) * ... * (n - k) / (n - k + 1)]
     * base case is k = 0 => 1 / n. For any k > 0 it will also equal to 1 / n. Therefore, we make sure each permutation will
     * have an equal probability to be chosen.
     * <p>
     * Time:
     * Constructor() & reset(): O(n) always need to clone the original array
     * shuffle(): O(n) since now we don't need to remove from the list and the shuffle will be done by one pass
     * Space: O(n)
     */
    private final int[] original;
    private final Random random;
    private int[] shuffled;

    public ArrayShuffleSwap(int[] nums) {
        original = nums.clone();
        shuffled = nums;
        random = new Random(1);
    }

    public int[] reset() {
        shuffled = original.clone();
        return shuffled;
    }

    private void swap(int i, int j) {
        int temp = shuffled[i];
        shuffled[i] = shuffled[j];
        shuffled[j] = temp;
    }

    private int randIntInRange(int min, int max) {
        // this will generate a random number from range [min, max)
        return random.nextInt(max - min) + min;
    }

    public int[] shuffle() {
        // for each element, we select a random index afterwards (can be itself) and swap the elements
        for (int i = 0; i < shuffled.length; i++) {
            swap(i, randIntInRange(i, shuffled.length));
        }
        return shuffled;
    }
}
