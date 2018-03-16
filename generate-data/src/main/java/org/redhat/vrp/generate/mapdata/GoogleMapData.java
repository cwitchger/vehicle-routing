package org.redhat.vrp.generate.mapdata;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.redhat.vrp.dataio.SpecialistAssignmentIO;
import org.redhat.vrp.domain.Home;
import org.redhat.vrp.domain.SpecialistRoutingSolution;
import org.redhat.vrp.domain.location.Location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

public class GoogleMapData {

	private static final String API_KEY = "AIzaSyDAG9ek3JROuCYs4VmYOm39VWB6lhcVJJs";
	private static String INPUT_DIR = "../routing-demo/data/small/";
	private static String OUTPUT_DIR = "data/distance-matrix.json";

	public static void main(String[] args) {
		Builder builder = new Builder();
		builder.apiKey(API_KEY);
		GeoApiContext context = builder.build();

		List<Location> locations = getAllLocations();
		Map<String, Map<String, Long>> distanceMatrix = new HashMap<String, Map<String, Long>>(locations.size());

		for (Location from : locations) {
			Map<String, Long> distanceRow = new HashMap<String, Long>();
			String origin = from.getLatitude() + "," + from.getLongitude();

			for (Location to : locations) {
				String destination = to.getLatitude() + "," + to.getLongitude();
				Long time = getTravelTime(context, origin, destination);

				distanceRow.put(to.getName(), time);
			}
			distanceMatrix.put(from.getName(), distanceRow);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new FileOutputStream(OUTPUT_DIR), distanceMatrix);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static List<Location> getAllLocations() {
		SpecialistRoutingSolution specialistPlan = SpecialistAssignmentIO.loadData(INPUT_DIR);
		List<Home> homeList = specialistPlan.getHomeList();
		List<Location> locations = homeList.stream().map(Home::getLocation).collect(Collectors.toList());
		locations.addAll(specialistPlan.getLocationList());
		return locations;
	}

	private static Long getTravelTime(GeoApiContext context, String origin, String destination) {
		DirectionsApiRequest request = DirectionsApi.getDirections(context, origin, destination);

		Long time = 0L;
		try {
			DirectionsResult result = request.await();
			time = result.routes[0].legs[0].duration.inSeconds;
		} catch (ApiException | InterruptedException | IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			time = 9999999L;
		}

		return time;
	}
}
