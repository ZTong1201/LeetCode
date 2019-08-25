import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class rightSideView {

    /**
     * Given a binary tree, imagine yourself standing on the right side of it, return the values of the nodes you can see ordered
     * from top to bottom.
     *
     * Approach 1: DFS
     * 此题可以用dfs来解。本质上，从右侧看某个level的时候，很大概率是该层右子树的节点为需要添加的节点值。因此可以使用dfs先尽可能的遍历右子树，因为对于每一个
     * level都需要添加一个值，同时在dfs过程中还需要记录当前所在的level。若当前level的值等于result list的size，说明需要将此level的最右侧的值放入list中
     * 同时因为先遍历右子树，再遍历左子树的关系，保证了当需要添加值的时候一定是该层最右侧的值
     *
     * Time: O(n) 需要遍历所有节点
     * Space: O(H) 递归深度为树的高度H，call stack需要O(H)的空间来完成dfs
     */
    public List<Integer> rightSideViewDFS(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        dfs(root, 0, res);
        return res;
    }

    private void dfs(TreeNode root, int level, List<Integer> res) {
        //base case，若输入节点为null，则不需要继续遍历
        if(root == null) return;
        //若当前level的值和res的size相同，说明需要将当前level最右侧值加入res中
        //因为先遍历右子树，再遍历左子树的关系，此时放入res的值一定为最右侧的节点值
        if(level == res.size()) {
            res.add(root.val);
        }
        //一定要先遍历右子树再遍历左子树，同时每次遍历时，深度都会增加1
        dfs(root.right, level + 1, res);
        dfs(root.left, level + 1, res);
    }

    @Test
    public void rightSideViewDFSTest() {
        /**
         * Example:
         * Input: [1,2,3,null,5,null,4]
         * Output: [1, 3, 4]
         * Explanation:
         *
         *    1            <---
         *  /   \
         * 2     3         <---
         *  \     \
         *   5     4       <---
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        tree.left.right = new TreeNode(5);
        tree.right.right = new TreeNode(4);
        List<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 4));
        List<Integer> actual = rightSideViewDFS(tree);
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    /**
     * Approach 2: BFS
     * 此题本质还是一个level order traversal，因此只要使用bfs即可分层遍历，在当前层，可以知道这一层的节点个数，只要将最后一个节点（即最右侧节点）放入result
     * list即可，同时对于所有的节点，若其左右孩子不为null，需要将其子节点都加入queue中，以便下一层遍历
     *
     * Time: O(n) 仍然需要遍历所有节点
     * Space: O(n) 因为queue中需要存储每一层的节点总数，若树是完全平衡的，最后一个level的节点数量为N/2，因此queue占用的空间为O(n)
     */
    public List<Integer> rightSideViewBFS(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if(root == null) return res;
        //因为要做bfs，所以需要用一个queue来记录待遍历节点
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            //遍历前，queue记录的是当前层所有节点
            //因此可以通过size找到当前的层的最右侧节点（即queue中的第size - 1个节点）
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                TreeNode curr = queue.poll();
                //只将最右侧节点值加入res
                if(i == size - 1) {
                    res.add(curr.val);
                }
                //同时判断左右孩子是否为null，若不为空则可以加入queue中
                if(curr.left != null) {
                    queue.add(curr.left);
                }
                if(curr.right != null) {
                    queue.add(curr.right);
                }
            }
        }
        return res;
    }

    @Test
    public void rightSideViewBFSTest() {
        /**
         * Example:
         * Input: [1,2,3,null,5,null,4]
         * Output: [1, 3, 4]
         * Explanation:
         *
         *    1            <---
         *  /   \
         * 2     3         <---
         *  \     \
         *   5     4       <---
         */
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        tree.left.right = new TreeNode(5);
        tree.right.right = new TreeNode(4);
        List<Integer> expected = new ArrayList<>(Arrays.asList(1, 3, 4));
        List<Integer> actual = rightSideViewBFS(tree);
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int x) {
            this.val = x;
        }
    }
}
