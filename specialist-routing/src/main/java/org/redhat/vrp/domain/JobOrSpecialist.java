package org.redhat.vrp.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import org.redhat.vrp.domain.location.Location;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@PlanningEntity
public interface JobOrSpecialist {

	/**
	 * Location should never be null
	 */
	public Location getLocation();

	/**
	 * Specialist can be null
	 */
	@JsonProperty(access = Access.WRITE_ONLY)
	public Specialist getSpecialist();

	/**
	 * Job can be null
	 */
	@InverseRelationShadowVariable(sourceVariableName = "previousJobOrSpecialist")
	public Job getNextJob();

	public void setNextJob(Job nextJob);

}
