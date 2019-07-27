public class minStack {

    /**
     * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
     *
     * push(x) -- Push element x onto stack.
     * pop() -- Removes the element on top of the stack.
     * top() -- Get the top element.
     * getMin() -- Retrieve the minimum element in the stack.
     *
     * Approach: Linked List With Min
     * By using a linked list, we can add element, retrieve element and remove element easily at the front of the list. In order to get
     * the minimum in the stack in O(1) time, we can add another attribute to the linked list. At each node, we record the minimum so far
     * and return it in O(1) time.
     *
     * To avoid annoying base cases, we add a sentinel node at the front of the linked list.
     *
     * Time: all O(1)
     * Space: O(n)
     */
    private ListNodeWithMin node;

    public minStack() {
        //we need to initialize a sentinel node at the front
        //since we'll never retrieve that value, no matter what value we assign
        this.node = new ListNodeWithMin(0, 0, null);

    }

    public void push(int x) {
        //add a new node at the front of the list
        //be sure to update the minimum
        int min = this.node.next == null ? x : Math.min(node.next.min, x);
        //remember, when we add a new node at the front, the next node of the new node
        //the whole list, which is just the next node of the sentinel node
        this.node.next = new ListNodeWithMin(x, min, this.node.next);
    }

    public void pop() {
        //simply skip the front node
        this.node.next = this.node.next.next;
    }

    public int top() {
        return this.node.next.val;
    }

    public int getMin() {
        return this.node.next.min;
    }

    private class ListNodeWithMin {
        int val;
        int min;
        ListNodeWithMin next;

        ListNodeWithMin(int val, int min, ListNodeWithMin next) {
            this.val = val;
            this.min = min;
            this.next = next;
        }
    }
}
