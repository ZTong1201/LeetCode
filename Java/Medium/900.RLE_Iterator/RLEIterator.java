public class RLEIterator {

    /**
     * We can use run-length encoding (i.e., RLE) to encode a sequence of integers. In a run-length encoded array of even
     * length encoding (0-indexed), for all even i, encoding[i] tells us the number of times that the non-negative integer
     * value encoding[i + 1] is repeated in the sequence.
     * <p>
     * For example, the sequence arr = [8,8,8,5,5] can be encoded to be encoding = [3,8,2,5]. encoding = [3,8,0,9,2,5] and
     * encoding = [2,8,1,8,2,5] are also valid RLE of arr.
     * Given a run-length encoded array, design an iterator that iterates through it.
     * <p>
     * Implement the RLEIterator class:
     * <p>
     * RLEIterator(int[] encoded) Initializes the object with the encoded array encoded.
     * int next(int n) Exhausts the next n elements and returns the last element exhausted in this way. If there is no element
     * left to exhaust, return -1 instead.
     * <p>
     * Constraints:
     * <p>
     * 2 <= encoding.length <= 1000
     * encoding.length is even.
     * 0 <= encoding[i] <= 10^9
     * 1 <= n <= 10^9
     * At most 1000 calls will be made to next.
     * <p>
     * Approach: Counting
     * Basically, we loop through each position which indicates a length see whether we can make n become 0. If we exhaust
     * all elements at a given position, we move to the next. If at any time n is decremented to 0, then we return the
     * corresponding value.
     * <p>
     * Time:
     * Constructor: O(1)
     * next(): O(N) in total, O(1) amortized for each call
     * Space:
     * O(n)
     */
    private final int[] encodedArray;
    private int index;

    public RLEIterator(int[] encoding) {
        this.encodedArray = encoding;
    }

    public int next(int n) {
        // make sure the search is still in the boundary
        while (index < encodedArray.length) {
            // if we can exhaust n at current index
            // the corresponding value will be returned
            if (n <= encodedArray[index]) {
                encodedArray[index] -= n;
                return encodedArray[index + 1];
            }

            // otherwise, we've used up all elements at current location but still cannot make n become 0
            // the value shall never be returned
            // decrement n and move to the next position
            n -= encodedArray[index];
            index += 2;
        }
        // we've exhausted all elements in the array but still cannot make n become 0
        return -1;
    }
}
