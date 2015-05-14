
package planet.simulate;

import planet.commonapi.exception.InitializationException;

/**
 * @author Pedro García 
 */
public interface ISimulator {
	
	public void run(int steps) throws InitializationException;
	public void stop();
	

}
