import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class fizzBuzz {

    /**
     * Write a program that outputs the string representation of numbers from 1 to n.
     *
     * But for multiples of three it should output “Fizz” instead of the number and for the multiples of five output “Buzz”.
     * For numbers which are multiples of both three and five output “FizzBuzz”.
     *
     * Time: O(N)
     * Space: O(N) for output list
     */
    public List<String> fizzBuzz(int n) {
        List<String> res = new LinkedList<>();
        for(int i = 0; i < n; i++) {
            if((i + 1) % 15 == 0) res.add("FizzBuzz");
            else if((i + 1) % 5 == 0) res.add("Buzz");
            else if((i + 1) % 3 == 0) res.add("Fizz");
            else res.add(String.valueOf(i + 1));
        }
        return res;
    }

    @Test
    public void fizzBuzzTest() {
        /**
         * Example 1:
         * Input: n = 1
         * Output: ["1"]
         */
        List<String> expected1 = new LinkedList<>(Arrays.asList("1"));
        List<String> actual1 = fizzBuzz(1);
        assertArrayEquals(expected1.toArray(), actual1.toArray());
        /**
         * Example 2:
         * Input: n = 15,
         *
         * Output:
         * [
         *     "1",
         *     "2",
         *     "Fizz",
         *     "4",
         *     "Buzz",
         *     "Fizz",
         *     "7",
         *     "8",
         *     "Fizz",
         *     "Buzz",
         *     "11",
         *     "Fizz",
         *     "13",
         *     "14",
         *     "FizzBuzz"
         * ]
         */
        List<String> expected2 = new LinkedList<>(Arrays.asList("1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz",
                "Buzz", "11", "Fizz", "13", "14", "FizzBuzz"));
        List<String> actual2 = fizzBuzz(15);
        assertArrayEquals(expected2.toArray(), actual2.toArray());

    }
}
