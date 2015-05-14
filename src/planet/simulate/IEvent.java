

package planet.simulate;

import planet.commonapi.Id;

/**
 * @author Pedro Garc�a
 * @author Carles Pairot
 */
public interface IEvent {

	public Id getFrom();
	public Id getTo();
	public int getType();
	public int getTimes();
	public int getTime();

}
