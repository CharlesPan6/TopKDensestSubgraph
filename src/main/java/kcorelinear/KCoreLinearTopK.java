package kcorelinear;


import input.StreamEdge;

import java.util.ArrayList;
import java.util.HashSet;

import main.DensestSubgraph;
import output.Output;
import struct.DegreeMap;
import struct.NodeMap;

public class KCoreLinearTopK implements DensestSubgraph{
	int k ;
	public KCoreLinear densest;
	
	public void setDensest(KCoreLinear densest) {
		this.densest = densest;
	}

	public KCoreLinearTopK(int k, NodeMap nodeMap) {
		this.k = k;
		densest = new KCoreLinear(nodeMap.map);
	}

	
	@Override
	public ArrayList<Output> getDensest(DegreeMap degreeMap, NodeMap nodeMap) {
		ArrayList<Output> list = new ArrayList<Output>();
		ArrayList<StreamEdge> removedEdges = new ArrayList<StreamEdge>();
		ArrayList<Output> out = null;
		
		for(int i =0 ; i< k; i++) {
			out = densest.getDensest(null, nodeMap);
			list.add(out.get(0));
			removeBulk(nodeMap, out.get(0), removedEdges);
			if(nodeMap.getNumNodes() == 0) {
				addRemovedEdges(removedEdges, nodeMap);
				return list;
			}
		}
		addRemovedEdges(removedEdges, nodeMap);
		
		return list;
	}
	
	void addRemovedEdges(ArrayList<StreamEdge> removedEdges, NodeMap nodeMap) {
		for(StreamEdge edge: removedEdges) {
			nodeMap.addEdge(edge.getSource(), edge.getDestination());
			nodeMap.addEdge(edge.getDestination(), edge.getSource());
			densest.addEdge(edge.getSource(), edge.getDestination());
		}
	}
 	void removeBulk(NodeMap nodeMap, Output out, ArrayList<StreamEdge> removedEdges) {
 		ArrayList<String> nodes = out.getNodes();
		for(String node:nodes) {
			HashSet<String > temp = nodeMap.getNeighbors(node);
			ArrayList<String> neighbors;
			if( temp != null)
				neighbors = new ArrayList<String>(nodeMap.getNeighbors(node));
			else 
				neighbors = new ArrayList<String>();
			for(String neighbor: neighbors) {
				removedEdges.add(new StreamEdge(node,neighbor));
				nodeMap.removeEdge(node, neighbor);
				nodeMap.removeEdge(neighbor, node);
				densest.removeEdge(node, neighbor);
			}
		}
	}
	
	
}