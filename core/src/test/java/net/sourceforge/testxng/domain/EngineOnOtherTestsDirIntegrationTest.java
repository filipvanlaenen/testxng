package net.sourceforge.testxng.domain;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;


/**
 * Integration tests on the <code>Engine</code> class using the test files in the so-called other directory.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: EngineOnOtherTestsDirIntegrationTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class EngineOnOtherTestsDirIntegrationTest
    extends AbstractEngineOnADirIntegrationTest {
    private static final String TRANSFORMS_CAN_HAVE_EXPECTED_RESULT_WITH_XML_CODE_TEST_ID =
        "TransformsCanHaveExpectedResultWithXmlCode";
    private static final String TRANSFORMS_CAN_HAVE_EXPECTED_RESULT_FILENAME_TEST_ID =
        "TransformsCanHaveExpectedResultFilename";
    private static final String TRANSFORMS_CAN_HAVE_MODE_TEST_ID = "TransformsCanHaveAMode";
    private static final String RESULT_ELEMENTS_CAN_HAVE_ATTRIBUTES_ID = "ResultElementsCanHaveAttributes";
    private static final String TRANSFORMS_AT_THE_ROOT_CAN_BE_DIFFERENT_ID = "TransformsAtTheRootCanBeDifferent";
    private static final String FOO_BAR_LOREM_IPSUM_XML_CODE = "<Foo><Bar>Lorem ipsum</Bar></Foo>";
    private static final String FOO_BAR_WITH_LOREM_IPSUM_XML_CODE = "<Foo>A Bar with Lorem ipsum</Foo>";
    private static final String DIR = TEST_RESOURCES_DIR + "otherTests";

    @BeforeTest(groups = "integration")
    void beforeTest() throws IOException, MalformedTestDefinitionFileException {
        initializeLogger(EngineOnOtherTestsDirIntegrationTest.class);
        initializeTestMap(DIR);
    }

    @Test(groups = "integration")
    public void shouldAllowSchemaReferenceInValidates() {
        net.sourceforge.testxng.domain.Test test = testMap.get(
                "ValidatesCanHaveASchemaReference");
        ValidatesAssertion assertion = (ValidatesAssertion) test.getAssertions()
                                                                .get(0);
        assertEquals(assertion.getSchemaFilename(), "FooBar.xsd");
    }

    @Test(groups = "integration", dependsOnMethods =  {
        "shouldAllowSchemaReferenceInValidates"}
    )
    public void shouldValidateXmlSourceFileWithSchemaReferenceInTestDefinition() {
        runTestAndAssertForSuccess("ValidatesCanHaveASchemaReference");
    }

    @Test(groups = "integration")
    public void shouldAssertNonValidationXmlSourceWithSchemaReferenceInTestDefinition() {
        runTestAndAssertForSuccess("DoesNotValidateCanHaveASchemaReference");
    }

    @Test(groups = "integration")
    public void shouldAllowXmlCodeDirectlyInSourceElement() {
        net.sourceforge.testxng.domain.Test test = testMap.get(
                "XmlCodeDirectlyInSourceElement");
        assertEquals(test.getSource(), FOO_BAR_LOREM_IPSUM_XML_CODE);
    }

    @Test(groups = "integration", dependsOnMethods =  {
        "shouldAllowXmlCodeDirectlyInSourceElement"}
    )
    public void shouldValidateXmlSourceDirectlyInSourceElement() {
        runTestAndAssertForSuccess("XmlCodeDirectlyInSourceElement");
    }

    @Test(groups = "integration")
    public void shouldAllowXmlCodeDirectlyInExpectedResultElement() {
        net.sourceforge.testxng.domain.Test test = testMap.get(TRANSFORMS_CAN_HAVE_EXPECTED_RESULT_WITH_XML_CODE_TEST_ID);
        TransformsAssertion transforms = (TransformsAssertion) test.getAssertions()
                                                                   .get(0);
        assertEquals(transforms.getExpectedResult(),
            FOO_BAR_WITH_LOREM_IPSUM_XML_CODE);
    }

    @Test(groups = "integration")
    public void shouldAllowTransformationReferenceInTransforms() {
        net.sourceforge.testxng.domain.Test test = testMap.get(TRANSFORMS_CAN_HAVE_EXPECTED_RESULT_WITH_XML_CODE_TEST_ID);
        TransformsAssertion transforms = (TransformsAssertion) test.getAssertions()
                                                                   .get(0);
        assertEquals(transforms.getTransformationFilename(), "FooBar.xslt");
    }

    @Test(groups = "integration", dependsOnMethods =  {
        "shouldAllowXmlCodeDirectlyInExpectedResultElement", "shouldAllowTransformationReferenceInTransforms"}
    )
    public void shouldTransformXmlToXmlCodeInExpectedResultElement() {
        runTestAndAssertForSuccess(TRANSFORMS_CAN_HAVE_EXPECTED_RESULT_WITH_XML_CODE_TEST_ID);
    }

    @Test(groups = "integration")
    public void shouldAllowExpectedResultReferenceInTransforms() {
        net.sourceforge.testxng.domain.Test test = testMap.get(TRANSFORMS_CAN_HAVE_EXPECTED_RESULT_FILENAME_TEST_ID);
        TransformsAssertion transforms = (TransformsAssertion) test.getAssertions()
                                                                   .get(0);
        assertEquals(transforms.getExpectedResultFilename(),
            "FooBarTransformed.xml");
    }

    @Test(groups = "integration", dependsOnMethods =  {
        "shouldAllowExpectedResultReferenceInTransforms", "shouldAllowTransformationReferenceInTransforms"}
    )
    public void shouldTransformXmlToXmlCodeInExpectedResultFile() {
        runTestAndAssertForSuccess(TRANSFORMS_CAN_HAVE_EXPECTED_RESULT_FILENAME_TEST_ID);
    }

    @Test(groups = "integration")
    public void shouldAllowModeInTransforms() {
        net.sourceforge.testxng.domain.Test test = testMap.get(TRANSFORMS_CAN_HAVE_MODE_TEST_ID);
        TransformsAssertion transforms = (TransformsAssertion) test.getAssertions()
                                                                   .get(0);
        assertEquals(transforms.getMode(), "qux");
    }

    @Test(groups = "integration", dependsOnMethods =  {
        "shouldAllowExpectedResultReferenceInTransforms"}
    )
    public void shouldTransformXmlToXmlCodeInExpectedResultElementUsingMode() {
        runTestAndAssertForSuccess(TRANSFORMS_CAN_HAVE_MODE_TEST_ID);
    }

    @Test(groups = "integration")
    public void transformationShouldIncludeAttributes() {
        runTestAndAssertForSuccess(RESULT_ELEMENTS_CAN_HAVE_ATTRIBUTES_ID);
    }

    @Test(groups = "integration")
    public void transformationAtTheRootCanBeDifferent() {
        runTestAndAssertForSuccess(TRANSFORMS_AT_THE_ROOT_CAN_BE_DIFFERENT_ID);
    }

    // TODO
    // @Test(groups = { "integration" })
    public void shouldAllowATestElementInSourceWithoutShowingUpAsATest() {
        assertNull(testMap.get("TestWithSourceWithTest"));
    }
}
