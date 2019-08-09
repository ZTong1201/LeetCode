import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class alienDictionary {

    /**
     * There is a new alien language which uses the latin alphabet. However, the order among letters are unknown to you. You receive a
     * list of non-empty words from the dictionary, where words are sorted lexicographically by the rules of this new language.
     * Derive the order of letters in this language.
     * Note:
     *
     * You may assume all letters are in lowercase.
     * You may assume that if a is a prefix of b, then a must appear before b in the given dictionary.
     * If the order is invalid, return an empty string.
     * There may be multiple valid order of letters, return any one of them is fine.
     *
     * Approach: Topological Sorting (BFS)
     * 此题本质上是一个拓扑排序。首先结果的字母顺序是由相邻的两个单词中第一个不相同的字符决定的，例如通过"wrt"和"wrf"，只能判断出f应排在t之后，同理
     * "ett"和"rftt"只能判断出r排在e之后。因此我们可以通过找寻两个单词之间的第一个不同字符，建立字符与字符之间的邻接图。
     * 此题需要一个hash map映射两个字符的先后关系，key比对应的value要在最终结果中更靠前。同时还要统计每个字符的入度。
     * 拓扑排序的基本过程是，先将所有入度为0的节点加入结果中，它们可以为任意顺序，然后对于所有后续相邻节点，假想把初始节点拿掉后，每个后续相邻的节点的入度都
     * 会减1，若此时有节点入度为0，说明它们为新的起始节点，可以加入最终结果。本质上是一个BFS
     *
     * 若出现图中有环的情况，则拓扑排序不能进行，当图中有环时，总会有节点的入度不能减少为0。所以若最终结果的字符不等于给定的字典里的，则返回空字符串。
     *
     * Time: O(NL) N为字符串个数，L为最长字符串的长度。最坏情况下，需要遍历每个字符串的每个字符来构建邻接图和入度表，上限为O(NL)
     * Space: O(N) 因为每搜索一个字符串，总会找出一对节点。所以遍历N个字符串，最多会放入N对节点。同时入度表需要常数空间，queue最坏情况下也需要O(N)空间
     */
    public String alienOrder(String[] words) {
        //用hash map来建立邻接表
        Map<Character, Set<Character>> graph = new HashMap<>();
        //因为输入都是小写字母，可以用一个size为26的数组储存每个字符的入度
        int[] indegree = new int[26];
        //根据输入数组，建立邻接图和入度表
        buildGraph(graph, words, indegree);
        //对图进行BFS拓扑排序，若能排序成功则返回排序结果，若不能，则返回""
        return bfs(graph, indegree);
    }

    private void buildGraph(Map<Character, Set<Character>> graph, String[] words, int[] indegree) {
        //首先对输入数组中所有出现过的字符建立一个hashset，用来存其相邻节点
        for(String s : words) {
            for(char c : s.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
            }
        }
        //遍历字符串，将当前字符串与前一字符串对比，找到第一个不相同的字符，建立两字符之间的邻接图，同时更新入度
        //注意不能重复计算入度，所以若在之前的搜索中发现相同的pair，则不更新。找到一个不同的字符后，直接跳出循环，后续的字符顺序无意义
        for(int i = 1; i < words.length; i++) {
            String first = words[i - 1];
            String second = words[i];
            //注意搜查边界为短的字符串长度
            int minLen = Math.min(first.length(), second.length());
            for(int j = 0; j < minLen; j++) {
                char out = first.charAt(j);
                char in = second.charAt(j);
                //找到第一个不同的字符
                if(out != in) {
                    //只计算unique的pair，避免重复计算入度
                    if(!graph.get(out).contains(in)) {
                        graph.get(out).add(in);
                        indegree[in - 'a'] += 1;
                    }
                    //找到不同字符后，跳出循环
                    break;
                }
            }
        }
    }

    private String bfs(Map<Character, Set<Character>> graph, int[] indegree) {
        //用BFS进行拓扑排序，需要一个queue
        Queue<Character> queue = new LinkedList<>();
        //用stringbuilder记录最终结果
        StringBuilder sb = new StringBuilder();
        //注意不是所有入度为0的点都是起点，只有在图中出现的字符，且其入度为0，才加入queue中
        for(char c : graph.keySet()) {
            if(indegree[c - 'a'] == 0) {
                queue.add(c);
            }
        }

        //进行BFS
        while(!queue.isEmpty()) {
            //只有入度为0时才会入队列，所以出队列的元素可以直接加入最终结果
            char out = queue.poll();
            sb.append(out);
            //访问其相邻节点，并将其所有相邻节点入度减1
            for(char in : graph.get(out)) {
                indegree[in - 'a'] -= 1;

                //若某节点入度为0，则变为新的起点，将其加入队列即可
                if(indegree[in - 'a'] == 0) {
                    queue.add(in);
                }
            }
        }
        //若图中有环，则总有节点入度不能0，拓扑排序无法完成，所以最终结果的长度少于输入的字符数量
        //此时返回空字符串
        return sb.length() == graph.size() ? sb.toString() : "";
    }

    @Test
    public void alienOrderTest() {
        /**
         * Example 1:
         * Input:
         * [
         *   "wrt",
         *   "wrf",
         *   "er",
         *   "ett",
         *   "rftt"
         * ]
         *
         * Output: "wertf"
         */
        String[] words1 = new String[]{"wrt", "wrf", "er", "ett", "rftt"};
        assertEquals("wertf", alienOrder(words1));
        /**
         * Example 2:
         * Input:
         * [
         *   "z",
         *   "x"
         * ]
         *
         * Output: "zx"
         */
        String[] words2 = new String[]{"z", "x"};
        assertEquals("zx", alienOrder(words2));
        /**
         * Example 3:
         * Input:
         * [
         *   "z",
         *   "x",
         *   "z"
         * ]
         *
         * Output: ""
         *
         * Explanation: The order is invalid, so return "".
         */
        String[] words3 = new String[]{"z", "x", "z"};
        assertEquals("", alienOrder(words3));
    }
}
