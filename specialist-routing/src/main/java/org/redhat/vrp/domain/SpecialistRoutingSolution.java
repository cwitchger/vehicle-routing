package org.redhat.vrp.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.redhat.vrp.domain.location.RoadLocation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@PlanningSolution
@XStreamAlias("RoutingSolution")
public class SpecialistRoutingSolution {

	protected DistanceType distanceType;

	protected String distanceUnitOfMeasurement;

	@JsonProperty(access = Access.WRITE_ONLY)
	protected Map<String, Map<String, Long>> distanceMatrix;

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
		addDistancesToLocations();
	}

	public List<Job> getJobList() {
		return jobList;
	}

	public void setJobList(List<Job> jobList) {
		this.jobList = jobList;

		addProblemFactsForJobs();
		addDistancesToLocations();
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

	public void setDistanceMatrix(Map<String, Map<String, Long>> distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
		addDistancesToLocations();
	}

	/**
	 * Gathers the specialists home locations from the specialist list and adds
	 * to the solution
	 */
	private void updateHomeList() {
		this.homeList = this.specialistList.stream().map(Specialist::getHome).collect(Collectors.toList());
	}

	/**
	 * Gathers the job locations from the job list and adds to the solution
	 */
	private void addToLocationList(Location location) {
		if (this.locationList == null) {
			this.locationList = new ArrayList<Location>();
		}

		this.locationList.add(location);
	}

	/**
	 * Creates proficiencies from the job list and adds them to the solution
	 */
	private void addProblemFactsForJobs() {
		this.proficiencies = new ArrayList<Proficiency>();

		for (Job job : this.jobList) {
			addProficienciesAndLocation(job);
		}
	}

	public void addProficienciesAndLocation(Job job) {
		for (Skill skill : job.getRequiredSkills()) {
			this.proficiencies.add(new Proficiency(job, skill));
		}

		addToLocationList(job.getLocation());
	}

	private void addDistancesToLocations() {
		if (locationList != null && homeList != null && distanceMatrix != null) {
			
			List<Location> locations = getAllLocations();
			for (Location from : locations) {
				Map<String, Long> distanceRow = distanceMatrix.get(from.getName());
				Map<RoadLocation, Long> distanceMap = new HashMap<RoadLocation, Long>();
				RoadLocation fromRoad = (RoadLocation) from;

				for (Location to : locations) {
					RoadLocation toRoad = (RoadLocation) to;

					long time = distanceRow.get(to.getName());
					distanceMap.put(toRoad, time);
				}

				fromRoad.setTravelDistanceMap(distanceMap);
			}
		}
	}

	private List<Location> getAllLocations() {
		List<Location> locations = this.homeList.stream().map(Home::getLocation).collect(Collectors.toList());
		locations.addAll(this.locationList);
		return locations;
	}
}
