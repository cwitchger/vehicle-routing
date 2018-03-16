package org.redhat.vrp.domain;

import java.util.Set;

import org.redhat.vrp.domain.location.Location;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Specialist")
public class Specialist implements JobOrSpecialist {

	protected Home home;

	protected String specialistId;

	protected Job nextJob;

	private Set<Skill> skills;

	/**
	 * The jobOrSpecialist parameter can not be null
	 */
	public long getDistanceTo(JobOrSpecialist jobOrSpecialist) {
		return this.home.getDistanceTo(jobOrSpecialist);
	}

	public Home getHome() {
		return this.home;
	}

	@Override
	public Location getLocation() {
		return this.home.getLocation();
	}

	@Override
	public Job getNextJob() {
		return this.nextJob;
	}

	public Set<Skill> getSkills() {
		return this.skills;
	}

	@Override
	@JsonProperty(access = Access.WRITE_ONLY)
	public Specialist getSpecialist() {
		return this;
	}

	public String getSpecialistId() {
		return this.specialistId;
	}

	public long getTotalWorkingHours() {
		long totalTime = 0;
		Job last = this.nextJob;
		if (this.nextJob != null) {
			while (true) {
				if (last.getNextJob() != null) {
					last = last.getNextJob();
				} else {
					totalTime = last.getDepartureTime() - this.nextJob.getArrivalTime();
					break;
				}
			}
		}

		return LocationTimeUtility.millisecondsToHours(totalTime);
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public Long getTotalWorkingMilliseconds() {
		long totalTime = 0;
		Job last = this.nextJob;
		while (true) {
			if (last.getNextJob() != null) {
				last = last.getNextJob();
			} else {
				totalTime = last.getDepartureTime() - this.nextJob.getArrivalTime();
				break;
			}
		}
		return totalTime;
	}

	public void setHome(Home home) {
		this.home = home;
	}

	@Override
	public void setNextJob(Job nextJob) {
		this.nextJob = nextJob;
	}

	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}

	public void setSpecialistId(String specialistId) {
		this.specialistId = specialistId;
	}

	@Override
	public String toString() {
		Location location = getLocation();
		if (location.getName() == null) {
			return super.toString();
		}
		return location.getName() + "/" + super.toString();
	}

}
