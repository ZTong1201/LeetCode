import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CBTInserterTest {

    @Test
    public void cbtInserterTest1() {
        /**
         * Example 1:
         * Input
         * ["CBTInserter", "insert", "insert", "get_root"]
         * [[[1, 2]], [3], [4], []]
         * Output
         * [null, 1, 2, [1, 2, 3, 4]]
         *
         * Explanation
         * CBTInserter cBTInserter = new CBTInserter([1, 2]);
         * cBTInserter.insert(3);  // return 1
         * cBTInserter.insert(4);  // return 2
         * cBTInserter.get_root(); // return [1, 2, 3, 4]
         *               1
         *              /
         *             2
         *  =>
         *              1
         *             / \
         *            2   3
         *  =>
         *              1
         *             / \
         *            2   3
         *           /
         *          4
         */
        CBTInserter.TreeNode root = new CBTInserter.TreeNode(1);
        root.left = new CBTInserter.TreeNode(2);
        CBTInserter cbtInserter = new CBTInserter(root);
        assertEquals(1, cbtInserter.insert(3));
        assertEquals(2, cbtInserter.insert(4));
        CBTInserter.TreeNode CBTRoot = cbtInserter.get_root();
        assertEquals(3, CBTRoot.right.val);
        assertEquals(4, CBTRoot.left.left.val);
    }

    @Test
    public void cbtInserterTest2() {
        /**
         * Example 2:
         * Input
         * ["CBTInserter", "insert", "insert", "insert", "get_root"]
         * [[[1]], [2], [3], [4], []]
         * Output
         * [null, 1, 1, 2, [1, 2, 3, 4]]
         *
         * Explanation
         * CBTInserter cBTInserter = new CBTInserter([1]);
         * cBTInserter.insert(2);  // return 1
         * cBTInserter.insert(3);  // return 1
         * cBTInserter.insert(4);  // return 2
         * cBTInserter.get_root(); // return [1, 2, 3, 4]
         *               1
         *  =>
         *              1
         *             /
         *            2
         *  =>
         *              1
         *             / \
         *            2   3
         *  =>
         *              1
         *             / \
         *            2   3
         *           /
         *          4
         */
        CBTInserter.TreeNode root = new CBTInserter.TreeNode(1);
        CBTInserter cbtInserter = new CBTInserter(root);
        assertEquals(1, cbtInserter.insert(2));
        assertEquals(1, cbtInserter.insert(3));
        assertEquals(2, cbtInserter.insert(4));
        CBTInserter.TreeNode CBTRoot = cbtInserter.get_root();
        assertEquals(2, CBTRoot.left.val);
        assertEquals(3, CBTRoot.right.val);
        assertEquals(4, CBTRoot.left.left.val);
    }
}
