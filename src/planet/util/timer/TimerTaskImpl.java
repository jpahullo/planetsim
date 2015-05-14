package planet.util.timer;

/**
 * This abstract implementation is only usefull for simulation environment.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 07/05/2004
 */
public class TimerTaskImpl implements TimerTask {
	/**
	 * Inform if this TimerTask has been executed.
	 */
	private boolean executed = false;
	/**
	 * Inform if it has been cancelled.
	 */
	private boolean finished = false;
	/**
	 * Inform if it is a periodic Timertask.
	 */
	private boolean periodic = false;

	/**
	 * Initialize this TimerTask.
	 * @param periodic If this task is periodic.
	 */
	public TimerTaskImpl(boolean periodic) {
		this.periodic = periodic;
	}
	
	/**
	 * Cancells the actual TimerTask. Returs true always if
	 * it prevents any activation of this TimerTask.
	 * @see planet.util.timer.TimerTask#cancel()
	 * @return true if and only if it prevents one or more
	 * activations of this TimerTask. false in other case.
	 */
	public boolean cancel() {
		this.finished = true;
		return (this.periodic || (!this.periodic && !this.executed));
	}

	/**
	 * Implement the job to do for activation of this TimerTask.
	 * The concrete implementations must include this line:
	 * <pre>
	 *    super.run();
	 * </pre>
	 * to inform when this task has been executed. 
	 * @see planet.util.timer.TimerTask#run()
	 * 
	 */
	public void run(){
		executed = true;
	}
	
	/**
	 * Inform if this TimerTask has been cancelled.
	 * @see planet.util.timer.TimerTask#isFinished()
	 * @return true if this TimerTask has been cancelled.
	 */
	public boolean isFinished() {
		return finished;
	}
}
