package org.redhat.vrp.domain.solver;

import java.util.Objects;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.redhat.vrp.domain.Job;
import org.redhat.vrp.domain.JobOrSpecialist;
import org.redhat.vrp.domain.Specialist;

@SuppressWarnings("rawtypes")
public class ArrivalTimeUpdatingVariableListener implements VariableListener<Job> {

	@Override
	public void afterEntityAdded(ScoreDirector scoreDirector, Job job) {
		if (job instanceof Job) {
			updateArrivalTime(scoreDirector, job);
		}
	}

	@Override
	public void afterEntityRemoved(ScoreDirector scoreDirector, Job job) {
		// Do nothing
	}

	@Override
	public void afterVariableChanged(ScoreDirector scoreDirector, Job job) {
		if (job instanceof Job) {
			updateArrivalTime(scoreDirector, job);
		}
	}

	@Override
	public void beforeEntityAdded(ScoreDirector scoreDirector, Job job) {
		// Do nothing
	}

	@Override
	public void beforeEntityRemoved(ScoreDirector scoreDirector, Job job) {
		// Do nothing
	}

	@Override
	public void beforeVariableChanged(ScoreDirector scoreDirector, Job job) {
		// Do nothing
	}

	private Long calculateArrivalTime(Job job, Long previousDepartureTime) {
		if (job == null || job.getPreviousJobOrSpecialist() == null) {
			return null;
		}
		if (job.getPreviousJobOrSpecialist() instanceof Specialist) {

			// PreviousJobOrSpecialist is the Specialist, so we leave from home
			// at the best suitable time
			return Math.max(job.getWindowStart(), previousDepartureTime + job.getDistanceFromPreviousJobOrSpecialist());
		}
		return previousDepartureTime + job.getDistanceFromPreviousJobOrSpecialist();
	}

	protected void updateArrivalTime(ScoreDirector scoreDirector, Job sourceJob) {
		JobOrSpecialist previousJobOrSpecialist = sourceJob.getPreviousJobOrSpecialist();
		Long departureTime = previousJobOrSpecialist == null ? null
				: (previousJobOrSpecialist instanceof Job) ? ((Job) previousJobOrSpecialist).getDepartureTime()
						: ((Specialist) previousJobOrSpecialist).getHome().getWindowStart();
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

}
