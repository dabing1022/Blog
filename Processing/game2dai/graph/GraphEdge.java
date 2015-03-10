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


/**
 * This class is used to represent a directed edge between 2 nodes and the cost 
 * of traversing this edge.
 * 
 * @author Peter Lager
 *
 */
public class GraphEdge implements Comparable<Object>{

	protected GraphNode from;
	protected GraphNode to;	
	protected double cost = 1.0;
	
	/** 
	 * Make protected to prevent its use outside the class.
	 */
	protected GraphEdge(){
		from = to = null;
	}

	/**
	 * Create an edge of cost 1.0f
	 * @param from 'from' node
	 * @param to 'to' node
	 */
	public GraphEdge(GraphNode from, GraphNode to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param edge
	 */
	public GraphEdge(GraphEdge edge){
		from = edge.from;
		to = edge.to;
		cost = edge.cost;
	}
	

	/**
	 * Create an edge from 2 existing nodes. If the cost is 0.0
	 * then calculate the cost based on the physical distance
	 * between the nodes. <br>
	 * 
	 * @param from 'from' node
	 * @param to 'to' node
	 * @param cost traversal cost
	 */
	public GraphEdge(GraphNode from, GraphNode to, double cost) {
		this.from = from;
		this.to = to;
		this.cost = (cost == 0) ? Geometry2D.distance(from.x(), from.y(), to.x, to.y) : cost;
	}

	/**
	 * This constructor is used to create new edges for use with the 
	 * path finding algorithms Dijkstra and A*  <br>
	 * <b>It should not be used directly. </b>
	 * @param edge the existing edge
	 * @param costSoFar the cost to the destination node so far.
	 */
	GraphEdge(GraphEdge edge, double costSoFar){
		from = edge.from;
		to = edge.to;
		this.cost = costSoFar;
	}
	
	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Change the traversal cost.
	 * @param cost the new traversal cost.
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @return the 'from' node
	 */
	public GraphNode from() {
		return from;
	}

	/**
	 * @return the 'to' node
	 */
	public GraphNode to() {
		return to;
	}
	
	/**
	 * Compare two graph edges.
	 */
	public int compareTo(Object o) {
		GraphEdge ge = (GraphEdge)o;
		if(from.compareTo(ge.from) == 0 && to.compareTo(ge.to) == 0)
			return 0;
		if(from.compareTo(ge.from) == 0)
			return  to.compareTo(ge.to);
		else
			return from.compareTo(ge.from);
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder("Edge from: " + from.id() + " to: " + to.id());
		s.append("   \t cost: " + cost);
		return new String(s);
	}
}
