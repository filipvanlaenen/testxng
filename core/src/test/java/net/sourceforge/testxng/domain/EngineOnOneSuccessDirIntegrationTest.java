package net.sourceforge.testxng.domain;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import java.io.IOException;

import java.util.List;


/**
 * Integration tests on the <code>Engine</code> class using the test files in the directory with one success.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: EngineOnOneSuccessDirIntegrationTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class EngineOnOneSuccessDirIntegrationTest
    extends AbstractEngineOnADirIntegrationTest {
    private static final String ONE_SUCCESS_DIR = TEST_RESOURCES_DIR +
        "oneSuccess";

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "assertionShouldBeValidates"}
    )
    public void shouldReturnOneSuccess()
        throws IOException, MalformedTestDefinitionFileException {
        Results actualResults = Engine.createAndRunForDirectory(ONE_SUCCESS_DIR);
        AssertResults.assertResultsAreEqual(actualResults, 1, 0, 0, 0);
    }

    @Test(groups =  {
        "integration"}
    )
    public void shouldFindOneTestFile() throws IOException {
        assertEquals(getTestFilesFromDir(ONE_SUCCESS_DIR).size(), 1);
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "shouldFindOneTestFile"}
    )
    void shouldFindOneTest()
        throws IOException, MalformedTestDefinitionFileException {
        assertEquals(getTestsFromDir(ONE_SUCCESS_DIR).size(), 1);
    }

    private net.sourceforge.testxng.domain.Test getTest()
        throws IOException, MalformedTestDefinitionFileException {
        List<net.sourceforge.testxng.domain.Test> tests = getTestsFromDir(ONE_SUCCESS_DIR);

        return tests.get(0);
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "shouldFindOneTest"}
    )
    void testShouldHaveIdOneSuccess()
        throws IOException, MalformedTestDefinitionFileException {
        assertEquals(getTest().getId(), "OneSuccess");
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "shouldFindOneTest"}
    )
    void testShouldHaveSourceFile()
        throws IOException, MalformedTestDefinitionFileException {
        assertEquals(getTest().getSourceFilename(), "OneSuccess-source.xml");
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "shouldFindOneTest"}
    )
    void testShouldHaveOneAssertion()
        throws IOException, MalformedTestDefinitionFileException {
        assertEquals(getTest().getAssertions().size(), 1);
    }

    private Assertion getAssertion()
        throws IOException, MalformedTestDefinitionFileException {
        return getTest().getAssertions().get(0);
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "testShouldHaveOneAssertion"}
    )
    void assertionShouldBeValidates()
        throws IOException, MalformedTestDefinitionFileException {
        assertTrue(getAssertion() instanceof ValidationAssertion);
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "shouldReturnOneSuccess"}
    )
    void testMessageShouldShowOneSuccess()
        throws IOException, MalformedTestDefinitionFileException {
        Results results = Engine.createAndRunForDirectory(ONE_SUCCESS_DIR);
        assertEquals(results.getSummary(),
            "Tests run: 1, Failures: 0, Errors: 0, Skipped: 0");
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "shouldReturnOneSuccess"}
    )
    void failuresMessageShouldBeEmpty()
        throws IOException, MalformedTestDefinitionFileException {
        Results results = Engine.createAndRunForDirectory(ONE_SUCCESS_DIR);
        assertEquals(results.getFailuresSummary(), "");
    }
}
