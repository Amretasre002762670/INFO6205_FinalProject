package com.neu.psa;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TSPVisualization extends JPanel {
    private List<Vertex> vertices;
    private List<Edge> tour;

    public TSPVisualization(List<Vertex> vertices) {
        this.vertices = vertices;
        this.tour = new ArrayList<>();
        for (int i = 0; i < vertices.size() - 1; i++) {
            tour.add(new Edge(vertices.get(i), vertices.get(i + 1), vertices.get(i).getEdgeWeight()));
        }
        
        //Add edge connecting last vertex to first vertex to complete the tour
       tour.add(new Edge(vertices.get(vertices.size() - 1), vertices.get(0), vertices.get(0).getEdgeWeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set up the canvas size
        int canvasWidth = getWidth() - 100;
        int canvasHeight = getHeight() - (40);

        // Determine the range of longitude and latitude values
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (Vertex vertex : vertices) {
            double longitude = vertex.getLongitude();
            double latitude = vertex.getLatitude();
            if (longitude < minX) {
                minX = longitude;
            }
            if (longitude > maxX) {
                maxX = longitude;
            }
            if (latitude < minY) {
                minY = latitude;
            }
            if (latitude > maxY) {
                maxY = latitude;
            }
        }

        // Calculate the scaling factors for longitude and latitude
        double scaleX = canvasWidth / (maxX - minX);
        double scaleY = canvasHeight / (maxY - minY);

        // Draw the tour edges
        g.setColor(Color.BLUE);
        for (Edge edge : tour) {
            Vertex v1 = edge.getOwner();
            Vertex v2 = edge.getChild();
            int x1 = (int) ((v1.getLongitude() - minX) * scaleX);
            int y1 = (int) ((maxY - v1.getLatitude()) * scaleY);
            int x2 = (int) ((v2.getLongitude() - minX) * scaleX);
            int y2 = (int) ((maxY - v2.getLatitude()) * scaleY);
            g.drawLine(x1, y1, x2, y2);
        }

        // Draw the vertices
        g.setColor(Color.RED);
        for (Vertex vertex : vertices) {
            int x = (int) ((vertex.getLongitude() - minX) * scaleX);
            int y = (int) ((maxY - vertex.getLatitude()) * scaleY);
            g.fillOval(x - 5, y - 5, 10, 10);
        }
        g.setColor(Color.BLUE);
        for (Vertex vertex : vertices) {
            int x = (int) ((vertex.getLongitude() - minX) * scaleX);
            int y = (int) ((maxY - vertex.getLatitude()) * scaleY);

            
            g.drawString(String.valueOf(vertex.getVertexID()),  x+5, y+5);
        }
    }

}

