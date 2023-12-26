import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumberSecondary;

/**
 * {@code NaturalNumber} represented as a {@code String} with implementations of
 * primary methods.
 *
 * @convention <pre>
 * [all characters of $this.rep are '0' through '9']  and
 * [$this.rep does not start with '0']
 * </pre>
 * @correspondence <pre>
 * this = [if $this.rep = "" then 0
 *         else the decimal number whose ordinary depiction is $this.rep]
 * </pre>
 *
 * @author Ibrahim Mohamed
 *
 */
public class NaturalNumber3 extends NaturalNumberSecondary {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Representation of {@code this}.
     */
    private String rep;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {

        this.rep = "";

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public NaturalNumber3() {
        // creates a new natural number instance.
        this.createNewRep();

    }

    /**
     * Constructor from {@code int}.
     *
     * @param i
     *            {@code int} to initialize from
     */
    public NaturalNumber3(int i) {
        assert i >= 0 : "Violation of: i >= 0";

        // creates a new natural number instance.
        this.createNewRep();
        /*
         * An if statement that checks the condition if i is greater than 0. if
         * it is, it increases the size of the natural number instance and
         * stores i as a string.
         */
        if (i > 0) {
            this.rep += i;
        }
    }

    /**
     * Constructor from {@code String}.
     *
     * @param s
     *            {@code String} to initialize from
     */
    public NaturalNumber3(String s) {
        assert s != null : "Violation of: s is not null";
        assert s.matches("0|[1-9]\\d*") : ""
                + "Violation of: there exists n: NATURAL (s = TO_STRING(n))";

        // creates a new natural number instance.
        this.createNewRep();

        /*
         * An if statement that checks the condition if s is not equal to 0. If
         * it is not, store string s in the natural number instance.
         */
        if (!s.equals("0")) {
            this.rep = s;
        }

    }

    /**
     * Constructor from {@code NaturalNumber}.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     */
    public NaturalNumber3(NaturalNumber n) {
        assert n != null : "Violation of: n is not null";

        // creates a new natural number instance.
        this.createNewRep();

        /*
         * An if statement that checks the condition if n is not equal to 0. If
         * it is not, store natural number n as a string in the new instance
         * representation.
         */
        if (!n.isZero()) {
            this.rep = n.toString();
        }

    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @Override
    public final NaturalNumber newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(NaturalNumber source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof NaturalNumber3 : ""
                + "Violation of: source is of dynamic type NaturalNumberExample";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case.
         */
        NaturalNumber3 localSource = (NaturalNumber3) source;
        this.rep = localSource.rep;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void multiplyBy10(int k) {
        assert 0 <= k : "Violation of: 0 <= k";
        assert k < RADIX : "Violation of: k < 10";

        /*
         * An if statement that checks the assert conditions. If they are true,
         * add k to the end of the natural number representation.
         */
        if (k > 0 || this.rep.length() > 0) {
            this.rep += k;
        }

    }

    @Override
    public final int divideBy10() {

        // store the character to be returned (remainder)
        char rem = '0';

        /*
         * An if statement that checks the condition if this.rep length is
         * greater than 0, and returns 0 if this.rep length is empty.
         */

        if (this.rep.length() > 0) {

            //  initialize remainder to the last position in the string
            rem = this.rep.charAt(this.rep.length() - 1);

            /*
             * after division, the final value will still have every digit
             * except for the remainder
             */
            this.rep = this.rep.substring(0, this.rep.length() - 1);
        }

        // convert the remainder into an int
        int newRem = Integer.parseInt(String.valueOf(rem));
        return newRem;

    }

    @Override
    public final boolean isZero() {

        // check if this.rep length is empty to see if it can store 0
        return this.rep.length() == 0;
    }

}
