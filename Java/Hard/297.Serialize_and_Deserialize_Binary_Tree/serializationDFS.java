import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class serializationDFS {

    /**
     * Serialization is the process of converting a data structure or object into a sequence of bits so that it can be stored in a file
     * or memory buffer, or transmitted across a network connection link to be reconstructed later in the same or another computer
     * environment.
     *
     * Design an algorithm to serialize and deserialize a binary tree. There is no restriction on how your serialization/deserialization
     * algorithm should work. You just need to ensure that a binary tree can be serialized to a string and this string can be
     * deserialized to the original tree structure.
     */

    /**
     * Example:
     *
     * You may serialize the following tree:
     *
     *     1
     *    / \
     *   2   3
     *      / \
     *     4   5
     *
     * as "[1,2,3,null,null,4,5]"
     * Clarification: The above format is the same as how LeetCode serializes a binary tree. You do not necessarily need to follow
     * this format, so please be creative and come up with different approaches yourself.
     *
     * Note: Do not use class member/global/static variables to store states. Your serialize and deserialize algorithms should be stateless.
     *
     * Approach 1: Preorder Traversal (DFS)
     * 本质上，就是对整个树做前序遍历。同时把null节点也记录下来。为了减少空间使用，可以用"#"代替null节点，然后再每个节点之间加上空格以示区分，因为可能有
     * 数字有多位的情况。
     *
     * Time: O(N) 无论是序列化还是去序列化，都需要遍历所有节点（包括空节点）
     * Space: O(N) 递归深度由树的高度决定，最坏情况为O(N)，同时序列化的string包含了所有节点，包括空节点，数量级也为O(N)
     */
    //Encodes a tree to a single string
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        preorder(root, sb);
        return sb.toString();
    }

    private void preorder(TreeNode root, StringBuilder sb) {
        if(root == null) {
            //若遇到空节点，需要在结果中插入"#"代替null，同时在后面加上空格以示与其他节点区分
            sb.append("# ");
            return;
        } else {
            //否则，该节点不为空，把节点值加入结果中
            sb.append(root.val + " ");
            preorder(root.left, sb);
            preorder(root.right, sb);
        }
    }

    //Decodes your encoded data to tree
    public TreeNode deserialize(String data) {
        //首先将整个string以空格分开，每个部分就是一个节点，再将其加入一个链表，每次从链表的头部拿出节点，构建当前节点
        LinkedList<String> nodes = new LinkedList<>(Arrays.asList(data.split((" "))));
        return buildTree(nodes);
    }

    private TreeNode buildTree(LinkedList<String> nodes) {
        //从链表头部拿出当前节点
        String node = nodes.removeFirst();
        //若当前节点值为"#"，说明是个空节点，返回null
        if(node.equals("#")) {
            return null;
        } else {
            //否则以该节点值构建当前节点，然后递归的调用其左子树和右子树
            TreeNode res = new TreeNode(Integer.valueOf(node));
            res.left = buildTree(nodes);
            res.right = buildTree(nodes);
            return res;
        }
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            this.val = x;
        }
    }
}
