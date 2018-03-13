package org.redhat.vrp.domain;

public class Proficiency {

	private Job job;

	private Skill skill;

	public Proficiency(Job job, Skill skill) {
		super();
		this.job = job;
		this.skill = skill;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}
}
