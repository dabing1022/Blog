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

package game2dai.utils;

public class StopWatch {

	private long currTime;
	private long lastTime;
	private long lapTime;
	private long startTime;
		
	/**
	 * Create a stop watch and initialise it to the current time.
	 */
	public StopWatch(){
		reset();
	}
	
	/**
	 * Initialise the stop-watch to the current time.
	 */
	public void reset(){
		currTime = lastTime = startTime = System.nanoTime();
		lapTime = 0;
	}
	
	/**
	 * Get the time since the stop watch was created or last reset.
	 * @return run time in seconds
	 */
	public double getRunTime(){
		double rt = 1.0E-9 * (System.nanoTime() - startTime);
		return rt;
	}
	
	/**
	 * Get the elapsed time since this method was called. <br>
	 * This should be called just ONCE at the beginning of the draw cycle
	 * @return elapsed time in seconds.
	 */
	public double getElapsedTime(){
		currTime = System.nanoTime();
		lapTime = currTime - lastTime;
		lastTime = currTime;
		return 1.0E-9 * lapTime;
	}
	
}
