import java.util.HashMap;
import java.util.Map;

public class Logger {

    /**
     * Design a logger system that receives a stream of messages along with their timestamps. Each unique message should
     * only be printed at most every 10 seconds (i.e. a message printed at timestamp t will prevent other identical messages
     * from being printed until timestamp t + 10).
     * <p>
     * All messages will come in chronological order. Several messages may arrive at the same timestamp.
     * <p>
     * Implement the Logger class:
     * <p>
     * Logger() Initializes the logger object.
     * bool shouldPrintMessage(int timestamp, string message) Returns true if the message should be printed in the given
     * timestamp, otherwise returns false.
     * <p>
     * Constraints:
     * <p>
     * 0 <= timestamp <= 10^9
     * Every timestamp will be passed in non-decreasing order (chronological order).
     * 1 <= message.length <= 30
     * At most 10^4 calls will be made to shouldPrintMessage.
     */

    private final Map<String, Integer> lastTimestamp;

    /**
     * Initialize your data structure here.
     */
    public Logger() {
        lastTimestamp = new HashMap<>();
    }

    /**
     * Returns true if the message should be printed in the given timestamp, otherwise returns false.
     * If this method returns false, the message will not be printed.
     * The timestamp is in seconds granularity.
     */
    public boolean shouldPrintMessage(int timestamp, String message) {
        if (!lastTimestamp.containsKey(message) || timestamp >= lastTimestamp.get(message) + 10) {
            lastTimestamp.put(message, timestamp);
            return true;
        }
        return false;
    }
}
