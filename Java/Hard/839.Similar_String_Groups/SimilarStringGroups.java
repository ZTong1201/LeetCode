import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SimilarStringGroups {

    /**
     * Two strings X and Y are similar if we can swap two letters (in different positions) of X, so that it equals Y.
     * Also two strings X and Y are similar if they are equal.
     * <p>
     * For example, "tars" and "rats" are similar (swapping at positions 0 and 2), and "rats" and "arts" are similar,
     * but "star" is not similar to "tars", "rats", or "arts".
     * <p>
     * Together, these form two connected groups by similarity: {"tars", "rats", "arts"} and {"star"}.  Notice that "tars"
     * and "arts" are in the same group even though they are not similar.  Formally, each group is such that a word is in
     * the group if and only if it is similar to at least one other word in the group.
     * <p>
     * We are given a list strs of strings where every string in strs is an anagram of every other string in strs.
     * How many groups are there?
     * <p>
     * Constraints:
     * <p>
     * 1 <= strs.length <= 300
     * 1 <= strs[i].length <= 300
     * strs[i] consists of lowercase letters only.
     * All words in strs have the same length and are anagrams of each other.
     * <p>
     * Approach: Brute Force + Union Find
     * For each given word, loop through every pair with the element after it and check whether they are similar strings. Since
     * they are all anagrams, then if there are two places have mismatched characters, or they are exact string strings then
     * two strings are similar. If two strings are similar, we union them together in the union find data structure. Return
     * the number of connected components in the end.
     * <p>
     * Time: O(n^2 * L) where L is the length of the word. If path compression & group by rank is used, the union runtime will
     * be approximately in O(1). Therefore, in the worst case, we need to enumerate all n(n + 1)/2 pairs and check the number
     * of mismatches at each index
     * Space: O(n)
     */
    public int numSimilarGroups(String[] strs) {
        int length = strs.length;
        UnionFind uf = new UnionFind(length);
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                int diff = 0;
                for (int k = 0; k < strs[i].length(); k++) {
                    if (strs[i].charAt(k) != strs[j].charAt(k)) diff++;
                }
                if (diff == 2 || strs[i].equals(strs[j])) {
                    uf.union(i, j);
                }
            }
        }
        return uf.numOfConnectedComponents();
    }

    @Test
    public void numSimilarGroupsTest() {
        /**
         * Example 1:
         * Input: strs = ["tars","rats","arts","star"]
         * Output: 2
         */
        assertEquals(2, numSimilarGroups(new String[]{"tars", "rats", "arts", "star"}));
        /**
         * Example 2:
         * Input: strs = ["blw","bwl","wlb"]
         * Output: 1
         */
        assertEquals(1, numSimilarGroups(new String[]{"blw", "bwl", "wlb"}));
        /**
         * Example 3:
         * Input: strs =
         * ["kccomwcgcs","socgcmcwkc","sgckwcmcoc","coswcmcgkc","cowkccmsgc","cosgmccwkc","sgmkwcccoc",
         * "coswmccgkc","kowcccmsgc","kgcomwcccs"]
         * Output: 5
         */
        assertEquals(5, numSimilarGroups(new String[]{"kccomwcgcs", "socgcmcwkc", "sgckwcmcoc", "coswcmcgkc",
                "cowkccmsgc", "cosgmccwkc", "sgmkwcccoc", "coswmccgkc", "kowcccmsgc", "kgcomwcccs"}));
    }

    private static class UnionFind {
        private final int[] parent;
        private final int[] size;

        public UnionFind(int n) {
            this.parent = new int[n];
            this.size = new int[n];
            Arrays.fill(this.size, 1);
            for (int i = 0; i < n; i++) {
                this.parent[i] = i;
            }
        }

        private int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int p = find(i);
            int q = find(j);
            if (p == q) return;
            if (size[p] >= size[q]) {
                parent[q] = p;
                size[p] += size[q];
            } else {
                parent[q] = p;
                size[q] += size[p];
            }
        }

        public int numOfConnectedComponents() {
            int count = 0;
            for (int i = 0; i < parent.length; i++) {
                if (i == parent[i]) count++;
            }
            return count;
        }
    }
}
