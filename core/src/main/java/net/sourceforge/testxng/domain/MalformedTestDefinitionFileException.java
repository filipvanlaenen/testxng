package net.sourceforge.testxng.domain;

import java.io.File;


/**
 * Exception thrown when a test definition file is malformed. The original
 * exception thrown by parsers, readers, etc... is wrapped as the cause of this
 * exception, together with a reference to the file that is malformed.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: MalformedTestDefinitionFileException.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class MalformedTestDefinitionFileException extends Exception {
    private final File file;

    MalformedTestDefinitionFileException(Throwable cause, File file) {
        super(cause);
        this.file = file;
    }

    /**
     * The test definition file that is malformed.
     *
     * @return The test definition file causing problems.
     */
    public File getFile() {
        return file;
    }
}
