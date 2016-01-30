/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package input;

/**
 * Interface for event queues. Any class that is not a movement model or a 
 * routing module but wishes to provide events for the simulation (like creating
 * messages) must implement this interface and register itself to the 
 * simulator. See the {@link EventQueueHandler} class for configuration 
 * instructions.
 * 
 * 事件队列接口；任何非移动模型及Routing的模型，但却希望为Simulation过程提供事件的类都应该
 * 实现这一接口，并将自己注册给Simulator
 */
public interface EventQueue {
	
	/**
	 * Returns the next event in the queue or ExternalEvent with time of 
	 * double.MAX_VALUE if there are no events left.
	 * @return The next event
	 */
	public ExternalEvent nextEvent();
	
	/**
	 * Returns next event's time or Double.MAX_VALUE if there are no 
	 * events left in the queue.
	 * @return Next event's time
	 */
	public double nextEventsTime();

}
