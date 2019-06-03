public class Valid_Palindrome_2 {
    
    public static boolean isPalindrome(String s) {
        int i = 0;
        int j = s.length() - 1;
        s = s.toLowerCase();
        while(i < j) {
            if(i < s.length() && !Character.isLetterOrDigit(s.charAt(i))) {
                i += 1;
                continue;
            }
            if(j >= 0 && !Character.isLetterOrDigit(s.charAt(j))) {
                j -= 1;
                continue;
            }
            if(i < j && s.charAt(i) != s.charAt(j)) return false;
            i += 1;
            j -= 1;
        }
    return true;
    }

    public static void main(String[] args) {
        String s1 = "A man, a plan, a canal: Panama";
        System.out.println("Expected: true Actual: " + isPalindrome(s1));
        String s2 = "race a car";
        System.out.println("Expected: false Actual: " + isPalindrome(s2));
        String s3 = "race car";
        System.out.println("Expected: true Actual: " + isPalindrome(s3));
    }
}