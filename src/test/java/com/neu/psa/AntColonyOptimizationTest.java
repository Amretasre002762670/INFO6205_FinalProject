package com.neu.psa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class AntColonyOptimizationTest {
	private static final String INPUT_FILENAME = "crimeSample500.csv";
	private static final String OUTPUT_FILENAME = "crimeSample500.csv.tour";

	// Checking the size (number of vertices)
	@Test
	public void newTourVertices() throws IOException {
		ChristofidesTour christofidesTour = finalAnswer(OUTPUT_FILENAME);
		ChristofidesTour acoTour = TSPMainProgram.christofidesAlgorithm(INPUT_FILENAME, 2, "aco");
		assertEquals("The number of vertices doesn't match", christofidesTour.getFinalTour().size(),
				acoTour.getFinalTour().size());
	}

	// Checking if all vertices are visited
	@Test
	public void checkAllVertices() throws IOException {

		ChristofidesTour christofidesTour = finalAnswer(OUTPUT_FILENAME);
		ChristofidesTour acoTour = TSPMainProgram.christofidesAlgorithm(INPUT_FILENAME, 2, "aco");

		for (String tour : christofidesTour.getFinalTour()) {
			assertTrue("The vertex : " + tour + " doesn't exist", acoTour.getFinalTour().contains(tour));
		}

	}

	// Checking if the distance calculated using Christofides algorithm is less than
	// 2-Opt algorithm
	@Test
	public void compareDistance() throws IOException {

		ChristofidesTour oldChristofidesTour = finalAnswer(OUTPUT_FILENAME);
		ChristofidesTour newChristofidesTour = TSPMainProgram.christofidesAlgorithm(INPUT_FILENAME, 2, "aco");
		assertTrue("The optimized distance should be less than the Chritofides distance",
				newChristofidesTour.getTourCost() < oldChristofidesTour.getTourCost());

	}

	private static ChristofidesTour finalAnswer(String fileName) throws IOException {
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			List<String> finalTour = stream.collect(Collectors.toCollection(ArrayList::new));
			double totalDistance = Double.parseDouble(finalTour.get(0));
			finalTour.remove(0);
			return new ChristofidesTour(finalTour, totalDistance);
		}
	}

}
