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

package game2dai.fsm;

import game2dai.World;
import game2dai.entities.BaseEntity;

/**
 * This is the base class for all 'finite states'. <br<
 * 
 * It is recommended that child classes have no instance variables
 * in which case they can be implemented as singleton classes. Since
 * entities are changing state frequently this will enable entities to
 * share a state object and reduce object creation and garbage 
 * collection. <br>
 * 
 * 
 * @author Peter Lager
 *
 */
public abstract class State {


	public String name = "";
	
	//this will execute when the state is entered
	public abstract void enter(BaseEntity user);

	//this is the state's normal update function
	public abstract void execute(BaseEntity user, double deltaTime, World world);

	//this will execute when the state is exited.
	public abstract void exit(BaseEntity user);

	//this executes if the agent receives a message from the message dispatcher
	public abstract boolean onMessage(BaseEntity user, Telegram tgram);

}
