package org.redhat.vrp.domain.location;

import java.util.Map;

import org.redhat.vrp.domain.LocationTimeUtility;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RoadLocation extends Location {

	// Prefer Map over array or List because customers might be added and
	// removed in real-time planning.
	@JsonIgnore
	protected Map<RoadLocation, Long> travelDistanceMap;

	public RoadLocation() {
	}

	public RoadLocation(String name, double latitude, double longitude) {
		super(name, latitude, longitude);
	}

	@Override
	public long getDistanceTo(Location location) {
		if (this == location) {
			return 0L;
		}

		RoadLocation roadLocation = (RoadLocation) location;
		long time = this.travelDistanceMap.get(roadLocation);

		return LocationTimeUtility.secondsToMillisecond(time);
	}

	public void setTravelDistanceMap(Map<RoadLocation, Long> travelDistanceMap) {
		this.travelDistanceMap = travelDistanceMap;
	}
}
