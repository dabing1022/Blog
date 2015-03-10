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

package game2dai.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Breadth First Search
 * @author Peter
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
	 * if the parameter is null, otherwise it is T (where T is GraphNode
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
