import java.util.*;

public class cloneGraph {
    /**
     * Given a reference of a node in a connected undirected graph, return a deep copy (clone) of the graph. Each node in the graph
     * contains a val (int) and a list (List[Node]) of its neighbors.
     *
     * Note:
     *
     * The number of nodes will be between 1 and 100.
     * The undirected graph is a simple graph, which means no repeated edges and no self-loops in the graph.
     * Since the graph is undirected, if node p has node q as neighbor, then node q must have node p as neighbor too.
     * You must return the copy of the given node as a reference to the cloned graph.
     *
     * Approach 1: DFS
     * 因为要deep copy一个图，此题的本质就是遍历整个图，当遇到新节点时，重新建立一个新的Node，值仍未当前节点值，neighbors初始化成为一个空的list。
     * 此题的关键是，要建立一个hash map，在原始节点和copy的新节点之间映射。目的是为了记住已经copy好的节点，当某个节点的neighbor是一个已经copy好的节点时，
     * （即在map中存在），可以直接将先前copy好的节点加在其neighbors的list里。
     *
     * 遍历图的过程可以用DFS或BFS，DFS的好处是，当遍历到终止节点（即其所有邻居都已经copy完毕，全在map里），直接将其所有neighbor拷贝进neighbors list中，然后
     * 一层一层的backtrack回到前一个节点，继续更新neighbors，最后回到起始节点，返回当前节点即可。
     *
     * Time: O(V + E)，每个节点只会copy一遍，之后从map中取出copy好的节点即可。同时图是无向的，即每个边会从两个方向各遍历一遍，将对方加进自己的neighbors
     * Space: O(V)，map中将所有节点都copy一份，同时递归的最大深度为经过所有节点而遍历到终止节点
     */
    public Node cloneGraphDFS(Node node) {
        if(node == null) return null;
        //建立原始节点和新copy的节点之间的映射
        Map<Node, Node> graph = new HashMap<>();
        //从起始节点开始，copy整个图
        return cloneDFS(node, graph);
    }

    private Node cloneDFS(Node node, Map<Node, Node> graph) {
        //base case，若当前遍历节点已经是copy过的节点，直接返回copy好的新节点
        if(graph.containsKey(node)) {
            return graph.get(node);
        }
        //否则的话，就要copy当前节点，然后将新旧节点映射放入graph中
        Node cloned = new Node(node.val, new ArrayList<>());
        graph.put(node, cloned);
        //然后进行DFS，对于其相邻节点，继续做同样操作
        for(Node neighbor : node.neighbors) {
            Node nei = cloneDFS(neighbor, graph);
            //当遍历到终止节点，其相邻节点都已copy完毕，或没有相邻节点，返回最后copy的节点
            //将返回节点放入当前copy好节点的neighbors中即可
            cloned.neighbors.add(nei);
        }
        //一层一层backtrack，最后返回copy好的起始节点即可
        return cloned;
    }


    /**
     * Approach 2: BFS
     * BFS本质差不多，只是遍历图的方式从深度优先变为广度优先。那么在当前节点时，首先判断其相邻节点是否copy完毕，若没有copy，就同样copy相邻节点，然后
     * 加入map中。同时将相邻节点放入队列，以便后续遍历。
     * 与DFS最大的区别是，在当前节点，copy了其相邻节点后，可以直接将所有copy好的相邻节点（无论是在当前节点copy还是之前copy）都放入neighbors中即可。因为
     * BFS不会在backtrack回到起始节点。因此需要在遍历下一节点前，将其neighbors安排好。
     *
     * Time: O(V + E)，与DFS类似，需要遍历每个节点，同时每条边还是会遍历两边
     * Space: O(V)，用queue来代替call stack实现BFS，最坏情况仍可能放入所有节点。同时graph的映射功能不变，需要copy所有节点
     */
    public Node cloneGraphBFS(Node node) {
        if(node == null) return null;
        Map<Node, Node> graph = new HashMap<>();
        return cloneBFS(node, graph);
    }

    private Node cloneBFS(Node node, Map<Node, Node> graph) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);
        //先copy起始节点
        graph.put(node, new Node(node.val, new ArrayList<>()));
        while(!queue.isEmpty()) {
            //由BFS遍历到当前节点
            Node curr = queue.poll();
            for(Node neighbor : curr.neighbors) {
                //查看其所有邻居，将其未copy的邻居都copy好
                if(!graph.containsKey(neighbor)) {
                    graph.put(neighbor, new Node(neighbor.val, new ArrayList<>()));
                    //同时将为遍历节放入队列
                    queue.add(neighbor);
                }
                //当相邻节点都copy好后，将所有copy好的邻居放入当前节点的neighbors中
                //因为只会不会再遍历回到当前节点。
                //注意所有节点都要用新copy的节点，所以都要用graph.get()
                graph.get(curr).neighbors.add(graph.get(neighbor));
            }
        }
        //最后拷贝完成，返回新copy好的起始节点即可
        return graph.get(node);
    }

    /**
     * Example:
     *  1 -------- 2
     *  |          |
     *  |          |
     *  |          |
     *  3 -------- 4
     * Input:
     * {"$id":"1","neighbors":[{"$id":"2","neighbors":[{"$ref":"1"},{"$id":"3","neighbors":[{"$ref":"2"},{"$id":"4","neighbors":
     * [{"$ref":"3"},{"$ref":"1"}],"val":4}],"val":3}],"val":2},{"$ref":"4"}],"val":1}
     *
     * Explanation:
     * Node 1's value is 1, and it has two neighbors: Node 2 and 4.
     * Node 2's value is 2, and it has two neighbors: Node 1 and 3.
     * Node 3's value is 3, and it has two neighbors: Node 2 and 4.
     * Node 4's value is 4, and it has two neighbors: Node 1 and 3.
     */


    private class Node {
        public int val;
        public List<Node> neighbors;

        public Node() {}

        public Node(int _val, List<Node> _neighbors) {
            this.val = _val;
            this.neighbors = _neighbors;
        }
    }

}
