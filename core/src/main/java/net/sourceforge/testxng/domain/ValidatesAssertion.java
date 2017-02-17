package net.sourceforge.testxng.domain;


/**
 * Concrete subclass of ValidationAssertion that tests for validation.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: ValidatesAssertion.java 51 2010-04-02 20:49:07Z filipvanlaenen $
 *
 */
public class ValidatesAssertion extends ValidationAssertion {
    @Override
    boolean isPositiveAssertion() {
        return true;
    }
}
