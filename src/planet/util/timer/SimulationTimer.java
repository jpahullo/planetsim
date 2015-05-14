package planet.util.timer;

/**
 * Permits to inform to differents Timer of nodes the actual step
 * in simulation process. 
 * <br><br>
 * The final implementations must to have a constructor with no arguments.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 05/05/2004
 */
public interface SimulationTimer extends Timer {
	/**
	 * Informs to Timer that the current step is <b>step</b>.
	 * @param step Current step in simulation.
	 */
	public void currentStep(int step);
}
