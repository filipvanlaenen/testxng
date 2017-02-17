package net.sourceforge.testxng.domain;


/**
 * Interface defining a class that can compare the result of a transformation to an expected result.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: TransformationResultComparator.java 50 2010-04-02 20:36:53Z filipvanlaenen $
 *
 */
public interface TransformationResultComparator<T> {
    boolean equivalent(T actual, T expected);
}
