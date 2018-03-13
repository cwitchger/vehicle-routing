package org.redhat.vrp.domain;

import org.redhat.vrp.domain.location.Location;

public class Home {

	protected Location location;

	private long windowStart;

	private long windowEnd;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public long getWindowStart() {
		return windowStart;
	}

	public void setWindowStart(long windowStart) {
		this.windowStart = windowStart;
	}

	public long getWindowEnd() {
		return windowEnd;
	}

	public void setWindowEnd(long windowEnd) {
		this.windowEnd = windowEnd;
	}

	@Override
	public String toString() {
		if (location.getName() == null) {
			return super.toString();
		}
		return location.getName();
	}

	/**
	 * The jobOrSpecialist parameter must never be null
	 */
	public long getDistanceTo(JobOrSpecialist jobOrSpecialist) {
		return location.getDistanceTo(jobOrSpecialist.getLocation());
	}

}
