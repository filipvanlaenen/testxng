package net.sourceforge.testxng.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * Class representing the results of a test run.
 *
 * @author Filip van Laenen &lt;f.a.vanlaenen@ieee.org&gt;
 * @version $Id: Results.java 88 2014-02-20 12:44:50Z filipvanlaenen $
 *
 */
public class Results {
    private final List<Test> tests = new ArrayList<Test>();

    public int getTestsRun() {
        return tests.size();
    }

    private int countTestsInGivenState(Test.State state) {
        int matches = 0;

        for (Test test : tests) {
            if (test.getState().equals(state)) {
                matches++;
            }
        }

        return matches;
    }

    public int getFailures() {
        return countTestsInGivenState(Test.State.FAILURE);
    }

    int getSkipped() {
        return countTestsInGivenState(Test.State.SKIPPED);
    }

    public int getErrors() {
        return countTestsInGivenState(Test.State.ERROR);
    }

    public String getSummary() {
        return "Tests run: " + getTestsRun() + ", Failures: " + getFailures() +
        ", Errors: " + getErrors() + ", Skipped: " + getSkipped();
    }

    /**
     * Adds a test to the Results. The test should be run; if it isn't, an
     * IllegalArgumentException will be thrown.
     *
     * @param test
     *            The test
     * @throws IllegalArgumentException
     *             if the test isn't run yet
     */
    void addTest(Test test) {
        if (test.getState().equals(Test.State.NOT_RUN)) {
            throw new IllegalArgumentException(
                "Can not add a Test to Results if it is not yet run");
        }

        tests.add(test);
    }

    /**
     * Returns a summary with all failed tests.
     *
     * @return A String with a summary of all failed tests.
     */
    public String getFailuresSummary() {
        StringBuilder sb = new StringBuilder();

        for (Test test : tests) {
            if (test.getState().equals(Test.State.FAILURE)) {
                sb.append("  ");
                sb.append(test.getId());
                sb.append('(').append(test.getFilename()).append(")\n");
            }
        }

        return sb.toString();
    }
}
