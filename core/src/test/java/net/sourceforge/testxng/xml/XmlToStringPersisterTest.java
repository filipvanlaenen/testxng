package net.sourceforge.testxng.xml;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Unit tests on the <code>XmlToStringPersister</code> class.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: XmlToStringPersisterTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class XmlToStringPersisterTest {
    private static final String FOO = "Foo";
    private static final String LOREM_IPSUM = "Lorem ipsum";
    private static final String DOLOR_SIT = "Dolor sit";
    private static final String BAZ = "Baz";
    private static final String BAR = "Bar";
    private Document document;
    private Element foo;
    private Element bar;

    @BeforeMethod(alwaysRun = true)
    void createNewXmlDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();
    }

    private void addFooToDocument() {
        foo = document.createElement(FOO);
        document.appendChild(foo);
    }

    private void addBarBazToFoo() {
        bar = document.createElement(BAR);
        foo.appendChild(bar);

        Element baz = document.createElement(BAZ);
        foo.appendChild(baz);
    }

    private void addFooBarBazToDocument() {
        addFooToDocument();
        addBarBazToFoo();
    }

    private void addTextLoremIpsumToElement(Element element) {
        Text loremIpsum = document.createTextNode(LOREM_IPSUM);
        element.appendChild(loremIpsum);
    }

    private String emptyElement(String tag) {
        return XmlToStringPersister.OPEN_TAG + tag +
        XmlToStringPersister.CLOSING_TAG_MARKER +
        XmlToStringPersister.CLOSE_TAG;
    }

    private String openElement(String tag) {
        return XmlToStringPersister.OPEN_TAG + tag +
        XmlToStringPersister.CLOSE_TAG;
    }

    private String closeElement(String tag) {
        return XmlToStringPersister.OPEN_TAG +
        XmlToStringPersister.CLOSING_TAG_MARKER + tag +
        XmlToStringPersister.CLOSE_TAG;
    }

    private String elementWithContent(String tag, String content) {
        return openElement(tag) + content + closeElement(tag);
    }

    private String emptyElementWithAttribute(String tag, String attributeName,
        String attributeValue) {
        return XmlToStringPersister.OPEN_TAG + tag +
        XmlToStringPersister.SPACE + attributeName +
        XmlToStringPersister.EQUALS + XmlToStringPersister.QUOTE +
        attributeValue + XmlToStringPersister.QUOTE +
        XmlToStringPersister.CLOSING_TAG_MARKER +
        XmlToStringPersister.CLOSE_TAG;
    }

    private String elementWithContentAndAttribute(String tag, String content,
        String attributeName, String attributeValue) {
        return XmlToStringPersister.OPEN_TAG + tag +
        XmlToStringPersister.SPACE + attributeName +
        XmlToStringPersister.EQUALS + XmlToStringPersister.QUOTE +
        attributeValue + XmlToStringPersister.QUOTE +
        XmlToStringPersister.CLOSE_TAG + content +
        XmlToStringPersister.OPEN_TAG +
        XmlToStringPersister.CLOSING_TAG_MARKER + tag +
        XmlToStringPersister.CLOSE_TAG;
    }

    @Test(groups = "unit")
    public void shouldWriteEmptyElementAsXmlString() {
        addFooBarBazToDocument();
        assertEquals(XmlToStringPersister.toXmlString(bar), emptyElement(BAR));
    }

    @Test(groups = "unit")
    public void shouldWriteTextElementAsXmlString() {
        addFooBarBazToDocument();
        addTextLoremIpsumToElement(bar);
        assertEquals(XmlToStringPersister.toXmlString(bar),
            elementWithContent(BAR, LOREM_IPSUM));
    }

    @Test(groups = "unit")
    public void shouldWriteElementTreeAsXmlString() {
        addFooBarBazToDocument();
        assertEquals(XmlToStringPersister.toXmlString(foo),
            elementWithContent(FOO, emptyElement(BAR) + emptyElement(BAZ)));
    }

    @Test(groups = "unit")
    public void shouldWriteElementTreeWithTextAsXmlString() {
        addFooBarBazToDocument();

        Text loremIpsum = document.createTextNode(LOREM_IPSUM);
        foo.appendChild(loremIpsum);
        assertEquals(XmlToStringPersister.toXmlString(foo),
            elementWithContent(FOO,
                emptyElement(BAR) + emptyElement(BAZ) + LOREM_IPSUM));
    }

    @Test(groups = "unit")
    public void shouldWriteEmptyElementWithAttributeAsXmlString() {
        addFooToDocument();
        foo.setAttribute(BAR, LOREM_IPSUM);
        assertEquals(XmlToStringPersister.toXmlString(foo),
            emptyElementWithAttribute(FOO, BAR, LOREM_IPSUM));
    }

    @Test(groups = "unit")
    public void shouldWriteElementWithTextAndAttributeAsXmlString() {
        addFooToDocument();
        addTextLoremIpsumToElement(foo);
        foo.setAttribute(BAR, DOLOR_SIT);
        assertEquals(XmlToStringPersister.toXmlString(foo),
            elementWithContentAndAttribute(FOO, LOREM_IPSUM, BAR, DOLOR_SIT));
    }
}
