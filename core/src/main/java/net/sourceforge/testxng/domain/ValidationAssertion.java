package net.sourceforge.testxng.domain;

import net.sourceforge.testxng.xml.XmlNavigator;

import org.apache.log4j.Logger;

import org.w3c.dom.Element;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Abstract class representing an assertion about the validation of a portion of
 * XML against an XML Schema.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: ValidationAssertion.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public abstract class ValidationAssertion extends Assertion {
    private static final DocumentBuilderFactory DEFAULT_DOCUMENT_BUILDER_FACTORY =
        createValidatingDocumentBuilderFactory();
    private String schemaFilename;

    private static DocumentBuilderFactory createValidatingDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        factory.setAttribute(Engine.JAXP_SCHEMA_LANGUAGE, Engine.W3C_XML_SCHEMA);

        return factory;
    }

    private DocumentBuilderFactory getValidatingDocumentBuilderFactory() {
        if (schemaFilename == null) {
            return DEFAULT_DOCUMENT_BUILDER_FACTORY;
        } else {
            DocumentBuilderFactory factory = createValidatingDocumentBuilderFactory();
            factory.setAttribute(Engine.JAXP_SCHEMA_SOURCE,
                new File(getAbsoluteSchemaFilename()));

            return factory;
        }
    }

    private void setSchemaFilename(String schemaFilename) {
        this.schemaFilename = schemaFilename;
    }

    boolean check(Logger logger)
        throws ParserConfigurationException, SAXException, IOException {
        try {
            tryToValidateDocument();

            return isPositiveAssertion();
        } catch (SAXParseException spe) {
            return !isPositiveAssertion();
        }
    }

    abstract boolean isPositiveAssertion();

    private void tryToValidateDocument()
        throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = getValidatingDocumentBuilderFactory();
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        builder.setErrorHandler(Engine.NON_VALIDATION_DETECTOR);

        if (test.getSource() == null) {
            builder.parse(test.getAbsoluteSourceFilename());
        } else {
            builder.parse(new InputSource(new StringReader(test.getSource())));
        }
    }

    private String getAbsoluteSchemaFilename() {
        assert schemaFilename != null;

        return test.getAbsoluteFilenameFromRelativeToSourceFilename(schemaFilename);
    }

    String getSchemaFilename() {
        return schemaFilename;
    }

    @Override
    void initializeFurtherFromElement(Element element) {
        setSchemaFilename(XmlNavigator.getNodeValueForFirstElementSubnodeWithTagname(
                element, "Schema"));
    }
}
