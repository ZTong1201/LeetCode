import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SnakeGame {

    /**
     * Design a Snake game that is played on a device with screen size height x width. Play the game online if you are not
     * familiar with the game.
     * <p>
     * The snake is initially positioned at the top left corner (0, 0) with a length of 1 unit.
     * <p>
     * You are given an array food where food[i] = (ri, ci) is the row and column position of a piece of food that the snake
     * can eat. When a snake eats a piece of food, its length and the game's score both increase by 1.
     * <p>
     * Each piece of food appears one by one on the screen, meaning the second piece of food will not appear until the snake
     * eats the first piece of food.
     * <p>
     * When a piece of food appears on the screen, it is guaranteed that it will not appear on a block occupied by the snake.
     * <p>
     * The game is over if the snake goes out of bounds (hits a wall) or if its head occupies a space that its body occupies
     * after moving (i.e. a snake of length 4 cannot run into itself).
     * <p>
     * Implement the SnakeGame class:
     * <p>
     * SnakeGame(int width, int height, int[][] food) Initializes the object with a screen of size height x width and the
     * positions of the food.
     * int move(String direction) Returns the score of the game after applying one direction move by the snake. If the game
     * is over, return -1.
     * <p>
     * Constraints:
     * <p>
     * 1 <= width, height <= 10^4
     * 1 <= food.length <= 50
     * food[i].length == 2
     * 0 <= ri < height
     * 0 <= ci < width
     * direction.length == 1
     * direction is 'U', 'D', 'L', or 'R'.
     * At most 10^4 calls will be made to move.
     * <p>
     * Approach: Deque
     * The key part of this problem is how to keep track of the snake and update snake accordingly when a move is made.
     * Basically, we need a data structure to keep growing and can somewhat handle both the head and tail operations.
     * Why? Because the snake length can never decrease, and when a snake moves, there are two scenarios:
     * 1. If it is a food move, then we basically move the snake head to the food position, and the rest of the snake body
     * should remain the same
     * 2. If it is not a food move, then the head will still be updated, and the previous tail will vanish.
     * In order to better keep track of the head and the tail information, we consider using a deque data structure such that
     * the operations on head and tail are both O(1).
     * Then another tricky part is, how to we know the snake doesn't bite itself after the new move. Based on the problem
     * statement, the food will not appear on the snake body, so we don't need to worry about this edge case when it is
     * a food move. If it's not a food move, after removing the old tail, we basically check whether the new head will touch
     * any cell of the snake body, this will take O(n) time if we loop through the entire deque. Therefore, we can use a
     * hash set to record all cell IDs on the snake path and check whether the snake bites itself in O(1) runtime.
     * <p>
     * Time:
     * move(): O(1) all operations are in O(1) time
     * Space: O(1) since the deque can only be at most 50
     */
    private final int width, height;
    private final int[][] food;
    private final Deque<Integer> snake;
    private final Set<Integer> snakePositions;
    private int foodIndex, score;

    public SnakeGame(int width, int height, int[][] food) {
        this.width = width;
        this.height = height;
        this.food = food;
        this.snake = new LinkedList<>();
        // since the snake starts at (0, 0), add the first position into the deque & set
        this.snake.add(0);
        this.snakePositions = new HashSet<>();
        this.snakePositions.add(0);
        this.foodIndex = 0;
        this.score = 0;
    }

    public int move(String direction) {
        // get the id of current snake head
        int currHeadId = snake.peekFirst();
        // convert to (row, col) coordinate
        int nextHeadRow = currHeadId / width, nextHeadCol = currHeadId % width;

        // update the head coordinate based on the move direction
        switch (direction) {
            case "U": {
                nextHeadRow--;
                break;
            }
            case "D": {
                nextHeadRow++;
                break;
            }
            case "L": {
                nextHeadCol--;
                break;
            }
            case "R": {
                nextHeadCol++;
                break;
            }
        }

        // move the snake to its correct position
        moveSnake(nextHeadRow, nextHeadCol);
        // after the snake has been moved - check whether the game is over
        // if it is over, then return -1
        if (isInvalid(nextHeadRow, nextHeadCol)) return -1;

        // now add the new head position at the front of the deque
        int nextHeadId = nextHeadRow * width + nextHeadCol;
        snake.addFirst(nextHeadId);
        snakePositions.add(nextHeadId);

        // if the game is not over, return the score
        return score;
    }

    private boolean isInvalid(int nextHeadRow, int nextHeadCol) {
        boolean outWidth = nextHeadCol < 0 || nextHeadCol >= width;
        boolean outHeight = nextHeadRow < 0 || nextHeadRow >= height;
        int nextHeadId = nextHeadRow * width + nextHeadCol;
        // if the new head position is already on the snake body
        // this is an invalid move
        boolean biteItself = snakePositions.contains(nextHeadId);
        // either move out of the grid or the snake bite itself, the game is over
        return outWidth || outHeight || biteItself;
    }

    private void moveSnake(int nextHeadRow, int nextHeadCol) {
        // if it's a food move, then we only need to update the head
        // the snake body will remain the same
        if ((foodIndex < food.length) && (nextHeadRow == food[foodIndex][0] && nextHeadCol == food[foodIndex][1])) {
            // move to the next food
            foodIndex++;
            // increment the score
            score++;
        } else {
            // otherwise, it's not a food move
            // we need to remove the tail from both the deque and the set
            int currId = snake.pollLast();
            snakePositions.remove(currId);
        }
    }
}
