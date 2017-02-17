package net.sourceforge.testxng.domain;

import net.sourceforge.testxng.xml.XmlNavigator;
import net.sourceforge.testxng.xml.XmlToStringPersister;

import org.apache.log4j.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;

import java.util.ArrayList;
import java.util.List;


/**
 * Class representing a single test.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: Test.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class Test {
    static final String TAG_NAME = "Test";
    private static final String ID_ATTR_NAME = "id";
    private static final String SOURCE_TAG_NAME = "Source";
    private static final String SOURCE_FILE_TAG_NAME = "SourceFile";
    private static final String ASSERTION_TAG_NAME = "Assert";
    private File file;
    private String id;
    private String sourceFilename;
    private String source;
    private List<Assertion> assertions;
    private State state = State.NOT_RUN;

    static Test createTestFromNode(Node node, File file) {
        Test test = new Test();
        Element element = (Element) node;
        test.setFile(file);
        test.setId(element.getAttribute(ID_ATTR_NAME));
        test.setSource(XmlNavigator.getFirstElementSubnodeWithName(element,
                SOURCE_TAG_NAME));
        test.setSourceFilename(XmlNavigator.getNodeValueForFirstElementSubnodeWithTagname(
                element, SOURCE_FILE_TAG_NAME));
        test.setAssertions(XmlNavigator.getAllElementSubnodesWithName(element,
                ASSERTION_TAG_NAME));

        return test;
    }

    private void setSource(Element element) {
        if (element != null) {
            setSource(XmlToStringPersister.toXmlString(
                    XmlNavigator.getFirstElementSubnode(element)));
        }
    }

    private void setSource(String source) {
        this.source = source;
    }

    private void setFile(File file) {
        this.file = file;
    }

    private void setAssertions(List<Element> assertionElements) {
        assertions = new ArrayList<Assertion>();

        for (Element assertionElement : assertionElements) {
            assertions.add(Assertions.createAssertionFromNode(
                    assertionElement, this));
        }
    }

    private void setSourceFilename(String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }

    private void setId(String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }

    void run(Logger logger) {
        try {
            boolean success = tryCheckAllAssertions(logger);
            this.state = success ? State.SUCCESS : State.FAILURE;
        } catch (Exception e) {
            logger.warn("An exception was thrown while running test " + id, e);
            this.state = State.ERROR;
        }
    }

    private boolean tryCheckAllAssertions(Logger logger)
        throws Exception {
        for (Assertion assertion : assertions) {
            boolean success = assertion.check(logger);

            if (!success) {
                return false;
            }
        }

        return true;
    }

    String getSourceFilename() {
        return sourceFilename;
    }

    String getAbsoluteSourceFilename() {
        return getAbsoluteFilenameFromRelativeToSourceFilename(sourceFilename);
    }

    String getAbsoluteFilenameFromRelativeToSourceFilename(String filename) {
        return file.getParent() + File.separator + filename;
    }

    List<Assertion> getAssertions() {
        return assertions;
    }

    State getState() {
        return state;
    }

    String getSource() {
        return source;
    }

    String getFilename() {
        return file.getName();
    }

    /**
     * An enumeration with the states that are relevant for a Test.
     */
    enum State {NOT_RUN,
        SUCCESS,
        FAILURE,
        ERROR,
        SKIPPED;
    }
}
