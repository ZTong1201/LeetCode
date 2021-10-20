import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class compareVersion {

    /**
     * Compare two version numbers version1 and version2.
     * If version1 > version2 return 1; if version1 < version2 return -1;otherwise return 0.
     * <p>
     * You may assume that the version strings are non-empty and contain only digits and the . character.
     * <p>
     * The . character does not represent a decimal point and is used to separate number sequences.
     * <p>
     * For instance, 2.5 is not "two and a half" or "half way to version three", it is the fifth second-level revision of the second
     * first-level revision.
     * <p>
     * You may assume the default revision number for each level of a version number to be 0. For example, version number 3.4 has a revision
     * number of 3 and 4 for its first and second level revision number. Its third and fourth level revision number are both 0.
     * <p>
     * Note:
     * <p>
     * Version strings are composed of numeric strings separated by dots . and this numeric strings may have leading zeroes.
     * Version strings do not start or end with dots, and they will not be two consecutive dots.
     * <p>
     * Approach: find first different number
     * 只需要将输入的string按"."split，然后遍历两个string数组，找到第一个不相同的位置，因为版本号中可能存在leading zeroes，因此需要先将string转化为数字，
     * 找到不同的数字后，即可按其大小，返回1或者-1。因为输入版本"1"意味着后续的revision number都是0，所以当遍历溢出某版本号的边界时，将其值视作0即可。
     * 若一直到最后一位，两个版本之间的数字都相同，说明为同一版本，返回0.
     * <p>
     * 值得注意的是，若直接按"."split字符串会得到一个空数组，因此要输入正则表达式"\\."才能正确分割
     * <p>
     * Time: O(max(v1, v2)) 最坏情况下，需要将两个版本字符串都遍历一遍，才能得到结果
     * Space: O(v1 + v2) 需要把两个数组按"."分割，记入新数组中。
     */
    public int compareVersion(String version1, String version2) {
        String[] revisions1 = version1.split("\\.");
        String[] revisions2 = version2.split("\\.");
        //最坏情况下需要将两个字符串都遍历到头
        int maxLength = Math.max(revisions1.length, revisions2.length);
        for (int i = 0; i < maxLength; i++) {
            //若某版本已经溢出边界，将其值赋为0即可，否则从数组中读取当前level的值
            int num1 = (i >= revisions1.length) ? 0 : Integer.parseInt(revisions1[i]);
            int num2 = (i >= revisions2.length) ? 0 : Integer.parseInt(revisions2[i]);
            //只有当前数值不相等时，才能直接判断大小。若相等，需要继续判断
            if (num1 > num2) return 1;
            else if (num1 < num2) return -1;
        }
        return 0;
    }


    @Test
    public void compareVersionTest() {
        /**
         * Example 1:
         * Input: version1 = "0.1", version2 = "1.1"
         * Output: -1
         */
        assertEquals(-1, compareVersion("0.1", "1.1"));
        /**
         * Example 2:
         * Input: version1 = "1.0.1", version2 = "1"
         * Output: 1
         */
        assertEquals(1, compareVersion("1.0.1", "1"));
        /**
         * Example 3:
         * Input: version1 = "7.5.2.4", version2 = "7.5.3"
         * Output: -1
         */
        assertEquals(-1, compareVersion("7.5.2.4", "7.5.3"));
        /**
         * Example 4:
         * Input: version1 = "1.01", version2 = "1.001"
         * Output: 0
         * Explanation: Ignoring leading zeroes, both “01” and “001" represent the same number “1”
         */
        assertEquals(0, compareVersion("1.01", "1.001"));
        /**
         * Example 5:
         * Input: version1 = "1.0", version2 = "1.0.0"
         * Output: 0
         * Explanation: The first version number does not have a third level revision number, which means its third level revision
         * number is default to "0"
         */
        assertEquals(0, compareVersion("1.0", "1.0.0"));
    }
}
