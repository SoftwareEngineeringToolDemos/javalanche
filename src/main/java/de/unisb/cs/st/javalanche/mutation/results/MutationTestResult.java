package de.unisb.cs.st.javalanche.mutation.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

@Entity
public class MutationTestResult implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final MutationTestResult NO_RESULT = new MutationTestResult();

	@Id
	@GeneratedValue
	private Long id;

	private int runs;

	/**
	 * True if the mutation was touched by at least one TestCase;
	 */
	boolean touched;

	@OneToMany(cascade = CascadeType.ALL)
	// , fetch = FetchType.EAGER)
	@OrderBy("testCaseName")
	@IndexColumn(name = "failure_list_id")
	private List<TestMessage> failures = new ArrayList<TestMessage>();

	@OneToMany(cascade = CascadeType.ALL)
	// , fetch = FetchType.EAGER)
	@JoinTable(name = "MutationTestResult_Errors", joinColumns = { @JoinColumn(name = "mutationTestResult_id") }, inverseJoinColumns = @JoinColumn(name = "testMessage_id"))
	@IndexColumn(name = "error_id")
	private List<TestMessage> errors = new ArrayList<TestMessage>();

	@OneToMany(cascade = CascadeType.ALL)
	// , fetch = FetchType.EAGER)
	@JoinTable(name = "MutationTestResult_Passing", joinColumns = { @JoinColumn(name = "mutationTestResult_id") }, inverseJoinColumns = @JoinColumn(name = "testMessage_id"))
	@IndexColumn(name = "passing_id")
	private List<TestMessage> passing = new ArrayList<TestMessage>();

	// @OneToMany(cascade = CascadeType.ALL)
	// // // , fetch = FetchType.EAGER)
	// @JoinTable(name = "MutationTestResult_ViolatedInvariants", joinColumns =
	// { @JoinColumn(name = "mutationTestResult_id") }, inverseJoinColumns =
	// @JoinColumn(name = "invariant_id"))
	// @IndexColumn(name = "mapping_id")
	@Transient
	private List<Invariant> invariants = new ArrayList<Invariant>();

	// Temporal(TemporalType.TIME)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@CollectionOfElements
	@JoinTable(name = "ViolatedInvariants", joinColumns = { @JoinColumn(name = "result_id") })
	@Column(name = "violatedInvariant", nullable = true)
	@IndexColumn(name = "violated_index")
	private int[] violatedInvariants;

	private int differentViolatedInvariants;

	private int totalViolations;

	 @OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "MutationTestResult_AddResults")
	private List<AddResult> addResults = new ArrayList<AddResult>();

	/**
	 * @return the violatedInvariants
	 */
	public int[] getViolatedInvariants() {
		return violatedInvariants;
	}

	/**
	 * @return the totalViolations
	 */
	public int getTotalViolations() {
		return totalViolations;
	}

	/**
	 * @param violatedInvariants
	 *            the violatedInvariants to set
	 */
	public void setViolatedInvariants(int[] violatedInvariants) {
		// int truncationSize = 100;
		// if (violatedInvariants.length > truncationSize) {
		// System.out
		// .println("MutationTestResult.setViolatedInvariants(): truncating
		// violated invariants");
		// this.violatedInvariants = new int[truncationSize];
		// System.arraycopy(violatedInvariants, 0, this.violatedInvariants, 0,
		// truncationSize);
		// } else {
		this.violatedInvariants = violatedInvariants;
		// }
	}

	/**
	 * @param totalViolations
	 *            the totalViolations to set
	 */
	public void setTotalViolations(int totalViolations) {
		this.totalViolations = totalViolations;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	@SuppressWarnings("unused")
	public MutationTestResult() {
		this.date = new Date();
	}


	public MutationTestResult(List<TestMessage> passing,
			List<TestMessage> failures, List<TestMessage> errors,
			boolean touched) {
		this.passing = passing;
		this.failures = failures;
		this.errors = errors;
		this.touched = touched;
		this.runs = passing.size() + failures.size() + errors.size();
	}


	public String toShortString() {
		StringBuilder sb = new StringBuilder(String.format(
				"Runs: %d  Failures: %d  Errors: %d LineTouched: %s", runs,
				failures.size(), errors.size(), touched ? "yes" : "no"));
		sb.append(" invariant violations: " + differentViolatedInvariants
				+ " (" + totalViolations + ") ids["
				+ Arrays.toString(violatedInvariants) + "]");
		sb.append(" date: " + date);
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(toShortString());
		sb.append('\n');
		if (failures.size() > 0) {
			sb.append("Failures:\n");
			for (TestMessage tm : failures) {
				sb.append(tm);
				sb.append('\n');
			}
		}
		if (errors.size() > 0) {
			sb.append("Errors:\n");
			for (TestMessage tm : errors) {
				sb.append(tm);
				sb.append('\n');
			}
		}
		if (passing.size() > 0) {
			sb.append("Passing:\n");
			for (TestMessage tm : passing) {
				sb.append(tm);
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	/**
	 * @return the runs
	 */
	public int getRuns() {
		return runs;
	}

	/**
	 * @param runs
	 *            The runs to set
	 */
	public void setRuns(int runs) {
		this.runs = runs;
	}

	/**
	 * @return the errors
	 */

	public int getNumberOfErrors() {
		return errors.size();
	}

	/**
	 * @return the failures
	 */
	public int getNumberOfFailures() {
		return failures.size();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the errors
	 */
	public Collection<TestMessage> getErrors() {
		return errors;
	}

	/**
	 * @return the failures
	 */
	public Collection<TestMessage> getFailures() {
		return failures;
	}

	/**
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(List<TestMessage> errors) {
		this.errors = errors;
	}

	/**
	 * @param failures
	 *            the failures to set
	 */
	public void setFailures(List<TestMessage> failures) {
		this.failures = failures;
	}

	/**
	 * @return the passing
	 */
	public List<TestMessage> getPassing() {
		return passing;
	}

	/**
	 * @param passing
	 *            the passing to set
	 */
	public void setPassing(List<TestMessage> passing) {
		this.passing = passing;
	}

	/**
	 * @return the touched
	 */
	public boolean isTouched() {
		return touched;
	}

	/**
	 * @param touched
	 *            the touched to set
	 */
	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	/**
	 * @return the differentViolatedInvariants
	 */
	public int getDifferentViolatedInvariants() {
		return differentViolatedInvariants;
	}

	/**
	 * @param differentViolatedInvariants
	 *            the differentViolatedInvariants to set
	 */
	public void setDifferentViolatedInvariants(int differentViolatedInvariants) {
		this.differentViolatedInvariants = differentViolatedInvariants;
	}

	public void addInvariant(Invariant invariant) {
		if (invariants == null) {
			invariants = new ArrayList<Invariant>();
		}
		invariants.add(invariant);
	}

	/**
	 * @return the invariants
	 */
	public List<Invariant> getInvariants() {
		return invariants;
	}

	/**
	 * @param invariants
	 *            the invariants to set
	 */
	public void setInvariants(List<Invariant> invariants) {
		this.invariants = invariants;
	}

	public void addFailure(TestMessage tm) {
		failures.add(tm);
	}

	public Collection<TestMessage> getAllTestMessages() {
		List<TestMessage> allMessages = new ArrayList<TestMessage>(passing);
		allMessages.addAll(failures);
		allMessages.addAll(errors);
		return allMessages;
	}

	public void loadAll() {
		if (errors != null)
			errors.size();
		if (passing != null)
			passing.size();
		if (failures != null)
			failures.size();
		if (invariants != null)
			invariants.size();
		if (violatedInvariants != null) {
			@SuppressWarnings("unused")
			int length = violatedInvariants.length;
		}
	}

	/**
	 * @return the addResults
	 */
	public List<AddResult> getAddResults() {
		return addResults;
	}

	
	public void addResults(AddResult r) {
		addResults.add(r);
	}

	/**
	 * @param addResults
	 *            the addResults to set
	 */
	public void setAddResults(List<AddResult> addResults) {
		this.addResults = addResults;
	}

	public <T> T getAddResult(Class<T> clazz) {
		for (AddResult addResult : addResults) {
			if (clazz.isInstance(addResult)) {
				return (T) addResult;
			}
		}
		return null;
	}
}
