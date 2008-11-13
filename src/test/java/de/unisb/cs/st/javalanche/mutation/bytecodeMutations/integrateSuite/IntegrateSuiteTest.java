package de.unisb.cs.st.javalanche.mutation.bytecodeMutations.integrateSuite;

import static org.junit.Assert.*;

import java.util.Map;

import junit.framework.TestSuite;

import org.junit.Test;

import de.unisb.cs.st.javalanche.mutation.bytecodeMutations.integrateSuite.testClasses.AllTests;
import de.unisb.cs.st.javalanche.mutation.runtime.testsuites.MutationTestSuite;
import de.unisb.cs.st.javalanche.mutation.runtime.testsuites.TestSuiteUtil;

public class IntegrateSuiteTest {

	@Test
	public void runTests() {
		TestSuite suite = AllTests.suite();
		Map<String, junit.framework.Test> map = TestSuiteUtil.getAllTests(suite);
//		suite.run(new TestResult());
		TestSuite selectiveTestSuite = MutationTestSuite.toMutationTestSuite(suite);
		assertTrue(selectiveTestSuite!=null);
//		assertTrue(selectiveTestSuite instanceof SelectiveTestSuite);
		assertTrue(map.size() >= suite.testCount());
	}

}
