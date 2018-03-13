package org.redhat.vrp.domain.location;

/**
 * The cost between two locations is a straight line: the Euclidean distance between their GPS coordinates.
 */
public class AirLocation extends Location {

	public AirLocation() {
	}

	public AirLocation(String name, double latitude, double longitude) {
		super(name, latitude, longitude);
	}

	@Override
	public long getDistanceTo(Location location) {
		double distance = getAirDistanceDoubleTo(location);
		return (long) (distance + 0.5);
	}

}
