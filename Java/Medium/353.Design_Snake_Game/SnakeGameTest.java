import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SnakeGameTest {

    @Test
    public void snakeGameTest() {
        /**
         * Example:
         * Input
         * ["SnakeGame", "move", "move", "move", "move", "move", "move"]
         * [[3, 2, [[1, 2], [0, 1]]], ["R"], ["D"], ["R"], ["U"], ["L"], ["U"]]
         * Output
         * [null, 0, 0, 1, 1, 2, -1]
         *
         * Explanation
         * SnakeGame snakeGame = new SnakeGame(3, 2, [[1, 2], [0, 1]]);
         * snakeGame.move("R"); // return 0
         * snakeGame.move("D"); // return 0
         * snakeGame.move("R"); // return 1, snake eats the first piece of food. The second piece of food appears at (0, 1).
         * snakeGame.move("U"); // return 1
         * snakeGame.move("L"); // return 2, snake eats the second food. No more food appears.
         * snakeGame.move("U"); // return -1, game over because snake collides with border
         */
        SnakeGame snakeGame = new SnakeGame(3, 2, new int[][]{{1, 2}, {0, 1}});
        assertEquals(0, snakeGame.move("R"));
        assertEquals(0, snakeGame.move("D"));
        assertEquals(1, snakeGame.move("R"));
        assertEquals(1, snakeGame.move("U"));
        assertEquals(2, snakeGame.move("L"));
        assertEquals(-1, snakeGame.move("U"));
    }
}
