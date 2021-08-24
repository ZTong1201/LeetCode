import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.assertArrayEquals;

public class AsteroidCollision {

    /**
     * We are given an array asteroids of integers representing asteroids in a row.
     * <p>
     * For each asteroid, the absolute value represents its size, and the sign represents its direction (positive meaning
     * right, negative meaning left). Each asteroid moves at the same speed.
     * <p>
     * Find out the state of the asteroids after all collisions. If two asteroids meet, the smaller one will explode. If
     * both are the same size, both will explode. Two asteroids moving in the same direction will never meet.
     * <p>
     * Constraints:
     * <p>
     * 2 <= asteroids.length <= 10^4
     * -1000 <= asteroids[i] <= 1000
     * asteroids[i] != 0
     * <p>
     * Approach: Stack
     * If we already have a stable row of asteroids, when an asteroid kicks in from the right, there are three possibilities
     * 1. It has the same direction with the rightmost asteroid, the row will still be stable after appending it to the right
     * 2. It has the opposite direction, and it has an equal size with the rightmost one, they both explode.
     * 3. It has the opposite direction, and it has a larger size, it will keep exploding the rightmost asteroid until it
     * reaches an equal or larger size.
     * <p>
     * We can easily achieve the logic by using a stack. If the current asteroid can make a longer stable sequence, push it
     * into the stack. If at any time, the rightmost asteroid (the peek value of the stack) explodes, it will be popped from
     * the stack.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int[] asteroidCollision(int[] asteroids) {
        Stack<Integer> stack = new Stack<>();
        for (int asteroid : asteroids) {
            // a flag value to indicate whether the current asteroid should be inserted
            // by default it will always be inserted
            boolean push = true;
            // only when the current stable row of asteroids is moving to the right
            // and there is an asteroid moving in the opposite direction
            // start exploding asteroids
            while (!stack.isEmpty() && asteroid < 0 && stack.peek() > 0) {
                if (stack.peek() >= -asteroid) {
                    // when the peek size is larger than or equal to the current size of asteroid
                    // the current asteroid is exploded - don't insert it to the stable row
                    push = false;
                    // if they are in equals size - both asteroids are exploded
                    if (stack.peek() == -asteroid) {
                        stack.pop();
                    }
                    break;
                } else {
                    // otherwise, only the peek asteroid is exploded
                    // and the loop will continue since it might keep exploding asteroids
                    stack.pop();
                }
            }
            if (push) stack.push(asteroid);
        }
        int[] res = new int[stack.size()];
        for (int i = res.length - 1; i >= 0; i--) {
            res[i] = stack.pop();
        }
        return res;
    }

    @Test
    public void asteroidCollisionTest() {
        /**
         * Example 1:
         * Input: asteroids = [5,10,-5]
         * Output: [5,10]
         * Explanation: The 10 and -5 collide resulting in 10. The 5 and 10 never collide.
         */
        int[] expected1 = new int[]{5, 10};
        int[] actual1 = asteroidCollision(new int[]{5, 10, -5});
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: asteroids = [8,-8]
         * Output: []
         * Explanation: The 8 and -8 collide exploding each other.
         */
        int[] expected2 = new int[0];
        int[] actual2 = asteroidCollision(new int[]{8, -8});
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: asteroids = [10,2,-5]
         * Output: [10]
         * Explanation: The 2 and -5 collide resulting in -5. The 10 and -5 collide resulting in 10.
         */
        int[] expected3 = new int[]{10};
        int[] actual3 = asteroidCollision(new int[]{10, 2, -5});
        assertArrayEquals(expected3, actual3);
        /**
         * Example 4:
         * Input: asteroids = [-2,-1,1,2]
         * Output: [-2,-1,1,2]
         * Explanation: The -2 and -1 are moving left, while the 1 and 2 are moving right. Asteroids moving the same
         * direction never meet, so no asteroids will meet each other.
         */
        int[] expected4 = new int[]{-2, -1, 1, 2};
        int[] actual4 = asteroidCollision(new int[]{-2, -1, 1, 2});
        assertArrayEquals(expected4, actual4);
    }
}
