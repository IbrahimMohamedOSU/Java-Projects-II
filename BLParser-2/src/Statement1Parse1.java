import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary methods {@code parse} and
 * {@code parseBlock} for {@code Statement}.
 *
 * @author Ibrahim Mohamed
 *
 */
public final class Statement1Parse1 extends Statement1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Converts {@code c} into the corresponding {@code Condition}.
     *
     * @param c
     *            the condition to convert
     * @return the {@code Condition} corresponding to {@code c}
     * @requires [c is a condition string]
     * @ensures parseCondition = [Condition corresponding to c]
     */
    private static Condition parseCondition(String c) {
        assert c != null : "Violation of: c is not null";
        assert Tokenizer
                .isCondition(c) : "Violation of: c is a condition string";
        return Condition.valueOf(c.replace('-', '_').toUpperCase());
    }

    /**
     * Parses an IF or IF_ELSE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"IF"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an if string is a proper prefix of #tokens] then
     *  s = [IF or IF_ELSE Statement corresponding to if string at start of #tokens]  and
     *  #tokens = [if string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseIf(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("IF") : ""
                + "Violation of: <\"IF\"> is proper prefix of tokens";

        /*
         * Dequeue the keyword IF, then dequeue the subsequent token and make
         * sure it is a condition.
         */

        tokens.dequeue();

        // convert the condition
        String checkCon = tokens.dequeue();

        // return an error message if it is not a condition
        Reporter.assertElseFatalError(Tokenizer.isCondition(checkCon),
                "Expected a condition, but instead was: " + checkCon);

        Condition c = parseCondition(checkCon);

        /*
         * Dequeue the next token and determine if it is the keyword THEN. If it
         * is not, return an error message.
         */
        String checkThen = tokens.dequeue();
        Reporter.assertElseFatalError(checkThen.equals("THEN"),
                "Expected keyword THEN, but instead was: " + checkThen);

        // declare a statement variable and parse if statement child
        Statement checkIf = s.newInstance();
        checkIf.parseBlock(tokens);

        /*
         * check if an else follows the if statement, and if there is then
         * declare a statement variable and parse else statement child.
         */
        if (tokens.front().equals("ELSE")) {
            tokens.dequeue();

            Statement checkElse = s.newInstance();
            checkElse.parseBlock(tokens);
            s.assembleIfElse(c, checkIf, checkElse);

        } else {

            s.assembleIf(c, checkIf);
        }

        /*
         * Dequeue the token and check if the last token of the if statement is
         * END. Return an error message if it is not.
         */
        String checkEnd = tokens.dequeue();
        Reporter.assertElseFatalError(checkEnd.equals("END"),
                "Expected keyword END, but instead was: " + checkEnd);

        /*
         * Dequeue the token and check if the if statement starts with keyword
         * IF. Return an error message if it does not.
         */
        String checkIf2 = tokens.dequeue();
        Reporter.assertElseFatalError(checkIf2.equals("IF"),
                "Expected keyword IF, but instead was: " + checkIf2);

    }

    /**
     * Parses a WHILE statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires <pre>
     * [<"WHILE"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [a while string is a proper prefix of #tokens] then
     *  s = [WHILE Statement corresponding to while string at start of #tokens]  and
     *  #tokens = [while string at start of #tokens] * tokens
     * else
     *  [reports an appropriate error message to the console and terminates client]
     * </pre>
     */
    private static void parseWhile(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0 && tokens.front().equals("WHILE") : ""
                + "Violation of: <\"WHILE\"> is proper prefix of tokens";

        /*
         * Dequeue WHILE, then dequeue the subsequent token and make sure it is
         * a condition.
         */
        tokens.dequeue();

        // convert the condition
        String checkCon = tokens.dequeue();

        // return an error message if it is not a condition.
        Reporter.assertElseFatalError(Tokenizer.isCondition(checkCon),
                "Expected condition, but instead was: " + checkCon);

        Condition c = parseCondition(checkCon);

        /*
         * Dequeue the next token and determine if it is the keyword DO. If it
         * is not, return an error message.
         */
        String checkDO = tokens.dequeue();

        Reporter.assertElseFatalError(checkDO.equals("DO"),
                "Expected keyword DO, but instead was: " + checkDO);

        // declare a new block and assemble the while statement
        Statement whileBlock = new Statement1();

        whileBlock.parseBlock(tokens);
        s.assembleWhile(c, whileBlock);

        /*
         * Dequeue the token and check if the last token of the while statement
         * is END. Return an error message if it is not.
         */
        String checkEnd = tokens.dequeue();
        Reporter.assertElseFatalError(checkEnd.equals("END"),
                "Expected keyword END, but instead was: " + checkEnd);

        /*
         * Dequeue the token and check if the while statement starts with
         * keyword WHILE. Return an error message if it does not.
         */
        String checkWhile = tokens.dequeue();
        Reporter.assertElseFatalError(checkWhile.equals("WHILE"), checkWhile);

    }

    /**
     * Parses a CALL statement from {@code tokens} into {@code s}.
     *
     * @param tokens
     *            the input tokens
     * @param s
     *            the parsed statement
     * @replaces s
     * @updates tokens
     * @requires [identifier string is a proper prefix of tokens]
     * @ensures <pre>
     * s =
     *   [CALL Statement corresponding to identifier string at start of #tokens]  and
     *  #tokens = [identifier string at start of #tokens] * tokens
     * </pre>
     */
    private static void parseCall(Queue<String> tokens, Statement s) {
        assert tokens != null : "Violation of: tokens is not null";
        assert s != null : "Violation of: s is not null";
        assert tokens.length() > 0
                && Tokenizer.isIdentifier(tokens.front()) : ""
                        + "Violation of: identifier string is proper prefix of tokens";

        // Assemble the call
        s.assembleCall(tokens.dequeue());
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Statement1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        Statement s = this.newInstance();

        // string variable to check the type of statement (IF, WHILE, or CALL)
        String first = tokens.front();

        /*
         * call the corresponding parse method on each statement type
         */
        if (first.equals("IF")) {
            parseIf(tokens, s);
        } else if (first.equals("WHILE")) {
            parseWhile(tokens, s);
        } else {
            // return an error if an identifier is not found
            Reporter.assertElseFatalError(Tokenizer.isIdentifier(first),
                    "Expected an IDENTIFIER, but instead was: " + first);
            parseCall(tokens, s);
        }

        this.transferFrom(s);

    }

    @Override
    public void parseBlock(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        this.clear();

        /*
         * check the front of the block (type of statement, WHILE, IF, or CALL)
         * and navigate through the nodes (children).
         */
        while (tokens.front().equals("IF") || tokens.front().equals("WHILE")
                || Tokenizer.isIdentifier(tokens.front())) {

            Statement s = this.newInstance();

            // call parse to ascertain the type of token (WHILE, IF, or CALL)
            s.parse(tokens);

            this.addToBlock(this.lengthOfBlock(), s);
        }

    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL statement(s) file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Statement s = new Statement1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        s.parse(tokens); // replace with parseBlock to test other method
        /*
         * Pretty print the statement(s)
         */
        out.println("*** Pretty print of parsed statement(s) ***");
        s.prettyPrint(out, 0);

        in.close();
        out.close();
    }

}
