package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.*;

import javax.swing.JPanel;

class GraphPanel extends JPanel {
    private ArrayList<Vertex> vertices;

    public GraphPanel(ArrayList<Vertex> TSP) {
        vertices = TSP;
    }

//    public void addVertex(Vertex vertex) {
//        vertices.add(vertex);
//    }

//    public void addEdge(Vertex source, Vertex destination) {
//        source.addNeighbor(destination);
//    }
    
//    public void addEdge(Vertex source, Vertex destination) {
//        source.addNeighbor(destination);
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
//        List<Vertex> neigbors;
        for (Vertex vertex : vertices) {
        	double x = vertex.getLongitude();
        	double y = vertex.getLatitude();
        	System.out.println(x + "x");
        	System.out.println(y + "y");
        	g2d.setColor(Color.BLUE);
//        	g2d.fillOval( x-5,  y-5 ,75, 75);
        	g2d.setColor(Color.WHITE);
//        	g2d.drawString(String.valueOf(vertex.getVertexID()), (int) x+20, (int) y+30);
        	g2d.drawString(String.valueOf(vertex.getVertexID()),  (float) x, (float) y);
//            ArrayList<Vertex> neighbors = vertex.getNeighbors();
            System.out.println(vertex.edge.getChild().getVertexID() + " edge child");
            ArrayList<Vertex> neighbors = (ArrayList<Vertex>) vertex.getPathTo(vertex.edge.getChild(), vertices.size());
            System.out.println(neighbors.size() + " size of neighbors");
//            for (Vertex neighbor : neighbors) {
//                double neighborX =  neighbor.getX();
//                double neighborY =  neighbor.getY();
//                g2d.setColor(Color.BLACK);
////                g.drawLine(x + 25, y + 25, neighborX + 25, neighborY + 25);
////                g2d.draw(new Line2D.Double(x, y, neighborX, neighborY));
//            }
        }
    }
    
//    public void yourDrawingMethod(Graphics gg)
//    {
//        /* Cast it to Graphics2D */
//        Graphics2D g = (Graphics2D) gg;
//
//        /* Enable anti-aliasing and pure stroke */
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
//
//        /* Construct a shape and draw it */
//        Ellipse2D.Double shape = new Ellipse2D.Double(0.5, 0.5, 50, 50);
//        g.draw(shape);
//    }
}