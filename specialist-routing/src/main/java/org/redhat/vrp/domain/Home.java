package org.redhat.vrp.domain;

import org.redhat.vrp.domain.location.Location;

public class Home {

	protected Location location;

	private long windowStart;

	private long windowEnd;

	/**
	 * The jobOrSpecialist parameter must never be null
	 */
	public long getDistanceTo(JobOrSpecialist jobOrSpecialist) {
		return this.location.getDistanceTo(jobOrSpecialist.getLocation());
	}

	public Location getLocation() {
		return this.location;
	}

	public long getWindowEnd() {
		return this.windowEnd;
	}

	public long getWindowStart() {
		return this.windowStart;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setWindowEnd(long windowEnd) {
		this.windowEnd = windowEnd;
	}

	public void setWindowStart(long windowStart) {
		this.windowStart = windowStart;
	}

	@Override
	public String toString() {
		if (this.location.getName() == null) {
			return super.toString();
		}
		return this.location.getName();
	}

}
