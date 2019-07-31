import org.junit.Test;
import static org.junit.Assert.*;

public class integerToWords {

    /**
     * Convert a non-negative integer to its english words representation. Given input is guaranteed to be less than 2^31 - 1.
     *
     * Approach: Group by three digits
     * 把数字拆成3个3个一组（每组内只有最后接的数量称呼不同thousand, million, billion），其余处理方法完全相同。此题base case极多，分为如下几类：
     *
     * 1.输入0，输出"Zero"
     * 2.输入为1位数字，输出对应"One", "Two", ..., "Nine"
     * 3.输入为小于20的2位数字, 输出对应"Ten", "Eleven", ... , "Nineteen"
     * 4.输入为大于20的2位数字, 可以将其拆分为十位数字和个位数字，个位数字与1位数字处理相同，十位数字按照大小分别输出"Twenty", "Thirty", ... , "Ninety"
     * 5.输入为3位数字，将其拆分为百位数字和后两位数字，后两位数字按2位数字处理，百位数字按1位数字处理，百位数字结果后要加上"Hundred"
     * 6.对于所有情况，若输入数字为0，输出空字符串""
     *
     * 对于输入数字（最大为2^31 - 1，所以数字最大到billion），将其拆分为billion位，million位，thousand位和后三位，若其中任意数字不为0，则按三位数字依次
     * 处理，如处理该位之前，输出字符串中已有元素，则记得加上空格与前一位分隔
     *
     * Time: O(n)，runtime和输入数字的位数有关，要1位1位的看
     * Space: O(1), 输入的数字空间为定值，因此字符串的长度最大会以billion开头，长度接近定值
     *
     */
    public String numberToWords(int num) {
        //base case，如果输入为0，直接返回"Zero"
        if(num == 0) return "Zero";
        //有了以下函数，可以将输入数字3个3个一组返回
        //注意输入数字最大数为billion
        int billion = num / 1000000000;
        int million = (num - billion * 1000000000) / 1000000;
        int thousand = (num - billion * 1000000000 - million * 1000000) / 1000;
        int rest = num - billion * 1000000000 - million * 1000000 - thousand * 1000;
        //用string builder存储最终结果
        StringBuilder res = new StringBuilder();
        //只要每一组内数字不为0，就需要将其结果返回
        //注意每个数字都是小于等于3位的数字，可直接用threeDigits函数处理
        if(billion != 0) {
            res.append(threeDigits(billion));
            res.append(" Billion");
        }
        if(million != 0) {
            //需要注意的是，若更大的数位已经有值，需要在加入新数字之前插入空格与之前结果分隔
            if(res.length() != 0) {
                res.append(" ");
            }
            res.append(threeDigits(million));
            res.append(" Million");
        }
        if(thousand != 0) {
            if(res.length() != 0) {
                res.append(" ");
            }
            res.append(threeDigits(thousand));
            res.append(" Thousand");
        }
        if(rest != 0) {
            if(res.length() != 0) {
                res.append(" ");
            }
            res.append(threeDigits(rest));
        }
        return res.toString();
    }


    //根据以下函数，可以处理任意3位数字
    private String threeDigits(int num) {
        //将3位数字拆成百位数字和后两位数字，后两位数字可由twoDigits函数直接输出结果
        int hundred = num / 100;
        int rest = num - hundred * 100;
        //若百位数和后两位数都不为0，则结果都要输出
        if(hundred != 0 && rest != 0) {
            //需注意hundred前后的空格
            return oneDigit(hundred) + " Hundred " + twoDigits(rest);
        } else if(hundred != 0 && rest == 0) {
            //如果百位数字不为0，后两位为0, (e.g. 100)
            //只需返回百位数字即可
            return oneDigit(hundred) + " Hundred";
        } else if(hundred == 0 && rest != 0) {
            //如果百位数字为0，即输入数字其实是小于等于2位的数字，直接返回剩余结果
            return twoDigits(rest);
        }
        //其他情况，说明输入是0，需要跳过该结果，返回空字符串
        return "";
    }


    //根据以下函数，可以处理任意小于等于2位的数字
    private String twoDigits(int num) {
        //注意base case，如果输入数字可被10整除，(e.g. 10010)，需要跳过中间的0
        if(num == 0) return "";
        //若输入数字为1位整数，可直接输出
        else if(num < 10) return oneDigit(num);
        //或输入数字为小于20的2位整数，也直接输出
        else if(num < 20) return twoDigitsLessThan20(num);
        else {
            //除此之外，需要将十位数字和个位数字拆分计算
            int tenner = num / 10;
            int rest = num % 10;
            //如果个位数字不为0，则两位数字都要输出
            if(rest != 0) {
                return ten(tenner) + " " + oneDigit(rest);
            } else {
                //如果个位数字为0，则只输出十位数字即可
                return ten(tenner);
            }
        }
    }

    //处理非0的1位数字
    private String oneDigit(int num) {
        //注意switch的应用
        switch (num) {
            case 1: return "One";
            case 2: return "Two";
            case 3: return "Three";
            case 4: return "Four";
            case 5: return "Five";
            case 6: return "Six";
            case 7: return "Seven";
            case 8: return "Eight";
            case 9: return "Nine";
        }
        //其他情况（e.g. 0)返回空字符串
        return "";
    }

    //处理小于20的2位数字
    private String twoDigitsLessThan20(int num) {
        switch (num) {
            case 10: return "Ten";
            case 11: return "Eleven";
            case 12: return "Twelve";
            case 13: return "Thirteen";
            case 14: return "Fourteen";
            case 15: return "Fifteen";
            case 16: return "Sixteen";
            case 17: return "Seventeen";
            case 18: return "Eighteen";
            case 19: return "Nineteen";
        }
        return "";
    }

    //处理十位数字
    private String ten(int num) {
        switch (num) {
            case 2: return "Twenty";
            case 3: return "Thirty";
            case 4: return "Forty";
            case 5: return "Fifty";
            case 6: return "Sixty";
            case 7: return "Seventy";
            case 8: return "Eighty";
            case 9: return "Ninety";
        }
        return "";
    }



    @Test
    public void numberToWordsTest() {
        /**
         * Example 1:
         * Input: 0
         * Output: "Zero"
         */
        assertEquals("Zero", numberToWords(0));
        /**
         * Example 2:
         * Input: 123
         * Output: "One Hundred Twenty Three"
         */
        assertEquals("One Hundred Twenty Three", numberToWords(123));
        /**
         * Example 3:
         * Input: 12345
         * Output: "Twelve Thousand Three Hundred Forty Five"
         */
        assertEquals("Twelve Thousand Three Hundred Forty Five", numberToWords(12345));
        /**
         * Example 4:
         * Input: 1234567
         * Output: "One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven"
         */
        assertEquals("One Million Two Hundred Thirty Four Thousand Five Hundred Sixty Seven", numberToWords(1234567));
        /**
         * Example 5:
         * Input: 1234567891
         * Output: "One Billion Two Hundred Thirty Four Million Five Hundred Sixty Seven Thousand Eight Hundred Ninety One"
         */
        assertEquals("One Billion Two Hundred Thirty Four Million Five Hundred Sixty Seven Thousand Eight Hundred Ninety One",
                numberToWords(1234567891));
        /**
         * Example 6:
         * Input: 1000000
         * Output: "One Million"
         */
        assertEquals("One Million", numberToWords(1000000));
        /**
         * Example 7;
         * Input: 1000010
         * Output: "One Million Ten"
         */
        assertEquals("One Million Ten", numberToWords(1000010));
    }
}
