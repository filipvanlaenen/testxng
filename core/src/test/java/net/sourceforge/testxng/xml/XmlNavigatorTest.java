package net.sourceforge.testxng.xml;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Unit tests on the <code>XmlNavigator</code> class.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: XmlNavigatorTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class XmlNavigatorTest {
    private static final String FOO = "Foo";
    private static final String BAZ = "Baz";
    private static final String BAR = "Bar";
    private static final String LOREM_IPSUM = "Lorem ipsum";
    private Document document;
    private Element foo;
    private Element bar;
    private Element bar2;
    private Element bar3;
    private Element baz;

    private void addFooToDocument() {
        foo = document.createElement(FOO);
        document.appendChild(foo);
    }

    private void addBarBazToFoo() {
        bar = document.createElement(BAR);
        foo.appendChild(bar);
        baz = document.createElement(BAZ);
        foo.appendChild(baz);
    }

    private void addFooBarBazToDocument() {
        addFooToDocument();
        addBarBazToFoo();
    }

    private void addSecondBarToFoo() {
        bar2 = document.createElement(BAR);
        foo.appendChild(bar2);
    }

    private void addThirdBarToBaz() {
        bar3 = document.createElement(BAR);
        baz.appendChild(bar3);
    }

    private void addTextLoremIpsumToBar() {
        Text loremIpsum = document.createTextNode(LOREM_IPSUM);
        bar.appendChild(loremIpsum);
    }

    @BeforeMethod(alwaysRun = true)
    void createNewXmlDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();
    }

    @Test(groups = "unit")
    public void shouldGetTheFirstElementSubnodeIfMatchingName() {
        addFooBarBazToDocument();
        assertEquals(XmlNavigator.getFirstElementSubnodeWithName(foo, BAR), bar);
    }

    @Test(groups = "unit")
    public void shouldGetTheFirstElementSubnodeOfTwoIfMatchingName() {
        addFooBarBazToDocument();
        addSecondBarToFoo();
        assertEquals(XmlNavigator.getFirstElementSubnodeWithName(foo, BAR), bar);
    }

    @Test(groups = "unit")
    public void shouldGetTheSecondElementSubnodeIfMatchingName() {
        addFooBarBazToDocument();
        assertEquals(XmlNavigator.getFirstElementSubnodeWithName(foo, BAZ), baz);
    }

    @Test(groups = "unit")
    public void shouldReturnNullWhenNoSubnodeMatchingName() {
        addFooBarBazToDocument();
        assertNull(XmlNavigator.getFirstElementSubnodeWithName(foo, "qux"));
    }

    @Test(groups = "unit")
    public void elementWithSubnodesIsNotTextElement() {
        addFooBarBazToDocument();
        assertFalse(XmlNavigator.isTextElement(foo));
    }

    @Test(groups = "unit")
    public void emptyElementIsNotTextElement() {
        addFooBarBazToDocument();
        assertFalse(XmlNavigator.isTextElement(bar));
    }

    @Test(groups = "unit")
    public void elementWithTextIsTextElement() {
        addFooBarBazToDocument();
        addTextLoremIpsumToBar();
        assertTrue(XmlNavigator.isTextElement(bar));
    }

    @Test(groups = "unit")
    public void shouldGetTheTextFromATextElement() {
        addFooBarBazToDocument();
        addTextLoremIpsumToBar();
        assertEquals(XmlNavigator.getTextFromTextElement(bar), LOREM_IPSUM);
    }

    @Test(groups = "unit", expectedExceptions =  {
        IllegalArgumentException.class}
    )
    public void shouldThrowIllegalArgumentExceptionWhenAskedToReturnTextFromANonTextElement() {
        addFooBarBazToDocument();
        XmlNavigator.getTextFromTextElement(bar);
    }

    @Test(groups = "unit")
    public void shouldReturnAllElementSubnodes() {
        addFooBarBazToDocument();
        addSecondBarToFoo();
        addThirdBarToBaz();

        List<Element> bars = XmlNavigator.getAllElementSubnodesWithName(foo, BAR);
        assertTrue(bars.contains(bar) && bars.contains(bar2));
    }

    @Test(groups = "unit")
    public void shouldNotReturnSubsubnodeBetweenElementSubnodes() {
        addFooBarBazToDocument();
        addSecondBarToFoo();
        addThirdBarToBaz();

        List<Element> bars = XmlNavigator.getAllElementSubnodesWithName(foo, BAR);
        assertFalse(bars.contains(bar3));
    }

    @Test(groups = "unit")
    public void shouldDetectPresenceOfElement() {
        addFooBarBazToDocument();
        assertTrue(XmlNavigator.hasSubnodeWithTagname(foo, BAR));
    }

    @Test(groups = "unit")
    public void shouldDetectAbsenceOfElement() {
        addFooBarBazToDocument();
        assertFalse(XmlNavigator.hasSubnodeWithTagname(foo, FOO));
    }
}
