import java.util.Comparator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine2;
import components.utilities.Reporter;

/**
 * Program that reads a text file and outputs an HTML file containing the words
 * with the highest occurrence formatted with their corresponding font size.
 *
 * @authors Ibrahim Mohamed
 *
 */
public final class TagCloudGenerator {

    /**
     * Compare {@code Map.Pair<String, Integer>} values and sort in decreasing
     * order.
     *
     */
    private static class NumericalOrder
            implements Comparator<Pair<String, Integer>> {

        /**
         * @param val1
         *
         * @param val2
         *
         * @returns 0 if equal, +1 if the second pair has a higher value, -1
         *          otherwise
         * @ensures Integers will be ordered from highest to lowest value
         */
        @Override
        public int compare(Pair<String, Integer> val1,
                Pair<String, Integer> val2) {
            return val2.value().compareTo(val1.value());

        }

    }

    /**
     * Compare {@code Map.Pair<String, Integer>} keys and sort in alphabetical
     * order.
     */
    private static class AlphabeticalOrder
            implements Comparator<Pair<String, Integer>> {
        /**
         * @param val1
         *
         * @param val2
         *
         * @requires single word String
         *
         * @ensures Strings will be ordered in alphabetical order (A to Z)
         */
        @Override
        public int compare(Pair<String, Integer> val1,
                Pair<String, Integer> val2) {
            return val1.key().compareTo(val2.key());
        }
    }

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TagCloudGenerator() {
    }

    /**
     * Global String containing list of separators.
     */
    private static final String SEPARATORS = "'., ()-_?\"/!@#$%^&*\t1234567890:"
            + ";[]{}+=~`><";

    /**
     * Produces the set of characters in the given {@code String} into the given
     * {@code Set}.
     *
     * @param s
     *            the given {@code String}
     * @return set of characters equal to entries(s)
     * @ensures createElements = entries(s)
     **/
    private static Set<Character> produceValues(String s) {
        Set<Character> strSet = new Set1L<Character>();
        for (int k = 0; k < s.length(); k++) {
            char currentChar = s.charAt(k);

            /*
             * check whether the character is already present in the set. If no
             * match is found, the character is added to the set.
             */
            boolean alreadyContains = false;
            for (char existingChar : strSet) {
                if (existingChar == currentChar) {
                    alreadyContains = true;
                    break;
                }
            }

            if (!alreadyContains) {
                strSet.add(currentChar);
            }
        }

        return strSet;

    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code body} starting at
     * the given {@code position}.
     *
     * @param body
     *            the {@code String} from which the word or separator is
     *            extracted
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} containing the separator characters
     * @return the first word or separator string found in {@code body} starting
     *         at index {@code position}
     * @requires <pre>
     * {@code 0 <= position < |body|}
     * </pre>
     * @ensures <pre>
     * {@code nextWordOrSeparator =
     *   body[position, position + |nextWordOrSeparator|)  and
     * if entries(body[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |body|  or
     *    entries(body[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |body|  or
     *    entries(body[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)}
     * </pre>
     */
    private static String nextWordOrSeparator(String body, int position,
            Set<Character> separators) {
        assert body != null : "Violation of: body is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < body.length() : "Violation of: position < |body|";

        /*
         * Check if the character at a certain position is a separator. Then
         * navigate through the body until a non-separator or non-word is found.
         */
        int p = position;

        if (!separators.contains(body.charAt(position))) {
            while (p < body.length() && !separators.contains(body.charAt(p))) {
                p++;
            }
        } else {

            while (p < body.length() && separators.contains(body.charAt(p))) {
                p++;
            }

        }
        return body.substring(position, p);

    }

    /**
     * Reads the input file, represents each word as a key of wordCounts, and
     * the number of occurrences as the values.
     *
     * @param wordNums
     *            a Map that stores the words and their occurrences
     * @param body
     *            SimpleReader that reads the file containing the words and
     *            occurrences
     * @param separators
     *            the separating characters
     * @replaces wordCounts
     * @requires body.is_open
     * @ensures <pre>
     *               every key in wordNums is a word defined by the separators
     *               in body.content and
     *               each word from body.content exists as a key in wordNums and
     *               the value of each key is it's number of occurrences in in.content and
     *               body.is_open
     *          </pre>
     */
    private static void createKeysValues(Map<String, Integer> wordNums,
            Set<Character> separators, SimpleReader body) {
        //make sure the number of words starts at 0
        wordNums.clear();

        //count words line by line and extract the words and separators
        while (!body.atEOS()) {

            // declare variables to store position and current line
            String curr = body.nextLine().toLowerCase();
            int pos = 0;

            // loop to add words
            while (pos < curr.length()) {
                String keyWord = nextWordOrSeparator(curr, pos, separators);
                /*
                 * increment wordNums when a word (key) is found or add it if
                 * its not present
                 */
                if (!separators.contains(keyWord.charAt(0))) {
                    if (!wordNums.hasKey(keyWord)) {
                        wordNums.add(keyWord, 1);
                    } else {
                        int i = wordNums.value(keyWord) + 1;
                        wordNums.remove(keyWord);
                        wordNums.add(keyWord, i);
                    }
                }

                pos = pos + keyWord.length();

            }
        }

    }

    /**
     * Prints the opening tags in the output HTML file.
     *
     * @param output
     *            the output stream
     * @param fileInput
     *            the input file name
     * @param wordAmount
     *            the number of words to be displayed
     * @updates {@code output.content}
     * @requires <pre>
     * {@code output.is_open and [fileInput is not null]}
     * </pre>
     * @ensures <pre>
     * {@code output.content = #output.content * [opening tags]}
     * </pre>
     */
    private static void printHeaderHTML(SimpleWriter output, String fileInput,
            int wordAmount) {
        assert output != null : "Violation of: output is not null";
        assert output.isOpen() : "Violation of: output.is_open";
        assert fileInput != null : "Violation of: fileInput is not null";

        /*
         * Print the HTML index header.
         */
        output.println("<html>");
        output.println("<head>");
        output.println("<title>Top " + wordAmount + " words in " + fileInput
                + "</title>");

        output.println(
                "<link href=\"http://web.cse.ohio-state.edu/software/2231/web-sw2/"
                        + "assignments/projects/tag-cloud-generator/data/tagcloud.css\""
                        + "rel=\"stylesheet\" type=\"text/css\">\n");
        output.println("</head>");
        output.println("<body>");
        output.println(
                "<h2>Top " + wordAmount + " words in " + fileInput + "</h2>");
        output.println("<hr>");
        output.println("<div class = \"cdiv\">");
        output.println("<p class = \"cbox\">");
    }

    /**
     * Applies appropriate font size to each word.
     *
     * @param maxOccurrences
     *            the maximum occurrences of the words (in tag cloud)
     * @param minOccurrences
     *            the minimum occurrences of the words (in tag cloud)
     * @param occurrences
     *            the occurrence count of target word
     * @requires minOccurr =< occurr =< maxOccurr
     * @ensures fontSize is a usable HTML font size within range [minOccurr,
     *          maxOccurr]
     * @return the correct font size for each word according to their
     *         occurrences
     */

    private static String fontSize(int maxOccurrences, int minOccurrences,
            int occurrences) {
        // initialize maximum and minimum font sizes
        final int minimumSize = 11;
        final int maximumSize = 48;

        /*
         * retrieve font size by using appropriate interval [minOccurrences,
         * maxOccurrences]
         */
        int getSize = maximumSize - minimumSize;
        getSize = getSize * (occurrences - minOccurrences);
        getSize = getSize / (maxOccurrences - minOccurrences);
        getSize = getSize + minimumSize;

        return "f" + getSize;

    }

    /**
     * prints the HTML with wordAmount of words from wordNums with the highest
     * occurrences (top words) sorted in alphabetical order.
     *
     * @param out
     *            the file to print to
     * @param wordAmount
     *            the number of words to be printed
     * @param wordNums
     *            the map containing the words and their occurrences
     * @clears wordNums
     * @ensures out.content = #out.content * wordAmount amount of words from
     *          wordNums and out.is_open
     */
    private static void printWords(SimpleWriter out, int wordAmount,
            Map<String, Integer> wordNums) {

        /*
         * Sort words in decreasing alphabetical order (A to Z)
         */
        Comparator<Pair<String, Integer>> orderAl = new AlphabeticalOrder();
        SortingMachine<Map.Pair<String, Integer>> sortAl;
        sortAl = new SortingMachine2<Map.Pair<String, Integer>>(orderAl);

        /*
         * Sort words by number of occurrences
         */
        Comparator<Pair<String, Integer>> orderNum = new NumericalOrder();
        SortingMachine<Map.Pair<String, Integer>> sortNum;
        sortNum = new SortingMachine2<Map.Pair<String, Integer>>(orderNum);
        while (wordNums.size() > 0) {
            sortNum.add(wordNums.removeAny());
        }
        sortNum.changeToExtractionMode();

        /*
         * Assign font sizes by checking largest and smallest count values
         */
        // initialize a variable to store the maximum number of occurrences
        int maxOccurences = 0;

        // loop that assigns font size according to number of occurrences
        if (sortNum.size() > 0) {
            Map.Pair<String, Integer> top = sortNum.removeFirst();
            maxOccurences = top.value();
            sortAl.add(top);
        }

        // initialize a variable to store the highest count
        int countMax = 0;
        while (countMax < wordAmount && sortNum.size() > 1) {
            Map.Pair<String, Integer> wordOccur = sortNum.removeFirst();
            sortAl.add(wordOccur);
            countMax++;
        }

        // initialize a variable to store the minimum number of occurrences
        int minOccurences = 0;

        // loop that assigns font size according to number of occurrences
        if (sortNum.size() > 0) {
            Map.Pair<String, Integer> bottom = sortNum.removeFirst();
            minOccurences = bottom.value();
            sortAl.add(bottom);

        }
        sortAl.changeToExtractionMode();

        // print HTML with top words
        while (sortAl.size() > 0) {
            Map.Pair<String, Integer> wordOccurr = sortAl.removeFirst();
            String fontSize = fontSize(maxOccurences, minOccurences,
                    wordOccurr.value());
            String tag = "<span style=\"cursor:default\" class=\"" + fontSize
                    + "\" title=\"count: " + wordOccurr.value() + "\">"
                    + wordOccurr.key() + "</span>";
            out.println(tag);
        }
    }

    /**
     * Prints HTML tags for words (wordAmount) in input file and HTML footer.
     *
     * @param out
     *            output file
     * @param in
     *            input file that contains top words
     * @param wordAmount
     *            the number of top words to include
     * @requires wordAmount>0 and in is open out.is_open
     * @ensures in.content = #out.content*HTML tags for highest count words in
     *          in*HTML footer in.is_open out.is_open
     *
     */
    private static void generateTagCloud(SimpleWriter out, SimpleReader in,
            int wordAmount) {

        /*
         * initialize number of word occurrences and output and print closing
         * tags
         */
        Map<String, Integer> wordOccurrences = new Map1L<String, Integer>();
        createKeysValues(wordOccurrences, produceValues(SEPARATORS), in);
        printWords(out, wordAmount, wordOccurrences);

        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        // user input and output file names
        out.print("Enter input text file name: ");
        String fileInput = in.nextLine();
        out.print("Enter output HTML file name: ");
        String fileOutput = in.nextLine();
        out.print("Enter # of words to be placed in the tag cloud: ");
        int wordAmount = in.nextInteger();

        /*
         * check if inputs are valid and create HTML file if they are
         */
        Reporter.assertElseFatalError(wordAmount > 0,
                "Error: The number of words must be greater than 0.");
        Reporter.assertElseFatalError(!fileInput.equals(fileOutput),
                "Error: The number of words must be greater than 0.");
        SimpleWriter outFile = new SimpleWriter1L(fileOutput);

        /*
         * print header tags and read input/output words
         */
        printHeaderHTML(outFile, fileInput, wordAmount);
        SimpleReader inFile = new SimpleReader1L(fileInput);
        generateTagCloud(outFile, inFile, wordAmount);

        //Close input and output streams
        inFile.close();
        outFile.close();

        in.close();
        out.close();
    }

}
