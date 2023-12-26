import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Ibrahim Mohamed
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    // TODO - add test cases for constructor, add, remove, removeAny, value,
    // hasKey, and size

    /**
     * Test case for an empty Constructor.
     */
    @Test
    public void noArgumentConstructer() {
        Map<String, String> test = this.constructorTest();
        Map<String, String> ref = this.constructorRef();
        assertEquals(ref, test);

    }

    /**
     * Test case for empty add method.
     */
    @Test
    public void emptyAdd() {
        Map<String, String> test = this.createFromArgsTest();
        test.add("Soccer", "Basketball");
        Map<String, String> ref = this.createFromArgsRef("Soccer",
                "Basketball");
        assertEquals(ref, test);

    }

    /**
     * Test case for non-empty add method.
     */
    @Test
    public void nonEmptyAdd() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye");
        test.add("Soccer", "Basketball");
        Map<String, String> ref = this.createFromArgsRef("Hey", "Bye", "Soccer",
                "Basketball");
        assertEquals(ref, test);

    }

    /**
     * Test case for non-empty remove method.
     */
    @Test
    public void removeNonEmpty() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye",
                "Soccer", "Basketball");
        test.remove("Hey");
        Map<String, String> ref = this.createFromArgsRef("Soccer",
                "Basketball");
        assertEquals(ref, test);

    }

    /**
     * Test case for empty remove method.
     */
    @Test
    public void removeEmpty() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye");
        test.remove("Hey");
        Map<String, String> ref = this.createFromArgsRef();
        assertEquals(ref, test);

    }

    /**
     * Test case for non-empty (multiple pairs) removeAny method.
     */
    @Test
    public void removeAnyNonEmpty1() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye",
                "Soccer", "Basketball");

        Map<String, String> ref = this.createFromArgsRef("Hey", "Bye", "Soccer",
                "Basketball");
        Map.Pair<String, String> remove = test.removeAny();
        assertEquals(true, ref.hasKey(remove.key()));

        ref.remove(remove.key());
        assertEquals(ref, test);

    }

    /**
     * Test case for non-empty (one pair) removeAny method.
     */
    @Test
    public void removeAnyNonEmpty2() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye");

        Map<String, String> ref = this.createFromArgsRef("Hey", "Bye");
        Map.Pair<String, String> remove = test.removeAny();
        assertEquals(true, ref.hasKey(remove.key()));

        ref.remove(remove.key());
        assertEquals(ref, test);

    }

    /**
     * Test case for non-empty (one pair) value method.
     */
    @Test
    public void valueNonEmpty1() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye");
        Map<String, String> mapCopy = this.createFromArgsRef("Hey", "Bye");

        String checker = test.value("Hey");
        String ref = "Bye";

        assertEquals(ref, checker);
        // check if the map is preserved.
        assertEquals(mapCopy, test);

    }

    /**
     * Test case for non-empty (multiple pairs) value method.
     */
    @Test
    public void valueNonEmpty2() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye",
                "Soccer", "Basketball");
        Map<String, String> mapCopy = this.createFromArgsRef("Hey", "Bye",
                "Soccer", "Basketball");

        String checker = test.value("Soccer");
        String ref = "Basketball";

        assertEquals(ref, checker);
        // check if the map is preserved.
        assertEquals(mapCopy, test);

    }

    /**
     * Test case for non-empty (multiple pairs) hasKey method (true).
     */
    @Test
    public void hasKeyNonEmpty1() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye",
                "Soccer", "Basketball");
        Map<String, String> mapCopy = this.createFromArgsRef("Hey", "Bye",
                "Soccer", "Basketball");

        boolean checker = test.hasKey("Soccer");
        boolean ref = true;

        assertEquals(ref, checker);
        // check if the map is preserved.
        assertEquals(mapCopy, test);

    }

    /**
     * Test case for non-empty (multiple pairs) hasKey method (false).
     */
    @Test
    public void hasKeyNonEmpty2() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye",
                "Soccer", "Basketball");
        Map<String, String> mapCopy = this.createFromArgsRef("Hey", "Bye",
                "Soccer", "Basketball");

        boolean checker = test.hasKey("Run");
        boolean ref = false;

        assertEquals(ref, checker);
        // check if the map is preserved.
        assertEquals(mapCopy, test);

    }

    /**
     * Test case for non-empty (one pair) hasKey method.
     */
    @Test
    public void hasKeyNonEmpty3() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye");
        Map<String, String> mapCopy = this.createFromArgsRef("Hey", "Bye");

        boolean checker = test.hasKey("Hey");
        boolean ref = true;

        assertEquals(ref, checker);
        // check if the map is preserved.
        assertEquals(mapCopy, test);

    }

    /**
     * Test case for empty hasKey method.
     */
    @Test
    public void hasKeyEmpty() {
        Map<String, String> test = this.createFromArgsTest();
        Map<String, String> mapCopy = this.createFromArgsRef();

        boolean checker = test.hasKey("Hey");
        boolean ref = false;

        assertEquals(ref, checker);
        // check if the map is preserved.
        assertEquals(mapCopy, test);

    }

    /**
     * Test case for non-empty (multiple pairs) size method.
     */
    @Test
    public void sizeNonEmpty1() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye",
                "Soccer", "Basketball");
        Map<String, String> mapCopy = this.createFromArgsRef("Hey", "Bye",
                "Soccer", "Basketball");

        int checker = test.size();
        int ref = 2;

        assertEquals(ref, checker);
        // check if the map is preserved.
        assertEquals(mapCopy, test);

    }

    /**
     * Test case for non-empty (one pair) size method.
     */
    @Test
    public void sizeNonEmpty2() {
        Map<String, String> test = this.createFromArgsTest("Hey", "Bye");
        Map<String, String> mapCopy = this.createFromArgsRef("Hey", "Bye");

        int checker = test.size();
        int ref = 1;

        assertEquals(ref, checker);
        // check if the map is preserved.
        assertEquals(mapCopy, test);

    }

    /**
     * Test case for empty size method.
     */
    @Test
    public void sizeEmpty() {
        Map<String, String> test = this.createFromArgsTest();
        Map<String, String> mapCopy = this.createFromArgsRef();

        int checker = test.size();
        int ref = 0;

        assertEquals(ref, checker);
        // check if the map is preserved.
        assertEquals(mapCopy, test);

    }

}
