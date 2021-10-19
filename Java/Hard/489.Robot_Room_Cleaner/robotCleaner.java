import java.util.HashSet;
import java.util.Set;

public class robotCleaner {

    /**
     * Given a robot cleaner in a room modeled as a grid.
     * Each cell in the grid can be empty or blocked.
     * The robot cleaner with 4 given APIs can move forward, turn left or turn right. Each turn it made is 90 degrees.
     * When it tries to move into a blocked cell, its bumper sensor detects the obstacle and it stays on the current cell.
     * Design an algorithm to clean the entire room using only the 4 given APIs shown below.
     * interface Robot {
     * //returns true if next cell is open and robot moves into the cell.
     * //returns false if next cell is obstacle and robot stays on the current cell.
     * boolean move();
     * //Robot will stay on the same cell after calling turnLeft/turnRight. Each turn will be 90 degrees.
     * void turnLeft();
     * void turnRight();
     * <p>
     * //Clean the current cell.
     * void clean();
     * }
     * <p>
     * Notes:
     * <p>
     * The input is only given to initialize the room and the robot's position internally. You must solve this problem "blindfolded".
     * In other words, you must control the robot using only the mentioned 4 APIs, without knowing the room layout and the initial robot's
     * position.
     * The robot's initial position will always be in an accessible cell.
     * The initial direction of the robot will be facing up.
     * All accessible cells are connected, which means the all cells marked as 1 will be accessible by the robot.
     * Assume all four edges of the grid are all surrounded by wall.
     * <p>
     * Approach: DFS
     * 本质上，是要对整个room的所有accessible cell进行一次遍历。为了避免重复遍历，需要用hashset将已遍历节点记录下来。因为无法直接获得整个room的信息，因此不妨
     * 将起始位置看作是坐标原点（0，0），robot只能沿四个方向行走，分别对于x坐标或y坐标的变化。因此，在任意一个位置，该robot有4个备选方向可以走，不妨按顺时针方向
     * 将四个direction标记下来（即0 - up，1 - right， 2 - down， 3 - left）。因为要进行dfs遍历，所以若当前的方向可以继续前进，就前进一格，并将新位置清扫，
     * 再将所经过的格子记录下来，后续将其视作不可达到的cell即可。若沿着当前方向走到死路（走到room边界，或前方是障碍物），那么按照顺时针不断移动90度，探索下一个方向
     * 若该格子四个方向都是死路，说明需要回退到上一个格子，看在上一个格子的位置调转方向能否继续遍历，若仍不能，就一直回退（backtrack）直到有新的方向可走。若最终
     * 不再有方向可走，说明所有accessible格子已被清扫完毕。
     * <p>
     * 如何回退？
     * 回退时，只需要将robot调转180度（即两次turn right），然后移动一格（move），即回到上一个格子，但此时方向需要回到原始方向，即再次将robot调转180度。
     * 每次探寻新方向时，只需要不断顺时针转动90度即可（即一次turn right）
     * <p>
     * Time: O(N) N是accessible格子的数量，对于每一个格子，都只需要判断四个方向 O(4N) = O(N)
     * Space: O(N) 需要记录下所有经过的accessible格子
     */
    private Set<String> visited;

    public void cleanRoom(Robot robot) {
        //将当前格子在grid中的坐标以i，j的字符串形式记录下来
        visited = new HashSet<>();
        cleanRoomRecursive(0, 0, 0, robot);
    }

    private void cleanRoomRecursive(int currRow, int currCol, int direction, Robot robot) {
        //将当前坐标记录成字符串
        String currLoc = currRow + "," + currCol;
        //若当前位置已被遍历，可将其视为inaccessible的格子，直接返回
        if (visited.contains(currLoc)) return;
        visited.add(currLoc);
        //清扫当前位置
        robot.clean();
        //对于每一个格子，都有四个方向需要判断
        for (int k = 0; k < 4; k++) {
            //若当前方向可以继续向前走，就沿着该方向做dfs遍历
            if (robot.move()) {
                //记录下当前位置
                int nextRow = currRow, nextCol = currCol;
                //根据当前robot的方向，得到robot移动到下一个格子的坐标
                switch (direction) {
                    case 0: { // 0 - up
                        nextRow--;
                        break;
                    }
                    case 1: { // 1 - right
                        nextCol++;
                        break;
                    }
                    case 2: { // 2 - down
                        nextRow++;
                        break;
                    }
                    case 3: { // 3 - left
                        nextCol--;
                        break;
                    }
                }
                //得到新坐标后，继续沿着当前方向进行dfs遍历
                cleanRoomRecursive(nextRow, nextCol, direction, robot);
                //若当前格子的四个方向都已走到死路，需要backtrack回到上一个格子，然后调转到下一个方向，判断是否可以继续遍历
                goBack(robot);
            }
            //若当前方向无法继续前进，考虑顺时针调转90度，遍历下一个方向
            robot.turnRight();
            direction = (direction + 1) % 4;
        }
    }

    private void goBack(Robot robot) {
        //先将robot调转180度
        robot.turnRight();
        robot.turnRight();

        //回退到上一格子
        robot.move();

        //重新调转180度，回到初始方向
        robot.turnLeft();
        robot.turnLeft();
    }

    /**
     * Example:
     * Input:
     * room = [
     *   [1,1,1,1,1,0,1,1],
     *   [1,1,1,1,1,0,1,1],
     *   [1,0,1,1,1,1,1,1],
     *   [0,0,0,1,0,0,0,0],
     *   [1,1,1,1,1,1,1,1]
     * ],
     * row = 1,
     * col = 3
     *
     * Explanation:
     * All grids in the room are marked by either 0 or 1.
     * 0 means the cell is blocked, while 1 means the cell is accessible.
     * The robot initially starts at the position of row=1, col=3.
     * From the top left corner, its position is one row below and three columns right.
     */
}
