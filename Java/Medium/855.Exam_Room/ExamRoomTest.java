import org.junit.Test;
import static org.junit.Assert.*;

public class ExamRoomTest {

    @Test
    public void ExamRoomTest() {
        /**
         * Example:
         * Input: ["ExamRoom","seat","seat","seat","seat","leave","seat"], [[10],[],[],[],[],[4],[]]
         * Output: [null,0,9,4,2,null,5]
         * Explanation:
         * ExamRoom(10) -> null
         * seat() -> 0, no one is in the room, then the student sits at seat number 0.
         * seat() -> 9, the student sits at the last seat number 9.
         * seat() -> 4, the student sits at the last seat number 4.
         * seat() -> 2, the student sits at the last seat number 2.
         * leave(4) -> null
         * seat() -> 5, the student sits at the last seat number 5.
         */
        ExamRoom room = new ExamRoom(10);
        assertEquals(0, room.seat());
        assertEquals(9, room.seat());
        assertEquals(4, room.seat());
        assertEquals(2, room.seat());
        room.leave(4);
        assertEquals(5, room.seat());
    }
}
