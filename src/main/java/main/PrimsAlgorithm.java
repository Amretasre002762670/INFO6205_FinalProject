package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PrimsAlgorithm {

	private PrimsAlgorithm()
	{
	}
	
/*
1: Initialize a tree with a single vertex, chosen arbitrarily from the graph. The vertex choosen in unsortedList(index 0)
2: Grow the tree by one edge: of the edges that connect the tree to vertices not yet in the tree, 
find the minimum-weight edge, and transfer it to the tree.
3: Repeat step 2 (until all vertices are in the tree).
* */	
public static List<Vertex>  run(List<Vertex> unsortedVertices, double[][] distances)
{
	Comparator<Vertex> vertexComparator = new VertexComparator();

//This will initialize all vertex shortest edge to the initial base vertex. This is important because it creates a new edge for all
// vertices. It is required as it seeds the next part of the algorithm (the while loop)
// It satisfies the "Initialize of the tree with a single vertex" part of Prim's Algorithm.
	final Vertex sourceVertex = unsortedVertices.get(0);
	unsortedVertices
			.stream()
			.forEach(vertex -> vertex.edge = new Edge(vertex, sourceVertex, distances[vertex.getID()][ sourceVertex.getID()]));
	Collections.sort(unsortedVertices, vertexComparator);

//This implements prim's algorithm. It "pops" off and adds to the tree the minimum edge and then 
//sorts and checks each edge to make sure it is the smallest.
//Sort is run BEFORE the new weights are assigned to insure that the new graph will be connected.
//This is mainly because the initial initialization happens before the while loop.
	List<Vertex> sortedVertices     = new ArrayList<>();
	while(!unsortedVertices.isEmpty())
	{
		Vertex pointConnected = unsortedVertices.get(0);
		pointConnected.getConnectedVertices.add(pointConnected.edge);

		//The following adds a childEdge helping the graph stay connected.
	    Edge parentEdge = pointConnected.edge;

	    if(sortedVertices.isEmpty())
	    {
			//special case for root node
			Vertex baseVertex = unsortedVertices.get(0);
			baseVertex.getConnectedVertices.remove(0);
			Vertex firstVertex = unsortedVertices.get(1);
			Edge updateEdge = firstVertex.edge;
			baseVertex.edge.parentId = baseVertex.getID();
			baseVertex.edge.childId = updateEdge.parentId;
			baseVertex.edge.ownerVertex = baseVertex;
			baseVertex.edge.childVertex = unsortedVertices.get(1);
			baseVertex.edge.weight = updateEdge.weight;
			baseVertex.getConnectedVertices.add(baseVertex.edge);
			Edge childEdge = new Edge(firstVertex, baseVertex, baseVertex.edge.weight);
			firstVertex.getConnectedVertices.add(childEdge);
	    }
	    else
	    {
	    	sortedVertices.forEach(vertex -> {
				if(parentEdge.childId == vertex.getID()) {
					Edge childEdge = new Edge(parentEdge.childVertex, parentEdge.ownerVertex, parentEdge.weight);
					vertex.getConnectedVertices.add(childEdge);
				}
			});
	    }

	    sortedVertices.add(pointConnected);
	    unsortedVertices.remove(pointConnected);
		Collections.sort(unsortedVertices, vertexComparator); //lowest weight connected to MST comes to the top

		if (!unsortedVertices.isEmpty()) {
			Vertex sourcedVertex = unsortedVertices.get(0);
			unsortedVertices.stream()
					.forEach(vertex -> {
				if (vertex != sourcedVertex) {
					double checkNewWeight = distances[(int) vertex.getID()][(int) sourcedVertex.getID()];
					if (checkNewWeight < vertex.edge.weight) {
						vertex.edge.childId = sourcedVertex.getID();
						vertex.edge.childVertex = sourcedVertex;
						vertex.edge.weight = checkNewWeight;
					}
				}
			});
		}
	}
	return sortedVertices;
}


}
