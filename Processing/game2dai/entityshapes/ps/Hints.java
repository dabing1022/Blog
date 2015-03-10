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

package game2dai.entityshapes.ps;

import game2dai.entities.BaseEntity;
import game2dai.entities.MovingEntity;
import game2dai.entities.Vehicle;
import game2dai.maths.FastMath;
import game2dai.maths.Vector2D;
import game2dai.steering.AutoPilot;
import processing.core.PApplet;

/**
 * Draw the steering behaviour hints for a particular entity (usually 
 * either MovingEntity or Vehicle entity). <br>
 * 
 * Hints available are <br>
 * HINT_HEADING  the direction the entity is facing<br>
 * HINT_VELOCITY the velocity vector<br>
 * HINT_COLLISION the collision radius <br>
 * HINT_WHISKERS the feelers used in wall avoidance <br>
 * HINT_OBS_AVOID the detection box used in obstacle avoidance <br>
 * HINT_WANDER the wander direction and circle <br>
 * HINT_VIEW the area that can be seen by the entity <br>
 * 
 * When setting the hints to be shown they can be or'ed together e.g.
 * Hints.HINT_HEADING | hints.HINT_VELOCITY
 * 
 * @author Peter Lager
 *
 */
public class Hints {

	public static final int HINT_HEADING			= 0x00000001;
	public static final int HINT_VELOCITY			= 0x00000002;
	public static final int	HINT_COLLISION			= 0x00000004;
	public static final int HINT_WHISKERS			= 0x00000008;
	public static final int HINT_OBS_AVOID			= 0x00000010;
	public static final int HINT_WANDER				= 0x00000020;
	public static final int HINT_VIEW				= 0x00000040;

	public static final int HINT_NONE				= 0x00000000;
	public static final int HINT_ALL				= 0x0000FFFF;

	public static final Vector2D ZERO_VECTOR 		= new Vector2D();
	public static final Vector2D FORWARD_VECTOR 	= new Vector2D(1,0);

	public static int hintFlags = 0;

	/**
	 * This is the only method available and should be called from the renderer object.
	 * 
	 * @param app
	 * @param owner
	 * @param velX
	 * @param velY
	 * @param headX
	 * @param headY
	 */
	public static void draw(PApplet app, BaseEntity owner, float velX, float velY, float headX, float headY) {
		// Make sure we have a moving entity and something to draw it with otherwise 
		// there is nothing to do there is nothing to do
		if( app == null || !(owner instanceof MovingEntity) )
			return;

		boolean isMovingEntity = (owner instanceof MovingEntity);
		AutoPilot ap = (owner instanceof Vehicle) ? ((Vehicle)owner).AP() : null;

		// Get the common stuff
		MovingEntity me = (MovingEntity)owner;
		Vector2D head = me.heading();
		Vector2D vel = me.velocity();
		Vector2D pos = me.pos();
		float colrad = (float) me.colRadius();

		app.pushStyle();
		app.pushMatrix();
		app.translate((float) pos.x, (float) pos.y);
		app.rotate(PApplet.atan2(headY, headX));
		app.ellipseMode(PApplet.CENTER);
		app.rectMode(PApplet.CORNER);
		if(ap != null){
			if((hintFlags & HINT_WANDER) != 0 && ap.isWanderOn())
				drawWanderBits(app, ap);
			if((hintFlags & HINT_OBS_AVOID ) != 0 && ap.isObstacleAvoidOn())
				drawDetectionBox(app, ap.obstacleAvoidDetectBoxLength(), vel.length(), me.maxSpeed(), colrad);
			if((hintFlags & HINT_WHISKERS) != 0  && ap.isWallAvoidOn())
				drawWhiskers(app, ap);
		}
		if((hintFlags & HINT_COLLISION) != 0)
			drawCollision(app, vel, colrad);
		if(isMovingEntity){
			if((hintFlags & HINT_VIEW) != 0)
				drawView(app, me);
			if((hintFlags & HINT_HEADING) != 0)
				drawHeading(app, colrad);
			if((hintFlags & HINT_VELOCITY) != 0)
				drawVelocity(app, vel, head, colrad);
		}
		app.popMatrix();
		app.popStyle();
	}

	private static void drawView(PApplet app, MovingEntity v){
		float searchDist = (float) v.viewDistance();
		float fov = (float) v.viewFOV();
		app.fill(0, 255, 0, 32);
		app.stroke(0, 128, 0, 64);
		app.strokeWeight(1);
		app.arc(0, 0, 2*searchDist, 2*searchDist, -fov/2, fov/2, PApplet.PIE);
	}

	private static void drawDetectionBox(PApplet app, double detectBoxLength,
			double speed, double maxSpeed, float colrad) {
		float boxLength = (float) (detectBoxLength * (1 + speed/maxSpeed));
		app.fill(128,128,255,64);
		app.stroke(64,64,255,64);
		app.strokeWeight(1);
		app.rect(0, -colrad, boxLength, 2*colrad);
	}

	private static void drawWhiskers(PApplet app, AutoPilot ap) {
		Vector2D[] feelers = ap.getFeelers(FORWARD_VECTOR, ZERO_VECTOR);
		app.stroke(0,192,192);
		app.strokeWeight(1);
		for(int f = 0; f < feelers.length; f++){
			app.line(0, 0, (float)feelers[f].x, (float)feelers[f].y);
		}
	}

	private static void drawCollision(PApplet app, Vector2D vel, float colrad) {
		app.fill(200,200,0,128);
		app.noStroke();
		app.ellipse(0, 0, 2*colrad, 2* colrad);	
	}

	private static void drawVelocity(PApplet app, Vector2D vel, Vector2D head, float colrad){
		float angle = (float) vel.angleBetween(head);
		int dir = vel.sign(head);
		app.pushMatrix();
		app.rotate(dir * angle);
		app.fill(255, 0, 255);
		app.stroke(255, 0, 255);
		app.strokeWeight(1.5f);
		app.line(0, 0, colrad + (float)vel.length(), 0);
		app.ellipse(colrad + (float)vel.length(), 0, 3, 3);
		app.popMatrix();
	}

	private static void drawHeading(PApplet app, float colrad){
		app.strokeWeight(2.5f);
		app.stroke(0,0,160);
		app.fill(0,0,160);
		app.line(0,0, 2 * colrad, 0);
		app.ellipse(2 * colrad, 0, 4, 4);
	}

	private static void drawWanderBits(PApplet app, AutoPilot ap){
		float wdist = (float) ap.wanderDist();
		float wradius = (float) ap.wanderRadius();
		float wangle = (float) ap.wanderAngle();
		float wangleDelta = (float) ap.wanderAngleDelta();

		float tx, ty;
		tx = wdist + (float) FastMath.cos(wangle) * wradius;
		ty = (float) FastMath.sin(wangle) * wradius;

		// Line to centre of wander circle
		app.stroke(0, 90);
		app.strokeWeight(1);
		app.line(0, 0, Math.max(0, wdist - wradius), 0);
		// Wander circle
		app.stroke(255, 0, 0);
		app.line(0, 0, tx, ty);
		app.noStroke();
		app.fill(255, 0, 0, 48);
		app.ellipse(wdist, 0, 2*wradius, 2*wradius);
		// Extent of jitter per frame
		app.arc(wdist, 0, 2*wradius, 2*wradius, wangle - wangleDelta, wangle + wangleDelta);
		// Steering force target 
		app.fill(255,0,0);
		app.ellipse(tx, ty, 6, 6);
	}
}
