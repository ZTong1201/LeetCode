import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StudentAttendanceRecordII {

    /**
     * An attendance record for a student can be represented as a string where each character signifies whether the student
     * was absent, late, or present on that day. The record only contains the following three characters:
     * <p>
     * 'A': Absent.
     * 'L': Late.
     * 'P': Present.
     * Any student is eligible for an attendance award if they meet both of the following criteria:
     * <p>
     * The student was absent ('A') for strictly fewer than 2 days total.
     * The student was never late ('L') for 3 or more consecutive days.
     * Given an integer n, return the number of possible attendance records of length n that make a student eligible for an
     * attendance award. The answer may be very large, so return it modulo 10^9 + 7.
     * <p>
     * Constraints:
     * <p>
     * 1 <= n <= 10^5
     * <p>
     * Approach 1: Brute Force
     * Use DFS to search all the possible results, also prune nodes in which have 2 absences or 3 consecutive late
     * <p>
     * Time: O(3^n) for each position, we have 3 choices ('A', 'L',' P')  hence the number of combinations is bounded by 3^n
     * Space: O(n) call stack will take O(n) space
     */
    private long count;

    public int checkRecordBruteForce(int n) {
        String[] options = new String[]{"A", "P", "L"};
        count = 0;
        dfs(options, 0, n, new StringBuilder());
        return (int) (count % 100000009);
    }

    private void dfs(String[] options, int absences, int n, StringBuilder record) {
        // if we have two absences or 3 consecutive late - stop
        if (absences > 1 || record.toString().contains("LLL")) return;
        if (record.length() == n) {
            count++;
            return;
        }
        for (String option : options) {
            record.append(option);
            if (option.equals("A")) {
                dfs(options, absences + 1, n, record);
            } else {
                dfs(options, absences, n, record);
            }
            record.deleteCharAt(record.length() - 1);
        }
    }

    /**
     * Approach 2: DP
     * We can consider only 'L' and 'P' in the record string because there can be at most 1 absence in the attendance.
     * From a math perspective, for a given record of length n, there are only three options, i.e.
     * 1. -------P 2. -------PL 3. -------PLL
     * any other options will be the duplicate of three options above.
     * Hence, f(n) = f(n - 1) + f(n - 2) + f(n - 3) is our recurring relation
     * =>
     * f(n - 1) = f(n - 2) + f(n - 3) + f(n - 4)
     * =>
     * f(n) = 2 * f(n - 1) - f(n - 4)
     * We can easily compute the result via DP if we know f(n - 1) and f(n - 4)
     * <p>
     * Then we can circle back to add absence. If there is no absence, then f(n) is already the desired answer. If there
     * is one absence, we can randomly pick a place from index 0 to n - 1. Then, the record will be split into two parts
     * [------] A [-----]
     * i - 1   i  n - i
     * There are f(i - 1) * f(n - i) combinations to place A at index i. We need add those numbers into the count
     * <p>
     * Time: O(n)
     * Space: O(n)
     */
    public int checkRecordDP(int n) {
        long modulo = (long) Math.pow(10, 9) + 7;
        // we need at least length 4 since f(i - 4) will be calculated
        int length = Math.max(n + 1, 4);
        long[] numOfRecordsWithoutAbsence = new long[length];
        // base case - only 1 combination if there is nothing
        numOfRecordsWithoutAbsence[0] = 1;
        // 'P' or 'L'
        numOfRecordsWithoutAbsence[1] = 2;
        // 'PP', 'PL', 'LP', and 'LL'
        numOfRecordsWithoutAbsence[2] = 4;
        // any combinations but 'LLL' hence 2^3 - 1 = 7
        numOfRecordsWithoutAbsence[3] = 7;
        for (int i = 4; i <= n; i++) {
            // since the count can be larger, we take the modulo on the fly
            // use modulo - f(i - 4) to avoid taking modulo on negative numbers
            numOfRecordsWithoutAbsence[i] = ((2 * numOfRecordsWithoutAbsence[i - 1]) % modulo
                    + (modulo - numOfRecordsWithoutAbsence[i - 4]) % modulo);
        }

        // the count now corresponds to all valid attendances without a single absence
        long count = numOfRecordsWithoutAbsence[n];
        // now select a position from 1 to n and add those combinations into the result
        // again, take modulo on the fly
        for (int i = 1; i <= n; i++) {
            count += (numOfRecordsWithoutAbsence[i - 1] * numOfRecordsWithoutAbsence[n - i]) % modulo;
        }
        return (int) (count % modulo);
    }

    @Test
    public void checkRecordTest() {
        /**
         * Example 1:
         * Input: n = 2
         * Output: 8
         * Explanation: There are 8 records with length 2 that are eligible for an award:
         * "PP", "AP", "PA", "LP", "PL", "AL", "LA", "LL"
         * Only "AA" is not eligible because there are 2 absences (there need to be fewer than 2).
         */
        assertEquals(8, checkRecordBruteForce(2));
        assertEquals(8, checkRecordDP(2));
        /**
         * Example 2:
         * Input: n = 1
         * Output: 3
         */
        assertEquals(3, checkRecordBruteForce(1));
        assertEquals(3, checkRecordDP(1));

        /**
         * Example 3:
         * Input: n = 10101
         * Output: 183236316
         * The brute force will take a much longer time
         */
        assertEquals(183236316, checkRecordDP(10101));
    }
}
