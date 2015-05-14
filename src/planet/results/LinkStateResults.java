package planet.results;

import java.util.Collection;
import java.util.Hashtable;

import planet.commonapi.RouteMessage;
import planet.commonapi.Id;

/**
 *  LinkStateResults is a class which shows how many incoming messages
 *  has been dropped by nodes and how many has been retransmitted towards 
 *  its destination. It showns the expected number of hops for whatever
 *  message to reach its destination. Also classifies the messages according
 *  to type and mode of <b>RouteMessageImpl</b>.
 *    
 *  @author Marc Sanchez	   <marc.sanchez@estudiants.urv.es>
 *  @version 1.0
 */
public class LinkStateResults {
	/**
	 * LinkStateResults property: stores how many hops per RouteMessage's 
	 * type and mode.
	 */
	protected static Hashtable hopsByTypeOf = new Hashtable();
	protected static Hashtable flowOfMessages = new Hashtable();
	/**
	 * Resets the LinkState results; 
	 */	
	public static void resetLinkState() {
		hopsByTypeOf = new Hashtable();
		flowOfMessages = new Hashtable();
	}
	/**
	 * Updates the number of hops per RouteMessage's type only.
	 * @param msg The RouteMessage taken as input.
	 */
	public static void updateHopsByTypeOnly(RouteMessage msg) {
		String typeOf = planet.simulate.Globals.typeToString(msg.getType());
		if (hopsByTypeOf.containsKey(typeOf)) {
			PatternStatus status = (PatternStatus) hopsByTypeOf.get(typeOf);
			status.updateHops();		  		  		  	  
		} else hopsByTypeOf.put(typeOf, new PatternStatus(typeOf));
	}
	/**
	 * Updates the number of hops per RouteMessage's type and mode.
	 * @param msg The RouteMessage taken as input.
	 */
	public static void updateHopsByTypeOf(RouteMessage msg){      
		String typeOf = planet.simulate.Globals.typeToString(msg.getType());
		String modeOf = planet.simulate.Globals.modeToString(msg.getMode());
      
		String patternBy = typeOf + " and " + modeOf;
 	    
		if (hopsByTypeOf.containsKey(patternBy)) {
			PatternStatus status = (PatternStatus) hopsByTypeOf.get(patternBy);
			status.updateHops();		  		  		  	  
		}  else	hopsByTypeOf.put(patternBy, new PatternStatus(typeOf, modeOf));
	}
	/**
	 * Stores a new RouteMessage has built up in order to be sent in a few steps.
	 * @param msg The RouteMessage taken as input.
	 */
	public static void newMessageOnlyByType(RouteMessage msg) {
		String typeOf = planet.simulate.Globals.typeToString(msg.getType());
		if (hopsByTypeOf.containsKey(typeOf)) {
			PatternStatus status = (PatternStatus) hopsByTypeOf.get(typeOf);
			status.updateMessages();		  		  		  	  
		}  else	hopsByTypeOf.put(typeOf, new PatternStatus(typeOf));
	}
	/**
	 * Stores a new RouteMessage has built up in order to be sent in a few steps.
	 * @param msg The RouteMessage taken as input.
	 */
	public static void newMessage(RouteMessage msg) {
		String typeOf = planet.simulate.Globals.typeToString(msg.getType());
		String modeOf = planet.simulate.Globals.modeToString(msg.getMode());
      
		String patternBy = typeOf + " and " + modeOf;
		if (hopsByTypeOf.containsKey(patternBy)) {
			PatternStatus status = (PatternStatus) hopsByTypeOf.get(patternBy);
			status.updateMessages();		  		  		  	  
		}  else	hopsByTypeOf.put(patternBy, new PatternStatus(typeOf, modeOf));
	}
	/**
	 * Stores the number of incoming messages received by the node.
	 * @param node Node to overhear.
	 */
	public static void updateIncoming(Id node) {
		if (flowOfMessages.containsKey(node)) {
			NodeFlowStatus status = (NodeFlowStatus) flowOfMessages.get(node);
			status.updateIncoming();		  		  		  	  
		}  else	{
			NodeFlowStatus status = new NodeFlowStatus(node);
			flowOfMessages.put(node, status);
		}
	}
	/**
	 * Stores the number of outcoming messages sent by the node.
	 * @param node Node to overhear.
	 */
	public static void updateOutcoming(Id node) {
		if (flowOfMessages.containsKey(node)) {
			NodeFlowStatus status = (NodeFlowStatus) flowOfMessages.get(node);
			status.updateOutcoming();		  		  		  	  
		}  else	{
			NodeFlowStatus status = new NodeFlowStatus(node);
			status.updateOutcoming();
			flowOfMessages.put(node, status);
		}
	}
	/**
	 * Stores the number of messages dropped by the node.
	 * @param node Node to overhear.
	 */
	public static void updateDropped(Id node) {
		if (flowOfMessages.containsKey(node)) {
			NodeFlowStatus status = (NodeFlowStatus) flowOfMessages.get(node);
			status.updateDropped();		  		  		  	  
		}  else	{
			NodeFlowStatus status = new NodeFlowStatus(node);
			status.updateDropped();
			flowOfMessages.put(node, status);
		}
	}
	/**
	 * Gets the average of hops for the Message Performative specified by "<b> typeOf </b>" and "<b> modeOf </b>".
	 * @param typeOf Type of the Message.
	 * @param modeOf Mode of the Message.
	 * @return Returns the average measured in hops for the current message performative.
	 * @throws NoSuchMessagePerformative Whenever the Message Performative does not match
	 * any Message.
	 */
	public static double getMeanByTypeOf(String typeOf, String modeOf) throws NoSuchMessagePerformative {
		String patternBy = typeOf + " and " + modeOf;
		
		if (hopsByTypeOf.containsKey(patternBy)) {
			PatternStatus status = (PatternStatus) hopsByTypeOf.get(patternBy);
			double mean = (double) status.getHopsPerType() / (double) status.getMessagesPerType();
			return mean;
		}
		throw new  NoSuchMessagePerformative();
	}
	/**
	 * Gets the average of hops for the Message Performative specified only by the type. 
	 * @param typeOf Type of the Message.
	 * @return Returns the average measured in hops for the current message performative.
	 * @throws NoSuchMessagePerformative Whenever the Message Performative does not match
	 * any Message.
	 */
	public static double getMeanByTypeOnly(String typeOf) throws NoSuchMessagePerformative {
		if (hopsByTypeOf.containsKey(typeOf)) {
			PatternStatus status = (PatternStatus) hopsByTypeOf.get(typeOf);
			double mean = (double) status.getHopsPerType() / (double) status.getMessagesPerType();
			return mean;
		}
		throw new NoSuchMessagePerformative();
	}
	/**
	 * Gets the PatternStatus of the Message Performative specified by "<b>TypeOf</b>" and "<b>ModeOf</b>".
	 * @param typeOf Type of the Message.
	 * @param modeOf Mode of the Message.
	 * @return Return the statistics mapped to this Message Performative.
	 * @see planet.results.PatternStatus for more details.
	 * @throws NoSuchMessagePerformative Whenever the Message Performative does not match any Message.
	 */
	public static PatternStatus getStatisticsByTypeOf(String typeOf, String modeOf) throws NoSuchMessagePerformative {
		String patternBy = typeOf + " and " + modeOf;
		if (hopsByTypeOf.containsKey(patternBy)) return (PatternStatus) hopsByTypeOf.get(patternBy);
		else throw new NoSuchMessagePerformative();
		
	}
	/**
	 * Gets the PatternStatus of the Message Performative specified only by the type.
	 * @param typeOf Type of the Message.
	 * @return Return the statistics mapped to this Message Performative.
	 * @throws NoSuchMessagePerformative Whenever the Message Performative does not match any Message.
	 */
	public static PatternStatus getStatisticsByTypeOnly(String typeOf) throws NoSuchMessagePerformative {
		if (hopsByTypeOf.containsKey(typeOf)) return (PatternStatus) hopsByTypeOf.get(typeOf);
		else throw new NoSuchMessagePerformative(); 
	}
	/**
	 * Prints Link State Results per RouteMessage's type and node.
	 */
	public static void printByType() {
		System.out.println("\nprintByType____________________________________________________________________________________\n");
		Collection c = hopsByTypeOf.values();
		java.util.Iterator it = c.iterator();
		java.text.DecimalFormat formatter = new java.text.DecimalFormat("########");
		System.out.println("Pattern                                  Hops       Messages   Mean\n");
		while(it.hasNext()) {
			PatternStatus status = (PatternStatus) it.next();
			String pattern = status.getType() + " and " + status.getMode();
			while (pattern.length() < 40) pattern += ' ';
			String hops = formatter.format(status.getHopsPerType());
			while (hops.length() < 10) hops += ' ';
			String messages = formatter.format(status.getMessagesPerType());
			while (messages.length() < 10) messages += ' ';
			formatter.applyPattern("#####.####");
			double mean = (double) status.getHopsPerType() / (double) status.getMessagesPerType();
			System.out.println(pattern + " " +  hops + " " + messages + " " + formatter.format(mean)); 
		}
		System.out.println("\n_______________________________________________________________________________________________");
	}
	/**
	* Prints Link State Results per Node's flow.
	*/
	public static void printByNode() {
		System.out.println("\nprintByNode____________________________________________________________________________________\n");
		Collection c = flowOfMessages.values();
		java.util.Iterator it = c.iterator();
		java.text.DecimalFormat formatter = new java.text.DecimalFormat("########");
		System.out.println("Id                                       Incoming   Outcoming  Dropped\n");
		while(it.hasNext()) {
			NodeFlowStatus status = (NodeFlowStatus) it.next();
			String id = status.getId().toString();
			while (id.length() < 40) id += ' ';
			String incoming = formatter.format(status.getIncoming());
			while (incoming.length() < 10) incoming += ' ';
			String outcoming = formatter.format(status.getOutcoming());
			while (outcoming.length() < 10) outcoming += ' ';
			String dropped = formatter.format(status.getDropped());
			while (dropped.length() < 10) dropped += ' ';
			System.out.println(id + " " + incoming + " " + outcoming + " " + dropped); 
		}
		System.out.println("\n_______________________________________________________________________________________________");
	}
}

