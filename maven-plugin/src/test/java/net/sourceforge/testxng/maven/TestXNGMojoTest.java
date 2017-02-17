package net.sourceforge.testxng.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Unit tests on the <code>TestXNGMojo</code> class.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: TestXNGMojoTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class TestXNGMojoTest {
    private static final String ROOT_TEST_DIR = "src/test/resources/";
    private static final String ONE_FAILURE_DIR = ROOT_TEST_DIR + "oneFailure";
    private static final String BROKEN_XML_DIR = ROOT_TEST_DIR + "brokenXml";
    private static final String INVALID_XML_DIR = ROOT_TEST_DIR + "invalidXml";
    private static final String EMPTY_DIR = ROOT_TEST_DIR + "empty";
    private static final String NO_TESTS_TO_RUN = TestXNGMojo.HEADER +
        TestXNGMojo.THERE_ARE_NO_XML_TESTS_TO_RUN + TestXNGMojo.RESULTS +
        "Tests run: 0, Failures: 0, Errors: 0, Skipped: 0\n";
    private TestXNGMojo mojo;

    @BeforeMethod(groups = "integration")
    void beforeMethod() {
        mojo = new TestXNGMojo();
        mojo.setLogging(false);
    }

    @Test(groups = "integration", expectedExceptions =  {
        MojoFailureException.class}
    )
    public void shouldThrowMojoFailureExceptionIfThereIsAFailure()
        throws MojoExecutionException, MojoFailureException {
        mojo.execute(ONE_FAILURE_DIR);
    }

    @Test(groups = "integration", expectedExceptions =  {
        MojoExecutionException.class}
    )
    public void shouldThrowMojoExecutionExceptionIfTestDirDoesNotExist()
        throws MojoExecutionException, MojoFailureException {
        mojo.execute("NonExistingDir");
    }

    @Test(groups = "integration", expectedExceptions =  {
        MojoExecutionException.class}
    )
    public void shouldThrowMojoExecutionExceptionIfTestDirContainsBrokenXmlFile()
        throws MojoExecutionException, MojoFailureException {
        mojo.execute(BROKEN_XML_DIR);
    }

    @Test(groups = "integration", expectedExceptions =  {
        MojoExecutionException.class}
    )
    public void shouldThrowMojoExecutionExceptionIfTestDirContainsInvalidXmlFile()
        throws MojoExecutionException, MojoFailureException {
        mojo.execute(INVALID_XML_DIR);
    }

    @Test(groups = "integration")
    public void shouldReportNoTestWereRunForEmptyDir()
        throws MojoExecutionException, MojoFailureException {
        mojo.execute(EMPTY_DIR);
        assertEquals(mojo.getLocalLog(), NO_TESTS_TO_RUN);
    }
}
