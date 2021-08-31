import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FindDuplicateSubtrees {

    /**
     * Given the root of a binary tree, return all duplicate subtrees.
     * <p>
     * For each kind of duplicate subtrees, you only need to return the root node of any one of them.
     * <p>
     * Two trees are duplicate if they have the same structure with the same node values.
     * <p>
     * Constraints:
     * <p>
     * The number of the nodes in the tree will be in the range [1, 10^4]
     * -200 <= Node.val <= 200
     * <p>
     * Approach 1: Postorder tree serialization + hash map
     * In order to find duplicated subtrees, we need a way to serialize the binary tree such that we know a same structure has
     * been visited. The serialization itself could be in preorder, meaning we have [curr.val, left-tree, right-tree].
     * The key part of this problem is to add a placeholder for null node hence it will give unique layout for a given tree.
     * The serialization construction can be formed by postorder - to always construct trees from the smallest to the largest.
     * We use a hash map to keep track of the frequency of each tree structure, whenever the frequency is 2, we add it to the
     * final list.
     * <p>
     * Time: O(n^2) we need to construct the serialization for each node, the serialization (string formation) takes O(n) time
     * for a tree of size n
     * Space: O(n)
     */
    public List<TreeNode> findDuplicateSubtreesStringFormation(TreeNode root) {
        List<TreeNode> res = new ArrayList<>();
        Map<String, Integer> serialFrequency = new HashMap<>();
        postorderTreeSerialization(root, res, serialFrequency);
        return res;
    }

    private String postorderTreeSerialization(TreeNode root, List<TreeNode> res, Map<String, Integer> serialFrequency) {
        // key part - return a placeholder value for null node
        if (root == null) return "#";
        // serialize tree in postorder
        String serializedTree = "[" + root.val + ',' +
                postorderTreeSerialization(root.left, res, serialFrequency) + ',' +
                postorderTreeSerialization(root.right, res, serialFrequency) + ']';
        // put the current serialization into the map
        serialFrequency.put(serializedTree, serialFrequency.getOrDefault(serializedTree, 0) + 1);
        // if the serialization is duplicated, add current node into the result list
        if (serialFrequency.get(serializedTree) == 2) res.add(root);
        return serializedTree;
    }

    /**
     * Approach 2: Serialization ID
     * The string concatenation in approach 1 takes O(n) time for tree of size n which leads to O(n^2) runtime in total.
     * We could assign each tree serialization an ID such that the tree structure can be represented by
     * [root.val, left tree ID, right tree ID], now the string concatenation will be constant time. However, we need
     * two hash maps now to map between a serialization with its ID and the frequency of a given serialization ID.
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    private int currId;
    private Map<String, Integer> serialToId;
    private Map<Integer, Integer> idFrequency;
    private List<TreeNode> res;

    public List<TreeNode> findDuplicateSubtreesSerialId(TreeNode root) {
        res = new ArrayList<>();
        serialToId = new HashMap<>();
        idFrequency = new HashMap<>();
        currId = 1;

        postorderTreeSerializationWithSerialId(root);
        return res;
    }

    private int postorderTreeSerializationWithSerialId(TreeNode root) {
        // assign id 0 for null node
        if (root == null) return 0;
        // recursively get serial IDs for left and right subtrees
        int leftId = postorderTreeSerializationWithSerialId(root.left);
        int rightId = postorderTreeSerializationWithSerialId(root.right);
        // construct serialization as root.val, leftId, rightId;
        String serial = root.val + "," + leftId + "," + rightId;
        // get serial ID for current serialization
        int serialId = serialToId.getOrDefault(serial, currId);
        // if the serial ID has been used - increment curr ID for subsequent usage
        if (serialId == currId) currId++;
        // put serial - ID mapping
        serialToId.put(serial, serialId);
        // increment the frequency of current serial ID
        idFrequency.put(serialId, idFrequency.getOrDefault(serialId, 0) + 1);
        // if a duplicate serial is found - add the current root node to the result list
        if (idFrequency.get(serialId) == 2) res.add(root);
        // return current serialization ID
        return serialId;
    }

    @Test
    public void findDuplicateSubtreesStringFormationTest() {
        /**
         * Example 1:
         * Input: root = [1,2,3,4,null,2,4,null,null,4]
         * Output: [[2,4],[4]]
         *                       1
         *                      / \
         *                     2   3
         *                    /   / \
         *                   4   2   4
         *                      /
         *                     4
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.right.left = new TreeNode(2);
        root1.right.right = new TreeNode(4);
        root1.right.left.left = new TreeNode(4);
        List<TreeNode> expected1 = List.of(root1.right.left.left, root1.right.left);
        List<TreeNode> actual1 = findDuplicateSubtreesStringFormation(root1);
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input: root = [2,1,1]
         * Output: [[1]]
         *             2
         *            / \
         *           1   1
         */
        TreeNode root2 = new TreeNode(2);
        root2.left = new TreeNode(1);
        root2.right = new TreeNode(1);
        List<TreeNode> expected2 = List.of(root2.right);
        List<TreeNode> actual2 = findDuplicateSubtreesStringFormation(root2);
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
        /**
         * Example 3:
         * Input: root = [2,2,2,3,null,3,null]
         * Output: [[2,3],[3]]
         *                       2
         *                      / \
         *                     2   2
         *                    /   /
         *                   3    3
         */
        TreeNode root3 = new TreeNode(2);
        root3.left = new TreeNode(2);
        root3.right = new TreeNode(2);
        root3.left.left = new TreeNode(3);
        root3.right.left = new TreeNode(3);
        List<TreeNode> expected3 = List.of(root3.right.left, root3.right);
        List<TreeNode> actual3 = findDuplicateSubtreesStringFormation(root3);
        assertEquals(expected3.size(), actual3.size());
        for (int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i), actual3.get(i));
        }
    }

    @Test
    public void findDuplicateSubtreesSerialIdTest() {
        /**
         * Example 1:
         * Input: root = [1,2,3,4,null,2,4,null,null,4]
         * Output: [[2,4],[4]]
         *                       1
         *                      / \
         *                     2   3
         *                    /   / \
         *                   4   2   4
         *                      /
         *                     4
         */
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.right.left = new TreeNode(2);
        root1.right.right = new TreeNode(4);
        root1.right.left.left = new TreeNode(4);
        List<TreeNode> expected1 = List.of(root1.right.left.left, root1.right.left);
        List<TreeNode> actual1 = findDuplicateSubtreesSerialId(root1);
        assertEquals(expected1.size(), actual1.size());
        for (int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input: root = [2,1,1]
         * Output: [[1]]
         *             2
         *            / \
         *           1   1
         */
        TreeNode root2 = new TreeNode(2);
        root2.left = new TreeNode(1);
        root2.right = new TreeNode(1);
        List<TreeNode> expected2 = List.of(root2.right);
        List<TreeNode> actual2 = findDuplicateSubtreesSerialId(root2);
        assertEquals(expected2.size(), actual2.size());
        for (int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
        /**
         * Example 3:
         * Input: root = [2,2,2,3,null,3,null]
         * Output: [[2,3],[3]]
         *                       2
         *                      / \
         *                     2   2
         *                    /   /
         *                   3    3
         */
        TreeNode root3 = new TreeNode(2);
        root3.left = new TreeNode(2);
        root3.right = new TreeNode(2);
        root3.left.left = new TreeNode(3);
        root3.right.left = new TreeNode(3);
        List<TreeNode> expected3 = List.of(root3.right.left, root3.right);
        List<TreeNode> actual3 = findDuplicateSubtreesSerialId(root3);
        assertEquals(expected3.size(), actual3.size());
        for (int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i), actual3.get(i));
        }
    }

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }
}
