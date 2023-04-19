package com.neu.psa;

import java.util.LinkedList;
import java.util.List;

public class HierholzerAlgorithm {

	private HierholzerAlgorithm() {
	}

	// Returns List<Vertex> based on the input i.e. Minimum Spanning Tree
	public static List<Vertex> runHierholzerAlgorithm(List<Vertex> vertexList) {

		List<Vertex> pathBetweenVertex;
		List<Vertex> oldTour = new LinkedList<>(vertexList);

		pathBetweenVertex = new LinkedList<>();
		while (oldTour.get(0).getConnectedVertices.size() != 0) {
			List<Vertex> returnedPath = returnPathBetweenVertex(oldTour.get(0));
			pathBetweenVertex = run(returnedPath);
		}
		return pathBetweenVertex;
	}

	private static List<Vertex> run(List<Vertex> oldPath) {

		for (int i = 0; i < oldPath.size(); i++) {
			if (oldPath.get(i).getConnectedVertices.size() > 0) {
				List<Vertex> newPath;
				newPath = returnPathBetweenVertex(oldPath.get(i));

				int count = i + 1;
				int newPathSize = newPath.size();

				for (int j = 1; j < newPathSize; j++) {
					oldPath.add(count, newPath.get(0));
					newPath.remove(0);
				}
			}
		}

		return oldPath;
	}

	private static List<Vertex> returnPathBetweenVertex(Vertex oldVertex) {

		Vertex pathFinish = null;
		List<Vertex> newVertex = new LinkedList<>();
		Vertex firstVertex = oldVertex;

		while (firstVertex != pathFinish) {
			pathFinish = oldVertex.getConnectedVertices.get(0).childVertex;
			oldVertex.getConnectedVertices.remove(0);

			// Loop is used for removing parent edge from the child edge
			for (int i = 0; i < pathFinish.getConnectedVertices.size(); i++) {
				if (pathFinish.getConnectedVertices.get(i).childId == oldVertex.getID()) {
					pathFinish.getConnectedVertices.remove(i);
					break;
				}
			}
			newVertex.add(oldVertex);
			oldVertex = pathFinish;
		}

		newVertex.add(pathFinish);
		return newVertex;
	}

}