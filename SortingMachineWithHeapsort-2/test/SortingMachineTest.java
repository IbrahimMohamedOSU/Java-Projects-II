import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author Ibrahim Mohamed
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /*
     * Sample test cases.
     */

    /**
     * Sample Test case for the Constructor method.
     */
    @Test
    public final void testConstructor() {
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        assertEquals(mExpected, m);
    }

    /**
     * Sample Test case for the add method that is empty.
     */
    @Test
    public final void testAddEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        m.add("green");
        assertEquals(mExpected, m);
    }

    /**
     * Test case for the add method that is non-empty. (one entry)
     */
    @Test
    public void testAddNonEmpty1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "Osu");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "Osu", "Red");
        m.add("Red");
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the add method that is non-empty. (multiple entries)
     */
    @Test
    public void testAddNonEmpty2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "Osu",
                "Red");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "Osu", "Red", "Bucks");
        m.add("Bucks");
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the changeToExtractionMode method that is empty.
     */
    @Test
    public void changeToExtractionModeEmptyTest() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        m.changeToExtractionMode();

        assertEquals(mExpected, m);

    }

    /**
     * Test case for the changeToExtractionMode method that is non-empty. (one
     * entry)
     */
    @Test
    public void changeToExtractionModeTest1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "Osu");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "Osu");
        m.changeToExtractionMode();

        assertEquals(mExpected, m);

    }

    /**
     * Test case for the changeToExtractionMode method that is non-empty
     * (multiple entries).
     */
    @Test
    public void changeToExtractionModeTest2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "Osu",
                "Cse", "Student");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "Osu", "Cse", "Student");
        m.changeToExtractionMode();

        assertEquals(mExpected, m);

    }

    /**
     * Test case for the removeFirst method that is empty.
     */
    @Test
    public final void removeFirstEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "Sports");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        String removedTest = m.removeFirst();
        assertEquals("Sports", removedTest);
        assertEquals(mExpected, m);
    }

    /**
     * Test case for the removeFirst method that is non-empty (multiple
     * entries).
     */
    @Test
    public final void removeFirstNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "Sports", "Basketball");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "Sports");
        String removedTest = m.removeFirst();
        assertEquals("Basketball", removedTest);
        assertEquals(mExpected, m);
    }

    /**
     * Test case for the isInsertionMode method where it is true (one entry).
     */
    @Test
    public void isInsertionModeTest1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "Osu");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "Osu");
        boolean modeTest = m.isInInsertionMode();
        assertEquals(true, modeTest);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the isInsertionMode method where it is true (multiple
     * entries).
     */
    @Test
    public void isInsertionModeTest2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "Osu",
                "Cse", "Student");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "Osu", "Cse", "Student");
        boolean modeTest = m.isInInsertionMode();
        assertEquals(true, modeTest);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the isInsertionMode method where it is false (non-empty).
     */
    @Test
    public void isInsertionModeTest3() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "Osu");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "Osu");
        boolean modeTest = m.isInInsertionMode();
        assertEquals(false, modeTest);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the isInsertionMode method where it is false (empty).
     */
    @Test
    public void isInsertionModeTest4() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        boolean modeTest = m.isInInsertionMode();
        assertEquals(false, modeTest);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the order method where it is Empty (false).
     */
    @Test
    public void orderTestEmpty1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        Comparator<String> test = m.order();
        assertEquals(ORDER, test);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the order method where it is Empty (true).
     */
    @Test
    public void orderTestEmpty2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        Comparator<String> test = m.order();
        assertEquals(ORDER, test);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the order method where it is Non-Empty (true).
     */
    @Test
    public void orderTestNonEmpty1() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "Osu",
                "Cse", "Student");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "Osu", "Cse", "Student");
        Comparator<String> test = m.order();
        assertEquals(ORDER, test);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the order method where it is Non-Empty (false).
     */
    @Test
    public void orderTestNonEmpty2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "Osu",
                "Cse", "Student");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "Osu", "Cse", "Student");
        Comparator<String> test = m.order();
        assertEquals(ORDER, test);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the size method where it is Non-Empty (multiple entries,
     * false).
     */
    @Test
    public void sizeNonEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "Osu",
                "Cse", "Student");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "Osu", "Cse", "Student");
        int sizeResult = m.size();
        assertEquals(3, sizeResult);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the size method where it is Non-Empty (multiple entries,
     * true).
     */
    @Test
    public void sizeNonEmpty2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "Osu",
                "Cse");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "Osu", "Cse");
        int sizeResult = m.size();
        assertEquals(2, sizeResult);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the size method where it is Empty (true).
     */
    @Test
    public void sizeEmpty() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        int sizeResult = m.size();
        assertEquals(0, sizeResult);
        assertEquals(mExpected, m);

    }

    /**
     * Test case for the size method where it is Empty (false).
     */
    @Test
    public void sizeEmpty2() {
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        int sizeResult = m.size();
        assertEquals(0, sizeResult);
        assertEquals(mExpected, m);

    }
}
