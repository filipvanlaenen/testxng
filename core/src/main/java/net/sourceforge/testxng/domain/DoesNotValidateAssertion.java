package net.sourceforge.testxng.domain;


/**
 * Concrete subclass of ValidationAssertion that tests for non-validation.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: DoesNotValidateAssertion.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class DoesNotValidateAssertion extends ValidationAssertion {
    @Override
    boolean isPositiveAssertion() {
        return false;
    }
}
