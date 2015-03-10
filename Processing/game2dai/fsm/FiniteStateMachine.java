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
 * 
 * @author Peter Lager
 *
 */
public class FiniteStateMachine {

	protected BaseEntity owner;

	protected State currentState;
	protected State previousState;
	protected State globalState;

	/**
	 * Prevent use of default constructor
	 */
	@SuppressWarnings("unused")
	private FiniteStateMachine(){}

	/**
	 * A FSM belongs to an entity (it cannot be shared by other entities)
	 * @param owner
	 */
	public FiniteStateMachine(BaseEntity owner) {
		this(owner, null, null, null);
	}

	/**
	 * 
	 * @param owner
	 * @param currentState
	 * @param previousState
	 * @param globalState
	 */
	public FiniteStateMachine(BaseEntity owner, State currentState,
			State previousState, State globalState) {
		this.owner = owner;
		this.currentState = currentState;
		this.previousState = previousState;
		this.globalState = globalState;
	}

	/**
	 * Use this to set the current state. <br>
	 * This will leave the previous state value unchanged.
	 * @param s the state to use
	 */
	public void setCurrentState(State s){
		currentState = s;
	}

	/**
	 * Use this to set the global state. <br>
	 * @param s the state to use
	 */
	public void setGlobalState(State s){
		globalState = s;
	}

	/**
	 * Use this to set the previous state. <br>
	 * @param s the state to use
	 */
	public void setPreviousState(State s){
		previousState = s;
	}

	/**
	 * This method is called by the world update method and should not be caled directly.
	 * @param deltaTime
	 * @param world
	 */
	public void update(double deltaTime, World world){
		//if a global state exists, call its execute method, else do nothing
		if(globalState != null) globalState.execute(owner, deltaTime, world);

		//same for the current state
		if (currentState != null) currentState.execute(owner, deltaTime, world);
	}

	/**
	 * This method is called when a telegram for the owning entity is received. <br>
	 * It first passes it to the current state and if it is not processed by 
	 * that state it is passed to the global state.
	 * @param msg the telegram
	 * @return true if this method processed the telegram
	 */
	public boolean onMessage(Telegram msg){
		//first see if the current state is valid and that it can handle
		//the message
		if (currentState != null && currentState.onMessage(owner, msg))
			return true;

		//if not, and if a global state has been implemented, send 
		//the message to the global state
		if (globalState != null && globalState.onMessage(owner, msg))
			return true;

		return false;
	}

	/**
	 * Change the current state of the entity. <br>
	 * Before changing the entity's state, the current state will be saved as the 
	 * previous state. This method will also call the exit() method of the old 
	 * state and the enter() method of the new state.
	 * @param newState the new current state
	 */
	public void changeState(State newState){
		// K track of the previous state
		previousState = currentState;

		// Call the exit method of the existing state
		if(currentState != null)
			currentState.exit(owner);

		// *****************************************
		// Change the current state to the new state
		currentState = newState;
		// *****************************************

		// Now call the entry method of the new current state
		if(currentState != null)
			currentState.enter(owner);
	}

	/**
	 * Performs the same actions as the changeState() method but the new state
	 * will be the previous state. <br>
	 */
	public void revertToPreviousState(){
		changeState(previousState);
	}

	/**
	 * Sees if the global state is of the same type (class) as the parameter.
	 * @param state the state we want to compare with.
	 * @return true if it is the same class as the global state.
	 */
	public boolean isGlobalState(State state){
		if(globalState != null && globalState.getClass() == state.getClass())
			return true;
		else
			return false;
	}

	/**
	 * Sees if the previous state is of the same type (class) as the parameter.
	 * @param state the state we want to compare with.
	 * @return true if it is the same class as the previous state.
	 */
	public boolean isPreviousState(State state){
		if(previousState != null && previousState.getClass() == state.getClass())
			return true;
		else
			return false;
	}

	/**
	 * Sees if the current state is of the same type (class) as the parameter.
	 * @param state the state we want to compare with.
	 * @return true if it is the same class as the current state.
	 */
	public boolean isCurrentState(State state){
		if(currentState != null && currentState.getClass() == state.getClass())
			return true;
		else
			return false;
	}

	/**
	 * Use this method if using the singleton objects for each state. If in 
	 * doubt use the other is????State methods. 
	 * @param st the current state object
	 * @return true if the parameter is the same object as this
	 */
	public boolean isUsingState(State st){
		if(currentState != null && currentState == st)
			return true;
		else
			return false;
	}

	/**
	 * Get the current state
	 */
	public State getCurrentState(){
		return currentState;
	}

	/**
	 * Get the global state
	 */
	public State getGlobalState(){
		return globalState;
	}

	public State getPreviousState(){
		return previousState;
	}

	// Only ever used during debugging to grab the name of the current state
	public String getNameOfCurrentState(){
		return currentState.getClass().getSimpleName();
	}

}
