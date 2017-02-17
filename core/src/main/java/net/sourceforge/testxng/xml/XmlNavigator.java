package net.sourceforge.testxng.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Helper class to handle navigation in XML.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: XmlNavigator.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public final class XmlNavigator {
    /**
     * Private constructor to prevent instantiation.
     *
     */
    private XmlNavigator() {
    }

    /**
     * Returns the node value for a tag from an element, or null if it doesn't
     * exist.
     *
     * @param element
     *            The element to start from
     * @param tagName
     *            The name of the tag for which the node value should be
     *            returned
     * @return A String containing the node value, or null.
     */
    public static String getNodeValueForFirstElementSubnodeWithTagname(
        Element element, String tagName) {
        NodeList elementsWithTagnameNodeList = element.getElementsByTagName(tagName);

        if (elementsWithTagnameNodeList.getLength() == 0) {
            return null;
        }

        Element firstElementWithTagname = (Element) elementsWithTagnameNodeList.item(0);
        NodeList firstElementWithTagnameNodeList = firstElementWithTagname.getChildNodes();

        return ((Node) firstElementWithTagnameNodeList.item(0)).getNodeValue();
    }

    /**
     * Returns the first subnode that is an Element, or null if there isn't one.
     *
     * @param node
     *            The parent Node
     * @return The first subnode that is an Element, or null.
     */
    public static Element getFirstElementSubnode(Node node) {
        NodeList subnodes = node.getChildNodes();

        for (int i = 0; i < subnodes.getLength(); i++) {
            Node subnode = subnodes.item(i);

            if (subnode instanceof Element) {
                return (Element) subnode;
            }
        }

        return null;
    }

    /**
     * Returns the first subnode that is an Element and that has the given tag
     * name, or null if there isn't one.
     *
     * @param node
     *            The parent Node.
     * @param tagName
     *            The tag name the subnode should have.
     * @return A subnode of the parent node with the given tag name, or null
     */
    public static Element getFirstElementSubnodeWithName(Node node,
        String tagName) {
        List<Element> elements = getAllElementSubnodesWithName(node, tagName);

        if (elements.isEmpty()) {
            return null;
        } else {
            return elements.get(0);
        }
    }

    /**
     * Returns all subnodes that are Elements and that have a given tag name. If
     * there aren't such subnodes, an empty List will be returned.
     *
     * @param node
     *            The parent Node.
     * @param tagName
     *            The tag name the subnode should have.
     * @return A List with all subnodes of the parent Node with the given tag
     *         name.
     */
    public static List<Element> getAllElementSubnodesWithName(Node node,
        String tagName) {
        List<Element> result = new ArrayList<Element>();
        NodeList subnodes = node.getChildNodes();

        for (int i = 0; i < subnodes.getLength(); i++) {
            Node subnode = subnodes.item(i);

            if ((subnode instanceof Element) &&
                    (subnode.getNodeName().equals(tagName))) {
                result.add((Element) subnode);
            }
        }

        return result;
    }

    /**
     * Checks whether the given Element is a text element, that is, an element
     * containing only one child of type Text.
     *
     * @param element
     *            The element to test.
     * @return True if the Element contains only one child of type Text
     */
    static boolean isTextElement(Element element) {
        NodeList children = element.getChildNodes();

        if (children.getLength() != 1) {
            return false;
        }

        return children.item(0) instanceof Text;
    }

    static String getTextFromTextElement(Element element) {
        if (!isTextElement(element)) {
            throw new IllegalArgumentException(
                "Cannot get text from a non-text element " +
                element.getNodeName());
        }

        NodeList children = element.getChildNodes();
        Node child = children.item(0);

        return child.getNodeValue();
    }

    /**
     * Checks whether an element has a subnode with the given name.
     *
     * @param element The element to test.
     * @param tagName The tag name to check for.
     * @return True if the element has at least one child with the given tag name.
     */
    public static boolean hasSubnodeWithTagname(Element element, String tagName) {
        return !getAllElementSubnodesWithName(element, tagName).isEmpty();
    }
}
