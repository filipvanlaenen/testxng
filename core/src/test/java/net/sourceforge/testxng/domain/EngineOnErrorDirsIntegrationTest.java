package net.sourceforge.testxng.domain;

import org.testng.annotations.Test;


/**
 * Integration tests on the <code>Engine</code> class using the test files in the directory having errors.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: EngineOnErrorDirsIntegrationTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class EngineOnErrorDirsIntegrationTest
    extends AbstractEngineOnADirIntegrationTest {
    public static final String BROKEN_XML_DIR = TEST_RESOURCES_DIR +
        "brokenXml";
    public static final String INVALID_XML_DIR = TEST_RESOURCES_DIR +
        "invalidXml";

    @Test(groups =  {
        "integration"}
    , expectedExceptions = MalformedTestDefinitionFileException.class)
    public void shouldThrowMalformedTestDefinitionFileExceptionOnReadingBrokenXmlFile()
        throws MalformedTestDefinitionFileException {
        getTestsFromTestFile(BROKEN_XML_DIR, "BrokenXml.testxng");
    }

    @Test(groups =  {
        "integration"}
    , expectedExceptions = MalformedTestDefinitionFileException.class)
    public void shouldThrowMalformedTestDefinitionFileExceptionOnReadingInvalidXmlFile()
        throws MalformedTestDefinitionFileException {
        getTestsFromTestFile(INVALID_XML_DIR, "InvalidXml.testxng");
    }
}
