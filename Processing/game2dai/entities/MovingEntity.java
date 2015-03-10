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
import game2dai.World;
import game2dai.maths.FastMath;
import game2dai.maths.Geometry2D;
import game2dai.maths.Matrix2D;
import game2dai.maths.Vector2D;
import game2dai.steering.SBF;

import java.util.Set;


/**
 * This class that models the behaviour of a moving entity that is not acting under
 * the influence of a steering behaviour.
 * 
 * 
 * @author Peter Lager
 *
 */
public class MovingEntity extends BaseEntity {

	// The domain the entity is constrained to.
	protected Domain   		wd = null;
	protected int 			domainConstraint = SBF.WRAP;

	// World position after last update
	protected Vector2D		prevPos = new Vector2D();

	protected Vector2D		velocity = new Vector2D();

	// a normalised vector pointing in the direction the entity is heading. 
	protected Vector2D		heading = new Vector2D();
	// a normalised vector pointing in the entity's rest heading. 
	private Vector2D		headingAtRest = null; // new Vector2D();

	//a normalised vector perpendicular to the heading vector
	protected Vector2D		side = new Vector2D();; 

	// The mass of the entity
	protected double		mass;

	// The maximum speed this entity may travel at.
	protected double		maxSpeed;

	// The maximum force this entity can use to power itself 
	protected double 		maxForce;

	// The maximum rate (radians per second) this vehicle can rotate         
	protected double		maxTurnRate;
	// The current rate of turn (radians per second)         
	protected double		currTurnRate;
	// The previous rate of turn i.e. on last update (radians per second)         
	protected double		prevTurnRate;

	// The distance that the entity can see another moving entity
	protected double 		viewDistance = 50;;
	// Field of view (radians)
	protected double 		viewFOV = 1.047; // Default is 60 degrees

	/**
	 * This is the constructor that should be used when creating an 
	 * unnamed MovingEntity
	 * @param position initial world position
	 * @param radius bounding radius
	 * @param velocity initial velocity
	 * @param max_speed maximum speed
	 * @param heading initial heading
	 * @param mass initial mass
	 * @param max_turn_rate how fast the entity can turn (radians / second)
	 * @param max_force the maximum force that can be applied by a steering behaviour
	 */
	public  MovingEntity( 
			Vector2D position,
			double   radius,
			Vector2D velocity,
			double   max_speed,
			Vector2D heading,
			double   mass,
			double   max_turn_rate,
			double   max_force)
	{
		this("", position, radius, velocity, max_speed, heading, mass, max_turn_rate, max_force);
	}

	/**
	 * This is the constructor that should be used when creating a
	 * named MovingEntity
	 * @param name the name of the entity
	 * @param position initial world position
	 * @param radius bounding radius
	 * @param velocity initial velocity
	 * @param max_speed maximum speed
	 * @param heading initial heading
	 * @param mass initial mass
	 * @param max_turn_rate how fast the entity can turn (radians / second)
	 * @param max_force the maximum force that can be applied by a steering behaviour
	 */
	public  MovingEntity( 
			String name,
			Vector2D position,
			double   radius,
			Vector2D velocity,
			double   max_speed,
			Vector2D heading,
			double   mass,
			double   max_turn_rate,
			double   max_force)
	{
		super(name, position, radius);
		this.heading.set(heading);
		this.velocity.set(velocity);
		this.prevPos.set(position);
		this.mass = mass;
		this.side = heading.getPerp();
		this.maxSpeed = max_speed;
		this.maxTurnRate = max_turn_rate;
		this.currTurnRate = max_turn_rate;
		this.prevTurnRate = max_turn_rate;
		this.maxForce = max_force;
		visible = true;
	}

	/**
	 * Gets the position of the entity prior to the last update.
	 * @return previous position before last update
	 */
	public Vector2D prevPos(){
		return prevPos;
	}

	/**
	 * Gets the current velocity
	 * @return the current velocity of this entity
	 */
	public Vector2D velocity(){
		return velocity;
	}

	/**
	 * Sets the current velocity for this entity
	 * @param newVel the new velocity vector
	 */
	public MovingEntity velocity(Vector2D newVel){
		velocity.set(newVel);
		return this;
	}

	/**
	 * Sets the current velocity
	 * @param vx 
	 * @param vy
	 */
	public MovingEntity velocity(double vx, double vy){
		velocity.set(vx, vy);
		return this;
	}

	/**
	 * Sets the direction the entity is facing. This is not the same as the 
	 * but will rotate towards the velocity vector at a speed dependent on 
	 * the turn-rate.
	 * 
	 * @param h
	 */
	public MovingEntity heading(Vector2D h){
		heading.set(h);
		return this;
	}

	/**
	/**
	 * Sets the direction the entity is facing. This is not the same as the 
	 * but will rotate towards the velocity vector at a speed dependent on 
	 * the turn-rate.
	 * 
	 * @param x
	 * @param y
	 */
	public MovingEntity heading(double x, double y){
		heading.set(x, y);
		return this;
	}

	/**
	 * Get the current heading for this entity
	 * @return the entity's heading
	 */
	public Vector2D heading(){
		return heading;
	}

	/**
	 * get the default heading for this entity.
	 * @return the headingAtRest
	 */
	public Vector2D headingAtRest() {
		return headingAtRest;
	}

	/**
	 * Set the default heading for this entity. If the parameter
	 * is <pre>null</pre> then the default heading is cancelled.
	 * The default heading will be normalised.
	 *  
	 * @param restHeading the headingAtRest to set
	 */
	public MovingEntity headingAtRest(Vector2D restHeading) {
		// if the parameter is null then we are cancelling it
		if(restHeading == null){ 		// Cancel heading at rest?
			headingAtRest = null; 		// yes
		}
		else {							// no
			if(headingAtRest == null)	// only create vector if needed.
				headingAtRest = new Vector2D(restHeading);
			else
				headingAtRest.set(restHeading);	
			headingAtRest.normalize();
		}
		return this;
	}

	/**
	 * Change the mass of the entity. The heavier the mass the more force 
	 * is needed to move it. <br>
	 * The new mass must be greater than zero otherwise the current mass
	 * will be unchanged..
	 * 
	 * @param mass the new mass
	 */
	public void mass(double mass){
		if(mass > 0)
			this.mass = mass;
	}

	/**
	 * Get the mass of this entity.
	 * @return the entity's mass
	 */
	public double mass(){
		return mass;
	}

	/**
	 * Get the side vector for this entity. The side vector is
	 * always perpendicular to the heading and normalised.
	 * @return the side vector
	 */
	public Vector2D side(){
		return side;
	}

	/**
	 * Get the maximum speed allowed for this entity.
	 * @return max speed allowed
	 */
	public double maxSpeed(){
		return maxSpeed;
	}                       

	/**
	 * Sets the maximum speed this entity is allowed to reach
	 * @param maxSpeed
	 */
	public MovingEntity maxSpeed(double maxSpeed){
		this.maxSpeed = maxSpeed;
		return this;
	}

	/**
	 * Gets the maximum force that can be applied to this entity.
	 * @return max force allowed
	 */
	public double maxForce(){
		return maxForce;
	}

	/**
	 * Sets the maximum force that can be applied to this entity.
	 * @param mf max force allowed
	 */
	public MovingEntity maxForce(double mf){
		maxForce = mf;
		return this;
	}

	/**
	 * See if the current speed exceeds the maximum speed permitted.
	 * @return true if the speed is greater or equal to the max speed.
	 */
	public boolean isSpeedMaxedOut(){
		return velocity.lengthSq() >= maxSpeed*maxSpeed;
	}

	/**
	 * Get the entity's speed. <br>
	 * This is the scalar length of the velocity vector.
	 * @return speed in direction of travel
	 */
	public double speed(){
		return velocity.length();
	}

	/**
	 * Get the square of the entity's speed.
	 * @return speed in direction of travel squared
	 */
	public double speedSq(){
		return velocity.lengthSq();
	}


	/**
	 * Get the maximum turn rate for this entity
	 */
	public double maxTurnRate(){
		return maxTurnRate;
	}

	/**
	 * Sets the maximum turnrate for this entity.
	 * @param maxTurnRate
	 */
	public MovingEntity maxTurnRate(double maxTurnRate){
		this.maxTurnRate = maxTurnRate;
		return this;
	}

	/**
	 * Get the current turn rate
	 */
	public double turnRate(){
		return currTurnRate;
	}

	/**
	 * Set the current turnrate making sure it does not
	 * exceed the maximum allowed. 
	 * @param turnRate
	 */
	public MovingEntity turnRate(double turnRate){
		currTurnRate = FastMath.min(turnRate, maxTurnRate);
		return this;
	}


	/**
	 * Get the distance that this entity can see.
	 * @return the viewDistance
	 */
	public double viewDistance() {
		return viewDistance;
	}

	/**
	 * This sets the distance that this entity can see. It is used by
	 * the canSee() method.
	 * @param viewDistance the viewDistance to set
	 */
	public MovingEntity viewDistance(double viewDistance) {
		this.viewDistance = viewDistance;
		return this;
	}

	/**
	 * Get the field of view for this entity.
	 * @return the viewFOV
	 */
	public double viewFOV() {
		return viewFOV;
	}

	/**
	 * Sets the total field of view angle for this entity.
	 * 
	 * @param viewFOV the viewFOV to set
	 */
	public MovingEntity viewFOV(double viewFOV) {
		this.viewFOV = viewFOV;
		return this;
	}

	/**
	 * Sets the distance and total field of view angle for this entity.
	 * @param viewDistance
	 * @param viewFOV the viewFOV to set
	 */
	public MovingEntity viewFactors(double viewDistance, double viewFOV){
		this.viewDistance = viewDistance;
		this.viewFOV = viewFOV;
		return this;
	}

	/**
	 * Set the world domain for this entity and its' constraint to the default value of SBF.WRAP 
	 * this means that when an entity leaves the domain it is wrapped to the other side.
	 * @param wd the world domain for this entity
	 */
	public MovingEntity worldDomain(Domain wd){
		this.wd = wd;
		domainConstraint = SBF.WRAP;
		return this;
	}

	/**
	 * Set the world domain
	 * @param wd the world domain for this entity
	 * @param constraint SBF.WRAP or SBF.REBOUND or SBF.PASS_THROUGH
	 */
	public MovingEntity worldDomain(Domain wd, int constraint){
		this.wd = wd;
		if(constraint == SBF.WRAP || constraint == SBF.REBOUND || constraint == SBF.PASS_THROUGH)
			domainConstraint = constraint;
		return this;
	}

	/**
	 * Set the constraint to be applied to the world domain.
	 * @param constraint SBF.WRAP or SBF.REBOUND or SBF.PASS_THROUGH
	 */
	public MovingEntity worldDomainConstraint(int constraint){
		if(constraint == SBF.WRAP || constraint == SBF.REBOUND || constraint == SBF.PASS_THROUGH)
			domainConstraint = constraint;
		return this;
	}

	/**
	 * Get the world domain for this entity.
	 * @return the world domain
	 */
	public Domain worldDomain(){
		return wd;
	}

	/**
	 * Get the world domain constraint for this entity.
	 * @return the world domain constraint (ie SBF.WRAP or SBF.REBOUND or SBF.PASS_THROUGH)
	 */
	public int worldDomainConstraint(){
		return domainConstraint;
	}

	/**
	 * After calculating the entity's position it is then constrained by
	 * the domain constraint WRAP or REBOUND
	 */
	protected void applyDomainConstraint(){
		switch(domainConstraint){
		case SBF.WRAP:
			if(pos.x < wd.lowX)
				pos.x += wd.width;
			else if(pos.x > wd.highX)
				pos.x -= wd.width;
			if(pos.y < wd.lowY)
				pos.y += wd.height;
			else if(pos.y > wd.highY)
				pos.y -= wd.height;
			break;
		case SBF.REBOUND:
			if(pos.x < wd.lowX)
				velocity.x = FastMath.abs(velocity.x);
			else if(pos.x > wd.highX)
				velocity.x = -FastMath.abs(velocity.x);
			if(pos.y < wd.lowY)
				velocity.y = FastMath.abs(velocity.y);
			else if(pos.y > wd.highY)
				velocity.y = -FastMath.abs(velocity.y);
			break;
		default:
			break;
		}
	}

	/**
	 * Determines whether two points are either side of this moving entity. If they are 
	 * then they cannot 'see' each other.
	 * 
	 * @param x0 x position of first point of interest
	 * @param y0 y position of first point of interest
	 * @param x1 x position of second point of interest
	 * @param y1 y position of second point of interest
	 * @return true if the points are either side else false
	 */
	public boolean isEitherSide(double x0, double y0, double x1, double y1){
		return Geometry2D.line_circle(x0, y0, x1, y1, pos.x, pos.y, colRadius);
	}

	/**
	 * This method determines whether this entity can see a particular location in the world. <br>
	 * It first checks to see if it is within this entity's view distance and field of view (FOV).
	 * If it is then it checks to see if there are any walls or obstacles between them.
	 * 
	 * @param world the world responsible for this entity
	 * @param x0 the x position of the location to test
	 * @param y0 the y position of the location to test
	 * @return true if the entity can see the location
	 */
	public boolean canSee(World world, double x0, double y0){
		Vector2D toTarget = new Vector2D(x0 - pos.x, y0 - pos.y);
		// See if in view range
		double distToTarget = toTarget.length();
		if(distToTarget > viewDistance)
			return false;
		// See if in field of view
		toTarget.div(distToTarget);	// normalise toTarget
		double cosAngle = heading.dot(toTarget);
		if(cosAngle <  FastMath.cos(viewFOV / 2))
			return false;
		// If we get here then the position is within range and field of view, but do we have an obstruction.
		// First check for an intervening wall 
		Set<Wall> walls = world.getWalls(this, x0, y0);
		if(walls != null && !walls.isEmpty()){
			for(Wall wall : walls){
				if(wall.isEitherSide(pos.x, pos.y, x0, y0))
					return false;
			}
		}
		// Next check for an intervening obstacle 
		Set<Obstacle> obstacles = world.getObstacles(this, x0, y0);
		if(obstacles != null && !obstacles.isEmpty()){
			for(Obstacle obstacle : obstacles){
				if(obstacle.isEitherSide(pos.x, pos.y, x0, y0))
					return false;					
			}
		}
		return true;
	}

	/**
	 * This method determines whether this entity can see a particular location in the world. <br>
	 * It first checks to see if it is within this entity's view distance and field of view (FOV).
	 * If it is then it checks to see if there are any walls or obstacles between them.
	 * 
	 * @param world the world responsible for this entity
	 * @param pos the location to test
	 * @return true if the entity can see the location
	 */
	public boolean canSee(World world, Vector2D pos){
		if(pos == null)
			return false;
		else
			return canSee(world, pos.x, pos.y);
	}

	/**
	 * ----------------------- RotateHeadingToFacePosition ------------------
	 * 
	 * given a target position, this method rotates the entity's heading and
	 * side vectors by an amount not greater than m_dMaxTurnRate until it
	 * directly faces the target.
	 * 
	 * @param deltaTime time
	 * @param faceTarget the world position to face
	 * @return true when the heading is facing in the desired direction
	 */
	public boolean rotateHeadingToFacePosition(double deltaTime, Vector2D faceTarget){
		// Calculate the normalised vetor to the face target
		Vector2D alignTo = Vector2D.sub(faceTarget, pos);
		alignTo.normalize();
		return rotateHeadingToAlignWith(deltaTime, alignTo);
	}

	/**
	 * Rotate this entities heading to align with a vector over a given time period
	 * @param deltaTime time (seconds) to turn entity
	 * @param allignTo vector to align entities heading with
	 * @return true if facing alignment vector
	 */
	public boolean rotateHeadingToAlignWith(double deltaTime, final Vector2D allignTo){
		// Calculate the angle between the heading vector and the target
		double angleBetween = heading.angleBetween(allignTo);

		// Return true if the player is virtually facing the target
		if (angleBetween < Double.MIN_VALUE) return true;

		// Calculate the amount of turn possible in time allowed
		double angleToTurn = currTurnRate * deltaTime;

		// Prevent over steer by clamping the amount to turn to the angle angle 
		// between the heading vector and the target
		if (angleToTurn > angleBetween) angleToTurn = angleBetween;

		// The next few lines use a rotation matrix to rotate the player's heading
		// vector accordingly
		Matrix2D rotMatrix = new Matrix2D();

		// The direction of rotation is needed to create the rotation matrix
		rotMatrix.rotate(angleToTurn * allignTo.sign(heading));	
		// Rotate heading
		heading = rotMatrix.transformVector2D(heading);
		heading.normalize();
		// Calculate new side
		side = heading.getPerp();
		return false;
	}

	/**
	 * Determine whether this moving entity is inside or part inside the domain. This method is
	 * used by the world draw method to see if this entity should be drawn.
	 * @param view the world domain
	 * @return true if any part of this entity is inside the domain
	 */
	@Override
	public boolean isInDomain(Domain view) {
		return (pos.x >= view.lowX && pos.x <= view.highX && pos.y >= view.lowY && pos.y <= view.highY);
	}

	/**
	 * Determines whether a point is over this entity's collision circle
	 */
	@Override
	public boolean isOver(double px, double py) {
		return ((pos.x - px)*(pos.x - px) + (pos.y - py)*(pos.y - py)) <= (colRadius * colRadius);
	}

	/**
	 * Update method for any moving entity in the world that is not under
	 * the influence of a steering behaviour.
	 * @param deltaTime elapsed time since last update
	 * @param world the game world object
	 */
	@Override
	public void update(double deltaTime, World world) {
		// Remember the starting position
		prevPos.set(pos);
		// Update position
		pos = new Vector2D(pos.x + velocity.x * deltaTime, pos.y + velocity.y * deltaTime);
		// Apply domain constraints
		if(wd != null)
			applyDomainConstraint();
		// Update heading
		if(velocity.lengthSq() > 0.01)
			rotateHeadingToAlignWith(deltaTime, velocity);
		else {
			velocity.set(0,0);
			if(headingAtRest != null)
				rotateHeadingToAlignWith(deltaTime, headingAtRest);
		}
		// Ensure heading and side are normalised
		heading.normalize();
		side.set(-heading.y, heading.x);
	}

	/**
	 * DO NOT CALL THIS METHOD DIRECTLY <br>
	 * If this shape has a renderer and is visible call the renderer's draw method passing all 
	 * appropriate information. <br>
	 * 
	 * This should be overridden in child classes
	 */
	@Override
	public void draw(double elapsedTime, World world){
		if(renderer != null && visible)
			renderer.draw(this, (float) pos.x, (float) pos.y, 
					(float) velocity.x, (float) velocity.y, 
					(float) heading.x, (float) heading.y, (float)elapsedTime); 	
	}	

	@Deprecated
	@Override
	public void draw(World world) {
		if(renderer != null && visible){
			renderer.draw(this, (float) pos.x, (float) pos.y, 
					(float) velocity.x, (float) velocity.y, 
					(float) heading.x, (float) heading.y); 
		}
	}

}
