import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class reorderLogs {
    public String[] reorderLogFiles(String[] logs) {
        /**
         * You have an array of logs.  Each log is a space delimited string of words.
         *
         * For each log, the first word in each log is an alphanumeric identifier. Then, either:
         * 1. Each word after the identifier will consist only of lowercase letters, or;
         * 2. Each word after the identifier will consist only of digits.
         * 3. We will call these two varieties of logs letter-logs and digit-logs.
         * It is guaranteed that each log has at least one word after its identifier.
         *
         * Reorder the logs so that all of the letter-logs come before any digit-log.
         * The letter-logs are ordered lexicographically ignoring identifier, with the identifier used in case of ties.
         * The digit-logs should be put in their original order.
         *
         * Note:
         *
         * 1. 0 <= logs.length <= 100
         * 2. 3 <= logs[i].length <= 100
         * 3. logs[i] is guaranteed to have an identifier, and a word after the identifier.
         *
         * Return the final order of the logs.
         *
         * Basically, this problem is revising the comparator function in Java. We have such sorting rules:
         * 1. Letter logs locate before any digit logs
         * 2. Letter logs is in lexicographical order, if there is a tie, sort identifier in lexicographical order
         * 3. Digit logs should remain their original relative order.
         *
         * Now, we can revise our comparator. We first split the String by the first whitespace using split(" ", 2), where 2 is a limit
         * number for splitting. If limit > 0, split at most limit - 1 times in the string. If limit < 0, split as many times as possible.
         * If limit = 0, still split as many times as possible, however, the returning array will get rid of all the trailing empty strings.
         *
         * Note that we can first check first words in two logs to be compared.
         * 1. If two words are both not digits: sort them lexicographically. The original compareTo function handle this case.
         *    If there is a tie, check identifiers
         * 2. If first word is digit, the second is not, return 1 (or any numbers larger than 0) indicating first log comes later.
         * 3. If first word is not a digit, while the second is, return -1 (or any numbers smaller than 0) indicating first log comes ahead.
         * 4. If both words are digits, simply return 0, indicating they should remain the original order.
         *
         * Time: O(NlogN), typical sorting algorithm takes O(NlogN) runtime.
         * Space: O(N), we need to split each string into an array.
         */
        Comparator<String> stringComparator = (String log1, String log2) -> {
            String[] split1 = log1.split(" ", 2);
            String[] split2 = log2.split(" ", 2);
            boolean isDigit1 = Character.isDigit(split1[1].charAt(0));
            boolean isDigit2 = Character.isDigit(split2[1].charAt(0));
            if(!isDigit1 && !isDigit2) {
                int tmp = split1[1].compareTo(split2[1]);
                if(tmp != 0) return tmp;
                return split1[0].compareTo(split2[0]);
            }
            /*if(isDigit1 && isDigit2) return 0;
            else if(isDigit1 && !isDigit2) return 1;
            else return -1;*/
            return isDigit1 ? (isDigit2 ? 0 : 1) : -1;
        };
        Arrays.sort(logs, stringComparator);
        return logs;
    }

    @Test
    public void reorderLogFilesTest() {
        /**
         * Example 1:
         * Input: ["a1 9 2 3 1","g1 act car","zo4 4 7","ab1 off key dog","a8 act zoo"]
         * Output: ["g1 act car","a8 act zoo","ab1 off key dog","a1 9 2 3 1","zo4 4 7"]
         */
        String[] logs1 = new String[]{"a1 9 2 3 1", "g1 act car", "zo4 4 7", "ab1 off key dog", "a8 act zoo"};
        String[] actual1 = reorderLogFiles(logs1);
        String[] expected1 = new String[]{"g1 act car", "a8 act zoo", "ab1 off key dog", "a1 9 2 3 1", "zo4 4 7"};
        assertArrayEquals(expected1, actual1);
        /**
         * Example 2:
         * Input: ["t kvr", "r 3 1", "i 403", "7 so", "t 54"];
         * Output: ["t kvr", "7 so", "r 3 1", "i 403", "t 54"];
         */
        String[] logs2 = new String[]{"t kvr", "r 3 1", "i 403", "7 so", "t 54"};
        String[] actual2 = reorderLogFiles(logs2);
        String[] expected2 = new String[]{"t kvr", "7 so", "r 3 1", "i 403", "t 54"};
        assertArrayEquals(expected2, actual2);
    }
}
