package net.sourceforge.testxng.domain;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import java.io.IOException;


/**
 * Integration tests on the <code>Engine</code> class on an empty directory.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: EngineOnEmptyDirIntegrationTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class EngineOnEmptyDirIntegrationTest
    extends AbstractEngineOnADirIntegrationTest {
    private static final String EMPTY_DIR = TEST_RESOURCES_DIR + "empty/";

    @Test(groups =  {
        "integration"}
    )
    public void shouldReturnDefaultResults()
        throws IOException, MalformedTestDefinitionFileException {
        Results actualResults = Engine.createAndRunForDirectory(EMPTY_DIR);
        AssertResults.assertResultsAreEqual(actualResults, 0, 0, 0, 0);
    }

    @Test(groups =  {
        "integration"}
    )
    public void shouldNotFindAnyTestFiles() throws IOException {
        assertTrue(getTestFilesFromDir(EMPTY_DIR).isEmpty());
    }

    @Test(groups =  {
        "integration"}
    , dependsOnMethods =  {
        "shouldNotFindAnyTestFiles"}
    )
    void shouldNotFindAnyTests()
        throws IOException, MalformedTestDefinitionFileException {
        assertTrue(getTestsFromDir(EMPTY_DIR).isEmpty());
    }
}
