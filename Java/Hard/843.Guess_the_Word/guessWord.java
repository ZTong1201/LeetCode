import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class guessWord {

    /**
     * We are given a word list of unique words, each word is 6 letters long, and one word in this list is chosen as secret.
     * <p>
     * You may call master.guess(word) to guess a word.  The guessed word should have type string and must be from the original list with
     * 6 lowercase letters.
     * <p>
     * This function returns an integer type, representing the number of exact matches (value and position) of your guess to the secret
     * word.  Also, if your guess is not in the given wordlist, it will return -1 instead.
     * <p>
     * For each test case, you have 10 guesses to guess the word. At the end of any number of calls, if you have made 10 or less calls to
     * master.guess and at least one of these guesses was the secret, you pass the testcase.
     * <p>
     * Besides the example test case below, there will be 5 additional test cases, each with 100 words in the word list.  The letters of
     * each word in those testcases were chosen independently at random from 'a' to 'z', such that every word in the given word lists is
     * unique.
     * <p>
     * Note:  Any solutions that attempt to circumvent the judge will result in disqualification.
     * <p>
     * Constraints:
     * <p>
     * 1 <= wordlist.length <= 100
     * wordlist[i].length == 6
     * wordlist[i] consist of lowercase English letters.
     * All the strings of wordlist are unique.
     * secret exists in wordlist.
     * numguesses == 10
     * <p>
     * Approach: Guess Word with Less Zero Match
     * 本质上，此题是要找到一个搜索策略，即希望根据当前结果，能得到最小的搜索空间。假设有所有的长度为6的字母组合，对于任意一个单词X，和它完全不一样的单词数量为
     * 25^6，有5个位置不一样的数量为C(6, 1) * (25^5)，有4个位置不一样C(6, 2) * (25^4)，意味着若某单词和wordlist中的其他单词match数量越大，说明接下来的
     * 搜索空间越小。而若某个单词与其他所有单词的zero match数量越大，说明该次guess不是一个好的guess。解决此题的要点为舍弃尽可能多的zero match
     * 同时增加randomness。
     * <p>
     * 所以搜索策略如下：
     * 1. 每次从当前单词list中随机选取一个单词作为candidate
     * 2. 若当前单词就是secret word，直接返回
     * 3. 若当前单词不是secret word，则能知道该单词正确字符的个数num，在剩下单词中仅搜索与当前单词字符匹配数目为num的单词，将这些单词作为新的备选单词，继续
     * 重复上述步骤
     * Time: O(n^2) 对于每个单词，都需要和其他所有单词进行对比，记录每个单词zero match的个数，第一次猜测的时候需要两个nested loop搜索整个wordlist
     * Space: O(n) 需要一个list记录当前candidate words，同时要记录每个candidate与其他剩余单词zero match的个数
     */
    public void findSecretWord(String[] wordlist, Master master) {
        List<String> candidates = new ArrayList<>(Arrays.asList(wordlist));
        //最多只能猜10次
        for (int i = 0; i < 10; i++) {
            //增加随机性，每次从当前list中选取一个随机单词
            Random random = new Random();
            int pick = random.nextInt(candidates.size());
            String guessWord = candidates.get(pick);

            //若当前单词就是secret word，可以直接返回
            int numOfMatches = master.guess(guessWord);
            if (numOfMatches == 6) return;
            //否则就在剩余单词中找到与当前单词匹配数目为numOfMatches的单词，作为新的candidates
            List<String> temp = new ArrayList<>();
            for (String word : candidates) {
                // no need to add the guessed word into candidate list again
                if (!word.equals(guessWord) && match(guessWord, word) == numOfMatches) {
                    temp.add(word);
                }
            }
            candidates = temp;
        }
    }

    //对比两个单词，得到match的字符的个数
    private int match(String s1, String s2) {
        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void findSecretWordTest() {
        /**
         * Example:
         * Input: secret = "acckzz", wordlist = ["acckzz","ccbazz","eiowzz","abcczz"]
         *
         * Explanation:
         *
         * master.guess("aaaaaa") returns -1, because "aaaaaa" is not in wordlist.
         * master.guess("acckzz") returns 6, because "acckzz" is secret and has all 6 matches.
         * master.guess("ccbazz") returns 3, because "ccbazz" has 3 matches.
         * master.guess("eiowzz") returns 2, because "eiowzz" has 2 matches.
         * master.guess("abcczz") returns 4, because "abcczz" has 4 matches.
         *
         * We made 5 calls to master.guess and one of them was the secret, so we pass the test case.
         */
        WordGuessGameMasterImpl wordGuessGameMaster = new WordGuessGameMasterImpl("acckzz");
        findSecretWord(new String[]{"acckzz", "ccbazz", "eiowzz", "abcczz"}, wordGuessGameMaster);
    }
}
