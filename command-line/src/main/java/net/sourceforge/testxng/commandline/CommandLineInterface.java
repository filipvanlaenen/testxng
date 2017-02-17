package net.sourceforge.testxng.commandline;

import java.io.PrintStream;


/**
 * The command-line interface from which TestXNG can be invoked and run.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: CommandLineInterface.java 89 2014-02-20 16:42:17Z filipvanlaenen $
 *
 */
public final class CommandLineInterface {
    static final String HELP_MESSAGE = "TestXNG\n\nUsage:\n\t-h Prints out this help message.";

    /**
     * Prevents instantiation.
     */
    private CommandLineInterface() {
    }

    public static void main(String... args) {
        main(System.out, args);
    }

    static void main(PrintStream ps, String... args) {
        ps.println(runCommand(args));
    }

    private static String runCommand(String... args) {
        String result;

        if (args.length == 0) {
            result = runOnDirectory(".");
        } else if (args[0].equals("-h")) {
            result = HELP_MESSAGE;
        } else {
            result = runOnDirectory(args[0]);
        }

        return result;
    }

    private static String runOnDirectory(String dirName) {
        return "Directory " + dirName + " does not exist.";
    }
}
