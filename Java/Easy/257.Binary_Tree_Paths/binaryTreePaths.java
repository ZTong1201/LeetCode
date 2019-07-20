import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class binaryTreePaths {

    /**
     * Given a binary tree, return all root-to-leaf paths.
     *
     * Note: A leaf is a node with no children.
     *
     * Approach 1: Depth-First Search (Recursion)
     *
     * Time: O(N) visit all the nodes once
     * Space: O(2H), where H is the height of the tree, the call stack will require O(H) space, and the list contains node value
     *        will require at most O(H) space. In the worst case, it would be O(N)
     */
    public List<String> binaryTreePathsRecursive(TreeNode root) {
        List<String> res = new ArrayList<>();
        List<String> path = new ArrayList<>();
        dfs(res, root, path);
        return res;
    }

    private void dfs(List<String> res, TreeNode root, List<String> path) {
        if(root != null) { //only visit non-null nodes
            //add current node value to the path list
            path.add(String.valueOf(root.val));
            //if we reach a leaf node
            if(root.left == null && root.right == null) {
                //add path to the final list
                res.add(String.join("->", path));
            }
            //keep searching recursively
            dfs(res, root.left, path);
            dfs(res, root.right, path);
            //before we back to the previous stage, we pop the last element out of the path list (backtrack)
            path.remove(path.size() - 1);
        }
    }

    /**
     * Approach 2: Using String (Recursive)
     * When using string to store path state, if the call stack backtrack the previous stage, the string value will back to the previous
     * stage, no need to care about pop the last element out
     *
     * Time: O(N) visit all the nodes once
     * Space: O(H), where H is the height of the tree, the call stack will require O(H) space
     */
    public List<String> binaryTreePathsString(TreeNode root) {
        List<String> res = new ArrayList<>();
        dfs(res, root, "");
        return res;
    }

    private void dfs(List<String> res, TreeNode root, String path) {
        if(root != null) {
            path += String.valueOf(root.val);
            if(root.left == null && root.right == null) {
                res.add(path);
            } else {
                path += "->";
                dfs(res, root.left, path);
                dfs(res, root.right, path);
            }
        }
    }

    /**
     * Approach 3: Depth-First Search (Iteration)
     * Convert the DFS process into an iteration. Using a stack to store tree nodes, and another stack to store path so far.
     * If the node is a leaf node, add the current path to the final result list
     *
     * Time: O(N) visit all the nodes once
     * Space: O(2H), where H is the height of the tree, we need to stacks to store tree nodes and the current path, which will both require
     *        up to O(H) space
     */
    public List<String> binaryTreePathsIterative(TreeNode root) {
        List<String> res = new ArrayList<>();
        if(root == null) return res;
        Stack<TreeNode> nodeStack = new Stack<>();
        Stack<String> pathStack = new Stack<>();
        nodeStack.push(root);
        pathStack.push(String.valueOf(root.val));

        while(!nodeStack.isEmpty()) {
            TreeNode curr = nodeStack.pop();
            String path = pathStack.pop();
            if(curr.left == null && curr.right == null) {
                res.add(path);
            }
            if(curr.right != null) {
                nodeStack.push(curr.right);
                //when do string concatenation, no need to convert integer values into a string
                pathStack.push(path + "->" + curr.right.val);
            }
            if(curr.left != null) {
                nodeStack.push(curr.left);
                pathStack.push(path + "->" + curr.left.val);
            }
        }
        return res;
    }

    @Test
    public void binaryTreePathsStringTest() {
        /**
         * Input:
         *
         *    1
         *  /   \
         * 2     3
         *  \
         *   5
         *
         * Output: ["1->2->5", "1->3"]
         *
         * Explanation: All root-to-leaf paths are: 1->2->5, 1->3
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        tree.left.right = new TreeNode(5);
        List<String> actual = binaryTreePathsString(tree);
        List<String> expected = new ArrayList<>();
        expected.add("1->2->5");
        expected.add("1->3");
        assertEquals(actual.size(), expected.size());
        for(int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
    }

    @Test
    public void binaryTreePathsRecursiveTest() {
        /**
         * Input:
         *
         *    1
         *  /   \
         * 2     3
         *  \
         *   5
         *
         * Output: ["1->2->5", "1->3"]
         *
         * Explanation: All root-to-leaf paths are: 1->2->5, 1->3
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        tree.left.right = new TreeNode(5);
        List<String> actual = binaryTreePathsRecursive(tree);
        List<String> expected = new ArrayList<>();
        expected.add("1->2->5");
        expected.add("1->3");
        assertEquals(actual.size(), expected.size());
        for(int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
    }

    @Test
    public void binaryTreePathsIterativeTest() {
        /**
         * Input:
         *
         *    1
         *  /   \
         * 2     3
         *  \
         *   5
         *
         * Output: ["1->2->5", "1->3"]
         *
         * Explanation: All root-to-leaf paths are: 1->2->5, 1->3
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        tree.left.right = new TreeNode(5);
        List<String> actual = binaryTreePathsIterative(tree);
        List<String> expected = new ArrayList<>();
        expected.add("1->2->5");
        expected.add("1->3");
        assertEquals(actual.size(), expected.size());
        for(int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), expected.get(i));
        }
    }


    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }
}
