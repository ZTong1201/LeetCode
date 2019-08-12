import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class reconstructItinerary {

    /**
     * Given a list of airline tickets represented by pairs of departure and arrival airports [from, to], reconstruct the
     * itinerary in order. All of the tickets belong to a man who departs from JFK. Thus, the itinerary must begin with JFK.
     *
     * Note:
     *
     * If there are multiple valid itineraries, you should return the itinerary that has the smallest lexical order when read as
     * a single string. For example, the itinerary ["JFK", "LGA"] has a smaller lexical order than ["JFK", "LGB"].
     * All airports are represented by three capital letters (IATA code).
     * You may assume all tickets form at least one valid itinerary.
     *
     * Approach: DFS and reverse
     * 本质上，此题想要贪心地每次都访问其lexical order最小的相邻节点，若一直采取这种策略可以经过所有edge，那么此条通路就是最终结果。但若每次访问lexical
     * order最小的节点，很有可能得到思路。此时需要回溯到上一个节点，继续查看它时候还有其他相邻节点。而之前所找到的"死路"就需要放在整个结果的最后。因为此题
     * 输入保证了一定有一条valid通路。那么当一侧走到死路时，只有可能另一侧有一个"环"，可以访问其他节点一圈后再回到此条死路的开始节点。所以整个过程很像对树
     * 的后序遍历。先访问其所有孩子节点（对二叉树而言是从左至右，对于此题是lexical order从小到大），最后访问parent节点。而整个结果就是后序遍历结果的反序，
     * 因为若lexical order较小的节点那一侧存在"环"，那么这条通路需要在最终结果的前面，但在遍历时，这条"环"是在"死路"之后遍历，因此需要反序输出。
     *
     * 需要注意的是，为了避免重复遍历，当遍历完一条边之后，直接将其从图中移除。即在对应source节点的相邻节点中，删除该节点即可
     *
     * Time: O(NlogN) 对于每个起点机场，都要对其相邻节点进行排序，所以总的时间应该是所有排序的加和。upper bound为O(NlogN)
     * Space: O(N) 需要一个hash map将所有的边存下来
     */
    public List<String> findItinerary(List<List<String>> tickets) {
        Map<String, LinkedList<String>> graph = new HashMap<>();
        buildGraph(graph, tickets);
        List<String> res = new ArrayList<>();
        dfs(graph, res, "JFK");
        //最终结果即是后序遍历结果的反序
        Collections.reverse(res);
        return res;
    }

    private void dfs(Map<String, LinkedList<String>> graph, List<String> res, String source) {
        //找到起始节点的所有相邻节点
        LinkedList<String> neighbors = graph.getOrDefault(source, new LinkedList<>());
        while(!neighbors.isEmpty()) {
            //按照lexical order依次遍历，每遍历到一条边，就要把它从图中移除
            String nei = neighbors.removeFirst();
            //按深度继续搜索其后续节点
            //用递归函数，可以在当前通路未能遍历所有边时，回溯到上一个有另一条通路的节点
            dfs(graph, res, nei);
        }
        //按照后序遍历思想，当遍历完所有孩子节点后，才能将该节点放入list中
        res.add(source);
    }

    private void buildGraph(Map<String, LinkedList<String>> graph, List<List<String>> tickets) {
        for(List<String> ticket : tickets) {
            graph.putIfAbsent(ticket.get(0), new LinkedList<>());
            graph.get(ticket.get(0)).addLast(ticket.get(1));
        }
        for(String key : graph.keySet()) {
            Collections.sort(graph.get(key));
        }
    }


    @Test
    public void findItineraryTest() {
        /**
         * Example 1:
         * Input: [["MUC", "LHR"], ["JFK", "MUC"], ["SFO", "SJC"], ["LHR", "SFO"]]
         * Output: ["JFK", "MUC", "LHR", "SFO", "SJC"]
         */
        List<List<String>> tickets1 = new ArrayList<>();
        tickets1.add(new ArrayList<>(Arrays.asList("MUC", "LHR")));
        tickets1.add(new ArrayList<>(Arrays.asList("JFK", "MUC")));
        tickets1.add(new ArrayList<>(Arrays.asList("SFO", "SJC")));
        tickets1.add(new ArrayList<>(Arrays.asList("LHR", "SFO")));
        List<String> actual1 = findItinerary(tickets1);
        List<String> expected1 = new ArrayList<>(Arrays.asList("JFK", "MUC", "LHR", "SFO", "SJC"));
        assertEquals(expected1.size(), actual1.size());
        for(int i = 0; i < expected1.size(); i++) {
            assertEquals(expected1.get(i), actual1.get(i));
        }
        /**
         * Example 2:
         * Input: [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
         * Output: ["JFK","ATL","JFK","SFO","ATL","SFO"]
         * Explanation: Another possible reconstruction is ["JFK","SFO","ATL","JFK","ATL","SFO"].
         *              But it is larger in lexical order.
         */
        List<List<String>> tickets2 = new ArrayList<>();
        tickets2.add(new ArrayList<>(Arrays.asList("JFK", "SFO")));
        tickets2.add(new ArrayList<>(Arrays.asList("JFK", "ATL")));
        tickets2.add(new ArrayList<>(Arrays.asList("SFO", "ATL")));
        tickets2.add(new ArrayList<>(Arrays.asList("ATL", "JFK")));
        tickets2.add(new ArrayList<>(Arrays.asList("ATL", "SFO")));
        List<String> actual2 = findItinerary(tickets2);
        List<String> expected2 = new ArrayList<>(Arrays.asList("JFK", "ATL", "JFK", "SFO", "ATL", "SFO"));
        assertEquals(expected2.size(), actual2.size());
        for(int i = 0; i < expected2.size(); i++) {
            assertEquals(expected2.get(i), actual2.get(i));
        }
        /**
         * Example 3:
         * Input: [["JFK","KUL"],["JFK","NRT"],["NRT","JFK"]]
         * Output: ["JFK", "NRT", "JFK", "KUL"]
         * Explanation: It is not possible to find an itinerary to traverse KUL first (KUL has a smaller lexical order than NRT).
         */
        List<List<String>> tickets3 = new ArrayList<>();
        tickets3.add(new ArrayList<>(Arrays.asList("JFK", "KUL")));
        tickets3.add(new ArrayList<>(Arrays.asList("JFK", "NRT")));
        tickets3.add(new ArrayList<>(Arrays.asList("NRT", "JFK")));
        List<String> actual3 = findItinerary(tickets3);
        List<String> expected3 = new ArrayList<>(Arrays.asList("JFK", "NRT", "JFK", "KUL"));
        assertEquals(expected3.size(), actual3.size());
        for(int i = 0; i < expected3.size(); i++) {
            assertEquals(expected3.get(i), actual3.get(i));
        }
    }
}
