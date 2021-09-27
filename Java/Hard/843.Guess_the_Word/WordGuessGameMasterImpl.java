public class WordGuessGameMasterImpl implements Master {

    private final String secretWord;

    public WordGuessGameMasterImpl(String secretWord) {
        this.secretWord = secretWord;
    }

    @Override
    public int guess(String guessWord) {
        int count = 0;
        for (int i = 0; i < guessWord.length(); i++) {
            if (secretWord.charAt(i) == guessWord.charAt(i)) {
                count++;
            }
        }
        return count;
    }
}
