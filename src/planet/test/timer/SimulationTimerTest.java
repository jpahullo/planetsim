package planet.test.timer;

import planet.util.timer.ControlTimer;
import planet.util.timer.SimulationTimer;
import planet.util.timer.SimulationTimerImpl;
import planet.util.timer.TimerTask;
import planet.util.timer.TimerTaskImpl;

/**
 * Test if the SimulationTimer runs correctly.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 05/05/2004
 */
public class SimulationTimerTest {
	
	private static final int MAX_TIMERS = 3;
	private static final int MAX_STEPS  = 100;
	
	/**
	 * Builds Timers and differents TimerTask with only one execution
	 * and periodic TimerTask. 
	 */
	public SimulationTimerTest() {
		System.out.println("BEGIN OF TIMER TEST\n");
		ControlTimer.init();
		ControlTimer.currentStep(0); //sets the current step to zero 
		
		// create Timers
		SimulationTimer[] timers = new SimulationTimer[MAX_TIMERS];
		for (int i = 0; i<timers.length;i++) {
			timers[i] = new SimulationTimerImpl();
		}
		
		// subscribe tasks of only one execution
		for (int i=0; i<timers.length; i++) {
			timers[i].setTimerTask( new TestTimerTask(""+i,false),(long)i+2);
		}
		TimerTask timerTask = new TestTimerTask("periodic TimerTask0",true);
		timers[0].setTimerTask(timerTask,3,3);
		timers[0].setTimerTask(new TestTimerTask("periodic TimerTask1",true),5,10);
		timers[0].setTimerTask(new TestTimerTask("periodic TimerTask2",true),15,5);
		
		//test all timers and ControlTimer
		for (int i =1; i< MAX_STEPS; i++) {
			if (i== MAX_STEPS -10)
				timerTask.cancel();
			//inform the actual step
			ControlTimer.currentStep(i);
				
		}
		System.out.println("\nEND OF TIMER TEST");
	}
	
	/**
	 * Make:
	 * <pre>
	 *    new SimulationTimerTest();
	 * </pre>
	 * @param args Nothing waiting.
	 */
	public static void main(String[] args) {
		new SimulationTimerTest();
	}
	
	/**
	 * Simple TimerTask that only prints the actual step and its name
	 * for each invocation of run() method. 
	 * 
	 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
	 * Date: 05/05/2004
	 */
	public class TestTimerTask extends TimerTaskImpl {
		/**
		 * Inform name of this TimerTask.
		 */
		private String name = null;
		
		/**
		 * Sets the name of this TimerTask.
		 * @param name New name of this TimerTask
		 * @param periodic true if this TimerTask is periodic
		 */
		public TestTimerTask(String name,boolean periodic) {
			super(periodic);
			this.name = name;
		}
		
		/**
		 * Prints the actual step and the name of this task.
		 * @see java.util.TimerTask#run()
		 */
		public void run() {
			super.run();
			if (!isFinished()) 
				System.out.println("["+ControlTimer.currentStep+"]: "+name);
		}
		
		
		/**
		 * Shows the name of the TimerTask.
		 * @see java.lang.Object#toString()
		 * @return The name of the TimerTask
		 */
		public String toString() {
			return name;
		}
	}
}
