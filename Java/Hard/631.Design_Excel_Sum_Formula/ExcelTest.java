import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExcelTest {

    @Test
    public void excelTest() {
        /**
         * Example 1:
         * Input
         * ["Excel", "set", "sum", "set", "get"]
         * [[3, "C"], [1, "A", 2], [3, "C", ["A1", "A1:B2"]], [2, "B", 2], [3, "C"]]
         * Output
         * [null, null, 4, null, 6]
         *
         * Explanation
         * Excel excel = new Excel(3, "C");
         *  // construct a 3*3 2D array with all zero.
         *  //   A B C
         *  // 1 0 0 0
         *  // 2 0 0 0
         *  // 3 0 0 0
         * excel.set(1, "A", 2);
         *  // set mat[1]["A"] to be 2.
         *  //   A B C
         *  // 1 2 0 0
         *  // 2 0 0 0
         *  // 3 0 0 0
         * excel.sum(3, "C", ["A1", "A1:B2"]); // return 4
         *  // set mat[3]["C"] to be the sum of value at mat[1]["A"] and the values sum of the rectangle range whose
         *  top-left cell is mat[1]["A"] and bottom-right cell is mat[2]["B"].
         *  //   A B C
         *  // 1 2 0 0
         *  // 2 0 0 0
         *  // 3 0 0 4
         * excel.set(2, "B", 2);
         *  // set mat[2]["B"] to be 2. Note mat[3]["C"] should also be changed.
         *  //   A B C
         *  // 1 2 0 0
         *  // 2 0 2 0
         *  // 3 0 0 6
         * excel.get(3, "C"); // return 6
         */
        Excel excel = new Excel(3, 'C');
        excel.set(1, 'A', 2);
        assertEquals(4, excel.sum(3, 'C', new String[]{"A1", "A1:B2"}));
        excel.set(2, 'B', 2);
        assertEquals(6, excel.get(3, 'C'));
        /**
         * Example 2:
         * Input
         * ["Excel","set","set","sum","sum"]
         * [[26,"Z"],[1,"A",1],[1,"I",1],[7,"D",["A1:D6","A1:G3","A1:C12"]],[10,"G",["A1:D7","D1:F10","D3:I8","I1:I9"]]]
         * Output
         * [null,null,null,3,11]
         */
        excel = new Excel(26, 'Z');
        excel.set(1, 'A', 1);
        excel.set(1, 'I', 1);
        assertEquals(3, excel.sum(7, 'D', new String[]{"A1:D6", "A1:G3", "A1:C12"}));
        assertEquals(11, excel.sum(10, 'G', new String[]{"A1:D7", "D1:F10", "D3:I8", "I1:I9"}));
    }
}
