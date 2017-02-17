package net.sourceforge.testxng.domain;

import static org.testng.Assert.assertSame;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;


/**
 * Unit tests on the <code>MalformedTestDefinitionFileException</code> class.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: MalformedTestDefinitionFileExceptionTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class MalformedTestDefinitionFileExceptionTest {
    private Throwable cause;
    private MalformedTestDefinitionFileException exception;
    private File file;

    @BeforeTest(groups = "unit")
    void beforeTest() {
        cause = new Exception();
        file = new File("");
        exception = new MalformedTestDefinitionFileException(cause, file);
    }

    @Test(groups = "unit")
    public void shouldReturnTheOriginalExceptionAsTheCause() {
        assertSame(exception.getCause(), cause);
    }

    @Test(groups = "unit")
    public void shouldReturnTheFileFromTheConstructor() {
        assertSame(exception.getFile(), file);
    }
}
