package main;

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

public class SimmulatedAnnealingTest {

	private static final String INPUT_FILENAME = "crimeSample500.csv";
	private static final String OUTPUT_FILENAME = "crimeSample500.csv.tour";


	@Test
	public void newTourVertices() throws IOException {
		ChristofidesTour christofidesTour = finalAnswer(OUTPUT_FILENAME);
		ChristofidesTour twoOptTour = mainProgram.christofidesAlgorithm(INPUT_FILENAME, 2, "simOpt");
		assertEquals("The number of vertices doesn't match", christofidesTour.getFinalTour().size(), twoOptTour.getFinalTour().size());
	}

	//Checking if all vertices are visited
	@Test
	public void checkAllVertices() throws IOException {

		ChristofidesTour christofidesTour = finalAnswer(OUTPUT_FILENAME);
		ChristofidesTour twoOptTour = mainProgram.christofidesAlgorithm(INPUT_FILENAME, 2, "simOpt");

		for(String tour : christofidesTour.getFinalTour()) {
			assertTrue("The vertex : "+tour+ " doesn't exist",twoOptTour.getFinalTour().contains(tour));
		}

	}

	//Checking if the distance calculated using Chritofides algo is less than Simmulated Annealing algo
	@Test
	public void compareDistance() throws IOException {

		ChristofidesTour oldChristofidesTour = finalAnswer(OUTPUT_FILENAME);
		ChristofidesTour newChristofidesTour = mainProgram.christofidesAlgorithm(INPUT_FILENAME, 2, "simOpt");
		System.out.println("New optimized distance: "+newChristofidesTour.getTourCost());
		System.out.println("Old optimized distance: "+oldChristofidesTour.getTourCost());
		assertTrue("The optimized distance should be less than the Chritofides distance", newChristofidesTour.getTourCost() < oldChristofidesTour.getTourCost() );

	}

	private static ChristofidesTour finalAnswer(String fileName) throws IOException {
		try(Stream<String> stream = Files.lines(Paths.get(fileName))) {
			List<String>  finalTour = stream
					.collect(Collectors.toCollection(ArrayList::new));
			double totalDistance = Double.parseDouble(finalTour.get(0));
			System.out.println("Total distance before opt : "+totalDistance);
			finalTour.remove(0);
			return new ChristofidesTour(finalTour, totalDistance);
		}
	}
}