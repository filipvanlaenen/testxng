package net.sourceforge.testxng.maven;

import net.sourceforge.testxng.domain.Engine;
import net.sourceforge.testxng.domain.MalformedTestDefinitionFileException;
import net.sourceforge.testxng.domain.Results;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;

import java.text.DecimalFormat;

import java.util.Date;


/**
 * Mojo to TestXNG tests.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @goal test
 */
public class TestXNGMojo extends AbstractMojo {
    static final String RESULTS = "\n\nResults:\n\n";
    static final String THERE_ARE_NO_XML_TESTS_TO_RUN = "There are no XML tests to run.";
    static final String BAR = "-------------------------------------------------------";
    static final String HEADER = "\n\n" + BAR + "\n X M L   T E S T S\n" + BAR +
        "\n";
    private static final String DEFAULT_TESTXNG_DIR = "src/test/testxng";
    private static final String DEFAULT_LOG_FILE = "target/testxng.log";
    private static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat(
            "0.###");
    private static final long MS_TO_S = 1000L;
    private boolean logging = true;
    private String localLog;

    /**
    * Base directory of the project.
    * @parameter expression="${basedir}"
    */
    private File baseDirectory;

    public void execute() throws MojoExecutionException, MojoFailureException {
        execute(baseDirectory.getAbsolutePath() + File.separator, DEFAULT_TESTXNG_DIR);
    }

    void setLogging(boolean logging) {
        this.logging = logging;
    }

    String getLocalLog() {
        return localLog;
    }

    void execute(String dir)
        throws MojoExecutionException, MojoFailureException {
        execute("", dir);
    }

    public void execute(String baseDir, String relDir)
        throws MojoExecutionException, MojoFailureException {
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER);

        Results results;
        long start = new Date().getTime();

        try {
            results = Engine.createAndRunForDirectory(baseDir  +
                    relDir, DEFAULT_LOG_FILE);
        } catch (IOException e) {
            throw new MojoExecutionException("IOException occurred while trying to run the TestXNG tests",
                e);
        } catch (MalformedTestDefinitionFileException e) {
            throw new MojoExecutionException(
                "The following exception occurred while trying to read the test definition file " +
                e.getFile().getAbsolutePath() + ":\n" +
                e.getCause().getMessage(), e);
        }

        long end = new Date().getTime();

        if (results.getTestsRun() == 0) {
            sb.append(THERE_ARE_NO_XML_TESTS_TO_RUN);
        } else {
            sb.append("Running TestSuite\n");
            sb.append(results.getSummary());
            sb.append(", Time elapsed: ");

            sb.append(SECONDS_FORMATTER.format(
                    ((double) (end - start)) / MS_TO_S));
            sb.append(" sec");
        }

        sb.append(RESULTS);

        if (results.getFailures() > 0) {
            sb.append("Failed tests:\n");
            sb.append(results.getFailuresSummary());
            sb.append('\n');
        }

        sb.append(results.getSummary());
        sb.append('\n');

        saveLog(sb);

        if ((results.getErrors() > 0) || (results.getFailures() > 0)) {
            throw new MojoFailureException(
                "There are test failures and/or errors. Please refer to " +
                DEFAULT_LOG_FILE + " for more information.");
        }
    }

    private void saveLog(StringBuilder sb) {
        localLog = sb.toString();

        if (logging) {
            getLog().info(localLog);
        }
    }
}
