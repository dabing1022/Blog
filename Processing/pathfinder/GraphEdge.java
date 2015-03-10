/*
  Part of the AI for Games library 

  Copyright (c) 2011 Peter Lager

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package pathfinder;


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
	 * This constructor is used to create new edges for use with the 
	 * path finding algorithms Dijkstra and A*  <br>
	 * <b>It should not be used directly. </b>
	 * @param edge the existing edge
	 * @param costSoFar the cost to the destination node so far.
	 */
	public GraphEdge(GraphEdge edge, double costSoFar){
		from = edge.from;
		to = edge.to;
		this.cost = costSoFar;
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
		if(cost == 0.0f){
			cost = Math.sqrt( (to.x() - from.x()) * (to.x() - from.x()) 
					+ (to.y() - from.y()) * (to.y() - from.y()) 
					+ (to.z() - from.z()) * (to.z() - from.z()) );		
		}
		this.cost = cost;
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
	
}
