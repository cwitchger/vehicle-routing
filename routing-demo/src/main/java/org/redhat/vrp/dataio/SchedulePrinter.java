package org.redhat.vrp.dataio;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.redhat.vrp.domain.Job;
import org.redhat.vrp.domain.JobOrSpecialist;
import org.redhat.vrp.domain.Skill;
import org.redhat.vrp.domain.Specialist;
import org.redhat.vrp.domain.SpecialistRoutingSolution;

public class SchedulePrinter {

	public static void print(SpecialistRoutingSolution solution) {
		for (Specialist specialist : solution.getSpecialistList()) {
			if (specialist.getNextJob() != null) {
				StringBuffer output = new StringBuffer();

				output.append(specialist.getSpecialistId() + "\n");

				JobOrSpecialist j = specialist;
				while (j.getNextJob() != null) {
					Job job = j.getNextJob();

					printJobFormat(output, job.getJobId(), job.getArrivalTime(), job.getDepartureTime());

					if (job.getRequiredSkills() != null) {
						Set<Skill> skills = new HashSet<Skill>(job.getRequiredSkills());
						for (Skill skill : specialist.getSkills()) {
							skills.remove(skill);
						}

						if (skills.size() > 0) {
							output.append("\n  Missing Skills: " + skills);
						}
					}

					output.append("\n");

					j = job;
				}

				System.out.println(output);
			}
		}

	}

	private static void printJobFormat(StringBuffer jobSkills, String name, long arrival, long departure) {
		String arrivalDate = SpecialistAssignmentIO.DATE_FORMAT.format(new Date(arrival));
		String departureDate = SpecialistAssignmentIO.DATE_FORMAT.format(new Date(departure));

		jobSkills.append(name + ": " + arrivalDate + " <-> " + departureDate);
	}

}
