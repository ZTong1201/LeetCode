import org.junit.Test;
import static org.junit.Assert.*;

public class myQueueTest {

    @Test
    public void myQueueTest1() {
        /**
         * MyQueue queue = new MyQueue();
         *
         * queue.push(1);
         * queue.push(2);
         * queue.peek();  // returns 1
         * queue.pop();   // returns 1
         * queue.empty(); // returns false
         */
        myQueue1 queue = new myQueue1();
        queue.push(1);
        queue.push(2);
        assertEquals(1, queue.peek());
        assertEquals(1, queue.pop());
        assertFalse(queue.empty());
    }

    @Test
    public void myQueueTest2() {
        /**
         * MyQueue queue = new MyQueue();
         *
         * queue.push(1);
         * queue.push(2);
         * queue.peek();  // returns 1
         * queue.pop();   // returns 1
         * queue.empty(); // returns false
         */
        myQueue2 queue = new myQueue2();
        queue.push(1);
        queue.push(2);
        assertEquals(1, queue.peek());
        assertEquals(1, queue.pop());
        assertFalse(queue.empty());
    }
}
