/*
 * Copyright (c) 2014 Peter Lager
 * <quark(a)lagers.org.uk> http:www.lagers.org.uk
 * 
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it freely,
 * subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented;
 * you must not claim that you wrote the original software.
 * If you use this software in a product, an acknowledgment in the product
 * documentation would be appreciated but is not required.
 * 
 * 2. Altered source versions must be plainly marked as such,
 * and must not be misrepresented as being the original software.
 * 
 * 3. This notice may not be removed or altered from any source distribution.
 */

/**
 * 
 */
package game2dai.graph;

import game2dai.maths.Geometry2D;
import game2dai.utils.GraphSAXParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;

/**
 * 
 * Objects of this class represents graphs that can be used in games. <br>
 * 
 * The class maintains collections of nodes (vertices) and directed edges. <br> 
 * 
 * Each node should have a unique ID number, attempting to add a node which the 
 * same ID as a node already added to the graph will replace the existing node. <br>
 * 
 * An edge is specified by the id numbers to the 2 nodes that are to be joined. Each
 * edge is directed i.e. one-way, so to create a bidirectional (two-way) link between
 * the nodes requires two edges to be created. This does have the advantage that each
 * the cost of travelling between 2 nodes does not have to be the same in both 
 * directions. <br>
 * 
 * It is more efficient to add all the nodes first and then the edges but not essential. <br>
 * 
 * Attempting to add an edge where one or both of the connecting nodes do not yet exist
 * in the graph will be 'remembered' - this is called a floating edge. Once both nodes 
 * have been added to the graph then the floating edge will also be added to the graph. <br>
 * Floating edges are segregated from the graph edges to simply the graph searching 
 * algorithms. <br><br>
 * 
 * This arrangement is very flexible and can simplify the code needed to create the graph
 * at the expense of creating large numbers of floating edges that will never be added
 * to the graph. Once you have created the final graph it is recommended that the user
 * calls the compact method which simply deletes any floating edges and requests a 
 * garbage collection to release the memory. <br><br>
 * 
 * The classes
 * @see	    GraphNode
 * @see		GraphEdge
 * are the base classes used to store nodes and edges. These classes support
 * inheritance so you can provide more specialised classes for your graphs. <br>
 * 
 * The following classes can be used to search the graph.
 * 
 * @see	    GraphSearch_DFS
 * @see		GraphSearch_BFS
 * @see		GraphSearch_Dijkstra
 * @see		GraphSearch_Astar
 * 
 * <br>
 * 
 * @author Peter Lager
 */
public class Graph  {

	public static Graph makeFromXML(PApplet app, String xmlFilename){
		return new GraphSAXParser(app, xmlFilename).get();
	}

	public static Graph makeFromXML(String xmlFilename){
		return new GraphSAXParser(xmlFilename).get();
	}

	public static Graph makeFromXML(File xmlFile){
		return new GraphSAXParser(xmlFile).get();
	}

	// Data structures to hold nodes and edges
	protected HashMap<Integer, GraphNode> nodes;
	protected HashMap<GraphNode, LinkedList<GraphEdge>> edgeLists;
	// Data structure to hold floating edges.
	protected HashMap<Integer, LinkedList<FloatingEdge>> nodesToBe;

	/**
	 * Create a graph with an initial capacity of 16 nodes.
	 */
	public Graph(){
		this(16);
	}

	/**
	 * Create a graph with an initial capacity based on an estimate of 
	 * the number of nodes to be added.
	 * @param nbrNodes
	 */
	public Graph(int nbrNodes){
		nodes = new HashMap<Integer, GraphNode>(nbrNodes);
		edgeLists = new HashMap<GraphNode, LinkedList<GraphEdge>>(nbrNodes);
	}

	/**
	 * Gets the world distance between 2 nodes.
	 * @param nodeFrom first node
	 * @param nodeTo second node
	 * @return distance between them or 0 if either node does not exist
	 */
	public double distance(GraphNode nodeFrom, GraphNode nodeTo){
		if(nodeFrom == null || nodeTo == null)
			return 0;
		return Geometry2D.distance(nodeFrom.x, nodeFrom.y, nodeTo.x, nodeTo.y);	
	}
	
	/**
	 * Get the distance between two nodes.
	 * @param nodeFromID ID of first node
	 * @param nodeToID ID of second node
	 * @return distance between them or 0 if either node does not exist
	 */
	public double distance(int nodeFromID, int nodeToID){
		return distance(nodes.get(nodeFromID),nodes.get(nodeToID));	
	}
	
	
	/**
	 * Add a node to the list. The user must ensure that the node id is unique.
	 * @param node
	 */
	public void addNode(GraphNode node){
		nodes.put(node.id(), node);
		if(nodesToBe != null)
			resolveFloatEdges(node);
	}

	/**
	 * This method is called every time a node is added to the graph. It will 
	 * update all floating edges and where possible add the resolved floating 
	 * edge to the graph. 
	 * @param node a node to be added to the graph (must not be null)
	 */
	protected void resolveFloatEdges(GraphNode node){
		int nodeID = node.id();
		LinkedList<FloatingEdge> elist = nodesToBe.get(nodeID);
		if(elist != null){
			Iterator<FloatingEdge> iter = elist.iterator();
			while(iter.hasNext()){
				FloatingEdge edge = iter.next();
				if(edge.fromID == nodeID)
					edge.from = node;
				else if(edge.toID == nodeID)
					edge.to = node;
				if(edge.from != null && edge.to != null){
					addValidEdge(new GraphEdge(edge.from, edge.to, edge.cost));
					iter.remove();
				}
			}
			// See if we have emptied the edgelist for this node id
			if(elist.isEmpty()){
				nodesToBe.remove(nodeID);
				// Edge list has been removed so see if there are
				// any more 'nodes to be' if not dump nodesToBe 
				if(nodesToBe.isEmpty())
					nodesToBe = null;
			}
		}
	}

	/**
	 * If the node exists remove it and all edges that start
	 * or end at this node.
	 * @param nodeID id of the node to remove
	 * @return true if the node was removed else false
	 */
	public boolean removeNode(int nodeID){
		GraphNode node = nodes.get(nodeID);
		if(node == null)
			return false;
		edgeLists.remove(node);	// remove edges from this node
		nodes.remove(nodeID);	// remove node
		// get a list of all edges that go to the node we just removed
		GraphEdge[] edges = getEdgeArray();
		ArrayList<GraphEdge> edgesToRemove = new ArrayList<GraphEdge>();
		for(int i = 0; i < edges.length; i++){
			if(edges[i].to().id() == nodeID)
				edgesToRemove.add(edges[i]);
		}
		// Now remove these edges.
		for(GraphEdge edge : edgesToRemove)
			edgeLists.get(edge.from).remove(edge);
		return true;
	}

	/**
	 * Get a node with a given id.
	 * 
	 * @param id
	 * @return the node if it exists else null
	 */
	public GraphNode getNode(int id){
		return nodes.get(id);
	}

	/**
	 * Does a node with a given id exist?
	 * @param id
	 * @return true if the node exists else false
	 */
	public boolean hasNode(int id){
		return nodes.get(id) != null;		
	}
	
	/**
	 * Locate and return the first node encountered that is within a
	 * stated distance of a position at [x,y,z]
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param maxDistance only consider a node that is with this distance of [x,y,z]
	 * @return the node if it meets the distance criteria else null
	 */
	public GraphNode getNodeNear(double x, double y, double z, double maxDistance){
		double d2 = maxDistance * maxDistance, dx2, dy2, dz2;
		Collection<GraphNode> c = nodes.values();
		for(GraphNode node : c){
			dx2 = (node.x() - x)*(node.x() - x);
			dy2 = (node.y() - y)*(node.y() - y);
			dz2 = (node.z() - z)*(node.z() - z);
			if(dx2+dy2+dz2 < d2)
				return node;
		}	
		return null;
	}

	/**
	 * Locate and return the first node encountered that is within a
	 * stated distance of a position at [x,y,0]
	 * 
	 * @param x
	 * @param y
	 * @param maxDistance only consider a node that is with this distance of [x,y,z]
	 * @return the node if it meets the distance criteria else null
	 */
	public GraphNode getNodeNear(double x, double y, double maxDistance){
		double d2 = maxDistance * maxDistance, dx2, dy2;
		Collection<GraphNode> c = nodes.values();
		for(GraphNode node : c){
			dx2 = (node.x() - x)*(node.x() - x);
			dy2 = (node.y() - y)*(node.y() - y);
			if(dx2+dy2 < d2)
				return node;
		}	
		return null;
	}

	/**
	 * Find the node nearest the node specified based on Euclidean distance between them.
	 * @param a_node the node to use as the origin.
	 * @return the nearest node found or null if none exists.
	 */
	public GraphNode getNodeNearest(GraphNode a_node){
		GraphNode nearestNode = null;
		if(a_node != null){
			Collection<GraphNode> allNodes = nodes.values();
			double d = Double.MAX_VALUE, dn;
			for(GraphNode node : allNodes){
				if(node != a_node){
					dn = Geometry2D.distance_sq(node.x, node.y, a_node.x, a_node.y);	
					if(dn < d){
						d = dn;
						nearestNode = node;
					}
				}
			}
		}
		return nearestNode;
	}
	
	/**
	 * Find the node nearest the node with the specified ID number based on Euclidean distance between them.
	 * @param id the ID number of the node to use as the origin
	 * @return the nearest node found or null if none exists.
	 */
	public GraphNode getNodeNearest(int id){
		return getNodeNearest(nodes.get(id));
	}
	
	/**
	 * Locate and return the node nearest a given position irrespective of the distance apart.
	 * @param x
	 * @param y
	 * @param z
	 * @return the nearest node
	 */
	public GraphNode getNodeNearest(double x, double y, double z){
		double d2 = Double.MAX_VALUE, dist;
		GraphNode nearest = null;
		Collection<GraphNode> c = nodes.values();
		for(GraphNode node : c){
			dist = (node.x() - x)*(node.x() - x) +
					(node.y() - y)*(node.y() - y) +
					(node.z() - z)*(node.z() - z);
			if(dist < d2){
				d2 = dist;
				nearest = node;				
			}
		}	
		return nearest;
	}

	/**
	 * Locate and return the node nearest a given position irrespective of the distance
	 *  apart in the x/y plane (ignores z).
	 * @param x
	 * @param y
	 * @return the nearest node
	 */
	public GraphNode getNodeNearest(double x, double y){
		double d2 = Double.MAX_VALUE, dist;
		GraphNode nearest = null;
		Collection<GraphNode> c = nodes.values();
		for(GraphNode node : c){
			dist = (node.x() - x)*(node.x() - x) +
					(node.y() - y)*(node.y() - y);
			if(dist < d2){
				d2 = dist;
				nearest = node;				
			}
		}	
		return nearest;
	}

	
	/**
	 * get the number of nodes in the graph
	 * 
	 */
	public int getNbrNodes(){
		return nodes.size();
	}

	/**
	 * Add a unidirectional edge to the graph.
	 * 
	 * @param fromID the ID number of the from node
	 * @param toID the ID number of the to node
	 * @param cost cost from > to
	 * @return true if the edge was added else false
	 */
	public boolean addEdge(int fromID, int toID, double cost){
		GraphNode fromNode = nodes.get(fromID);
		GraphNode toNode = nodes.get(toID);
		GraphEdge ge;
		if(fromNode != null && toNode != null){
			ge = new GraphEdge(fromNode, toNode ,cost);
			addValidEdge(ge);
			return true;
		}
		FloatingEdge floatEdge = new FloatingEdge(fromID, toID, fromNode, toNode, cost);
		if(fromNode == null)
			rememberFloatingEdge(fromID, floatEdge);
		if(toNode == null)
			rememberFloatingEdge(toID, floatEdge);
		return false;
	}

	/**
	 * Add bidirectional link with the costs indicated.
	 * 
	 * @param fromID the ID number of the from node
	 * @param toID the ID number of the to node
	 * @param costOutward cost from > to
	 * @param costInward cost to > from
	 * @return true if the edge was added else false
	 */
	public boolean addEdge(int fromID, int toID, double costOutward, double costInward){
		boolean added = false;
		added = addEdge(fromID, toID, costOutward);
		added &= addEdge(toID, fromID, costInward);
		return added;
	}

	/**
	 * This method is called to add a validated edge to the graph.
	 * @param edge the validated edge to add.
	 */
	protected void addValidEdge(GraphEdge edge){
		GraphNode fromNode = edge.from();
		LinkedList<GraphEdge> geList = edgeLists.get(fromNode);
		if(geList == null){
			geList = new LinkedList<GraphEdge>();
			edgeLists.put(fromNode, geList);
		}
		geList.add(edge);
	}

	/**
	 * This method is used to remember floating edges.
	 * @param id
	 * @param floatEdge
	 */
	protected void rememberFloatingEdge(int id, FloatingEdge floatEdge){
		if(nodesToBe == null)
			nodesToBe = new HashMap<Integer, LinkedList<FloatingEdge>>();
		if(!nodesToBe.containsKey(id))
			nodesToBe.put(id, new LinkedList<FloatingEdge>());
		nodesToBe.get(id).add(floatEdge);
	}

	//	public void unusedFloatingEdges(){
	//		System.out.println("Unresolved floating edges");
	//		int count = 0;
	//		if(nodesToBe != null){
	//			Collection<LinkedList<FloatingEdge>> c = nodesToBe.values();
	//			for(LinkedList<FloatingEdge> list : c){
	//				for(FloatingEdge fedge : list){
	//					System.out.println(fedge);
	//					count++;
	//				}
	//			}
	//		}
	//		System.out.println("======  " + count +"  ============================");
	//	}

	/**
	 * Clear out all remaining floating edges.
	 */
	public void compact(){
		if(nodesToBe != null){
			Collection<LinkedList<FloatingEdge>> c = nodesToBe.values();
			for(LinkedList<FloatingEdge> list : c)
				list.clear();
			nodesToBe.clear();
			nodesToBe = null;
			System.gc();	// request garbage collection
		}
	}

	/**
	 * Get the edge between 2 nodes. <br>
	 * If either node does not exist or there is no edge
	 * exists between them then the method returns null.
	 * @param fromID ID for the from node
	 * @param toID ID for the to node
	 * @return the edge or null if it doesn't exist
	 */
	public GraphEdge getEdge(int fromID, int toID){
		GraphNode fromNode = nodes.get(fromID);
		GraphNode toNode = nodes.get(toID);
		if(fromNode == null || toNode == null)
			return null;
		LinkedList<GraphEdge> edgeList = edgeLists.get(fromNode);
		if(edgeList == null)
			return null;
		for(GraphEdge ge : edgeList){
			if(ge.to() == toNode)
				return ge;
		}
		return null;		
	}

	/**
	 * Get the cost of traversing an edge between 2 nodes. <br>
	 * If either node does not exist or there is no edge
	 * exists between them then the method returns a value <0.
	 * @param fromID ID for the from node
	 * @param toID ID for the to node
	 * @return the edge or null if it doesn't exist
	 */
	public double getEdgeCost(int fromID, int toID){
		GraphNode fromNode = nodes.get(fromID);
		GraphNode toNode = nodes.get(toID);
		if(fromNode == null || toNode == null)
			return -1;
		LinkedList<GraphEdge> edgeList = edgeLists.get(fromNode);
		if(edgeList == null)
			return -1;
		for(GraphEdge ge : edgeList){
			if(ge.to() == toNode)
				return ge.getCost();
		}
		return -1;		
	}

	/**
	 * Remove an edge between 2 nodes. <br>
	 * This will delete the edge from one node to another
	 * but does not remove any return edge. <br>
	 * To remove a 'bidirectional route' between nodes
	 * 22 and 33 then you must call this method twice e.g.
	 * <code>
	 * graph.removeEdge(22, 33);
	 * graph.removeEdge(33, 22);
	 * </code>
	 * @param fromID ID for the from node
	 * @param toID ID for the to node
	 * @return true if an edge has been removed
	 */
	public boolean removeEdge(int fromID, int toID){
		GraphEdge ge = getEdge(fromID, toID);
		if(ge != null){
			GraphNode fromNode = nodes.get(fromID);
			edgeLists.get(fromNode).remove(ge);
			return true;
		}
		return false;
	}

	/**
	 * Sees whether the graph has this edge
	 * @param from node id of from-node
	 * @param to node if of to-node
	 * @return true if the graph has this node else false
	 */
	public boolean hasEdge(int from, int to){
		GraphNode fromNode = nodes.get(from);
		GraphNode toNode = nodes.get(to);
		if(fromNode != null && toNode != null){
			LinkedList<GraphEdge> geList = edgeLists.get(fromNode);
			Iterator<GraphEdge> iter = geList.iterator();
			while(iter.hasNext()){
				if(iter.next().to() == toNode)
					return true;
			}
		}
		return false;
	}

	/**
	 * Gets a list of GraphEdges leaving (from) this node. <br>
	 * Used by graph search classes.
	 * @param nodeID id of the node where the edges start from
	 * 
	 */
	public LinkedList<GraphEdge> getEdgeList(int nodeID){
		return edgeLists.get(nodes.get(nodeID));
	}

	/**
	 * Gets a list of GraphEdges leaving (from) this node. <br>
	 * Used by graph search classes.
	 * @param node the node where the edges start from
	 * 
	 */
	public LinkedList<GraphEdge> getEdgeList(GraphNode node){
		return edgeLists.get(node);
	}

	/**
	 * Will return an array of all the GraphEdges in the graph. <br>
	 * The type of each element in the array will be of type GraphEdge
	 *  
	 */
	public GraphEdge[] getEdgeArray(){
		return getEdgeArray(new GraphEdge[0]);
	}

	/**
	 * Will return an array of all the GraphEdges in the graph. <br>
	 * The type of each element in the array will be of type Object 
	 * if the parameter is null otherwise it is T (where T is GraphEdge
	 * or any class derived from GraphEdge.
	 * 
	 * @param <T>
	 * @param array a zero length array of GraphNode or any derived class.
	 */
	@SuppressWarnings("unchecked")
	public <T extends GraphEdge> T[] getEdgeArray(T[] array){
		if(array == null)
			array = (T[]) new Object[0];
		LinkedList<GraphEdge> edges = new LinkedList<GraphEdge>(); 
		Collection<LinkedList<GraphEdge>> c = edgeLists.values();
		for(LinkedList<GraphEdge> geList : c)
			edges.addAll(geList);
		return edges.toArray(array);
	}

	/**
	 * Will return an array of all the GraphEdges that start from the node. <br>
	 * The type of each element in the array will be of type GraphEdge
	 * 
	 * @param from the node where the edges start from
	 */
	public GraphEdge[] getEdgeArray(int from){
		return getEdgeArray(from, new GraphEdge[0]);
	}

	/**
	 * Will return an array of all the GraphEdges that start from the node. <br>
	 * The type of each element in the array will be of type Object
	 * if the parameter is null otherwise it is T (where T is GraphEdge
	 * or any class that extends GrahEdge.
	 * 
	 * @param <T>
	 * @param from the node where the edges start from
	 * @param array a zero length array of GraphNode or any derived class.
	 */
	@SuppressWarnings("unchecked")
	public <T extends GraphEdge> T[] getEdgeArray(int from, T[] array){
		if(array == null)
			array = (T[]) new Object[0];
		LinkedList<GraphEdge> edges = getEdgeList(from);
		return edges.toArray(array);
	}

	public int getNbrEdges(){
		int count = 0;
		Collection<LinkedList<GraphEdge>> c = edgeLists.values();
		for(LinkedList<GraphEdge> geList : c)
			count += geList.size();
		return count;
	}
	

	/**
	 * Will return an array of all the GraphNodes in the graph. <br>
	 * The type of each element in the array will be of type GraphNode
	 * 
	 */
	public GraphNode[] getNodeArray() {
		return getNodeArray(new GraphNode[0]);
	}

	/**
	 * Will return an array of all the GraphNodes in the graph. <br>
	 * The type of each element in the array will be of type Object
	 * if the parameter is null otherwise it is T (where T is GraphNode
	 * or any class that extends GraphNode.
	 * 
	 * @param <T>
	 * @param array a zero length array of GraphNode or any derived class.
	 */
	@SuppressWarnings("unchecked")
	public <T extends GraphNode> T[] getNodeArray(T[] array) {
		if(array == null)
			array = (T[]) new Object[0];
		Collection<GraphNode> c = nodes.values();
		return c.toArray(array);
	}

	/**
	 * Used for debugging only.
	 */
	public String toString(){
		LinkedList<GraphEdge> edgeList;
		StringBuilder s = new StringBuilder("========= Graph ==============");
		LinkedList<GraphNode> n = new LinkedList<GraphNode>();
		n.addAll(nodes.values());
		Collections.sort(n);
		//Collection<Integer> k = nodes.keySet();
		for(GraphNode node : n){
			s.append("\n\t" + node);
			edgeList = edgeLists.get(node);
			if(edgeList != null) {
				for(GraphEdge edge : edgeList)
					s.append("\n\t\t" + edge);
			}
		}
		s.append("\n===================================\n");
		return new String(s);
	}

	/**
	 * Inner class to represent floating edges.
	 * 
	 * @author Peter Lager
	 *
	 */
	private class FloatingEdge {

		public int fromID = -1;
		public int toID = -1;
		public GraphNode from;
		public GraphNode to;
		public double cost = 1.0;

		/**
		 * @param fromID
		 * @param toID
		 * @param from
		 * @param to
		 * @param cost
		 */
		public FloatingEdge(int fromID, int toID, GraphNode from, GraphNode to,
				double cost) {
			super();
			this.fromID = fromID;
			this.toID = toID;
			this.from = from;
			this.to = to;
			this.cost = cost;
		}

	}

}
