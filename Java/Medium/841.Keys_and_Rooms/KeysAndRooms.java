import org.junit.Test;

import java.util.List;
import java.util.Stack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KeysAndRooms {

    /**
     * There are n rooms labeled from 0 to n - 1 and all the rooms are locked except for room 0. Your goal is to visit
     * all the rooms. However, you cannot enter a locked room without having its key.
     * <p>
     * When you visit a room, you may find a set of distinct keys in it. Each key has a number on it, denoting which
     * room it unlocks, and you can take all of them with you to unlock the other rooms.
     * <p>
     * Given an array rooms where rooms[i] is the set of keys that you can obtain if you visited room i, return true
     * if you can visit all the rooms, or false otherwise.
     * <p>
     * Constraints:
     * <p>
     * n == rooms.length
     * 2 <= n <= 1000
     * 0 <= rooms[i].length <= 1000
     * 1 <= sum(rooms[i].length) <= 3000
     * 0 <= rooms[i][j] < n
     * All the values of rooms[i] are unique.
     * <p>
     * Approach: DFS
     * <p>
     * Time: O(N * M) where N is the length of rooms list, M is the average length of rooms.get(i)
     * Space: O(N * M) need to keep track of visited rooms
     */
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        boolean[] visited = new boolean[n];
        // always starts from room 0
        visited[0] = true;
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        // count the number of visited rooms
        int count = 1;

        while (!stack.isEmpty()) {
            int curr = stack.pop();
            for (int neighbor : rooms.get(curr)) {
                // for all unvisited neighbors
                if (!visited[neighbor]) {
                    // mark it as visited to avoid revisiting
                    visited[neighbor] = true;
                    // push it to the stack
                    stack.push(neighbor);
                    // increment count
                    count++;
                }
            }
        }
        // check whether all rooms have been visited
        return count == n;
    }

    @Test
    public void canVisitAllRoomsTest() {
        /**
         * Example 1:
         * Input: rooms = [[1],[2],[3],[]]
         * Output: true
         * Explanation:
         * We visit room 0 and pick up key 1.
         * We then visit room 1 and pick up key 2.
         * We then visit room 2 and pick up key 3.
         * We then visit room 3.
         * Since we were able to visit every room, we return true.
         */
        List<List<Integer>> rooms1 = List.of(List.of(1), List.of(2), List.of(3), List.of());
        assertTrue(canVisitAllRooms(rooms1));
        /**
         * Example 2:
         * Input: rooms = [[1,3],[3,0,1],[2],[0]]
         * Output: false
         * Explanation: We can not enter room number 2 since the only key that unlocks it is in that room.
         */
        List<List<Integer>> rooms2 = List.of(List.of(1, 3), List.of(3, 0, 1), List.of(2), List.of(0));
        assertFalse(canVisitAllRooms(rooms2));
    }
}
