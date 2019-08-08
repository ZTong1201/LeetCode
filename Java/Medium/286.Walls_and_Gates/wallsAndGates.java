import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class wallsAndGates {

    /**
     * You are given a m x n 2D grid initialized with these three possible values.
     *
     * -1 - A wall or an obstacle.
     * 0 - A gate.
     * INF - Infinity means an empty room. We use the value 231 - 1 = 2147483647 to represent INF as you may assume that the
     * distance to a gate is less than 2147483647.
     * Fill each empty room with the distance to its nearest gate. If it is impossible to reach a gate, it should be filled with INF.
     *
     * Approach 1: BFS from the rooms
     * 对于每一个empty room，将其相邻节点（要判断是否有效，例如是否溢出边界，或相邻节点为-1）存在queue里，同时把对应到相邻节点的距离记录在另一个queue中，若
     * 其相邻节点为0 - gate，则当前距离就是到gate的最短距离，更新当前empty room的最短距离，因为是bfs遍历，因此此时的距离就是最短距离，可以直接停止遍历，更新
     * 结果。为避免重复遍历，每次做bfs之前需要一个二维boolean数组记录某节点是否被遍历
     *
     * Time: O((MN)^2) 矩阵中有MN个节点，最坏情况下，每个节点都要遍历整个矩阵以更新最短距离
     * Space: O(MN)，每次遍历前需要一个二维boolean数组，同时两个queue要存储某节点的相邻节点和到相邻节点的最短距离
     */
    public void wallsAndGates1(int[][] rooms) {
        if(rooms == null || rooms.length == 0) return;
        int row = rooms.length, col = rooms[0].length;
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                if(rooms[i][j] == Integer.MAX_VALUE) {
                    bfs(rooms, i, j);
                }
            }
        }
    }

    private void bfs(int[][] rooms, int i, int j) {
        int[] dr = new int[]{0, 1, 0, -1};
        int[] dc = new int[]{1, 0, -1, 0};
        Queue<int[]> queue = new LinkedList<>();
        Queue<Integer> dist = new LinkedList<>();
        boolean[][] visited = new boolean[rooms.length][rooms[0].length];
        queue.add(new int[]{i, j});
        dist.add(0);
        while(!queue.isEmpty()) {
            int[] curr = queue.poll();
            int currDist = dist.poll();
            if(!visited[curr[0]][curr[1]]) {
                visited[curr[0]][curr[1]] = true;
                if(rooms[curr[0]][curr[1]] == 0) {
                    rooms[i][j] = currDist;
                    return;
                }
                for(int k = 0; k < 4; k++) {
                    int newR = curr[0] + dr[k];
                    int newC = curr[1] + dc[k];
                    if(newR < 0 || newR >= rooms.length || newC < 0 || newC >= rooms[0].length || rooms[newR][newC] == -1) {
                        continue;
                    }
                    queue.add(new int[]{newR, newC});
                    dist.add(currDist + 1);
                }
            }
        }
    }

    @Test
    public void wallsAndGatesTest1() {
        /**
         * Example:
         * Given the 2D grid:
         *
         * INF  -1  0  INF
         * INF INF INF  -1
         * INF  -1 INF  -1
         *   0  -1 INF INF
         * After running your function, the 2D grid should be:
         *
         *   3  -1   0   1
         *   2   2   1  -1
         *   1  -1   2  -1
         *   0  -1   3   4
         */
        int[][] rooms = new int[][]{{Integer.MAX_VALUE, -1, 0, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, -1},
                {Integer.MAX_VALUE, -1, Integer.MAX_VALUE, -1},
                {0, -1, Integer.MAX_VALUE, Integer.MAX_VALUE}};
        int[][] expected = new int[][]{{3, -1, 0, 1}, {2, 2, 1, -1}, {1, -1, 2, -1}, {0, -1, 3, 4}};
        wallsAndGates1(rooms);
        assertArrayEquals(expected, rooms);
    }

    /**
     * Approach 2: BFS from the gate
     * 可以将方法1的BFS反过来遍历，即从所有的gate出发，然后遍历整个矩阵，如果遇到empty room就更新当前最短距离。因为使用BFS，所以保证了遍历d + 1的节点前
     * 一定先遍历所有距离为d的节点，即每个empty room都会更新最小值。同时，不在需要visited矩阵来记录是否遍历某节点，BFS只会更新值为MAX_VALUE的节点，当某
     * 节点被遍历之后，其值已更改，不会再重复遍历。
     *
     * Time: O(MN)，从每个gate出发，每个节点只会被更新一次（即当其位MAX_VALUE时），因此最坏情况就是遍历整个矩阵
     * Space: O(MN)，空间由queue的大小决定，最坏情况下，需要将所有节点放入queue中
     */
    public void wallsAndGates2(int[][] rooms) {
        if(rooms == null || rooms.length == 0) return;
        int row = rooms.length, col = rooms[0].length;
        Queue<int[]> gates = new LinkedList<>();
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                if(rooms[i][j] == 0) {
                    gates.add(new int[]{i, j});
                }
            }
        }
        bfs(rooms, gates);
    }

    private void bfs(int[][] rooms, Queue<int[]> gates) {
        int[] dr = new int[]{0, 1, 0, -1};
        int[] dc = new int[]{1, 0, -1, 0};
        while(!gates.isEmpty()) {
            int[] curr = gates.poll();
            for(int k = 0; k < 4; k++) {
                int newR = curr[0] + dr[k];
                int newC = curr[1] + dc[k];
                //只在下一个节点没有溢出边界，且为距离尚未更新的empty room时，更新当前节点，并将该节点加入队列，以便后续遍历
                if(newR < 0 || newR >= rooms.length || newC < 0 || newC >= rooms[0].length || rooms[newR][newC] != Integer.MAX_VALUE) {
                    continue;
                }
                rooms[newR][newC] = rooms[curr[0]][curr[1]] + 1;
                gates.add(new int[]{newR, newC});
            }
        }
    }

    @Test
    public void wallsAndGatesTest2() {
        /**
         * Example:
         * Given the 2D grid:
         *
         * INF  -1  0  INF
         * INF INF INF  -1
         * INF  -1 INF  -1
         *   0  -1 INF INF
         * After running your function, the 2D grid should be:
         *
         *   3  -1   0   1
         *   2   2   1  -1
         *   1  -1   2  -1
         *   0  -1   3   4
         */
        int[][] rooms = new int[][]{{Integer.MAX_VALUE, -1, 0, Integer.MAX_VALUE},
                {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, -1},
                {Integer.MAX_VALUE, -1, Integer.MAX_VALUE, -1},
                {0, -1, Integer.MAX_VALUE, Integer.MAX_VALUE}};
        int[][] expected = new int[][]{{3, -1, 0, 1}, {2, 2, 1, -1}, {1, -1, 2, -1}, {0, -1, 3, 4}};
        wallsAndGates2(rooms);
        assertArrayEquals(expected, rooms);
    }
}
