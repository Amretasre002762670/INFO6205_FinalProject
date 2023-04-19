package com.neu.psa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DuplicateHelper {

	private DuplicateHelper() {
	}

	// Used for preserving the order of the elements which are encountered first
	public static List<Vertex> run(List<Vertex> tour) {
		return new ArrayList<>(tour).stream().distinct().collect(Collectors.toCollection(ArrayList::new));
	}
}
