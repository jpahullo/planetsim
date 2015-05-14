package planet.simulate;

import planet.util.OrderedList;
import java.util.*;

/**
 * This class implements the interface IScheduler and manage
 * events to produce to simulated network.
 * @author <a href="mailto: pedro.garcia@urv.net">Pedro Garcia</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 14-jul-2005
 */
/**
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 14-jul-2005
 */
public class Scheduler implements IScheduler, java.io.Serializable {

    /**
     * The specified internal value for the attribute <b>nextAlarm</b> to
     * show that there are no more alerts (events).
     */
    private static final int NO_MORE_ALERTS = -1;
    /**
     * Shows the time step for the next alarm (event).
     */
	private int nextAlarm;
    /**
     * Ordered list of alarms (events).
     */
	private OrderedList alarms = null;
	

    /**
     * Initializes this Scheduler.
     */
	public Scheduler(){
        nextAlarm = NO_MORE_ALERTS;
	}
	
    /**
     * Adds all the Event instances within <b>events</b> into the current
     * scheduler.
     * @param events Unordered list of events.
     * @see planet.simulate.IScheduler#addEvents(java.util.Vector)
     */
	public void addEvents(Vector events) {
        if (events == null || events.size()==0) return;
        
        Iterator it = events.iterator();

        Vector list = null;
        Event e = null;
        int time = 0;

        //adds the first element into the list.
        if (alarms == null)
        {
            list = new Vector();
            e = (Event) it.next();
            list.add(e);
            alarms = new OrderedList(list,e.getTime());
        }
        
        //adds the rest
        while (it.hasNext())
        {
			e = (Event) it.next();
			time = e.getTime();
			
			if (!alarms.contains(time)){
				list = new Vector();
				list.add(e);
				alarms.insert(new OrderedList(list,time));
			}else {
				list = (Vector)alarms.get(time);
				list.add(e);
			}
		}
		nextAlarm = alarms.value();
	}

    /**
     * Gets all alerts occurred at <b>time</b> time.
     * @param time Number of step which obtain all its alerts.
     * @return All alerts occurred at <b>time</b> time.
     * @see planet.simulate.IScheduler#getEvents(int)
     */
	public Vector getEvents(int time){
		Vector events = new Vector();
		Iterator it;
		
		if (nextAlarm==time)
        {
			Vector evts = (Vector)alarms.get(time);
			events.addAll(evts);
			alarms = alarms.pop();
			
            if (alarms == null)
                nextAlarm = NO_MORE_ALERTS;
            else
                nextAlarm = alarms.value();
		}
		return events;
	}
	
    /**
     * Shows if there are more alerts.
     * @return true if there are more alerts.
     * @see planet.simulate.IScheduler#hasNext()
     */
	public boolean hasNext() {
	  return nextAlarm != NO_MORE_ALERTS;
	}

    /**
     * Shows the string representation of the current scheduler.
     * @return The string representation of the current scheduler.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "Scheduler: nextAlarm = "+nextAlarm+", alarms = "+alarms;
    }
}
