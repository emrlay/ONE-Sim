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
 * 事件队列接口；任何非移动模型及Routing，但却希望为Simulation过程提供事件的类都应该
 * 实现这一接口，并将自己注册给Simulator
 * （移动模型和Routing在World中的update里被显式地调用）
 * 
 * 很显然，任何外部事件都是“有组织的”，可以被组织成某种“类别”的。
 * 比如模拟过程中消息的产生，它可以看成是一系列以“生成消息”为目的的事件组织成的“聚合体”。
 * 故所有试图在模拟过程中产生除节点移动和Routing以外的显式update事件的情况，都应该成为一个EventQueue
 */
public interface EventQueue {
	
	/**
	 * Returns the next event in the queue or ExternalEvent with time of 
	 * double.MAX_VALUE if there are no events left.
	 * 获取下一事件对象
	 * @return The next event
	 */
	public ExternalEvent nextEvent();
	
	/**
	 * Returns next event's time or Double.MAX_VALUE if there are no 
	 * events left in the queue.
	 * 获取下一事件的时间
	 * @return Next event's time
	 */
	public double nextEventsTime();

}
