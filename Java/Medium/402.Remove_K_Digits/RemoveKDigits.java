import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class RemoveKDigits {

    /**
     * Given string num representing a non-negative integer num, and an integer k, return the smallest possible integer after
     * removing k digits from num.
     * <p>
     * Constraints:
     * <p>
     * 1 <= k <= num.length <= 10^5
     * num consists of only digits.
     * num does not have any leading zeros except for the zero itself.
     * <p>
     * Approach: Greedy + Stack
     * Given a number, in order to achieve the smallest number after removing some digits, we actually want to greedily
     * remove the most significant digits from the left. For example, given "435", consider we only want to remove one digit,
     * then we must remove '4' from the left since the most significant digit will become '3', it will be smaller than any
     * other numbers like "43"  and "45". Therefore, for a given number string [D1D2...DN] we'd like to remove Di if
     * Di > D(i + 1). We will keep removing that until there is nothing in the left or we already removed k digits or
     * there is no greater element from the left. In order to achieve that, we could take advantage of the stack property.
     * The top value in the stack will be the previous most significant digits from the left. We compare current digit with
     * it and see whether we need to pop anything from the stack. Then we always add the current digit into the stack.
     * <p>
     * There will be three edge cases after we go through the entire string.
     * 1. We haven't removed enough digits from the string, then we just remove the rest of digits from the stack (right -> left)
     * Why? Because now we guaranteed the smallest significant digits are already on the left, removing from left will only
     * result in a larger value, e.g. "1234" remove 1 "123" will be the smallest
     * 2. There might be some leading zeros, we need to make sure to not add them into the result string.
     * 3. If the result is empty, we will return "0" instead of an empty string.
     * <p>
     * Since we need to build the result string from the smallest to the largest, we could use a linked list to mock the behavior
     * of a stack. Adding and removing from the last as if we were pushing and popping from the stack. And finally go through the
     * stack from the front to the end.
     * <p>
     * Time: O(N)
     * Space: O(N)
     */
    public String removeKDigits(String num, int k) {
        // use a linked list for the stack
        LinkedList<Character> stack = new LinkedList<>();
        for (int i = 0; i < num.length(); i++) {
            char curr = num.charAt(i);
            // if the stack is not empty and we can still remove some digits from the string
            // greedily remove a larger digit from the left
            while (!stack.isEmpty() && k > 0 && stack.getLast() > curr) {
                stack.removeLast();
                k--;
            }
            stack.addLast(curr);
        }

        // edge case 1: we may not remove enough digits, remove the rest from the right
        for (int i = 0; i < k; i++) {
            stack.removeLast();
        }

        StringBuilder res = new StringBuilder();
        boolean leadingZero = true;
        // iterate over the stack from the smallest to the largest elements
        for (char digit : stack) {
            // edge case 2: not adding leading zeros into the result string
            if (digit == '0' && leadingZero) continue;
            leadingZero = false;
            res.append(digit);
        }
        // edge case 3: if the result string is empty, return "0" instead
        return res.length() == 0 ? "0" : res.toString();
    }

    @Test
    public void removeKDigitsTest() {
        /**
         * Example 1:
         * Input: num = "1432219", k = 3
         * Output: "1219"
         * Explanation: Remove the three digits 4, 3, and 2 to form the new number 1219 which is the smallest.
         */
        assertEquals("1219", removeKDigits("1432219", 3));
        /**
         * Example 2:
         * Input: num = "10200", k = 1
         * Output: "200"
         * Explanation: Remove the leading 1 and the number is 200. Note that the output must not contain leading zeroes.
         */
        assertEquals("200", removeKDigits("10200", 1));
        /**
         * Example 3:
         * Input: num = "10", k = 2
         * Output: "0"
         * Explanation: Remove all the digits from the number and it is left with nothing which is 0.
         */
        assertEquals("0", removeKDigits("10", 2));
    }
}
