import java.util.*;

/**
 * Given a string, determine if it is a palindrome, considering only alphanumeric characters and ignoring cases.
 * Note: For the purpose of this problem, we define empty string as valid palindrome.
 * Remember Character.isLetterOrDigit() (Character.isLetter()), Character.toLowerCase() API for this problem
 * Use deque to verify valid palindrome
 */
public class Valid_Palindrome_1 {
    
    public static boolean isPalindrome(String s) {
        Deque<Character> letterDeque = new ArrayDeque<>();
        for(int i = 0; i < s.length(); i++) {
            char letter = s.charAt(i);
            if(Character.isLetterOrDigit(letter)) {
                letterDeque.add(Character.toLowerCase(letter));
            }
        }
        while(letterDeque.size() > 1) {
            if(letterDeque.removeLast() != letterDeque.removeFirst()) return false;
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