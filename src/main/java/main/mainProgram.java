package main;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.JFrame;

public class mainProgram {

	public static void main(String[] args) throws IOException {

		christofidesAlgorithm("crimeSample.csv", 2, "simopt");
	}

	public static ChristofidesTour christofidesAlgorithm(String inputFilePath, int secondsToRunTwoOpt, String optimization)
			throws IOException {
		Benchmark benchmark = new Benchmark();
		benchmark.startTimer();
		List<Vertex> graphList = parseGraph(inputFilePath);
		double[][] distances = getDistances(graphList);
		List<Vertex> MST = PrimsAlgorithm.run(graphList, distances);
		createEvenlyVertexedEularianMultiGraphFromMST(MST, distances);
		List<Vertex> eulerTour = HierholzerAlgorithm.run(MST);
		List<Vertex> travelingSalesPath = ShortCut.run(eulerTour);
		
		// Two optimization
//		TwoOpt twoOpt = new TwoOpt(travelingSalesPath, distances, secondsToRunTwoOpt);
//		travelingSalesPath = twoOpt.run();
		
		// Three optimization
//		travelingSalesPath = TSPThreeOpt.threeOpt(travelingSalesPath, distances);

		// Ant colony optimization,
//		ACOChristofides aco = new ACOChristofides(travelingSalesPath, distances);
//		travelingSalesPath = aco.acoChristofides();
		
		//Simulated Annealing
//		SimulatedAnnealing sa = new SimulatedAnnealing();
//		travelingSalesPath = SimulatedAnnealing.simulatedAnnealing(travelingSalesPath, distances);
		
//		travelingSalesPath = SimulatedAnnealing2.simulatedAnnealing(travelingSalesPath, distances);
		
//		travelingSalesPath = OptimizeWithSimulatedAnnealing.optimizeWithSimulatedAnnealing(travelingSalesPath, distances);
		
		optimization = optimization.toLowerCase();
		
		// Optimization switch case
		switch(optimization) {
		case "twoopt": {
			TwoOpt twoOpt = new TwoOpt(travelingSalesPath, distances, secondsToRunTwoOpt);
			travelingSalesPath = twoOpt.run();
			System.out.println("Inside Two Opt");
			break;
		}
		case "threeopt": {
			travelingSalesPath = TSPThreeOpt.threeOpt(travelingSalesPath, distances);
			System.out.println("Inside Three Opt");
			break;
		}
		case "aco": {
			ACOChristofides aco = new ACOChristofides(travelingSalesPath, distances);
			travelingSalesPath = aco.acoChristofides();
			System.out.println("Inside ACO Opt");
			break;
		}
		case "simopt": {
			travelingSalesPath = OptimizeWithSimulatedAnnealing.optimizeWithSimulatedAnnealing(travelingSalesPath, distances);
			System.out.println("Inside Simulated Opt");
			break;
		}

		}
		System.out.println(travelingSalesPath.size() + " final size");
		ChristofidesTour finalAnswer = finalAnswer(travelingSalesPath, distances, inputFilePath);
		benchmark.endTimer();
		System.out.println("Program took: " + benchmark.getResultTime() + " ms");
		return finalAnswer;
	}

//	private static List<Vertex> parseGraph(String fileName) throws IOException {
//
//		List<Vertex> ver = null;
//
//		FileInputStream fis = new FileInputStream(new File(fileName));
//		HSSFWorkbook workbook = new HSSFWorkbook(fis);
//		
//	
//		HSSFSheet worksheet = workbook.getSheetAt(0);
//		Iterator<Row> iterator = worksheet.iterator();
//
//		while (iterator.hasNext()) {
//
//			Row row = iterator.next();
//			Iterator<Cell> individualCell = row.cellIterator();
//
//			while (individualCell.hasNext()) {
//				Cell cell = individualCell.next();
//
//				int columnIndex = cell.getColumnIndex();
//				String id = null;
//				double actualId = 0;
//				double longtitude = 0, latitude = 0;
//
//				switch (columnIndex) {
//				case 0:
//					id = cell.getStringCellValue();
//					id = id.substring(id.length() - 5, id.length());
//
//					actualId = Integer.parseInt(id, 16);
//
//					break;
//
//				case 1:
//					longtitude = cell.getNumericCellValue();
//					break;
//
//				case 2:
//					latitude = cell.getNumericCellValue();
//					break;
//				}
//				
//				System.out.println("actualId: "+ actualId);
//				System.out.println("longtitude: "+longtitude);
//				System.out.println("latitude: "+latitude);
//
//				ver = (List<Vertex>) new Vertex(actualId, longtitude, latitude);
//				ver.add((Vertex) ver);
//				// ver.add(v.getID(), v.getX(), v.getY());
//				// ver.add((Vertex) v);
//			}
//		}
//
//		return ver;
//}

	// function calculates distances between all points on the graph
	private static double[][] getDistances(List<Vertex> graphList) {
		double[][] graphDistance = new double[graphList.size()][graphList.size()];
		for (int i = 0; i < graphList.size(); i++) {
			for (int j = 0; j < graphList.size(); j++) {
				
				graphDistance[i][j] = calculateDistance(graphList.get(i), graphList.get(j));
				
			}
		}
		return graphDistance;
	}

	// function that calculates the difference in location using A^2 + B^2 = C^2
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
	     
	     double distance = 6371 * distRadC * 1000; // Convert to meters
	     
//		double difference = (Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2)));
		return distance;
	}

	/*
	 * This splits even and odds from each other and then creates a
	 * "perfect matching" (it doesn't actually, but attempts something close to),
	 * and then reconnects the graph. Please note this creates a Eulerian Multigraph
	 * which means edges can be connected to each other twice. 2 edges "A-B" "A-B"
	 * can exist from the same main.java.Vertex.
	 */
	private static List<Vertex> createEvenlyVertexedEularianMultiGraphFromMST(List<Vertex> minimumSpanningTree,
			double[][] distances) {

		List<Vertex> oddVertices = minimumSpanningTree.stream()
				.filter(vertex -> vertex.getConnectedVertices.size() % 2 == 1)
				.collect(Collectors.toCollection(ArrayList::new));

		while (!oddVertices.isEmpty()) {
			double distance = Double.MAX_VALUE;
			final Vertex parent = oddVertices.get(0);

			// Compare pointers to not use root node.
			double minDistanceToNextNode = oddVertices.stream().mapToDouble(vertex -> vertex == parent ? Double.MAX_VALUE
					: distances[(int) parent.getID()][(int) vertex.getID()]).min().getAsDouble();

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

	private static ChristofidesTour finalAnswer(List<Vertex> TSP, double[][] distances, String p)
			throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter((p + ".tour"))) {
			int lineFormatting = 0;
			for (Vertex vertex : TSP) {
//				System.out.println(vertex.getVertexID());
				if (lineFormatting == 20) {
					lineFormatting = 0;
					System.out.println();
				}
				lineFormatting++;
			}
			System.out.println();

			double totalDistance = TSP.stream()
					.mapToDouble(vertex -> TSP.indexOf(vertex) == TSP.size() - 1
							? distances[(int) TSP.get(0).getID()][(int) TSP.get(TSP.size() - 1).getID()] // logic to
																											// connect
																											// end node
																											// to start
																											// node.
							: distances[(int) vertex.getID()][(int) TSP.get(TSP.indexOf(vertex) + 1).getID()])
					.sum();

			System.out.println("Total distance covered of the " + TSP.size() + " vertices is: " + totalDistance);
			writer.println(totalDistance);

			List<String> finalTour = TSP.stream().map(vertex -> {
				writer.println(vertex.getVertexID());
				return vertex.getVertexID();
			}).collect(Collectors.toCollection(ArrayList::new));
			
			//Visualization code
			
//			ArrayList<Vertex> finalList = (ArrayList<Vertex>) TSP;
//			System.out.println(finalList.size());
//			GraphPanel graphPanel = new GraphPanel(finalList);
//			JFrame frame = new JFrame("Graph Drawing Example with GUI");
//	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	        frame.setSize(2000, 2000);
//	        frame.add(graphPanel);
//	        frame.setVisible(true);
			
			// Create the TSPVisualization panel
	        TSPVisualization panel = new TSPVisualization(TSP);
	
	        // Create a JFrame to display the TSPVisualization panel
	        JFrame frame = new JFrame("TSP Visualization");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.add(panel);
	        frame.setSize(2000, 2000);
	        frame.setVisible(true);
	        
			return new ChristofidesTour(finalTour, totalDistance);
		}
	}

	private static List<Vertex> parseGraph(String fileName) throws IOException {

		List<Vertex> vertexList = new ArrayList<Vertex>();
		int i = 0;

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
				id = id.substring(id.length() - 5, id.length());
				//System.out.println("Id: "+id);

//				actualId = Integer.parseInt(id, 16);
				
				actualId = id;

				longtitude = Double.parseDouble(data[1]);
				
				latitude = Double.parseDouble(data[2]);
			
			

			
			//System.out.println(data[0]+"|"+data[1]+"|"+data[2]);
			//System.out.println(actualId+"|"+longtitude+"|"+latitude);
			
		//Vertex	ver1 = (List<Vertex>) new Vertex(actualId, longtitude, latitude);
//		ver.add((Vertex) ver);
			Vertex v1 = new Vertex(i++, actualId,  longtitude, latitude);
			vertexList.add(v1);
			
			}
			readFile.close();
			} catch (FileNotFoundException e) {
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
			}

		//System.out.println("Array Size: "+ver.size());
		return vertexList;
}
}
