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
 * This class represents an artefact that can be added to a game. This artefact may
 * have a FSM and a renderer but it is invisible to any moving entity, so is not 
 * detected by any Vehicle's steering behaviour.
 * 
 * @author Peter Lager
 *
 */
public class Artefact extends BaseEntity {
	
	protected double minX, maxX, minY, maxY;

	/**
	 * Default constructor
	 */
	public Artefact(){
		this(null, null, 0, 0);
	}

	/**
	 * Constructor will give the entity a unique ID. 
	 * @param entityName the name of this entity - default name is used if none provided.
	 */
	public Artefact(String entityName){
		this(entityName, null, 0, 0);
	}

	/**
	 * 
	 * @param entityName
	 * @param entityPos
	 * @param width
	 * @param height
	 */
	public Artefact(String entityName, Vector2D entityPos, double width, double height) {
		super(entityName);
		if(entityPos == null)
			pos.set(0,0);
		else
			this.pos.set(entityPos);
		minX = pos.x - width/2;
		maxX = pos.x + width/2;
		minY = pos.y - height/2;
		maxY = pos.y + height/2;
		visible = false;
	}

	/**
	 * Get the rectangle that encompasses this artefact in world coordinates. <br>
	 */
	public Domain getExtent(){
		return new Domain(minX, minY, maxX, maxY);
	}

	/**
	 * Determine whether this artefact is inside or part inside the domain. This method is
	 * used by the world draw method to see if this entity should be drawn.
	 * @param view the world domain
	 * @return true if any part of this entity is inside the domain
	 */
	@Override
	public boolean isInDomain(Domain view) {
		return Geometry2D.box_box(minX, minY, maxX, maxY, view.lowX, view.lowY, view.highX, view.highY);
	}

}
