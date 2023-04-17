package main;

import java.util.LinkedList;
import java.util.List;

public class HierholzerAlgorithm {
    private HierholzerAlgorithm() {
    }

    public static List<Vertex> run(List<Vertex> vertexList) {
        List<Vertex> path1;
        List<Vertex> currentTour = new LinkedList<>(vertexList);
        path1 = new LinkedList<>();
        while (currentTour.get(0).getConnectedVertices.size() != 0) {
            List<Vertex> returnedPath = returnAPath(currentTour.get(0));
            path1 = runHelper(returnedPath);
        }
        return path1;
    }

    private static List<Vertex> runHelper(List<Vertex> path1) {
        for (int i = 0; i < path1.size(); i++) {
            if (path1.get(i).getConnectedVertices.size() > 0) {
                List<Vertex> addToPath;
                addToPath = returnAPath(path1.get(i));
                int indexSaved = i + 1;
                int addPathSize = addToPath.size();
                for (int j = 1; j < addPathSize; j++) {
                	path1.add(indexSaved, addToPath.get(0));
                    addToPath.remove(0);
                }
            }
        }

        return path1;
    }


    private static List<Vertex> returnAPath(Vertex startingPath) {
        List<Vertex> returnValue = new LinkedList<>();
        Vertex pathFinish = null;
        Vertex firstNode = startingPath;
        while (firstNode != pathFinish) {
            pathFinish = startingPath.getConnectedVertices.get(0).childVertex;
            startingPath.getConnectedVertices.remove(0);
            //remove the parent edge from child node
            for (int i = 0; i < pathFinish.getConnectedVertices.size(); i++) {
                if (pathFinish.getConnectedVertices.get(i).childId == startingPath.getID()) {
                    pathFinish.getConnectedVertices.remove(i);
                    break;
                }
            }
            returnValue.add(startingPath);
            startingPath = pathFinish;
        }
        returnValue.add(pathFinish); //This needs to be added to "complete" the loop.
        return returnValue;
    }

}