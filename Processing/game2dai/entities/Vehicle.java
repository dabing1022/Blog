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

import game2dai.World;
import game2dai.maths.Vector2D;
import game2dai.steering.AutoPilot;
import game2dai.utils.ForceRecorder;
import game2dai.utils.VehicleSAXParser;

import java.io.File;

import processing.core.PApplet;

/**
 * This class models the behaviour of a moving entity acting under the influence 
 * of an AutoPilot.
 * 
 * @author Peter Lager
 *
 */
public class Vehicle extends MovingEntity {

	/**
	 * This is the one to use with Processing
	 * 
	 * @param app
	 * @param xmlFilename
	 * @return an array of Vehicles
	 */
	public static Vehicle[] makeFromXML(PApplet app, String xmlFilename){
		return new VehicleSAXParser(app, xmlFilename).get();
	}

	/**
	 * Alternative if not using Processing
	 * @param xmlFilename name of the file to parse
	 * @return an array of Buildings
	 */
	public static Vehicle[] makeFromXML(String xmlFilename){
		return new VehicleSAXParser(xmlFilename).get();
	}

	/**
	 * Alternative if not using Processing
	 * @param xmlFile File to parse
	 * @return an array of Buildings
	 */
	public static Vehicle[] makeFromXML(File xmlFile){
		return new VehicleSAXParser(xmlFile).get();
	}

	/** The auto-pilot */
	private AutoPilot ap;

	/** The accumulated force from the steering behaviours */
	protected Vector2D force = new Vector2D();
	/** The acceleration created from the accumulated force */
	protected Vector2D accel = new Vector2D();

	/** Used to record force calculation data for this vehicle. */
	protected ForceRecorder forceRecorder = null;

	/**
	 * The constructor that should be used when creating a Vehicle without a name.
	 * @param position initial world position
	 * @param radius bounding radius
	 * @param velocity initial velocity
	 * @param max_speed maximum speed
	 * @param heading initial heading
	 * @param mass initial mass
	 * @param max_turn_rate how fast the entity can turn (radians / second)
	 * @param max_force the maximum force that can be applied by a steering behaviour
	 */
	public  Vehicle(	
			Vector2D position,
			double   radius,
			Vector2D velocity,
			double   max_speed,
			Vector2D heading,
			double   mass,
			double   max_turn_rate,
			double   max_force){

		this("",  position, radius, velocity, max_speed, heading, mass,
				max_turn_rate, max_force);
	}

	/**
	 * The constructor that should be used when creating a Vehicle with a name.
	 * @param name optional name for the entity
	 * @param position initial world position
	 * @param radius bounding radius
	 * @param velocity initial velocity
	 * @param max_speed maximum speed
	 * @param heading initial heading
	 * @param mass initial mass
	 * @param max_turn_rate how fast the entity can turn (radians / second)
	 * @param max_force the maximum force that can be applied by a steering behaviour
	 */
	public  Vehicle(	
			String name,
			Vector2D position,
			double   radius,
			Vector2D velocity,
			double   max_speed,
			Vector2D heading,
			double   mass,
			double   max_turn_rate,
			double   max_force){

		super(name, position, radius, velocity, max_speed, heading, mass,
				max_turn_rate, max_force);
		visible = true;
		ap = new AutoPilot();
		ap.setOwner(this);
	}

	/**
	 * Set the auto-pilot to use with this entity. Null vales and
	 * steering behaviour object with an owner are ignored.
	 * 
	 * @param ap an available steering behaviour 
	 */
	public Vehicle AP(AutoPilot ap){
		if(ap != null && !ap.hasOwner()){
			this.ap = ap;
			this.ap.setOwner(this);
		}
		return this;
	}

	/**
	 * Use this to access the auto-pilot object so that it can be modified. 
	 */
	public AutoPilot AP(){
		return ap;
	}

	/**
	 * Get the maximum force that can be applied to this entity
	 */
	public double maxForce(){
		return maxForce;
	}

	/**
	 * Enable the force recorder on for this Vehicle. This will delete any 
	 * collected data and start the logger. <br>
	 * 
	 * The recorder will record the minimum, maximum average and number of readings
	 * for each steering force. <br>
	 *  
	 * This is useful in tweaking max force values for vehicles and to 
	 * help decide on the best force calculation method to use. <br>
	 * 
	 * The force recorder should be switched off in the final sketch.
	 * 
	 */
	public Vehicle forceRecorderOn(){
		forceRecorder = new ForceRecorder(this);
		return this;
	}

	/**
	 * Dump the force recorder on for this Vehicle. Any collected 
	 * data will be lost.<br>
	 */
	public Vehicle forceRecorderOff(){
		forceRecorder = null;
		return this;
	}

	/**
	 * Get the force recorder if this vehicle has one. This is used by the World class
	 * when displaying all force logs
	 * @return the force logger or null if it doesn't exist
	 */
	public ForceRecorder forceRecorder(){
		return forceRecorder;
	}

	/**
	 * See if this vehicle has a force recorder.
	 * 
	 * @return true if force logger exists
	 */
	public boolean hasForceRecorder(){
		return forceRecorder != null;
	}

	/**
	 * Display the steering force data for this Vehicle. If there is no 
	 * recorder or no data has been collected for this Vehicle then
	 * nothing is displayed.
	 */
	public void printForceData(){
		if(forceRecorder != null && forceRecorder.hasData())
			System.out.println(forceRecorder);
	}

	/**
	 * Update method for any moving entity in the world that is under
	 * the influence of a steering behaviour.
	 * @param deltaTime elapsed time since last update
	 * @param world the game world object
	 */
	public void update(double deltaTime, World world) {
		// Remember the starting position
		prevPos.set(pos);
		// Accumulator for forces
		force.set(0,0);
		accel.set(0,0);
		if(ap != null){
			force.set(ap.calculateForce(deltaTime, world));
			force.truncate(maxForce);
			accel.set(Vector2D.div(force, mass));
			accel.set(force);
			accel.div(mass);
			// Change velocity according to acceleration and elapsed time
			accel.mult(deltaTime);
			velocity.add(accel);
		}
		// Make sure we don't exceed maximum speed
		velocity.truncate(maxSpeed);
		// Change position according to velocity and elapsed time
		pos.add(Vector2D.mult(velocity, deltaTime));
		// Apply domain constraints
		if(wd != null)
			applyDomainConstraint();
		// Update heading
		if(velocity.lengthSq() > 0.25)
			rotateHeadingToAlignWith(deltaTime, velocity);
		else {
			velocity.set(0,0);
			if(headingAtRest() != null)
				rotateHeadingToAlignWith(deltaTime, headingAtRest());
		}
		// Ensure heading and side are normalised
		heading.normalize();
		side.set(-heading.y, heading.x);
	}

	public String toString(){
		StringBuilder s = new StringBuilder(name + "  @ " + pos + "  V:" + velocity + "  H: "+ heading + "\n");
		s.append("\tCR: " + colRadius + "   MASS: " + mass + "   MTR: " + maxTurnRate + "   MF: " + maxForce + "   MS: " + maxSpeed);
		return new String(s);
	}
}
