import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;

public class evaluateDivision {

    /**
     * Equations are given in the format A / B = k, where A and B are variables represented as strings, and k is a real number
     * (floating point number). Given some queries, return the answers. If the answer does not exist, return -1.0.
     * <p>
     * Example:
     * Given a / b = 2.0, b / c = 3.0.
     * queries are: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ? .
     * return [6.0, 0.5, -1.0, 1.0, -1.0].
     * <p>
     * The input is: vector<pair<string, string>> equations, vector<double>& values, vector<pair<string, string>> queries ,
     * where equations.size() == values.size(), and the values are positive. This represents the equations. Return vector<double>.
     * <p>
     * The input is always valid. You may assume that evaluating the queries will result in no division by zero and
     * there is no contradiction.
     * <p>
     * Approach 1: BFS
     * 此题是在寻找图中两节点的连通路径。首先要将输入转化为一个weighted有向图，即若给定a/b = 2.0，则有一条有向边从a指向b，weight为2.0，同时要再加另一条
     * 从b指向a的边，weight为1/2.0，当图构建好以后，就可以对输入的query中的两个节点进行查找。
     * base case为
     * 1.当两节点为相同节点，返回1.0
     * 2.当两节点有任意节点不在图中，则为无效query，返回-1.0
     * 对于剩下的情况，只需对图做BFS遍历即可。为保证BFS遍历，需要两个queue，一个记录节点，一个记录到当前节点的division值，另外需要一个set来记录遍历过的
     * 节点，避免重复遍历.
     * <p>
     * Time: O(E + Q*E) 若equation的size为E，query的size为Q，首先建图需要O(E)时间，同时每次query，最坏情况下需要O(E)时间，即查询的两个节点位于图的
     * 两端。
     * Space: O(E)，无论是图的简历，两个queue，还是一个hash set，都需要最多O(N)空间
     */
    public double[] calcEquationBFS(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> graph = new HashMap<>();
        buildGraph(equations, values, graph);
        double[] res = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            res[i] = queryBFS(graph, queries.get(i));
        }
        return res;
    }


    private double queryBFS(Map<String, Map<String, Double>> graph, List<String> query) {
        String numerator = query.get(0);
        String denominator = query.get(1);
        // edge case 1 - if either numerator or denominator is not in the graph
        // return -1.0
        if (!graph.containsKey(numerator) || !graph.containsKey(denominator)) {
            return -1.0;
        } else if (numerator.equals(denominator)) {
            // edge case 2 - return 1.0 if numerator equals to denominator
            return 1.0;
        } else {
            Set<String> seen = new HashSet<>();
            // use BFS to query
            Queue<String> queue1 = new LinkedList<>();
            Queue<Double> queue2 = new LinkedList<>();
            queue1.add(numerator);
            // need to keep track of the value until current search
            queue2.add(1.0);

            while (!queue1.isEmpty()) {
                String curr = queue1.poll();
                double num = queue2.poll();
                // avoid duplicate visit
                if (!seen.contains(curr)) {
                    seen.add(curr);

                    // search the neighbors
                    for (String neighbor : graph.get(curr).keySet()) {
                        // if we found the target - return the desired value
                        if (neighbor.equals(denominator)) {
                            num *= graph.get(curr).get(neighbor);
                            return num;
                        } else {
                            // otherwise, add the neighbor into the queue for further search
                            queue1.add(neighbor);
                            queue2.add(num * graph.get(curr).get(neighbor));
                        }
                    }
                }
            }
            // if we cannot find the value for current query - return -1.0
            return -1.0;
        }
    }

    private void buildGraph(List<List<String>> equations, double[] values, Map<String, Map<String, Double>> graph) {
        for (int i = 0; i < values.length; i++) {
            String numerator = equations.get(i).get(0);
            String denominator = equations.get(i).get(1);
            graph.putIfAbsent(numerator, new HashMap<>());
            graph.put(denominator, new HashMap<>());
            graph.get(numerator).put(denominator, values[i]);
            graph.get(denominator).put(numerator, 1.0 / values[i]);
        }
    }

    /**
     * Approach 2: DFS
     * 此题遍历也可以用DFS。base case为当A == B时，返回1.0
     * 若想查询A/B, 当A != B时，则需要继续访问A的相邻节点，不妨设为C，则dfs会继续查询C/B。因此当新查询不违反继续遍历规则（即新节点未被访问），则最终会
     * 返回一个正数（因为给定的值都为正数），所以只要能有正数返回，说明要么hit base case，返回1，要么call stack不断向上返回新计算的值。
     * 那么A / B = C / B * A / C，即新结果为前一步查询的C / B的值乘上graph里记录的A / C的值即可。
     * <p>
     * Time: O(E + Q*E) 虽然遍历方式不同，但运行时间完全相同，最坏情况下每次query都要遍历所有的equation
     * Space: O(E) 虽然省去了两个queue，但是hash set和graph的建立均无变化，同时call stack也可能会需要O(N)的空间
     */
    public double[] calcEquationDFS(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> graph = new HashMap<>();
        buildGraph(equations, values, graph);
        double[] res = new double[queries.size()];
        for (int i = 0; i < res.length; i++) {
            String numerator = queries.get(i).get(0);
            String denominator = queries.get(i).get(1);
            if (!graph.containsKey(numerator) || !graph.containsKey(denominator)) {
                res[i] = -1.0;
            } else {
                Set<String> visited = new HashSet<>();
                res[i] = queryDFS(numerator, denominator, graph, visited);
            }
        }
        return res;
    }


    //compute A / B
    private double queryDFS(String A, String B, Map<String, Map<String, Double>> graph, Set<String> visited) {
        //base case，若A,B相等，返回1.0
        if (A.equals(B)) {
            return 1.0;
        }
        visited.add(A);
        //遍历当前节点的相邻节点
        for (String C : graph.get(A).keySet()) {
            //跳过已遍历节点
            if (visited.contains(C)) continue;
            double d = queryDFS(C, B, graph, visited); //继续递归调用函数，计算C / B

            //若query的节点一直在图内，最后会返回正数
            //A / B = C / B * A / C
            if (d > 0) return d * graph.get(A).get(C);
        }
        return -1.0;
    }

    @Test
    public void calcEquationTest() {
        /**
         * Example:
         * According to the example above:
         * equations = [ ["a", "b"], ["b", "c"] ],
         * values = [2.0, 3.0],
         * queries = [ ["a", "c"], ["b", "a"], ["a", "e"], ["a", "a"], ["x", "x"] ].
         * return [6.0, 0.5, -1.0, 1.0, -1.0 ]
         */
        List<List<String>> equations = new ArrayList<>();
        equations.add(new ArrayList<>(Arrays.asList("a", "b")));
        equations.add(new ArrayList<>(Arrays.asList("b", "c")));
        double[] values = new double[]{2.0, 3.0};
        List<List<String>> queries = new ArrayList<>();
        queries.add(new ArrayList<>(Arrays.asList("a", "c")));
        queries.add(new ArrayList<>(Arrays.asList("b", "a")));
        queries.add(new ArrayList<>(Arrays.asList("a", "e")));
        queries.add(new ArrayList<>(Arrays.asList("a", "a")));
        queries.add(new ArrayList<>(Arrays.asList("x", "x")));
        double[] expected = new double[]{6.0, 0.5, -1.0, 1.0, -1.0};
        double[] actual1 = calcEquationBFS(equations, values, queries);
        double[] actual2 = calcEquationDFS(equations, values, queries);
        assertArrayEquals(expected, actual1, 0);
        assertArrayEquals(expected, actual2, 0);
    }
}
