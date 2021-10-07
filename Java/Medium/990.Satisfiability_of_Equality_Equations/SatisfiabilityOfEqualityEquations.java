import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SatisfiabilityOfEqualityEquations {

    /**
     * You are given an array of strings equations that represent relationships between variables where each string
     * equations[i] is of length 4 and takes one of two different forms: "xi==yi" or "xi!=yi".Here, xi and yi are lowercase
     * letters (not necessarily different) that represent one-letter variable names.
     * <p>
     * Return true if it is possible to assign integers to variable names so as to satisfy all the given equations, or false
     * otherwise.
     * <p>
     * Constraints:
     * <p>
     * 1 <= equations.length <= 500
     * equations[i].length == 4
     * equations[i][0] is a lowercase letter.
     * equations[i][1] is either '=' or '!'.
     * equations[i][2] is '='.
     * equations[i][3] is a lowercase letter.
     * <p>
     * Approach: Union Find
     * Basically, if there is an equality relation, e.g. "a==b", which means a and b must be in the same connected component.
     * And if "a!=b", then a and b must not be in the same component. We only need to connect all nodes based on the equality
     * relations first, then go through all inequality to check whether there are two nodes already in the same connected
     * component which they shouldn't have. The optimal way to find connected component is to use a UnionFind data structure.
     * Since all variables are in lowercase letters, we only need 26 spaces in the union find.
     * <p>
     * Time: O(n) we need to go through the entire equation array, and union any two nodes will take O(a(n)) runtime, which
     * is constant time in practice.
     * Space: O(1) only need two arrays of size 26
     */
    public boolean equationsPossible(String[] equations) {
        // only need 26 spaces in the union find since only lowercase letters will be used
        UnionFind uf = new UnionFind(26);
        for (String equation : equations) {
            // connect two nodes if they are equal
            if (equation.charAt(1) == '=') {
                int node1 = equation.charAt(0) - 'a', node2 = equation.charAt(3) - 'a';
                uf.union(node1, node2);
            }
        }

        // then go through all inequalities - if two nodes are in the same connected component - return false
        for (String equation : equations) {
            if (equation.charAt(1) == '!') {
                int node1 = equation.charAt(0) - 'a', node2 = equation.charAt(3) - 'a';
                if (uf.find(node1) == uf.find(node2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static class UnionFind {
        private final int[] size;
        private final int[] parent;

        public UnionFind(int n) {
            this.size = new int[n];
            this.parent = new int[n];
            Arrays.fill(size, 1);
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int p = find(i), q = find(j);
            if (p == q) return;
            if (size[p] >= size[q]) {
                parent[q] = p;
                size[p] += size[q];
            } else {
                parent[p] = q;
                size[q] += size[p];
            }
        }
    }

    @Test
    public void equationsPossibleTest() {
        /**
         * Example 1:
         * Input: equations = ["a==b","b!=a"]
         * Output: false
         * Explanation: If we assign say, a = 1 and b = 1, then the first equation is satisfied, but not the second.
         * There is no way to assign the variables to satisfy both equations.
         */
        assertFalse(equationsPossible(new String[]{"a==b", "b!=a"}));
        /**
         * Example 2:
         * Input: equations = ["b==a","a==b"]
         * Output: true
         * Explanation: We could assign a = 1 and b = 1 to satisfy both equations.
         */
        assertTrue(equationsPossible(new String[]{"a==b", "b==a"}));
        /**
         * Example 3:
         * Input: equations = ["a==b","b==c","a==c"]
         * Output: true
         */
        assertTrue(equationsPossible(new String[]{"a==b", "b==c", "a==c"}));
        /**
         * Example 4:
         * Input: equations = ["a==b","b!=c","c==a"]
         * Output: false
         */
        assertFalse(equationsPossible(new String[]{"a==b", "b!=c", "c==a"}));
        /**
         * Example 5:
         * Input: equations = ["c==c","b==d","x!=z"]
         * Output: true
         */
        assertTrue(equationsPossible(new String[]{"c==c", "b==d", "x!=z"}));
    }

}
