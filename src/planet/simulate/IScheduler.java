
package planet.simulate;

import java.util.Vector;

/**
 * @author Pedro García 
 */
public interface IScheduler {
	
	public void addEvents(Vector events);	
	public Vector getEvents(int time);
	public boolean hasNext();
}
