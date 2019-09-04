import org.junit.Test;
import static org.junit.Assert.*;

public class utf8Validation {

    /**
     * A character in UTF8 can be from 1 to 4 bytes long, subjected to the following rules:
     *
     * For 1-byte character, the first bit is a 0, followed by its unicode code.
     * For n-bytes character, the first n-bits are all one's, the n+1 bit is 0, followed by n-1 bytes with most significant 2 bits being 10.
     * This is how the UTF-8 encoding would work:
     *
     *    Char. number range  |        UTF-8 octet sequence
     *       (hexadecimal)    |              (binary)
     *    --------------------+---------------------------------------------
     *    0000 0000-0000 007F | 0xxxxxxx
     *    0000 0080-0000 07FF | 110xxxxx 10xxxxxx
     *    0000 0800-0000 FFFF | 1110xxxx 10xxxxxx 10xxxxxx
     *    0001 0000-0010 FFFF | 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
     * Given an array of integers representing the data, return whether it is a valid utf-8 encoding.
     *
     * Note:
     * The input is an array of integers. Only the least significant 8 bits of each integer is used to store the data. This means each
     * integer represents only 1 byte of data.
     *
     * Approach 1: String Manipulation
     * 本质上，只要严格按照题意给定的要求进行逐个判断即可。
     * 首先对于每一个数字，都只考虑对应least significant的8位二进制数，即若输入数字大于255，那么只取其后8位二进制数，若输入数字小于255（即可能不足8位），则
     * 在二进制表达式前添加0以补足8位。
     * 然后要判断当前的数字是否可以作为一个valid utf8编码的开始位置。因为只考虑1 - 4bytes的utf8编码，所以开头只可能是'0'，'110'，'1110'和'11110'。因此若
     * 开头的1的个数为1或大于4，可以直接返回false。在得到当前的bytes数目后，则需要判断后续的几位是否以'10'开头，即若当前数字是2bytes的utf8编码的开始，那么
     * 下一个数字需要以'10'开头，若不满足条件，则也可以直接返回false。若遍历完整个数组，仍满足条件，则可以返回true
     *
     * Time: O(N) 需要遍历整个数组判断其8位二进制数表达式的值
     * Space: O(N) 对于每个数字都需要将其转换成8位二进制字符串
     */
    public boolean validUtf8String(int[] data) {
        //记录当前数字是以多少bytes开头的utf8编码
        int numOfBytes = 0;
        for(int num : data) {
            //将当前数字转化为二进制表达式
            String bin_rep = Integer.toBinaryString(num);
            int len = bin_rep.length();
            //若当前数字多于8位，则只取least significant的后8位，若不足8位，则在前面补齐0
            bin_rep = len >= 8 ? bin_rep.substring(len - 8) : "00000000".substring(8 - len) + bin_rep;
            //如果之前没有遇到以多位bytes开头的utf8编码，则需要判断当前数字是以多少bytes开头
            if(numOfBytes == 0) {
                for(int i = 0; i < 8; i++) {
                    //遇到第一个0就可以跳出循环
                    if(bin_rep.charAt(i) == '0') {
                        break;
                    }
                    //否则则加下1的个数
                    numOfBytes++;
                }
                //若没有leading的1，说明该位置是1byte的utf8编码，可以继续查看下一个数字
                if(numOfBytes == 0) {
                    continue;
                }
                //如果leading的1的个数大于4或为1，说明不符合条件，直接返回false
                if(numOfBytes > 4 || numOfBytes == 1) {
                    return false;
                }
            } else { //反之说明当前位置不是utf8编码的开头，需要判断后续是否有正确个数的以'10'开头的数字
                //如果当前位置不是以'10'开头，可以直接返回false
                if(!(bin_rep.charAt(0) == '1' && bin_rep.charAt(1) == '0')) {
                    return false;
                }
            }
            //反之，说明该位置符合utf8编码要求，减少需要的'10'开头的数字的个数，然后继续向前遍历
            numOfBytes--;
        }
        //最后需要结果为0
        return numOfBytes == 0;
    }

    /**
     * Approach 2: Bit Manipulation
     * 方法一需要将数字转化为字符串，然后再对字符串进行操作，影响运行效率，可以直接对数字进行bit操作即可。
     * 首先对于判断有多少leading的1,可以将数字与10000000进行and操作，判断第一位是否为1，然后将10000000中的1右移一位变为01000000，直到遇到0为止。
     * 对于判断前两位是否为'10'，则可以将数字分别与10000000和01000000进行and操作，保证该数字与10000000结果不为0（即第一位为1）同时该数字与01000000操作为0
     * （说明第二位为0）即可。
     * 注意10000000 = 1 << 7，即将1中的1向左移动7位，01000000 = 1 << 6即向左移动6位
     *
     * Time: O(n) 仍然需要遍历所有数字
     * Space: O(1) 不需要将数字转化为字符串再进行判断
     */
    public boolean validUtf8Bit(int[] data) {
        int numOfBytes = 0;
        int mask1 = 1 << 7;
        int mask2 = 1 << 6;
        for(int num : data) {
            if(numOfBytes == 0) {
                //判断该数字有多少leading的1
                int mask = 1 << 7;
                while((num & mask) != 0) {
                    numOfBytes++;
                    //将mask当前的1向右移动一位，继续判断是否有leading的1
                    mask = mask >> 1;
                }
                //剩下部分与方法一类似
                if(numOfBytes == 0) {
                    continue;
                }
                if(numOfBytes > 4 || numOfBytes == 1) {
                    return false;
                }
            } else {
                //判断当前数字的二进制数是否为'10'，若不为'10'，可以直接返回false
                if(!((num & mask1) != 0 && (num & mask2) == 0)) {
                    return false;
                }
            }
            numOfBytes--;
        }
        return numOfBytes == 0;
    }

    @Test
    public void validUtf8Test() {
        /**
         * Example 1:
         * data = [197, 130, 1], which represents the octet sequence: 11000101 10000010 00000001.
         *
         * Return true.
         * It is a valid utf-8 encoding for a 2-bytes character followed by a 1-byte character.
         */
        int[] data1 = new int[]{197, 130, 1};
        assertTrue(validUtf8String(data1));
        assertTrue(validUtf8Bit(data1));
        /**
         * Example 2:
         * data = [235, 140, 4], which represented the octet sequence: 11101011 10001100 00000100.
         *
         * Return false.
         * The first 3 bits are all one's and the 4th bit is 0 means it is a 3-bytes character.
         * The next byte is a continuation byte which starts with 10 and that's correct.
         * But the second continuation byte does not start with 10, so it is invalid.
         */
        int[] data2 = new int[]{235, 140, 4};
        assertFalse(validUtf8String(data2));
        assertFalse(validUtf8Bit(data2));
    }
}
