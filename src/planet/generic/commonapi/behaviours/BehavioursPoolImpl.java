package planet.generic.commonapi.behaviours;

import java.util.Iterator;
import java.util.Vector;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.commonapi.behaviours.BehavioursFilter;
import planet.commonapi.behaviours.BehavioursInvoker;
import planet.commonapi.behaviours.BehavioursPool;
import planet.commonapi.behaviours.exception.NoBehaviourDispatchedException;
import planet.commonapi.behaviours.exception.NoSuchBehaviourException;
import planet.commonapi.behaviours.exception.OutOfRangeError;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;
import planet.util.Properties;

/**
 * The BehavioursPool's class is aimed at providing an internal  scheduler of
 * node's behaviuours. At startup, behaviours are registered to the pool from 
 * Behaviour.properties file, following the next syntax:
 * <br><pre>    
 *      Class = Type, Mode, Probability, When, Role 
 * </pre>
 * Then, a message interceptor is build up and is ready to invoke  behaviours
 * when a message pattern matches to those provided by <b> Behaviour.properties </b>
 * file.  
 * @author <a href="mailto: marc.sanchez@urv.net">Marc Sanchez</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 10/10/2004   
 */
public class BehavioursPoolImpl implements BehavioursPool {
	/**
	 * BehavioursPool properties: mapping from <b>patterns</b> to <b>behaviours</b>.
     * <ul>
     * <li>Patterns are composed by: Type and Mode, for example: 
	 * 					"QUERY_JOIN" and "REQUEST" related to the available
     *     RouteMessage types and modes. </li>
	 * <li>Types: The available RouteMessage types. Correct range in: 0..NumberOfTypes. </li>
     * <li>Modes: The available RouteMessage mode. Correct range in: 0..NumberOfModes. </li>
     * </ul>
	 * 	By reflection, the behaviours pool given a set of patterns P do:
     * <pre>
	 *  	For each pattern on P do
	 *  		Mode = pattern.getMode();
	 *  		Type = pattern.getType();
	 *  		beh[Type * NumberOfModes + Mode] = pattern; 
	 *      done;
	 *  </pre>
	 * Moreover, Pattern's definiton provides additional specifiers:
	 * <ul>
     * <li><b>Incoming RouteMessage's destination</b>: Local, Remote  and
	 *  	Always. Local qualifier affects iff incoming messages for
	 *      the local node. Hence, only behaviours for the local node
	 *      may be matched. Remote qualifier affects iff messages for
	 *      remote nodes. Hence, programmer must code behaviours which 
	 *      alter the underlying overlay protocol when RouteMessages
	 *      are destined to remote nodes. Always modifier is used as
	 *      a wild card to ignore local or remote behaviour. </li>
	 * <li><b>Behaviours's role</b>: Bad, Good and Neutral. Bad qualifier
	 *      alters only the behaviours from bad nodes. Good qualifier
	 *      alters only the behaviours from good nodes and Neutral is
	 *      a wild card used to ignore peer's behaviour.</li>
     * </ul>
	 **/
	protected java.util.Vector[][] beh;
	/**
	 * Behaviour's Property: Good modifier for Behaviour's role. This means the behaviour
	 * qualified by this modifier only will be scheduled by good peers.
	 */
	protected static final int ROLE_GOOD       = 0;
	/**
	 * Behaviour's Property: Bad modifier for Behaviour's role. This means the behaviour
	 * qualified by this modifier only will be scheduled by bad peers.
	 */
	protected static final int ROLE_BAD        = 1;
	/**
	 * Behaviour's Property: Local modifier applied to a behaviour means it only will
	 * be scheduled when a RouteMessage has as destination the local node.
	 */
	protected static final int TRAFFIC_LOCAL   = 0;
	/**
	 * Behaviour's Property: Remote modifier applied to a behaviour means it only will
	 * be scheduled when a RouteMessage has as destination a remote node and needs to
	 * be rerouted trough the overlay again.
	 */
	protected static int TRAFFIC_REMOTE  = 1;
	/**
	 * Filter instance.
	 */
	protected BehavioursFilter filter;
	/**
	 * Number of behaviour's slots: numberTypes * numberModes;
	 */
	protected int behSlots;
    /** 
     * Auxiliar Vector to use in the onMessage() method.
     */
    protected Vector behaviours = null;
    /**
     * Auxiliar Iterator to use in the onMessage() method.
     */
    protected Iterator behIt    = null;
    
	/**
	 * Behaviour's Property: Always modifier applied to a behaviour ignores the
	 * distinction between local or remote traffic handled by a node.
	 */
	protected static final int TRAFFIC_ALWAYS_MASK = 0x2;
	/**
	 * Behaviour's Property: Neutral modifier applied to a behaviour ignores the
	 * distinction between local or remote traffic handled by a node.
	 */
	protected static final int ROLE_NEUTRAL_MASK = 0x1;
	/**
	 * Behaviour's Property: Lack Of Neutral and Always Mask. 
	 */
	protected static final int NULL_MASK = 0x0;
	/**
	 * Total number of Behaviour's Queue: 
     * <ol type="1" start="0">
     * <li>Good and Local  Behaviours</li>
     * <li>Good and Remote Behaviours</li>
     * <li>Bad  and Local  Behaviours</li>
     * <li>Bad  and Remote Behaviours</li>
     * </ol>
	 */
	protected static final int BEH_MAPPINGS = 4;
	
	/**
     * Name of whole behaviours queue.
	 */
	private static final String Queues[] = { 
			"Good and Local  ", 
			"Good and Remote ", 
			"Bad  and Local  ", 
			"Bad  and Remote " }; 
	
    /**
     * Specific properties for these behaviours.
     */
    private BehavioursPropertiesImpl props = null;
    
    /**
     * Builds a non initialized pool. Requires the setValues(...) method
     * invokation.
     */
	public BehavioursPoolImpl() throws InitializationException {
       filter = GenericFactory.buildBehavioursFilter();
       behSlots =  Properties.behavioursNumberOfTypes *
                   Properties.behavioursNumberOfModes;
       props = (BehavioursPropertiesImpl)Properties.behavioursPropertiesInstance;
       build(props.patterns);
       if (props.debug) prettyPrintAll();
    }
    
	/**
	 * The WhichQueue's method selects for a given pattern what Queues must
	 * include a copy of the current behaviour.
	 * @param Mask The Mask for Pattern.Always and Pattern.Neutral modifiers.
	 * @param RoleOf The Behaviour's Role for a <b>Good</b> or <b>Bad</b> peer.
	 * @param WhenTo Specifies Behaviour should be activated when incoming messages 
	 * refer to the local node or either when refer to a remote.
	 * @return Which Behaviour's Queue must copy this behaviour.
	 */
	protected boolean[] whichQueues(int Mask, int RoleOf, int WhenTo) {
		boolean[] which = { false, false, false, false };
		if (Mask == NULL_MASK) which[RoleOf * BEH_MAPPINGS / 2 + WhenTo] = true;
		else if ((Mask & (ROLE_NEUTRAL_MASK|TRAFFIC_ALWAYS_MASK)) == (ROLE_NEUTRAL_MASK|TRAFFIC_ALWAYS_MASK))
			which = new boolean[] { true, true, true, true };
		else if ((Mask & ROLE_NEUTRAL_MASK) == ROLE_NEUTRAL_MASK)  {
			which[ROLE_GOOD * BEH_MAPPINGS / 2 + WhenTo] = true;
			which[ROLE_BAD  * BEH_MAPPINGS / 2 + WhenTo] = true;
		} else if ((Mask & TRAFFIC_ALWAYS_MASK) == TRAFFIC_ALWAYS_MASK) {
			which[RoleOf * BEH_MAPPINGS / 2 + TRAFFIC_LOCAL]  = true;
			which[RoleOf * BEH_MAPPINGS / 2 + TRAFFIC_REMOTE] = true;
		}
		return which;
	}
	/**
	 * Given the patterns sorted from more-to-less specific as input,
	 * builds a mapping from RouteMessage's patterns to behaviours.
	 * @param patterns The List of patterns.
	 */
	protected void build(Vector patterns) throws InitializationException {
		beh = new Vector[BEH_MAPPINGS][behSlots];
		for (int behQueue = 0; behQueue  < BEH_MAPPINGS; behQueue++)
			for(int slot = 0; slot < behSlots; slot++)
				beh[behQueue][slot] = new Vector();
		
		// Flags	
		int Flags;
		// Role Modifier
		int role = 0;
		// When Modifier
		int when = 0;
		
		while(!patterns.isEmpty()) {
			BehavioursPatternImpl pattern = (BehavioursPatternImpl) patterns.remove(0);
			Flags = NULL_MASK;

			// Role
			String  roleOf  = pattern.getRoleOf();
			if (roleOf.equals(BehavioursPatternImpl.ROLE_GOOD)) { 
				role = ROLE_GOOD;
			} else if(roleOf.equals(BehavioursPatternImpl.ROLE_BAD)) {
				role = ROLE_BAD;
			} else Flags = Flags | ROLE_NEUTRAL_MASK;

			// When
			String  whenTo  = pattern.getWhenTo();
			if (whenTo.equals(BehavioursPatternImpl.RUN_LOCAL)) {
				when = TRAFFIC_LOCAL;
			} else if(whenTo.equals(BehavioursPatternImpl.RUN_REMOTE)) {
				when = TRAFFIC_REMOTE;
			} else Flags = Flags | TRAFFIC_ALWAYS_MASK;
			
			String  typeOf = pattern.getTypeOf();
			String  modeOf = pattern.getModeOf();
			boolean[] queues = whichQueues(Flags, role, when);

			
			for(int queue = 0; queue < BEH_MAPPINGS; queue++) {
				if (!queues[queue]) continue;
				// Type's is '?' wildcard
				if(typeOf.equals(BehavioursPatternImpl.COMPLEMENTARY_WILDCARD)) {
					for (int typePos = 0; typePos < behSlots; typePos = typePos + Properties.behavioursNumberOfModes) {
						boolean empty = true;
						for (int modePos = 0; modePos < Properties.behavioursNumberOfModes; modePos++)
							if (beh[queue][modePos + typePos].size() > 0) { 
								empty = false; 
								break;
							}
						if (empty) setPattern(queue, typePos, modeOf, pattern);
					}
					// 	Type's is '*' wildcard
				} else if(typeOf.equals(BehavioursPatternImpl.UNIVERSAL_WILDCARD)) {
					for (int pos = 0; pos < behSlots; pos = pos + Properties.behavioursNumberOfModes)
						setPattern(queue, pos, modeOf, pattern);
					// 	Type's tag
				} else {
					int typePos = 0;
					try {
						typePos = ((java.lang.reflect.Field) Properties.overlayNode.getField(typeOf)).getInt(null);
					} catch(Exception e) {
						throw new InitializationException("Node class '" + Properties.overlayNode.getName() + "' does " +
							"not provide constant '" + typeOf + "'", e);
					}
					if (inRangeOf(Properties.behavioursNumberOfTypes, typePos)) setPattern(queue, typePos * Properties.behavioursNumberOfModes, modeOf, pattern);
					else throw new InitializationException("Type '" + typeOf + "' constant not in range [" + 0 + ".." + Properties.behavioursNumberOfTypes + "]");
				}
			}
		}
	}
	/**
	 * Given the initial position for type on mapping <b>typePos</b> and <b>mode</b> as input, sets the 
	 * pattern on the mapping taking into account the grammar tree extracted from wildcards semantics.
	 * Hence, the maximum number of mappings goes from <b>typePos</b> to <b>typePos + NumberOfModes</b>.
	 * For example, suppose type is QUERY_JOIN and modes are REQUEST and REPLY the maximun number of
	 * mappings to set is 2;
	 * @param queue Identifies where to be queued the pattern.
	 * @param typePos Initial position on behaviours mapping array, i.e, [typePos..typePos + NumberOfModes]
	 * @param modeOf Mode of the pattern: Universal wildcard or '*', Complementary wildcard  or '?' and Tag.
	 * @param pattern Pattern to be treat.
	 */
	protected void setPattern (int queue, int typePos, String modeOf, BehavioursPatternImpl pattern) throws InitializationException {
		// Type's is '?' wildcard
		if(modeOf.equals(BehavioursPatternImpl.COMPLEMENTARY_WILDCARD)) {
			for(int pos = typePos; pos < (typePos + Properties.behavioursNumberOfModes); pos++) {
				if (beh[queue][pos].isEmpty()) beh[queue][pos].add(newInvoker(pattern));
			}
		// Type's is '*' wildcard
		} else if(modeOf.equals(BehavioursPatternImpl.UNIVERSAL_WILDCARD)) {
			for(int pos = typePos; pos < (typePos + Properties.behavioursNumberOfModes); pos++) 
					beh[queue][pos].add(newInvoker(pattern));
		// Type's tag
		} else {
			int modePos = 0;
			try {
				modePos = ((java.lang.reflect.Field) Properties.overlayNode.getField(modeOf)).getInt(null);
			} catch(Exception e) {
				throw new InitializationException("Node class [" + Properties.overlayNode.getName() + "] does " +
						"not provide this constant: '" + modeOf + "'", e);
			}
			if (inRangeOf(Properties.behavioursNumberOfModes, modePos)) beh[queue][typePos + modePos].add(newInvoker(pattern));
			else throw new InitializationException("Mode [" + modeOf + "] constant not in range [" + 0 + ".." + Properties.behavioursNumberOfModes + "]");
		}		
	}
	
	private BehavioursInvoker newInvoker(BehavioursPatternImpl pattern) throws InitializationException {
		Class behClass = pattern.getClassOf();
		try {
			return ((BehavioursInvokerImpl)GenericFactory.buildBehavioursInvoker()).setValues((Behaviour) GenericFactory.newInstance(pattern.getClassOf()), pattern.getPdf());
		} catch(Exception e) {
			throw new InitializationException("Initialization Error For Behaviour '" + behClass.getName() + "'.", e);
		}
	}
	
	protected void prettyPrintAll() {
		for (int queue = 0; queue < BEH_MAPPINGS; queue++) {
			System.out.println("\n____ " + Queues[queue] + "_______________________________________________\n");
			printMap(queue);
			System.out.println("\n____________________________________________________________________");
		}
	}

	protected void printMap(int queue) {
		for (int slot = 0; slot < behSlots; slot++) {
			java.util.Iterator it = beh[queue][slot].iterator();
			if (it.hasNext()) System.out.print("\n[" + slot + "]: ");
			while(it.hasNext()) {
				BehavioursInvokerImpl behInvoker = (BehavioursInvokerImpl) it.next();
				String behaviour = behInvoker.getName(); 
				System.out.print(behaviour.substring(behaviour.lastIndexOf('.') + 1));
				if (it.hasNext()) System.out.print(", ");
				else System.out.print("\n");
			}
		}
	}
	/**
	 * Given an integer expression and upper bound as input, inRangeOf checks wether 
	 * this integer literal belongs to [0..upperBound).
	 * @param upperBound The upper bound of the set range.
	 * @param toEval Integer literal to evaluate.
	 * @return True if integer <b>toEval</b> belongs to [0..upperBound].
	 */
	private boolean inRangeOf(int upperBound, int toEval) {
		return toEval >= 0 && toEval < upperBound;
	}
	/**
	 * Given a RouteMessage and a Node as input, onMessage's method intends to
	 * invoke some behaviours only if RouteMessage's type and mode fields matches
	 * some behaviour's pattern.  
	 * @param msg RoteMessage taken as input.
	 * @param node Node taken as input.
	 * @return Returns either an array of RouteMessages or null when no messages
	 * need to transmit this node.
	 * @throws NoSuchBehaviourException when no behaviour matches RouteMessage's
	 * pattern. 
	 */
	public void onMessage(RouteMessage msg, Node node) 
										throws NoSuchBehaviourException, NoBehaviourDispatchedException {
			// ______________Checking RouteMessage's Type and Mode Pattern______________________________
			if (!inRangeOf(Properties.behavioursNumberOfModes, msg.getMode())) throw new OutOfRangeError("Mode [" + msg.getMode() + "] of RouteMessage Out of Range [0.." + Properties.behavioursNumberOfModes + ") \n" + msg);
			if (!inRangeOf(Properties.behavioursNumberOfTypes, msg.getType())) throw new OutOfRangeError("Type [" + msg.getType() + "] of RouteMessage Out of Range [0.." + Properties.behavioursNumberOfTypes + ") \n" + msg);
			
			// ______________Filtering__________________________________________________________________
			boolean toReject = false;
			if (filter != null) { 
				toReject = filter.filter(msg, node);
			}
			if (toReject) return;
			
			// _____________Dispatch Behaviour__________________________________________________________
			int whichQueue = node.playsGoodRole()? ROLE_GOOD: ROLE_BAD; 
			whichQueue = whichQueue * BEH_MAPPINGS / 2 + (node.isLocalMessage(msg)? TRAFFIC_LOCAL: TRAFFIC_REMOTE);
			
			behaviours = beh[whichQueue][msg.getType() * Properties.behavioursNumberOfModes + msg.getMode()];

			if (behaviours.isEmpty()) throw new NoSuchBehaviourException();
			
			boolean anyExec = false;
//			behIt = behaviours.iterator();
            BehavioursInvokerImpl behInvoker;
            for (int i = 0; i < behaviours.size(); i++)
            {
//			while(behIt.hasNext()) {
				behInvoker = (BehavioursInvokerImpl) behaviours.get(i);//behIt.next();
				if (props.debug)
					System.out.println("Invoking [" + behInvoker.getName() + "] On Node [" + node.getId() + "] " +
						"For [" + planet.simulate.Globals.typeToString(msg.getType()) + "]" +
							"[" + planet.simulate.Globals.modeToString(msg.getMode()) + "]");
				anyExec |= behInvoker.invoke(msg, node);
			}
			if (!anyExec) throw new NoBehaviourDispatchedException();
	}
}
