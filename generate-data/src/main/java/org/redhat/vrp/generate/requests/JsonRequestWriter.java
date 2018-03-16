package org.redhat.vrp.generate.requests;

import java.io.FileOutputStream;
import java.io.IOException;

import org.redhat.vrp.dataio.SpecialistAssignmentIO;
import org.redhat.vrp.domain.SpecialistRoutingSolution;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRequestWriter {

	private static String EXAMPLE_DATA = SpecialistAssignmentIO.DATA_FOLDER_PATH + "/json-examples/with-roads";
	private static String OUTPUT_DATA = SpecialistAssignmentIO.DATA_FOLDER_PATH + "output/json-examples";

	public static void main(String[] args) {
		SpecialistRoutingSolution specialistPlan = SpecialistAssignmentIO.loadDataWithRoads(EXAMPLE_DATA);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

		try {
			FileOutputStream outputStream = new FileOutputStream(OUTPUT_DATA + "/output.json");
			mapper.writeValue(outputStream, specialistPlan);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
