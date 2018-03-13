package org.redhat.vrp.app;

import java.io.File;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import org.redhat.vrp.dataio.SpecialistAssignmentIO;
import org.redhat.vrp.domain.SpecialistRoutingSolution;

/**
 * This is an executable java class which uses the default data set and default
 * solver configuration to schedule specialists to jobs
 */
public class SpecialistAssignmentApp {

	private static final String group = "org.redhat.vrp";
	private static final String artifact = "specialist-routing";
	private static final String version = "0.0.1-SNAPSHOT";
	private static final String configFile = "org/redhat/vrp/solver/solverConfig.xml";

	public String inputDataPath = "data";
	public String outputDataFile = inputDataPath + "/output/response.json";

	private SpecialistRoutingSolution specialistPlan;

	public static void main(String[] args) {
		SpecialistAssignmentApp vrpApp = new SpecialistAssignmentApp();
		vrpApp.solve();
	}

	public SpecialistAssignmentApp() {
		specialistPlan = SpecialistAssignmentIO.loadData(inputDataPath);
	}

	/**
	 * Initializes the solver
	 */
	private void solve() {
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kieContainer = kieServices.newKieContainer(kieServices.newReleaseId(group, artifact, version));
		SolverFactory<SpecialistRoutingSolution> factory = SolverFactory.createFromKieContainerXmlResource(kieContainer,
				configFile);

		Solver<SpecialistRoutingSolution> solver = factory.buildSolver();
		SpecialistRoutingSolution solution = solver.solve(specialistPlan);

		SpecialistAssignmentIO.printAll(solution, new File(outputDataFile));
	}

}
