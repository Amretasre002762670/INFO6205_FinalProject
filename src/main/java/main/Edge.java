package main;

public class Edge {

	double parentId;
	double childId;
	double weight;
	Vertex ownerVertex;
	Vertex childVertex;

	public Edge(Vertex parentEdge, Vertex childEdge, double distance) {
		parentId = parentEdge.getID();
		childId = childEdge.getID();
		weight = distance;
		ownerVertex = parentEdge;
		childVertex = childEdge;
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
