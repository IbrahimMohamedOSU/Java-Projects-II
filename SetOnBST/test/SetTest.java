import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Ibrahim Mohamed
 *
 */
public abstract class SetTest {

    /**
     * Invokes the appropriate {@code Set} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new set
     * @ensures constructorRef = {}
     */
    protected abstract Set<String> constructorRef();

    /**
     * Creates and returns a {@code Set<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsTest = [entries in args]
     */
    private Set<String> createFromArgsTest(String... args) {
        Set<String> set = this.constructorTest();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Creates and returns a {@code Set<String>} of the reference implementation
     * type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsRef = [entries in args]
     */
    private Set<String> createFromArgsRef(String... args) {
        Set<String> set = this.constructorRef();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    // TODO - add test cases for constructor, add, remove, removeAny, contains, and size

    /**
     * Test case for an empty Constructor.
     */
    @Test
    public void emptyConstructorTest() {
        Set<String> test = this.constructorTest();
        Set<String> ref = this.constructorRef();
        assertEquals(ref, test);

    }

    /**
     * Test case for non-empty add with multiple entries.
     */
    @Test
    public void nonEmptyAddMultiple() {
        Set<String> test = this.createFromArgsTest("Cis", "Cse");
        Set<String> ref = this.createFromArgsRef("Cis", "Cse", "Ece");
        test.add("Ece");
        assertEquals(ref, test);
    }

    /**
     * Test case for Empty add method.
     */
    @Test
    public void emptyAdd() {
        Set<String> test = this.createFromArgsTest();
        Set<String> ref = this.createFromArgsRef("Ece");
        test.add("Ece");
        assertEquals(ref, test);
    }

    /**
     * Test case for non-empty remove right method.
     */
    @Test
    public void nonEmptyRemoveRight() {
        Set<String> test = this.createFromArgsTest("Cis", "Cse");
        Set<String> ref = this.createFromArgsRef("Cis");
        String check = test.remove("Cse");
        assertEquals(ref, test);
        assertEquals("Cse", check);
    }

    /**
     * Test case for non-empty remove left method.
     */
    @Test
    public void nonEmptyRemoveLeft() {
        Set<String> test = this.createFromArgsTest("Cis", "Ae");
        Set<String> ref = this.createFromArgsRef("Cis");
        String check = test.remove("Ae");
        assertEquals(ref, test);
        assertEquals("Ae", check);
    }

    /**
     * Test case for non-empty remove root method.
     */
    @Test
    public void nonEmptyRemoveRoot() {
        Set<String> test = this.createFromArgsTest("Cis", "Ae", "Ece");
        Set<String> ref = this.createFromArgsRef("Ae", "Ece");
        String check = test.remove("Cis");
        assertEquals(ref, test);
        assertEquals("Cis", check);
    }

    /**
     * Test case for Empty remove method.
     */
    @Test
    public void emptyRemove() {
        Set<String> test = this.createFromArgsTest("Cis");
        Set<String> ref = this.createFromArgsRef();
        String check = test.remove("Cis");
        assertEquals(ref, test);
        assertEquals("Cis", check);
    }

    /**
     * Test case for non-empty (Multiple set) removeAny method.
     */
    @Test
    public void nonEmptyRemoveAny() {
        Set<String> test = this.createFromArgsTest("Cis", "Cse", "Ece");
        Set<String> ref = this.createFromArgsRef("Cis", "Cse", "Ece");
        String t = test.removeAny();
        assertEquals(true, ref.contains(t));
        ref.remove(t);
        assertEquals(ref, test);

    }

    /**
     * Test case for non-empty (One set) removeAny method.
     */
    @Test
    public void emptyRemoveAny() {
        Set<String> test = this.createFromArgsTest("Cis");
        Set<String> ref = this.createFromArgsRef("Cis");
        String t = test.removeAny();
        assertEquals(true, ref.contains(t));
        ref.remove(t);
        assertEquals(ref, test);

    }

    /**
     * Test case for non-empty (multiple sets) contain method (true).
     */
    @Test
    public void nonEmptyContains1() {
        Set<String> test = this.createFromArgsTest("Cis", "Cse");
        Set<String> setCopy = this.createFromArgsRef("Cis", "Cse");

        boolean checker = test.contains("Cse");
        boolean ref = true;

        assertEquals(ref, checker);
        assertEquals(setCopy, test);
    }

    /**
     * Test case for non-empty (multiple sets) contain method (false).
     */
    @Test
    public void nonEmptyContains2() {
        Set<String> test = this.createFromArgsTest("Cis", "Cse");
        Set<String> setCopy = this.createFromArgsRef("Cis", "Cse");

        boolean checker = test.contains("Ece");
        boolean ref = false;

        assertEquals(ref, checker);
        assertEquals(setCopy, test);
    }

    /**
     * Test case for non-empty (one set) contain method.
     */
    @Test
    public void nonEmptyContains3() {
        Set<String> test = this.createFromArgsTest("Cis");
        Set<String> setCopy = this.createFromArgsRef("Cis");

        boolean checker = test.contains("Cis");
        boolean ref = true;

        assertEquals(ref, checker);
        assertEquals(setCopy, test);
    }

    /**
     * Test case for empty contain method.
     */
    @Test
    public void emptyContains() {
        Set<String> test = this.createFromArgsTest();
        Set<String> setCopy = this.createFromArgsRef();

        boolean checker = test.contains("Cis");
        boolean ref = false;

        assertEquals(ref, checker);
        assertEquals(setCopy, test);
    }

    /**
     * Test case for non-empty (with Multiple sets) size method.
     */
    @Test
    public void sizeNonEmpty1() {
        Set<String> test = this.createFromArgsTest("Cis", "Cse");
        Set<String> setCopy = this.createFromArgsRef("Cis", "Cse");

        int checker = test.size();
        int ref = 2;

        assertEquals(ref, checker);
        assertEquals(setCopy, test);
    }

    /**
     * Test case for non-empty (with one set) size method.
     */
    @Test
    public void sizeNonEmpty2() {
        Set<String> test = this.createFromArgsTest("Cis");
        Set<String> setCopy = this.createFromArgsRef("Cis");

        int checker = test.size();
        int ref = 1;

        assertEquals(ref, checker);
        assertEquals(setCopy, test);
    }

    /**
     * Test case for empty size method.
     */
    @Test
    public void sizeEmpty() {
        Set<String> test = this.createFromArgsTest();
        Set<String> setCopy = this.createFromArgsRef();

        int checker = test.size();
        int ref = 0;

        assertEquals(ref, checker);
        assertEquals(setCopy, test);
    }

}
