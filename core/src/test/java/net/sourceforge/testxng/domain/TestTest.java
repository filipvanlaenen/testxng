package net.sourceforge.testxng.domain;

import static net.sourceforge.testxng.domain.Test.State.NOT_RUN;

import org.apache.log4j.Logger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Unit tests on the <code>Test</code> class.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: TestTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class TestTest {
    private net.sourceforge.testxng.domain.Test test;

    @BeforeMethod(groups =  {
        "unit"}
    )
    void beforeMethod() {
        test = new net.sourceforge.testxng.domain.Test();
    }

    @Test(groups =  {
        "unit"}
    )
    public void initialStateShouldBeNotRun() {
        assertEquals(test.getState(), NOT_RUN);
    }

    @Test(groups =  {
        "unit"}
    )
    public void stateShouldNotBeNotRunAfterRunning() {
        test.run(Logger.getRootLogger());
        assertFalse(test.getState().equals(NOT_RUN));
    }
}
