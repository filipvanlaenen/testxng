package net.sourceforge.testxng.commandline;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * Tests for the command-line interface.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 *
 * @version $Id: CommandLineInterfaceTest.java 48 2010-04-01 21:23:00Z filipvanlaenen $
 *
 */
public class CommandLineInterfaceTest {
    @Test
    public void shouldPrintHelpMessageWhenHelpRequested() {
        runTestAndCompareResut(CommandLineInterface.HELP_MESSAGE, "-h");
    }

    @Test
    public void shouldPrintErrorMessageWhenInvokedOnNonExistingDir() {
        runTestAndCompareResut("Directory foo does not exist.", "foo");
    }

    @Test
    public void shouldUseCurrentDirWhenNoArgumentProvided() {
        runTestAndCompareResut("Directory . does not exist.");
    }

    private void runTestAndCompareResut(String expectedResult, String... args) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        CommandLineInterface.main(ps, args);
        assertEquals(baos.toString(), expectedResult + "\n");
    }
}
