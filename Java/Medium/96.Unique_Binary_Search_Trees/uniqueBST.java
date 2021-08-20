import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class uniqueBST {

    /**
     * Given n, how many structurally unique BST's (binary search trees) that store values 1 ... n?
     * <p>
     * Approach 1: Dynamic Programming
     * <p>
     * For a given integer sequence 1 ... n, in order to compute the number of unique BSTs, we can actually select each value as a root value,
     * and use the rest of values to construct unique left subtrees and right subtrees. By doing so, we make sure all the BSTs are unique.
     * Hence, we convert the problem into how many unique ways to construct left and right subtrees for a given root node. It sounds like a
     * recursion process, because we divide a large problem into several small subproblems. However, we can keep track of the previous
     * computed result, i.e. the unique ways to construct BSTs for small n, and compute the final result in dynamic programming. For example,
     * if we have a sequence of [1, 2, 3, 4, 5, 6, 7], how many ways to construct unique BSTs using 3 as the root node? Notice that, we have
     * [1, 2, 3] in its left subtree, and [4, 5, 6, 7] in its right subtree. The number of ways equals to the unique number of left subtree
     * times unique number of right subtree, i.e. unique number of BSTs for 4 nodes and 3 nodes. We can use recursion to keep searching until
     * we reach the base case, i.e. when there is only one node, the number is simply 1. Or we can record the unique number of BSTs for 3 and
     * 4 nodes for quicker information retrieval using an array.
     * <p>
     * In summary, we can denote the number of unique BSTs for value n as F(n), and denote number of unique BSTs with root value i given n nodes
     * as G(i, n). Hence F(n) = sum from i = 1 to n G(i, n)
     * And G(i, n) = F(i - 1) * F(n - i)
     * <p>
     * We should initialize the array with res[0] = 1 and res[1] = 1 which corresponds to an empty tree or a tree with only the root value
     * (obviously, there is only 1 unique BST)
     * <p>
     * <p>
     * Time: The total number of iteration = (2 + 3 + 4 + ... + n) = (n + 2)(n - 1)/2, which is O(n^2)
     * Space: O(n) we need an array with size n + 1 to keep track of the number of unique BSTs for a given number of nodes
     */
    public int numTreesDP(int n) {
        int[] res = new int[n + 1];
        res[0] = 1;
        res[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                res[i] += res[j - 1] * res[i - j];
            }
        }
        return res[n];
    }

    /**
     * Approach 2: Catalan Numbers
     * <p>
     * Actually, the number of unique BSTs is the Catalan number. The convenient form for calculating such number is defined as follows:
     * <p>
     * C0 = 1, C(n + 1) = 2(2n + 1)/(n + 2) * C(n)
     * <p>
     * Hence we can use this simple form to compute the catalan number.
     * The only thing we need to note is that the catalan number can be very big as n increase, which leads to integer overflow in java,
     * we can actually store the result in a long, and convert it back to int when returning the result.
     * <p>
     * Time: O(n) only one simple loop now
     * Space: O(1) no extra space required
     */
    public int numTreesCatalan(int n) {
        long res = 1;
        for (int i = 0; i < n; i++) {
            res = res * 2 * (2L * i + 1) / (i + 2);
        }
        return (int) res;
    }

    @Test
    public void numTreesTest() {
        /**
         * Input: 3
         * Output: 5
         * Explanation:
         * Given n = 3, there are a total of 5 unique BST's:
         *
         *    1         3     3      2      1
         *     \       /     /      / \      \
         *      3     2     1      1   3      2
         *     /     /       \                 \
         *    2     1         2                 3
         */
        assertEquals(5, numTreesDP(3));
        assertEquals(5, numTreesCatalan(3));
    }
}
