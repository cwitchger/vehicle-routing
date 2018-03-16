package org.redhat.vrp.domain;

import java.util.Set;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;
import org.redhat.vrp.domain.location.Location;
import org.redhat.vrp.domain.solver.ArrivalTimeUpdatingVariableListener;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@PlanningEntity
@XStreamAlias("Job")
public class Job implements JobOrSpecialist {

	protected String jobId;

	protected Specialist specialist;

	protected Location location;

	private long windowStart;

	private long windowEnd;

	private long serviceDuration;

	private Set<Skill> requiredSkills;

	// Shadow variable
	private Long arrivalTime;

	// Planning variables: changes during planning, between score calculations.
	protected JobOrSpecialist previousJobOrSpecialist;

	// Shadow variables
	protected Job nextJob;

	@CustomShadowVariable(variableListenerClass = ArrivalTimeUpdatingVariableListener.class,
			sources = { @PlanningVariableReference(variableName = "previousJobOrSpecialist") })
	public Long getArrivalTime() {
		return this.arrivalTime;
	}

	/**
	 * This method adds a hard constraint saying that a specialist cannot start
	 * working till the start time.
	 */
	public Long getDepartureTime() {
		if (this.arrivalTime == null) {
			return null;
		}
		return Math.max(this.arrivalTime, this.windowStart) + this.serviceDuration;
	}

	/**
	 * The jobOrSpecialist parameter can not be null
	 */
	public long getDistanceFrom(JobOrSpecialist jobOrSpecialist) {
		return jobOrSpecialist.getLocation().getDistanceTo(this.location);
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public long getDistanceFromPreviousJobOrSpecialist() {
		if (this.previousJobOrSpecialist == null) {
			throw new IllegalStateException("This method must not be called when the previousJobOrSpecialist ("
					+ this.previousJobOrSpecialist + ") is not initialized yet.");
		}
		return getDistanceFrom(this.previousJobOrSpecialist);
	}

	/**
	 * The jobOrSpecialist parameter can not be null
	 */
	public long getDistanceTo(JobOrSpecialist jobOrSpecialist) {
		return this.location.getDistanceTo(jobOrSpecialist.getLocation());
	}

	public String getJobId() {
		return this.jobId;
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public Job getNextJob() {
		return this.nextJob;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	@PlanningVariable(valueRangeProviderRefs = { "specialistRange", "jobRange" },
			graphType = PlanningVariableGraphType.CHAINED)
	public JobOrSpecialist getPreviousJobOrSpecialist() {
		return this.previousJobOrSpecialist;
	}

	public Set<Skill> getRequiredSkills() {
		return this.requiredSkills;
	}

	public long getServiceDuration() {
		return this.serviceDuration;
	}

	@Override
	@AnchorShadowVariable(sourceVariableName = "previousJobOrSpecialist")
	public Specialist getSpecialist() {
		return this.specialist;
	}

	/**
	 * Calculates the difference in time if the arrival time is earlier than
	 * desired or later than desired
	 */
	public long getTimeOutsideOfWindow() {
		long timeOutsideOfWindow = 0L;

		if (isArrivalBeforeWindowStart()) {
			timeOutsideOfWindow = this.arrivalTime - this.windowStart;
		} else if (isArrivalAfterWindowEnd()) {
			timeOutsideOfWindow = this.windowEnd - this.arrivalTime;
		}

		return timeOutsideOfWindow;
	}

	/**
	 * There could be an added weight to the distance
	 */
	public long getTimeWindowGapTo(Job other) {
		long latestDepartureTime = this.arrivalTime + this.serviceDuration;
		long otherLatestDepartureTime = other.getArrivalTime() + other.getServiceDuration();

		if (latestDepartureTime < other.getWindowStart()) {
			return other.getWindowStart() - latestDepartureTime;
		}

		if (otherLatestDepartureTime < this.windowStart) {
			return this.windowStart - otherLatestDepartureTime;
		}

		return 0L;
	}

	public long getWindowEnd() {
		return this.windowEnd;
	}

	public long getWindowStart() {
		return this.windowStart;
	}

	public boolean isArrivalAfterWindowEnd() {
		return this.arrivalTime != null && this.windowEnd != 0 && this.windowEnd < this.arrivalTime;
	}

	public boolean isArrivalBeforeWindowStart() {
		return this.arrivalTime != null && this.windowEnd != 0 && this.arrivalTime < this.windowStart;
	}

	public void setArrivalTime(Long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public void setNextJob(Job nextJob) {
		this.nextJob = nextJob;
	}

	public void setPreviousJobOrSpecialist(JobOrSpecialist previousJobOrSpecialist) {
		this.previousJobOrSpecialist = previousJobOrSpecialist;
	}

	public void setRequiredSkills(Set<Skill> requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	public void setServiceDuration(long serviceDuration) {
		this.serviceDuration = serviceDuration;
	}

	public void setSpecialist(Specialist specialist) {
		this.specialist = specialist;
	}

	public void setWindowEnd(long windowEnd) {
		this.windowEnd = windowEnd;
	}

	public void setWindowStart(long windowStart) {
		this.windowStart = windowStart;
	}

	@Override
	public String toString() {
		if (this.location.getName() == null) {
			return super.toString();
		}
		return this.location.getName();
	}

}
