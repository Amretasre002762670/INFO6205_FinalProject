package com.neu.psa;

import java.util.Comparator;

public class VertexComparator implements Comparator<Vertex> {

	public int compare(Vertex vertex1, Vertex vertex2) {
		if (vertex1.edge.weight > vertex2.edge.weight) 
			return 1;
		 else if (vertex1.edge.weight < vertex2.edge.weight) 
			return -1;
		else 
			return 0;
		
	}

}
