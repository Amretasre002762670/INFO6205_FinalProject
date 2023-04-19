package com.neu.psa;

public class Edge {

	Vertex ownerVertex;
	Vertex childVertex;
	double parentId;
	double childId;
	double weight;

	public Edge(Vertex parentEdge, Vertex childEdge, double distance) {

		ownerVertex = parentEdge;
		childVertex = childEdge;
		weight = distance;
		parentId = parentEdge.getID();
		childId = childEdge.getID();
	}

	public Vertex getOwner() {
		return ownerVertex;
	}

	public Vertex getChild() {
		return childVertex;
	}

	public double getWeight() {
		return weight;
	}
}
