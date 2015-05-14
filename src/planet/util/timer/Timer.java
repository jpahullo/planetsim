package planet.util.timer;

/**
 * This interface permits to schedule differents tasks to specified delays, and
 * to make periodic any task, begining to the specified delay and rerun after
 * <b>period</b> time.
 * <br><br>
 * Both <b>delay</b> and <b>period</b> are longs. Differents implementations
 * will use this value as steps (in simulation case) or millis (in real
 * implementation case).
 * <br><br>
 * At last, also it permits to cancel all scheduled tasks.
 * <br><br>
 * The final implementations must to have a constructor with no arguments.
 * @author Jordi Pujol
 */
public interface Timer extends java.io.Serializable {
	
	/**
	 * Sets a TimerTask to execute only once, after <b>delay</b> time.
	 * The time will be  steps in simulation world, or millis in
	 * real world.
	 * @param task TimerTask implementation with the job to do.
	 * @param delay Time after which will be invoked <b>task.run()</b>
	 */
	public void setTimerTask(TimerTask task,long delay);
	
	/**
	 * Sets a TimerTask to execute only once, after <b>delay</b> time, and
	 * after each <b>period</b> time.
	 * The time will be  steps in simulation world, or millis in
	 * real world.
	 * @param task TimerTask implementation with the job to do.
	 * @param delay Timer after which will be invoked <b>task.run()</b>
	 * @param period The period of time for waiting after first invocation
	 * (and followings) to invoke another time <b>task.run()</b>.
	 */
	public void setTimerTask(TimerTask task,long delay,long period);
	
	/**
	 * Cancels all TimerTasks active actually. 
	 */
	public void cancel();
	
}
