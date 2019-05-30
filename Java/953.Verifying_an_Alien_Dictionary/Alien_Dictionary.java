import java.util.*;
/**
 * In an alien language, surprisingly they also use
 * english lowercase letters, but possibly in a different order.
 * The order of the alphabet is some permutation of lowercase letters.
 * Given a sequence of words written in the alien language, 
 * and the order of the alphabet, 
 * return true if and only if the given words are sorted lexicographicaly in this alien language.
 */

public class Alien_Dictionary {

    public static boolean isAlienSorted(String[] words, String order) {
        int alienOrder = 0;
        Map<Character, Integer> alienDict = new HashMap<>();
        for(int i = 0; i < order.length(); i++) {
            alienDict.put(order.charAt(i), alienOrder);
            alienOrder += 1;
        }
        for(int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            int minLength = Math.min(word1.length(), word2.length());
            int j;
            for(j = 0; j < minLength; j++) {
                if(word1.charAt(j) != word2.charAt(j)) {
                    if(alienDict.get(word1.charAt(j)) > alienDict.get(word2.charAt(j))) {
                        return false;
                    }
                    break;
                }
            }
            if(j == minLength && word1.length() > word2.length()) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String[] words1 = new String[]{"hello","leetcode"};
        String order1 = "hlabcdefgijkmnopqrstuvwxyz";
        System.out.println("Expected: true Acutal: " + isAlienSorted(words1, order1));
        String[] words2 = new String[]{"word","world","row"};
        String order2 = "worldabcefghijkmnpqstuvxyz";
        System.out.println("Expected: false Acutal: " + isAlienSorted(words2, order2));
        String[] words3 = new String[]{"apple","app"};
        String order3 = "abcdefghijklmnopqrstuvwxyz";
        System.out.println("Expected: false Acutal: " + isAlienSorted(words3, order3));
        
    }

}