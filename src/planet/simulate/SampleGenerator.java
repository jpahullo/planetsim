package planet.simulate;

import planet.chord.*;
import planet.commonapi.Id; 

/**
 * @author Pedro García
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SampleGenerator implements IEventGenerator {
  private Id id;
  private Id target;
  private int type; 
  private int times;

  public SampleGenerator (Id id, Id target, int type, int times) {
    this.id = id;
    this.target = target;
    this.type = type;
    this.times = times;
  }

  public SampleGenerator (String id, String target, int type, int times) {
    // !!!
    int[] arrayId = {Integer.parseInt (id), 0, 0, 0, 0};
    int[] arrayTarget = {Integer.parseInt (target), 0, 0, 0, 0};
    this.id = new ChordId().setValues(arrayId);
    this.target = new ChordId().setValues(arrayTarget);
    this.type = type;
    this.times = times;
  }

  /* (non-Javadoc)
   * @see planet.IEventGenerator#generate()
   */
  public IEvent generate() {
    Event evt = new Event (id,target,type,times);
    return evt;
  }
}
