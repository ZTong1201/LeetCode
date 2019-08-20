import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class shortestDistance {

    /**
     * You want to build a house on an empty land which reaches all buildings in the shortest amount of distance. You can only
     * move up, down, left and right. You are given a 2D grid of values 0, 1 or 2, where:
     *
     * Each 0 marks an empty land which you can pass by freely.
     * Each 1 marks a building which you cannot pass through.
     * Each 2 marks an obstacle which you cannot pass through.
     *
     * Note:
     * There will be at least one building. If it is not possible to build such house according to the above rules, return -1.
     *
     * Approach 1: BFS from empty land
     * 可以先遍历一遍数组，记录下总共的building个数。然后对于每一个empty land，做BFS遍历整个数组。若遇到building（1）就更新当前的最短距离和，并减少
     * 所需要遍历的building总数。若最后该empty land能遍历所有building（即最后building个数为0），则返回记录下来的最短距离和。若不能遍历所有building，
     * 此次遍历结果为MAX_VALUE。最终返回每个empty遍历返回的最小值，若最小值仍为MAX_VALUE，则说明没有任何的empty land能遍历所有节点，返回-1
     *
     * Time: O((mn)^2) 对于所有的empty land，最坏情况下都需要遍历整个图，最坏情况下有mn - 1个empty land，所以总的运行时间为O((mn)^2)
     * Space: O(mn)，对于每次遍历，需要一个二维boolean数组记录访问过的节点避免重复遍历。同时需要两个queue来记录待遍历节点，最坏情况下也可能需要O(mn)
     */
    public int shortestDistanceBFS1(int[][] grid) {
        int res = Integer.MAX_VALUE;
        int numBuildings = 0;
        //先遍历一遍数组，记录building个数
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == 1) {
                    numBuildings++;
                }
            }
        }

        //再遍历一次数组，若遇到empty land，则从当前位置做bfs，记录最短距离和
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == 0) {
                    res = Math.min(res, bfs(grid, i, j, numBuildings));
                }
            }
        }
        return res == Integer.MAX_VALUE ? -1 : res;
    }

    private int bfs(int[][] grid, int row, int col, int numBuildings) {
        int res = 0;
        int[] dr = new int[]{0, 1, 0, -1};
        int[] dc = new int[]{1, 0, -1, 0};
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        visited[row][col] = true;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});
        Queue<Integer> dist = new LinkedList<>();
        dist.add(0);

        while(!queue.isEmpty()) {
            int[] curr = queue.poll();
            int currDist = dist.poll();
            for(int k = 0; k < 4; k++) {
                int newR = curr[0] + dr[k];
                int newC = curr[1] + dc[k];
                if(newR < 0 || newC < 0 || newR >= grid.length || newC >= grid[0].length) {
                    continue;
                } else {
                    if(!visited[newR][newC]) {
                        visited[newR][newC] = true;
                        if(grid[newR][newC] == 1) {
                            numBuildings -= 1;
                            res += currDist + 1;
                        } else if(grid[newR][newC] == 0) {
                            queue.add(new int[]{newR, newC});
                            dist.add(currDist + 1);
                        }
                    }
                }
            }
        }
        return numBuildings == 0 ? res : Integer.MAX_VALUE;
    }

    @Test
    public void shortestDistanceBFS1Test() {
        /**
         * Example 2:
         * Input: [[1,0,2,0,1],[0,0,0,0,0],[0,0,1,0,0]]
         *
         * 1 - 0 - 2 - 0 - 1
         * |   |   |   |   |
         * 0 - 0 - 0 - 0 - 0
         * |   |   |   |   |
         * 0 - 0 - 1 - 0 - 0
         *
         * Output: 7
         *
         * Explanation: Given three buildings at (0,0), (0,4), (2,2), and an obstacle at (0,2),
         *              the point (1,2) is an ideal empty land to build a house, as the total
         *              travel distance of 3+3+1=7 is minimal. So return 7.
         */
        int[][] grid1 = new int[][]{{1, 0, 2, 0, 1}, {0, 0, 0, 0, 0}, {0, 0, 1, 0, 0}};
        assertEquals(7, shortestDistanceBFS1(grid1));
        /**
         * Example 2:
         * Input: [[1,1,1,1,1,0],[0,0,0,0,0,1],[0,1,1,0,0,1],[1,0,0,1,0,1],[1,0,1,0,0,1],[1,0,0,0,0,1],[0,1,1,1,1,0]]
         *
         * Output: 88
         */
        int[][] grid2 = new int[][]{
                {1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 1},
                {0, 1, 1, 0, 0, 1},
                {1, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 1},
                {0, 1, 1, 1, 1, 0}};
        assertEquals(88, shortestDistanceBFS1(grid2));
    }

    /**
     * Approach 2: BFS from buildings
     * 本质与方法1相同，若building数目远远小于empty lands时，从building出发会大大减小运行时间。在遍历数组的过程中，若遇到building（1），则从该位置开始
     * 做BFS，将所有可以到达的empty land（0）的最短距离进行更新，记在另一个distance二维数组中。在对所有的building进行完BFS后，遍历一遍distance数组，
     * 找到在原grid中为0的所有位置，并判断该位置是否所有building都能reach，然后更新最小值。为了判断每个位置能被building reach的个数，需要另一个二维数组。
     *
     * Time: O((mn)^2) 最坏情况下仍为O((mn)^2)时间，因为对于每个building也可能需要遍历整个数组
     * Space: O(mn)，需要两个二维数组记录每个位置的最短距离和，以及每个位置能reach的building数。同时在每次遍历时，都需要一个二维boolean数组记录访问过的
     *        节点
     */
    public int shortestDistanceBFS2(int[][] grid) {
        Queue<int[]> queue = new LinkedList<>();
        int numBuildings = 0;
        int row = grid.length, col = grid[0].length;
        int[][] reach = new int[row][col];
        int[][] distance = new int[row][col];
        int[] dr = new int[]{0, 1, 0, -1};
        int[] dc = new int[]{1, 0, -1, 0};

        //遍历整个grid，若遇到building（1），则进行BFS遍历，更新最短距离和
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                if(grid[i][j] == 1) {
                    numBuildings++;
                    boolean[][] visited = new boolean[row][col];
                    queue.add(new int[]{i, j});
                    //记录当前的遍历层数
                    int level = 1;

                    while(!queue.isEmpty()) {
                        int size = queue.size();

                        for(int k = 0; k < size; k++) {
                            int[] curr = queue.poll();
                            for(int l = 0; l < 4; l++) {
                                int newR = curr[0] + dr[l];
                                int newC = curr[1] + dc[l];
                                //若待遍历左边没有溢出边界，没有被访问过，同时可以通过（即在原grid中为0）
                                if(newR >= 0 && newC >=0 && newR < row && newC < col && !visited[newR][newC] && grid[newR][newC] == 0) {
                                    //那么遍历该节点，更新当前节点的最短距离和，可到达building树，并将节点放入队列以待后续遍历
                                    visited[newR][newC] = true;
                                    distance[newR][newC] += level;
                                    reach[newR][newC] += 1;
                                    queue.add(new int[]{newR, newC});
                                }
                            }
                        }
                        //下一循环开始前记得将层级 + 1
                        level++;
                    }
                }
            }
        }


        //BFS结束后，需要对distance进行遍历，找到所有符合条件的empty land，更新最短距离和
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                if(grid[i][j] == 0 && reach[i][j] == numBuildings) {
                    min = Math.min(min, distance[i][j]);
                }
            }
        }
        //若没有符合要求的empty land，min不会更新，返回-1
        return min == Integer.MAX_VALUE ? -1 : min;
    }


    @Test
    public void shortestDistanceBFS2Test() {
        /**
         * Example 2:
         * Input: [[1,0,2,0,1],[0,0,0,0,0],[0,0,1,0,0]]
         *
         * 1 - 0 - 2 - 0 - 1
         * |   |   |   |   |
         * 0 - 0 - 0 - 0 - 0
         * |   |   |   |   |
         * 0 - 0 - 1 - 0 - 0
         *
         * Output: 7
         *
         * Explanation: Given three buildings at (0,0), (0,4), (2,2), and an obstacle at (0,2),
         *              the point (1,2) is an ideal empty land to build a house, as the total
         *              travel distance of 3+3+1=7 is minimal. So return 7.
         */
        int[][] grid1 = new int[][]{{1, 0, 2, 0, 1}, {0, 0, 0, 0, 0}, {0, 0, 1, 0, 0}};
        assertEquals(7, shortestDistanceBFS2(grid1));
        /**
         * Example 2:
         * Input: [[1,1,1,1,1,0],[0,0,0,0,0,1],[0,1,1,0,0,1],[1,0,0,1,0,1],[1,0,1,0,0,1],[1,0,0,0,0,1],[0,1,1,1,1,0]]
         *
         * Output: 88
         */
        int[][] grid2 = new int[][]{
                {1, 1, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 1},
                {0, 1, 1, 0, 0, 1},
                {1, 0, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 1},
                {1, 0, 0, 0, 0, 1},
                {0, 1, 1, 1, 1, 0}};
        assertEquals(88, shortestDistanceBFS2(grid2));
    }
}
