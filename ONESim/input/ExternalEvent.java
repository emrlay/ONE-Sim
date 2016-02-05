/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package input;

import java.io.Serializable;

import core.World;

/**
 * Super class for all external events. All new classes of external events
 * must extend this class. This can also be used as a dummy event if only
 * an update request (and no further actions) to all hosts is needed.
 * 外部事件的超类，所有外部事件都必须继承此类，它可以作为一个dummy事件来使所有host进行更新操作
 */
public class ExternalEvent implements Comparable<ExternalEvent>, Serializable {
	/** Time of the event (simulated seconds) */
	protected double time;
	
	public ExternalEvent(double time) {
		this.time = time;
	}
	
	/**
	 * Processes the external event.
	 * 处理事件的方法，所有继承ExternalEvent的子类都需要重写这一方法以实现自己的事件逻辑
	 * @param world World where the actors of the event are
	 */
	public void processEvent(World world) {
		// this is just a dummy event
	}

	/**
	 * Returns the time when this event should happen.
	 * 获取事件发生的时间
	 * @return Event's time
	 */
	public double getTime() {
		return this.time;
	}
	
	/**
	 * Compares two external events by their time.
	 * @return -1, zero, 1 if this event happens before, at the same time, 
	 * or after the other event
	 * @param other The other external event
	 */
	public int compareTo(ExternalEvent other) {
		if (this.time == other.time) { 
			return 0;
		}
		else if (this.time < other.time) {
			return -1;
		}
		else {
			return 1;
		}
	}
	
	/**
	 * Returns a String representation of the event
	 * @return a String representation of the event
	 */
	public String toString() {
		return "ExtEvent @ " + this.time;
	}
	
}
