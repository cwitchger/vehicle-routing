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

	public Set<Skill> getRequiredSkills() {
		return requiredSkills;
	}

	public void setRequiredSkills(Set<Skill> requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	// Shadow variable
	private Long arrivalTime;

	// Planning variables: changes during planning, between score calculations.
	protected JobOrSpecialist previousJobOrSpecialist;

	// Shadow variables
	protected Job nextJob;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@PlanningVariable(valueRangeProviderRefs = { "specialistRange",
			"jobRange" }, graphType = PlanningVariableGraphType.CHAINED)
	public JobOrSpecialist getPreviousJobOrSpecialist() {
		return previousJobOrSpecialist;
	}

	public void setPreviousJobOrSpecialist(JobOrSpecialist previousJobOrSpecialist) {
		this.previousJobOrSpecialist = previousJobOrSpecialist;
	}

	@AnchorShadowVariable(sourceVariableName = "previousJobOrSpecialist")
	public Specialist getSpecialist() {
		return specialist;
	}

	public void setSpecialist(Specialist specialist) {
		this.specialist = specialist;
	}

	@CustomShadowVariable(variableListenerClass = ArrivalTimeUpdatingVariableListener.class, sources = {
			@PlanningVariableReference(variableName = "previousJobOrSpecialist") })
	public Long getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Job getNextJob() {
		return nextJob;
	}

	public void setNextJob(Job nextJob) {
		this.nextJob = nextJob;
	}

	public long getWindowStart() {
		return windowStart;
	}

	public void setWindowStart(long windowStart) {
		this.windowStart = windowStart;
	}

	public long getWindowEnd() {
		return windowEnd;
	}

	public void setWindowEnd(long windowEnd) {
		this.windowEnd = windowEnd;
	}

	public long getServiceDuration() {
		return serviceDuration;
	}

	public void setServiceDuration(long serviceDuration) {
		this.serviceDuration = serviceDuration;
	}

	@Override
	public String toString() {
		if (location.getName() == null) {
			return super.toString();
		}
		return location.getName();
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public long getDistanceFromPreviousJobOrSpecialist() {
		if (previousJobOrSpecialist == null) {
			throw new IllegalStateException("This method must not be called when the previousJobOrSpecialist ("
					+ previousJobOrSpecialist + ") is not initialized yet.");
		}
		return getDistanceFrom(previousJobOrSpecialist);
	}

	/**
	 * The jobOrSpecialist parameter can not be null
	 */
	public long getDistanceFrom(JobOrSpecialist jobOrSpecialist) {
		return jobOrSpecialist.getLocation().getDistanceTo(location);
	}

	/**
	 * The jobOrSpecialist parameter can not be null
	 */
	public long getDistanceTo(JobOrSpecialist jobOrSpecialist) {
		return location.getDistanceTo(jobOrSpecialist.getLocation());
	}

	/**
	 * This method adds a hard constraint saying that a specialist cannot start working till the start time.
	 */
	public Long getDepartureTime() {
		if (arrivalTime == null) {
			return null;
		}
		return Math.max(arrivalTime, windowStart) + serviceDuration;
	}

	public boolean isArrivalBeforeWindowStart() {
		return arrivalTime != null && windowEnd != 0 && arrivalTime < windowStart;
	}

	public boolean isArrivalAfterWindowEnd() {
		return arrivalTime != null && windowEnd != 0 && windowEnd < arrivalTime;
	}

	/**
	 * Calculates the difference in time if the arrival time is earlier than desired or later than desired
	 */
	public long getTimeOutsideOfWindow() {
		long timeOutsideOfWindow = 0L;

		if (isArrivalBeforeWindowStart()) {
			timeOutsideOfWindow = arrivalTime - windowStart;
		} else if (isArrivalAfterWindowEnd()) {
			timeOutsideOfWindow = windowEnd - arrivalTime;
		}

		return timeOutsideOfWindow;
	}

	/**
	 * There could be an added weight to the distance
	 */
	public long getTimeWindowGapTo(Job other) {
		long latestDepartureTime = arrivalTime + serviceDuration;
		long otherLatestDepartureTime = other.getArrivalTime() + other.getServiceDuration();

		if (latestDepartureTime < other.getWindowStart()) {
			return other.getWindowStart() - latestDepartureTime;
		}

		if (otherLatestDepartureTime < windowStart) {
			return windowStart - otherLatestDepartureTime;
		}

		return 0L;
	}

}
