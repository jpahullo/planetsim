package planet.util.timer;

/**
 * Implements a Timer based with java.util.Timer, with a non daemon
 * mode. 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 07/05/2004
 */
public class ThreadTimer extends java.util.Timer implements Timer {
	
	/**
	 * Builds a new ThreadTimer, invoking the java.util.Timer.Timer() 
	 * constructor.
	 */
	public ThreadTimer() {
		super();
	}

	/**
	 * Sets a new TimerTask to schedule for only one activation.
	 * If the TimerTask not extends
	 * java.util.TimerTask, this method will throw a ClassCastException
	 * at runtime.
	 * @see planet.util.timer.Timer#setTimerTask(planet.util.timer.TimerTask, long)
	 * @param task TimerTask that must extends java.util.TimerTask
	 * @param delay Time in millis for the first invocation.
	 */
	public void setTimerTask(TimerTask task, long delay) {
		super.schedule((java.util.TimerTask)task,delay);
		
	}

	/**
	 * Sets a new TimerTask to schedule periodicly. If the TimerTask not extends
	 * java.util.TimerTask, this method will throw a ClassCastException
	 * at runtime.
	 * @see planet.util.timer.Timer#setTimerTask(planet.util.timer.TimerTask, long, long)
	 * @param task TimerTask that must extends java.util.TimerTask
	 * @param delay Time in millis for the first invocation.
	 * @param period Time in millis between each activation of this TimerTask.
	 */
	public void setTimerTask(TimerTask task, long delay, long period) {
		super.schedule((java.util.TimerTask)task,delay,period);
		
	}
}
