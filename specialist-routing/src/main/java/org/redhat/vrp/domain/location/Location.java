package org.redhat.vrp.domain.location;

import org.redhat.vrp.domain.LocationTimeUtility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = AirLocation.class, name = "AirLocation"),
		@JsonSubTypes.Type(value = RoadLocation.class, name = "RoadLocation") })
public abstract class Location {

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double distanceInMilliseconds(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = (dist * 60 * 1.1515 * LocationTimeUtility.FEET_IN_MILE) / LocationTimeUtility.FEET_PER_MILLISECOND;

		return (dist);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	protected String name = null;

	protected double latitude;

	protected double longitude;

	public Location() {
	}

	public Location(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getAirDistanceDoubleTo(Location location) {
		return distanceInMilliseconds(location.latitude, location.longitude, this.latitude, this.longitude);
	}

	public double getAngle(Location location) {
		double latitudeDifference = location.latitude - this.latitude;
		double longitudeDifference = location.longitude - this.longitude;
		return Math.atan2(latitudeDifference, longitudeDifference);
	}

	public abstract long getDistanceTo(Location location);

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public String getName() {
		return this.name;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		if (this.name == null) {
			return super.toString();
		}
		return this.name;
	}

}
