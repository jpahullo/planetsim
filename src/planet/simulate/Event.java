/*
 * Created on 01-dic-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package planet.simulate;

import planet.commonapi.Id;

/**
 * @author Pedro García
 * @author Carles Pairot
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Event implements IEvent, java.io.Serializable {
  private Id from;
  private Id to;
  private int type;
  private int times;
  private int time;


  public Event (Id from, Id to,int type, int times) {
    this.from = from;
    this.to = to;
    this.type = type;
    this.times = times;
  }

  public Event (Id from, Id to,int type, int times, int time) {
    this.from = from;
    this.to = to;
    this.type = type;
    this.times = times;
    this.time = time;
  }

  /* (non-Javadoc)
   * @see planet.IEvent#getFrom()
   */
  public Id getFrom() {
    return from;
  }

  /* (non-Javadoc)
   * @see planet.IEvent#getTo()
   */
  public Id getTo() {
    return to;
  }

  /* (non-Javadoc)
   * @see planet.IEvent#getType()
   */
  public int getType() {
    return type;
  }

  /* (non-Javadoc)
   * @see planet.IEvent#getTimes()
   */
  public int getTimes() {
    return times;
  }

  public int getTime() {
    return time;
  }
  
  public String toString() {
  	return "From: "+this.from+"; To:"+this.to+"; Type:"+Globals.typeToString(this.type)+"; Times:"+this.times+"; Time:"+this.time+"\n";
  }
}
