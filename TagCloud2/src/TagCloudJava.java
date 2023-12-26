import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Program that reads a text file and outputs an HTML file containing the words
 * with the highest occurrence formatted with their corresponding font size.
 *
 * @authors Ibrahim Mohamed
 *
 */
public final class TagCloudJava {

    /**
     * Compare {@code Map.Pair<String, Integer>} values and sort in decreasing
     * order.
     */
    private static class NumericalOrder
            implements Serializable, Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            /*
             * initializing comparator variables and validate equality
             */
            Integer eq1 = o1.getValue();
            Integer eq2 = o2.getValue();
            int compar = eq2.compareTo(eq1);

            if (compar == 0) {
                compar = o1.getKey().compareTo(o2.getKey());
            }

            return compar;
        }
    }

    /**
     * Compare {@code Map.Pair<String, Integer>} keys and sort in alphabetical
     * order.
     */
    private static class AlphabeticalOrder
            implements Serializable, Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            /*
             * initialize comparator variables and return 0 if str1 = str2
             */
            String str1 = o1.getKey();
            String str2 = o2.getKey();
            int comp = str1.compareToIgnoreCase(str2);

            if (comp == 0) {
                comp = str1.compareTo(str2);
            }

            return comp;
        }
    }

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TagCloudJava() {
    }

    /**
     * Global String containing list of separators.
     */
    private static final String SEPARATORS = "'., ()-_?\"/!@#$%^&*\t1234567890:"
            + ";[]{}+=~`><";

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
    private static void printHeaderHTML(PrintWriter output, String fileInput,
            int wordAmount) {
        assert output != null : "Violation of: output is not null";
        assert fileInput != null : "Violation of: fileInput is not null";
        /*
         * Print the HTML index header.
         */
        output.println("<html>");
        output.println("<head>");
        output.println("<title>Top " + wordAmount + " words in " + fileInput
                + "</title>");
        /*
         * Print using CSS to format tag clouds.
         */
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
     * Produces the set of characters in the given {@code String} into the given
     * {@code Set}.
     *
     * @param s
     *            the given {@code String}
     * @return set of characters equal to entries(s)
     * @ensures createElements = entries(s)
     **/
    private static Set<Character> createElements(String s) {
        Set<Character> strSet = new HashSet<Character>();
        for (int k = 0; k < s.length(); k++) {
            if (!strSet.contains(s.charAt(k))) {
                strSet.add(s.charAt(k));
            }
        }
        return strSet;

    }

    /**
     * Generates an ordered List of words sorted by occurrences.
     *
     * @clears wordAmount
     * @param wordAmount
     *            the Map containing the words
     * @return a list of Entries from wordAmount sorted from highest to lowest
     *         occurrences
     * @ensures all of the Entries in #wordAmount are stored in orderWords and
     *          for all orderWords elements, orderWords(k+1)=<orderWords(k)
     *
     */
    private static List<Map.Entry<String, Integer>> countSort(
            Map<String, Integer> wordAmount) {
        /*
         * Initialize variables
         */
        List<Map.Entry<String, Integer>> orderWords;
        orderWords = new ArrayList<Map.Entry<String, Integer>>();

        Set<Map.Entry<String, Integer>> entries = wordAmount.entrySet();
        Iterator<Map.Entry<String, Integer>> move = entries.iterator();
        /*
         * Avoid aliasing by inserting and extracting words manually
         */
        while (move.hasNext()) {
            Map.Entry<String, Integer> curr = move.next();
            move.remove();
            orderWords.add(curr);
        }
        /*
         * Sort the words numerically (descending order)
         */
        Comparator<Map.Entry<String, Integer>> sort = new NumericalOrder();
        orderWords.sort(sort);
        return orderWords;

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
         * Determine if the character at this position is a separator. Then
         * navigate through the text until a non-separator or non-word is found
         */
        boolean isSeparator = separators.contains(body.charAt(position));
        int posNext = position + 1;

        while (posNext < body.length()
                && (separators.contains(body.charAt(posNext)) == isSeparator)) {
            posNext++;
        }
        return body.substring(position, posNext);
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
     * @replaces wordNums
     * @requires body.is_open
     * @ensures <pre>
     *               every key in wordNums is a word defined by the separators
     *               in body.content and
     *               each word from body.content exists as a key in wordNums and
     *               the value of each key is it's number of occurrences in in.content and
     *               body.is_open
     *          </pre>
     * @throws IOException
     *             if an I/O exception occurs
     */
    private static void createKeysValues(Map<String, Integer> wordNums,
            Set<Character> separators, BufferedReader body) throws IOException {
        /*
         * make sure the number of words starts at 0
         */
        wordNums.clear();
        /*
         * count words line by line and extract the words and separators
         */
        String curr = body.readLine();
        while (curr != null) {
            curr = curr.toLowerCase();
            int pos = 0;
            while (pos < curr.length()) {
                String keyWord = nextWordOrSeparator(curr, pos, separators);
                /*
                 * increment wordNums when a word (key) is found or add it if
                 * its not present
                 */
                if (!separators.contains(keyWord.charAt(0))) {
                    if (!wordNums.containsKey(keyWord)) {
                        wordNums.put(keyWord, 1);
                    } else {
                        int i = wordNums.get(keyWord) + 1;
                        wordNums.remove(keyWord);
                        wordNums.put(keyWord, i);
                    }
                }

                pos += keyWord.length();
            }

            curr = body.readLine();
        }

    }

    /**
     * Applies appropriate font size to each word.
     *
     * @param maxOccur
     *            the maximum occurrences of the words (in tag cloud)
     * @param minOccur
     *            the minimum occurrences of the words (in tag cloud)
     * @param occur
     *            the occurrence count of target word
     * @requires minOccur =< occur =< maxOccur
     * @ensures fontSize is a usable HTML font size within range [minOccur,
     *          maxOccur]
     * @return the correct font size for each word according to their
     *         occurrences
     */

    private static String fontSize(int maxOccur, int minOccur, int occur) {
        /*
         * initialize maximum and minimum font sizes according to CSS/HTML
         * format
         */
        final int fontMin = 11;
        final int fontMax = 48;
        /*
         * retrieve font size by using appropriate interval [minOccurr,
         * maxOccurr]
         */
        int numFont = fontMax - fontMin;
        if (maxOccur > minOccur) {
            numFont = numFont * (occur - minOccur);
            numFont = numFont / (maxOccur - minOccur);
            numFont = numFont + fontMin;
        } else {
            numFont = fontMax;
        }
        /*
         * return the correct font size
         */
        return "f" + numFont;

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
    private static void outputTopWords(PrintWriter out, int wordAmount,
            Map<String, Integer> wordNums) {
        /*
         * Initialize top word counts and sort words by number of occurrences
         * (descending order)
         */
        List<Map.Entry<String, Integer>> orderNum = countSort(wordNums);
        List<Map.Entry<String, Integer>> maxOccurrences;
        maxOccurrences = new ArrayList<Map.Entry<String, Integer>>();
        while (orderNum.size() > 0 && maxOccurrences.size() < wordAmount) {
            Map.Entry<String, Integer> occurMax = orderNum.get(0);
            orderNum.remove(0);
            maxOccurrences.add(occurMax);
        }
        /*
         * Assign font sizes by checking largest and smallest count values
         */
        int maxOccur = 1;
        int minOccur = 1;
        if (maxOccurrences.size() > 0) {
            minOccur = maxOccurrences.get(maxOccurrences.size() - 1).getValue();
            maxOccur = maxOccurrences.get(0).getValue();
        }
        /*
         * Sort words in decreasing alphabetical order (A -> Z)
         */
        Comparator<Map.Entry<String, Integer>> sortAl = new AlphabeticalOrder();
        maxOccurrences.sort(sortAl);
        /*
         * Print HTML file for words with most occurrences, with their
         * appropriate font size
         */
        for (Map.Entry<String, Integer> occurWords : maxOccurrences) {
            String fontSize = fontSize(maxOccur, minOccur,
                    occurWords.getValue());
            String tag = "<span style=\"cursor:default\" class=\"" + fontSize
                    + "\" title=\"count: " + occurWords.getValue() + "\">"
                    + occurWords.getKey() + "</span>";
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
    private static void generateTagCloud(PrintWriter out, BufferedReader in,
            int wordAmount) {
        /*
         * word occurrences and output
         */
        Map<String, Integer> wordOccurr = new HashMap<String, Integer>();
        try {
            createKeysValues(wordOccurr, createElements(SEPARATORS), in);
        } catch (IOException e) {
            System.out.println(
                    "Error. Cannot interpret file or determine word count.");
        }
        outputTopWords(out, wordAmount, wordOccurr);
        /*
         * print closing tags
         */
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
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));
        /*
         * user input file name and check validity
         */
        System.out.println("Enter input text file name: ");
        String userIn = "";
        BufferedReader inFile = null;
        while (inFile == null) {
            try {
                userIn = in.readLine();
                inFile = new BufferedReader(new FileReader(userIn));
            } catch (IOException e) {
                System.out.println("Error. File cannot be read due to " + e
                        + "\nPlease try again.");
            }
        }
        String fileInput = userIn;

        /*
         * user output file name and check validity
         */
        System.out.print("Enter output HTML file name: ");
        String fileOutput = "";
        PrintWriter outFile = null;
        while (outFile == null) {
            try {
                fileOutput = in.readLine();
                outFile = new PrintWriter(
                        new BufferedWriter(new FileWriter(fileOutput)));
            } catch (IOException e) {
                System.out.println("Error. File cannot be printed due to" + e
                        + "\nPlease try again.");
            }
        }

        /*
         * word amount and check validity
         */
        System.out.print("Enter # of words to be placed in the tag cloud: ");
        int wordAmount = 0;
        try {
            wordAmount = Integer.parseInt(in.readLine());
        } catch (NumberFormatException e) {
            System.err.println("Error. the number is not in correct format.");
        } catch (IOException e) {
            System.err.println("Error. the input cannot be interpreted.");
        }
        if (wordAmount < 0) {
            wordAmount = 0;
        }

        /*
         * Print the header/footer and top words
         */
        printHeaderHTML(outFile, fileInput, wordAmount);
        generateTagCloud(outFile, inFile, wordAmount);

        //Close input and output streams
        outFile.close();
        try {
            in.close();
        } catch (IOException e) {
            System.err.println("Error. Cannot close file.");
        }

    }

}
