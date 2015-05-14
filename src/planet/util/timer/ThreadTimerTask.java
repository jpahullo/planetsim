package planet.util.timer;

/**
 * This concrete implementation extends the java.util.TimerTask and adapt
 * this implementation to use the superclass. It is required to implement
 * TimerTask to use with ThreadTimer. 
 * 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 07/05/2004
 * @see planet.util.timer.ThreadTimer ThreadTimer
 */
public abstract class ThreadTimerTask extends java.util.TimerTask implements TimerTask {
	private boolean finished = false;
	
	/**
	 * Initialize this TimerTask. 
	 */
	public ThreadTimerTask() {
		super();
	}

	/**
	 * Cancels this TimerTask. If this task is in execution
	 * it normally finish.
	 * @see planet.util.timer.TimerTask#cancel()
	 * @return true if it avoid almost one execution of this task.
	 */
	public boolean cancel() {
		finished = true;
		return super.cancel();
	}
	
	/**
	 * Implements this method to make the concrete necessary job. 
	 * @see planet.util.timer.TimerTask#run()
	 */
	public abstract void run();
	
	/**
	 * Inform if this TimerTask has been cancelled.
	 * @see planet.util.timer.TimerTask#isFinished()
	 * @return true if this TimerTask has been cancelled.
	 */
	public boolean isFinished() {
		return finished;
	}
}
