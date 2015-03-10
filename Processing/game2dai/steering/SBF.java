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

public interface SBF {

	// Bit positions for flags for internal library use.
    int BIT_WALL_AVOID			= 0;
    int BIT_OBSTACLE_AVOID		= 1;
    int BIT_EVADE				= 2;
    int BIT_FLEE				= 3;
    int BIT_SEPARATION			= 4; // These three
    int BIT_ALIGNMENT			= 5; // together for
    int BIT_COHESION			= 6; // flocking
    int BIT_SEEK				= 7;
    int BIT_ARRIVE				= 8;
    int BIT_WANDER				= 9;
    int BIT_PURSUIT				= 10;
    int BIT_OFFSET_PURSUIT		= 11;
    int BIT_INTERPOSE			= 12;
    int BIT_HIDE				= 13;
    int BIT_PATH				= 14;
    int BIT_FLOCK				= 15;

    // Behaviour identifier constants (flag values)
    int WALL_AVOID			= 1 << BIT_WALL_AVOID;
    int OBSTACLE_AVOID		= 1 << BIT_OBSTACLE_AVOID;
    int EVADE				= 1 << BIT_EVADE;
    int FLEE				= 1 << BIT_FLEE;
    int SEPARATION			= 1 << BIT_SEPARATION;	// These three
    int ALIGNMENT			= 1 << BIT_ALIGNMENT;	// together for
    int COHESION			= 1 << BIT_COHESION;	// flocking
    int SEEK				= 1 << BIT_SEEK;
    int ARRIVE				= 1 << BIT_ARRIVE;
    int WANDER				= 1 << BIT_WANDER;
    int PURSUIT				= 1 << BIT_PURSUIT;
    int OFFSET_PURSUIT		= 1 << BIT_OFFSET_PURSUIT;
    int INTERPOSE			= 1 << BIT_INTERPOSE;
    int HIDE				= 1 << BIT_HIDE;
    int PATH				= 1 << BIT_PATH;
    int FLOCK 				= 1 << BIT_FLOCK;

    // All behaviours mask used when switching off a behaviour
     int ALL_SB_MASK			= 0x0ffffff;
    
    int WEIGHTED					= 1;
    int WEIGHTED_PRIORITIZED		= 2;
 
    double[] DECEL_TWEEK	= new double[] {0.0, 0.3, 0.6, 0.9}; 
    int FAST        		= 1;
    int NORMAL           	= 2;
    int SLOW   				= 3;
    
    // These refer to array index values so don't change them.
    int AGENT0				= 0;
    int AGENT1				= 1;
    int AGENT_TO_PURSUE		= 2;
    int AGENT_TO_EVADE		= 3;
    int NBR_AGENT_ARRAY		= 4;
       
	int PASS_THROUGH		= 10000;
	int WRAP 				= 10001;
	int REBOUND 			= 10002;
}
