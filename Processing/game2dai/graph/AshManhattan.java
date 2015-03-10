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
 * This class is used to calculate the heuristic estimated-cost-to-goal. <br>
 * 
 * It estimates the cost to goal as the sum of the differences between the 
 * nodes in all there primary directions. So if there were 2 nodes then the
 * estimated-cost between them is<br>
 * <pre>|x1 - x2| + |y1 - y2| + |z1 - z2| </pre><br>
 * 
 * It is also possible to apply a scaling factor to the heuristic. <br>
 * 
 * @author Peter Lager
 *
 */
public class AshManhattan implements AstarHeuristic {

	private double factor = 1.0;
	
	/**
	 * Will use a factor of 1.0 to calculate the estimated cost 
	 * between nodes
	 */
	public AshManhattan() {
		factor = 1.0;
	}

	/**
	 * Create the heuristic.
	 * @param factor scaling factor
	 */
	public AshManhattan(double factor) {
		this.factor = factor;
	}

	/**
	 * Estimate the cost between the node and the target.
	 */
	public double getCost(GraphNode node, GraphNode target) {
		return factor * (target.x - node.x + target.y - node.y + target.z - node.z);
	}

}
