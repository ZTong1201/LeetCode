import org.junit.Test;
import static org.junit.Assert.*;

public class wordDistanceTest {

    /**
     * Assume that words = ["practice", "makes", "perfect", "coding", "makes"].
     *
     * Input: word1 = “coding”, word2 = “practice”
     * Output: 3
     * Input: word1 = "makes", word2 = "coding"
     * Output: 1
     */
    @Test
    public void wordDistanceTest() {
        String[] words = new String[]{"practice", "makes", "perfect", "coding", "makes"};
        wordDistance wordDistance = new wordDistance(words);
        assertEquals(3, wordDistance.shortest("coding", "practice"));
        assertEquals(1, wordDistance.shortest("makes", "coding"));
    }
}
