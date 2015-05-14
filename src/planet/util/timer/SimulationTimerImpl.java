package planet.util.timer;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Implements the contract that is specified by the SimulationTimer 
 * interface. This permits schedule differents task for only one node.
 * <br><br>
 * Is important to know that is in a simulation environment. For that
 * reason, all time is measured in steps.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 05/05/2004
 */
public class SimulationTimerImpl implements SimulationTimer {
	private TreeSet tasks = null;

	/**
	 * Initialize a Vector to contain all scheduled TimerTask.
	 */
	public SimulationTimerImpl() {
		this.tasks = new TreeSet();
		//inform for its own existence
		ControlTimer.add(this);
	}

	/**
	 * Informs to the Timer that the actual step is <b>step</b>
	 * and permits to advise to differents taks for their timeouts.
	 * @see planet.util.timer.SimulationTimer#currentStep(int)
	 * @param step
	 */
	public void currentStep(int step) {
		Iterator it = tasks.iterator();
		Vector toReinsert = new Vector();
		Vector toRemove   = new Vector();
		TaskEntry task = null;
		boolean toContinue = true;
		while (it.hasNext() && toContinue) {
			task = (TaskEntry)it.next();
			it.remove(); //always to remove
			toContinue = task.currentStep(step);
			//reinsert if task not finished
			if (!task.isFinished()) toReinsert.add(task);   
		}
		tasks.addAll(toReinsert); //reorder elements for next activation
	}
	
	/**
	 * Permits add a <b>task</b> for only one execution after
	 * <b>delay</b> steps.
	 * @see planet.util.timer.Timer#setTimerTask(planet.util.timer.TimerTask, long)
	 * @param task TimerTask with action to do after delay steps.
	 * @param delay Number of steps to wait for to schedule the task.
	 */
	public void setTimerTask(TimerTask task, long delay) {
		tasks.add(new TaskEntry((int)delay,0,task));
	}
	
	/**
	 * Permits add a <b>task</b> for repeatly execution, the first time
	 * after <b>delay</b> steps, and others after <b>period</b> steps. 
	 * @see planet.util.timer.Timer#setTimerTask(planet.util.timer.TimerTask, long, long)
	 * @param task TimerTask with action to do after delay steps, and
	 * after period steps.
	 * @param delay Number of steps to wait for to schedule the task.
	 * @param period Number of steps to wait for others scheduling of the task.
	 */
	public void setTimerTask(TimerTask task, long delay, long period) {
		//System.out.println("["+ControlTimer.currentStep+"]: Timer.add("+task+",delay="+delay+",period="+period+")\n\n");
		tasks.add(new TaskEntry((int)delay,(int)period,task));
	}
	/**
	 * Cancels all scheduled tasks. After this, this Timer is ready
	 * for new TimerTask schedulings.
	 * @see planet.util.timer.Timer#cancel()
	 */
	public void cancel() {
		tasks.clear();
	}
	
	/**
	 * Shows in String format the representation of this Timer.
	 * @see java.lang.Object#toString()
	 * @return A representation of this Timer in String format.
	 */
	public String toString() {
		return tasks.toString();
	}
	
	/**
	 * This class permits contains the necessary information to
	 * activate at correct steps the related TimerTask. If the period
	 * is zero, the TimerTask only once will be activated. If 
	 * someone attempts to invoke actualStep(int) more times, nothing
	 * does and ever returns true to be deleted.
	 * 
	 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
	 * Date: 05/05/2004
	 */
	private class TaskEntry implements Comparable {
		/**
		 * Number of steps for the future activations. If is zero, only
		 * once will be activaded.
		 */
		private int period;
		/**
		 * Absolute number of step to activate the TimerTask.
		 */
		private int nextActivation;
		/**
		 * TimerTask to schedule.
		 */
		private TimerTask task = null;
		/**
		 * Informs that this entry is finished.
		 */
		private boolean finished = false;
		
		/**
		 * It initializes the next activation of the task as follows:
		 * <pre>
		 *   nextActivation = delay + ControlTimer.actualStep
		 * </pre>
		 * If the period is 0 (zero), this entry only once will be activated.
		 * @param delay Number of steps to wait for the first activation.
		 * @param period Number of steps to wait between future activations.
		 * @param task
		 */
		public TaskEntry(int delay, int period, TimerTask task) {
			this.nextActivation = delay + ControlTimer.currentStep;
			this.period         = period;
			this.task           = task;
			this.finished      = false;
		}
		
		/**
		 * Informs to the entry of the actual step. If the entry
		 * is for only one execution and if is invoked more times, 
		 * nothing does. This method return true if and only if
		 * the activation step is less or equals to the current step.
		 * It implies that the inner TimerTask has been activated. 
		 * @param step Actual simulation step.
		 * @return true if and only if this entry has been activated.
		 * false if this entry just still has not been activated. 
		 */
		public boolean currentStep(int step) {
			if (isFinished())
				return true; //is activated
			if (this.nextActivation<=step) {
				task.run();
				this.nextActivation += period;
				if (period == 0) {
					//update finished flag to allow be removed
					this.finished = true;
				}
				return true; //is activated
			}
			return false; //is not activated
		}

		/**
		 * Compare its nextActivation step. If this value is zero,
		 * compares its String representation.
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 * @param o Object to be compared.
		 * @return Greater, equals or less than zero if this instance
		 * is greater equals or less than <b>o</b>.
		 */
		public int compareTo(Object o) {
			TaskEntry et = (TaskEntry)o;
			int toReturn = 0;
			if (this.nextActivation != et.getNextActivation()) {
				//return negative value if this instance has a smaller nextActivation
				toReturn =   this.nextActivation - et.getNextActivation();
			}
			if (toReturn == 0)
				//return a comparison for theirs String representation
				return this.toString().compareTo(et.toString());
			return toReturn;
		}
		
		/**
		 * Inform if the inner TimerTask is finished or if 
		 * this same TimerTask is cancelled. 
		 * If true, this TaskEntry may to be removed.
		 * In other case, this entry must be maintained.
		 * @return Returns the finished flag.
		 */
		public boolean isFinished() {
			return finished || task.isFinished();
		}
		/**
		 * Gets the absolute nextActivation step.
		 * @return Returns the absolute nextActivation step.
		 */
		public int getNextActivation() {
			return nextActivation;
		}
		/**
		 * Gets the period, the number of steps between future activations.
		 * @return Returns the period.
		 */
		public int getPeriod() {
			return period;
		}
		
		/**
		 * Overwrites the default implementation to make  
		 * the compareTo(Object) method consistent with the
		 * equals(Object) method. It returns:
		 * <pre>
		 *     return compareTo(obj)==0;
		 * </pre>
		 * @see java.lang.Object#equals(java.lang.Object)
		 * @param obj Object to be compared.
		 * @return True only and only if two instances are the same.
		 */
		public boolean equals(Object obj) {
			return compareTo(obj)==0;
		}
		
		
		/**
		 * Return the information of this entry in String format.
		 * @see java.lang.Object#toString()
		 * @return The information of this entry in String format.
		 */
		public String toString() {
			return "{"+task+" next["+nextActivation+"] period["+period+"] finished ["+finished+"]}";
		}
	}
}
