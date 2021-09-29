import java.util.Stack;

public class myQueue1 {

    /**
     * Implement the following operations of a queue using stacks.
     * <p>
     * push(x) -- Push element x to the back of queue.
     * pop() -- Removes the element from in front of queue.
     * peek() -- Get the front element.
     * empty() -- Return whether the queue is empty.
     * <p>
     * Notes:
     * <p>
     * You must use only standard operations of a stack -- which means only push to top, peek/pop from top, size, and is empty operations
     * are valid.
     * Depending on your language, stack may not be supported natively. You may simulate a stack by using a list or deque (double-ended
     * queue), as long as you use only standard operations of a stack.
     * <p>
     * You may assume that all operations are valid (for example, no pop or peek operations will be called on an empty queue).
     * <p>
     * Approach 1: Two Stacks (push - O(n), pop - O(1))
     * 因为stack是LIFO，而queue是FIFO，所以需要维持两个stack，先将stack1中的所有元素移到stack2中，然后在stack1中加入新元素，最后再将stack2中的元素全部
     * 放回stack1中，这样的互换方式使得当前栈内的元素顺序和queue的要求一致，栈顶元素是最先进来的元素。因此无论是pop还是peek都只需要O(1)时间。
     */

    private final Stack<Integer> stack1, stack2;

    /**
     * Initialize your data structure here.
     */
    public myQueue1() {
        stack1 = new Stack<>();
        stack2 = new Stack<>();
    }

    /**
     * Push element x to the back of queue.
     */
    public void push(int x) {
        //先将stack1的元素放入stack2中
        while (!stack1.isEmpty()) {
            stack2.push(stack1.pop());
        }
        //然后再在stack1中进入最新元素，即放在栈底，保证其是最后一个出栈
        stack1.push(x);
        //再将stack2中元素放回stack1，顺序即是queue要求的顺序
        while (!stack2.isEmpty()) {
            stack1.push(stack2.pop());
        }
    }

    /**
     * Removes the element from in front of queue and returns that element.
     */
    public int pop() {
        //此时栈顶元素就是最先入栈的元素
        return stack1.pop();
    }

    /**
     * Get the front element.
     */
    public int peek() {
        return stack1.peek();
    }

    /**
     * Returns whether the queue is empty.
     */
    public boolean empty() {
        return stack1.isEmpty();
    }
}
