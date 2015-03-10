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

package game2dai.entityshapes;

import game2dai.entities.BaseEntity;
import game2dai.entityshapes.ps.Hints;

/**
 * Any class to be used to render an entity on the screen should inherit
 * from this class. <br>
 * It provides a simple way to display steering behaviour hints for any
 * entity renderer. This is particularly useful when it comes to testing
 * your steering behaviours. <br>
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
public abstract class Picture {

	/** bit flag defining hints to be displayed */
	protected int hints = 0;

	/**
	 * This constructor is provided when not using Processing.
	 */
	public Picture(){ }
	

	/**
	 * DO NOT USE THIS METHOD <br/>
	 * This method is left to ensure backward compatibility with library versions prior to 1.0 <br/>
	 * 
	 * Use the draw method with the extra parameter for elapsed time. <br/>
	 * 
	 */
	@Deprecated
	public void draw(BaseEntity owner, float posX, float posY, float velX,
			float velY, float headX, float headY) {
		draw(owner, posX, posY, velX, velY, headX, headY, 0);
	}

	/**
	 * This method must be overridden in all child classes otherwise nothing is drawn. <br/>
	 * This version of the draw method has an additional parameter for the elpased time since last
	 * update and will be called will be called by the 
	 * 
	 * @param owner the entity that owns this renderer.
	 * @param posX real world position (x)
	 * @param posY real world position (x)
	 * @param velX magnitude of the velocity vector in the x direction
	 * @param velY magnitude of the velocity vector in the y direction
	 * @param headX magnitude of the heading vector in the x direction
	 * @param headY magnitude of the heading vector in the y direction
	 * @param etime the elapsed time in seconds since last update
	 */
	public void draw(BaseEntity owner, float posX, float posY, float velX,
			float velY, float headX, float headY, float elapsedTime) {
	}
	
	/**
	 * Defines the steering behaviour (SB) hints to be displayed. <br>
	 * Although you can specify any and all hints, only hints for SBs currently in use will be displayed. <br> 
	 * This method replaces any existing SB hints.
	 * @param hints
	 */
	public void showHints(int hints){
		this.hints = hints;
	}

	/**
	 * Add more steering behaviour (SB) hints to those already selected.
	 * @param hints
	 */
	public void addHints(int hints){
		this.hints |= hints;
	}
	
	/**
	 * Remove some or all of the steering behaviour (SB) hints currently selected.
	 * @param hints
	 */
	public void removeHints(int hints){
		this.hints &= (Hints.HINT_ALL ^ hints);
	}
	
	/**
	 * Remove all steering behaviour (SB) hints.
	 */
	public void removeAllHints(){
		this.hints = 0;
	}
	
	/**
	 * Get the hints being currently used.
	 */
	public int getHints(){
		return hints;
	}
}
