package net.sourceforge.testxng.domain;

import static net.sourceforge.testxng.domain.Test.State.FAILURE;
import static net.sourceforge.testxng.domain.Test.State.SUCCESS;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Abstract superclass for integration test classes on Engine.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: AbstractEngineOnADirIntegrationTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class AbstractEngineOnADirIntegrationTest {
    static final String TEST_RESOURCES_DIR = "src/test/resources/";
    protected Map<String, net.sourceforge.testxng.domain.Test> testMap;
    private Logger logger;

    protected List<File> getTestFilesFromDir(String dir)
        throws IOException {
        Engine engine = new Engine(dir);

        return engine.getTestFilesFromDir();
    }

    protected List<net.sourceforge.testxng.domain.Test> getTestsFromDir(
        String dir) throws IOException, MalformedTestDefinitionFileException {
        Engine engine = new Engine(dir);

        return engine.getTestsFromDir();
    }

    protected List<net.sourceforge.testxng.domain.Test> getTestsFromTestFile(
        String dir, String filename)
        throws MalformedTestDefinitionFileException {
        Engine engine = new Engine(dir);

        return engine.getTestsFromTestFile(new File(dir + File.separator +
                filename));
    }

    protected void initializeLogger(
        Class<?extends AbstractEngineOnADirIntegrationTest> aClass) {
        logger = Logger.getLogger(aClass);

        ConsoleAppender appender = new ConsoleAppender(new SimpleLayout(),
                ConsoleAppender.SYSTEM_ERR);
        logger.addAppender(appender);
    }

    void initializeTestMap(String dir)
        throws IOException, MalformedTestDefinitionFileException {
        List<net.sourceforge.testxng.domain.Test> tests = getTestsFromDir(dir);
        testMap = new HashMap<String, net.sourceforge.testxng.domain.Test>();

        for (net.sourceforge.testxng.domain.Test test : tests) {
            testMap.put(test.getId(), test);
        }
    }

    private void runTestAndAssertForState(String testId, Test.State state) {
        net.sourceforge.testxng.domain.Test test = testMap.get(testId);
        test.run(logger);
        assertEquals(test.getState(), state);
    }

    protected void runTestAndAssertForSuccess(String testId) {
        runTestAndAssertForState(testId, SUCCESS);
    }

    protected void runTestAndAssertForFailure(String testId) {
        runTestAndAssertForState(testId, FAILURE);
    }
}
