package net.sourceforge.testxng.xml;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


/**
 * Helper class to to persist XML to Strings.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: XmlToStringPersister.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public final class XmlToStringPersister {
    static final String CLOSE_TAG = ">";
    static final String OPEN_TAG = "<";
    static final String CLOSING_TAG_MARKER = "/";
    static final String SPACE = " ";
    static final String EQUALS = "=";
    static final String QUOTE = "\"";

    /**
     * Private constructor to precent instantiation.
     *
     */
    private XmlToStringPersister() {
    }

    public static String toXmlString(Node node) {
        String result = "";

        if (node instanceof Element) {
            result = toXmlString((Element) node);
        } else if (node instanceof Text) {
            result = node.getNodeValue();
        }

        return result;
    }

    private static String toXmlString(Element element) {
        String result;

        if (isEmptyElementWithNoAttributes(element)) {
            result = emptyElementWithNoAttributesToXmlString(element);
        } else if (isEmptyElementWithAttributes(element)) {
            result = emptyElementWithAttributesToXmlString(element);
        } else if (XmlNavigator.isTextElement(element)) {
            result = textElementToXmlString(element);
        } else {
            result = treeElementToXmlString(element);
        }

        return result;
    }

    private static String createContentInsideElement(Element element) {
        NodeList childNodes = element.getChildNodes();
        StringBuffer childrenAsXmlStringBuffer = new StringBuffer();

        for (int i = 0; i < childNodes.getLength(); i++) {
            childrenAsXmlStringBuffer.append(toXmlString(childNodes.item(i)));
        }

        return childrenAsXmlStringBuffer.toString();
    }

    private static String createAttributes(Element element) {
        StringBuffer attributesStringBuffer = new StringBuffer();

        NamedNodeMap attributes = element.getAttributes();

        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);

            attributesStringBuffer.append(SPACE).append(attribute.getNodeName())
                                  .append(EQUALS).append(QUOTE)
                                  .append(attribute.getNodeValue()).append(QUOTE);
        }

        return attributesStringBuffer.toString();
    }

    private static String treeElementToXmlString(Element element) {
        return elementWithContentXmlString(element.getNodeName(),
            createAttributes(element), createContentInsideElement(element));
    }

    private static String elementWithContentXmlString(String tag,
        String attributes, String content) {
        return OPEN_TAG + tag + attributes + CLOSE_TAG + content + OPEN_TAG +
        CLOSING_TAG_MARKER + tag + CLOSE_TAG;
    }

    private static String textElementToXmlString(Element element) {
        return elementWithContentXmlString(element.getNodeName(),
            createAttributes(element),
            XmlNavigator.getTextFromTextElement(element));
    }

    private static String emptyElementWithNoAttributesToXmlString(
        Element element) {
        return OPEN_TAG + element.getNodeName() + CLOSING_TAG_MARKER +
        CLOSE_TAG;
    }

    private static boolean isEmptyElement(Element element) {
        return element.getChildNodes().getLength() == 0;
    }

    private static boolean isEmptyElementWithNoAttributes(Element element) {
        return isEmptyElement(element) && !hasAttributes(element);
    }

    private static boolean hasAttributes(Element element) {
        return element.getAttributes().getLength() > 0;
    }

    private static boolean isEmptyElementWithAttributes(Element element) {
        return isEmptyElement(element) && hasAttributes(element);
    }

    private static String emptyElementWithAttributesToXmlString(Element element) {
        return OPEN_TAG + element.getNodeName() + createAttributes(element) +
        CLOSING_TAG_MARKER + CLOSE_TAG;
    }
}
