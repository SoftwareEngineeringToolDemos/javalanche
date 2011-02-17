package de.unisb.cs.st.javalanche.mutation.runtime.testDriver.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;

import de.unisb.cs.st.javalanche.mutation.properties.MutationProperties;
import de.unisb.cs.st.javalanche.mutation.runtime.testDriver.junit.data.Junit3SuiteMultipleTestsSameName;

public class TestWithSameNameTest {

	@Test
	public void testSameTestNameJunit4Util() throws Exception {
		String back = MutationProperties.TEST_SUITE;
		MutationProperties.TEST_SUITE = Junit3SuiteMultipleTestsSameName.class
				.getCanonicalName();
		Runner runner = Junit4Util.getRunner();
		int testCount = runner.getDescription().testCount();
		assertEquals(5, testCount);
		List<String> testNames = getTestNames(runner.getDescription());
		assertEquals(5, testNames.size());
		Set<String> testNameSet = new HashSet<String>(testNames);
		assertEquals(1, testNameSet.size());
		MutationProperties.TEST_SUITE = back;
	}

	@Test
	public void testSameTestNameJunit4MutationTestDriver() throws Exception {
		String back = MutationProperties.TEST_SUITE;
		MutationProperties.TEST_SUITE = Junit3SuiteMultipleTestsSameName.class
				.getCanonicalName();
		Junit4MutationTestDriver driver = new Junit4MutationTestDriver();
		List<String> testNames = driver.getAllTests();
		assertEquals(5, testNames.size());
		Set<String> testNameSet = new HashSet<String>(testNames);
		assertEquals(5, testNameSet.size());
		MutationProperties.TEST_SUITE = back;
	}

	private List<String> getTestNames(Description d) {
		List<String> testNames = new ArrayList<String>();
		ArrayList<Description> children = d.getChildren();
		if (children != null && children.size() > 0) {
			for (Description description : children) {
				testNames.addAll(getTestNames(description));
			}
		} else {
			testNames.add(d.getDisplayName());
		}
		return testNames;
	}

}
