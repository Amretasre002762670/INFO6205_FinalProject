package com.neu.psa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColonyOptimization {

	private List<Vertex> bestAntColonyTour; // list of vertices in the best tour
	private List<Vertex> chrisitofidesTour;
	private final int NUMBER_OF_ANTS = 50; // Number of Ants
	private final double ALPHA = 1.0; // Controls the influence on pheromone
	private final double BETA = 1.0; // Controls the influence on distance
	private final double RHO = 0.1; // Pheromone Evaporation Rate
	private final double DEPOSIT_FACTOR = 2.5; // pheromone deposit factor
	private double[][] distances; // 2-D Distance Matrix
	private double[][] pheromones; // pheromone matrix
	private double bestTourLength; // Stores the length of the best tour found

	public AntColonyOptimization(List<Vertex> chrisitofidesTour, double[][] distances) {
		this.distances = distances;
		this.pheromones = new double[distances.length][distances.length];
		this.bestTourLength = Double.MAX_VALUE;
		this.bestAntColonyTour = null;
		this.chrisitofidesTour = chrisitofidesTour;
	}

	public List<Vertex> solve() {

		// To get the initial tour chritofides algorithm is used
		List<Vertex> christofidesTour = chrisitofidesTour;

		double initPheromone = 0.01;
		for (int i = 0; i < pheromones.length; i++) {
			for (int j = 0; j < pheromones[i].length; j++) {
				pheromones[i][j] = initPheromone;
			}
		}

		// This will run the Ant Colonoy Optimization algorithm
		for (int iteration = 0; iteration < 1000; iteration++) {

			List<List<Vertex>> antTours = new ArrayList<>();
			for (int i = 0; i < NUMBER_OF_ANTS; i++) {
				List<Vertex> antTour = new ArrayList<>(christofidesTour);
				antTours.add(antTour);
			}
			// Construct ant colony tour
			for (int i = 0; i < distances.length - 1; i++) {
				for (int j = i + 1; j < distances.length; j++) {
					for (int k = 0; k < NUMBER_OF_ANTS; k++) {
						List<Vertex> antTour = antTours.get(k);
						if (antTour.get(i).getID() == antTour.get(j).getID()) {

							// Swap vertices if they are the same
							int randIdx = new Random().nextInt(antTour.size() - 1) + 1;
							Vertex tmp = antTour.get(randIdx);
							antTour.set(randIdx, antTour.get(j));
							antTour.set(j, tmp);
						}
						double tau = pheromones[antTour.get(i).getID()][antTour.get(j).getID()];
						double eta = 1.0 / distances[antTour.get(i).getID()][antTour.get(j).getID()];
						double prob = Math.pow(tau, ALPHA) * Math.pow(eta, BETA);
						if (new Random().nextDouble() < prob) {
							// Swap vertices
							Vertex tmp = antTour.get(j);
							antTour.set(j, antTour.get(i));
							antTour.set(i, tmp);
						}
					}
				}
			}
			// Update pheromone matrix
			for (int i = 0; i < pheromones.length; i++) {
				for (int j = 0; j < pheromones[i].length; j++) {
					pheromones[i][j] *= (1.0 - RHO);
				}
			}
			for (int k = 0; k < NUMBER_OF_ANTS; k++) {
				List<Vertex> antTour = antTours.get(k);
				double tourLength = computeTourLength(antTour);
				if (tourLength < bestTourLength) {
					bestTourLength = tourLength;
					bestAntColonyTour = antTour;
				}
				for (int i = 0; i < antTour.size() - 1; i++) {
					int city1 = antTour.get(i).getID();
					int city2 = antTour.get(i + 1).getID();
					pheromones[city1][city2] += (DEPOSIT_FACTOR / tourLength);
					pheromones[city2][city1] += (DEPOSIT_FACTOR / tourLength);
				}
			}
		}
		return bestAntColonyTour;
	}

	private double computeTourLength(List<Vertex> tour) {
		double length = 0;
		for (int i = 0; i < tour.size() - 1; i++) {
			int city1 = tour.get(i).getID();
			int city2 = tour.get(i + 1).getID();
			length += distances[city1][city2];
		}
		return length;
	}
}
