
package planet.simulate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import planet.commonapi.Id;
import planet.generic.commonapi.factory.GenericFactory;

/**
 * This class permits to parser the events file with a concrete text format.
 * This format is the following:
 * <pre>
 * at {stepNumber} JOIN {sourceNodeId} {destinationNodeId} {numberOfTimes}
 * at {stepNumber} {LEAVE|FAIL} {sourceNodeId} {numberOfTimes}
 * </pre>
 * as for example:
 * <pre>
 * at 3 JOIN 0 0 1
 * at 65 FAIL 6 1
 * at 24 LEAVE 4 1
 * </pre>
 * If the <b>{sourceNodeId}</b> field is exactly <B>ID</b> the parser
 * will randomly build a new Id.
 * <br><br>
 * This last implementation is generic and permits the use of any
 * implemented Id to load the specified values into the 
 * <b>{sourceNodeId}</b> and <b>{destinationNodeId}</b> fields.
 * @author <a href="mailto: pedro.garcia@urv.net">Pedro Garcia</a>
 * @author <a href="mailto: carles.pairot@urv.net">Carles Pairot</a>
 * @author <a href="mailto: ruben.mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 08-jul-2005
 */
public class EventParser {
    
    /**
     * Parse the specified <b>fileName</b>.
     * @param fileName Path to the file with the events to load.
     * @return A Vector with all loaded events.
     * @throws FileNotFoundException if the file is not found.
     * @throws IOException if occurs any problem loading the file.
     */
    public static Vector parseEvents(String fileName) throws FileNotFoundException, IOException {
        Vector events = new Vector();

        FileReader fis = new FileReader(fileName);
        BufferedReader in = new BufferedReader(fis);
        String line;

        while ((line = in.readLine())!=null){
            events.addAll(parseLine(line));
        }

        return events;
    }

    /**
     * Parse a loaded line from events file.
     * @param line The event to load in text format.
     * @return A Vector with all built events from the line.
     */
    private static Vector parseLine(String line) {
        StringTokenizer st = new StringTokenizer(line);
        String token;

        Vector evts = new Vector();

        //'at' token
        token = st.nextToken();
        //'{stepNumber}' field
        token = st.nextToken();
        int time = Integer.parseInt(token);
        //'{actionType}' field
        token = st.nextToken();
        int type = getType(token);

        switch (type) {
        case Globals.JOIN:  evts.addAll (parseJoin (st, type, time));
            break;
        case Globals.LEAVE: evts.addAll (parseLeaveFail (st, type, time));
            break;
        case Globals.FAIL:  evts.addAll (parseLeaveFail (st, type, time));
            break;
        }
        return evts;
    }

    /**
     * Parse a JOIN event.
     * @param st StringTokenizer with the rest of the line to process.
     * @param type The current int value for the JOIN event.
     * @param time Begining event step.
     * @return The vector with all JOIN events.
     */
    private static Vector parseJoin (StringTokenizer st,int type,int time) {
        try {
            Vector evts = new Vector();
            Id id,target;
        
            String tid = st.nextToken();
            tid = tid.toUpperCase();
            String ttarget = st.nextToken();
            ttarget = ttarget.toUpperCase();
            String token = st.nextToken();
            int times = Integer.parseInt(token);
            int cnt = 0;
            Random r = new Random();
            for (cnt = 0; cnt < times; cnt++){
              if (tid.equals("ID")) {        
                id = GenericFactory.buildRandomId();
              }
              else {
        		id = GenericFactory.buildId(tid);
              }
        
              if (ttarget.equals("ID"))
                target = null;
              else {
        		target  = GenericFactory.buildId(ttarget);
              }
              Event e = new Event (id,target,type,times,time);
              evts.add(e);
            }
        
            return evts;
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    /**
     * Parse a LEAVE or FAIL event.
     * @param st StringTokenizer with the rest of the line to process.
     * @param type The current int value for the LEAVE or FAIL event.
     * @param time Begining event step.
     * @return The vector with all LEAVE or FAIL events.
     */
    public static Vector parseLeaveFail(StringTokenizer st,int type,int time) {
        try {
            Vector evts = new Vector();
            Id id,target;
    
            String tid = st.nextToken();
            tid = tid.toUpperCase();
            String token = st.nextToken();
            int times = Integer.parseInt(token);
            int cnt = 0;
            for (cnt=0;cnt<times;cnt++){
                if (tid.equals("ID"))
                    id = null;
                else {        
                    id = GenericFactory.buildId(tid);
                }
    
                Event e = new Event(id,null,type,times,time);
                evts.add(e);
            }
            
            return evts;
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
            return null;
        }
    }

    /**
     * Gets the int value for the incomming <b>type</b>.
     * @param type Event type name.
     * @return The required int value for the <b>type</b> or <B>-1</b> if
     * the type is unknown.
     */
    private static int getType(String type) {
        type = type.toUpperCase();
        if (type.equals("JOIN")){
            return Globals.JOIN;
        } else if (type.equals("LEAVE")){
            return Globals.LEAVE;
        } else if (type.equals("FAIL")){
            return Globals.FAIL;
        }
        return -1;
    }
}
