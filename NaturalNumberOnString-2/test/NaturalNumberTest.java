import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;

/**
 * JUnit test fixture for {@code NaturalNumber}'s constructors and kernel
 * methods.
 *
 * @author Ibrahim Mohamed
 *
 */
public abstract class NaturalNumberTest {

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new number
     * @ensures constructorTest = 0
     */
    protected abstract NaturalNumber constructorTest();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorTest = i
     */
    protected abstract NaturalNumber constructorTest(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorTest)
     */
    protected abstract NaturalNumber constructorTest(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorTest = n
     */
    protected abstract NaturalNumber constructorTest(NaturalNumber n);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @return the new number
     * @ensures constructorRef = 0
     */
    protected abstract NaturalNumber constructorRef();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorRef = i
     */
    protected abstract NaturalNumber constructorRef(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorRef)
     */
    protected abstract NaturalNumber constructorRef(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorRef = n
     */
    protected abstract NaturalNumber constructorRef(NaturalNumber n);

    // TODO - add test cases for four constructors, multiplyBy10, divideBy10, isZero

    /**
     * Routine test case for empty constructor.
     */
    @Test
    public void emptyConstructerTest() {
        NaturalNumber test = this.constructorTest();
        NaturalNumber ref = this.constructorRef(test);
        assertEquals(ref, test);

    }

    /**
     * Routine test case for constructor that takes int i as an argument. Use a
     * nonzero value as an argument.
     */
    @Test
    public void intConstructerTest() {
        NaturalNumber test = this.constructorTest(15);
        NaturalNumber ref = this.constructorRef(15);
        assertEquals(ref, test);
    }

    /**
     * Boundary test case for constructor that takes int i as an argument. Use 0
     * as an argument.
     */

    @Test
    public void intConstructerTestBoundary() {
        NaturalNumber test = this.constructorTest(0);
        NaturalNumber ref = this.constructorRef(0);
        assertEquals(ref, test);
    }

    /**
     * Challenging test case for constructor that takes int i as an argument.
     * Use the max value as an argument.
     */
    @Test
    public void intConstructerTestChallenge() {
        NaturalNumber test = this.constructorTest(Integer.MAX_VALUE);
        NaturalNumber ref = this.constructorRef(Integer.MAX_VALUE);
        assertEquals(ref, test);
    }

    /**
     * Routine test case for constructor that takes string s as an argument. Use
     * a nonzero value as an argument.
     */
    @Test
    public void stringConstructerTest() {
        NaturalNumber test = this.constructorTest(5);
        NaturalNumber ref = this.constructorRef(5);
        assertEquals(ref, test);
    }

    /**
     * Boundary test case for constructor that takes string s as an argument.
     * Use 0 as an argument.
     */

    @Test
    public void stringConstructerTestBoundary() {
        NaturalNumber test = this.constructorTest(0);
        NaturalNumber ref = this.constructorRef(0);
        assertEquals(ref, test);
    }

    /**
     * Challenging test case for constructor that takes string s as an argument.
     * Use a very large value as an argument.
     */
    @Test
    public void stringConstructerTestChallenge() {
        NaturalNumber test = this.constructorTest("298342093423094823232323");
        NaturalNumber ref = this.constructorRef("298342093423094823232323");
        assertEquals(ref, test);
    }

    /**
     * Routine test case for constructor that takes natural number n as an
     * argument. Use a nonzero value as an argument.
     */
    @Test
    public void naturalConstructerTest() {
        NaturalNumber test = this.constructorTest(this.constructorTest(100));
        NaturalNumber ref = this.constructorRef(this.constructorRef(100));
        assertEquals(ref, test);
    }

    /**
     * Boundary test case for constructor that takes natural number n as an
     * argument. Use 0 as an argument.
     */

    @Test
    public void naturalConstructerTestBoundary() {
        NaturalNumber test = this.constructorTest(this.constructorTest(0));
        NaturalNumber ref = this.constructorRef(this.constructorRef(0));
        assertEquals(ref, test);
    }

    /**
     * Challenging test case for constructor that takes natural number n as an
     * argument. Use a very large value as an argument.
     */
    @Test
    public void naturalConstructerTestChallenge() {
        NaturalNumber test = this
                .constructorTest(this.constructorTest("98724873614535345345"));
        NaturalNumber ref = this
                .constructorRef(this.constructorRef("98724873614535345345"));
        assertEquals(ref, test);
    }

    /**
     * Routine test case for multiplyby10 kernel method. Use a nonzero value as
     * an argument.
     */
    @Test
    public void multiplyBy10TestRoutine() {
        NaturalNumber test = new NaturalNumber3("150");
        NaturalNumber ref = new NaturalNumber3("1500");
        test.multiplyBy10(0);
        assertEquals(ref, test);
    }

    /**
     * Boundary test case for multiplyby10 kernel method. Use 0 as an argument.
     */

    @Test
    public void multiplyBy10TestBoundary() {
        NaturalNumber test = new NaturalNumber3("0");
        NaturalNumber ref = new NaturalNumber3("5");
        test.multiplyBy10(5);
        assertEquals(ref, test);
    }

    /**
     * Challenging test case for multiplyby10 kernel method. Use a very large
     * value as an argument.
     */
    @Test
    public void multiplyBy10TestChallenge() {
        NaturalNumber test = new NaturalNumber3("29341283618231232234");
        NaturalNumber ref = new NaturalNumber3("293412836182312322349");
        test.multiplyBy10(9);
        assertEquals(ref, test);
    }

    /**
     * Routine test case for divideBy10 kernel method. Use a nonzero value as an
     * argument.
     */
    @Test
    public void divideBy10TestRoutine() {

        NaturalNumber test = new NaturalNumber3("147");
        NaturalNumber ref = new NaturalNumber3("14");
        int remainder = test.divideBy10();
        final int seven = 7;
        assertEquals(ref, test);
        assertEquals(seven, remainder);
    }

    /**
     * Boundary test case for divideBy10 kernel method. Use 0 as an argument.
     */

    @Test
    public void divideBy10TestBoundary() {
        NaturalNumber test = new NaturalNumber3("0");
        NaturalNumber ref = new NaturalNumber3(0);
        int remainder = test.divideBy10();
        final int zero = 0;
        assertEquals(ref, test);
        assertEquals(zero, remainder);
    }

    /**
     * Challenging test case for divideBy10 kernel method. Use a very large
     * value as an argument.
     */
    @Test
    public void divideBy10TestChallenge() {
        NaturalNumber test = new NaturalNumber3("32114748917921393949");
        NaturalNumber ref = new NaturalNumber3("3211474891792139394");
        int remainder = test.divideBy10();
        final int nine = 9;
        assertEquals(ref, test);
        assertEquals(nine, remainder);
    }

    /**
     * Routine test case for the isZero kernel method . Use a nonzero value as
     * an argument.
     */
    @Test
    public void isZeroTest() {
        NaturalNumber value = new NaturalNumber3("120");
        boolean test = value.isZero();
        boolean ref = false;
        assertEquals(ref, test);
    }

    /**
     * Boundary test case for isZero kernel method. Use 0 as an argument.
     */

    @Test
    public void isZeroTestBoundary() {
        NaturalNumber value = new NaturalNumber3("0");
        boolean test = value.isZero();
        boolean ref = true;
        assertEquals(ref, test);
    }

}
