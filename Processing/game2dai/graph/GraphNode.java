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


/**
 * This class represents a node (vertex) that can be used with
 * the Graph class. <br>
 * 
 * The node has a position in 3D space for 2D applications the z value should 
 * be zero. <br>
 * 
 * Each node should be given a unique ID number >= 0. Node ID numbers do
 * not need to start at 0 (zero) or be sequential but they must be unique. <br>
 * 
 * It is the responsibility of the user to ensure that each node ID is unique 
 * as duplicate ID numbers can lead to unpredictable behaviour.
 * 
 * @author Peter Lager
 *
 */
public class GraphNode implements Comparable<Object> {

	protected int id;
	
	double x,y,z;
	
	
	protected GraphNode(){
		id = -1;
	}
	
	/**
	 * Create a node with a given ID
	 * @param id
	 */
	public GraphNode(int id){
		this.id = id;
		x = y = z = 0;
	}

	/**
	 * Create a node
	 * @param id unique id number for this node
	 * @param x
	 * @param y
	 * @param z set to 0 (zero for 2D applications
	 */
	public GraphNode(int id, double x, double y, double z) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	
	/**
	 * Create a node
	 * @param id unique id number for this node
	 * @param x
	 * @param y
	 */
	public GraphNode(int id, double x, double y) {
		this.id = id;
		this.x = x;
		this.y = y;
		z = 0;
	}

	/**
	 * Copy constructor.
	 * @param node node to copy from
	 */
	public GraphNode(GraphNode node){
		id = node.id;
		x = node.x;
		y = node.y;
		z = node.z;
	}
	
	/**
	 * Get the node ID 
	 * @return the id
	 */
	public int id() {
		return id;
	}

	/**
	 * Change the node id. <br>
	 * Care should be taken to ensure the new ID number is unique
	 * @param id the id to set
	 */
	public void id(int id) {
		this.id = id;
	}

	/**
	 * @return the x position
	 */
	public double x() {
		return x;
	}

	/**
	 * @return the x position as a float
	 */
	public float xf() {
		return (float) x;
	}

	/**
	 * @param x the new x position
	 */
	public void x(double x) {
		this.x = x;
	}

	/**
	 * @return the y position
	 */
	public double y() {
		return y;
	}

	/**
	 * @return the y position as a float
	 */
	public float yf() {
		return (float) y;
	}

	/**
	 * @param y the new y position
	 */
	public void y(double y) {
		this.y = y;
	}

	/**
	 * @return the z position
	 */
	public double z() {
		return z;
	}

	/**
	 * @return the y position as a float
	 */
	public float zf() {
		return (float) z;
	}

	/**
	 * @param z the new z position
	 */
	public void z(double z) {
		this.z = z;
	}

	/**
	 * Compare nodes based on ID number. <br>
	 */
	public int compareTo(Object o) {
		GraphNode node = (GraphNode) o;
		if(id == node.id)
			return 0;
		else 
			return (id < node.id) ? -1 : 1;
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder("Node ID:" + id + " \t[");
		s.append(x + ", " + y + ", " + z + "]");
		return new String(s);
	}
}
