package net.sourceforge.testxng.domain;

import org.testng.annotations.Test;

import java.io.IOException;


/**
 * Unit tests on the <code>Engine</code> class.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: EngineTest.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class EngineTest {
    @Test(groups =  {
        "unit"}
    , expectedExceptions = IllegalStateException.class)
    public void getResultsShouldThrowIllegalStateExceptionWhenCalledBeforeRun() {
        Engine engine = new Engine(".");
        engine.getResults();
    }

    @Test(groups =  {
        "integration"}
    , expectedExceptions = IOException.class)
    public void shouldThrowIOExceptionWhenDirDoesNotExist()
        throws IOException {
        Engine engine = new Engine("dir-that-does-not-exist");
        engine.getTestFilesFromDir();
    }
}
