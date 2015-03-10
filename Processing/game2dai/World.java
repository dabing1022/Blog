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

package game2dai;

import game2dai.entities.Artefact;
import game2dai.entities.BaseEntity;
import game2dai.entities.Building;
import game2dai.entities.MovingEntity;
import game2dai.entities.Obstacle;
import game2dai.entities.Vehicle;
import game2dai.entities.Wall;
import game2dai.fsm.Dispatcher;
import game2dai.maths.FastMath;
import game2dai.maths.Geometry2D;
import game2dai.maths.Vector2D;
import game2dai.utils.StopWatch;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents the '2D game play area' on which all the game entities are placed. Internally all world
 * coordinates are stored as doubles (rather than floats) to minimise rounding errors, particularly when using 
 * steering behaviours. <br>
 * This means the size of the world is only limited by the range of numbers that can be stored in the double data
 * type ( i.e. +/- 1.7976931348623157e+308) <br>
 *   
 * This class provides the core functionality for maintaining collections of all the game entities. <br>
 * 
 * When there are a large number of game entities, cell space partitioning (CSP) can significantly reduce the CPU 
 * time needed to update all the moving entities. It will also reduce the rendering time by not attempting to draw
 * entities that fall outside of the viewable area. This is normally at the expense of additional memory needed for 
 * the data structures.<br>
 * 
 * Depending on which constructor is used when creating the 'world' object determines whether CSP is used or not.
 * The CSP implementation is totally transparent to the user - so apart from changing the constructor used no other
 * changes are needed in the users code. <br>
 *  
 * @author Peter Lager  
 *
 */
public final class World {

	// Used to calculate the average update time
	private static StopWatch timer = new StopWatch();
	private static double cumTime;
	private static int count = 0;

	// This is used to quickly locate an entity based on its entityID and is
	// used when dispatching telegrams 
	public static HashMap<Integer, BaseEntity> allEntities = new HashMap<Integer, BaseEntity>();


	// Used to collect references to anything to be drawn
	// used to avoid multiple drawings in partition mode
	// and for ordering the entities for drawing.
	private Set<BaseEntity> drawSet = new TreeSet<BaseEntity> (new BaseEntity.DepthOrder());

	// Keep a list of birth and deaths
	private ArrayList<Changling> entityChangeList = new ArrayList<Changling>();

	// For use with and without partitions
	private Set<Artefact> artefacts;
	private Set<Building> buildings;
	private Set<Wall> walls;
	private Set<Obstacle> obstacles;
	private Set<MovingEntity> movers;

	// For use with partitions
	private HashMap<Point, HashSet<Artefact>> artefact_parts;
	private HashMap<Point, HashSet<Building>> building_parts;
	private HashMap<Point, HashSet<Wall>> wall_parts;
	private HashMap<Point, HashSet<Obstacle>> obstacle_parts;
	private HashMap<Point, HashSet<MovingEntity>> moving_parts;

	private boolean noOverlap = true;

	private final boolean cspOn;
	private final double  partSize;
	private final double partOverlap; 

	/** Number of artefacts in this world */
	public int nbr_artefacts = 0;
	/** Number of buildings in this world */
	public int nbr_buildings = 0;
	/** Number of obstacles in this world */
	public int nbr_obstacles = 0;
	/** Number of walls in this world */
	public int nbr_walls = 0;
	/** Number of moving entities in this world */
	public int nbr_movers = 0;
	/** The average time it takes for the world to update all the components in the world. */
	public double worldUpdateTime = 0;

	/*
	 * This represents that area of the 'real game world' that is visible in the display.
	 * It does NOT represent the actual display area in pixels, but its aspect ratio should
	 * be the same as the display area aspect ratio.
	 *
	 */
	private Domain viewOnWorld;
	private double viewScale = 1.0;

	// These are the width and height of the display area and are only required
	// to ensure that the zoom feature is centred on the display area.
	private final double displayWidth, displayHeight;

	/**
	 * Make the default constructor private so it can't be used
	 */
	@SuppressWarnings("unused")
	private World(){
		cspOn = false;
		displayWidth = displayHeight = 0;
		partSize = partOverlap = 0; 
	}

	/**
	 * Create a world without cell space partitioning. <br>
	 * The height and width are the physical display area size in pixels. It maybe smaller than 
	 * the width and height of the application's window. <br>
	 * The portion of the world to be displayed will be a initialised to a rectangle (0,0) to (width, height). <br>
	 * <pre>
	 * (0,0) ___________________________________ X axis
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |__________________________| (width, height)
	 *       |
	 *       Y axis
	 * </pre><br>
	 * This gives a view scale of 1. (i.e. 1 pixel = 1 world unit) <br>
	 * The actual part of the world to be displayed cane be changed with the <pre>
	 * moveTo, panPixel, panWorld & setScale methods. </pre><br><br>
	 * 
	 * @param width the physical screen display width in pixels
	 * @param height the physical screen display height in pixels
	 */
	public World(int width, int height){
		cspOn = false;
		partSize = partOverlap = 0; 
		displayWidth = width;
		displayHeight = height;
		viewOnWorld =  new Domain(0, 0, displayWidth, displayHeight);
		viewScale = 1.0;

		artefacts = new TreeSet<Artefact>();

		buildings = new HashSet<Building>();
		walls = new HashSet<Wall>();
		obstacles = new HashSet<Obstacle>();
		movers = new HashSet<MovingEntity>();
	}

	/**
	 * Create a world with cell space partitioning. <br>
	 * The height and width are the physical display area size in pixels. It maybe smaller than 
	 * the width and height of the applications window. <br>
	 * The portion of the world to be displayed will be a initialised to a rectangle (0,0) to (width, height). <br>
	 * <pre>
	 * (0,0) ___________________________________ X axis
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |                          |
	 *       |__________________________| (width, height)
	 *       |
	 *       Y axis
	 * </pre><br>
	 * This gives a view scale of 1. (i.e. 1 pixel = 1 world unit) <br>
	 * The actual part of the world to be displayed cane be changed with the <pre>
	 * moveTo, panPixel, panWorld & setScale methods. </pre><br><br>
	 * 
	 * This constructor will cause cell-space partitioning to be used. Use the following guide when deciding on 
	 * the values for the partition size and overlap parameters <br><pre>
	 * partitionSize >= 5 x MCR <br>
	 * partitionOverlap >= 2 x MCR <br>
	 * partitionSize >= 2 x partitionOverlap <br>
	 * partitionOverlap > whisker length if using steering behaviours with wall avoidance enabled</pre><br>
	 * where MCR is the maximum collision radius for your moving entities. The values used will have a significant 
	 * effect on performance so it is worth experimenting with different values for your game. <br>
	 * 
	 * @param width the physical screen display width in pixels
	 * @param height the physical screen display height in pixels
	 * @param partitionSize the size of the partition in world distance
	 * @param partitionOverlap the overlap between partitions
	 */
	public World(int width, int height, double partitionSize, double partitionOverlap){
		cspOn = true;
		displayWidth = width;
		displayHeight = height;
		viewOnWorld =  new Domain(0, 0, displayWidth, displayHeight);
		viewScale = 1.0;

		partSize = partitionSize;
		partOverlap = partitionOverlap;

		// Needed for when moving between partitions
		movers = new HashSet<MovingEntity>();

		// Needed for steering behaviours
		artefact_parts = new HashMap<Point, HashSet<Artefact>>();
		building_parts = new HashMap<Point, HashSet<Building>>();
		wall_parts = new HashMap<Point, HashSet<Wall>>();
		obstacle_parts = new HashMap<Point, HashSet<Obstacle>>();
		moving_parts = new HashMap<Point, HashSet<MovingEntity>>();
	}

	/**
	 * Calculate the pixel position from a given world position.
	 * 
	 * @param wx world x position
	 * @param wy world y position
	 * @param pxy object to hold the calculate pixel position
	 * @return the calculated pixel position
	 */
	public Point world2pixel(double wx, double wy, Point pxy){
		if(pxy == null)
			pxy = new Point();
		int px = (int) FastMath.round((wx - viewOnWorld.lowX) * viewScale);
		int py = (int) FastMath.round((wy - viewOnWorld.lowY) * viewScale);
		pxy.setLocation(px, py);
		return pxy;
	}

	/**
	 * Calculate the pixel position from a given world position.
	 * 
	 * @param wxy world x,y position
	 * @param pxy object to hold the calculate pixel position
	 * @return the calculated pixel position
	 */
	public Point world2pixel(Vector2D wxy, Point pxy){
		return world2pixel(wxy.x, wxy.y, pxy);
	}

	/**
	 * Calculate the equivalent world position from a pixel position.
	 * 
	 * @param px display screen pixel x position
	 * @param py display screen pixel x position
	 * @param wxy a Vector2D object to hold the world position
	 * @return the calculated world position
	 */
	public Vector2D pixel2world(int px, int py, Vector2D  wxy){
		if(wxy == null)
			wxy = new Vector2D();
		wxy.x = px;
		wxy.y = py;
		wxy.mult(1.0 / viewScale);
		wxy.x += viewOnWorld.lowX;
		wxy.y += viewOnWorld.lowY;
		return wxy;
	}

	/**
	 * Resize the world due to changes in magnification
	 * so that the image is centred in the display area.
	 * 
	 * @param scale the level of magnification (must be > 0)
	 */
	public void scale(double scale){
		if(scale > 0) {
			double newX, newY, newWidth, newHeight;
			double scaleFactor = viewScale / scale;

			newWidth = viewOnWorld.width * scaleFactor;
			newHeight = viewOnWorld.height * scaleFactor;
			viewScale = scale;

			newX = viewOnWorld.lowX + 0.5 * viewOnWorld.width * (1-scaleFactor);
			newY = viewOnWorld.lowY + 0.5 * viewOnWorld.height * (1-scaleFactor);
			viewOnWorld.setDomain_xywh(newX, newY, newWidth, newHeight);
		}
	}

	/**
	 * Get the current viewScale (zoom factor)
	 * @return view scale value
	 */
	public double scale(){
		return viewScale;
	}


	/**
	 * Pan the world display by the given number of pixels.
	 * @param px pixels to pan horizontally.
	 */
	public void panPixelX(int px){
		panWorldX(px / viewScale);
	}

	/**
	 * Pan the world display by the given number of pixels.
	 * @param py pixels to pan vertically.
	 */
	public void panPixelY(int py){
		panWorldY(py / viewScale);
	}

	/**
	 * Pan the world display by the given number of pixels.
	 * @param px pixels to pan horizontally.
	 * @param py pixels to pan vertically.
	 */
	public void panPixelXY(int px, int py){
		panWorldXY(px / viewScale, py / viewScale);
	}


	/**
	 * Pan the world display by the given world distance.
	 * @param wx world distance to pan horizontally.
	 */
	public void panWorldX(double wx){
		viewOnWorld.move_centre_x_by(wx);
	}

	/**
	 * Pan the world display by the given world distance.
	 * @param wy world distance to pan vertically.
	 */
	public void panWorldY(double wy){
		viewOnWorld.move_centre_y_by(wy);
	}

	/**
	 * Pan the world display by the given world distance.
	 * @param wx world distance to pan horizontally.
	 * @param wy world distance to pan vertically.
	 */
	public void panWorldXY(double wx, double wy){
		viewOnWorld.move_centre_xy_by(wx, wy);
	}

	/**
	 * Move the world view so it is centred about the given world position.
	 * @param wx centre horizontally about this value.
	 */
	public void moveToWorldX(double wx){
		viewOnWorld.move_centre_x_to(wx);
	}

	/**
	 * Move the world view so it is centred about the given world position.
	 * @param wy centre vertically about this value.
	 */
	public void moveToWorldY(double wy){
		viewOnWorld.move_centre_y_to(wy);
	}

	/**
	 * Move the world view so it is centred about the given world position.
	 * @param wx centre horizontally about this value.
	 * @param wy centre vertically about this value.
	 */
	public void moveToWorldXY(double wx, double wy){
		viewOnWorld.move_centre_xy_to(wx, wy);
	}

	/**
	 * Get the world x position equivalent to the left hand side of the display area. <br>
	 * This is used when rendering the world view.
	 * @return the amount to translate before drawing
	 */
	public double xOffset(){
		return -viewOnWorld.lowX * viewScale;
	}

	/**
	 * Get the world x position equivalent to the left hand side of the display area. <br>
	 * This is used when rendering the world view.
	 * @return the amount to translate before drawing
	 */
	public double yOffset(){
		return -viewOnWorld.lowY * viewScale;
	}

	/**
	 * Get the extent of the world being drawn. It returns a Domain object
	 * @return Domain object with the extent of the world being drawn.
	 */
	public Domain viewOnWorld(){
		return viewOnWorld;
	}

	/**
	 * Get the entity associated with a particular ID number. <br>
	 * If we are not using state machines or if the id number has not been used 
	 * then the entity will not be found.
	 * 
	 * @param entityID unique ID number for the entity
	 * @return null if the entity was not found else the entity that has the given ID number
	 */

	public BaseEntity getEntity(int entityID){
		return allEntities.get(entityID);
	}

	/**
	 * For a particular moving entity get all the buildings in the same partition.
	 * @param m the moving entity
	 * @return a set of nearby buildings
	 */
	public Set<Building> getBuildings(MovingEntity m){
		if(cspOn)
			return building_parts.get(getPartition(m.pos().x, m.pos().y));
		else
			return buildings;
	}

	/**
	 * For a particular moving entity get all the moving entities in the same partition. It 
	 * will include itself.
	 * @param m the moving entity
	 * @return a set containing all the moving entities in its partition
	 */
	public Set<MovingEntity> getMovers(MovingEntity m){
		if(cspOn)
			return moving_parts.get(getPartition(m.pos().x, m.pos().y));
		else
			return movers;
	}

	/**
	 * Get a list of moving entities in partitions within a given range 
	 * of a moving entity.
	 * @param m the mover we are interested in getting the neighbours
	 * @param range the radius of the circle surrounding neighbours.
	 * @return a set containing the neighbours
	 */
	public Set<MovingEntity> getMovers(MovingEntity m, double range){
		HashSet<MovingEntity> neighbours = null;
		if(cspOn){
			neighbours = new HashSet<MovingEntity>();
			double x0 	= m.pos().x - range;
			double y0 	= m.pos().y - range;
			double x1 	= m.pos().x + range;
			double y1 	= m.pos().y + range;

			int left 	= (int) FastMath.floor(x0 / partSize)-1;
			int right 	= (int) FastMath.floor(x1 / partSize)+1;
			int top 	= (int) FastMath.floor(y0 / partSize)-1;
			int bottom 	= (int) FastMath.floor(y1 / partSize)+1;

			Point part = new Point();
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					part.setLocation(partX, partY);
					if(moving_parts.containsKey(part)){
						HashSet<MovingEntity> mpart = moving_parts.get(part);
						neighbours.addAll(mpart);
					}
				}
			}
		}
		else {
			neighbours = (HashSet<MovingEntity>) movers;
		}
		return neighbours;
	}

	/**
	 * Get a list of walls that need to be tested for wall avoidance.
	 * @param m the moving entity
	 * @return a set containing the walls (maybe empty but not null)
	 */
	public Set<Wall> getWalls(MovingEntity m){
		if(cspOn)
			return wall_parts.get(getPartition(m.pos().x, m.pos().y));
		else
			return walls;
	}

	/**
	 * Get a list of walls that are in the box made by the moving entity's position 
	 * and the position px,py
	 * @param m the moving entity
	 * @param px
	 * @param py
	 * @return a set containing the walls (maybe empty but not null)
	 */
	public Set<Wall> getWalls(MovingEntity m, double px, double py){
		if(cspOn){
			Set<Wall> wallsBetween = new HashSet<Wall>();
			Point part = new Point();
			// Get the wall end points
			double x0 = m.pos().x;
			double y0 = m.pos().y;
			// Calculate the range of partitions to check
			int left 	= (int) (FastMath.floor(FastMath.min(x0, px) / partSize) - 1);
			int right 	= (int) (FastMath.floor(FastMath.max(x0, px) / partSize) + 1);
			int top 	= (int) (FastMath.floor(FastMath.min(y0, py) / partSize) - 1);
			int bottom 	= (int) (FastMath.floor(FastMath.max(y0, py) / partSize) + 1);
			// Extended partition size
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					part.setLocation(partX, partY);
					if(wall_parts.containsKey(part))
						wallsBetween.addAll(wall_parts.get(part));
				}
			}
			return wallsBetween;
		}
		else
			return walls;
	}

	/**
	 * Get a list of obstacles that need to be tested for obstacle avoidance.
	 * @param m the moving entity
	 * @return a set containing the obstacles (maybe empty but not null)
	 */
	public Set<Obstacle> getObstacles(MovingEntity m){
		if(cspOn)
			return obstacle_parts.get(getPartition(m.pos().x, m.pos().y));
		else
			return obstacles;
	}

	public Set<Obstacle> getObstacles(double x, double y){
		if(cspOn)
			return obstacle_parts.get(getPartition(x, y));
		else
			return obstacles;
	}

	/**
	 * Get a list of obstacles that are in the box made by the moving entity's position 
	 * and the position px,py
	 * @param m the moving entity
	 * @param px
	 * @param py
	 * @return a set containing the obstacles (maybe empty but not null)
	 */
	public Set<Obstacle> getObstacles(MovingEntity m, double px, double py){
		if(cspOn){
			Set<Obstacle> obstaclesBetween = new HashSet<Obstacle>();
			Point part = new Point();
			// Get the wall end points
			double x0 = m.pos().x;
			double y0 = m.pos().y;
			// Calculate the range of partitions to check
			int left 	= (int) (FastMath.floor(FastMath.min(x0, px) / partSize) - 1);
			int right 	= (int) (FastMath.floor(FastMath.max(x0, px) / partSize) + 1);
			int top 	= (int) (FastMath.floor(FastMath.min(y0, py) / partSize) - 1);
			int bottom 	= (int) (FastMath.floor(FastMath.max(y0, py) / partSize) + 1);
			// Extended partition size
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					part.setLocation(partX, partY);
					if(obstacle_parts.containsKey(part))
						obstaclesBetween.addAll(obstacle_parts.get(part));
				}
			}
			return obstaclesBetween;
		}
		else
			return obstacles;
	}

	/**
	 * Determines whether the entity still exists in this world.
	 * @param id the entity's ID number
	 * @return true if entity is still alive
	 */
	public boolean exists(int id){
		return allEntities.get(id) != null;
	}

	/**
	 * Determines whether the entity still exists in this world.
	 * @param entity
	 * @return true if entity is still alive
	 */
	public boolean exists(BaseEntity entity){
		return exists(entity.ID());
	}

	/**
	 * This method must <b>only</b> be used before the main program starts. If it 
	 * is called during a world update then the program is likely to crash, in 
	 * this case use the birth() method instead. 
	 * @param entity the entity to add.
	 */
	public void add(BaseEntity entity){
		if(entity instanceof MovingEntity)
			add((MovingEntity) entity);
		else if(entity instanceof Artefact)
			add((Artefact) entity);
		else if(entity instanceof Obstacle)
			add((Obstacle) entity);
		else if(entity instanceof Building)
			add((Building) entity);
		else if(entity instanceof Wall)
			add((Wall) entity);
	}
	
	/**
	 * Remove an entity from the world. <br>
	 * 
	 * @param entity the entity to use
	 */
	private void remove(BaseEntity entity){
		/*
		 * Use of the instanceof is not good programming practice but since the
		 * entity type classes are in a different package it was the most practical
		 * way of hiding the internal library implementation.
		 * The same applies to the add(BaseEntity) method 
		 */
		if(entity instanceof MovingEntity)
			remove((MovingEntity) entity);
		else if(entity instanceof Artefact)
			remove((Artefact) entity);
		else if(entity instanceof Obstacle)
			remove((Obstacle) entity);
		else if(entity instanceof Building)
			remove((Building) entity);
		else if(entity instanceof Wall)
			remove((Wall) entity);
	}
	
	/**
	 * Add an artefact . <br>
	 * A artefact entity may or may not have a renderer and a state machine.
	 * 
	 */
	private void add(Artefact artefact){
		// Avoid null entries and duplicates
		if(artefact == null || exists(artefact.ID())) return;

		nbr_artefacts++;
		allEntities.put(artefact.ID(), artefact);

		if(cspOn){
			double x0 	= artefact.pos().x + artefact.getExtent().lowX;
			double y0 	= artefact.pos().y - artefact.getExtent().lowY;
			double x1 	= artefact.pos().x + artefact.getExtent().highX;
			double y1 	= artefact.pos().y + artefact.getExtent().highY;

			int left 	= (int) FastMath.floor(x0 / partSize);
			int right 	= (int) FastMath.floor(x1 / partSize);
			int top 	= (int) FastMath.floor(y0 / partSize);
			int bottom 	= (int) FastMath.floor(y1 / partSize);

			// Extended partition size
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					Point part = new Point(partX, partY);
					if(!artefact_parts.containsKey(part))
						artefact_parts.put(part, new HashSet<Artefact>());
					artefact_parts.get(part).add(artefact);
				}
			}
		}
		else {
			artefacts.add(artefact);
		}
	}

	/**
	 * Adds a building to the world.
	 * 
	 * @param b the building to add.
	 */
	private void add(Building b){
		// Avoid null entries and duplicates
		if(b == null || exists(b.ID())) return;

		nbr_buildings++;
		allEntities.put(b.ID(), b);

		if(cspOn){
			double x0 	= b.pos().x + b.getExtent().lowX;
			double y0 	= b.pos().y - b.getExtent().lowY;
			double x1 	= b.pos().x + b.getExtent().highX;
			double y1 	= b.pos().y + b.getExtent().highY;

			int left 	= (int) FastMath.floor(x0 / partSize)-1;
			int right 	= (int) FastMath.floor(x1 / partSize)+1;
			int top 	= (int) FastMath.floor(y0 / partSize)-1;
			int bottom 	= (int) FastMath.floor(y1 / partSize)+1;
			//			Point part = new Point();
			// Extended partition size
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					Point part = new Point(partX, partY);
					if(!building_parts.containsKey(part))
						building_parts.put(part, new HashSet<Building>());
					building_parts.get(part).add(b);
				}
			}
		}
		else {
			buildings.add(b);
		}
		// Now add the walls - they are invisible, have no renderer and no FSM
		Vector2D offset = b.pos();
		Vector2D[] contour = b.contour();
		// Create the walls
		Wall[] walls = new Wall[contour.length];
		for(int i = 1; i < contour.length; i++)
			walls[i] = (new Wall(Vector2D.add(contour[i-1], offset),
					Vector2D.add(contour[i], offset), false));
		walls[0] = (new Wall(Vector2D.add(contour[contour.length-1], offset), 
				Vector2D.add(contour[0], offset), false));
		// Register walls with building
		b.walls(walls);
		// Now add the walls to the world
		for(Wall w : walls)
			add(w);
	}

	/**
	 * Adds an obstacle to the world.
	 * @param obstacle the obstacle to add.
	 */
	private void add(Obstacle obstacle){
		// Avoid null entries and duplicates
		if(obstacle == null || exists(obstacle.ID())) return;

		nbr_obstacles++;
		allEntities.put(obstacle.ID(), obstacle);

		if(cspOn){
			double x0 	= obstacle.pos().x - obstacle.colRadius();
			double y0 	= obstacle.pos().y - obstacle.colRadius();
			double x1 	= obstacle.pos().x + obstacle.colRadius();
			double y1 	= obstacle.pos().y + obstacle.colRadius();

			int left 	= (int) FastMath.floor(x0 / partSize)-1;
			int right 	= (int) FastMath.floor(x1 / partSize)+1;
			int top 	= (int) FastMath.floor(y0 / partSize)-1;
			int bottom 	= (int) FastMath.floor(y1 / partSize)+1;
			//			Point part = new Point();
			// Extended partition size
			double extPartSize = partSize + 2 * partOverlap;
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					if(Geometry2D.box_box(x0, y0, x1, y1,
							partX * partSize - partOverlap, partY * partSize - partOverlap, 
							partX * partSize * partOverlap + extPartSize, partY * partSize + partOverlap + extPartSize)){
						Point part = new Point(partX, partY);
						if(!obstacle_parts.containsKey(part))
							obstacle_parts.put(part, new HashSet<Obstacle>());
						obstacle_parts.get(part).add(obstacle);
					}
				}
			}
		}
		else {
			obstacles.add(obstacle);
		}
	}

	/**
	 * Add a wall to the world. <br>
	 * When looking to see whether the wall passes through any particular partition
	 * then it will consider the extended partition i.e. as if it had been extended
	 * on all sides by 'partSize'. <br>
	 * partSize should be greater than the whisker length used in wall direction.
	 * 
	 * @param w
	 */
	private void add(Wall w){
		// Avoid null entries and duplicates
		if(w == null || exists(w.ID())) return;

		nbr_walls++;
		allEntities.put(w.ID(), w);

		if(cspOn){
			// Get the wall end points
			double x0 = w.pos().x;
			double x1 = w.getEndPos().x;
			double y0 = w.pos().y;
			double y1 = w.getEndPos().y;
			// Calculate the range of partitions to check
			int left 	= (int) (FastMath.floor(FastMath.min(x0, x1) / partSize) - 1);
			int right 	= (int) (FastMath.floor(FastMath.max(x0, x1) / partSize) + 1);
			int top 	= (int) (FastMath.floor(FastMath.min(y0, y1) / partSize) - 1);
			int bottom 	= (int) (FastMath.floor(FastMath.max(y0, y1) / partSize) + 1);
			// Extended partition size
			double extendedPartSize = partSize + 2 * partOverlap;
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					if(Geometry2D.line_box_xywh(x0, y0, x1, y1, 
							partX * partSize - partOverlap, partY * partSize - partOverlap, extendedPartSize, extendedPartSize)){
						// Add the wall
						Point part = new Point(partX, partY);
						if(!wall_parts.containsKey(part))
							wall_parts.put(part, new HashSet<Wall>());
						wall_parts.get(part).add(w);
					}
				}
			}
		}
		else
			walls.add(w);
	}

	/**
	 * Add a moving object to the world.
	 * @param me the moving object to add
	 */
	private void add(MovingEntity me){
		// Avoid null entries and duplicates
		if(me == null || exists(me.ID())) return;

		nbr_movers++;
		allEntities.put(me.ID(), me);

		if(cspOn){
			Point part = getPartition(me.pos().x, me.pos().y);
			if(!moving_parts.containsKey(part))
				moving_parts.put(part, new HashSet<MovingEntity>());
			moving_parts.get(part).add(me);
		}
		// Movers always exists even with cell partitioning
		movers.add(me);
	}

	/**
	 * Removes an artefact from the world. <br>
	 * The only safe way to remove this artefact is to use one of these methods - <br>
	 * <pre>void die(BaseEntity entity, double timeToLive)<br>
	 * void die(int entityID, double timeToLive)</pre>
	 */
	private void remove(Artefact artefact){
		if(artefact == null) return;

		boolean removed = false;
		allEntities.remove(artefact.ID());
		if(cspOn){
			double x0 	= artefact.pos().x - artefact.colRadius();
			double y0 	= artefact.pos().y - artefact.colRadius();
			double x1 	= artefact.pos().x + artefact.colRadius();
			double y1 	= artefact.pos().y + artefact.colRadius();
			// No need to expand partitions for an Artefact
			int left 	= (int) FastMath.floor(x0 / partSize);
			int right 	= (int) FastMath.floor(x1 / partSize);
			int top 	= (int) FastMath.floor(y0 / partSize);
			int bottom 	= (int) FastMath.floor(y1 / partSize);
			Point part = new Point();
			for(int partY = top; partY <= bottom; partY++){
				for(int partX = left; partX <= right; partX++){
					part.setLocation(partX, partY);
					if(obstacle_parts.containsKey(part) && obstacle_parts.get(part).remove(artefact))
						removed = true;	
				}
			}
		}
		else {
			removed = artefacts.remove(artefact);	
		}
		if(removed) nbr_artefacts--;
	}

	/**
	 * Removes an obstacle from the world. <br>
	 * The only safe way to remove an entity is to use one of these methods - <br>
	 * <pre>void die(BaseEntity entity, double timeToLive)<br>
	 * void die(int entityID, double timeToLive)</pre>
	 */
	private void remove(Obstacle obstacle){
		if(obstacle == null) return;
		boolean removed = false;
		if(allEntities.remove(obstacle.ID()) != null){
			if(cspOn){
				double x0 	= obstacle.pos().x - obstacle.colRadius();
				double y0 	= obstacle.pos().y - obstacle.colRadius();
				double x1 	= obstacle.pos().x + obstacle.colRadius();
				double y1 	= obstacle.pos().y + obstacle.colRadius();
				// Expand partitions to look at
				int left 	= (int) FastMath.floor(x0 / partSize)-1;
				int right 	= (int) FastMath.floor(x1 / partSize)+1;
				int top 	= (int) FastMath.floor(y0 / partSize)-1;
				int bottom 	= (int) FastMath.floor(y1 / partSize)+1;
				Point part = new Point();
				for(int partY = top; partY <= bottom; partY++){
					for(int partX = left; partX <= right; partX++){
						part.setLocation(partX, partY);
						if(obstacle_parts.containsKey(part) && obstacle_parts.get(part).remove(obstacle))
							removed = true;	
					}
				}
			}
			else {
				removed = obstacles.remove(obstacle);	
			}
		}
		if(removed) nbr_obstacles--;
	}

	/**
	 * Removes a wall from the world. <br>
	 * The only safe way to remove an entity is to use one of these methods - <br>
	 * <pre>void die(BaseEntity entity, double timeToLive)<br>
	 * void die(int entityID, double timeToLive)</pre>
	 */
	private void remove(Wall wall){
		if(wall == null) return;
		boolean removed = false;
		if(allEntities.remove(wall.ID()) != null){
			if(cspOn){
				double x0 = wall.pos().x;
				double x1 = wall.getEndPos().x;
				double y0 = wall.pos().y;
				double y1 = wall.getEndPos().y;
				// Expand partitions to look at
				int left 	= (int) FastMath.floor(x0 / partSize)-1;
				int right 	= (int) FastMath.floor(x1 / partSize)+1;
				int top 	= (int) FastMath.floor(y0 / partSize)-1;
				int bottom 	= (int) FastMath.floor(y1 / partSize)+1;
				Point part = new Point();
				for(int partY = top; partY <= bottom; partY++){
					for(int partX = left; partX <= right; partX++){
						part.setLocation(partX, partY);
						if(wall_parts.containsKey(part) && wall_parts.get(part).remove(wall))
							removed = true;	
					}
				}
			}
			else {
				removed  = walls.remove(wall);	
			}
		}
		if(removed) nbr_walls--;
	}

	/**
	 * Removes a building from the world. <br>
	 * The only safe way to remove an entity is to use one of these methods - <br>
	 * <pre>void die(BaseEntity entity, double timeToLive)<br>
	 * void die(int entityID, double timeToLive)</pre>
	 */
	private void remove(Building b){
		if(b == null) return;
		boolean removed = false;
		if(allEntities.remove(b.ID()) != null){
			// Remove associated wall
			for(Wall wall : b.walls())
				remove(wall);
			// Dis-associate walls from building
			b.walls(null);
			// Now remove the building itself
			if(cspOn){
				double x0 	= b.pos().x + b.getExtent().lowX;
				double y0 	= b.pos().y - b.getExtent().lowY;
				double x1 	= b.pos().x + b.getExtent().highX;
				double y1 	= b.pos().y + b.getExtent().highY;

				int left 	= (int) FastMath.floor(x0 / partSize)-1;
				int right 	= (int) FastMath.floor(x1 / partSize)+1;
				int top 	= (int) FastMath.floor(y0 / partSize)-1;
				int bottom 	= (int) FastMath.floor(y1 / partSize)+1;
				Point part = new Point();
				// Extended partition size
				for(int partY = top; partY <= bottom; partY++){
					for(int partX = left; partX <= right; partX++){
						part.setLocation(partX, partY);
						if(building_parts.containsKey(part) && building_parts.get(part).remove(b))
							removed = true;
					}
				}
			}
			else {
				removed  = buildings.remove(b);	
			}
			if(removed) nbr_buildings--;
		}
	}

	/**
	 * Removes a moving entity or vehicle from the world. <br>
	 * The only safe way to remove an entity is to use one of these methods - <br>
	 * <pre>void die(BaseEntity entity, double timeToLive)<br>
	 * void die(int entityID, double timeToLive)</pre>
	 */
	private void remove(MovingEntity me){
		allEntities.remove(me.ID());
		if(cspOn){
			Point part = getPartition(me.pos().x, me.pos().y);
			if(moving_parts.containsKey(part))
				moving_parts.get(part).remove(me);
		}
		// 'movers' always exists even with cell partitioning
		if(movers.remove(me))
			nbr_movers--;			
	}

	/**
	 * This is the preferred method to remove an entity from the 
	 * world once the game is in progress i.e. outside the 
	 * <pre>setup()</pre> method.
	 * 
	 * @param entity the entity to die
	 * @param timeToLive number of seconds of life left
	 */
	public void death(BaseEntity entity, double timeToLive){
		if(entity != null && allEntities.containsKey(entity.ID()))
			entityChangeList.add(new Changling(entity, timeToLive, true));
	}

	/**
	 * This is the preferred method to remove an entity from the 
	 * world once the game is in progress i.e. outside the 
	 * <pre>setup()</pre> method.
	 * 
	 * @param entityID the ID number of the entity to die
	 * @param timeToLive number of seconds of life left
	 */
	public void death(int entityID, double timeToLive){
		BaseEntity entity = allEntities.get(entityID);
		if(entity != null)
			entityChangeList.add(new Changling(entity, timeToLive, true));
	}

	/**
	 * This is the preferred method to add an entity to the 
	 * world once the game is in progress i.e. outside the 
	 * <pre>setup()</pre> method.
	 * 
	 * @param entity
	 * @param timeToBirth
	 */
	public void birth(BaseEntity entity, double timeToBirth){
		if(entity != null && !allEntities.containsKey(entity.ID()))
			entityChangeList.add(new Changling(entity, timeToBirth, false));
	}

	/**
	 * Cancels outstanding birth and deaths scheduled with birth and death methods.
	 */
	public void cancelBirthsAndDeaths(){
		entityChangeList.clear();
	}

	/**
	 * This is the core method which will loop through entity state machines (if any) then it will update 
	 * the positions and velocities of all moving entities (applying any steering behaviours used). <br>
	 * It also calculates and stores the average time taken to perform this update.
	 *  
	 * @param deltaTime the time in seconds since the last update.
	 */
	public void update(double deltaTime){
		// Initialise the update timer - this is used to measure the time taken to
		timer.reset();
		count++;

		// ===============================================================================
		// ===================== CHECK FOR ANY ENTITY BIRTHS OR DEATHS ===================
		if(!entityChangeList.isEmpty()){
			Iterator<Changling> iter = entityChangeList.iterator();
			while(iter.hasNext()){
				Changling d = iter.next();
				if(d.delay > 0)
					d.delay -= deltaTime;
				else {
					// Time up either kill or give birth to the entity
					// and then remove from the changeling list.
					if(d.toDie)
						remove(d.entity);
					else
						add(d.entity);
					iter.remove();
				}
			}
		}

		// ===============================================================================
		// =================== UPDATE STATE MACHINES AND TELEGRAMS =======================
		Dispatcher.update();
		for(BaseEntity be : allEntities.values())
			be.updateFSM(deltaTime, this);


		// ===============================================================================
		// ======== UPDATE ALL MOVING ENTITIES BASED ON THEIR STEERING BEHAVIOURS ========
		for(MovingEntity mover : movers)
			mover.update(deltaTime, this);

		// ===============================================================================
		// ======================== UPDATE ALL ARTEFACTS =================================
		if(cspOn){
			Collection<HashSet<Artefact>> parts = artefact_parts.values();
			for(HashSet<Artefact> part : parts)
				for(Artefact artefect : part)
					artefect.update(deltaTime, this);				
		}
		else {
			for(Artefact artefect : artefacts)
				artefect.update(deltaTime, this);
		}
		
		// ===============================================================================
		// ========== ENSURE THAT ALL MOVING ENTITIES DO NOT OVERLAP (IF REQD) ===========
		if(noOverlap){
			if(cspOn)
				ensureNoOverlap(deltaTime);
			else
				ensureNoOvelapInPartition(deltaTime, movers);
		}

		// ===============================================================================
		// ========== ENSURE MOVING ENTITIES ARE IN THE CORRECT PARTION (IF REQD) ========
		if(cspOn)
			checkPartitionMoves();

		// ===============================================================================
		// ======= CALCULATE THE AVERAGE UPDATE TIME =====================================
		// Calculate the average update time every 100 frames
		cumTime += timer.getElapsedTime();
		if(count >= 100){
			worldUpdateTime = cumTime / (double)count;
			cumTime = 0;
			count = 0;
		}

		// END OF WORLD UPDATE
	}

	/**
	 * Ensure no overlap between moving entities when using BSP
	 * ignoring entities that touch across partition barriers.
	 */
	private void ensureNoOverlap(double deltaTime){
		// Get the hash sets for each partition
		Collection<HashSet<MovingEntity>> c = moving_parts.values();
		// Check each partition
		for(HashSet<MovingEntity> part : c)
			ensureNoOvelapInPartition(deltaTime, part);
	}

	/**
	 * Ensure no overlap between moving entities within a partition. If BSP is not
	 * being used then the 'whole world' is a single partition so this method will
	 * be used for a set of all movers. <br>
	 * Entities that touch across partition barriers are ignored.
	 * 
	 */
	private void ensureNoOvelapInPartition(double deltaTime, Set<MovingEntity> partSet){
		int n = partSet.size();
		if(n > 1){
			LinkedList<MovingEntity> list = new LinkedList<MovingEntity>(partSet);
			for(int i = 0; i < n - 1; i++){
				MovingEntity mi = list.get(i);
				// If the first entity allows overlap forget it
				if(!mi.isOverLapAllowed()){
					for(int j = i + 1; j < n; j++){
						// If the second mover allows overlap forget it
						MovingEntity mj = list.get(j);
						if(!mj.isOverLapAllowed())
							ensureNoOverlap(deltaTime, mi, list.get(j)); // Use World class method
					}
				}
			}
		}
	}

	/**
	 * Checks to see if there is overlap between the entities. If there is it moves both 
	 * entities slightly apart along the collision plane normal. The distance an entity 
	 * moves is proportional to the mass of the other entity.
	 * 
	 * @param deltaTime
	 * @param m0
	 * @param m1
	 */
	private void ensureNoOverlap(double deltaTime, MovingEntity m0, MovingEntity m1){
		Vector2D collisionNormal = Vector2D.sub(m0.pos(), m1.pos());
		Vector2D collisionNormal0, collisionNormal1;
		double dist = collisionNormal.length();
		double overlap = m0.colRadius() + m1.colRadius() - dist; 
		if(overlap >= 0){
			Vector2D relVel = Vector2D.sub(m0.velocity(), m1.velocity());
			float nv = (float) collisionNormal.dot(relVel);
			float n2 = (float) collisionNormal.dot(collisionNormal);
			if(nv < 0 && n2 > 1E-5){
				collisionNormal.div(dist); // normalize				
				collisionNormal.mult(10 * overlap * deltaTime);
				// Assume they are both affected equally
				collisionNormal0 = collisionNormal1 = collisionNormal;
				double mass0 = m0.mass();
				double mass1 = m1.mass();
				if(mass0 != mass1){
					double mass = mass0 + mass1;;
					collisionNormal0 = Vector2D.mult(collisionNormal, 2 * mass1 / mass);
					collisionNormal1 = Vector2D.mult(collisionNormal, 2 * mass0 / mass);
				}
				m0.pos().add(collisionNormal0);
				m1.pos().sub(collisionNormal1);
			}
		}
	}

	/**
	 * If we are using space partitioning then check if movers have crossed a partition boundary and if so move it
	 * to the correct hash set
	 */
	private void checkPartitionMoves(){
		Point prevPart, currPart;
		Vector2D prevPos, currPos;
		for(MovingEntity mover : movers){
			prevPos = mover.prevPos();
			currPos = mover.pos();
			prevPart = new Point((int) FastMath.floor(prevPos.x / partSize), (int) FastMath.floor(prevPos.y / partSize));
			currPart = new Point((int) FastMath.floor(currPos.x / partSize), (int) FastMath.floor(currPos.y / partSize));
			if(prevPart.x != currPart.x || prevPart.y != currPart.y){
				// Remove from old partition
				moving_parts.get(prevPart).remove(mover);
				// Add to new partition
				if(!moving_parts.containsKey(currPart))
					moving_parts.put(currPart, new HashSet<MovingEntity>());
				moving_parts.get(currPart).add(mover);
			}
		}
	}

	/**
	 * DO NOT USE THIS METHOD <br/>
	 * This method is left to ensure backward compatibility with library versions prior to 1.0 <br/>
	 * Use the draw(elapsedTime) method instead.
	 * 
	 */
	@Deprecated
	public void draw(){
		makeDrawSet();
		// Draw the buildings, obstacles and walls
		for(BaseEntity entity : drawSet){
			entity.draw(this);
		}
	}

	/**
	 * Any entity that requires to be rendered will have its own render object. This method ensures that
	 * the draw method of each render object is executed. <br>
	 * It will not skip entities that are completely outside the visible area of the world. 
	 * @param elapsedTime the elapsed time since the last update
	 */
	public void draw(double elapsedTime){
		makeDrawSet();
		// Draw the buildings, obstacles and walls
		for(BaseEntity entity : drawSet){
			entity.draw(elapsedTime, this);
		}
	}

	/**
	 * Make a set of entities that are to be drawn in z order
	 */
	protected void makeDrawSet(){
		drawSet.clear();
		if(cspOn){
			int left 	= (int) FastMath.floor(viewOnWorld.lowX / partSize);
			int right 	= (int) FastMath.floor(viewOnWorld.highX / partSize);
			int top 	= (int) FastMath.floor(viewOnWorld.lowY / partSize);
			int bottom 	= (int) FastMath.floor(viewOnWorld.highY / partSize);
			Point p = new Point();
			// Collect a list of all buildings, obstacles and walls to be drawn so as to avoid duplication
			for(int partY = top; partY <= bottom; partY++){
				p.y = partY;
				for(int partX = left; partX <= right; partX++){
					p.x = partX;
					if(artefact_parts.containsKey(p))
						drawSet.addAll(artefact_parts.get(p));
					if(building_parts.containsKey(p))
						drawSet.addAll(building_parts.get(p));
					// Obstacles
					if(obstacle_parts.containsKey(p))
						drawSet.addAll(obstacle_parts.get(p));
					// Obstacles
					if(wall_parts.containsKey(p))
						drawSet.addAll(wall_parts.get(p));
					if(moving_parts.containsKey(p)){
						for(MovingEntity me : moving_parts.get(p))
							if(me.isInDomain(viewOnWorld))
								drawSet.add(me);
					}
				}
			}
		}
		else { // BSP off
			for(Artefact artefact : artefacts)
				if(artefact.isInDomain(viewOnWorld))
					drawSet.add(artefact);
			for(Building building : buildings)
				if(building.isInDomain(viewOnWorld))
					drawSet.add(building);
			for(Obstacle ob : obstacles)
				if(ob.isInDomain(viewOnWorld))
					drawSet.add(ob);
			for(Wall wall : walls)
				if(wall.isInDomain(viewOnWorld))
					drawSet.add(wall);
			for(MovingEntity mover : movers)
				if(mover.isInDomain(viewOnWorld))
					drawSet.add(mover);
		}
	}
	
	/**
	 * For a given point get the partition key
	 */
	private Point getPartition(double x, double y){
		return new Point( (int) FastMath.floor(x / partSize), (int) FastMath.floor(y / partSize) );
	}

	/**
	 * @return the bspOn
	 */
	public boolean isCspOn() {
		return cspOn;
	}

	/**
	 * @return the ensureZeroOverlap
	 */
	public boolean isNoOverlapOn() {
		return noOverlap;
	}

	/**
	 * @param noOverlap the ensureZeroOverlap to set
	 */
	public void noOverlap(boolean noOverlap) {
		this.noOverlap = noOverlap;
	}

	/**
	 * Used to display all collected steering force data by Vehicle <br>
	 * A vehicle must have it's force logger activated with <br>
	 * <pre>vehicle.forceLoggerOn(); </pre>
	 */
	public void printForceData(){
		System.out.println("\n==============================================================================");
		System.out.println("Steering Force Data Collected");
		for(MovingEntity me : movers){
			if(me instanceof Vehicle){
				Vehicle v = (Vehicle) me;
				if(v.hasForceRecorder() && v.forceRecorder().hasData())
					v.printForceData();
			}
		}
	}

	private class Changling {
		BaseEntity entity;
		double delay;
		boolean toDie;

		/**
		 * @param lifeSpan
		 * @param entity
		 */
		public Changling(BaseEntity entity, double lifeSpan, boolean toDie) {
			this.delay = lifeSpan;
			this.entity = entity;
			this.toDie = toDie;
		}
	} // End of class Changeling
	
}
