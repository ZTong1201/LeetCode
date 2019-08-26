public class Reader4 {

    /**
     * Definition of read4:
     *
     *     Parameter:  char[] buf
     *     Returns:    int
     *
     * Note: buf[] is destination not source, the results from read4 will be copied to buf[]
     * Below is a high level example of how read4 works:
     *
     * File file("abcdefghijk"); // File is "abcdefghijk", initially file pointer (fp) points to 'a'
     * char[] buf = new char[4]; // Create buffer with enough space to store characters
     * read4(buf); // read4 returns 4. Now buf = "abcd", fp points to 'e'
     * read4(buf); // read4 returns 4. Now buf = "efgh", fp points to 'i'
     * read4(buf); // read4 returns 3. Now buf = "ijk", fp points to end of file
     */
    public int read4(char[] buf) {
        return 4;
    }
}
