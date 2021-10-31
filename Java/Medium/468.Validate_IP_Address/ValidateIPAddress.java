import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ValidateIPAddress {

    /**
     * Given a string IP, return "IPv4" if IP is a valid IPv4 address, "IPv6" if IP is a valid IPv6 address or "Neither" if
     * IP is not a correct IP of any type.
     * <p>
     * A valid IPv4 address is an IP in the form "x1.x2.x3.x4" where 0 <= xi <= 255 and xi cannot contain leading zeros.
     * For example, "192.168.1.1" and "192.168.1.0" are valid IPv4 addresses but "192.168.01.1", while "192.168.1.00"
     * and "192.168@1.1" are invalid IPv4 addresses.
     * <p>
     * A valid IPv6 address is an IP in the form "x1:x2:x3:x4:x5:x6:x7:x8" where:
     * <p>
     * 1 <= xi.length <= 4
     * xi is a hexadecimal string which may contain digits, lower-case English letter ('a' to 'f') and upper-case English
     * letters ('A' to 'F').
     * Leading zeros are allowed in xi.
     * For example, "2001:0db8:85a3:0000:0000:8a2e:0370:7334" and "2001:db8:85a3:0:0:8A2E:0370:7334" are valid IPv6 addresses,
     * while "2001:0db8:85a3::8A2E:037j:7334" and "02001:0db8:85a3:0000:0000:8a2e:0370:7334" are invalid IPv6 addresses.
     * <p>
     * Constraints:
     * <p>
     * IP consists only of English letters, digits and the characters '.' and ':'.
     * <p>
     * Approach: String manipulation
     * There is no fancy algorithms nor data structures to solve this problem. We can only split the string into multiple tokens
     * and check whether each token satisfies all the conditions.
     * <p>
     * Time: O(n) in the worst case, we still need to check the entire string and see whether there is a "." or a ":" in the
     * IP address
     * Space: O(1)
     */
    private final String NEITHER = "Neither";
    private final String IPV4 = "IPv4";
    private final String IPV6 = "IPv6";

    public String validIPAddress(String IP) {
        if (IP == null || IP.equals("")) return NEITHER;

        if (IP.contains(".")) {
            return validateIPv4(IP);
        } else if (IP.contains(":")) {
            return validateIPv6(IP);
        }

        return NEITHER;
    }

    private String validateIPv4(String IP) {
        // case 1: it's not valid if it has leading and trailing "."
        if (IP.charAt(0) == '.' || IP.charAt(IP.length() - 1) == '.') return NEITHER;

        // need to use regular expression in order to split by "."
        String[] tokens = IP.split("\\.");

        // case 2: it's not a valid IP if we don't have 4 tokens
        if (tokens.length != 4) return NEITHER;

        for (String token : tokens) {
            int num = 0;
            // case 3: not valid if the length is not in range [1,4]
            if (token.length() < 1 || token.length() > 4) return NEITHER;

            // case 4: not valid if the token has leading zeros
            if (token.charAt(0) == '0' && token.length() > 1) return NEITHER;

            for (int i = 0; i < token.length(); i++) {
                char curr = token.charAt(i);

                // case 5: IPv4 address can only have digits
                if (!Character.isDigit(curr)) return NEITHER;
                else {
                    num = num * 10 + curr - '0';
                    // if the number exceeds 255 limit, it's not valid
                    if (num > 255) return NEITHER;
                }
            }
        }
        // if all conditions are met, it's a valid IPv4 address
        return IPV4;
    }

    private String validateIPv6(String IP) {
        // case 1: it's not valid if it has leading and trailing ":"
        if (IP.charAt(0) == ':' || IP.charAt(IP.length() - 1) == ':') return NEITHER;

        String[] tokens = IP.split(":");
        // define a hash set to store all valid letters in a hexadecimal string
        Set<Character> validChars = Set.of('a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F');

        // case 2: it's not a valid IP if we don't have 8 tokens
        if (tokens.length != 8) return NEITHER;

        for (String token : tokens) {
            // case 3: it's not valid if the length of token is not in range [1,4]
            if (token.length() < 1 || token.length() > 4) return NEITHER;

            for (int i = 0; i < token.length(); i++) {
                char curr = token.charAt(i);

                // case 4: it's not valid if it has characters which are not allowed
                if (!Character.isDigit(curr) && !validChars.contains(curr)) return NEITHER;
            }
        }
        // if all conditions are met, it's a valid IPv6 address
        return IPV6;
    }

    @Test
    public void validIPAddressTest() {
        /**
         * Example 1:
         * Input: IP = "172.16.254.1"
         * Output: "IPv4"
         * Explanation: This is a valid IPv4 address, return "IPv4".
         */
        assertEquals("IPv4", validIPAddress("172.16.254.1"));
        /**
         * Example 2:
         * Input: IP = "2001:0db8:85a3:0:0:8A2E:0370:7334"
         * Output: "IPv6"
         * Explanation: This is a valid IPv6 address, return "IPv6".
         */
        assertEquals("IPv6", validIPAddress("2001:0db8:85a3:0:0:8A2E:0370:7334"));
        /**
         * Example 3:
         * Input: IP = "256.256.256.256"
         * Output: "Neither"
         * Explanation: This is neither a IPv4 address nor a IPv6 address.
         */
        assertEquals("Neither", validIPAddress("256.256.256.256"));
        /**
         * Example 4:
         * Input: IP = "2001:0db8:85a3:0:0:8A2E:0370:7334:"
         * Output: "Neither"
         */
        assertEquals("Neither", validIPAddress("2001:0db8:85a3:0:0:8A2E:0370:7334:"));
        /**
         * Example 5:
         * Input: IP = "1e1.4.5.6"
         * Output: "Neither"
         */
        assertEquals("Neither", validIPAddress("1e1.4.5.6"));
        /**
         * Example 6:
         * Input: IP = "12.12.12"
         * Output: "Neither"
         */
        assertEquals("Neither", validIPAddress("12.12.12"));
        /**
         * Example 7:
         * Input: IP = "01.01.01.01"
         * Output: "Neither"
         */
        assertEquals("Neither", validIPAddress("01.01.01.01"));
    }
}
