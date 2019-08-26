public class ReaderN extends Reader4 {

    /**
     * Given a file and assume that you can only read the file using a given method read4, implement a method read to read n characters.
     * Your method read may be called multiple times.
     *
     *
     *
     * Method read4:
     *
     * The API read4 reads 4 consecutive characters from the file, then writes those characters into the buffer array buf.
     *
     * The return value is the number of actual characters read.
     *
     * Note that read4() has its own file pointer, much like FILE *fp in C.
     *
     * Method read:
     *
     * By using the read4 method, implement the method read that reads n characters from the file and store it in the buffer array buf.
     * Consider that you cannot manipulate the file directly.
     *
     * The return value is the number of actual characters read.
     *
     * Definition of read:
     *
     *     Parameters:	char[] buf, int n
     *     Returns:	int
     *
     * Note: buf[] is destination not source, you will need to write the results to buf[]
     *
     * Note:
     *
     * Consider that you cannot manipulate the file directly, the file is only accesible for read4 but not for read.
     * The read function may be called multiple times.
     * Please remember to RESET your class variables declared in Solution, as static/class variables are persisted across multiple test cases.
     * You may assume the destination buffer array, buf, is guaranteed to have enough space for storing n characters.
     * It is guaranteed that in a given test case the same buffer buf is called by read
     *
     *
     * 因为该函数要不call很多次，所以有如下几种特殊情况需要考虑：
     * 1.假设可以从文件中读出4个字符，但是此次需要写入buf的字符数少于4个，因此要先写入需要的n个字符，剩余的4 - n个字符等到下次call这个method时，要先被写入
     * 2.无法从文件读出4个字符，即文件已经读完，那么把剩余的这些字符读完后，之后再call这个method都不应该再继续写入任何字符
     *
     * 因此需要用global variable记录此次读到的4个字符，同时记录从此次读的4个字符当中，又读到了第几个字符。因为可能出现一次无法读入4个字符的情况，因此
     * 还需要另一个变量记录当前读入的字符总数
     */
    private char[] cache;
    private int size;
    private int cacheStart;

    public ReaderN() {
        this.cache = new char[4];
        this.size = 0;
        this.cacheStart = 0;
    }
    public int read(char[] buf, int n) {
        //将此次写入buf的开始位置
        int bufferStart = 0;
        //用一个boolean变量记录该文件是否读完
        boolean eof = false;
        //记录总共需要写入的字符个数，在写的过程，不断从n减去写入的字符数，那么最后N - n就是此次写入的字符总数
        int N = n;
        //若文件尚未读完（或者上一次读取的字符还未写完），并且需要写入字符时
        while((!eof || cacheStart < size) && n > 0) {
            if(cacheStart < size) {
                //先将上次剩余字符写入buffer
                buf[bufferStart++] = cache[cacheStart++];
                //然后减少需要写入字符数
                n--;
            }
            //如果上次剩余的字符写完了，但还需要写入一些字符的话，就重新从文件中读取字符
            if(cacheStart == size) {
                //注意读取的字符数可能小于4，即文件已被读完
                size = read4(cache);
                //更新从cache写入字符的位置，同时更新该文件已被读完
                cacheStart = 0;
                eof = size != 4;
            }
        }
        //返回此次写入的总字符数
        return N - n;
    }


    /**
     * Example 1:
     * File file("abc");
     * Solution sol;
     * // Assume buf is allocated and guaranteed to have enough space for storing all characters from the file.
     * sol.read(buf, 1); // After calling your read method, buf should contain "a". We read a total of 1 character from the file, so return 1.
     * sol.read(buf, 2); // Now buf should contain "bc". We read a total of 2 characters from the file, so return 2.
     * sol.read(buf, 1); // We have reached the end of file, no more characters can be read. So return 0.
     */

    /**
     * Example 2:
     * File file("abc");
     * Solution sol;
     * sol.read(buf, 4); // After calling your read method, buf should contain "abc". We read a total of 3 characters from the file,
     *                      so return 3.
     * sol.read(buf, 1); // We have reached the end of file, no more characters can be read. So return 0.
     */
}
