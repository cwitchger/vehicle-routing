package org.redhat.vrp.domain.solver.realtime;

import java.util.ArrayList;

import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.solver.ProblemFactChange;
import org.redhat.vrp.domain.Job;
import org.redhat.vrp.domain.SpecialistRoutingSolution;

public class AddJobProblemFactChange implements ProblemFactChange<SpecialistRoutingSolution> {

	private Job job;

	public AddJobProblemFactChange() {
		super();
		job = null;
	}

	public AddJobProblemFactChange(Job job) {
		this.job = job;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void doChange(ScoreDirector<SpecialistRoutingSolution> scoreDirector) {
		SpecialistRoutingSolution solution = scoreDirector.getWorkingSolution();

		solution.setJobList(new ArrayList<Job>(solution.getJobList()));
		
		// Add the problem fact itself
		scoreDirector.beforeProblemFactAdded(job);
		solution.getJobList().add(job);
		scoreDirector.afterProblemFactAdded(job);
	}

}
