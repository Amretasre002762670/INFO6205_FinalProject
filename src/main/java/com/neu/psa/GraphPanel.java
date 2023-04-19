package com.neu.psa;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

//This class is used for setting the color of the edges and displaying crimeIds on the plotted graph
class GraphPanel extends JPanel {
	private ArrayList<Vertex> vertices;

	public GraphPanel(ArrayList<Vertex> TSP) {
		vertices = TSP;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		for (Vertex vertex : vertices) {
			double longtitude = vertex.getLongitude();
			double latitude = vertex.getLatitude();
			System.out.println(longtitude + "Longtitude");
			System.out.println(latitude + "Latitude");

			// Sets color for the connected edges and Crime Id
			g2d.setColor(Color.BLUE);
			g2d.setColor(Color.WHITE);

			// Displays the crimeId of all the vertices
			g2d.drawString(String.valueOf(vertex.getVertexID()), (float) longtitude, (float) latitude);
			System.out.println(vertex.edge.getChild().getVertexID() + " edge child");

			ArrayList<Vertex> neighbors = (ArrayList<Vertex>) vertex.getPathTo(vertex.edge.getChild(), vertices.size());

			System.out.println(neighbors.size() + " size of neighbors");
		}
	}

}