package com.neu.psa;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JFrame;

public class TSPMainProgram {

	public static void main(String[] args) throws IOException {

		// Based on the last parameter passed, an optimization algorithm will be
		// executed
		christofidesAlgorithm("crimeSample500.csv", 2, "");
	}

	public static ChristofidesTour christofidesAlgorithm(String inputFilePath, int secondsToRunTwoOpt,
			String optimization) throws IOException {
		Benchmark benchmark = new Benchmark();
		benchmark.startTimer();
		List<Vertex> graphList = parseGraph(inputFilePath);
		double[][] distances = getDistances(graphList);
		List<Vertex> MST = PrimsAlgorithm.run(graphList, distances);
		createEvenlyVertexedEularianMultiGraphFromMST(MST, distances);
		List<Vertex> eulerTour = HierholzerAlgorithm.runHierholzerAlgorithm(MST);
		List<Vertex> travelingSalesPath = DuplicateHelper.run(eulerTour);

		optimization = optimization.toLowerCase();

		// Executes the Optimization algorithm based on the parameter passed
		switch (optimization) {

		// Handles 2-Opt optimization algorithm (Tactical Algorithm)
		case "twoopt": {
			TwoOptOptimization twoOpt = new TwoOptOptimization(travelingSalesPath, distances, secondsToRunTwoOpt);
			travelingSalesPath = twoOpt.run();
			break;
		}

		// Handles 3-Opt optimization algorithm (Tactical Algorithm)
		case "threeopt": {
			travelingSalesPath = ThreeOptOptimization.threeOpt(travelingSalesPath, distances);
			break;
		}

		// Handles Ant Colony optimization algorithm (Strategic Algorithm)
		case "aco": {
			AntColonyOptimization aco = new AntColonyOptimization(travelingSalesPath, distances);
			travelingSalesPath = aco.solve();
			break;
		}

		// Handles Simulated Annealing optimization algorithm (Strategic Algorithm)
		case "simopt": {
			travelingSalesPath = SimulatedAnnealingOptimization.optimizeWithSimulatedAnnealing(travelingSalesPath,
					distances);
			break;
		}

		}

		ChristofidesTour finalAnswer = finalAnswer(travelingSalesPath, distances, inputFilePath, optimization);
		benchmark.endTimer();
		System.out.println("Program took: " + benchmark.getResultTime() + " ms");
		return finalAnswer;
	}

	// Calculates distances between all points on the graph
	private static double[][] getDistances(List<Vertex> graphList) {
		double[][] graphDistance = new double[graphList.size()][graphList.size()];
		for (int i = 0; i < graphList.size(); i++) {
			for (int j = 0; j < graphList.size(); j++)
				graphDistance[i][j] = calculateDistance(graphList.get(i), graphList.get(j));
		}
		return graphDistance;
	}

	// Calculates the differences in the distance and converts the distance to meters
	private static double calculateDistance(Vertex a, Vertex b) {

		double lat1Rad = Math.toRadians(a.getLatitude());
		double lon1Rad = Math.toRadians(a.getLongitude());
		double lat2Rad = Math.toRadians(b.getLatitude());
		double lon2Rad = Math.toRadians(b.getLongitude());

		double deltaLat = lat2Rad - lat1Rad;
		double deltaLon = lon2Rad - lon1Rad;

		double distRadA = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
				+ Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

		double distRadC = 2 * Math.atan2(Math.sqrt(distRadA), Math.sqrt(1 - (distRadA)));

		// Converts the distance to meters
		double distance = 6371 * distRadC * 1000;

		return distance;
	}

	private static List<Vertex> createEvenlyVertexedEularianMultiGraphFromMST(List<Vertex> minimumSpanningTree,
			double[][] distances) {

		List<Vertex> oddVertices = minimumSpanningTree.stream()
				.filter(vertex -> vertex.getConnectedVertices.size() % 2 == 1)
				.collect(Collectors.toCollection(ArrayList::new));

		while (!oddVertices.isEmpty()) {
			double distance = Double.MAX_VALUE;
			final Vertex parent = oddVertices.get(0);

			// Compare pointers to not use root node.
			double minDistanceToNextNode = oddVertices.stream()
					.mapToDouble(vertex -> vertex == parent ? Double.MAX_VALUE
							: distances[(int) parent.getID()][(int) vertex.getID()])
					.min().getAsDouble();

			Vertex child = oddVertices.stream()
					.filter(vertex -> distances[(int) parent.getID()][(int) vertex.getID()] == minDistanceToNextNode
							&& vertex != parent)
					.findFirst().get();

			Edge fromParentToChildEdge = new Edge(parent, child, distance);
			Edge fromChildToParentEdge = new Edge(child, parent, distance);
			parent.getConnectedVertices.add(fromParentToChildEdge);
			child.getConnectedVertices.add(fromChildToParentEdge);
			oddVertices.remove(parent);
			oddVertices.remove(child);
		}
		return minimumSpanningTree;
	}

	private static ChristofidesTour finalAnswer(List<Vertex> TSP, double[][] distances, String p, String optimization)
			throws FileNotFoundException {
		
		String optimizationUsed = optimization.toLowerCase();
		String optUsed = "";
		switch (optimizationUsed) {

		case "twoopt": {
			optUsed = "2-Opt Optimization Technique";
			break;
		}

		case "threeopt": {
			optUsed = "3-Opt Optimization Technique";
			break;
		}

		case "aco": {
			optUsed = "Ant Colony Optimization Technique";
			break;
		}

		case "simopt": {
			optUsed = "Simulated Annealing Optimization Technique";
			break;
		}
		
		case "":
			optUsed = "Christofides";
			break;

		}
		
		try (PrintWriter writer = new PrintWriter((p + ".tour"))) {

			double totalDistance = TSP.stream()
					.mapToDouble(vertex -> TSP.indexOf(vertex) == TSP.size() - 1
							? distances[(int) TSP.get(0).getID()][(int) TSP.get(TSP.size() - 1).getID()]
							: distances[(int) vertex.getID()][(int) TSP.get(TSP.indexOf(vertex) + 1).getID()])
					.sum();

			System.out
					.println("Total distance covered by " + TSP.size() + " vertices is : " + totalDistance + " meters");
			writer.println(totalDistance);

			List<String> finalTour = TSP.stream().map(vertex -> {
				writer.println(vertex.getVertexID());
				return vertex.getVertexID();
			}).collect(Collectors.toCollection(ArrayList::new));

			// Create the TSPVisualization panel
			TSPVisualization panel = new TSPVisualization(TSP);

			// Create a JFrame to display the TSPVisualization panel
			JFrame frame = new JFrame("TSP Visualization using "+optUsed);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(panel);		
			frame.setSize(2000, 2000);
			frame.setVisible(true);

			return new ChristofidesTour(finalTour, totalDistance);
		}
	}

	private static List<Vertex> parseGraph(String fileName) throws IOException {

		List<Vertex> vertexList = new ArrayList<Vertex>();
		int count = 0;

		// Handles logic for extracting the data from the csv file
		try {
			BufferedReader readFile = new BufferedReader(new FileReader(fileName));
			readFile.readLine();
			String readFilerow = null;
			while ((readFilerow = readFile.readLine()) != null) {
				String[] data = readFilerow.split(",");

				String id = null;
				String actualId = "";
				double longtitude = 0, latitude = 0;

				id = data[0];
				id = id.substring(id.length() - 5, id.length()); // Used for extracting last 5 digits from the Crime Id

				actualId = id;
				longtitude = Double.parseDouble(data[1]);
				latitude = Double.parseDouble(data[2]);

				Vertex v1 = new Vertex(count++, actualId, longtitude, latitude); // Creates a new Vertex
				vertexList.add(v1);

			}
			readFile.close();
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}

		return vertexList;
	}
}
