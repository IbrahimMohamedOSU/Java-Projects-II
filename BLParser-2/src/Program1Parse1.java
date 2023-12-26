import components.map.Map;
import components.program.Program;
import components.program.Program1;
import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary method {@code parse} for {@code Program}.
 *
 * @author Ibrahim Mohamed
 *
 */
public final class Program1Parse1 extends Program1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Parses a single BL instruction from {@code tokens} returning the
     * instruction name as the value of the function and the body of the
     * instruction in {@code body}.
     *
     * @param tokens
     *            the input tokens
     * @param body
     *            the instruction body
     * @return the instruction name
     * @replaces body
     * @updates tokens
     * @requires <pre>
     * [<"INSTRUCTION"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an instruction string is a proper prefix of #tokens]  and
     *    [the beginning name of this instruction equals its ending name]  and
     *    [the name of this instruction does not equal the name of a primitive
     *     instruction in the BL language] then
     *  parseInstruction = [name of instruction at start of #tokens]  and
     *  body = [Statement corresponding to the block string that is the body of
     *          the instruction string at start of #tokens]  and
     *  #tokens = [instruction string at start of #tokens] * tokens
     * else
     *  [report an appropriate error message to the console and terminate client]
     * </pre>
     */
    private static String parseInstruction(Queue<String> tokens,
            Statement body) {
        assert tokens != null : "Violation of: tokens is not null";
        assert body != null : "Violation of: body is not null";
        assert tokens.length() > 0 && tokens.front().equals("INSTRUCTION") : ""
                + "Violation of: <\"INSTRUCTION\"> is proper prefix of tokens";

        // start by dequeuing the instruction
        tokens.dequeue();

        // Dequeue the next string and make sure that it is an identifier
        String id = tokens.dequeue();

        // Return an error message if it is not an identifier
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(id),
                "Expected identifier, but instead was: " + id);
        Reporter.assertElseFatalError(!id.equals("move")
                && !id.equals("turnright") && !id.equals("turnleft")
                && !id.equals("infect") && !id.equals("skip"), "error");

        /*
         * Dequeue the next token and determine if it is the keyword IS. Then
         * parse the instruction body.
         */
        String token = tokens.dequeue();

        // Return an error message if it is not the keyword IS
        Reporter.assertElseFatalError(token.equals("IS"),
                "Expected keyword IS, but instead was: " + token);

        body.parseBlock(tokens);

        // Dequeue the next token and determine if it is the keyword END
        token = tokens.dequeue();

        // Return an error message if it is not the keyword END
        Reporter.assertElseFatalError(token.equals("END"),
                "Expected keyword END, but instead was: " + token);

        /*
         * Dequeue the next token and determine if it is the same identifier
         * used at the start of the instruction.
         */
        token = tokens.dequeue();

        // Return an error message if incorrect values are found.
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(token),
                "Expected identifier, but instead was: " + token);
        Reporter.assertElseFatalError(token.equals(id),
                "Instruction identifiers do not match");

        return id;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Program1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        assert in.isOpen() : "Violation of: in.is_open";
        Queue<String> tokens = Tokenizer.tokens(in);
        this.parse(tokens);
    }

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";

        // Dequeue the next token and determine if it is the keyword PROGRAM
        String value = tokens.dequeue();

        // Return an error message if it is not the keyword PROGRAM
        Reporter.assertElseFatalError(value.equals("PROGRAM"),
                "Expected keyword PROGRAM, but instead was: " + value);

        // Dequeue the next token and determine if it is an identifier
        String id = tokens.dequeue();

        // Return an error message if it is not an identifier
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(id),
                "Expected identifier, but instead was: " + id);

        // initialize the program name using the identifier
        this.setName(id);

        // Dequeue the next token and determine if it is the keyword IS
        String value2 = tokens.dequeue();

        // Return an error message if it is not the keyword IS
        Reporter.assertElseFatalError(value2.equals("IS"),
                "Expected keyword PROGRAM, but instead was: " + value2);

        /*
         * Generate a new context for the instructions. Then loop until the
         * keyword INSTRUCTION is no longer among the tokens.
         */
        Map<String, Statement> instrContext = this.newContext();

        while (tokens.front().equals("INSTRUCTION")) {

            /*
             * Generate a new statement to be used in parseInstruction. Then
             * check for copies (duplicates).
             */
            Statement instrBody = this.newBody();
            String instruction = parseInstruction(tokens, instrBody);

            // Return an error message if a duplicate identifier is found.
            Reporter.assertElseFatalError(!instrContext.hasKey(instruction),
                    "Copy of INSTRUCTION Identifier: " + value);

            instrContext.add(instruction, instrBody);

        }

        // Swap the context then initialize a new statement
        this.swapContext(instrContext);
        Statement newBody = this.newBody();

        // Dequeue the token and check if the body starts with the keyword BEGIN
        String value3 = tokens.dequeue();

        // Return an error message if it does not start with the keyword BEGIN
        Reporter.assertElseFatalError(value3.equals("BEGIN"),
                "Expected body to start with keyword BEGIN, but it instead started with: "
                        + value3);

        newBody.parseBlock(tokens);

        // Dequeue the token and check if the body ends with the keyword END
        String value4 = tokens.dequeue();

        // Return an error message if it does not end with the keyword END
        Reporter.assertElseFatalError(value4.equals("END"),
                "Expected body to end with keyword END, but it instead ended with: "
                        + value4);

        /*
         * Dequeue the token and determine if it is an identifier to clarify the
         * end of the program. Then check if it is the same identifier used at
         * the start.
         */
        String id2 = tokens.dequeue();

        // Return an error message if these conditions are not met
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(id2),
                "Expected identifier, but instead was: " + id2);
        Reporter.assertElseFatalError(id2.equals(id),
                "Instruction identifiers do not match. Expected identifier: "
                        + id + ", but instead was: " + id2);

        /*
         * Dequeue the token and check if the final token is END_OF_INPUT. Then
         * check if the queue is empty.
         */
        String endOfIn = tokens.dequeue();

        // Return an error message if incorrect values are found.
        Reporter.assertElseFatalError(endOfIn.equals(Tokenizer.END_OF_INPUT),
                "Expected final token to be END_OF_INPUT, but instead was: "
                        + endOfIn);
        Reporter.assertElseFatalError(tokens.length() == 0,
                "Expected queue to be empty, but instead was length: "
                        + tokens.length());

        this.swapBody(newBody);

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
        out.print("Enter valid BL program file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Program p = new Program1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        p.parse(tokens);
        /*
         * Pretty print the program
         */
        out.println("*** Pretty print of parsed program ***");
        p.prettyPrint(out);

        in.close();
        out.close();
    }

}
