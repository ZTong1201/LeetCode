import org.junit.Test;
import static org.junit.Assert.*;

public class myStackTest {

    @Test
    public void myStackTest1() {
        /**
         * MyStack stack = new MyStack();
         *
         * stack.push(1);
         * stack.push(2);
         * stack.top();   // returns 2
         * stack.pop();   // returns 2
         * stack.empty(); // returns false
         */
        myStack1 stack = new myStack1();
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.top());
        assertEquals(2, stack.pop());
        assertFalse(stack.empty());
    }

    @Test
    public void myStackTest2() {
        /**
         * MyStack stack = new MyStack();
         *
         * stack.push(1);
         * stack.push(2);
         * stack.top();   // returns 2
         * stack.pop();   // returns 2
         * stack.empty(); // returns false
         */
        myStack2 stack = new myStack2();
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.top());
        assertEquals(2, stack.pop());
        assertFalse(stack.empty());
    }
}
