package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ACOChristofidesSolver {

	private int numAnts; // number of ants
	private double alpha; // parameter controlling the influence of pheromone
	private double beta; // parameter controlling the influence of distance
	private double rho; // pheromone evaporation rate
	private double q; // pheromone deposit factor
	private double[][] distances; // distance matrix
	private double[][] pheromones; // pheromone matrix
	private double bestTourLength; // length of the best tour found
	private List<Vertex> bestTour; // list of vertices in the best tour\
	private List<Vertex> chrisitofidesTour;

	public ACOChristofidesSolver( List<Vertex> chrisitofidesTour, double[][] distances) {
		this.numAnts = 50;
		this.alpha = 1.0;
		this.beta = 1.0;
		this.rho = 0.1;
		this.q = 2.5;
		this.distances = distances;
		this.pheromones = new double[distances.length][distances.length];
		this.bestTourLength = Double.MAX_VALUE;
		this.bestTour = null;
		this.chrisitofidesTour = chrisitofidesTour;
	}

	public List<Vertex> solve() {
		// Run Christofides algorithm to get initial solution
		List<Vertex> christofidesTour = chrisitofidesTour;
		// Initialize pheromone matrix with a small value
		double initPheromone = 0.01;
		for (int i = 0; i < pheromones.length; i++) {
			for (int j = 0; j < pheromones[i].length; j++) {
				pheromones[i][j] = initPheromone;
			}
		}
		// Run ACO algorithm
		for (int iter = 0; iter < 1000; iter++) {
			// Initialize ant tours
			List<List<Vertex>> antTours = new ArrayList<>();
			for (int i = 0; i < numAnts; i++) {
				List<Vertex> antTour = new ArrayList<>(christofidesTour);
				antTours.add(antTour);
			}
			// Construct ant tours
			for (int i = 0; i < distances.length - 1; i++) {
				for (int j = i + 1; j < distances.length; j++) {
					for (int k = 0; k < numAnts; k++) {
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
						double prob = Math.pow(tau, alpha) * Math.pow(eta, beta);
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
					pheromones[i][j] *= (1.0 - rho);
				}
			}
			for (int k = 0; k < numAnts; k++) {
				List<Vertex> antTour = antTours.get(k);
				double tourLength = computeTourLength(antTour);
				if (tourLength < bestTourLength) {
					bestTourLength = tourLength;
					bestTour = antTour;
				}
				for (int i = 0; i < antTour.size() - 1; i++) {
					int city1 = antTour.get(i).getID();
					int city2 = antTour.get(i + 1).getID();
					pheromones[city1][city2] += (q / tourLength);
					pheromones[city2][city1] += (q / tourLength);
				}
			}
		}
		return bestTour;
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
