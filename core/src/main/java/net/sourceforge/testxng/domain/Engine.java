package net.sourceforge.testxng.domain;

import net.sourceforge.testxng.xml.XmlNavigator;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Test engine for TestXNG. The test engine reads the tests from the TestXNG
 * test files, runs them, and returns the results.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: Engine.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class Engine {
    static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    static final ErrorHandler NON_VALIDATION_DETECTOR = new ErrorHandler() {
            public void fatalError(SAXParseException exception)
                throws SAXException {
            }

            public void error(SAXParseException e) throws SAXParseException {
                throw e;
            }

            public void warning(SAXParseException err)
                throws SAXParseException {
            }
        };

    private static final String TESTXNG_XML_SCHEMA = loadResource("TestXNG.xsd");
    private static final String TESTXNG_EXTENSION = ".testxng";
    private File dir;
    private final Results results = new Results();
    private boolean testsRun;
    private final Logger logger = Logger.getLogger(this.toString());

    /**
     *
     * Creates an TestXNG engine for a directory.
     *
     * @param dir
     *            The directory in which the tests reside as a String
     */
    Engine(String dir) {
        this(new File(dir), null);
    }

    /**
     *
     * Creates an TestXNG engine for a directory.
     *
     * @param dir
     *            The directory in which the tests reside
     */
    Engine(File dir, String logFile) {
        this.dir = dir;

        if (logFile != null) {
            tryInitializeLogger(logFile);
        }
    }

    private void tryInitializeLogger(String logFile) {
        try {
            initializeLogger(logFile);
        } catch (IOException ioe) {
            logger.warn("Could not create an appender for Log4j to " + logFile,
                ioe);
        }
    }

    private void initializeLogger(String logFile) throws IOException {
        FileAppender appender = new FileAppender(new SimpleLayout(), logFile);
        logger.addAppender(appender);
    }

    /**
     * Creates an TestXNG engine for a directory, runs the tests and returns the
     * results.
     *
     * @param dir
     *            The directory in which the tests reside
     * @return The results of running all the tests found in the test directory.
     * @throws MalformedTestDefinitionFileException
     */
    public static Results createAndRunForDirectory(File dir)
        throws IOException, MalformedTestDefinitionFileException {
        return createAndRunForDirectory(dir, null);
    }

    public static Results createAndRunForDirectory(File dir, String logFile)
        throws IOException, MalformedTestDefinitionFileException {
        Engine engine = new Engine(dir, logFile);
        engine.run();

        return engine.getResults();
    }

    public static Results createAndRunForDirectory(String dir)
        throws IOException, MalformedTestDefinitionFileException {
        return createAndRunForDirectory(dir, null);
    }

    public static Results createAndRunForDirectory(String dir, String logFile)
        throws IOException, MalformedTestDefinitionFileException {
        return createAndRunForDirectory(new File(dir), logFile);
    }

    private static String loadResource(String resourceName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(resourceName);

        return getContent(is, resourceName);
    }

    private static String getContent(InputStream is, String resourceName) {
        try {
            return tryGetContent(is);
        } catch (IOException e) {
            throw new RuntimeException(
                "Could not load the content of the resource" + resourceName, e);
        }
    }

    private static String tryGetContent(InputStream is)
        throws IOException {
        StringBuffer sb = new StringBuffer();
        int c;

        while ((c = is.read()) != -1) {
            sb.append((char) c);
        }

        return sb.toString();
    }

    /**
     * Runs the engine. This includes reading the tests from all TestXNG files
     * in the test directory, and then running each of these tests.
     *
     * @throws MalformedTestDefinitionFileException
     *
     */
    void run() throws IOException, MalformedTestDefinitionFileException {
        List<Test> tests = getTestsFromDir();
        runTests(tests);
    }

    List<Test> getTestsFromDir()
        throws IOException, MalformedTestDefinitionFileException {
        List<File> testFiles = getTestFilesFromDir();

        return getTestsFromTestFiles(testFiles);
    }

    private List<Test> getTestsFromTestFiles(List<File> testFiles)
        throws MalformedTestDefinitionFileException {
        List<Test> tests = new ArrayList<Test>();

        for (File testFile : testFiles) {
            tests.addAll(getTestsFromTestFile(testFile));
        }

        return tests;
    }

    List<Test> getTestsFromTestFile(File testFile)
        throws MalformedTestDefinitionFileException {
        List<Element> testElements = getTestNodeListFromTestFile(testFile);
        List<Test> tests = new ArrayList<Test>();

        for (Element testElement : testElements) {
            tests.add(Test.createTestFromNode(testElement, testFile));
        }

        return tests;
    }

    private List<Element> getTestNodeListFromTestFile(File file)
        throws MalformedTestDefinitionFileException {
        try {
            return tryGetTestNodeListFromTestFile(file);
        } catch (SAXException se) {
            throw new MalformedTestDefinitionFileException(se, file);
        } catch (Exception e) {
            logger.warn("An exception occured while trying to get the node list from a test file",
                e);

            return null;
        }
    }

    private List<Element> tryGetTestNodeListFromTestFile(File file)
        throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        dbf.setAttribute(JAXP_SCHEMA_SOURCE,
            new InputSource(new StringReader(TESTXNG_XML_SCHEMA)));

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(Engine.NON_VALIDATION_DETECTOR);

        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();

        Node tests = XmlNavigator.getFirstElementSubnodeWithName(doc, "Tests");

        return XmlNavigator.getAllElementSubnodesWithName(tests, Test.TAG_NAME);
    }

    List<File> getTestFilesFromDir() throws IOException {
        if (!dir.exists()) {
            logger.warn("TestXNG directory " + dir + " does not exist");
            throw new IOException("TestXNG directory " + dir +
                " does not exist");
        }

        File[] files = dir.listFiles(new FilenameFilter() {
                    public boolean accept(File d, String n) {
                        return n.endsWith(TESTXNG_EXTENSION);
                    }
                });

        return Arrays.asList(files);
    }

    private void runTests(List<Test> tests) {
        for (Test test : tests) {
            runTestAndAddToResults(test);
        }

        testsRun = true;
    }

    private void runTestAndAddToResults(Test test) {
        logger.info("Running test " + test.getId());
        test.run(logger);
        results.addTest(test);
        logger.info("Result for test " + test.getId() + ": " + test.getState());
    }

    /**
     * Returns a Results object reflecting the results of running all the
     * TestXNG tests in this engine.
     *
     * @exception IllegalStateException
     *                if the tests haven't been run before this method is
     *                called.
     * @return A Results object containing all information about the tests run
     *         by this object.
     */
    Results getResults() {
        if (!testsRun) {
            logger.warn(
                "You must run the tests before you can ask for the results");
            throw new IllegalStateException(
                "You must run the tests before you can ask for the results");
        }

        return results;
    }
}
