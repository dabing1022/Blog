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

package game2dai.entities;

import game2dai.Domain;
import game2dai.maths.Geometry2D;
import game2dai.maths.Vector2D;

/**
 * This class represents a wall. <br>
 * A wall is used to constrain the movement of Vehicle entities that are 
 * using 'wall avoidance' in their steering behaviour. A vehicle will not 
 * pass through a wall from the 'outside' but will pass through from the
 * inside (i.e. a one-way wall!). <br>
 * If you image the wall to be the radius of a circle with the wall start 
 * position at the centre and the end position on the circumference, then 
 * the clockwise side is considered to be the 'outside' and the anti-clockwise
 * side the 'inside'. So be careful which way you define the wall end-points.
 * 
 * @author Peter Lager
 *
 */
public class Wall extends BaseEntity {

	Vector2D end = new Vector2D();;
	Vector2D wallNorm;

	/**
	 * Create a wall.
	 * 
	 * @param start wall start position.
	 * @param end wall end position
	 * @param visible whether the wall is visible
	 */
	public Wall(Vector2D start, Vector2D end, boolean visible) {
		this("", start, end, visible);
	}

	/**
	 * Create a 'named' wall.
	 * 
	 * @param name the name you want to give this entity
	 * @param start wall start position.
	 * @param end wall end position
	 * @param visible whether the wall is visible
	 */
	public Wall(String name, Vector2D start, Vector2D end, boolean visible) {
		super(name, start, 0);
		this.end.set(end);
		wallNorm = new Vector2D(-(end.y - start.y), end.x - start.x);
		wallNorm.normalize();
		this.visible = visible;
	}

	/**
	 * Get the wall start position
	 */
	public Vector2D getStartPos(){
		return pos;
	}

	/**
	 * Get the world end position
	 */
	public Vector2D getEndPos(){
		return end;
	}

	/**
	 * Get the wall normal - it will point 'outside'
	 * @return the wall normal (always in normalised form)
	 */
	public Vector2D norm(){
		return wallNorm;
	}

	/**
	 * Determines whether the two points given are 'either side of the object' if true then 
	 * the two positions are not visible to each other.
	 * @param x0 first point x position
	 * @param y0 first point y position
	 * @param x1 second point x position
	 * @param y1 second point y position
	 * @return true if the points are on opposites sides of the wall
	 */
	public boolean isEitherSide(double x0, double y0, double x1, double y1){
		return Geometry2D.line_line(x0, y0, x1, y1, pos.x, pos.y, end.x, end.y);
	}

	/**
	 * Determine whether this entity is inside or part inside the domain. This method is
	 * used by the world draw method to see if this entity should be drawn.
	 * @param view the world domain
	 * @return true if any part of this entity is inside the domain
	 */
	@Override
	public boolean isInDomain(Domain view) {
		return Geometry2D.line_box_xyxy(pos.x, pos.y, end.x, end.y, view.lowX, view.lowY, view.highX, view.highY);
	}

	/**
	 * Since the wall is considered to have no thickness then the
	 * world point cannot be over the wall. So this always returns false; 
	 */
	@Override
	public boolean isOver(double px, double py) {
		return false;
	}

}
