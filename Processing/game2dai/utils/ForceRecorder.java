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

import java.text.DecimalFormat;

import game2dai.entities.Vehicle;
import game2dai.maths.FastMath;
import game2dai.maths.Vector2D;
import game2dai.steering.SBF;

public class ForceRecorder implements SBF {

	private Vehicle owner = null;
	private Force[] forces = null;
	private int nbrReadings = 0;
	
	public ForceRecorder(Vehicle owner){
		this.owner = owner;
		forces = new Force[16];
		for(int i = 0; i < forces.length; i++)
			forces[i] = new Force(i, forceNames[i]);
	}
	
	public void addData(int type, Vector2D force){
		if(type >= 0 && type < forces.length){
			double mag = force.length();
			if(mag > 1){
				nbrReadings++;
				forces[type].addData(mag);
			}
		}
	}
	
	public boolean hasData(){
		return (nbrReadings > 0);
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder("------------------------------------------------------------------------------");
		s.append("\nName:   " + owner.name());
		s.append("\nID:     " + owner.ID());
		if(owner.AP().calculateMethod() == WEIGHTED)
			s.append("\nForce calculator:   Weighted Truncated Sum");
		else
			s.append("\nForce calculator:   Weighted Truncated Running Sum with Prioritization");
		s.append("\nMax force: " + owner.maxForce() + "\n");
		s.append("\n                        Min        Max        Avg    Std Dev  Count     Weight");
		for(Force f : forces){
			if(f.hasData())
				s.append("\n" + f.toString());
		}
		s.append("\n------------------------------------------------------------------------------");
		return new String(s);
	}
	
	private static String[] forceNames = new String[] {
		" Wall avoid.    ",	
		" Obstacle avoid ",
		" Evade          ",
		" Flee           ",
		" Separation     ", 
		" Alignment      ",
		" Cohesion       ",
		" Seek           ",
		" Arrive         ",
		" Wander         ",
		" Pursuit        ", 
		" Offset Pursuit ",
		" Interpose      ",
		" Hide           ",
		" Path           ",
		" Flock          "
	};
	
	private static String spacer = "                                         ";
	
	public class Force {
		private int forceID = -1;
		private String name = "";
		private double min = Double.MAX_VALUE;
		private double max = 0;
		private double s1 = 0;
		private double s2 = 0;
		private int n = 0;
		
		public Force(int id, String name){
			this.forceID = (1 << id);
			this.name = name;
		}
		
		public void addData(double forceMagnitude){
			if(forceMagnitude < min) min = forceMagnitude;
			if(forceMagnitude > max) max = forceMagnitude;
			s1 += forceMagnitude;
			s2 += forceMagnitude * forceMagnitude;
			n++;
		}

		public boolean hasData(){
			return (n > 0);
		}

		public double getAverage(){
			if(n > 0)
				return s1 / n;
			else
				return 0;
		}
		
		public double getStdDev(){
			if(nbrReadings > 0)
				return FastMath.sqrt(n * s2 - s1 * s1)/n;
			else
				return 0;
		}
		
		
		private String getString(double number, String pattern){
			DecimalFormat myFormatter = new DecimalFormat(pattern);
			String ns =  myFormatter.format(number);
			int fillLengthReqd = pattern.length() - ns.length();
			if(fillLengthReqd > 0)
				ns = spacer.substring(0, fillLengthReqd) + ns;
			return ns;
		}
		
		public String toString(){
			StringBuilder s = new StringBuilder(name);
			s.append(getString(min, "  #####0.00"));
			s.append(getString(max, "  #####0.00"));
			s.append(getString(getAverage(), "  #####0.00"));
			s.append(getString(getStdDev(), "  #####0.00"));
			s.append(getString(n, "  #####"));
			s.append(getString(owner.AP().getWeight(forceID), "  #####0.00"));
			return new String(s);
		}
		
	}
}
