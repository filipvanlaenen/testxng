package net.sourceforge.testxng.domain;

import org.apache.log4j.Logger;

import org.w3c.dom.Element;


/**
 * Abstract superclass for all assertion types.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: Assertion.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public abstract class Assertion {
    protected Test test;

    void setTest(Test test) {
        this.test = test;
    }

    /**
     * Tries to check the assertion. If the assertion is true, the method should
     * return true. If the assertion is false, the method should return false.
     * If something goes wrong, an exception should be thrown.
     *
     * @return
     * @throws Exception
     */
    abstract boolean check(Logger logger) throws Exception;

    /**
     * Continues the initialization of the assertion with information from the
     * XML element.
     *
     * @param element
     *            The element from which further information can be extracted to
     *            continue the initialization of the assertion.
     */
    abstract void initializeFurtherFromElement(Element element);
}
