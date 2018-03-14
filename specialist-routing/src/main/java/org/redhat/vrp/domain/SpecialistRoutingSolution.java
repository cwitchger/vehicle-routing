package org.redhat.vrp.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.persistence.xstream.api.score.buildin.hardsoftlong.HardSoftLongScoreXStreamConverter;
import org.redhat.vrp.domain.location.DistanceType;
import org.redhat.vrp.domain.location.Location;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@PlanningSolution
@XStreamAlias("RoutingSolution")
public class SpecialistRoutingSolution {

	protected DistanceType distanceType;

	protected String distanceUnitOfMeasurement;

	@ProblemFactCollectionProperty
	protected List<Location> locationList;

	@ProblemFactCollectionProperty
	protected List<Home> homeList;

	@ProblemFactCollectionProperty
	protected List<Proficiency> proficiencies;

	@PlanningEntityCollectionProperty
	@ValueRangeProvider(id = "specialistRange")
	protected List<Specialist> specialistList;

	@PlanningEntityCollectionProperty
	@ValueRangeProvider(id = "jobRange")
	protected List<Job> jobList;

	@PlanningScore
	@XStreamConverter(HardSoftLongScoreXStreamConverter.class)
	protected HardSoftLongScore score;

	public DistanceType getDistanceType() {
		return distanceType;
	}

	public void setDistanceType(DistanceType distanceType) {
		this.distanceType = distanceType;
	}

	public String getDistanceUnitOfMeasurement() {
		return distanceUnitOfMeasurement;
	}

	public void setDistanceUnitOfMeasurement(String distanceUnitOfMeasurement) {
		this.distanceUnitOfMeasurement = distanceUnitOfMeasurement;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public List<Home> getHomeList() {
		return homeList;
	}

	public void setHomeList(List<Home> homeList) {
		this.homeList = homeList;
	}

	public List<Specialist> getSpecialistList() {
		return specialistList;
	}

	public void setSpecialistList(List<Specialist> specialistList) {
		this.specialistList = specialistList;

		updateHomeList();
	}

	public List<Job> getJobList() {
		return jobList;
	}

	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;

		updateProficiencies();
		updateLocationList();
	}

	public HardSoftLongScore getScore() {
		return score;
	}

	public void setScore(HardSoftLongScore score) {
		this.score = score;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public List<Proficiency> getProficiencies() {
		return proficiencies;
	}

	public void setProficiencies(List<Proficiency> proficiencies) {
		this.proficiencies = proficiencies;
	}

	/**
	 * Gathers the specialists home locations from the specialist list and adds
	 * to the solution
	 */
	private void updateHomeList() {
		homeList = specialistList.stream().map(Specialist::getHome).collect(Collectors.toList());
	}

	/**
	 * Gathers the job locations from the job list and adds to the solution
	 */
	private void updateLocationList() {
		locationList = jobList.stream().map(Job::getLocation).collect(Collectors.toList());
	}

	/**
	 * Creates proficiencies from the job list and adds them to the solution
	 */
	private void updateProficiencies() {
		if (this.proficiencies == null || this.proficiencies.size() < 1) {
			this.proficiencies = new ArrayList<Proficiency>();

			for (Job job : this.jobList) {
				for (Skill skill : job.getRequiredSkills()) {
					proficiencies.add(new Proficiency(job, skill));
				}
			}
		}
	}

}
