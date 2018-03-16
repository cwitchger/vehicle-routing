package org.redhat.vrp.app;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.optaplanner.core.api.solver.SolverFactory;
import org.redhat.vrp.domain.SpecialistRoutingSolution;

public class AppHelper {
	public static final String group = "org.redhat.vrp";
	public static final String artifact = "specialist-routing";
	public static final String version = "0.0.1-SNAPSHOT";
	public static final String configFile = "org/redhat/vrp/solver/solverConfig.xml";

	public static SolverFactory<SpecialistRoutingSolution> getSolverFactory() {
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kieContainer = kieServices
				.newKieContainer(kieServices.newReleaseId(AppHelper.group, AppHelper.artifact, AppHelper.version));
		return SolverFactory.createFromKieContainerXmlResource(kieContainer, AppHelper.configFile);
	}
}
