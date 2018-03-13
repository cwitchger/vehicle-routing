package org.redhat.vrp.domain.location;

import org.redhat.vrp.domain.LocationTimeUtility;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({ @JsonSubTypes.Type(value = AirLocation.class, name = "AirLocation") })
public abstract class Location {

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public abstract long getDistanceTo(Location location);

	public double getAirDistanceDoubleTo(Location location) {
		return distanceInMilliseconds(location.latitude, location.longitude, latitude, longitude);
	}

	public double getAngle(Location location) {
		double latitudeDifference = location.latitude - latitude;
		double longitudeDifference = location.longitude - longitude;
		return Math.atan2(latitudeDifference, longitudeDifference);
	}

	@Override
	public String toString() {
		if (name == null) {
			return super.toString();
		}
		return name;
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

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

}