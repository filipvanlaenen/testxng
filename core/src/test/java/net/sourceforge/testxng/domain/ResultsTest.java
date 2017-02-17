package net.sourceforge.testxng.domain;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * Unit tests on the <code>Results</code> class.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: ResultsTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class ResultsTest {
    Results defaultResults;

    @BeforeTest(groups =  {
        "unit"}
    )
    void createDefaultResults() {
        defaultResults = new Results();
    }

    @Test(groups =  {
        "unit"}
    )
    public void defaultShouldBe0TestsRun0Failures0Errors0Skipped() {
        AssertResults.assertResultsAreEqual(defaultResults, 0, 0, 0, 0);
    }

    @Test(groups =  {
        "unit"}
    )
    public void defaultMessage() {
        assertEquals(defaultResults.getSummary(),
            "Tests run: 0, Failures: 0, Errors: 0, Skipped: 0");
    }

    @Test(groups =  {
        "unit"}
    , expectedExceptions =  {
        IllegalArgumentException.class}
    )
    public void shouldThrowIllegalArgumentExceptionIfATestNotRunIsAddded() {
        defaultResults.addTest(new net.sourceforge.testxng.domain.Test());
    }

    @Test(groups =  {
        "unit"}
    )
    public void defaultFailuresSummaryShouldBeEmpty() {
        assertEquals(defaultResults.getFailuresSummary(), "");
    }
}
