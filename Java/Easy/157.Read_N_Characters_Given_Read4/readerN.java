public class readerN extends Reader4 {

    /**
     * Given a file and assume that you can only read the file using a given method read4, implement a method to read n characters.
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
     * Approach: Brute force
     *
     * The algorithm looks like this
     *
     * 1. Call read4 at each time, and store the character into an array of size 4
     * 2. Copy these characters to the buffer array
     * 3. Check ending point, which is read4 return 0, which means we have nothing to read, or we have already read n characters
     *
     * @param buf Destination buffer
     * @param n   Number of characters to read
     * @return    The number of actual characters read
     */
    public int read(char[] buf, int n) {
        int charReadSofar = 0;
        char[] letters = new char[4];
        int num = read4(letters);
        while(num > 0) { //as long as we can read something
            //copy them into the buffer array
            for(int i = 0; i < num; i++) {
                if(charReadSofar == n) { //if we have read n characters
                    break;
                }
                //otherwise, copy them into buffer array
                buf[charReadSofar++] = letters[i];
            }
            //keep reading characters
            num = read4(letters);
        }
        return charReadSofar;
    }
}
