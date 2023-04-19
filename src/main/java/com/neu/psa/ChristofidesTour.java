package com.neu.psa;

import java.util.List;

public class ChristofidesTour {

	private List<String> christofidesTour;
	private double tourCost;

	public ChristofidesTour(List<String> tour, double tourCost) {
		this.christofidesTour = tour;
		this.tourCost = tourCost;
	}

	public ChristofidesTour() {
	}

	public List<String> getFinalTour() {
		return christofidesTour;
	}

	public double getTourCost() {
		return tourCost;
	}

	public void setFinalTour(List<String> tour) {
		this.christofidesTour = tour;
	}

	public void setTourCost(double tourCost) {
		this.tourCost = tourCost;
	}
}