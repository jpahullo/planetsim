package planet.simulate;

import planet.chord.*;
import planet.commonapi.Id;
 
/**
 * @author Pedro García
 * @author Carles Pairot
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JoinGenerator implements IEventGenerator { 

  /* (non-Javadoc)
   * @see planet.IEventGenerator#generate()
   */
  public IEvent generate() {
    Id id = new ChordId().setValues(ChordId.ONE_CHORD);
    Id nodeId = new ChordId().setValues(ChordId.ONE_CHORD);

    Event evt = new Event (id,nodeId,Globals.JOIN,1);
    return evt;
  }
}
