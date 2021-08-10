import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class SmallestStringWithSwaps {

    /**
     * You are given a string s, and an array of pairs of indices in the string pairs where pairs[i] = [a, b] indicates 2
     * indices(0-indexed) of the string.
     * <p>
     * You can swap the characters at any pair of indices in the given pairs any number of times.
     * <p>
     * Return the lexicographically smallest string that s can be changed to after using the swaps.
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 10^5
     * 0 <= pairs.length <= 10^5
     * 0 <= pairs[i][0], pairs[i][1] < s.length
     * s only contains lower case English letters.
     * <p>
     * Approach: Union Find
     * The problem can be solved if we treat each character as a node in the graph. Each pair actually connect two nodes
     * in the graph. Once we get all connected components in the graph, we can sort those characters within each connected
     * group, and it will finally a string with the smallest lexicographical order.
     * <p>
     * Time: O(NlogN) The union and find time complexity will be around O(1) in practice and hence it gives O(N) in total
     * to construct the graph. However, in order to sort in each group (or use a priority queue), in the worst case it will
     * take up to O(NlogN) runtime
     * Space: O(N)
     */
    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int length = s.length();
        UnionFind uf = new UnionFind(length);
        for (List<Integer> pair : pairs) {
            // union two nodes specified by the pair
            uf.union(pair.get(0), pair.get(1));
        }

        // traverse the graph and group characters together
        // use priority queue to avoid subsequent sorting
        Map<Integer, PriorityQueue<Character>> connectedComponent = new HashMap<>();
        for (int i = 0; i < length; i++) {
            // get root for each node
            int root = uf.find(i);
            // create a new key-value pair if needed
            connectedComponent.putIfAbsent(root, new PriorityQueue<>());
            // add the character to that connected component
            connectedComponent.get(root).add(s.charAt(i));
        }

        // construct the final string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // at each index, poll the smallest character from the desired connected component
            // remember to find the root node at each index
            sb.append(connectedComponent.get(uf.find(i)).poll());
        }
        return sb.toString();
    }

    @Test
    public void smallestStringWithSwapsTest() {
        /**
         * Example 1:
         * Input: s = "dcab", pairs = [[0,3],[1,2]]
         * Output: "bacd"
         * Explanation:
         * Swap s[0] and s[3], s = "bcad"
         * Swap s[1] and s[2], s = "bacd"
         */
        List<List<Integer>> pairs1 = List.of(List.of(0, 3), List.of(1, 2));
        assertEquals("bacd", smallestStringWithSwaps("dcab", pairs1));
        /**
         * Example 2:
         * Input: s = "dcab", pairs = [[0,3],[1,2],[0,2]]
         * Output: "abcd"
         * Explanation:
         * Swap s[0] and s[3], s = "bcad"
         * Swap s[0] and s[2], s = "acbd"
         * Swap s[1] and s[2], s = "abcd"
         */
        List<List<Integer>> pairs2 = List.of(List.of(0, 3), List.of(1, 2), List.of(0, 2));
        assertEquals("abcd", smallestStringWithSwaps("dcab", pairs2));
        /**
         * Example 3:
         * Input: s = "cba", pairs = [[0,1],[1,2]]
         * Output: "abc"
         * Explanation:
         * Swap s[0] and s[1], s = "bca"
         * Swap s[1] and s[2], s = "bac"
         * Swap s[0] and s[1], s = "abc"
         */
        List<List<Integer>> pairs3 = List.of(List.of(0, 1), List.of(1, 2));
        assertEquals("abc", smallestStringWithSwaps("cba", pairs3));
    }

    private static class UnionFind {
        int[] size;
        int[] parent;

        UnionFind(int n) {
            this.size = new int[n];
            Arrays.fill(size, 1);
            this.parent = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        void union(int i, int j) {
            int p = find(i);
            int q = find(j);
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
}
