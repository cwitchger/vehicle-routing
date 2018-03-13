package org.redhat.vrp.domain.solver;

import java.util.Objects;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import org.redhat.vrp.domain.JobOrSpecialist;
import org.redhat.vrp.domain.Specialist;
import org.redhat.vrp.domain.Job;
import org.redhat.vrp.domain.Home;

@SuppressWarnings("rawtypes")
public class ArrivalTimeUpdatingVariableListener implements VariableListener<Job> {

	public void beforeEntityAdded(ScoreDirector scoreDirector, Job job) {
		// Do nothing
	}

	public void afterEntityAdded(ScoreDirector scoreDirector, Job job) {
		if (job instanceof Job) {
			updateArrivalTime(scoreDirector, (Job) job);
		}
	}

	public void beforeVariableChanged(ScoreDirector scoreDirector, Job job) {
		// Do nothing
	}

	public void afterVariableChanged(ScoreDirector scoreDirector, Job job) {
		if (job instanceof Job) {
			updateArrivalTime(scoreDirector, (Job) job);
		}
	}

	public void beforeEntityRemoved(ScoreDirector scoreDirector, Job job) {
		// Do nothing
	}

	public void afterEntityRemoved(ScoreDirector scoreDirector, Job job) {
		// Do nothing
	}

	protected void updateArrivalTime(ScoreDirector scoreDirector, Job sourceJob) {
		JobOrSpecialist previousJobOrSpecialist = sourceJob.getPreviousJobOrSpecialist();
		Long departureTime = previousJobOrSpecialist == null ? null
				: (previousJobOrSpecialist instanceof Job) ? ((Job) previousJobOrSpecialist).getDepartureTime()
						: ((Home) ((Specialist) previousJobOrSpecialist).getHome()).getWindowStart();
		Job shadowJob = sourceJob;
		Long arrivalTime = calculateArrivalTime(shadowJob, departureTime);
		while (shadowJob != null && !Objects.equals(shadowJob.getArrivalTime(), arrivalTime)) {
			scoreDirector.beforeVariableChanged(shadowJob, "arrivalTime");
			shadowJob.setArrivalTime(arrivalTime);
			scoreDirector.afterVariableChanged(shadowJob, "arrivalTime");
			departureTime = shadowJob.getDepartureTime();
			shadowJob = shadowJob.getNextJob();
			arrivalTime = calculateArrivalTime(shadowJob, departureTime);
		}
	}

	private Long calculateArrivalTime(Job job, Long previousDepartureTime) {
		if (job == null || job.getPreviousJobOrSpecialist() == null) {
			return null;
		}
		if (job.getPreviousJobOrSpecialist() instanceof Specialist) {
			// PreviousJobOrSpecialist is the Specialist, so we leave from the Home at the best suitable time
			return Math.max(job.getWindowStart(),
					previousDepartureTime + job.getDistanceFromPreviousJobOrSpecialist());
		}
		return previousDepartureTime + job.getDistanceFromPreviousJobOrSpecialist();
	}

}
