package planet.util.timer;

import java.util.Iterator;
import java.util.Vector;

/**
 * This class has all its members statics to permit 
 * in a simulation envirionment controls
 * all timers and to inform them of the actual step of simulation.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 05/05/2004
 */
public class ControlTimer {
	/**
	 * Actual step of simulation.
	 */
	public static int currentStep = 0;
	/**
	 * To contain all timers.
	 */
	private static Vector timers = null;
	
	/**
	 * Initialize the ControlTimer to permit adding Timers.
	 * It must be invoked before its fully use.
	 */
	public static void init() {
		timers = new Vector();
	}
	
	/**
	 * Adds the specified timer to current timers.
	 * @param timer SimulationTimer to advise each time.
	 */
	public static void add(SimulationTimer timer) {
		timers.add(timer);
	}
	
	/**
	 * Iterate for all active Timers to inform the actual step.
	 * @param step Actual simulation step.
	 */
	public static void currentStep(int step) {
		//nothing does if this step is passed.
		if (step <= currentStep) return;
		currentStep = step;
		Iterator it = timers.iterator();
		while (it.hasNext()) {
			SimulationTimer t = (SimulationTimer)it.next();
			//System.out.println("["+currentStep+"]: ControlTimer.currentStep(): timerTasks:\n"+t+"\n\n");
			t.currentStep(step);
		}
	}
	
	/**
	 * Sets the current step.
	 * @param step Current step
	 */
	public static void setCurrentStep(int step) {
		currentStep = step;
	}
}
