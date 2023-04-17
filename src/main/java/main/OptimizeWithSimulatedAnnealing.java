package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OptimizeWithSimulatedAnnealing {
	public static List<Vertex> optimizeWithSimulatedAnnealing(List<Vertex> vertices, double[][] distanceMatrix) {
	    // Step 1: Generate an initial Hamiltonian circuit using the Christofides algorithm
	    List<Vertex> initialSolution = vertices;

	    // Step 2: Optimize the Hamiltonian circuit using Simulated Annealing
	    double currentDistance = calculateTotalDistance(initialSolution, distanceMatrix);
	    double temperature = 1000.0; // Initial temperature
	    double coolingRate = 0.999; // Cooling rate
	    Random random = new Random();

	    while (temperature > 0.1) { // Continue until temperature reaches a low value
	        List<Vertex> newSolution = new ArrayList<>(initialSolution); // Create a new solution
	        int index1 = random.nextInt(vertices.size()); // Randomly select two indices
	        int index2 = random.nextInt(vertices.size());
	        Collections.swap(newSolution, index1, index2); // Swap the vertices at the selected indices
	        double newDistance = calculateTotalDistance(newSolution, distanceMatrix);
	        double delta = newDistance - currentDistance;
	        if (delta < 0 || Math.exp(-delta / temperature) > random.nextDouble()) {
	            // If the new solution is better or satisfies the acceptance probability, accept it
	            initialSolution = newSolution;
	            currentDistance = newDistance;
	        }
	        temperature *= coolingRate; // Cool down the temperature
	    }
	 
	    return initialSolution;
	}

	// Helper method to calculate the total distance of a Hamiltonian circuit
	public static double calculateTotalDistance(List<Vertex> circuit, double[][] distance) {
	    double totalDistance = 0.0;
	    for (int i = 0; i < circuit.size() - 1; i++) {
	        int vertex1 = circuit.get(i).getID();
	        int vertex2 = circuit.get(i + 1).getID();
	        totalDistance += distance[vertex1][vertex2];
	    }
	    int lastVertex = circuit.get(circuit.size()-1).getID();
	    int firstVertex = circuit.get(0).getID();
	    totalDistance += distance[lastVertex][firstVertex]; // Add distance from last to first vertex
	    return totalDistance;
	}

}
