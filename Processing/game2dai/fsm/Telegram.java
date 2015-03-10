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

import game2dai.maths.FastMath;


public class Telegram implements Comparable<Telegram>{

	private static final long SAME_TIME_INTERVAL = 250;
	
	//the entity that sent this telegram
	public final int sender;

	//the entity that is to receive this telegram
	public final int receiver;

	//the message itself.
	public final int msg;

	//messages can be dispatched immediately or delayed for a specified amount
	//of time. If a delay is necessary this field is stamped with the time 
	//the message should be dispatched.
	public final long despatchAt;

	//any additional information that may accompany the message
	public Object[] extraInfo;


	protected Telegram(){
		despatchAt = -1;
		sender = -1;
		receiver = -1;
		msg = -1;
	}


	public Telegram(long despatchAt, int sender, int receiver, int msg, Object... info){ 
		this.despatchAt = despatchAt;
		this.sender = sender;
		this.receiver = receiver;
		this.msg = msg;
		if(info != null)
			extraInfo = info;
		else
			extraInfo = new Object[0];
	}

	/**
	 * Telegrams are consider the same if they are to be despatched within 250ms
	 * as defined in SAME_TIME_INTERVAL
	 */
	@Override
	public int compareTo(Telegram tgram) {
		if(sender == tgram.sender 
				&& receiver == tgram.receiver 
				&& msg == tgram.msg
				&& FastMath.abs(despatchAt - tgram.despatchAt) < SAME_TIME_INTERVAL){
			return 0;
		}
		return (despatchAt <= tgram.despatchAt) ? -1 : 1;
	}

}
