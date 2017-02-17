package net.sourceforge.testxng.domain;


/**
 * Helper class to make assertions on results.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: AssertResults.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public final class AssertResults {
    /**
     * Private constructor to prevent instantiation.
     *
     */
    private AssertResults() {
    }

    static void assertResultsAreEqual(Results actualResults, int expectedRun,
        int expectedErrors, int expectedFailures, int expectedSkipped) {
        assert (actualResults.getTestsRun() == expectedRun) &&
        (actualResults.getErrors() == expectedErrors) &&
        (actualResults.getFailures() == expectedFailures) &&
        (actualResults.getSkipped() == expectedSkipped) : "Actual results has " +
        actualResults.getTestsRun() + " tests run, " +
        actualResults.getErrors() + " errors, " + actualResults.getFailures() +
        " failures and " + actualResults.getSkipped() +
        " tests skipped, but the expected has " + expectedRun + " tests run, " +
        expectedErrors + " errors, " + expectedFailures + " failures and " +
        expectedSkipped + " tests skipped";
    }
}
