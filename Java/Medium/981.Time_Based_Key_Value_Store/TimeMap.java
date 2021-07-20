import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TimeMap {
    /**
     * Design a time-based key-value data structure that can store multiple values for the same key at different
     * time stamps and retrieve the key's value at a certain timestamp.
     * <p>
     * Implement the TimeMap class:
     * <p>
     * TimeMap() Initializes the object of the data structure.
     * void set(String key, String value, int timestamp) Stores the key key with the value value at the given
     * time timestamp.
     * String get(String key, int timestamp) Returns a value such that set was called previously, with
     * timestamp_prev <= timestamp. If there are multiple such values, it returns the value associated with
     * the largest timestamp_prev. If there are no values, it returns "".
     * <p>
     * Constraints:
     * <p>
     * 1 <= key.length, value.length <= 100
     * key and value consist of lowercase English letters and digits.
     * 1 <= timestamp <= 107
     * All the timestamps timestamp of set are strictly increasing.
     * At most 2 * 105 calls will be made to set and get.
     * <p>
     * Approach: binary search
     * Since all the timestamps timestamp of set are strictly increasing, the get operation can be benefited from it to
     * use binary search.
     * <p>
     * Assume on average each key can have N values, and there are K keys inserted
     * Time: TimeMap(): O(1), set(): O(1), get(): O(logN)
     * Space: O(K * N)
     */
    private final Map<String, List<valueWithTime>> map;

    public TimeMap() {
        map = new HashMap<>();
    }

    public void set(String key, String value, int timestamp) {
        map.putIfAbsent(key, new ArrayList<>());
        map.get(key).add(new valueWithTime(value, timestamp));
    }

    public String get(String key, int timestamp) {
        if (!map.containsKey(key)) return "";
        return search(map.get(key), timestamp);
    }

    private String search(List<valueWithTime> values, int timestamp) {
        int left = 0, right = values.size();
        while (left < right) {
            int mid = (right - left) / 2 + left;
            int midTime = values.get(mid).time;
            if (midTime == timestamp) {
                return values.get(mid).value;
            } else if (midTime < timestamp) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        // 因为选择左闭右开区间，因此若不存在exact timestamp的值
        // left指针会指向如果该值插入区间应该所处的位置，因此满足
        // timestamp_prev <= timestamp条件的最大index为left - 1
        return (left - 1 >= 0) ? values.get(left - 1).value : "";
    }

    @Test
    public void timeMapTest() {
        /**
         * Example:
         * Input
         * ["TimeMap", "set", "get", "get", "set", "get", "get"]
         * [[], ["foo", "bar", 1], ["foo", 1], ["foo", 3], ["foo", "bar2", 4], ["foo", 4], ["foo", 5]]
         * Output
         * [null, null, "bar", "bar", null, "bar2", "bar2"]
         *
         * Explanation
         * TimeMap timeMap = new TimeMap();
         * timeMap.set("foo", "bar", 1);  // store the key "foo" and value "bar" along with timestamp = 1.
         * timeMap.get("foo", 1);         // return "bar"
         * timeMap.get("foo", 3);         // return "bar", since there is no value corresponding to foo at timestamp 3 and timestamp 2, then the only value is at timestamp 1 is "bar".
         * timeMap.set("foo", "bar2", 4); // store the key "foo" and value "ba2r" along with timestamp = 4.
         * timeMap.get("foo", 4);         // return "bar2"
         * timeMap.get("foo", 5);         // return "bar2"
         */
        TimeMap timeMap = new TimeMap();
        timeMap.set("foo", "bar", 1);
        assertEquals("", timeMap.get("bar", 1));
        assertEquals("bar", timeMap.get("foo", 1));
        assertEquals("bar", timeMap.get("foo", 3));
        timeMap.set("foo", "bar2", 4);
        assertEquals("bar2", timeMap.get("foo", 4));
        assertEquals("bar2", timeMap.get("foo", 5));
    }

    private static class valueWithTime {
        String value;
        int time;

        public valueWithTime(String value, int timestamp) {
            this.value = value;
            this.time = timestamp;
        }
    }

}
