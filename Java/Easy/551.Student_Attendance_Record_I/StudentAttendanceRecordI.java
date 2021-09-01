import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StudentAttendanceRecordI {

    /**
     * You are given a string s representing an attendance record for a student where each character signifies whether
     * the student was absent, late, or present on that day. The record only contains the following three characters:
     * <p>
     * 'A': Absent.
     * 'L': Late.
     * 'P': Present.
     * The student is eligible for an attendance award if they meet both of the following criteria:
     * <p>
     * The student was absent ('A') for strictly fewer than 2 days total.
     * The student was never late ('L') for 3 or more consecutive days.
     * Return true if the student is eligible for an attendance award, or false otherwise.
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 1000
     * s[i] is either 'A', 'L', or 'P'.
     * <p>
     * Approach: One pass brute force
     * <p>
     * Time: O(n)
     * Space: O(1)
     */
    public boolean checkRecord(String s) {
        int absence = 0;
        int i = 0;
        while (i < s.length()) {
            char attendance = s.charAt(i);
            if (attendance == 'A') {
                absence++;
                if (absence > 1) return false;
            } else if (attendance == 'L') {
                int consecutiveLate = 1;
                while (i < s.length() - 1 && s.charAt(i + 1) == 'L') {
                    consecutiveLate++;
                    i++;
                }
                if (consecutiveLate >= 3) return false;
            }
            i++;
        }
        return true;
    }

    @Test
    public void checkRecordTest() {
        /**
         * Example 1:
         *
         * Input: s = "PPALLP"
         * Output: true
         * Explanation: The student has fewer than 2 absences and was never late 3 or more consecutive days.
         */
        assertTrue(checkRecord("PPALLP"));
        /**
         * Example 2:
         * Input: s = "PPALLL"
         * Output: false
         * Explanation: The student was late 3 consecutive days in the last 3 days, so is not eligible for the award.
         */
        assertFalse(checkRecord("PPALLL"));
    }
}
