import org.junit.Test;
import static org.junit.Assert.*;

public class serializationTest extends serializationDFS {

    @Test
    public void serializationDFSTest() {
        /**
         * Example:
         * You may serialize the following tree:
         *
         *     1
         *    / \
         *   2   3
         *      / \
         *     4   5
         *
         * as "1 2 # # 3 4 5 # # "
         */
        serializationDFS serialization = new serializationDFS();
        TreeNode expected = new TreeNode(1);
        expected.left = new TreeNode(2);
        expected.right = new TreeNode(3);
        expected.right.left = new TreeNode(4);
        expected.right.right = new TreeNode(5);
        TreeNode actual = serialization.deserialize(serialization.serialize(expected));
        assertTrue(isSameTree(expected, actual));
    }

    @Test
    public void serializationBFSTest() {
        /**
         * Example:
         * You may serialize the following tree:
         *
         *     1
         *    / \
         *   2   3
         *      / \
         *     4   5
         *
         * as "1 2 # # 3 4 5 # # "
         */
        serializationBFS serialization = new serializationBFS();
        TreeNode expected = new TreeNode(1);
        expected.left = new TreeNode(2);
        expected.right = new TreeNode(3);
        expected.right.left = new TreeNode(4);
        expected.right.right = new TreeNode(5);
        TreeNode actual = serialization.deserialize(serialization.serialize(expected));
        assertTrue(isSameTree(expected, actual));
    }


    private boolean isSameTree(TreeNode p, TreeNode q) {
        if(p == null && q == null) return true;
        if(p == null || q == null) return false;
        if(p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}
