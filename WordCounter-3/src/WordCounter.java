import java.util.Iterator;

import components.map.Map;
import components.map.Map.Pair;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * A program that counts the number of times each word occurs in an input file.
 * Then sorts those words in alphabetical order and outputs an HTML table.
 *
 * @author Ibrahim Mohamed
 *
 */
public final class WordCounter {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private WordCounter() {
    }

    /**
     * Global String listing every separator.
     */

    private static final String SEPARATORS = "0123456789-_!^&*/., #?$@%\t:()"
            + "{}+=;~[]<>`";

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires <pre>
     * {@code 0 <= position < |text|}
     * </pre>
     * @ensures <pre>
     * {@code nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)}
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        int k = position;

        if (!separators.contains(text.charAt(position))) {
            while (k < text.length() && !separators.contains(text.charAt(k))) {
                k++;
            }
        } else {

            while (k < text.length() && separators.contains(text.charAt(k))) {
                k++;
            }

        }
        return text.substring(position, k);
    }

    /**
     * Prints the header in the HTML document.
     *
     * @param out
     *            output stream
     * @param fileInput
     *            the input file that contains the words
     * @updates {@code out.content}
     * @requires {@code [fileInput is not null] and out.is_open}
     *
     * @ensures {@code out.content = #out.content * [HTML header tags]}
     *
     */
    private static void printHeaderHTML(SimpleWriter out, String fileInput) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";
        assert fileInput != null : "Violation of: fileInput is not null";

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Number of words in " + fileInput + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Number of words in " + fileInput + "</h2>");
        out.println("<hr />");
        out.println("<table border=\"1\">");
        out.println("<tr>");
        out.println("<th>Words</th>");
        out.println("<th>Counts</th>");
        out.println("</tr>");
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param strSet
     *            the {@code Set} to be replaced
     * @replaces {@code strSet}
     * @ensures <pre>
     * {@code strSet = entries(str)}
     * </pre>
     */
    private static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";

        //Navigate through the string via loop and place certain characters into the set.
        strSet.clear();
        for (int k = 0; k < str.length() - 1; k++) {

            strSet.add(str.charAt(k));

        }

    }

    /**
     * Moves all of the content in wordMap to a queue then organizes them in
     * alphabetical order (String.CASE_INSENSITIVE_ORDER).
     *
     * @param wordMap
     *            a map of the words and their definitions
     * @return an alphabetically ordered queue of the words
     *
     * @requires {@code wordMap /= empty_string and [wordMap is not null]}
     *
     * @ensures {@code wordQueue = [#wordQueue sorted in String.CASE_INSENSITIVE_ORDER]}
     *
     */
    private static Queue<String> organizeWords(Map<String, Integer> wordMap) {
        assert wordMap != null : "Violation of: wordMap is not null";
        assert wordMap.size() > 0 : "Violation of: wordMap is not empty";

        //An iterator that sends content to the map
        Iterator<Pair<String, Integer>> iterate = wordMap.iterator();

        //A Queue that stores the remaining words
        Queue<String> wordQueue = new Queue1L<>();

        //A Comparator that sorts the words in alphabetical order
        while (iterate.hasNext()) {
            Pair<String, Integer> temporary = iterate.next();
            wordQueue.enqueue(temporary.key());
            wordQueue.sort(String.CASE_INSENSITIVE_ORDER);
        }
        return wordQueue;
    }

    /**
     * Interprets the input, records the number of times each word occurs, then
     * generates a table consisting of each word and its occurrences in
     * alphabetical order.
     *
     * @param out
     *            the output stream
     * @param threads
     *            queue that contains every line from the input file
     *
     */
    private static void generateTable(SimpleWriter out, Queue<String> threads) {
        // Generate a directory of the separators
        Set<Character> separators = new Set1L<>();
        generateElements(SEPARATORS, separators);

        //A while loop that separates each word
        Queue<String> words = new Queue1L<>();
        while (threads.length() > 0) {
            String threadNew = threads.dequeue();
            int location = 0;
            while (location < threadNew.length()) {
                String temporary = nextWordOrSeparator(threadNew, location,
                        separators);
                location = location + temporary.length();
                //If any separators remain, they are deleted from the directory.
                if (temporary.length() > 0
                        && !separators.contains(temporary.charAt(0))) {
                    words.enqueue(temporary);
                }
            }
        }

        /*
         * A while loop that produces a map with words defined as terms and
         * number of occurrences defined as values.
         */
        Map<String, Integer> wordsWithOccurences = new Map1L<>();
        while (words.length() > 0) {
            String term = words.dequeue();
            //Either increment existing words or generate a new documentation
            if (!wordsWithOccurences.hasKey(term)) {
                wordsWithOccurences.add(term, 1);
            } else {
                Map.Pair<String, Integer> temporary = wordsWithOccurences
                        .remove(term);
                int value = temporary.value();
                value++;
                wordsWithOccurences.add(term, value);
            }
        }

        /*
         * The organized terms are generated into a queue, and then into a
         * table.
         */
        Queue<String> sortedTerms = organizeWords(wordsWithOccurences);
        while (sortedTerms.length() > 0) {
            String word = sortedTerms.dequeue();
            out.println("<tr>");
            out.println("<td>" + word + "</td>");
            out.println("<td>" + wordsWithOccurences.value(word) + "</td>");
            out.println("</tr>");
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        // Open input and output streams
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        //Take user input for the input and output files
        out.print("Please enter the input file name: ");
        String fileInput = in.nextLine();
        out.print("Please enter the output file name: ");
        String fileOutput = in.nextLine();

        //Generate the HTML document
        SimpleWriter output = new SimpleWriter1L(fileOutput);

        //Print the opening tags
        printHeaderHTML(output, fileInput);

        //Read the input file
        SimpleReader input = new SimpleReader1L(fileInput);

        //Generating a word map of the input
        Queue<String> threads = new Queue1L<>();
        while (!input.atEOS()) {
            String temporary = input.nextLine();
            threads.enqueue(temporary);
        }

        // Check if there are words to generate a table for
        if (threads.length() != 0) {
            // Finally generate the table
            generateTable(output, threads);
        } else {
            // Handle the case of an empty input file
            output.println("<tr>");
            output.println(
                    "<td colspan=\"2\">No words found in the input file</td>");
            output.println("</tr>");
        }

        //Close the input and output streams
        input.close();
        output.close();
        in.close();
        out.close();
    }

}