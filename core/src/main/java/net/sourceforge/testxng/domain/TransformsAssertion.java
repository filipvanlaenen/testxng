package net.sourceforge.testxng.domain;

import net.sourceforge.testxng.xml.XmlNavigator;
import net.sourceforge.testxng.xml.XmlToStringPersister;

import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * Class representing an XSL Transformation assertion.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: TransformsAssertion.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class TransformsAssertion extends Assertion {
    private static final String EXPECTED_RESULT_TAG_NAME = "ExpectedResult";
    private static final String EXPECTED_RESULT_FILE_TAG_NAME = "ExpectedResultFile";
    private static final String TRANSFORMATION_TAG_NAME = "Transformation";
    private static final String MODE_TAG_NAME = "Mode";
    private static final String AS_ROOT_ELEMENT_TAG_NAME = "AsRootElement";
    private String expectedResult;
    private String expectedResultFilename;
    private String transformationFilename;
    private String mode;
    private boolean asRootElement;

    @Override
    boolean check(Logger logger) throws Exception {
        String actualResult = tryGetTransformationResult(logger);

        return isEquivalentToExpectedResult(actualResult, logger);
    }

    private boolean isEquivalentToExpectedResult(String actualResult,
        Logger logger) throws IOException {
        String expectedResultString = getExpectedResultString();
        boolean equivalence = new StringsLiterallyEqualComparator().equivalent(actualResult,
                expectedResultString);

        if (!equivalence) {
            logger.warn("Test failed: expected [" + expectedResultString +
                "] but was [" + actualResult + "]");
        }

        return equivalence;
    }

    private String getExpectedResultString() throws IOException {
        if (expectedResult == null) {
            return getContentFromFile(expectedResultFilename);
        } else {
            return expectedResult;
        }
    }

    private String getContentFromFile(String filename)
        throws IOException {
        FileReader fr = new FileReader(test.getAbsoluteFilenameFromRelativeToSourceFilename(
                    filename));
        BufferedReader br = new BufferedReader(fr);
        String nextLine = "";
        String lineSep = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer();

        while ((nextLine = br.readLine()) != null) {
            sb.append(nextLine);
            sb.append(lineSep);
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    private String tryGetTransformationResult(Logger logger)
        throws ParserConfigurationException, SAXException, IOException,
            TransformerException {
        DOMSource source = createDOMSource();
        Transformer transformer = createTransformer(logger);
        StringWriter resultWriter = new StringWriter();
        StreamResult transformationResult = new StreamResult(resultWriter);
        transformer.transform(source, transformationResult);

        return resultWriter.toString();
    }

    private Transformer createTransformer(Logger logger)
        throws TransformerConfigurationException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(createTransformationStreamSource(
                    logger));
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        return transformer;
    }

    private String addTemplate(String transformation, String template) {
        return transformation.replace("</xsl:transform>",
            template + "</xsl:transform>");
    }

    private StreamSource createTransformationStreamSource(Logger logger) {
        try {
            return tryCreateTransformationStreamSource();
        } catch (IOException e) {
            logger.warn("An exception occured while trying to create the transformation source",
                e);
        }

        return null;
    }

    private StreamSource tryCreateTransformationStreamSource()
        throws IOException {
        String transformation = getContentFromFile(transformationFilename);

        if (hasMode()) {
            transformation = addTemplate(transformation, getModeTemplate());
        } else if (!isAsRootElement()) {
            transformation = addTemplate(transformation, getRootTemplate());
        }

        return new StreamSource(new StringReader(transformation));
    }

    private boolean hasMode() {
        return getMode() != null;
    }

    private String getRootTemplate() {
        return "<xsl:template match=\"" + getRootElementTag() +
        "\"><xsl:apply-templates select=\"*\"/></xsl:template>";
    }

    private String getModeTemplate() {
        return "<xsl:template match=\"" + getRootElementTag() +
        "\"><xsl:apply-templates select=\"*\" mode=\"" + getMode() +
        "\"/> </xsl:template>";
    }

    private DOMSource createDOMSource()
        throws ParserConfigurationException, SAXException, IOException {
        return new DOMSource(createSourceDocument());
    }

    private String getRootElementTag() {
        String tag = null;

        if (hasMode()) {
            tag = "TestXngMode" + getMode();
        } else if (!isAsRootElement()) {
            tag = "TestXngRoot";
        }

        return tag;
    }

    private Document createSourceDocument()
        throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        if (test.getSource() == null) {
            return builder.parse(new File(test.getSourceFilename()));
        } else {
            String source = test.getSource();
            String rootElementTag = getRootElementTag();

            if (rootElementTag != null) {
                source = wrapInElement(rootElementTag, source);
            }

            return builder.parse(new InputSource(new StringReader(source)));
        }
    }

    private String wrapInElement(String tag, String source) {
        StringBuffer sb = new StringBuffer();
        sb.append('<');
        sb.append(tag);
        sb.append('>');
        sb.append(source);
        sb.append("</");
        sb.append(tag);
        sb.append('>');

        return sb.toString();
    }

    @Override
    void initializeFurtherFromElement(Element element) {
        setExpectedResult(XmlNavigator.getFirstElementSubnodeWithName(element,
                EXPECTED_RESULT_TAG_NAME));
        setExpectedResultFilename(XmlNavigator.getNodeValueForFirstElementSubnodeWithTagname(
                element, EXPECTED_RESULT_FILE_TAG_NAME));
        setTransformationFilename(XmlNavigator.getNodeValueForFirstElementSubnodeWithTagname(
                element, TRANSFORMATION_TAG_NAME));
        setMode(XmlNavigator.getNodeValueForFirstElementSubnodeWithTagname(
                element, MODE_TAG_NAME));
        setAsRootElement(XmlNavigator.hasSubnodeWithTagname(element,
                AS_ROOT_ELEMENT_TAG_NAME));
    }

    private void setTransformationFilename(String transformationFilename) {
        this.transformationFilename = transformationFilename;
    }

    private void setExpectedResultFilename(String expectedResultFilename) {
        this.expectedResultFilename = expectedResultFilename;
    }

    private void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    private void setExpectedResult(Element element) {
        if (element != null) {
            setExpectedResult(XmlToStringPersister.toXmlString(
                    XmlNavigator.getFirstElementSubnode(element)));
        }
    }

    private void setMode(String mode) {
        this.mode = mode;
    }

    String getExpectedResult() {
        return expectedResult;
    }

    String getExpectedResultFilename() {
        return expectedResultFilename;
    }

    String getTransformationFilename() {
        return transformationFilename;
    }

    String getMode() {
        return mode;
    }

    private boolean isAsRootElement() {
        return asRootElement;
    }

    private void setAsRootElement(boolean asRootElement) {
        this.asRootElement = asRootElement;
    }
}
