import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapSumTest {

    @Test
    public void mapSumTest() {
        /**
         * Input
         * ["MapSum", "insert", "sum", "insert", "sum"]
         * [[], ["apple", 3], ["ap"], ["app", 2], ["ap"]]
         * Output
         * [null, null, 3, null, 5]
         *
         * Explanation
         * MapSum mapSum = new MapSum();
         * mapSum.insert("apple", 3);
         * mapSum.sum("ap");           // return 3 (apple = 3)
         * mapSum.insert("app", 2);
         * mapSum.sum("ap");           // return 5 (apple + app = 3 + 2 = 5)
         */
        MapSum mapSum = new MapSum();
        mapSum.insert("apple", 3);
        assertEquals(3, mapSum.sum("ap"));
        mapSum.insert("app", 2);
        assertEquals(5, mapSum.sum("ap"));
    }
}
