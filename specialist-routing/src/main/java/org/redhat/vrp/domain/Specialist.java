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

	public Set<Skill> getSkills() {
		return skills;
	}

	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}

	public String getSpecialistId() {
		return specialistId;
	}

	public void setSpecialistId(String specialistId) {
		this.specialistId = specialistId;
	}

	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}

	public Job getNextJob() {
		return nextJob;
	}

	public void setNextJob(Job nextJob) {
		this.nextJob = nextJob;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public Specialist getSpecialist() {
		return this;
	}

	public Location getLocation() {
		return home.getLocation();
	}

	/**
	 * The standstill parameter can not be null
	 */
	public long getDistanceTo(JobOrSpecialist standstill) {
		return home.getDistanceTo(standstill);
	}

	public long getTotalWorkingHours() {
		long totalTime = 0;
		Job last = nextJob;
		if (nextJob != null) {
			while (true) {
				if (last.getNextJob() != null) {
					last = last.getNextJob();
				} else {
					totalTime = last.getDepartureTime() - nextJob.getArrivalTime();
					break;
				}
			}
		}

		return LocationTimeUtility.millisecondsToHours(totalTime);
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public Long getTotalWorkingMilliseconds() {
		long totalTime = 0;
		Job last = nextJob;
		while (true) {
			if (last.getNextJob() != null) {
				last = last.getNextJob();
			} else {
				totalTime = last.getDepartureTime() - nextJob.getArrivalTime();
				break;
			}
		}
		return totalTime;
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
