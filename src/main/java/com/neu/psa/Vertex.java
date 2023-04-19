package com.neu.psa;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
	private int id;
	private String vertexId;
	private double xVar;
	private double yVar;
	public ArrayList<Edge> getConnectedVertices;
	public Edge edge; // used for prims algorithm

	boolean evenEdge;

	public Vertex(int id, String vertexId, double xVar, double yVar) {
		this.id = id;
		this.vertexId = vertexId;
		this.xVar = xVar;
		this.yVar = yVar;
		this.getConnectedVertices = new ArrayList<>();
		this.edge = null;
	}

	int getID() {
		return id;
	}

	String getVertexID() {
		return vertexId;
	}

	double getLongitude() {
		return xVar;
	}

	double getLatitude() {

		return yVar;
	}

	public double getEdgeWeight() {
		return edge.getWeight();
	}

	public List<Vertex> getPathTo(Vertex other, int n) {
		List<Vertex> path = new ArrayList<>();
		if (id < other.id) {
			for (int i = id + 1; i <= other.id; i++) {
				path.add(new Vertex(i, other.getVertexID(), other.getLongitude(), other.getLatitude())); 
																											
			}
		} else {
			for (int i = id + 1; i <= n; i++) 
				path.add(new Vertex(i, other.getVertexID(), other.getLongitude(), other.getLatitude()));
																											
			
			for (int i = 1; i <= other.id; i++) 
				path.add(new Vertex(i, other.getVertexID(), other.getLongitude(), other.getLatitude())); 
			
		}
		return path;
	}

}