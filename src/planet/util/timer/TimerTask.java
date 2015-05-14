package planet.util.timer;

import java.io.Serializable;

/**
 * This interface abstract the responsability of a Timer,
 * who invokes the <b>timeout()</b> method when the 
 * specified time is passed.
 * 
 * @author Jordi Pujol
 */
public interface TimerTask extends Serializable {
	
	/**
	 * It avoids that this TimerTask executes more times. If the
	 * execution is in process, it finish normally. 
	 * This method returns true if it prevents one or more 
	 * scheduled executions from taking place.
	 */
	public boolean cancel();
	
	/**
	 * Job to do when the period of time specified by
	 * this TimerTask is passed.
	 */
	public void run();
	
	/**
	 * Inform that this TimerTask is just cancelled.
	 * @return true if this timer has been finished. false in other case.
	 */
	public boolean isFinished();
}
