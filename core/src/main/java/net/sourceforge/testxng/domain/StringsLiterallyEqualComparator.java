package net.sourceforge.testxng.domain;


/**
 * Comparator that checks that both objects are equal as Strings.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: StringsLiterallyEqualComparator.java 50 2010-04-02 20:36:53Z filipvanlaenen $
 *
 */
public class StringsLiterallyEqualComparator
    implements TransformationResultComparator<String> {
    public boolean equivalent(String actual, String expected) {
        return actual.equals(expected);
    }
}
