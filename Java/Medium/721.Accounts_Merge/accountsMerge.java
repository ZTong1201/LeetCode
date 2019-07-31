import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class accountsMerge {

    /**
     * Given a list accounts, each element accounts[i] is a list of strings, where the first element accounts[i][0] is a name,
     * and the rest of the elements are emails representing emails of the account.
     *
     * Now, we would like to merge these accounts. Two accounts definitely belong to the same person if there is some email that is common
     * to both accounts. Note that even if two accounts have the same name, they may belong to different people as people could have the
     * same name. A person can have any number of accounts initially, but all of their accounts definitely have the same name.
     *
     * After merging the accounts, return the accounts in the following format: the first element of each account is the name, and the rest
     * of the elements are emails in sorted order. The accounts themselves can be returned in any order.
     *
     * Note:
     *
     * The length of accounts will be in the range [1, 1000].
     * The length of accounts[i] will be in the range [1, 10].
     * The length of accounts[i][j] will be in the range [1, 30].
     *
     * Approach 1: Depth-First Search
     * 关键点为如何构建图。可以利用HashMap将每个用户的第一个email和其他email相连（双向)， 同时再建一个HashMap链接每个email和相应用户名。
     * 图建好后，利用DFS将所有connected component放在一个list里sort一下，最后在list首端加入用户名。为避免充实，DFS过程中，再建一个hashset用来记录
     * 访问过的节点。
     *
     * Time: O(n + sum(ai * log(ai))) n是所用元素的总个数，构建图需要遍历所有节点，时间O(n), ai是account[i]的长度，再找到所有connected component后
     *      要对list进行排序操作
     * Space: O(n)， 需要一个map来构建图，同时另一个map来存储email和name之间的关系。DFS中还需要额外的hashset和stack来遍历节点。All are in O(n) space.
     */
    private Map<String, String> emailToName;
    private Map<String, List<String>> graph;

    public List<List<String>> accountsMergeDFS(List<List<String>> accounts) {
        this.emailToName = new HashMap<>();
        this.graph = new HashMap<>();
        buildGraph(accounts);
        List<List<String>> res = new ArrayList<>();
        dfs(res);
        return res;
    }

    //构建图和email与name之间的连接关系
    private void buildGraph(List<List<String>> accounts) {
        for(List<String> alist : accounts) {
            emailToName.putIfAbsent(alist.get(1), alist.get(0));
            graph.putIfAbsent(alist.get(1), new ArrayList<>());
            for(int i = 2; i < alist.size(); i++) {
                graph.get(alist.get(1)).add(alist.get(i));
                graph.putIfAbsent(alist.get(i), new ArrayList<>());
                graph.get(alist.get(i)).add(alist.get(1));
                emailToName.putIfAbsent(alist.get(i), alist.get(0));
            }
        }
    }

    //implement DFS to get connected components
    private void dfs(List<List<String>> res) {
        //需要一个set来记录已遍历节点
        Set<String> seen = new HashSet<>();
        for(String email : graph.keySet()) {
            if(!seen.contains(email)) {
                seen.add(email);
                //need a stack to realize DFS
                Stack<String> stack = new Stack<>();
                stack.push(email);
                //构建一个链表存储connected component
                LinkedList<String> component = new LinkedList<>();
                while(!stack.isEmpty()) {
                    String curr = stack.pop();
                    component.addLast(curr);
                    //search until the deepest for all the adjacent nodes
                    for(String next : graph.get(curr)) {
                        if(!seen.contains(next)) {
                            seen.add(next);
                            stack.push(next);
                        }
                    }
                }
                Collections.sort(component);
                //remember to add name at the front of the list
                component.addFirst(emailToName.get(email));
                res.add(component);
            }
        }
    }

    @Test
    public void accountsMergeDFSTest() {
        /**
         * Example 1:
         * Input:
         * accounts = [["John", "johnsmith@mail.com", "john00@mail.com"], ["John", "johnnybravo@mail.com"],
         * ["John", "johnsmith@mail.com", "john_newyork@mail.com"], ["Mary", "mary@mail.com"]]
         * Output: [["John", 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com'],  ["John", "johnnybravo@mail.com"],
         * ["Mary", "mary@mail.com"]]
         * Explanation:
         * The first and third John's are the same person as they have the common email "johnsmith@mail.com".
         * The second John and Mary are different people as none of their email addresses are used by other accounts.
         * We could return these lists in any order, for example the answer [['Mary', 'mary@mail.com'], ['John', 'johnnybravo@mail.com'],
         * ['John', 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com']] would still be accepted.
         */
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(new ArrayList<>(Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com")));
        accounts.add(new ArrayList<>(Arrays.asList("John", "johnnybravo@mail.com")));
        accounts.add(new ArrayList<>(Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com")));
        accounts.add(new ArrayList<>(Arrays.asList("Mary", "mary@mail.com")));
        List<List<String>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(Arrays.asList("John", "johnnybravo@mail.com")));
        expected.add(new ArrayList<>(Arrays.asList("John", "john00@mail.com", "john_newyork@mail.com", "johnsmith@mail.com")));
        expected.add(new ArrayList<>(Arrays.asList("Mary", "mary@mail.com")));
        List<List<String>> actual = accountsMergeDFS(accounts);
        assertEquals(actual.size(), expected.size());
        for(int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).size(), actual.get(i).size());
            for(int j = 0; j < expected.get(i).size(); j++) {
                assertEquals(expected.get(i).get(j), actual.get(i).get(j));
            }
        }
    }


    /**
     * Approach 2: Union Find
     * 找到所有的connected component， union find时间最快。给每一个新的email赋予一个integer id， 建立一个email和id的map就可以快速找到每个email属于
     * union find中哪一个节点。同时还需要一个email到name的map来寻找对应关系。只要两个email隶属于同一个人，就讲两个节点连接起来。最后只需要查union find中
     * distinct的id值，将隶属于该id的所有components放到一个list，排序，在首端加上name即可。
     * 因为accounts的长度不超过1000，accounts[i]的长度不超过10， 所以我们有至多10000个distinct emails，union find只需要常数空间
     *
     * Time: O(n*a(n)), 利用union by rank和path compression，union的time complexity只需a(n) < 5, 所以约等于O(n)
     * Space: O(n) union find需要常数空间即可（size为10000的两个array， 但同时我们需要两个map来存储email和name以及email和ID之间的关系，需要O(n)space
     */
    public List<List<String>> accountsMergeUnionFind(List<List<String>> accounts) {
        Map<String, String> emailToName = new HashMap<>();
        Map<String, Integer> emailToID = new HashMap<>();
        unionFind uf = new unionFind();
        int id = 0;
        //先看每一个用户，建立email和id，email和name的联系，同时将隶属于该用户的所有email节点(id)连接起来
        for(List<String> alist : accounts) {
            for(int i = 1; i < alist.size(); i++) {
                emailToName.putIfAbsent(alist.get(i), alist.get(0));
                if(!emailToID.containsKey(alist.get(i))) {
                    emailToID.putIfAbsent(alist.get(i), id++);
                }
                //将所有email与属于该用户的第一个邮箱相连
                uf.union(emailToID.get(alist.get(i)), emailToID.get(alist.get(1)));
            }
        }

        //构建一个id和component list的map来存储所有connected component
        Map<Integer, LinkedList<String>> res = new HashMap<>();
        for(String email : emailToID.keySet()) {
            int index = uf.root(emailToID.get(email));
            res.putIfAbsent(index, new LinkedList<>());
            res.get(index).add(email);
        }
        //不同的connected component就是res这个hashmap的value set
        //只需对res的values进行操作(排序，在首端加入名字)即可得到最终结果
        for(LinkedList<String> component : res.values()) {
            Collections.sort(component);
            //用户名存放在emailToName的map里，提取即可
            component.addFirst(emailToName.get(component.get(0)));
        }
        //最终返回以res的values构建的list即可
        return new ArrayList<>(res.values());
    }

    private class unionFind {
        private int[] parent;
        private int[] size;

        public unionFind() {
            this.size = new int[10001];
            this.parent = new int[10001];
            Arrays.fill(this.size, 1);
            for(int i = 0; i < 10001; i++) {
                this.parent[i] = i;
            }
        }

        public int root(int i) {
            while(i != parent[i]) {
                parent[i] = parent[parent[i]];
                i = parent[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int p = root(i);
            int q = root(j);
            if(p == q) return;
            if(size[p] < size[q]) {
                size[q] += size[p];
                parent[p] = q;
            } else {
                size[p] += size[q];
                parent[q] = p;
            }
        }
    }

    @Test
    public void accountsMergeUnionFindTest() {
        /**
         * Example 1:
         * Input:
         * accounts = [["John", "johnsmith@mail.com", "john00@mail.com"], ["John", "johnnybravo@mail.com"],
         * ["John", "johnsmith@mail.com", "john_newyork@mail.com"], ["Mary", "mary@mail.com"]]
         * Output: [["John", 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com'],  ["John", "johnnybravo@mail.com"],
         * ["Mary", "mary@mail.com"]]
         * Explanation:
         * The first and third John's are the same person as they have the common email "johnsmith@mail.com".
         * The second John and Mary are different people as none of their email addresses are used by other accounts.
         * We could return these lists in any order, for example the answer [['Mary', 'mary@mail.com'], ['John', 'johnnybravo@mail.com'],
         * ['John', 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com']] would still be accepted.
         */
        List<List<String>> accounts = new ArrayList<>();
        accounts.add(new ArrayList<>(Arrays.asList("John", "johnsmith@mail.com", "john00@mail.com")));
        accounts.add(new ArrayList<>(Arrays.asList("John", "johnnybravo@mail.com")));
        accounts.add(new ArrayList<>(Arrays.asList("John", "johnsmith@mail.com", "john_newyork@mail.com")));
        accounts.add(new ArrayList<>(Arrays.asList("Mary", "mary@mail.com")));
        List<List<String>> expected = new ArrayList<>();
        expected.add(new ArrayList<>(Arrays.asList("John", "john00@mail.com", "john_newyork@mail.com", "johnsmith@mail.com")));
        expected.add(new ArrayList<>(Arrays.asList("John", "johnnybravo@mail.com")));
        expected.add(new ArrayList<>(Arrays.asList("Mary", "mary@mail.com")));
        List<List<String>> actual = accountsMergeUnionFind(accounts);
        assertEquals(actual.size(), expected.size());
        for(int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).size(), actual.get(i).size());
            for(int j = 0; j < expected.get(i).size(); j++) {
                assertEquals(expected.get(i).get(j), actual.get(i).get(j));
            }
        }
    }
}
