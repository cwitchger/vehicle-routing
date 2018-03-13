package org.redhat.vrp.dataio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.optaplanner.persistence.common.api.domain.solution.SolutionFileIO;

import org.redhat.vrp.domain.Job;
import org.redhat.vrp.domain.Home;
import org.redhat.vrp.domain.Specialist;
import org.redhat.vrp.domain.LocationTimeUtility;
import org.redhat.vrp.domain.Proficiency;
import org.redhat.vrp.domain.Skill;
import org.redhat.vrp.domain.SpecialistRoutingSolution;
import org.redhat.vrp.domain.location.AirLocation;
import org.redhat.vrp.domain.location.DistanceType;
import org.redhat.vrp.domain.location.Location;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpecialistAssignmentIO implements SolutionFileIO<SpecialistRoutingSolution> {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String DAY_START = "09:00:00";
	private static final String DAY_END = "18:00:00";
	private static String WORKING_DAY = "2018-01-01";

	private static final String UNIT_OF_MEASUREMENT = "miles";

	public static String DATA_DELIMITER = "\t";

	public static String specialistFilename = "specialists.txt";

	public static String jobsFilename = "jobs.txt";

	/**
	 * Loads the specialist and job data using the default filenames.
	 */
	public static SpecialistRoutingSolution loadData(String dataFolderPath) {
		return loadAllData(dataFolderPath, specialistFilename, jobsFilename);
	}

	/**
	 * Loads the specialist and job data using the file names and folder
	 * provided
	 */
	public static SpecialistRoutingSolution loadAllData(String dataFolderPath, String specialistFilename,
			String jobsFilename) {
		File dataFolder = new File(dataFolderPath);

		// Initializes the solution with some default data
		SpecialistRoutingSolution specialistPlan = new SpecialistRoutingSolution();
		specialistPlan.setDistanceUnitOfMeasurement(UNIT_OF_MEASUREMENT);
		specialistPlan.setDistanceType(DistanceType.AIR_DISTANCE);

		// Load all of the jobs that need to be worked
		List<Proficiency> proficiencies = new ArrayList<Proficiency>();
		List<Job> jobs = loadJobData(dataFolder, jobsFilename, proficiencies);
		specialistPlan.setJobList(jobs);
		specialistPlan.setProficiencies(proficiencies);

		// Load all of the specialist that are worked
		List<Specialist> specialistList = loadSpecialistData(dataFolder, specialistFilename);
		specialistPlan.setSpecialistList(specialistList);

		// Gathers the specialists home locations from the specialist list and
		// adds to the solution for faster solving.
		List<Home> homeList = specialistList.stream().map(Specialist::getHome).collect(Collectors.toList());
		specialistPlan.setHomeList(homeList);

		// Gathers the job locations from the job list and adds to the
		// solution for faster solving.
		List<Location> locationList = jobs.stream().map(Job::getLocation).collect(Collectors.toList());
		specialistPlan.setLocationList(locationList);

		return specialistPlan;
	}

	/**
	 * Loads the specialist data using the java Scanner. The data should be a
	 * tab delimited list in the following order. 0 - specialist id. 1 -
	 * specialist home latitude. 2 - specialist home longitude, 3 - skill list.
	 */
	private static List<Specialist> loadSpecialistData(File dataFolder, String specialistFilename) {
		File specialistFile = new File(dataFolder, specialistFilename);
		List<Specialist> specialists = new ArrayList<Specialist>();

		Scanner scanner = null;
		try {
			scanner = new Scanner(specialistFile);

			while (scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(DATA_DELIMITER);

				String name = tokens[0];
				// Create specialist with home latitude and longitude
				Double latitude = Double.valueOf(tokens[1]);
				Double longitude = Double.valueOf(tokens[2]);
				Set<Skill> skillList = getSkillSet(tokens[3]);

				Specialist specialist = new Specialist();
				Home home = new Home();
				home.setLocation(new AirLocation(name, latitude, longitude));

				Long windowStart = DATE_FORMAT.parse(WORKING_DAY + " " + DAY_START).getTime();
				Long windowEnd = DATE_FORMAT.parse(WORKING_DAY + " " + DAY_END).getTime();
				home.setWindowStart(windowStart);
				home.setWindowEnd(windowEnd);

				specialist.setSpecialistId(name);
				specialist.setHome(home);
				specialist.setSkills(skillList);

				specialists.add(specialist);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return specialists;
	}

	private static Map<String, Skill> allSkills = new HashMap<String, Skill>(10);

	private static Set<Skill> getSkillSet(String skills) {
		Set<Skill> skillList = new HashSet<Skill>(10);
		StringTokenizer tokenizer = new StringTokenizer(skills, ",");

		while (tokenizer.hasMoreTokens()) {
			String skillName = tokenizer.nextToken();
			Skill skill = allSkills.get(skillName);
			if (skill == null) {
				skill = new Skill(skillName);
			}

			skillList.add(skill);
		}

		return skillList;
	}

	/**
	 * Loads the job data using the java Scanner. The data should be a tab
	 * delimited list in the following order. 0 - job id. 1 - skill type. 2 -
	 * job latitude. 3 - job longitude.
	 */
	private static List<Job> loadJobData(File dataFolder, String jobFilename, List<Proficiency> proficiencies) {
		File specialistFile = new File(dataFolder, jobFilename);
		List<Job> jobs = new ArrayList<Job>();

		Scanner scanner = null;
		try {
			scanner = new Scanner(specialistFile);

			while (scanner.hasNextLine()) {
				String[] tokens = scanner.nextLine().split(DATA_DELIMITER);

				// Create specialist with home latitude and longitude
				Double latitude = Double.valueOf(tokens[2]);
				Double longitude = Double.valueOf(tokens[3]);
				Long serviceDuration = (long) (Double.valueOf(tokens[4]) * LocationTimeUtility.MINUTES_TO_MILLISECONDS);

				Set<Skill> skills = getSkillSet(tokens[1]);

				Job job = new Job();
				job.setJobId(tokens[0]);
				job.setRequiredSkills(skills);

				for (Skill s : skills) {
					proficiencies.add(new Proficiency(job, s));
				}

				job.setLocation(new AirLocation(tokens[0], latitude, longitude));
				job.setServiceDuration(serviceDuration);

				Long windowStart = DATE_FORMAT.parse(WORKING_DAY + " " + DAY_START).getTime();
				Long windowEnd = DATE_FORMAT.parse(WORKING_DAY + " " + DAY_END).getTime();
				job.setWindowStart(windowStart);
				job.setWindowEnd(windowEnd);

				jobs.add(job);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return jobs;
	}

	@Override
	public String getInputFileExtension() {
		return "txt";
	}

	@Override
	public String getOutputFileExtension() {
		return "json";
	}

	@Override
	public SpecialistRoutingSolution read(File file) {
		SpecialistRoutingSolution specialistPlan = SpecialistAssignmentIO.loadAllData(file.getPath(),
				SpecialistAssignmentIO.specialistFilename, SpecialistAssignmentIO.jobsFilename);
		return specialistPlan;
	}

	@Override
	public void write(SpecialistRoutingSolution solution, File file) {
		printAll(solution, file);
	}

	public static void printAll(SpecialistRoutingSolution solution, File file) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		SchedulePrinter.print(solution);

		if (file != null) {
			try {
				mapper.writeValue(new FileOutputStream(file), solution);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
