package net.sourceforge.testxng.domain;

import net.sourceforge.testxng.xml.XmlNavigator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;


/**
 * Factory class with static methods to create Assertion objects.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: Assertions.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public final class Assertions {
    private static final String VALIDATES_TAG_NAME = "Validates";
    private static final String DOES_NOT_VALIDATE_TAG_NAME = "DoesNotValidate";
    private static final String TRANSFORMS_TAG_NAME = "Transforms";
    private static final Map<String, Class> TAGNAME_TO_CLASS_MAP = new HashMap<String, Class>();

    static {
        TAGNAME_TO_CLASS_MAP.put(VALIDATES_TAG_NAME, ValidatesAssertion.class);
        TAGNAME_TO_CLASS_MAP.put(DOES_NOT_VALIDATE_TAG_NAME,
            DoesNotValidateAssertion.class);
        TAGNAME_TO_CLASS_MAP.put(TRANSFORMS_TAG_NAME, TransformsAssertion.class);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private Assertions() {
    }

    /**
     * Creates an assertion of the appropriate subclass from an XML node, and
     * connects it to the test.
     *
     * @param node
     *            The XML to create the assertion from.
     * @param test
     *            The test to which the assertion belongs
     * @return An assertion of the appropriate subclass.
     */
    static Assertion createAssertionFromNode(Node node, Test test) {
        Element subnode = XmlNavigator.getFirstElementSubnode(node);
        Assertion assertion = createNewAssertionInstanceOfCorrectType(subnode);
        assertion.setTest(test);
        assertion.initializeFurtherFromElement(subnode);

        return assertion;
    }

    private static Assertion createNewAssertionInstanceOfCorrectType(
        Element node) {
        try {
            return tryCreateNewAssertionInstanceOfCorrectType(node);
        } catch (Exception e) {
            return handleInstantiationException(e);
        }
    }

    private static Assertion handleInstantiationException(Exception e) {
        // This should not happen, but if it happens, something is seriously
        // wrong.
        // Print out a stack trace to System.err
        e.printStackTrace(System.err);

        // Return null, which will almost certainly result in a
        // NullPointerException later on.
        return null;
    }

    private static Assertion tryCreateNewAssertionInstanceOfCorrectType(
        Element node) throws InstantiationException, IllegalAccessException {
        Class clazz = TAGNAME_TO_CLASS_MAP.get(node.getNodeName());

        return (Assertion) clazz.newInstance();
    }
}
