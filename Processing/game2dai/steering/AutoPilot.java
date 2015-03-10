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

package game2dai.steering;

import game2dai.World;
import game2dai.entities.BaseEntity;
import game2dai.entities.MovingEntity;
import game2dai.entities.Obstacle;
import game2dai.entities.Vehicle;
import game2dai.entities.Wall;
import game2dai.graph.Graph;
import game2dai.graph.GraphNode;
import game2dai.graph.GraphSearch_Astar;
import game2dai.graph.IGraphSearch;
import game2dai.maths.FastMath;
import game2dai.maths.Geometry2D;
import game2dai.maths.MathUtils;
import game2dai.maths.Transformations;
import game2dai.maths.Vector2D;
import game2dai.utils.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * AutoPilot Objects are used to control the motion of a Vehicle object by 
 * specifying a set of steering behaviours the Vehicle must obey. <br>
 * Every Vehicle object has it's own AutoPilot object (An AutoPilot object 
 * cannot be shared by multiple Vehicles) <br>
 * 
 * This class supports 16 different behaviours which can be combined to provide 
 * appropriate motion based on the game environments. <br>
 * 
 * A large number of constants are defined to simplify the use of this class.<br>
 * <b>Behaviour constants</b><br><pre>
 * WALL_AVOID	OBSTACLE_AVOID		EVADE		FLEE	SEPARATION	<br>
 * ALIGNMENT 	COHESION			SEEK		ARRIVE	WANDER <br>
 * PURSUIT		OFFSET_PURSUIT		INTERPOSE	HIDE	PATH	<br>
 * FLOCK</pre><br><br>
 * 
 * <b>Force calculation constants.</b> The default is WEIGHTED_PRIORITIZED<br><pre>
 * WEIGHTED   WEIGHTED_PRIORITIZED </pre><br><br>
 * 
 * <b>Agents (other Vehicle objects) to pursue, evade etc.</b><br><pre>
 * AGENT0	AGENT1		AGENT_TO_PURSUE		AGENT_TO_FLEE </pre><br><br>
 * 
 * @author Peter Lager
 *
 */
public class AutoPilot implements Cloneable, SBF{

	private static double WANDER_MIN_ANGLE 		= -1.0 * FastMath.PI;
	private static double WANDER_MAX_ANGLE 		= FastMath.PI;
	private static double WANDER_ANGLE_RANGE 	=  2.0 * FastMath.PI;

	// These are used during force calculations so do not need to be cloned
	private Set<Obstacle> obstacles = null;
	private Set<Wall> walls = null;
	private Set<MovingEntity> movers = null;

	// these values are passed as parameters and are stored at the start
	// of the calculateForce method so do not need to be cloned
	private double deltaTime = 0;
	private World world;

	// This is set when this steering behaviour is added to a vehicle
	// it indicates the owner of this behaviour
	private Vehicle owner = null;   

	// ======================================================================
	// The following variables need to be cloned as they are unique 
	// to the particular behaviour.
	// ======================================================================
	private int flags = 0;

	private Vector2D accum = new Vector2D();
	private Vector2D f = new Vector2D();
	private Vector2D steeringForce = new Vector2D();

	// How should the steering force be calculated
	private int forceCalcMethod = WEIGHTED_PRIORITIZED;
	
	// Target for arrive and seek
	private Vector2D gotoTarget = new Vector2D();
	// Deceleration rate for arrive
	private int arriveRate = NORMAL;
	private double arriveDist = 0.5;

	private Vector2D fleeTarget = new Vector2D();
	// Panic distance squared for flee to be effective
	private double fleeRadius = 100;

	// Used in path following
	private LinkedList<GraphNode> path = new LinkedList<GraphNode>();
	private double pathSeekDist = 20;                                          
	private double pathArriveDist = 0.5;                                          

	// Obstacle avoidance
	private double detectBoxLength = 20.0;

	/**
	 * the first 2 are used for the interpose
	 * AGENT0, AGENT1, AGENT_TO_PURSUE, AGENT_TO_EVADE
	 */
	private MovingEntity[] agents = new MovingEntity[NBR_AGENT_ARRAY];
	
	private Vector2D pursueOffset = new Vector2D();

	// the current angle to target on the wander circle 
	private double wanderAngle = 0;
	// the maximum amount of angular displacement per second
	private double wanderAngleJitter = 60;
	// the radius of the constraining circle for the wander behaviour
	private double wanderRadius = 20.0;
	// distance the wander circle is projected in front of the agent
	private double wanderDist = 60.0;
	// The maximum angular displacement in this frame.
	private double wanderAngleDelta = 0;

	// Cats whiskers used for wall avoidance
	private int nbrWhiskers = 5;
	private double whiskerFOV = FastMath.PI; // radians
	private double whiskerLength = 30;
	private boolean ovalEnvelope = false;

	// The maximum distance between moving entities for them to be considered
	// as neighbours. Used for group behaviours
	private double neighbourDist = 100.0;

	/**
	 * DO NOT USE THIS METHOD <br>
	 * This method is for sole use by the Vehicle class when a steering
	 * behaviour is added.
	 * 
	 * @param vehicle
	 */
	public AutoPilot setOwner(final Vehicle vehicle){
		this.owner = vehicle;
		return this;
	}

	/**
	 * Used to see if this auto-pilot has an owner.
	 * @return true if the AP has an owner
	 */
	public boolean hasOwner(){
		return owner != null;
	}
	
	/*
	 * **************************************************************************************
	 * **************************************************************************************
	 * 
	 * The following section has all the methods to switch on/off and get/set the tweak
	 * factors  for the various behaviours.
	 * 
	 * **************************************************************************************
	 * **************************************************************************************
	 */
	

	/**
	 * Switch off all steering behaviours
	 * @return this auto-pilot object
	 */
	public AutoPilot allOff(){
		flags = 0;
		return this;		
	}

	/*
	 * =======================================================================================
	 * 			WALL AVOIDANCE
	 * =======================================================================================
	 */
	
	/**
	 * Switch off wall avoidance
	 * @return this auto-pilot object
	 */
	public AutoPilot wallAvoidOff(){
		flags &= (ALL_SB_MASK - WALL_AVOID);
		return this;		
	}
	
	/**
	 * Switch on wall avoidance
	 * @return this auto-pilot object
	 */
	public AutoPilot wallAvoidOn(){
		flags |= WALL_AVOID;
		return this;		
	}
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot wallAvoidWeight(double weight){
		weightings[BIT_WALL_AVOID] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double wallAvoidWeight(){
		return weightings[BIT_WALL_AVOID];
	}

	/**
	 * Is wall avoidance switched on?
	 */
	public boolean isWallAvoidOn(){
		return (flags & WALL_AVOID) != 0;
	}
	
	/**
	 * Set some or all of the factors used for wall avoidance. <br>
	 * Only provide values for the factors you want to set, pass 'null'
	 * for any factor that is to be unchanged. <br>
	 * Where appropriate validation will be applied to the value passed
	 * and if invalid (eg out of permitted range) will be silently ignored
	 * (no warning message) and the factor will remain unchanged.
	 * 
	 * @param nbrWhiskers the number of feelers to use (>0)
	 * @param whiskerLength the length of the feelers (>0)
	 * @param fov the arc angle (radians) covered by the feeler array (>0 and <= 2Pi
	 * @param shortOnSide if true then the side feelers are shorter than front facing feelers else all feelers are the same length
	 * @return this auto-pilot object
	 */
	public AutoPilot wallAvoidFactors(Object nbrWhiskers, Object whiskerLength, Object fov, Boolean shortOnSide){
		Integer i;
		Double d;
		if(null != (i = MathUtils.getInteger(nbrWhiskers)) && i > 0)
			this.nbrWhiskers = i;
		if(null != (d = MathUtils.getDouble(whiskerLength)) && d > 0)
			this.whiskerLength = d;
		if(null != (d = MathUtils.getDouble(fov)) && d > 0 && d <= FastMath.TWO_PI)
			this.whiskerFOV = d;
		if(shortOnSide != null)
			this.ovalEnvelope = shortOnSide;
		return this;
	}
	
	/**
	 * Get the number of whiskers used.
	 */
	public int wallAvoidNbrWhiskers() {
		return nbrWhiskers;
	}

	/**
	 * Get the angle covered by the whiskers
	 */
	public double wallAvoidFOV() {
		return whiskerFOV;
	}

	/**
	 * Get the length of the whiskers
	 * @return the whiskerLength
	 */
	public double wallAvoidWhiskerLength() {
		return whiskerLength;
	}

	/**
	 * If on then the side whiskers are smaller than the front whiskers.
	 */
	public boolean isWallAvoidOvalEnvelopeOn(){
		return ovalEnvelope;
	}
	
	/*
	 * =======================================================================================
	 * 			OBSTACLE AVOIDANCE
	 * =======================================================================================
	 */
	
	/**
	 * Switch off obstacle avoidance
	 * @return this auto-pilot object
	 */
	public AutoPilot obstacleAvoidOff(){
		flags &= (ALL_SB_MASK - OBSTACLE_AVOID);
		return this;		
	}

	/**
	 * Switch on obstacle avoidance
	 * @return this auto-pilot object
	 */
	public AutoPilot obstacleAvoidOn(){
		flags |= OBSTACLE_AVOID;
		return this;		
	}

	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot obstacleAvoidWeight(double weight){
		weightings[BIT_OBSTACLE_AVOID] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double obstacleAvoidWeight(){
		return weightings[BIT_OBSTACLE_AVOID];
	}

	/**
	 * Only provided to enable drawing the box during testing. <br>
	 * 
	 * @return the detection box length
	 */
	public double obstacleAvoidDetectBoxLength(){
		return detectBoxLength;
	}

	/**
	 * Set the obstacle avoidance detection box length.
	 * @param boxLength new length
	 * @return this auto-pilot object
	 */
	public AutoPilot obstacleAvoidDetectBoxLength(double boxLength){
		this.detectBoxLength = boxLength;
		return this;
	}


	/**
	 * Is wall avoidance switched on?
	 */
	public boolean isObstacleAvoidOn(){
		return (flags & OBSTACLE_AVOID) != 0;
	}
	
	/**
	 * Set the factors used for obstacle avoidance. <br>
	 * There is only one the detectBoxLength the lager the 
	 * value the earlier it will see the obstacle.
	 * @param detectBoxLength detect box length (>0)
	 * @return this auto-pilot object
	 */
	public AutoPilot obstacleAvoidFactors(double detectBoxLength){
		if(detectBoxLength > 0)
			this.detectBoxLength = detectBoxLength;
		return this;
	}
	
	/*
	 * =======================================================================================
	 * 			EVADE
	 * =======================================================================================
	 */
	
	/**
	 * Switch off evade pursuer
	 * @return this auto-pilot object
	 */
	public AutoPilot evadeOff(){
		flags &= (ALL_SB_MASK - EVADE);
		return this;		
	}

	/**
	 * Switch on evade pursuer
	 * @return this auto-pilot object
	 */
	public AutoPilot evadeOn(){
		flags |= EVADE;
		return this;		
	}
	
	/**
	 * Switch on evade pursuer and specify the entity to avoid.
	 * @param me the entity to evade
	 * @return this auto-pilot object
	 */
	public AutoPilot evadeOn(MovingEntity me){
		agents[AGENT_TO_EVADE] = me;
		flags |= EVADE;
		return this;		
	}

	/**
	 * Get current distance from the owning entity and the moving entity
	 * we want to evade.
	 * @return distance in world units
	 */
	public double evadeDistance(){
		return Vector2D.dist(owner.pos(), agents[AGENT_TO_EVADE].pos());
	}
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot evadeWeight(double weight){
		weightings[BIT_EVADE] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double evadeWeight(){
		return weightings[BIT_EVADE];
	}

	/**
	 * Is evade switched on?
	 */
	public boolean isEvadeOn(){
		return (flags & EVADE) != 0;
	}
	
	/**
	 * Set the factors used for evade behaviour. <br>
	 * There is only one factor the flee distance. While the distance 
	 * between this entity and the evade agent is less than this the
	 * entity will move away from the evade agent.
	 * 
	 * @param fleeDistance the distance we want to evade by (>0)
	 * @return this auto-pilot object
	 */
	public AutoPilot evadeFactors(double fleeDistance){
		if(fleeDistance > 0)
			this.fleeRadius = fleeDistance;
		return this;
	}
	
	/*
	 * =======================================================================================
	 * 			SEEK
	 * =======================================================================================
	 */

	/**
	 * Switch off seek
	 * @return this auto-pilot object
	 */
	public AutoPilot seekOff(){
		flags &= (ALL_SB_MASK - SEEK);
		return this;		
	}
	
	/**
	 * Switch on seek
	 * @return this auto-pilot object
	 */
	public AutoPilot seekOn(){
		flags |= SEEK;
		return this;		
	}
	
	/**
	 * Switch on seek
	 * @param target the location to seek
	 * @return this auto-pilot object
	 */
	public AutoPilot seekOn(Vector2D target){
		flags |= SEEK;
		gotoTarget.set(target);
		return this;		
	}
	
	/**
	 * Switch on seek
	 * @param x the x position of the target to seek
	 * @param y the y position of the target to seek
	 * @return this auto-pilot object
	 */
	public AutoPilot seekOn(double x, double y){
		flags |= SEEK;
		gotoTarget.set(x,y);
		return this;		
	}
	
	/**
	 * Get current distance from the owning entity to the seek target position.
	 * 
	 * @return distance in world units
	 */
	public double seekDistance(){
		return Vector2D.dist(owner.pos(), gotoTarget);
	}

	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot seekWeight(double weight){
		weightings[BIT_SEEK] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double seekWeight(){
		return weightings[BIT_SEEK];
	}

	/**
	 * Is seek switched on?
	 */
	public boolean isSeekOn(){
		return (flags & SEEK) != 0;
	}
	
	/*
	 * =======================================================================================
	 * 			ARRIVE
	 * =======================================================================================
	 */

	/**
	 * Switch off arrive
	 * @return this auto-pilot object
	 */
	public AutoPilot arriveOff(){
		flags &= (ALL_SB_MASK - ARRIVE);
		return this;		
	}
	
	/**
	 * Switch on arrive
	 * @return this auto-pilot object
	 */
	public AutoPilot arriveOn(){
		flags |= ARRIVE;
		return this;		
	}
	
	/**
	 * Switch on arrive. <br>
	 * The approach rate is unchanged by this method.
	 * @param target the location to arrive at
	 * @return this auto-pilot object
	 */
	public AutoPilot arriveOn(Vector2D target){
		flags |= ARRIVE;
		gotoTarget.set(target);
		return this;		
	}
	
	/**
	 * Switch on arrive and define speed of approach. This should
	 * be SBF.SLOW, SBF.NORMAL or SBF.FAST any other value will be
	 * result in reverting to default (NORMAL) 
	 * @param target the location to arrive at
	 * @param speed the approach rate
	 * @return this auto-pilot object
	 */
	public AutoPilot arriveOn(Vector2D target, int speed){
		flags |= ARRIVE;
		gotoTarget.set(target);
		switch(speed){
		case SLOW:
		case NORMAL:
		case FAST:
			arriveRate = speed;
			break;
		default:
			arriveRate = NORMAL;
		}
		return this;		
	}

	/**
	 * Switch on arrive
	 * @param x the x position of the target to arrive at
	 * @param y the y position of the target to arrive at
	 * @return this auto-pilot object
	 */
	public AutoPilot arriveOn(double x, double y){
		flags |= ARRIVE;
		gotoTarget.set(x,y);
		return this;		
	}
	
	/**
	 * Switch on arrive and define speed of approach. This should
	 * be SBF.SLOW, SBF.NORMAL or SBF.FAST any other value will be
	 * result in reverting to default (NORMAL) 
	 * @param x the x position of the target to arrive at
	 * @param y the y position of the target to arrive at
	 * @param speed the approach rate
	 * @return this auto-pilot object
	 */
	public AutoPilot arriveOn(double x, double y, int speed){
		flags |= ARRIVE;
		gotoTarget.set(x,y);
		switch(speed){
		case SLOW:
		case NORMAL:
		case FAST:
			arriveRate = speed;
			break;
		default:
			arriveRate = NORMAL;
		}
		return this;		
	}
	
	/**
	 * Get current distance from the owning entity to the arrive target position.
	 * 
	 * @return distance in world units
	 */
	public double arriveDistance(){
		return Vector2D.dist(owner.pos(), gotoTarget);
	}

	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot arriveWeight(double weight){
		weightings[BIT_ARRIVE] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double arriveWeight(){
		return weightings[BIT_ARRIVE];
	}

	/**
	 * Is arrive switched on?
	 */
	public boolean isArriveOn(){
		return (flags & ARRIVE) != 0;
	}
	
	/*
	 * =======================================================================================
	 * 			FLEE
	 * =======================================================================================
	 */

	/**
	 * Switch off flee
	 * @return this auto-pilot object
	 */
	public AutoPilot fleeOff(){
		flags &= (ALL_SB_MASK - FLEE);
		return this;		
	}
	
	/**
	 * Switch on flee
	 * @return this auto-pilot object
	 */
	public AutoPilot fleeOn(){
		flags |= FLEE;
		return this;		
	}
	
	/**
	 * Switch on flee
	 * @param location the location to flee from
	 * @return this auto-pilot object
	 */
	public AutoPilot fleeOn(Vector2D location){
		flags |= FLEE;
		fleeTarget.set(location);
		return this;		
	}
	
	/**
	 * Switch on flee
	 * @param x the x position of the location to flee from
	 * @param y the y position of the location to flee from
	 * @return this auto-pilot object
	 */
	public AutoPilot fleeOn(double x, double y){
		flags |= FLEE;
		fleeTarget.set(x,y);
		return this;		
	}

	/**
	 * Get current distance from the owning entity to the flee target position.
	 * 
	 * @return distance in world units
	 */
	public double fleeDistance(){
		return Vector2D.dist(owner.pos(), fleeTarget);
	}

	/**
	 * The flee force applied will be zero if the entity is outside
	 * the flee radius
	 * @param fleeRadius
	 * @return this auto-pilot object
	 */
	public AutoPilot fleeRadius(double fleeRadius){
		if(fleeRadius > 0)
			this.fleeRadius = fleeRadius;
		return this;
	}

	/**
	 * The effective range of the flee target.
	 */
	public double fleeRadius(){
		return fleeRadius;
	}
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot fleeWeight(double weight){
		weightings[BIT_FLEE] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double fleeWeight(){
		return weightings[BIT_FLEE];
	}

	/**
	 * Is seek switched on?
	 */
	public boolean isFleeOn(){
		return (flags & FLEE) != 0;
	}
	
	/**
	 * Set the factors used for flee behaviour agent. <br>
	 * There is only one factor the flee distance. While the distance 
	 * between this entity and the flee-target is less than this the
	 * entity will move away from it. <br>
	 * 
	 * @param fleeDistance the distance we want to evade by (>0)
	 * @return this auto-pilot object
	 */
	public AutoPilot fleeFactors(double fleeDistance){
		if(fleeDistance > 0)
			this.fleeRadius = fleeDistance;
		return this;
	}

	/*
	 * =======================================================================================
	 * 			SEPARATION
	 * =======================================================================================
	 */
	
	/**
	 * Switch off separation
	 * @return this auto-pilot object
	 */
	public AutoPilot separationOff(){
		flags &= (ALL_SB_MASK - SEPARATION);
		return this;		
	}
	
	/**
	 * Switch on separation
	 * @return this auto-pilot object
	 */
	public AutoPilot separationOn(){
		flags |= SEPARATION;
		return this;		
	}
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot separationWeight(double weight){
		weightings[BIT_SEPARATION] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double separationWeight(){
		return weightings[BIT_SEPARATION];
	}

	/**
	 * Is separation switched on?
	 */
	public boolean isSeparationOn(){
		return (flags & SEPARATION) != 0;
	}
	
	/*
	 * =======================================================================================
	 * 			ALIGNMENT
	 * =======================================================================================
	 */
	
	/**
	 * Switch off alignment
	 * @return this auto-pilot object
	 */
	public AutoPilot alignmentOff(){
		flags &= (ALL_SB_MASK - ALIGNMENT);
		return this;		
	}
	
	/**
	 * Switch on alignment
	 * @return this auto-pilot object
	 */
	public AutoPilot alignmentOn(){
		flags |= ALIGNMENT;
		return this;		
	}
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot alignmentWeight(double weight){
		weightings[BIT_ALIGNMENT] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double alignmentWeight(){
		return weightings[BIT_ALIGNMENT];
	}

	/**
	 * Is alignment switched on?
	 */
	public boolean isAlignmentOn(){
		return (flags & ALIGNMENT) != 0;
	}
	
	/*
	 * =======================================================================================
	 * 			COHESION
	 * =======================================================================================
	 */
	
	/**
	 * Switch off cohesion
	 * @return this auto-pilot object
	 */
	public AutoPilot cohesionOff(){
		flags &= (ALL_SB_MASK - COHESION);
		return this;		
	}
	
	/**
	 * Switch on cohesion
	 * @return this auto-pilot object
	 */
	public AutoPilot cohesionOn(){
		flags |= COHESION;
		return this;		
	}
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot cohesionWeight(double weight){
		weightings[BIT_COHESION] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double cohesionWeight(){
		return weightings[BIT_COHESION];
	}

	/**
	 * Is cohesion switched on?
	 */
	public boolean isCohesionOn(){
		return (flags & COHESION) != 0;
	}
	
	/*
	 * =======================================================================================
	 * 			FLOCK
	 * =======================================================================================
	 */
	
	/**
	 * Switch off flock
	 * @return this auto-pilot object
	 */
	public AutoPilot flockOff(){
		flags &= (ALL_SB_MASK - FLOCK);
		return this;		
	}
	
	/**
	 * Switch on flock
	 * @return this auto-pilot object
	 */
	public AutoPilot flockOn(){
		flags |= FLOCK;
		return this;		
	}
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot flockWeight(double weight){
		weightings[BIT_FLOCK] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double flockWeight(){
		return weightings[BIT_FLOCK];
	}

	/**
	 * Is flock switched on?
	 */
	public boolean isFlockOn(){
		return (flags & FLOCK) != 0;
	}
	
	/**
	 * Sets the radius for ALL the group behaviours (separation, alignment 
	 * and cohesion) even if you are using them without flocking.
	 * 
	 * @param neighbourhoodRadius the neighbourhood radius
	 */
	public AutoPilot flockFactors(double neighbourhoodRadius){
		neighbourDist = neighbourhoodRadius;
		return this;
	}

	/*
	 * =======================================================================================
	 * 			WANDER
	 * =======================================================================================
	 */
	
	/**
	 * Switch off wander
	 * @return this auto-pilot object
	 */
	public AutoPilot wanderOff(){
		flags &= (ALL_SB_MASK - WANDER);
		return this;		
	}
	
	/**
	 * Switch on wander
	 * @return this auto-pilot object
	 */
	public AutoPilot wanderOn(){
		flags |= WANDER;
		return this;		
	}
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot wanderWeight(double weight){
		weightings[BIT_WANDER] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double wanderWeight(){
		return weightings[BIT_WANDER];
	}

	/**
	 * Is wander switched on?
	 */
	public boolean isWanderOn(){
		return (flags & WANDER) != 0;
	}
	
	/**
	 * Set some or all of the factors used for wander behaviour. <br>
	 * Only provide values for the factors you want to set, pass 'null'
	 * for any factor that is to be unchanged. <br>
	 * Where appropriate validation will be applied to the value passed
	 * and if invalid (eg out of permitted range) will be silently ignored
	 * (no warning message) and the factor will remain unchanged.
	 * 
	 * @param dist
	 * @param radius
	 * @param jitter
	 * @return this auto-pilot object
	 */
	public AutoPilot wanderFactors(Object dist, Object radius, Object jitter){
		Double d;
		if(null != (d = MathUtils.getDouble(dist)) && d > 0)
			this.wanderDist = d;
		if(null != (d = MathUtils.getDouble(radius)) && d > 0)
			this.wanderRadius = d;
		if(null != (d = MathUtils.getDouble(jitter)) && d > 0)
			this.wanderAngleJitter = d;
		return this;
	}
	
	/**
	 * @return the wanderRadius
	 */
	public double wanderRadius() {
		return wanderRadius;
	}

	/**
	 * @return the wanderDist
	 */
	public double wanderDist() {
		return wanderDist;
	}

	/**
	 * Gets the current angle in the wander radius circle
	 * @return the wander angle
	 */
	public double wanderAngle(){
		return wanderAngle;
	}

	/**
	 * Gets the maximum amount of jitter allowed per second
	 */
	public double wanderAngleJitter(){
		return wanderAngleJitter;
	}

	
	public double wanderAngleDelta(){
		return wanderAngleDelta;
	}
	
	/*
	 * =======================================================================================
	 * 			PURSUIT
	 * =======================================================================================
	 */
	
	/**
	 * Switch off pursuit pursuer
	 * @return this auto-pilot object
	 */
	public AutoPilot pursuitOff(){
		flags &= (ALL_SB_MASK - PURSUIT);
		return this;		
	}

	/**
	 * Switch on pursuit pursuer
	 * @return this auto-pilot object
	 */
	public AutoPilot pursuitOn(){
		flags |= PURSUIT;
		return this;		
	}
	
	/**
	 * Switch on pursuit and specify the entity to persue.
	 * @param me the entity to pursuit
	 * @return this auto-pilot object
	 */
	public AutoPilot pursuitOn(MovingEntity me){
		agents[AGENT_TO_PURSUE] = me;
		flags |= PURSUIT;
		return this;		
	}

	/**
	 * Get current distance from the owning entity and the moving entity
	 * we want are pursuing.
	 * @return distance in world units
	 */
	public double pursuitDistance(){
		return Vector2D.dist(owner.pos(), agents[AGENT_TO_PURSUE].pos());
	}

	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot pursuitWeight(double weight){
		weightings[BIT_PURSUIT] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double pursuitWeight(){
		return weightings[BIT_PURSUIT];
	}

	/**
	 * Is pursuit switched on?
	 */
	public boolean isPursuitOn(){
		return (flags & PURSUIT) != 0;
	}
	
	
	/*
	 * =======================================================================================
	 * 			OFFSET_PURSUIT
	 * =======================================================================================
	 */
	
	/**
	 * Switch off offsetPursuit
	 * @return this auto-pilot object
	 */
	public AutoPilot offsetPursuitOff(){
		flags &= (ALL_SB_MASK - OFFSET_PURSUIT);
		return this;		
	}
	
	/**
	 * Switch on offsetPursuit
	 * @return this auto-pilot object
	 */
	public AutoPilot offsetPursuitOn(){
		flags |= OFFSET_PURSUIT;
		return this;		
	}
	
	/**
	 * Set the target enitiy for offsetPursuit and the offset.
	 * 
	 * @param me
	 * @param offset
	 * @return this auto-pilot object
	 */
	public AutoPilot offsetPursuitOn(MovingEntity me, Vector2D offset){
		if(offset != null)
			pursueOffset.set(offset);
		if(me != null)
			agents[SBF.AGENT_TO_PURSUE] = me;
		flags |= OFFSET_PURSUIT;
		return this;		
	}
	
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot offsetPursuitWeight(double weight){
		weightings[BIT_OFFSET_PURSUIT] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double offsetPursuitWeight(){
		return weightings[BIT_OFFSET_PURSUIT];
	}

	/**
	 * Is offsetPursuit switched on?
	 */
	public boolean isOffsetPursuitOn(){
		return (flags & OFFSET_PURSUIT) != 0;
	}
	
	/*
	 * =======================================================================================
	 * 			INTERPOSE
	 * =======================================================================================
	 */
	
	/**
	 * Switch off interpose
	 * @return this auto-pilot object
	 */
	public AutoPilot interposeOff(){
		flags &= (ALL_SB_MASK - INTERPOSE);
		return this;		
	}
	
	/**
	 * Switch on interpose
	 * @return this auto-pilot object
	 */
	public AutoPilot interposeOn(){
		flags |= INTERPOSE;
		return this;		
	}
	
	/**
	 * Switch on interpose and specify the two moving entities
	 * to get between.
	 * @param me0 the first mover
	 * @param me1 the second mover
	 * @return this auto-pilot object
	 */
	public AutoPilot interposeOn(MovingEntity me0, MovingEntity me1){
		agents[AGENT0] = me0;
		agents[AGENT1] = me1;
		flags |= INTERPOSE;
		return this;		
	}
	
	/**
	 * Switch on interpose and specify a moving entity and a fixed point
	 * in the world to get between.
	 * @param me0 the first mover
	 * @param fixedPos1 fixed position in the world.
	 * @return this auto-pilot object
	 */
	public AutoPilot interposeOn(MovingEntity me0, Vector2D fixedPos1){
		agents[AGENT0] = me0;
		// Create dummy moving entity to represent fixed poit
		agents[AGENT1] = new MovingEntity(
				fixedPos1,		// Position of fixed point
				1,				// radius
				Vector2D.ZERO,	// Velocity
				0,				// Max speed
				Vector2D.PLUS_I, // heading
				1,				// mass
				1,				// max turn rate
				0				// max force
				);
		flags |= INTERPOSE;
		return this;		
	}

	/**
	 * Switch on interpose and specify two fixed points
	 * in the world to get between. <br>
	 * This is overkill in this situation, instead  use arrive behaviour 
	 * to the halfway position between the fixed points.
	 * 
	 * @param fixedPos0 first fixed position in the world.
	 * @param fixedPos1 second fixed position in the world.
	 * @return this auto-pilot object
	 */
	public AutoPilot interposeOn(Vector2D fixedPos0, Vector2D fixedPos1){
		agents[AGENT0] =  new MovingEntity(
				fixedPos0,		// Position of fixed point
				1,				// radius
				Vector2D.ZERO,	// Velocity
				0,				// Max speed
				Vector2D.PLUS_I, // heading
				1,				// mass
				1,				// max turn rate
				0				// max force
				);
		// Create dummy moving entity to represent fixed poit
		agents[AGENT1] = new MovingEntity(
				fixedPos1,		// Position of fixed point
				1,				// radius
				Vector2D.ZERO,	// Velocity
				0,				// Max speed
				Vector2D.PLUS_I, // heading
				1,				// mass
				1,				// max turn rate
				0				// max force
				);
		flags |= INTERPOSE;
		return this;		
	}

	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot interposeWeight(double weight){
		weightings[BIT_INTERPOSE] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double interposeWeight(){
		return weightings[BIT_INTERPOSE];
	}

	/**
	 * Is interpose switched on?
	 */
	public boolean isInterposeOn(){
		return (flags & INTERPOSE) != 0;
	}
	
	/*
	 * =======================================================================================
	 * 			HIDE
	 * =======================================================================================
	 */
	
	/**
	 * Switch off hide
	 * @return this auto-pilot object
	 */
	public AutoPilot hideOff(){
		flags &= (ALL_SB_MASK - HIDE);
		return this;		
	}
	
	/**
	 * Switch on hide
	 * @return this auto-pilot object
	 */
	public AutoPilot hideOn(){
		flags |= HIDE;
		return this;		
	}
	
	public AutoPilot hideOn(MovingEntity me){
		agents[AGENT_TO_EVADE] = me;
		flags |= HIDE;
		return this;		
	}
	
	/**
	 * Get current distance from the owning entity and the moving entity
	 * we want are hiding from.
	 * @return distance in world units
	 */
	public double hideDistance(){
		return Vector2D.dist(owner.pos(), agents[AGENT_TO_EVADE].pos());
	}

	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot hideWeight(double weight){
		weightings[BIT_HIDE] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double hideWeight(){
		return weightings[BIT_HIDE];
	}

	/**
	 * Is hide switched on?
	 */
	public boolean isHideOn(){
		return (flags & HIDE) != 0;
	}
	
	/*
	 * =======================================================================================
	 * 			PATH
	 * =======================================================================================
	 */
	
	/**
	 * Switch off path
	 * @return this auto-pilot object
	 */
	public AutoPilot pathOff(){
		flags &= (ALL_SB_MASK - PATH);
		return this;		
	}
	
	/**
	 * Switch on path
	 * @return this auto-pilot object
	 */
	public AutoPilot pathOn(){
		flags |= PATH;
		return this;		
	}
	
	/**
	 * Set the weight for this behaviour
	 * @param weight the weighting to be applied to this behaviour.
	 * @return this auto-pilot object
	 */
	public AutoPilot pathWeight(double weight){
		weightings[BIT_PATH] = weight;
		return this;
	}

	/**
	 * Get the weighting for this behaviour
	 */
	public double pathWeight(){
		return weightings[BIT_PATH];
	}

	/**
	 * Is path switched on?
	 */
	public boolean isPathOn(){
		return (flags & PATH) != 0;
	}
	
	/**
	 * 
	 * @param seekDist distance to way-point when considered reached
	 * @param arriveDist distance to destination when considered reached
	 * @return this auto-pilot object
	 */
	public AutoPilot pathFactors(Object seekDist, Object arriveDist){
		Double d;
		if(null != (d = MathUtils.getDouble(seekDist)) && d > 0)
			this.pathSeekDist = d;
		if(null != (d = MathUtils.getDouble(arriveDist)) && d > 0)
			this.pathArriveDist = d;
		return this;
	}

	/**
	 * Get the path the entity is following
	 * @return linked list of nodes in the current path 
	 */
	public LinkedList<GraphNode> pathRoute(){
		return path;
	}

	/**
	 * Get the number of way-points left on the current route
	 */
	public int pathRouteLength(){
		return path.size();
	}

	/**
	 * Set the path the entity should follow. Ignore if route is null or has
	 * less than 2 nodes.
	 * @param route the path to follow
	 * @return this auto-pilot object
	 */
	public AutoPilot pathSetRoute(Vector2D[] route){
		if(route != null && route.length >= 2){
			path.clear();
			for(Vector2D v : route)
				path.add(new GraphNode(-1, v.x, v.y));
			pathOn();
		}
		return this;
	}

	/**
	 * Add the route to the end of the existing path and switch path following on.
	 *  Do nothing if the parameter is null or empty.
	 * @param route the path to follow
	 * @return this auto-pilot object
	 */
	public AutoPilot pathAddToRoute(Vector2D[] route){
		if(route != null && route.length > 0){
			for(Vector2D v : route)
				path.add(new GraphNode(-1, v.x, v.y));
			pathOn();
		}
		return this;
	}

	/**
	 * Add the waypoint to the end of the existing path and switch path following on.
	 * Do nothing if the parameter is null or empty.
	 * @param waypoint add a single waypoint to the route
	 * @return this auto-pilot object
	 */
	public AutoPilot pathAddToRoute(Vector2D waypoint){
		if(waypoint != null){
			path.add(new GraphNode(-1, waypoint.x, waypoint.y));
			pathOn();
		}
		return this;
	}

	/**
	 * Set the path the entity should follow. Ignore if route is null or has
	 * less than 2 locations. <br>
	 * 
	 * @param route the path to follow
	 * @return this auto-pilot object
	 */
	public AutoPilot pathSetRoute(List<GraphNode> route){
		if(route != null && route.size() >= 2){
			path = (LinkedList<GraphNode>) route;
			pathOn();
		}
		return this;
	}



	/**
	 * Add the route to the end of the existing path. Do nothing if the 
	 * parameter is null.
	 * @param route the path to add
	 */
	public AutoPilot pathAddToRoute(List<GraphNode> route){
		if(route != null){
			path.addAll(route);
			pathOn();
		}
		return this;
	}

	/**
	 * Calculate and add the route from the end of the existing path (or the nearest node if 
	 * there is no existing path) to the dest Node. The path calculation will use A* with 
	 * crow's flight heuristic when calculating the route. <br>
	 * The instruction is ignored if the graph or destination node does note exist.
	 * @param graph
	 * @param dest
	 */
	public AutoPilot pathAddToRoute(Graph graph, GraphNode dest){
		GraphNode start = null;
		if(graph != null && dest != null) {
			if(path.isEmpty())
				start = graph.getNodeNearest(owner.pos().x, owner.pos().y, 0);
			else
				start = path.getLast();
			IGraphSearch gs = new GraphSearch_Astar(graph);
			LinkedList<GraphNode> toadd = gs.search(start.id(), dest.id());
			path.addAll(toadd);
			if(!path.isEmpty())
				pathOn();
		}
		return this;
	}

	/**
	 * Get the node the entity is currently moving towards. If there is no
	 * path defined or the entity has reached the end of its last path 
	 * the method returns null.
	 * @return current path target or null if none
	 */
	public GraphNode pathNextNode(){
		if(!path.isEmpty())
			return path.getFirst();
		else
			return null;
	}

	/*
	 * **************************************************************************************
	 * **************************************************************************************
	 * 
	 * The next two methods are related to the force calculation method.
	 * 
	 * **************************************************************************************
	 * **************************************************************************************
	 */

	/**
	 * Set which method is to be used for calculating the steering force, options are <b><pre>
	 * SBF.WEIGHTED_AVERAGE (default) or SBF.PRIORITIZED <br></pre>
	 * any other value will be ignored.
	 * @param method the method to use
	 */
	public AutoPilot calculateMethod(int method){
		if(method == WEIGHTED || method == WEIGHTED_PRIORITIZED)
			forceCalcMethod = method;
		return this;
	}

	/**
	 * Find out which method is being used for calculating the steering force.
	 * 
	 * return the calculation method being used (SBF.WEIGHTED_AVERAGE or SBF.PRIORITIZED).
	 */
	public int calculateMethod(){
		return forceCalcMethod;
	}
	
	/*
	 * **************************************************************************************
	 * **************************************************************************************
	 * 
	 * The following section has all the methods to calculate the steering forces. Although
	 * some of the methods are public they are only suitable for more advanced users.
	 * 
	 * **************************************************************************************
	 * **************************************************************************************
	 */

	/**
	 * Calculates (according to selected calculation method) the steering forces from 
	 * any active behaviours. 
	 * @param deltaTime time since last update in seconds
	 * @param world the game world object
	 * @return the calculated steering force as a Vector2D
	 */
	public Vector2D calculateForce(double deltaTime, World world){
		obstacles = null;
		walls = null;
		movers = null;

		this.deltaTime = deltaTime;
		this.world = world;

		steeringForce.set(0,0);

		if(owner.hasForceRecorder()){
			switch(forceCalcMethod){
			case WEIGHTED:
				steeringForce.set(calculateWeightedAverage_LogForces());
				break;
			case WEIGHTED_PRIORITIZED:
				steeringForce.set(calculatePrioritised_LogForces());
				break;
			}
		}
		else {
			switch(forceCalcMethod){
			case WEIGHTED:
				steeringForce.set(calculateWeightedAverage());
				break;
			case WEIGHTED_PRIORITIZED:
				steeringForce.set(calculatePrioritised());
				break;
			}
		}
		obstacles = null;
		walls = null;
		movers = null;
		return steeringForce;
	}

	/**
	 * Uses the weighted average method to calculate the steering force.
	 * @return the calculated steering force as a Vector2D
	 */
	private Vector2D calculateWeightedAverage(){
		accum.set(0,0);
		f.set(0,0);
		if((flags & WALL_AVOID) != 0){
			f = wallAvoidance();
			f.mult(weightings[BIT_WALL_AVOID]);
			accum.add(f);
		}
		if((flags & OBSTACLE_AVOID) != 0){
			f = obstacleAvoidance();
			f.mult(weightings[BIT_OBSTACLE_AVOID]);
			accum.add(f);
		}
		if((flags & EVADE) != 0){
			f = evade();
			f.mult(weightings[BIT_EVADE]);
			accum.add(f);
		}
		if((flags & FLEE) != 0){
			f = flee();
			f.mult(weightings[BIT_FLEE]);
			accum.add(f);
		}
		if((flags & FLOCK) != 0){
			f = flock();
			f.mult(weightings[BIT_FLOCK]);
			accum.add(f);
		}
		else {
			if((flags & SEPARATION) != 0){
				f = separation();
				f.mult(weightings[BIT_SEPARATION]);
				accum.add(f);
			}
			if((flags & ALIGNMENT) != 0){
				f = alignment();
				f.mult(weightings[BIT_ALIGNMENT]);
				accum.add(f);
			}
			if((flags & COHESION) != 0){
				f = cohesion();
				f.mult(weightings[BIT_COHESION]);
				accum.add(f);
			}
		}
		if((flags & SEEK) != 0){
			f = seek();
			f.mult(weightings[BIT_SEEK]);
			accum.add(f);
		}
		if((flags & ARRIVE) != 0){
			f = arrive();
			f.mult(weightings[BIT_ARRIVE]);
			accum.add(f);
		}
		if((flags & WANDER) != 0){
			f = wander();
			f.mult(weightings[BIT_WANDER]);
			accum.add(f);
		}
		if((flags & PURSUIT) != 0){
			f = pursuit();
			f.mult(weightings[BIT_PURSUIT]);
			accum.add(f);
		}
		if((flags & OFFSET_PURSUIT) != 0){
			f = offsetPursuit();
			f.mult(weightings[BIT_OFFSET_PURSUIT]);
			accum.add(f);
		}
		if((flags & INTERPOSE) != 0){
			f = interpose();
			f.mult(weightings[BIT_INTERPOSE]);
			accum.add(f);
		}
		if((flags & HIDE) != 0){
			f = hide();
			f.mult(weightings[BIT_HIDE]);
			accum.add(f);
		}
		if((flags & PATH) != 0){
			f = pathFollow();
			f.mult(weightings[BIT_PATH]);
			accum.add(f);
		}
		return accum;		
	}
	
	/**
	 * Uses the weighted average method to calculate the steering force 
	 * and logs the results
	 * @return the calculated steering force as a Vector2D
	 */
	private Vector2D calculateWeightedAverage_LogForces(){
		accum.set(0,0);
		f.set(0,0);
		if((flags & WALL_AVOID) != 0){
			f = wallAvoidance();
			f.mult(weightings[BIT_WALL_AVOID]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_WALL_AVOID, f);
			accum.add(f);
		}
		if((flags & OBSTACLE_AVOID) != 0){
			f = obstacleAvoidance();
			f.mult(weightings[BIT_OBSTACLE_AVOID]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_OBSTACLE_AVOID, f);
			accum.add(f);
		}
		if((flags & EVADE) != 0){
			f = evade();
			f.mult(weightings[BIT_EVADE]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_EVADE, f);
			accum.add(f);
		}
		if((flags & FLEE) != 0){
			f = flee();
			f.mult(weightings[BIT_FLEE]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_FLEE, f);
			accum.add(f);
		}
		if((flags & FLOCK) != 0){
			f = flock();
			f.mult(weightings[BIT_FLOCK]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_FLOCK, f);
			accum.add(f);
		}
		else {
			if((flags & SEPARATION) != 0){
				f = separation();
				f.mult(weightings[BIT_SEPARATION]);
				if(owner.hasForceRecorder())
					owner.forceRecorder().addData(SBF.BIT_SEPARATION, f);
				accum.add(f);
			}
			if((flags & ALIGNMENT) != 0){
				f = alignment();
				f.mult(weightings[BIT_ALIGNMENT]);
				if(owner.hasForceRecorder())
					owner.forceRecorder().addData(SBF.BIT_ALIGNMENT, f);
				accum.add(f);
			}
			if((flags & COHESION) != 0){
				f = cohesion();
				f.mult(weightings[BIT_COHESION]);
				if(owner.hasForceRecorder())
					owner.forceRecorder().addData(SBF.BIT_COHESION, f);
				accum.add(f);
			}
		}
		if((flags & SEEK) != 0){
			f = seek();
			f.mult(weightings[BIT_SEEK]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_SEEK, f);
			accum.add(f);
		}
		if((flags & ARRIVE) != 0){
			f = arrive();
			f.mult(weightings[BIT_ARRIVE]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_ARRIVE, f);
			accum.add(f);
		}
		if((flags & WANDER) != 0){
			f = wander();
			f.mult(weightings[BIT_WANDER]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_WANDER, f);
			accum.add(f);
		}
		if((flags & PURSUIT) != 0){
			f = pursuit();
			f.mult(weightings[BIT_PURSUIT]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_PURSUIT, f);
			accum.add(f);
		}
		if((flags & OFFSET_PURSUIT) != 0){
			f = offsetPursuit();
			f.mult(weightings[BIT_OFFSET_PURSUIT]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_OFFSET_PURSUIT, f);
			accum.add(f);
		}
		if((flags & INTERPOSE) != 0){
			f = interpose();
			f.mult(weightings[BIT_INTERPOSE]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_INTERPOSE, f);
			accum.add(f);
		}
		if((flags & HIDE) != 0){
			f = hide();
			f.mult(weightings[BIT_HIDE]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_HIDE, f);
			accum.add(f);
		}
		if((flags & PATH) != 0){
			f = pathFollow();
			f.mult(weightings[BIT_PATH]);
			if(owner.hasForceRecorder())
				owner.forceRecorder().addData(SBF.BIT_PATH, f);
			accum.add(f);
		}
		return accum;		
	}

	/**
	 * Uses the prioritised weighted average method to calculate the steering force.
	 * @return the calculated steering force as a Vector2D
	 */
	private Vector2D calculatePrioritised(){
		double maxForce = owner.maxForce();
		accum.set(0,0);
		f.set(0,0);
		if((flags & WALL_AVOID) != 0){
			f = wallAvoidance();
			f.mult(weightings[BIT_WALL_AVOID]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & OBSTACLE_AVOID) != 0){
			f = obstacleAvoidance();
			f.mult(weightings[BIT_OBSTACLE_AVOID]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & EVADE) != 0){
			f = evade();
			f.mult(weightings[BIT_EVADE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & FLEE) != 0){
			f = flee();
			f.mult(weightings[BIT_FLEE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & FLOCK) != 0){
			f = flock();
			f.mult(weightings[BIT_FLOCK]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		else {
			if((flags & SEPARATION) != 0){
				f = separation();
				f.mult(weightings[BIT_SEPARATION]);
				if(!accumulateForce(accum, f, maxForce)) return accum;
			}
			if((flags & ALIGNMENT) != 0){
				f = alignment();
				f.mult(weightings[BIT_ALIGNMENT]);
				if(!accumulateForce(accum, f, maxForce)) return accum;
			}
			if((flags & COHESION) != 0){
				f = cohesion();
				f.mult(weightings[BIT_COHESION]);
				if(!accumulateForce(accum, f, maxForce)) return accum;
			}
		}
		if((flags & SEEK) != 0){
			f = seek();
			f.mult(weightings[BIT_SEEK]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & ARRIVE) != 0){
			f = arrive();
			f.mult(weightings[BIT_ARRIVE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & WANDER) != 0){
			f = wander();
			f.mult(weightings[BIT_WANDER]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & PURSUIT) != 0){
			f = pursuit();
			f.mult(weightings[BIT_PURSUIT]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & OFFSET_PURSUIT) != 0){
			f = offsetPursuit();
			f.mult(weightings[BIT_OFFSET_PURSUIT]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & INTERPOSE) != 0){
			f = interpose();
			f.mult(weightings[BIT_INTERPOSE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & HIDE) != 0){
			f = hide();
			f.mult(weightings[BIT_HIDE]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & PATH) != 0){
			f = pathFollow();
			f.mult(weightings[BIT_PATH]);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		return accum;			
	}
	/**
	 * Uses the prioritised weighted average method to calculate the steering force.
	 * @return the calculated steering force as a Vector2D
	 */
	private Vector2D calculatePrioritised_LogForces(){
		double maxForce = owner.maxForce();
		accum.set(0,0);
		f.set(0,0);
		if((flags & WALL_AVOID) != 0){
			f = wallAvoidance();
			f.mult(weightings[BIT_WALL_AVOID]);
			owner.forceRecorder().addData(SBF.BIT_WALL_AVOID, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & OBSTACLE_AVOID) != 0){
			f = obstacleAvoidance();
			f.mult(weightings[BIT_OBSTACLE_AVOID]);
			owner.forceRecorder().addData(SBF.BIT_OBSTACLE_AVOID, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & EVADE) != 0){
			f = evade();
			f.mult(weightings[BIT_EVADE]);
			owner.forceRecorder().addData(SBF.BIT_EVADE, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & FLEE) != 0){
			f = flee();
			f.mult(weightings[BIT_FLEE]);
			owner.forceRecorder().addData(SBF.BIT_FLEE, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & FLOCK) != 0){
			f = flock();
			f.mult(weightings[BIT_FLOCK]);
			owner.forceRecorder().addData(SBF.BIT_FLOCK, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		else {
			if((flags & SEPARATION) != 0){
				f = separation();
				f.mult(weightings[BIT_SEPARATION]);
				owner.forceRecorder().addData(SBF.BIT_SEPARATION, f);
				if(!accumulateForce(accum, f, maxForce)) return accum;
			}
			if((flags & ALIGNMENT) != 0){
				f = alignment();
				f.mult(weightings[BIT_ALIGNMENT]);
				owner.forceRecorder().addData(SBF.BIT_ALIGNMENT, f);
				if(!accumulateForce(accum, f, maxForce)) return accum;
			}
			if((flags & COHESION) != 0){
				f = cohesion();
				f.mult(weightings[BIT_COHESION]);
				owner.forceRecorder().addData(SBF.BIT_COHESION, f);
				if(!accumulateForce(accum, f, maxForce)) return accum;
			}
		}
		if((flags & SEEK) != 0){
			f = seek();
			f.mult(weightings[BIT_SEEK]);
			owner.forceRecorder().addData(SBF.BIT_SEEK, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & ARRIVE) != 0){
			f = arrive();
			f.mult(weightings[BIT_ARRIVE]);
			owner.forceRecorder().addData(SBF.BIT_ARRIVE, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & WANDER) != 0){
			f = wander();
			f.mult(weightings[BIT_WANDER]);
			owner.forceRecorder().addData(SBF.BIT_WANDER, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & PURSUIT) != 0){
			f = pursuit();
			f.mult(weightings[BIT_PURSUIT]);
			owner.forceRecorder().addData(SBF.BIT_PURSUIT, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & OFFSET_PURSUIT) != 0){
			f = offsetPursuit();
			f.mult(weightings[BIT_OFFSET_PURSUIT]);
			owner.forceRecorder().addData(SBF.BIT_OFFSET_PURSUIT, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & INTERPOSE) != 0){
			f = interpose();
			f.mult(weightings[BIT_INTERPOSE]);
			owner.forceRecorder().addData(SBF.BIT_WALL_AVOID, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & HIDE) != 0){
			f = hide();
			f.mult(weightings[BIT_HIDE]);
			owner.forceRecorder().addData(SBF.BIT_HIDE, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		if((flags & PATH) != 0){
			f = pathFollow();
			f.mult(weightings[BIT_PATH]);
			owner.forceRecorder().addData(SBF.BIT_PATH, f);
			if(!accumulateForce(accum, f, maxForce)) return accum;
		}
		return accum;			
	}

	/**
	 * This method is used by the PRIORITISED force calculation method. <br>
	 * For each active behaviour in turn it calculates how much of the
	 * maximum steering force is left then adds that amount of to the accumulator.
	 * 
	 * @param runningTotal running total
	 * @param forceToAdd the force we want to add from the current behaviour
	 * @param maxForce the maximum force available.
	 * @return true if we have not reached the maximum permitted force.
	 */
	private boolean accumulateForce(Vector2D runningTotal, Vector2D forceToAdd, double maxForce){ 
		//calculate how much steering force the vehicle has used so far
		double magnitudeSoFar = runningTotal.length();
		//calculate how much steering force remains to be used by this vehicle
		double magnitudeLeft = maxForce - magnitudeSoFar;
		//calculate the magnitude of the force we want to add
		double magnitudeToAdd = forceToAdd.length();

		//if the magnitude of the sum of ForceToAdd and the running total
		//does not exceed the maximum force available to this vehicle, just
		//add together. Otherwise add as much of the ForceToAdd vector is
		//possible without going over the max.
		if (magnitudeToAdd < magnitudeLeft){
			runningTotal.add(forceToAdd);
			return true;
		}
		else {
			forceToAdd.normalize();
			forceToAdd.mult(magnitudeLeft);
			//add it to the steering force
			runningTotal.add(forceToAdd);
			return false;
		}
	}

	/*
	 * INTERNAL FUNCTIONS USED TO CALCULATE THE STEERING FORCES FOR EACH behaviour
	 */

	/**
	 * HIDE
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D hide(){
		return hide(owner, agents[AGENT_TO_EVADE]);
	}

	protected Vector2D hide(MovingEntity me, MovingEntity from){
		double distToNearest = Double.MAX_VALUE;
		Vector2D bestHidingSpot = null;
		//		Obstacle closest = null;

		// This maybe required by other behaviours
		if(obstacles == null)
			obstacles = world.getObstacles(me);
		if(obstacles == null || obstacles.size() < 2)
			return Vector2D.ZERO;

		for(Obstacle ob : obstacles){
			//calculate the position of the hiding spot for this obstacle
			Vector2D hidingSpot = getHidingPosition(me, ob, from);

			//work in distance-squared space to find the closest hiding
			//spot to the agent
			double dist = Vector2D.distSq(hidingSpot, me.pos());

			if (dist < distToNearest){
				distToNearest = dist;
				bestHidingSpot = hidingSpot;
			}  
		}
		//if no suitable obstacles found then Evade the hunter
		if (bestHidingSpot == null)
			return evade(me, from);
		else // Go to hiding place
			return arrive(me, bestHidingSpot, FAST);
	}

	/**
	 * Given the position of a hunter, and the position and radius of
	 * an obstacle, this method calculates a position DistanceFromBoundary
	 * away from its bounding radius and directly opposite the hunter.
	 */
	private Vector2D getHidingPosition(MovingEntity me, Obstacle ob, BaseEntity target){
		// calculate how far away the agent is to be from the chosen obstacle's
		// bounding radius

		double distAway = ob.colRadius() + me.colRadius();

		//calculate the heading toward the object from the hunter
		Vector2D hidePos = Vector2D.sub(ob.pos(), target.pos());
		hidePos.normalize();
		hidePos.mult(distAway);
		hidePos.add(ob.pos());

		//scale it to size and add to the obstacles position to get
		//the hiding spot.
		return hidePos;
	}

	/**
	 * ALIGNMENT
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D alignment(){
		return alignment(owner);
	}

	protected Vector2D alignment(MovingEntity me){
		Vector2D avgHeading = new Vector2D();
		if(movers == null)
			movers = world.getMovers(me, neighbourDist);
		if(movers == null || movers.size() < 2)
			return avgHeading;

		int neighbourCount = 0;
		double ndistSq = neighbourDist * neighbourDist;
		for(MovingEntity mover : movers){
			double distSq = Vector2D.distSq(me.pos(), mover.pos());
			if(mover != me && mover != agents[AGENT_TO_EVADE] && distSq < ndistSq){
				avgHeading.add(mover.heading());
				neighbourCount++;
			}
		}
		if(neighbourCount > 0){
			avgHeading.div(neighbourCount);
			avgHeading.sub(me.heading());
			avgHeading.normalize();
		}
		return avgHeading;
	}


	/**
	 * SEPARATION
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D separation(){
		return separation(owner);
	}

	protected Vector2D separation(MovingEntity me){  
		Vector2D sForce = new Vector2D();

		if(movers == null)
			movers = world.getMovers(me, neighbourDist);
		if(movers == null || movers.size() < 2)
			return sForce;

		int neighbourCount = 0;
		double ndistSq = neighbourDist * neighbourDist;
		for(MovingEntity mover : movers){
			double distSq = Vector2D.distSq(me.pos(), mover.pos());
			if(mover != me && mover != agents[AGENT_TO_EVADE] && distSq < ndistSq){
				Vector2D toAgent = Vector2D.sub(me.pos(), mover.pos());
				toAgent.div(distSq);
				sForce.add(toAgent);
				neighbourCount++;
			}
		}
		if(neighbourCount > 0){
			sForce.normalize();
		}
		return sForce;
	}

	/**
	 * COHESION
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D cohesion(){
		return cohesion(owner);
	}

	protected Vector2D cohesion(MovingEntity me){
		//first find the centre of mass of all the agents
		Vector2D centreOfMass = new Vector2D();
		Vector2D sForce = new Vector2D();

		if(movers == null)
			movers = world.getMovers(me, neighbourDist);
		if(movers == null || movers.size() < 2)
			return sForce;

		//used to count the number of vehicles in the neighbourhood
		int neighbourCount = 0;

		double ndistSq = neighbourDist * neighbourDist;
		for(MovingEntity mover : movers){
			double distSq = Vector2D.distSq(me.pos(), mover.pos());
			if(mover != me && mover != agents[AGENT_TO_EVADE] && distSq < ndistSq){
				centreOfMass.add(mover.pos());
				neighbourCount++;
			}
		}
		if(neighbourCount > 0){
			centreOfMass.div(neighbourCount);
			sForce = seek(me, centreOfMass);
			//the magnitude of cohesion is usually much larger than separation or
			//alignment so it usually helps to normalize it.
			sForce.normalize();
		}
		return sForce;
	}

	/**
	 * FLOCK
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D flock(){
		return flock(owner);
	}

	protected Vector2D flock(MovingEntity me){
		Vector2D sepForce = new Vector2D();
		Vector2D cohForce = new Vector2D();
		Vector2D alnForce = new Vector2D();
		Vector2D flockForce = new Vector2D();

		if(movers == null)
			movers = world.getMovers(me, neighbourDist);

		// Intermediate values for calculation
		Vector2D avgHeading = new Vector2D();
		Vector2D toAgent = new Vector2D();

		int neighbourCount = 0;
		double distSq;

		double ndistSq = neighbourDist * neighbourDist;

		for(MovingEntity mover : movers){
			distSq = Vector2D.distSq(me.pos(), mover.pos());
			if(mover != me && mover != agents[AGENT_TO_EVADE] && distSq < ndistSq){
				neighbourCount++;
				// Cohesion
				cohForce.add(mover.pos());
				// Alignment
				avgHeading.add(mover.heading());
				// Separation
				toAgent.set(me.pos());
				toAgent.sub(mover.pos());
				toAgent.div(distSq);
				sepForce.add(toAgent);
			}			
		}
		if(neighbourCount > 0){
			// Cohesion
			cohForce.div(neighbourCount);
			cohForce.sub(me.pos());
			cohForce.normalize();
			cohForce.mult(me.maxSpeed());
			cohForce.sub(me.velocity());
			cohForce.normalize();
			cohForce.mult(weightings[BIT_COHESION]);

			// Separation
			sepForce.normalize();
			sepForce.mult(weightings[BIT_SEPARATION]);

			// Alignment
			avgHeading.div(neighbourCount);
			avgHeading.sub(me.heading());
			alnForce.set(avgHeading);
			alnForce.mult(weightings[BIT_ALIGNMENT]);

			// Add them up
			flockForce.set(cohForce);
			flockForce.add(sepForce);
			flockForce.add(alnForce);
		}
		return flockForce;
	}

	/**
	 * INTERPOSE
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D interpose(){
		return interpose(owner, agents[AGENT0], agents[AGENT1]);
	}

	protected Vector2D interpose(MovingEntity me, MovingEntity m0, MovingEntity m1){
		if(m0 == null || m1 == null){
			System.out.println("Interpose: need to set agents");
			return Vector2D.ZERO;
		}
		//first we need to figure out where the two agents are going to be at 
		//time T in the future. This is approximated by determining the time
		//taken to reach the mid way point at the current time at at max speed.
		Vector2D currMidPoint = Vector2D.add(m0.pos(), m1.pos());
		currMidPoint.div(2.0);
		double timeToReachMidPoint = Vector2D.dist(me.pos(), currMidPoint) / me.maxSpeed();

		//now we have T, we assume that agent A and agent B will continue on a
		//straight trajectory and extrapolate to get their future positions
		Vector2D agent0Pos = Vector2D.mult(m0.velocity(), timeToReachMidPoint);
		agent0Pos.add(m0.pos());
		Vector2D agent1Pos = Vector2D.mult(m1.velocity(), timeToReachMidPoint);
		agent1Pos.add(m1.pos());

		//calculate the mid point of these predicted positions
		Vector2D target = Vector2D.add(agent0Pos, agent1Pos);
		target.div(2.0);
		return arrive(me, target, FAST);
	}

	protected Vector2D interpose(MovingEntity me, MovingEntity m0, Vector2D fixedPos){
		if(m0 == null || fixedPos == null){
			System.out.println("Interpose: need an agent and a fixed position");
			return Vector2D.ZERO;
		}
		//first we need to figure out where the agent is going to be at 
		//time T in the future. This is approximated by determining the time
		//taken to reach the mid way point at the current time at at max speed.
		Vector2D currMidPoint = Vector2D.add(m0.pos(), fixedPos);
		currMidPoint.div(2.0);
		double timeToReachMidPoint = Vector2D.dist(me.pos(), currMidPoint) / me.maxSpeed();

		//now we have T, we assume that agent A and agent B will continue on a
		//straight trajectory and extrapolate to get their future positions
		Vector2D agent0Pos = Vector2D.mult(m0.velocity(), timeToReachMidPoint);
		agent0Pos.add(m0.pos());
		
		//calculate the mid point of these predicted positions
		Vector2D target = Vector2D.add(agent0Pos, fixedPos);
		target.div(2.0);
		return arrive(me, target, FAST);
	}


	/**
	 * WALL AVOIDANCE
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D wallAvoidance(){
		return wallAvoidance(owner);
	}

	protected Vector2D wallAvoidance(MovingEntity me){
		Vector2D sForce = Vector2D.ZERO;

		// This maybe required by other behaviours
		if(walls == null)
			walls = world.getWalls(me);
		if(walls == null || walls.size() < 2)
			return sForce;

		Vector2D[] feelers = getFeelers(me);

		// Distance to currently calculated interception point
		double distToThisIP    = 0.0;
		// Records distance to nearest IP found so far
		double distToClosestIP = Double.MAX_VALUE;

		Wall closestWall = null;		// The current wall with the nearest intersection point
		Vector2D closestPoint = null;  	// The current closest intersection point
		int feeler = 0;					// found with this feeler


		// For each feeler in turn
		for (int flr=0; flr < feelers.length; ++flr)
		{
			// test against each wall for any intersection points
			for(Wall wall : walls){
				Vector2D intercept = Geometry2D.line_line_p(me.pos(),
						feelers[flr],
						wall.getStartPos(),
						wall.getEndPos());
				if(intercept != null){
					distToThisIP = Vector2D.dist(intercept, me.pos());
					// If this is the closest found so far remember it
					if (distToThisIP < distToClosestIP){
						distToClosestIP = distToThisIP;
						closestWall = wall;
						closestPoint = intercept;
						feeler = flr;
					}
				}
			} // next wall
		} // next feeler

		// If an intersection point has been detected, calculate a force  
		// that will direct the agent away
		if (closestWall != null){
			//calculate by what distance the projected position of the agent
			//will overshoot the wall
			Vector2D overShoot = Vector2D.sub(feelers[feeler], closestPoint);

			//create a force in the direction of the wall normal, with a 
			//magnitude of the overshoot
			sForce = Vector2D.mult(closestWall.norm(), overShoot.length());
		}
		return sForce;
	}

	/**
	 * FOR INTERNAL USE ONLY <br>
	 * Calculates and returns an array of feelers around the vehicle that
	 * owns this steering behaviour.
	 * 
	 * @return array of whisker vectors
	 */
	public Vector2D[] getFeelers(){
		return createWhiskers( nbrWhiskers, // Number of Whiskers
				whiskerLength, // Whisker length
				whiskerFOV, // fov
				owner.heading(), // facing
				owner.pos() // origin
		);
	}

	/**
	 * Calculates and returns an array of feelers arround the specified
	 * moving entity using the whisker details in this steering behaviour.
	 * 
	 * @param me the moving entity needing whiskers
	 * @return array of whisker vectors
	 */
	public Vector2D[] getFeelers(MovingEntity me){
		return createWhiskers( nbrWhiskers, // Number of Whiskers
				whiskerLength, // Whisker length
				whiskerFOV, // fov
				me.heading(), // facing
				me.pos() // origin
		);
	}

	/**
	 * Calculates and returns an array of feelers around the vehicle that
	 * owns this steering behaviour using the specified heading and origin. <br>
	 * This method is called by the Hints class when drawing the whiskers.
	 * 
	 * @param facing the facing direction
	 * @param origin
	 * @return array of whisker vectors
	 */
	public Vector2D[] getFeelers(final Vector2D  facing, final Vector2D  origin ){
		return createWhiskers( nbrWhiskers, // Number of Whiskers
				whiskerLength, // Whisker length
				whiskerFOV, // fov
				facing, // facing
				origin // origin
		);
	}

	/**
	 * Used internally by the getFeelers() methods. <br>
	 * Given an origin, a facing direction, a 'field of view' describing the 
	 * limit of the outer whiskers, a whisker length and the number of whiskers
	 * this method returns a vector containing the end positions of a series
	 * of whiskers radiating away from the origin and with equal distance between
	 * them. (like the spokes of a wheel clipped to a specific segment size)
	 * 
	 * @param nbrWhiskers number of whiskers (>=1)
	 * @param whiskerLength (the length of the whiskers
	 * @param fov the 'field of view'
	 * @param facing the vehicle's heading
	 * @param origin the vehicle's position
	 * @return an array of whiskers for wall avoidance
	 */
	private Vector2D[] createWhiskers(
			final int  		nbrWhiskers,
			final double    whiskerLength,
			final double    fov,
			final Vector2D  facing,
			final Vector2D  origin ){
		//this is the magnitude of the angle separating each whisker
		double angleBetweenWhiskers = fov/(double)(nbrWhiskers-1);

		Vector2D[] whiskers = new Vector2D[nbrWhiskers];
		Vector2D temp;
		double angle = -fov*0.5; 
		for (int w = 0; w < nbrWhiskers; ++w) {			
			temp = Transformations.vec2DRotateAroundOrigin(facing, angle);
			if(ovalEnvelope)
				temp.mult(whiskerLength * (0.75 + 0.25 * FastMath.abs(FastMath.cos(angle))) );
			else
				temp.mult(whiskerLength);
			temp.add(origin);
			whiskers[w] = temp;
			angle += angleBetweenWhiskers;
		}
		return whiskers;
	}

	/**
	 * OBSTACLE AVOIDANCE
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D obstacleAvoidance(){
		return obstacleAvoidance(owner);
	}

	protected Vector2D obstacleAvoidance(MovingEntity me){
		//Vector2D desiredVelocity = new Vector2D();
		Vector2D desiredVelocity = Vector2D.ZERO;

		// This maybe required by other behaviours
		if(obstacles == null)
			obstacles = world.getObstacles(me);
		if(obstacles == null || obstacles.isEmpty())
			return desiredVelocity;

		Obstacle closestIO = null;
		double distToClosestIP = Double.MAX_VALUE;
		Vector2D localPosOfClosestIO = null;
		double dboxLength = detectBoxLength * (1.0 + me.speed() / me.maxSpeed());

		Vector2D velocity = Vector2D.normalize(me.velocity());
		Vector2D vside = velocity.getPerp();

		for(Obstacle ob : obstacles){
			Vector2D localPos = Transformations.pointToLocalSpace(ob.pos(), velocity, vside, me.pos());
			double expandedRadius = ob.colRadius() + me.colRadius();
			if(localPos.x >= 0 && localPos.x < dboxLength + expandedRadius){
				if(FastMath.abs(localPos.y) < expandedRadius){
					double cX = localPos.x;
					double cY = localPos.y;
					double sqrtPart = FastMath.sqrt(expandedRadius * expandedRadius - cY*cY);

					double ip = cX - sqrtPart;
					if(ip <= 0)
						ip = cX + sqrtPart;

					if(ip < distToClosestIP){
						distToClosestIP = ip;
						closestIO = ob;
						localPosOfClosestIO = localPos;
					}
				}
			}
		} // end of for loop
		Vector2D sForce = new Vector2D();
		if(closestIO != null){
			double multiplier = 1.0 + (dboxLength - localPosOfClosestIO.x) / dboxLength;
			sForce.y = (closestIO.colRadius() - localPosOfClosestIO.y) * multiplier;
			double breakingWeight = 0.01;
			sForce.x = (closestIO.colRadius() - localPosOfClosestIO.x) * breakingWeight;
			desiredVelocity = Transformations.vectorToWorldSpace(sForce, velocity, vside);
		}
		return desiredVelocity;
	}


	/**
	 * SEEK
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D seek(){
		return seek(owner, gotoTarget);
	}

	protected Vector2D seek(MovingEntity me, Vector2D target){
		Vector2D desiredVelocity = Vector2D.sub(target, me.pos());
		desiredVelocity.normalize();
		desiredVelocity.mult(me.maxSpeed());
		desiredVelocity.sub(me.velocity());
		return desiredVelocity;
	}


	/**
	 * FLEE
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D flee(){
		return flee(owner, fleeTarget);
	}

	protected Vector2D flee(MovingEntity me, Vector2D target){
		//only flee if the target is within 'panic distance'. Work in distance
		//squared space.
		Vector2D desiredVelocity = Vector2D.ZERO;
		if(Vector2D.distSq(target, me.pos()) < fleeRadius * fleeRadius){
			desiredVelocity = Vector2D.sub(me.pos(), target);
			desiredVelocity.normalize();
			desiredVelocity.mult(me.maxSpeed());
			desiredVelocity.sub(me.velocity());
		}
		return desiredVelocity;
	}

	/**
	 * WANDER
	 * Used internally to calculate the wander force
	 */
	private Vector2D wander(){ 
		// this behaviour is dependent on the update rate, so this line must
		// be included when using time independent frame rate.
		wanderAngleDelta = wanderAngleJitter * deltaTime;
		wanderAngle += wanderAngleDelta * MathUtils.randomClamped();
		// Not really essential considering the range of the type double.
		if(wanderAngle < WANDER_MIN_ANGLE)
			wanderAngle += WANDER_ANGLE_RANGE;
		else if(wanderAngle > WANDER_MAX_ANGLE)
			wanderAngle -= WANDER_ANGLE_RANGE;

		// Calculate position on wander circle
		Vector2D targetLocal = new Vector2D(wanderRadius * FastMath.cos(wanderAngle), wanderRadius * FastMath.sin(wanderAngle));
		// Add wander distance
		targetLocal.add(wanderDist, 0);

		// project the target into world space
		Vector2D targetWorld = Transformations.pointToWorldSpace(targetLocal,
				owner.heading(),
				owner.side(), 
				owner.pos());

		// and steer towards it
		targetWorld.sub(owner.pos());
		return targetWorld; 
	}

	/**
	 * ARRIVE
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D arrive(){
		return arrive(owner, gotoTarget, arriveRate);
	}

	//--------------------------- Arrive -------------------------------------
	//
	// This behaviour is similar to seek but it attempts to arrive at the
	// target with a zero velocity
	//------------------------------------------------------------------------
	protected Vector2D arrive(MovingEntity me, Vector2D target, int rate){
		Vector2D desiredVelocity = Vector2D.ZERO;
		double dist = Vector2D.dist(target, me.pos());
		if(dist > arriveDist){
			double speed = dist / DECEL_TWEEK[rate];
			speed = FastMath.min(speed, me.maxSpeed());
			desiredVelocity = Vector2D.sub(target, me.pos());
			desiredVelocity.mult(speed / dist);
			desiredVelocity.sub( me.velocity());
		}
		else {
			me.moveTo(target.x, target.y);
			me.velocity(0, 0);
		}
		return desiredVelocity;		
	}

	/**
	 * OFFSET PURSUIT
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D offsetPursuit(){
		return offsetPursuit(owner, agents[AGENT_TO_PURSUE], pursueOffset);
	}

	//------------------------- Offset Pursuit -------------------------------
	//
	// Produces a steering force that keeps a vehicle at a specified offset
	// from a leader vehicle
	//------------------------------------------------------------------------
	protected Vector2D offsetPursuit(MovingEntity me, MovingEntity leader, Vector2D offset){
		//calculate the offset's position in world space
		Vector2D worldOffsetPos = Transformations.pointToWorldSpace( offset,
				leader.heading(),
				leader.side(),
				leader.pos());

		Vector2D toOffset = Vector2D.sub(worldOffsetPos, me.pos());

		//the lookahead time is proportional to the distance between the leader
		//and the pursuer; and is inversely proportional to the sum of both
		//agent's velocities
		double lookAheadTime = toOffset.length() / (me.maxSpeed() + leader.speed());

		//now Arrive at the predicted future position of the offset
		Vector2D target = new Vector2D(leader.velocity());
		target.mult(lookAheadTime);
		target.add(worldOffsetPos);
		
		return arrive(me, target, FAST);
	}

	/**
	 * PURSUIT
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D pursuit(){
		return pursuit(owner, agents[AGENT_TO_PURSUE]);
	}

	/** 
	 * PURSUIT
	 * Seek to the agent's predicted position based on velocities and estimated time to intercept
	 */
	protected Vector2D pursuit(MovingEntity me, MovingEntity toPursue){
		Vector2D toAgent = Vector2D.sub(toPursue.pos(), me.pos());
		double relativeHeading = me.heading().dot(toPursue.heading());

		if ( (toAgent.dot(me.heading()) > 0) &&  (relativeHeading < -0.95)){  // acos(0.95)=18 degs
			return seek(me, toPursue.pos());
		}
		// the lookahead time is proportional to the distance between the agent
		// and the pursuer; and is inversely proportional to the sum of the
		// agent's velocities
		double lookAheadTime = toAgent.length() / (me.maxSpeed() + toPursue.velocity().length());
		Vector2D target = new Vector2D(toPursue.pos());
		target.add(Vector2D.mult(toPursue.velocity(), lookAheadTime));
		return seek(me, target);
	}


	/**
	 * EVADE
	 * Used internally to calculate the force for this behaviour on the entity
	 * that owns this steering behaviour object.
	 */
	private Vector2D evade(){
		return evade(owner, agents[AGENT_TO_EVADE]);
	}

	protected Vector2D evade(MovingEntity me, MovingEntity evadeAgent){
		Vector2D target = new Vector2D(evadeAgent.pos());
		Vector2D fromAgent = Vector2D.sub(target, me.pos());

		//the lookahead time is proportional to the distance between the pursuer
		//and the pursuer; and is inversely proportional to the sum of the
		//agents' velocities
		double lookAheadTime = fromAgent.length() / (me.maxSpeed() + evadeAgent.velocity().length());

		target.add(Vector2D.mult(evadeAgent.velocity(), lookAheadTime));

		return flee(me, target);	
	}

	/**
	 * PATH
	 * Given a series of Vector2Ds, this method produces a force that will
	 * move the agent along the waypoints in order. The agent uses the
	 *'Seek' behaviour to move to the next waypoint - unless it is the last
	 * waypoint, in which case it 'Arrives'
	 * ------------------------------------------------------------------------
	 */
	private Vector2D pathFollow(){ 
		if(!path.isEmpty()){
			Vector2D target = new Vector2D(path.getFirst().x(), path.getFirst().y());
			double dist = (path.size() > 1) ? pathSeekDist : pathArriveDist;
			dist *= dist; // Square the distance
			// If we are close enough to target then remove it from the list
			// for next time.
			if(Vector2D.distSq(target, owner.pos()) < dist)
				path.removeFirst();
			return (path.size() == 1) ? arrive(owner, target, FAST) : seek(owner, target);
		}
		else {
			pathOff();
		}
		return Vector2D.ZERO;
	}

	/**
	 * Creates and returns an 'available' (auto-pilot object that can be 
	 * used in another Vehicle) copy of this object. 
	 */
	public Object clone(){
		AutoPilot copy = null;
		try {
			copy = (AutoPilot)super.clone();
			copy.weightings = new double[this.weightings.length];
			System.arraycopy(weightings, 0, copy.weightings, 0, copy.weightings.length);
			copy.gotoTarget = new Vector2D(gotoTarget);
			copy.fleeTarget = new Vector2D(fleeTarget);
			copy.agents = new MovingEntity[agents.length];
			System.arraycopy(agents, 0, copy.agents, 0, copy.agents.length);
			copy.pursueOffset = new Vector2D(pursueOffset);
			for(GraphNode n : path)
				copy.path.add(new GraphNode(n));
		} 
		catch (CloneNotSupportedException e) {
			System.out.println("Failed to clone this object.");
		}
		// Remove the owner from the copy
		copy.owner = null;
		return copy;
	}

	/**
	 * This method provides a String object that describes the active behaviours and their details.
	 * This is useful when debugging.
	 */
	public String toString(){
		StringBuilder s = new StringBuilder("============================ AUTO-PILOT DETAILS ======================================================\n  Active behaviours >>>  ");
		int f = flags, idx = 0;
		while(f > 0){
			if( (f & 1) != 0)
				s.append(bnames[idx]);
			idx++;			
			f >>= 1;
		}
		f = flags;
		if((f & WALL_AVOID) != 0) 
			s.append(Message.build("\n\tWall avoidance: Weighting {0}  Whiskers: number {1}  FOV {2}  length {3}", getWeight(WALL_AVOID), nbrWhiskers, whiskerFOV, whiskerLength));		
		if((f & OBSTACLE_AVOID) != 0) s.append(Message.build("\n\tObstacle avoidance: Weighting {0}", getWeight(OBSTACLE_AVOID)));
		if((f & EVADE) != 0) s.append(Message.build("\n\tEvade: Weighting {0}  form {1}", getWeight(EVADE), agents[AGENT_TO_EVADE]));
		if((f & FLEE) != 0) s.append(Message.build("\n\tFlee: Weighting {0}  from {1}", getWeight(FLEE), agents[AGENT_TO_EVADE]));
		if((f & SEPARATION) != 0) s.append(Message.build("\n\tSeparation: Weighting {0}", getWeight(SEPARATION)));
		if((f & ALIGNMENT) != 0) s.append(Message.build("\n\tAlignment: Weighting {0}", getWeight(ALIGNMENT)));
		if((f & COHESION) != 0) s.append(Message.build("\n\tCohesion: Weighting {0}", getWeight(COHESION)));
		if((f & SEEK) != 0) s.append(Message.build("\n\tSeek: Weighting {0}  for {1}", getWeight(SEEK), gotoTarget));
		if((f & ARRIVE) != 0) s.append(Message.build("\n\tArrive: Weighting {0}  target is {1}", getWeight(ARRIVE), gotoTarget));
		if((f & WANDER) != 0) s.append(Message.build("\n\tWander: Weighting {0}  jitter {1}  radius {2}  distance {3}", getWeight(WANDER), wanderAngleJitter, wanderRadius, wanderDist));		
		if((f & PURSUIT) != 0) s.append(Message.build("\n\tPursue: Weighting {0}  pursue {1}", getWeight(PURSUIT), agents[AGENT_TO_PURSUE]));
		if((f & OFFSET_PURSUIT) != 0) s.append(Message.build("\n\tOffset Pursuue: Weighting {0}  follow {1}  offset {2}", getWeight(OFFSET_PURSUIT), agents[AGENT_TO_PURSUE], pursueOffset));
		if((f & INTERPOSE) != 0) s.append(Message.build("\n\tInterpose: Weighting {0}  between {1} and {2}", getWeight(FLEE), agents[AGENT0], agents[AGENT1]));
		if((f & HIDE) != 0) s.append(Message.build("\n\tHide: Weighting {0}", getWeight(HIDE)));
		if((f & PATH) != 0) s.append(Message.build("\n\tPath: Weighting {0}", getWeight(PATH)));
		s.append("\n=====================================================================================================\n");
		return new String(s);
	}

	/**
	 * Get the weighting for a <b>single</b> behaviour. <br>
	 * @param behaviour should represent a <b>single</b> behaviour
	 * @return weighting of the specified behaviour
	 */
	public double getWeight(int behaviour){
		return weightings[bitPos(behaviour)];
	}

	

	// Some static helper methods and variables
	private static String[] bnames = new String[] {
		" wall-avoid",	
		" obstacle-avoid",
		" evade ",
		" flee ",
		" separation ", 
		" alignment ",
		" cohesion ",
		" seek ",
		" arrive ",
		" wander ",
		" pursuit ", 
		" offset-pursuit",
		" interpose ",
		" hide ",
		" path ",
		" flock "
	};

	/**
	 * For a given a bit mask find the position of the least significant set bit.
	 * @param flag
	 * @return position of the least significant bit.
	 */
	private static int bitPos(int flag){
		int counter;
		for(counter = 0; flag > 1; flag >>= 1, counter++);
		return counter;		
	}

	/**
	 * Default values for steering behaviour objects.
	 */
	private double[] weightings = new double[] {
			220.0,		// wall avoidance weight
			80.0,		// obstacle avoidance weight
			1.0,		// evade weight
			20.0,		// flee weight
			1.0, 		// separation weight
			4.0,		// alignment weight
			15.0, 		// cohesion weight
			20.0, 		// seek weight
			20.0, 		// arrive weight
			5.0, 		// wander weight
			100.0, 		// pursuit weight
			10.0, 		// offset pursuit weight
			10.0, 		// interpose weight
			10.0, 		// hide weight
			20.0, 		// follow path weight
			1.0 		// flock weight
	};
	
} // End of class
