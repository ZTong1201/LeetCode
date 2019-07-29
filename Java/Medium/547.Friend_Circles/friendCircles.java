import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class friendCircles {

    /**
     * There are N students in a class. Some of them are friends, while some are not. Their friendship is transitive in nature.
     * For example, if A is a direct friend of B, and B is a direct friend of C, then A is an indirect friend of C.
     * And we defined a friend circle is a group of students who are direct or indirect friends.
     *
     * Given a N*N matrix M representing the friend relationship between students in the class.
     * If M[i][j] = 1, then the ith and jth students are direct friends with each other, otherwise not.
     * And you have to output the total number of friend circles among all the students.
     *
     * Approach 1: Depth-First Search (DFS)
     *
     * The problem is very much like leetcode 200. The matrix is a symmetric matrix. Hence we can only care about the row, since it indicates
     * each different student. For a given student, we could use a boolean array to check whether we have visited before. If not, we will
     * find a new friend circle for that student. To avoid recounting, for each student, we can mark their direct friends as visited by
     * DFS. The only thing we need to consider is that for any of his direct friends, we will repeat the same DFS process to mark visited
     * for his indirect friend.
     *
     * Time: O(n^2) we need to traverse each node in the matrix
     * Space: O(2n) = O(n) we need a boolean array to record visited student, and the call stack may require up to O(n) space as well.
     */
    public int findCircleNumDFS(int[][] M) {
        //a boolean array to record visited students
        boolean[] visited = new boolean[M.length];
        int count = 0;
        for(int i = 0; i < M.length; i++) {
            //for each student, if we haven't visited before
            //we will find a new friend circle from him
            if(!visited[i]) {
                count++;
                //then we need to find all his direct friends to avoid recounting
                dfs(M, visited, i);
            }
        }
        return count;
    }

    private void dfs(int[][] M, boolean[] visited, int i) {
        //check all his direct friends
        for(int j = 0; j < M.length; j++) {
            if(M[i][j] == 1 && !visited[j]) {
                //mark all the direct friends as visited
                visited[j] = true;
                //then do the same thing for his direct friends
                //i.e. mark all his indirect friends as visited
                dfs(M, visited, j);
            }
        }
    }

    /**
     * Approach 2: Breadth-First Search (BFS)
     * We can also implement BFS to traverse within a connected component by using a queue. At a given node, we add all the adjacent nodes
     * to the queue for further searching. The property of a queue will let us always visit the nodes has the smallest distance to that
     * node first. If we found any connected nodes, we mark all of them as visited. By traversing the whole matrix and count the number
     * of unvisited students (nodes), we found the number of friend circles.
     *
     * Time: O(n^2) we still need to traverse all the nodes
     */
    public int findCircleNumBFS(int[][] M) {
        boolean[] visited = new boolean[M.length];
        //we need a queue to implement BFS
        Queue<Integer> queue = new LinkedList<>();
        int count = 0;
        //loop through each student
        for(int i = 0; i < M.length; i++) {
            //if we haven't visited the student, we can find a new friend circle now
            if(!visited[i]) {
                queue.add(i);
                count++;
                //then we need to do BFS for this student (node)
                while(!queue.isEmpty()) {
                    int curr = queue.poll();
                    //mark current student as visited
                    visited[curr] = true;
                    //check all his direct friends
                    for(int j = 0; j < M.length; j++) {
                        //if we found a new (unvisited) direct friend of current student
                        if(M[curr][j] == 1 && !visited[j]) {
                            //we need to add this new student in the queue
                            //to search for any indirect friends of the current student
                            queue.add(j);
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * Approach 3: Union Find
     * We can convert the matrix into a disjoint set. Then we can traverse the entire matrix, and if we found that any two students are
     * friends, we union them in our disjoint set. Finally, these students will form some connected components. The number of connected
     * components is just the number of friend circles.
     *
     * If using path compression and union by rank (size), the union runtime will be amortized O(a(n)) runtime, which is typically less
     * than 5 in practice, we can treat it as O(1)
     *
     * Time: O(n^2) for each entry in the matrix, if the value is 1, we need to do a union operation
     * Space: O(2n), we need two arrays to store the size of current tree, and the parent node
     */
    public int findCircleNumUnionFind(int[][] M) {
        unionFind uf = new unionFind(M.length);
        //traverse the entire matrix and union any two students who are direct friends
        for(int i = 0; i < M.length; i++) {
            for(int j = 0; j < M.length; j++) {
                if(M[i][j] == 1 && i != j) {
                    uf.union(i, j);
                }
            }
        }
        //then we check how many connected components we have
        //i.e. we count the number of root nodes
        int count = 0;
        for(int k = 0; k < M.length; k++) {
            if(uf.parent[k] == k) {
                count++;
            }
        }
        return count;
    }

    private class unionFind {
        private int[] size;
        public int[] parent;

        public unionFind(int length) {
            this.size = new int[length];
            this.parent = new int[length];
            for(int i = 0; i < length; i++) {
                //set all nodes' parents to itself
                this.parent[i] = i;
            }
            //set all single trees of size 1
            Arrays.fill(this.size, 1);
        }

        //find the root value of the given node
        private int root(int i) {
            while(parent[i] != i) { //as long as the parent value is not itself, it is not the root node
                //path compression, since it is not a root node, we assign the parent node of its parent node as its new parent node
                //i.e. we move current node one level up, the tree is more flattened
                parent[i] = parent[parent[i]];
                //keep checking the root value of its parent node
                i = parent[i];
            }
            return i;
        }

        //connect two nodes in the disjoint set
        public void union(int i, int j) {
            //first find two root nodes
            int p = root(i);
            int q = root(j);
            //if they are the same, they have already connected, do nothing
            if(p == q) {
                return;
            }
            //otherwise, union them by rank (size)
            //i.e. always connect the smaller tree to the larger tree
            if(size[p] < size[q]) {
                parent[p] = q;
                //remember to update the size of the new tree
                size[q] += size[p];
            } else {
                parent[q] = p;
                size[p] += size[q];
            }
        }
    }


    @Test
    public void findCircleNumTest() {
        /**
         * Example 1:
         * Input:
         * [[1,1,0],
         *  [1,1,0],
         *  [0,0,1]]
         * Output: 2
         * Explanation:The 0th and 1st students are direct friends, so they are in a friend circle.
         * The 2nd student himself is in a friend circle. So return 2.
         */
        int[][] M1 = new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}};
        assertEquals(2, findCircleNumDFS(M1));
        assertEquals(2, findCircleNumBFS(M1));
        assertEquals(2, findCircleNumUnionFind(M1));
        /**
         * Example 2:
         * Input:
         * [[1,1,0],
         *  [1,1,1],
         *  [0,1,1]]
         * Output: 1
         * Explanation:The 0th and 1st students are direct friends, the 1st and 2nd students are direct friends,
         * so the 0th and 2nd students are indirect friends. All of them are in the same friend circle, so return 1.
         */
        int[][] M2 = new int[][]{{1, 1, 0}, {1, 1, 1}, {0, 1, 1}};
        assertEquals(1, findCircleNumDFS(M2));
        assertEquals(1, findCircleNumBFS(M2));
        assertEquals(1, findCircleNumUnionFind(M2));
        /**
         * Example 3:
         * Input:
         * [[1,0,0,1],
         *  [0,1,1,0],
         *  [0,1,1,1],
         *  [1,0,1,1]]
         *  Output: 1
         */
        int[][] M3 = new int[][]{{1, 0, 0, 1}, {0, 1, 1, 0}, {0, 1, 1, 1}, {1, 0, 1, 1}};
        assertEquals(1, findCircleNumDFS(M3));
        assertEquals(1, findCircleNumBFS(M3));
        assertEquals(1, findCircleNumUnionFind(M3));
    }
}
