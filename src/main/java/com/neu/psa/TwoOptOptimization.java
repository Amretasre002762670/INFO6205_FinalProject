package com.neu.psa;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TwoOptOptimization {
	private double[][] distances;
	private List<Vertex> vertices;
	private int secondsToRun;
	private LocalDateTime start;

	TwoOptOptimization(List<Vertex> vertices, double[][] distances, int secondsToRun) {
		this.distances = distances;
		this.vertices = vertices;
		this.secondsToRun = secondsToRun / 2; // divide by two as implementation has two opt running twice.
	}

	private List<Vertex> runTwoOpt(boolean twoOptIterationB) {
		start = LocalDateTime.now();
		do {
			if (isTimeUp())
				break;

			double currentDistance = calculateTotalDistance(vertices);

			for (int i = 1; i < vertices.size() - 1; i++) {
				for (int k = i + 1; k < vertices.size() - 2; k++) {

					ArrayList<Vertex> updatedRoute = TwoOptSwap(vertices, i, k);
					double updatedDistance = calculateTotalDistance(updatedRoute);
					if (updatedDistance < currentDistance) {
						vertices = updatedRoute;
						currentDistance = updatedDistance;
						if (twoOptIterationB)
							break;

						if (isTimeUp())
							break;
					}
					if (isTimeUp())
						break;
				}
				if (isTimeUp())
					break;
			}

		} while (!isTimeUp());

		return vertices;
	}

	private boolean isTimeUp() {
		long elapsedTime;
		elapsedTime = ChronoUnit.SECONDS.between(start, LocalDateTime.now());
		return elapsedTime >= secondsToRun;
	}

	public List<Vertex> run() {
		vertices = runTwoOpt(true);
		return vertices;
	}

	private ArrayList<Vertex> TwoOptSwap(List<Vertex> route, int i, int k) {

		ArrayList<Vertex> newRoute = new ArrayList<>();

		for (int first = 0; first <= i - 1; first++)
			newRoute.add(route.get(first));

		for (int last = k; last >= i; last--)
			newRoute.add(route.get(last));

		for (int end = k + 1; end <= route.size() - 1; end++)
			newRoute.add(route.get(end));

		return newRoute;
	}

	private double calculateTotalDistance(List<Vertex> path) {
		double totalDistance = 0;

		for (int i = 0; i < path.size() - 1; i++)
			totalDistance += distances[(int) path.get(i).getID()][(int) path.get(i + 1).getID()];

		totalDistance += distances[(int) path.get(0).getID()][(int) path.get(path.size() - 1).getID()];

		return totalDistance;
	}
}
