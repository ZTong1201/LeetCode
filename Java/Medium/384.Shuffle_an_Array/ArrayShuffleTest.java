import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArrayShuffleTest {

    @Test
    public void ArrayShuffleListCopyTest() {
        /**
         * Example:
         * Input
         * ["Solution", "shuffle", "reset", "shuffle"]
         * [[[1, 2, 3]], [], [], []]
         * Output
         * [null, [3, 1, 2], [1, 2, 3], [1, 3, 2]]
         *
         * Explanation
         * Solution solution = new Solution([1, 2, 3]);
         * solution.shuffle();    // Shuffle the array [1,2,3] and return its result.
         *                        // Any permutation of [1,2,3] must be equally likely to be returned.
         *                        // Example: return [3, 1, 2]
         * solution.reset();      // Resets the array back to its original configuration [1,2,3]. Return [1, 2, 3]
         * solution.shuffle();    // Returns the random shuffling of array [1,2,3]. Example: return [1, 3, 2]
         */
        int[] nums = new int[]{1, 2, 3};
        ArrayShuffleListCopy arrayShuffleListCopy = new ArrayShuffleListCopy(nums);
        Set<Integer> expected = Set.of(1, 2, 3);
        int[] shuffledArray = arrayShuffleListCopy.shuffle();
        assertEquals(expected.size(), shuffledArray.length);
        for (int num : shuffledArray) {
            assertTrue(expected.contains(num));
        }
        assertArrayEquals(nums, arrayShuffleListCopy.reset());
        shuffledArray = arrayShuffleListCopy.shuffle();
        assertEquals(expected.size(), shuffledArray.length);
        for (int num : shuffledArray) {
            assertTrue(expected.contains(num));
        }
    }

    @Test
    public void ArrayShuffleSwapTest() {
        /**
         * Example:
         * Input
         * ["Solution", "shuffle", "reset", "shuffle"]
         * [[[1, 2, 3]], [], [], []]
         * Output
         * [null, [3, 1, 2], [1, 2, 3], [1, 3, 2]]
         *
         * Explanation
         * Solution solution = new Solution([1, 2, 3]);
         * solution.shuffle();    // Shuffle the array [1,2,3] and return its result.
         *                        // Any permutation of [1,2,3] must be equally likely to be returned.
         *                        // Example: return [3, 1, 2]
         * solution.reset();      // Resets the array back to its original configuration [1,2,3]. Return [1, 2, 3]
         * solution.shuffle();    // Returns the random shuffling of array [1,2,3]. Example: return [1, 3, 2]
         */
        int[] nums = new int[]{1, 2, 3};
        ArrayShuffleSwap arrayShuffleSwap = new ArrayShuffleSwap(nums);
        Set<Integer> expected = Set.of(1, 2, 3);
        int[] shuffledArray = arrayShuffleSwap.shuffle();
        assertEquals(expected.size(), shuffledArray.length);
        for (int num : shuffledArray) {
            assertTrue(expected.contains(num));
        }
        assertArrayEquals(nums, arrayShuffleSwap.reset());
        shuffledArray = arrayShuffleSwap.shuffle();
        assertEquals(expected.size(), shuffledArray.length);
        for (int num : shuffledArray) {
            assertTrue(expected.contains(num));
        }
    }
}
