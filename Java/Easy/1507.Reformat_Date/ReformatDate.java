import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ReformatDate {

    /**
     * Given a date string in the form Day Month Year, where:
     * <p>
     * Day is in the set {"1st", "2nd", "3rd", "4th", ..., "30th", "31st"}.
     * Month is in the set {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"}.
     * Year is in the range [1900, 2100].
     * Convert the date string to the format YYYY-MM-DD, where:
     * <p>
     * YYYY denotes the 4 digit year.
     * MM denotes the 2 digit month.
     * DD denotes the 2 digit day.
     * <p>
     * Constraints:
     * <p>
     * The given dates are guaranteed to be valid, so no error handling is necessary.
     * <p>
     * Approach: String split
     * <p>
     * Time: O(1)
     * Space: O(1)
     */
    private Map<String, String> monthMap;

    public String reformatDate(String date) {
        buildMonthMap();

        String[] tokens = date.split(" ");
        String[] res = new String[3];
        res[0] = tokens[2];
        res[1] = monthMap.get(tokens[1]);
        String day = (tokens[0].length() == 4) ? tokens[0] : "0" + tokens[0];
        res[2] = day.substring(0, 2);

        return String.join("-", res);
    }

    private void buildMonthMap() {
        monthMap = new HashMap<>();
        monthMap.put("Jan", "01");
        monthMap.put("Feb", "02");
        monthMap.put("Mar", "03");
        monthMap.put("Apr", "04");
        monthMap.put("May", "05");
        monthMap.put("Jun", "06");
        monthMap.put("Jul", "07");
        monthMap.put("Aug", "08");
        monthMap.put("Sep", "09");
        monthMap.put("Oct", "10");
        monthMap.put("Noc", "11");
        monthMap.put("Dec", "12");
    }

    @Test
    public void reformatDateTest() {
        /**
         * Example 1:
         * Input: date = "20th Oct 2052"
         * Output: "2052-10-20"
         */
        assertEquals("2052-10-20", reformatDate("20th Oct 2052"));
        /**
         * Example 2:
         * Input: date = "6th Jun 1933"
         * Output: "1933-06-06"
         */
        assertEquals("1933-06-06", reformatDate("6th Jun 1933"));
        /**
         * Example 3:
         * Input: "22nd Apr 2023"
         * Output: "2023-04-22"
         */
        assertEquals("2023-04-22", reformatDate("22nd Apr 2023"));
    }
}
