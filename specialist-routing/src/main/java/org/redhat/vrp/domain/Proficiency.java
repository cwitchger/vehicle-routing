package org.redhat.vrp.domain;

public class Proficiency {

	private Job job;

	private Skill skill;

	public Proficiency() {
		super();
	}

	public Proficiency(Job job, Skill skill) {
		super();
		this.job = job;
		this.skill = skill;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Proficiency other = (Proficiency) obj;
		if (this.job == null) {
			if (other.job != null) {
				return false;
			}
		} else if (!this.job.equals(other.job)) {
			return false;
		}
		if (this.skill == null) {
			if (other.skill != null) {
				return false;
			}
		} else if (!this.skill.equals(other.skill)) {
			return false;
		}
		return true;
	}

	public Job getJob() {
		return this.job;
	}

	public Skill getSkill() {
		return this.skill;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.job == null) ? 0 : this.job.hashCode());
		result = prime * result + ((this.skill == null) ? 0 : this.skill.hashCode());
		return result;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

}
