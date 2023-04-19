package com.neu.psa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreeOptOptimization {
	public static List<Vertex> threeOpt(List<Vertex> tour, double[][] distanceMatrix) {
		boolean improvement = true;
		while (improvement) {
			improvement = false;
			for (int i = 0; i < tour.size() - 2; i++) {
				for (int j = i + 2; j < tour.size() - 1; j++) {
					List<Vertex> updatedTour = applyThreeOptSwap(tour, i, j);
					double updatedDistance = calculateTourDistance(updatedTour, distanceMatrix);
					if (updatedDistance < calculateTourDistance(tour, distanceMatrix)) {
						tour = updatedTour;
						improvement = true;
					}
				}
			}
		}
		return tour;
	}

	private static List<Vertex> applyThreeOptSwap(List<Vertex> tour, int i, int j) {
		List<Vertex> updatedTour = new ArrayList<>(tour.subList(0, i + 1));
		List<Vertex> reversedSublist = new ArrayList<>(tour.subList(i + 1, j + 1));
		Collections.reverse(reversedSublist);
		updatedTour.addAll(reversedSublist);
		updatedTour.addAll(tour.subList(j + 1, tour.size()));
		return updatedTour;
	}

	private static double calculateTourDistance(List<Vertex> tour, double[][] distanceMatrix) {
		double distance = 0;
		for (int i = 0; i < tour.size() - 1; i++) {
			int from = tour.get(i).getID();
			int to = tour.get(i + 1).getID();
			distance += distanceMatrix[from][to];
		}
		distance += distanceMatrix[tour.get(tour.size() - 1).getID()][tour.get(0).getID()];
		return distance;
	}

}
