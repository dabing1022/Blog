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

import game2dai.entityshapes.Picture;
import processing.core.PApplet;

/**
 * This is the base class for all entity renderers to be used in Processing <br/>
 * 
 * For details about rendering hints for steering behaviours see the Picture class API.
 * 
 * @author Peter Lager
 *
 */
public class PicturePS extends Picture {

	
	/** Used for drawing the shape */
	protected PApplet app = null;

	/**
	 * Default constructor - use this constructor if the child class is to be a 
	 * local class in the sketch. <br>
	 * This constructor must be called by all child class constructors so that
	 * the renderer has access to the drawing surface. <br>
	 */
	public PicturePS(){
		super();
	}
	
	/**
	 * Use this constructor if the child class is to be a top level class (either 
	 * in its own .java tab or declared as static in a .pde tab) in the sketch. <br>
	 * This constructor must be called by all child class constructors so that
	 * the renderer has access to the drawing surface. <br>
	 * @param papp
	 */
	public PicturePS(PApplet papp){
		super();
		app = papp;
	}
	
	/**
	 * This method allows the user to specify the PApplet object responsible
	 * for drawing this picture. This need only be called if the renderer was 
	 * created with the default (no parameter) constructor and you want to display hints.
	 * @param app
	 */
	public void setApplet(PApplet app){
		if(app != null)
			this.app = app;		
	}
	
}
