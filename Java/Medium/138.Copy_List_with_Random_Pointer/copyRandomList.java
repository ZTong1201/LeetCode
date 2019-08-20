import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class copyRandomList {

    /**
     * A linked list is given such that each node contains an additional random pointer which could point to any node in the list or null.
     *
     * Return a deep copy of the list.
     *
     * Note:
     *
     * You must return the copy of the given head as a reference to the cloned list.
     *
     * Approach 1: Iteration
     * 只需要遍历整个链表，同时记录一个从old节点到新节点的map即可。在当前节点，在map中找寻该节点的next和random节点，若二者任意一个为null，则构造一个新的
     * null节点。若二者中任意一个被copy过，则返回map中copy的新节点。若为copy过，则构建一个新的节点，并将新节点与旧节点的映射加入map中
     *
     * Time: O(N) 遍历链表中所有节点进行deep copy
     * Space: O(N) 需要用一个map来记录新旧节点之间的映射
     */
    private Map<Node, Node> map;

    public Node copyRandomListIterative(Node head) {
        if(head == null) return null;
        this.map = new HashMap<>();
        //copy第一个节点，并将映射加入map
        Node cloned = new Node(head.val, null, null);
        Node ptr = cloned;
        this.map.put(head, cloned);

        while(head != null) {
            ptr.next = getClonedNode(head.next);
            ptr.random = getClonedNode(head.random);

            ptr = ptr.next;
            head = head.next;
        }
        return cloned;
    }

    private Node getClonedNode(Node root) {
        //如果该节点为null，直接返回null即可
        if(root != null) {
            //若当前节点没有被copy，则构建一个新节点，并加入映射
            this.map.putIfAbsent(root, new Node(root.val, null, null));
            return this.map.get(root);
        }
        return null;
    }

    /**
     * Approach 2: Recursion
     * 将链表看成图，每个节点就是图中的一个node，next和random pointer分别是每个节点的两条边。只要遍历整个图，都每个节点进行copy即可。注意的是为了避免重复
     * 遍历，还是需要一个map来进行新旧节点的映射。
     *
     * Time: O(N)  需要遍历所有节点进行copy或者连接到已copy节点
     * Space: O(N) call stack需要O(N)空间
     */
    private Map<Node, Node> oldToNew = new HashMap<>();

    public Node copyRandomListRecursive(Node head) {
        //base case，如果当前节点是null，直接返回null
        if(head == null) {
            return null;
        }

        //如果当前节点已被copy，返回已copy节点
        if(this.map.containsKey(head)) {
            return this.map.get(head);
        }

        //否则，copy当前节点，并将节点映射放入map
        Node cloned = new Node(head.val, null, null);
        this.map.put(head, cloned);

        //之后递归地遍历整个链表，并将当前节点的next和random copy好
        cloned.next = copyRandomListRecursive(head.next);
        cloned.random = copyRandomListRecursive(head.random);
        //copy结束后，返回链表头部即可
        return cloned;
    }

    /**
     * Approach 3: In-place Copy
     * 可以避免额外空间的使用进行deep copy，本质上，就是将每个节点进行一次copy，并将新节点直接连接在原节点之后。
     * 算法分为三部分
     * 1.对原链表所有节点进行deep copy，将A -> B -> C变成A -> A' -> B -> B' -> C -> C'
     * 2.将每个节点copy之后，再遍历一遍原节点，将新节点的random pointer指向新copy好的节点，即new.random = old.random.next（因为新节点都在原节点之后）
     * 3.最后再将整个链表拆开即可。
     *
     * Time: O(N) 分别遍历原链表3次，每次都是O(n)时间
     * Space: O(1) 不需要额外空间
     */
    public Node copyRandomListInPlace(Node head) {
        if(head == null) return null;
        //第一步，先遍历整个链表，将所有节点copy，并连接在原节点之后
        Node ptr = head;
        while(ptr != head) {
            Node newNode = new Node(ptr.val, null, null);
            newNode.next = ptr.next;
            ptr.next = newNode;
            //下一个节点应该为原链表节点
            ptr = newNode.next;
        }
        //第二步，重新遍历一遍原链表节点，排好新节点的random节点
        ptr = head;
        while(ptr != null) {
            //注意要指向新copy的节点，所以要指向ptr.random的下一个节点
            ptr.next.random = ptr.random == null ? null : ptr.random.next;
            ptr = ptr.next.next;
        }

        //第三步，将新旧链表分离
        Node cloned = head.next;
        ptr = head;
        while(ptr != null) {
            Node temp = ptr.next;
            ptr.next = temp.next;
            temp.next = ptr.next == null ? null : ptr.next.next;
            ptr = ptr.next;
        }
        return cloned;
    }

    private class Node {
        public int val;
        public Node next;
        public Node random;

        public Node() {}

        public Node(int _val, Node _next, Node _random) {
            this.val = _val;
            this.next = _next;
            this.random = _random;
        }
    }
}
