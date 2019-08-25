import org.junit.Test;
import static org.junit.Assert.*;

public class RandomizedCollectionTest {

    /**
     * // Init an empty collection.
     * RandomizedCollection collection = new RandomizedCollection();
     *
     * // Inserts 1 to the collection. Returns true as the collection did not contain 1.
     * collection.insert(1);
     *
     * // Inserts another 1 to the collection. Returns false as the collection contained 1. Collection now contains [1,1].
     * collection.insert(1);
     *
     * // Inserts 2 to the collection, returns true. Collection now contains [1,1,2].
     * collection.insert(2);
     *
     * // getRandom should return 1 with the probability 2/3, and returns 2 with the probability 1/3.
     * collection.getRandom();
     *
     * // Removes 1 from the collection, returns true. Collection now contains [1,2].
     * collection.remove(1);
     *
     * // getRandom should return 1 and 2 both equally likely.
     * collection.getRandom();
     */
    @Test
    public void RandomizedCollectionTest1() {
        RandomizedCollection randomizedCollection = new RandomizedCollection();
        assertTrue(randomizedCollection.insert(1));
        assertFalse(randomizedCollection.insert(1));
        assertTrue(randomizedCollection.insert(2));
        assertTrue(randomizedCollection.getRandom() == 1 || randomizedCollection.getRandom() == 2);
        assertTrue(randomizedCollection.remove(1));
        assertTrue(randomizedCollection.getRandom() == 1 || randomizedCollection.getRandom() == 2);
    }

    @Test
    public void RandomizedCollectionTest2() {
        RandomizedCollection randomizedCollection = new RandomizedCollection();
        assertTrue(randomizedCollection.insert(0));
        assertTrue(randomizedCollection.remove(0));
        assertTrue(randomizedCollection.insert(-1));
        assertFalse(randomizedCollection.remove(0));
        assertTrue(randomizedCollection.getRandom() == -1);
        assertTrue(randomizedCollection.getRandom() == -1);
    }
}
