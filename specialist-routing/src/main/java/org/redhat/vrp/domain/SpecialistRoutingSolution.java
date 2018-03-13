package org.redhat.vrp.domain;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.persistence.xstream.api.score.buildin.hardsoftlong.HardSoftLongScoreXStreamConverter;
import org.redhat.vrp.domain.location.DistanceType;
import org.redhat.vrp.domain.location.Location;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@PlanningSolution
@XStreamAlias("RoutingSolution")
public class SpecialistRoutingSolution {

	protected String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

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
	}

	public List<Job> getJobList() {
		return jobList;
	}

	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;
	}

	public HardSoftLongScore getScore() {
		return score;
	}

	public void setScore(HardSoftLongScore score) {
		this.score = score;
	}

	public List<Job> getAllJobs() {
		return jobList;
	}

	public List<Proficiency> getProficiencies() {
		return proficiencies;
	}

	public void setProficiencies(List<Proficiency> proficiencies) {
		this.proficiencies = proficiencies;
	}

}
