package main;

import java.util.List;

public class ChristofidesTour {
    private List<String> tour;
    private double tourCost;

    public ChristofidesTour(List<String> tour2, double tourCost ) {
        this.tour = tour2;
        this.tourCost = tourCost;
    }

    public ChristofidesTour() {
    }
    public List<String> getFinalTour() {
        return tour;
    }

    public void setFinalTour(List<String> tour) {
        this.tour = tour;
    }

    public double getTourCost() {
        return tourCost;
    }

    public void setTourCost(int tourCost) {
        this.tourCost = tourCost;
    }
}