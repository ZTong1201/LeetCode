import java.util.LinkedList;
import java.util.Queue;

public class myStack2 {

    /**
     * Approach 2: One Queue - (push - O(n), pop - O(1), top - O(1))
     * 也可以将上述pop时的过程放在push中，这样保证queue中元素其实和入queue的顺序相反，因此每次的queue的头部元素都是栈顶元素，使得pop和top都为O(1)
     */
    private Queue<Integer> queue;

    /** Initialize your data structure here. */
    public myStack2() {
        queue = new LinkedList<>();
    }

    /** Push element x onto stack. */
    public void push(int x) {
        queue.add(x);
        //将前n - 1个入queue的元素移到queue的后面
        //每次push1都这样做其实queue中存的元素顺序与入queue的顺序相反
        for(int i = 0; i < queue.size() - 1; i++) {
            queue.add(queue.poll());
        }
    }

    /** Removes the element on top of the stack and returns that element. */
    public int pop() {
        return queue.poll();
    }

    /** Get the top element. */
    public int top() {
        return queue.peek();
    }

    /** Returns whether the stack is empty. */
    public boolean empty() {
        return queue.isEmpty();
    }
}
