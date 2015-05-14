package planet.simulate;

import java.util.*;

import planet.commonapi.Id;

/**
 * @author Ruben Mondejar
 * @author Marc Sánchez <msa.ei@estudiants.urv.es>
 *  Node stress
 *  Link stress
 *  Average messages/queues (IN/OUT)
 *  Average total messages / time unit (t)
 *  Average hops/message (specific)
 *  Total number of messages
 */
public class Results {
    
    private static Hashtable results = new Hashtable();
    private static Hashtable lookups = new Hashtable();
    private static Hashtable inserts = new Hashtable();
	private static Hashtable hops = new Hashtable();
    private static int ringSize = 0;
    private static int stabRate = 0;
    private static int traffic = 0;	
 
      
    public static void incStabRate() {
      stabRate++;
    }
    
	public static void clearStabRate() {
	  stabRate=0;
	}
	
	public static int getStabRate() {
		return stabRate;
	}
    
	public static void incTraffic() {
      traffic++;
	}
    
	public static void decTraffic() {
	  traffic--;
	}
	
	public static void incTrafficBy(int inc) {
		traffic = traffic + inc;
	}
	
	public static int getTraffic() {
	  return traffic;
	}    
	
	
        
    //type --> in/out    
    public static void numMessagesTime(Id id, int step, int num, String type) {
      updateNodeStress(id,num,type);
	  addMaxMessages(id,step,num,type);	
    }
    
	private static void updateNodeStress(Id id, int num, String type){
      
	  Hashtable node_info;
	  Integer stress;
      
	  if (results.containsKey(id)) {
		node_info = (Hashtable) results.get(id);      
		if (node_info.containsKey("Stress "+type)) {
		  stress = (Integer) node_info.get("Stress "+type);
		  int value = stress.intValue();
		  value+=num;		  	 
		  node_info.put("Stress "+type,new Integer(value));
		  results.put(id,node_info);		  		  	  
		}
		else {		  		  	 
		  node_info.put("Stress "+type,new Integer(num));		  
		  results.put(id,node_info);		
		}				          	
	  }
	  else {		
		node_info = new Hashtable();	 
		node_info.put("Stress "+type,new Integer(num));
		results.put(id,node_info);		
	  }	  
	}
			
	private static void addMaxMessages(Id id, int step, int num ,String type){
      
	  Hashtable node_info;
	  Vector max;
      
	  if (results.containsKey(id)) {
		node_info = (Hashtable) results.get(id);      
		if (node_info.containsKey("Num max "+type)) {
		  max = (Vector) node_info.get("Num max "+type);
		  int value = ((Integer) max.get(1)).intValue();
		  if (num>value) {		 
		    max.clear();
			max.add(new Integer(step));
			max.add(new Integer(num));	 
			node_info.put("Num max "+type,max);
			results.put(id,node_info);
		  }		  	  
		}
		else {
		  max = new Vector();
		  max.add(new Integer(step));
		  max.add(new Integer(num));	 
		  node_info.put("Num max "+type,max);
		  results.put(id,node_info);		
		}				          	
	  }
	  else {		
		max = new Vector();
		max.add(new Integer(step));
		max.add(new Integer(num));
		
		node_info = new Hashtable();	 
		node_info.put("Num max "+type,max);
		results.put(id,node_info);		
	  }	  
	}
	
	public static void updateHopsMsg(Id source, String key_msg){      
          
      String identif = source+" "+key_msg;
      
	  if (hops.containsKey(identif)) {
	    int num = ((Integer) hops.get(identif)).intValue();
		hops.put(identif,new Integer(num++));		  		  		  	  
	  }
	  else {		  		  	 
	    hops.put(identif,new Integer(1));
	  }		  	  
	}
	
	public static void addInsert(String key,String dest) {
		inserts.put(key,dest);
	}
	
	public static String getInsert(String key) {
		return (String) inserts.get(key);
	}

	public static void resetInserts() {
		inserts.clear();
	}
	
	public static Hashtable getAllInserts() {
		return (Hashtable) inserts.clone();
	}
	
	public static void addLookup(Id key, Id own) {
	  lookups.put(key,own);
    }
	
	public static Id getLookup(Id key) {	  
	  return ((Id) lookups.get(key));
    }
    
    public static void resetLookups(){
      lookups.clear();
    }
    
    public static Hashtable getAllLookups() {
      return (Hashtable) lookups.clone();
    }
    
	public static void print () {
	  long total_in_msg = 0;
	  long total_out_msg = 0;
	 
	  Logger.log("************************",Logger.PRINT_LOG);
	  Logger.log(" **   Node Results    **",Logger.PRINT_LOG);
	  Logger.log("************************",Logger.PRINT_LOG);
      Collection c = results.keySet();
      ringSize = c.size();
      Iterator it = c.iterator();
      while (it.hasNext()) {
      	Id id = (Id) it.next();
      	Logger.log(" *** Results of node "+id,Logger.PRINT_LOG);
		Hashtable node_info = (Hashtable) results.get(id);
		int link_stress = ((Integer) node_info.get("Stress in")).intValue();
		Logger.log(" * Link stress :"+link_stress,Logger.PRINT_LOG);		
		
		int node_stress = link_stress + ((Integer) node_info.get("Stress out")).intValue();      	
		Logger.log(" * Node stress :"+node_stress,Logger.PRINT_LOG);		
		
		Vector max_in = (Vector) node_info.get("Num max in");
		Logger.log(" * Max number in messages :"+max_in.get(1)+" at step "+max_in.get(0),Logger.PRINT_LOG);		    
		
		Vector max_out = (Vector) node_info.get("Num max out");
		Logger.log(" * Max number out messages :"+max_out.get(1)+" at step "+max_out.get(0),Logger.PRINT_LOG);		
			    
		total_in_msg+=link_stress;
		total_out_msg+=node_stress-link_stress;
      }
      
      long total_n_msg = total_in_msg + total_out_msg;
      int steps = Logger.getStep();	
	  Logger.log("***********************",Logger.PRINT_LOG);
	  Logger.log(" ** Globals Results  **",Logger.PRINT_LOG);
	  Logger.log("***********************",Logger.PRINT_LOG);
	  Logger.log(" * Ring size "+ringSize,Logger.PRINT_LOG);
	  Logger.log(" * Total number of message     "+total_n_msg,Logger.PRINT_LOG);
	  if (ringSize>0) {	  
	  	Logger.log(" * Average messages/in queues  "+total_in_msg/ringSize,Logger.PRINT_LOG); 
	    Logger.log(" * Average messages/out queues "+total_out_msg/ringSize,Logger.PRINT_LOG);      
	    Logger.log(" * Average messages/nodes      "+total_n_msg/ringSize,Logger.PRINT_LOG);
	  }
	  if (steps>0) Logger.log(" * Average messages/steps      "+total_n_msg/steps,Logger.PRINT_LOG);
	  Logger.log(" * Stabilize Global Rate "+stabRate,Logger.PRINT_LOG);
    }
}
