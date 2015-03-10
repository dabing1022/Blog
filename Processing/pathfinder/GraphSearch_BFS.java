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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Breadth First Search <br>
 * Objects of this class are used to search a graph and find a path
 * between two nodes using this algorithm.
 * 
 * @author Peter Lager
 *
 */
public class GraphSearch_BFS  implements IGraphSearch{

	protected Graph graph;
	
	/**
	 * Used to remember which nodes had been visited
	 */
	protected HashSet<Integer> visited;

	/**
	 * Remember the route found
	 * 				 to       from
	 */ 
	protected HashMap<Integer, Integer> settledNodes;

	/**
	 * Queue of graph edges to consider
	 */
	LinkedList<GraphEdge> queue;

	/**
	 * Used to remember examined edges
	 */
	protected HashSet<GraphEdge> examinedEdges;

	/**
	 * List for routes nodes in order of travel
	 */
	protected LinkedList<GraphNode> route;

	/**
	 * Prevent creating an object without a graph
	 */
	protected GraphSearch_BFS(){ }

	/**
	 * Create a search object that uses breadth first search algorithm
	 * for the given graph.
	 * @param graph the graph to use
	 */
	public GraphSearch_BFS(Graph graph) {
		super();
		this.graph = graph;
		int nbrNodes = graph.getNbrNodes();
		visited = new HashSet<Integer>(nbrNodes);
		settledNodes = new HashMap<Integer, Integer>(nbrNodes);
		queue = new LinkedList<GraphEdge>();
		examinedEdges = new HashSet<GraphEdge>();
		route = new LinkedList<GraphNode>();
	}

	/**
	 * Clears all data related to a search so this object can be
	 * reused for another search
	 */
	public void clear(){
		queue.clear();
		settledNodes.clear();
		visited.clear();
		examinedEdges.clear();
		route.clear();
	}

	/**
	 * Search for a route from node startID and ends at targetID. <br>
	 * This will return a linkedlist of the nodes that make up the route
	 * from start to end order. <br>
	 * If either the start or target node does not exist or if a route 
	 * can't be found the returned list is empty.
	 * 
	 * @param startID id of the start node
	 * @param targetID id of the target node
	 * @return the route as a list of nodes
	 */
	public LinkedList<GraphNode> search(int startID, int targetID){
		return search(startID, targetID, false);
	}

	/**
	 * Search for a route from node startID and ends at targetID. <br>
	 * This will return a linkedlist of the nodes that make up the route
	 * from start to end order. <br>
	 * If either the start or target node does not exist or if a route 
	 * can't be found the returned list is empty.
	 * 
	 * @param startID id of the start node
	 * @param targetID id of the target node
	 * @param remember whether to remember the examined edges.
	 * @return the route as a list of nodes
	 */
	public LinkedList<GraphNode> search(int startID, int targetID, boolean remember){
		clear();
		LinkedList<GraphEdge> nextEdges;

		GraphEdge next;
		GraphNode start = graph.getNode(startID);
		GraphNode target = graph.getNode(targetID);
		if(start == null || target == null)
			return null;

		GraphEdge dummy = new GraphEdge(start, start, 0);
		queue.addLast(dummy);

		while(!queue.isEmpty()){
			next = queue.removeFirst();
			settledNodes.put(next.to().id(), next.from().id());
			visited.add(next.to().id());
			if(next.to().id() == targetID){
				int parent = target.id();
				route.addFirst(target);
				do {
					parent = settledNodes.get(parent);
					route.addFirst(graph.getNode(parent));

				} while (parent != startID);
				return route;
			}
			nextEdges = graph.getEdgeList(next.to().id());
			for(GraphEdge ge : nextEdges){
				if(!visited.contains(ge.to().id())){
					queue.addLast(ge);
					visited.add(ge.to().id());
					// Edges visited collection update
					if(remember)
						examinedEdges.add(ge);
				}
			}
		}
		System.out.println("No route found");
		return null;
	}
	
	/**
	 * Get all the edges examined during the search. <br>
	 * 
	 * @return edges examined or array size 0 if none found
	 */
	public GraphEdge[] getExaminedEdges(){
		return getExaminedEdges(new GraphEdge[0]);
	}
	
	/**
	 * Get all the edges examined during the search. <br>
	 * The type of each element in the array will be of type Object
	 * if the parameter is null otherwise it is T (where T is GraphEdge
	 * or any class that extends GraphEdge.
	 * 
	 * @param array the array to populate
	 * @return edges examined or array size 0 if none found
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getExaminedEdges(T[] array){
		if(array == null)
			return (T[]) examinedEdges.toArray(new Object[0]);
		else
			return examinedEdges.toArray(array);
	}
	
	/**
	 * Get the path found as an array of GraphNode(s) in start->end
	 * order <br>
	 * @return path found or array size 0 if none found
	 */
	public GraphNode[] getRoute(){
		return route.toArray(new GraphNode[route.size()]);
	}
	
	/**
	 * Get the path found as an array of T(s) in start->end
	 * order. <br>
	 * The type of each element in the array will be of type Object
	 * if the parameter is null otherwise it is T (where T is GraphNode
	 * or any class that extends GraphNode.
	 * @param array the array to populate
	 * @return path found or array size 0 if none found
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getRoute(T[] array){
		if(array == null)
			return (T[]) route.toArray(new Object[0]);
		else
			return route.toArray(array);
	}
	
}
