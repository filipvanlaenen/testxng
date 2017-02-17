package net.sourceforge.testxng.domain;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;


/**
 * Integration tests on the <code>Engine</code> class using the test files in the directory with one failure.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: EngineOnOneFailureDirIntegrationTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class EngineOnOneFailureDirIntegrationTest
    extends AbstractEngineOnADirIntegrationTest {
    public static final String ONE_FAILURE_DIR = TEST_RESOURCES_DIR +
        "oneFailure";
    private Results results;

    @BeforeTest(groups =  {
        "integration"}
    )
    void runTestAndGetResults()
        throws IOException, MalformedTestDefinitionFileException {
        results = Engine.createAndRunForDirectory(ONE_FAILURE_DIR);
    }

    @Test(groups =  {
        "integration"}
    )
    public void shouldReturnOneFailure() throws IOException {
        AssertResults.assertResultsAreEqual(results, 1, 0, 1, 0);
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "shouldReturnOneFailure"}
    )
    void testMessageShouldShowOneFailure() throws IOException {
        assertEquals(results.getSummary(),
            "Tests run: 1, Failures: 1, Errors: 0, Skipped: 0");
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "shouldReturnOneFailure"}
    )
    void failureSummaryShouldListOneFailure() throws IOException {
        assertEquals(results.getFailuresSummary(),
            "  OneFailure(OneFailure.testxng)\n");
    }
}
