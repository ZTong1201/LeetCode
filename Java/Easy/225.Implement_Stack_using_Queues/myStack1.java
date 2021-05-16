import java.util.LinkedList;
import java.util.Queue;

public class myStack1 {

    /**
     * Implement the following operations of a stack using queues.
     *
     * push(x) -- Push element x onto stack.
     * pop() -- Removes the element on top of the stack.
     * top() -- Get the top element.
     * empty() -- Return whether the stack is empty.
     *
     * Notes:
     *
     * You must use only standard operations of a queue -- which means only push to back, peek/pop from front, size, and is empty
     * operations are valid.
     * Depending on your language, queue may not be supported natively. You may simulate a queue by using a list or deque
     * (double-ended queue), as long as you use only standard operations of a queue.
     * You may assume that all operations are valid (for example, no pop or top operations will be called on an empty stack).
     *
     * Approach 1: One Queue (push - O(1), pop - O(n), top - O(1))
     * push的时候把元素一个一个放入队列即可，同时用一个global variable记录最新加入的元素，可以使top为O(1)时间，但在pop时，因为queue是先进先出（FIFO），
     * 所以需要将前面的n - 1个元素先移动到queue的末端，然后将现在queue的第一个元素移除即可
     */
    private final Queue<Integer> queue;
    private int top;

    /** Initialize your data structure here. */
    public myStack1() {
        queue = new LinkedList<>();
        top = 0;
    }

    /** Push element x onto stack. */
    public void push(int x) {
        queue.add(x);
        //最新加入的元素一定是栈顶元素
        top = x;
    }

    /** Removes the element on top of the stack and returns that element. */
    public int pop() {
        //pop的时候需要把前n - 1个元素按与原顺序放入queue的后面
        for(int i = 0; i < queue.size() - 1; i++) {
            //最后一个移动过去的元素就是新的栈顶元素
            top = queue.poll();
            queue.add(top);
        }
        //将此时queue头部的元素移除即可
        return queue.poll();
    }

    /** Get the top element. */
    public int top() {
        //直接返回记录的top元素
        return top;
    }

    /** Returns whether the stack is empty. */
    public boolean empty() {
        return queue.isEmpty();
    }
}
