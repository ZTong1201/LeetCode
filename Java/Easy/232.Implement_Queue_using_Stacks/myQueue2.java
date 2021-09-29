import java.util.Stack;

public class myQueue2 {

    /**
     * Approach 2: Two Stacks (push - O(1), pop()/peek() - amortized O(1))
     * 本质上，只有当需要pop的时候，才需要将stack1中的元素reverse存进stack2，而且stack2中的元素保持了queue所要求的顺序。但问题是，当新元素加进来，无法直接
     * 从stack1移动到stack2，因为加入新元素会影响stack2中的原有顺序。因此只要当stack2不为空时，就一直从stack2中pop元素，当stack为空后，再将当前stack1中的
     * 元素移动进stack2即可。因此push的时候只需要一直往stack1里添加元素即可。pop和peek的时候要先判断stack2是否为空，若不为空，则可以返回栈顶元素，否则就将
     * 当前stack1中的元素移动进stack2。
     * <p>
     * 在worst case，需要进行以下操作：push1, push2, ..., pushn, pop1, pop2, ..., popn.
     * 若在push了n个元素后，第一次pop需要O(n)时间，因为需要将前n个元素移动到stack2，但若后续再进行pop操作，就只需要O(1)时间。所以前2n次操作需要的总操作数
     * 为n（n次push进stack1） + 2n + 1（第一次pop，需要从stack1 pop n次，同时往stack2 push n次，再从stack2 pop一次） + n - 1（后续n - 1次从stack2
     * pop的操作）= 4n，总的时间为O(4n/2n) = O(2) = O(1)，因此为amortized O(1)时间
     */
    private final Stack<Integer> inStack, outStack;

    /**
     * Initialize your data structure here.
     */
    public myQueue2() {
        inStack = new Stack<>();
        outStack = new Stack<>();
    }

    /**
     * Push element x to the back of queue.
     */
    public void push(int x) {
        inStack.push(x);
    }

    /**
     * Removes the element from in front of queue and returns that element.
     */
    public int pop() {
        //pop的时候前判断stack2是否还有元素，有的话直接从栈顶返回，没有则将当前stack1的元素全部移动到stack2
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
        return outStack.pop();
    }

    /**
     * Get the front element.
     */
    public int peek() {
        //peek和pop的操作一样，只是返回栈顶元素，不用顶出
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
        return outStack.peek();
    }

    /**
     * Returns whether the queue is empty.
     */
    public boolean empty() {
        //当两个栈都为空时，整个queue为空
        return inStack.isEmpty() && outStack.isEmpty();
    }
}
