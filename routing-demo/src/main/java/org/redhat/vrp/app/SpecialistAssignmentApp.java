package org.redhat.vrp.app;

import org.optaplanner.core.api.solver.Solver;
import org.redhat.vrp.dataio.SpecialistAssignmentIO;
import org.redhat.vrp.domain.SpecialistRoutingSolution;

/**
 * This is an executable java class which uses the default data set and default
 * solver configuration to schedule specialists to jobs
 */
public class SpecialistAssignmentApp {

	public static void main(String[] args) {
		SpecialistAssignmentApp vrpApp = new SpecialistAssignmentApp();
		vrpApp.solve();
	}

	public String outputDataFile = SpecialistAssignmentIO.DATA_FOLDER_PATH + "/output/response.json";

	private SpecialistRoutingSolution specialistPlan;

	public SpecialistAssignmentApp() {
		this.specialistPlan = SpecialistAssignmentIO.loadData(SpecialistAssignmentIO.DATA_FOLDER_PATH);
	}

	/**
	 * Initializes the solver
	 */
	private void solve() {
		Solver<SpecialistRoutingSolution> solver = AppHelper.getSolverFactory().buildSolver();
		SpecialistRoutingSolution solution = solver.solve(this.specialistPlan);

		SpecialistAssignmentIO.printAll(solution, null);
	}

}
