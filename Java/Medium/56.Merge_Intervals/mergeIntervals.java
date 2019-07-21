import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class mergeIntervals {

    /**
     * Given a collection of intervals, merge all overlapping intervals.
     * Approach 1: Sorting
     * Sorting the initial intervals, then all overlapping intervals will be close to each other. As we iterate over the whole interval
     * array, if they overlaps, update the end time, otherwise, add it directly into result
     *
     * Time: O(nlogn) for typical sorting algorithm
     * Space: O(n) depends on sorting algorithm, it cost O(1) or O(n) space. we need a linked list to contain intervals and then convert
     *       it back to 2-d array
     */
    public int[][] mergeSorting(int[][] intervals) {
        //the reason use a linked list is to get the last element in the list in O(1) time
        LinkedList<int[]> res = new LinkedList<>();
        //lambda comparator function for sorting intervals
        Comparator<int[]> intervalComparator = (int[] a, int[] b) -> {
            return a[0] - b[0];
        };
        Arrays.sort(intervals, intervalComparator);
        for(int[] interval : intervals) {
            //if it is the first interval or the top interval has no overlaps with the current interval we care, add it to the list
            if(res.isEmpty() || interval[0] > res.getLast()[1]) {
                res.add(interval);
            } else {
                //otherwise, they overlap, update the ending time with the maximum
                res.getLast()[1] = Math.max(res.getLast()[1], interval[1]);
            }
        }
        return listToArray(res);
    }

    private int[][] listToArray(LinkedList<int[]> res) {
        int length = res.size();
        int[][] arr = new int[length][2];
        for(int i = 0; i < length; i++) {
            arr[i] = res.removeFirst();
        }
        return arr;
    }

    @Test
    public void mergeSortingTest() {
        /**
         * Example 1:
         * Input: [[1,3],[2,6],[8,10],[15,18]]
         * Output: [[1,6],[8,10],[15,18]]
         * Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
         */
        int[][] intervals1 = new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] expected1 = new int[][]{{1, 6}, {8, 10}, {15, 18}};
        int[][] actual1 = mergeSorting(intervals1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: [[1,4],[4,5]]
         * Output: [[1,5]]
         * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
         */
        int[][] intervals2 = new int[][]{{1, 4}, {4, 5}};
        int[][] expected2 = new int[][]{{1, 5}};
        int[][] actual2 = mergeSorting(intervals2);
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: [[1,4],[2,3]]
         * Output: [[1,4]]
         * Explanation: Since intervals [1,4] and [2,3] overlaps, merge them into [1,4].
         */
        int[][] intervals3 = new int[][]{{1, 4}, {2, 3}};
        int[][] expected3 = new int[][]{{1, 4}};
        int[][] actual3 = mergeSorting(intervals3);
        assertArrayEquals(expected3, actual3);
    }

    /**
     * Approach 2: Priority Queue
     * If sorting is not allowed, simply put all the elements in the min priority queue, and each time remove the smallest element from it
     *
     * Time: O(nlogn)
     * Space: O(2n) a minPQ requires O(n) space and the result list requires O(n) space as well
     */
    public int[][] mergePQ(int[][] intervals) {
        LinkedList<int[]> res = new LinkedList<>();
        Comparator<int[]> intervalComparator = (int[] a, int[] b) -> {
            return a[0] - b[0];
        };
        PriorityQueue<int[]> minPQ = new PriorityQueue<>(intervalComparator);
        for(int[] interval : intervals) {
            minPQ.add(interval);
        }
        while(!minPQ.isEmpty()) {
            int[] interval = minPQ.poll();
            if(res.isEmpty() || interval[0] > res.getLast()[1]) {
                res.add(interval);
            } else {
                res.getLast()[1] = Math.max(res.getLast()[1], interval[1]);
            }
        }
        return listToArray(res);
    }

    @Test
    public void mergePQTest() {
        /**
         * Example 1:
         * Input: [[1,3],[2,6],[8,10],[15,18]]
         * Output: [[1,6],[8,10],[15,18]]
         * Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
         */
        int[][] intervals1 = new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] expected1 = new int[][]{{1, 6}, {8, 10}, {15, 18}};
        int[][] actual1 = mergePQ(intervals1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: [[1,4],[4,5]]
         * Output: [[1,5]]
         * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
         */
        int[][] intervals2 = new int[][]{{1, 4}, {4, 5}};
        int[][] expected2 = new int[][]{{1, 5}};
        int[][] actual2 = mergePQ(intervals2);
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: [[1,4],[2,3]]
         * Output: [[1,4]]
         * Explanation: Since intervals [1,4] and [2,3] overlaps, merge them into [1,4].
         */
        int[][] intervals3 = new int[][]{{1, 4}, {2, 3}};
        int[][] expected3 = new int[][]{{1, 4}};
        int[][] actual3 = mergePQ(intervals3);
        assertArrayEquals(expected3, actual3);
    }

    /**
     * Approach 3: Connected Component
     * Construct a graph based on the intervals. Treat each interval as a node in the graph, if two intervals have overlaps, there is
     * an unweighted edge connect these two nodes. After constructing the graph, and connect nodes which overlap with each other. We simply
     * merge the intervals within each connected component with the minimum start time and the maximum end time.
     *
     * Time: O(n^2) build the graph cost O(V + E) = O(n) + O(n^2) = O(n^2) since in the worst case, all the intervals overlap with each other, we need
     *      add edges between each node. Traversal costs the same time. Merge intervals within each connected component cost O(V) = O(n) time
     *      Overall, it is O(n^2)
     * Space: O(n^2), the graph hash map will cost O(n^2) if all the intervals overlap, which will dominate the space complexity.
     */
    private Map<int[], List<int[]>> graph;
    private Map<Integer, List<int[]>> nodesInComp;
    private Set<int[]> visited;

    //check whether two intervals are overlapped (inclusive)
    private boolean isOverlap(int[] a, int[] b) {
        return a[0] <= b[1] && b[0] <= a[1];
    }

    //build the graph, i.e. if either two intervals are overlapped, connect an edge between them
    private void buildGraph(int[][] intervals) {
        graph = new HashMap<>();

        for(int[] interval : intervals) {
            graph.put(interval, new ArrayList<>());
        }

        for(int[] interval1 : intervals) {
            for(int[] interval2 : intervals) {
                if(isOverlap(interval1, interval2)) {
                    //note that the edges are bidirectional
                    graph.get(interval1).add(interval2);
                    graph.get(interval2).add(interval1);
                }
            }
        }
    }

    //build connected components, i.e. for each interval, we implement dfs to find all the overlapped intervals
    //to avoid revisiting, use a hash set to store visited intervals
    private void buildComponent(int[][] intervals) {
        //each connected component has an index which starting from 0
        visited = new HashSet<>();
        nodesInComp = new HashMap<>();
        int compNumber = 0;

        for(int[] interval : intervals) {
            //only search those nodes who haven't been visited
            if(!visited.contains(interval)) {
                //implement dfs to build current connected component
                dfs(interval, compNumber);
                //after this component has been built, increment the component number
                compNumber += 1;
            }
        }
    }

    private void dfs(int[] start, int compNumber) {
        //to implement dfs, we need a stack to store nodes to be visited
        Stack<int[]> stack = new Stack<>();
        stack.push(start);

        while(!stack.isEmpty()) {
            //current interval we car about
            int[] node = stack.pop();
            //only visit the node if it is unvisited
            if(!visited.contains(node)) {
                //add the node into the set since we have visited
                visited.add(node);

                //if nothing in the current component, add a new empty list to it
                if(nodesInComp.get(compNumber) == null) {
                    nodesInComp.put(compNumber, new ArrayList<>());
                }
                //add nodes into the component
                nodesInComp.get(compNumber).add(node);

                //implement dfs by adding all nodes with an edge in between
                for(int[] child : graph.get(node)) {
                    stack.push(child);
                }
            }
        }
    }

    //merge all the intervals into the broadest one within the connected component
    private int[] mergeNodes(List<int[]> nodes) {

        int minStart = Integer.MAX_VALUE, maxEnd = Integer.MIN_VALUE;

        for(int[] node : nodes) {
            minStart = Math.min(minStart, node[0]);
            maxEnd = Math.max(maxEnd, node[1]);
        }

        return new int[]{minStart, maxEnd};
    }

    public int[][] mergeConnectedComponent(int[][] intervals) {
        //first, build graph;
        buildGraph(intervals);
        //then, build connected components
        //each connected components has a index starting from 0
        buildComponent(intervals);

        List<int[]> res = new ArrayList<>();
        //iterate over all the connected components and the merge all the intervals in it
        for(int comp = 0; comp < nodesInComp.size(); comp++) {
            res.add(mergeNodes(nodesInComp.get(comp)));
        }
        return listToArray(res);
    }


    private int[][] listToArray(List<int[]> res) {
        int length = res.size();
        int[][] arr = new int[length][2];
        for(int i = 0; i < length; i++) {
            arr[i] = res.get(i);
        }
        return arr;
    }


    @Test
    public void mergeConnectedComponentTest() {
        /**
         * Example 1:
         * Input: [[1,3],[2,6],[8,10],[15,18]]
         * Output: [[1,6],[8,10],[15,18]]
         * Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
         */
        int[][] intervals1 = new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] expected1 = new int[][]{{1, 6}, {8, 10}, {15, 18}};
        int[][] actual1 = mergeConnectedComponent(intervals1);
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: [[1,4],[4,5]]
         * Output: [[1,5]]
         * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
         */
        int[][] intervals2 = new int[][]{{1, 4}, {4, 5}};
        int[][] expected2 = new int[][]{{1, 5}};
        int[][] actual2 = mergeConnectedComponent(intervals2);
        assertArrayEquals(expected2, actual2);
        /**
         * Example 3:
         * Input: [[1,4],[2,3]]
         * Output: [[1,4]]
         * Explanation: Since intervals [1,4] and [2,3] overlaps, merge them into [1,4].
         */
        int[][] intervals3 = new int[][]{{1, 4}, {2, 3}};
        int[][] expected3 = new int[][]{{1, 4}};
        int[][] actual3 = mergeConnectedComponent(intervals3);
        assertArrayEquals(expected3, actual3);
    }
}
