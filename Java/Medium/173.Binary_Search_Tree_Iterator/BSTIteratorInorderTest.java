import org.junit.Test;
import static org.junit.Assert.*;

public class BSTIteratorInorderTest extends BSTIteratorInorder{

    @Test
    public void BSTIteratorInorderTest() {
        /**
         * Example:
         *            7
         *          /  \
         *         3    15
         *             /  \
         *            9   20
         *
         * BSTIterator iterator = new BSTIterator(root);
         * iterator.next();    // return 3
         * iterator.next();    // return 7
         * iterator.hasNext(); // return true
         * iterator.next();    // return 9
         * iterator.hasNext(); // return true
         * iterator.next();    // return 15
         * iterator.hasNext(); // return true
         * iterator.next();    // return 20
         * iterator.hasNext(); // return false
         */
        TreeNode tree = new TreeNode(7);
        tree.left = new TreeNode(3);
        tree.right = new TreeNode(15);
        tree.right.left = new TreeNode(9);
        tree.right.right = new TreeNode(20);
        BSTIteratorInorder iterator = new BSTIteratorInorder(tree);
        assertEquals(3, iterator.next());
        assertEquals(7, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(9, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(15, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(20, iterator.next());
        assertFalse(iterator.hasNext());
    }
}
