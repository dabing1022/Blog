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

import java.util.Comparator;

import game2dai.Domain;
import game2dai.World;
import game2dai.entityshapes.Picture;
import game2dai.fsm.FiniteStateMachine;
import game2dai.maths.Vector2D;

/**
 * This is the base class for all game entities.
 * 
 * @author Peter Lager
 *
 */
public abstract class BaseEntity implements Comparable<BaseEntity> {

	private static int nextID = Integer.MIN_VALUE;

	protected Integer entityID;
	protected String name = "";

	/**
	 * The tag attribute is not used by the library code. It is there for the user
	 * to use in any way they wish. <br>
	 * When the entity is created it is initialised to <br>
	 * <pre>"Entity <i>entityID</i>"</pre><br>
	 * Where <i>entityID</i> is the unique id created by the library for this entity.
	 */
	public String tag = "";
	
	/**
	 * The tagNo attribute is not used by the library code. It is there for the user
	 * to use in any way they wish.
	 */	
	public int tagNo = 0;
	
	protected Picture renderer = null;
	protected boolean visible = true;

	// Its location in the world
	protected Vector2D pos = new Vector2D();

	// The length of this entity's bounding radius
	protected double colRadius;

	// Finite state machine
	private FiniteStateMachine fsm = null;

	// Controls the order that the controls are drawn
	private int z = 0;

	// Should this entity be considered if the world enforces no overlap rule.
	boolean overlapAllowed = false;
	
	/**
	 * Default constructor
	 */
	public BaseEntity(){
		this(null, null, 0);
	}

	/**
	 * Constructor will give the entity a unique ID. 
	 * @param name the name of this entity - default name is used if none provided.
	 */
	public BaseEntity(String name){
		this(name, null, 0);
	}

	/**
	 * Constructor will give the entity a unique ID. 
	 * @param entityName the name of this entity - default name is used if none provided.
	 * @param entityPos the world position for this entity
	 * @param entityColRadius the bounding radius for this entity
	 */
	public BaseEntity(String entityName, Vector2D entityPos, double entityColRadius) {
		entityID = nextID++;
		if(entityName == null || entityName.length() == 0)
			name = "Entity: "+ entityID;
		else
			name = entityName;
		if(entityPos == null)
			pos.set(0,0);
		else
			this.pos.set(entityPos);
		colRadius = entityColRadius;
		visible = false;
	}


	/**
	 * Get the entity's unique ID number
	 */
	public int ID(){
		return entityID;
	}

	/**
	 * If this entity has a state machine then call its update method.
	 * @param deltaTime elapsed time since last update (seconds)
	 * @param world the game world object
	 */
	public void updateFSM(double deltaTime, World world){
		if(fsm != null)
			fsm.update(deltaTime, world);
	}

	/**
	 * Returns a reference to the entity's finite state machine or
	 * null if there is no FSM
	 */
	public FiniteStateMachine FSM(){
		return fsm;
	}

	/**
	 * Does this entity have a finite state machine
	 * @return true if the entity has a FSM else false
	 */
	public boolean hasFSM(){
		return fsm != null;
	}

	/**
	 * This is the only way to add a finite state machine to the entity.
	 */
	public void addFSM(){
		fsm = new FiniteStateMachine(this);
	}

	/**
	 * This is the only way to remove a FSM once added. The FSM is always removed,
	 * @return true if the entity had a FSM to remove., else false.
	 */
	public boolean removeFSM(){
		if(fsm != null){
			fsm = null;
			return true;
		}
		else
			return false;
	}

	/**
	 * Get the draw order depth.
	 * @return the z
	 */
	public int Z() {
		return z;
	}

	/**
	 * Set the draw order depth. The greater the value of z the closer
	 * it is to the viewer. 
	 * 
	 * @param z the z to set
	 */
	public BaseEntity Z(int z) {
		this.z = z;
		return this;
	}

	/**
	 * Is this entity visible
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set the entity's visibility
	 * @param visible true or false
	 */
	public BaseEntity visible(boolean visible){
		this.visible = visible;
		return this;
	}

	/**
	 * Get the bounding radius for this object.
	 * @return the collision radius
	 */
	public double colRadius() {
		return colRadius;
	}

	/**
	 * Set the bounding radius for this object
	 * @param colRadius the collision radius to set
	 */
	public BaseEntity colRadius(double colRadius) {
		this.colRadius = colRadius;
		return this;
	}

	/**
	 * Should this entity be considered if the world is enforcing 
	 * the no-overlap rule.
	 */
	public boolean isOverLapAllowed(){
		return this.overlapAllowed;
	}
	
	/**
	 * Set whether the entity should be considered when (and if) the 
	 * world is enforcing the no overLap rule.
	 * 
	 * @param overLapAllowed
	 * @return this entity
	 */
	public BaseEntity overLapAllowed(boolean overLapAllowed){
		this.overlapAllowed = overLapAllowed;
		return this;
	}
	
	/**
	 * Determine whether this entity is inside or part inside the domain. This method is
	 * used by the world draw method to see if this entity should be drawn.
	 * @param view the world domain
	 * @return true if any part of this entity is inside the domain
	 */
	public boolean isInDomain(Domain view){
		return false;
	}

	/**
	 * Determine whether the given world position is over a given entity.
	 * @param px x position of point of interest
	 * @param py y position of point of interest
	 * @return true if over this entity
	 */
	public boolean isOver(double px, double py){
		return false;
	}

	/**
	 * Add a render object to this entity. This is essential if you want to display 
	 * the entity. <br>
	 * Assumes that if the parameter is not null then you want to see this
	 * entity. If you don't then
	 * @param renderer the renderer to use (it must implement Renderer)
	 */
	public BaseEntity renderer(Picture renderer){
		this.renderer = renderer;
		visible = (renderer != null);
		return this;
	}

	/**
	 * Get the render object. This is useful if you want to change how an object is drawn
	 * during a game. In most cases you will have to cast it to the appropriate type before
	 * using it.
	 * @return the render object.
	 */
	public Picture renderer(){
		return renderer;
	}

	/**
	 * This is the only safe way to remove an entity from the World object. It does not 
	 * remove the object directly but schedules it for removal at the start of the world update() 
	 * method.
	 * @param world the world to remove this entity in
	 * @param timeToLive time left to stay in the world in seconds
	 */
	public void die(World world, double timeToLive){
		if(world != null){
			world.death(this, timeToLive);
		}
	}
	
	/**
	 * This is the only safe way to add an entity to the World object once initialisation has  
	 * finished and the game/simulation is in play. It does not add the entity 
	 * directly but schedules it for addition at the start of the world update() 
	 * method. <br>
	 * During initialisation (i.e. setup) it is recommended that you use the World add(0
	 * method instead.
	 * @param world the world to add this entity to
	 * @param timeToLive time left to wait before adding to the world in seconds
	 */
	public void born(World world, double timeToLive){
		if(world != null){
			world.birth(this, timeToLive);
		}
	}
	
	/**
	 * Set the current position for this entity.
	 * @param x world x position
	 * @param y world y position
	 */
	public BaseEntity moveTo(double x, double y){
		pos.x = x;
		pos.y = y;
		return this;
	}

	/**
	 * Set the current position for this entity.
	 * @param p the position to set
	 */
	public BaseEntity moveTo(Vector2D p){
		pos.x = p.x;
		pos.y = p.y;
		return this;
	}

	/**
	 * Move the current position for this entity by the specified amount.
	 * @param x world x distance to move
	 * @param y world y distance to move
	 */
	public BaseEntity moveBy(double x, double y){
		pos.x += x;
		pos.y += y;
		return this;
	}

	/**
	 * Move the current position for this entity by the specified amount.
	 * @param p the vector to move by
	 */
	public BaseEntity moveBy(Vector2D p){
		pos.x += p.x;
		pos.y += p.y;
		return this;
	}

	/**
	 * Get the current position.
	 * @return current position
	 */
	public Vector2D pos(){
		return pos;
	}

	/**
	 * Change the name of this entity
	 * @param name the new name to use
	 */
	public BaseEntity name(String name){
		this.name = name;
		return this;
	}

	/**
	 * Get the current name of the entity.
	 */
	public String name(){
		return name;
	}

	/**
	 * Determines whether the two points given are 'either side of the object' if true then 
	 * the two positions are not visible to each other. <br>
	 * This should be overridden in child classes.
	 * @param x0 x position of first point of interest
	 * @param y0 y position of first point of interest
	 * @param x1 x position of second point of interest
	 * @param y1 y position of second point of interest
	 * @return true if the points are either side else false
	 */
	public boolean isEitherSide(double x0, double y0, double x1, double y1){
		return false;
	}

	/**
	 * Determines whether the two points given are 'either side of the object' if true then 
	 * the two positions are not visible to each other. <br>
	 * This should be overridden in child classes.
	 * @param p0 first point of interest
	 * @param p1 second point of interest
	 * @return true if the points are either side else false
	 */
	public boolean isEitherSide(Vector2D p0, Vector2D p1){
		return isEitherSide(p0.x, p0.y, p1.x, p1.y);
	}

	/**
	 * The main update method for an entity. This method will be called by
	 * the world update method if the entity has been 'added' to the
	 * world.
	 * @param deltaTime elapsed time since last update
	 * @param world the game world object
	 */
	public void update(double deltaTime, World world){ }


	/**
	 * DO NOT CALL THIS METHOD DIRECTLY <br>
	 * This method is left to ensure backward compatibility with library versions prior to 1.0 <br/>
	 * Use the draw(elapsedTime, world) method instead.
	 * 
	 * This should be overridden in child classes
	 */
	@Deprecated
	public void draw(World world){
		if(renderer != null && visible)
			renderer.draw(this, (float) pos.x, (float) pos.y, 0, 0, 0, 0);
	}	

	/**
	 * DO NOT CALL THIS METHOD DIRECTLY <br>
	 * If this shape has a renderer and is visible call the renderer's draw method passing all 
	 * appropriate information. <br>
	 * 
	 * This should be overridden in child classes
	 */
	public void draw(double elapsedTime, World world){
		if(renderer != null && visible)
			renderer.draw(this, (float) pos.x, (float) pos.y, 0, 0, 0, 0, (float)elapsedTime);
	}	


	/**
	 * Compare 2 entities based on their entity ID numbers
	 */
	public int compareTo(BaseEntity o) {
		if(o != null)
			return entityID.compareTo(o.entityID);
		else
			return -1;	
	}

	/**
	 * Entity ID number and name returned as a String
	 */
	public String toString(){
		return "ID: " + entityID + "  " + name;
	}

	/**
	 * This comparator is used to control the order the entities are drawn to the screen.
	 * 
	 * @author Peter Lager
	 *
	 */
	public static class DepthOrder implements Comparator<BaseEntity> {
		@Override
		public int compare(BaseEntity be1, BaseEntity be2) {
			if(be1.z == be2.z){
				if(be1.entityID == be2.entityID)
					return 0;
				else
					return (be1.entityID < be2.entityID) ? -1 : 1;
			}
			// Different display depths
			return (be1.z < be2.z) ? -1 : 1;
		}
	}

	
}
