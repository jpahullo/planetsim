package planet.generic.commonapi.behaviours;

import java.util.Random;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.commonapi.behaviours.BehavioursInvoker;

/**
 * This class is used to invoke a behaviour. It acts 
 * like a facade calling a behaviour only when several preconditions are met. 
 * Pre-conditions on this default implementation are probability-based.  
 * @author <a href="mailto: marc.sanchez@urv.net">Marc Sanchez</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 */
public class BehavioursInvokerImpl implements BehavioursInvoker {
	/**
	 * Behaviour object: this properties is a placeholder for a behaviour.
	 */
	protected Behaviour beh = null;
	/**
	 * Behaviour property: probability to be checked.
	 */
	protected double prob;
	/**
	 * Randomizer.
	 */
	private java.util.Random random = null;

	/**
     * Builds an uninitialized BehavioursInvoker. Requires to invoke
     * setValues(...) method.
	 */
	public BehavioursInvokerImpl(){}
    
    /**
     * Initializes this invoker with specified values. 
     * @param behaviour Behaviour to be invoked at any incomming RouteMessage
     * @param probability The probability to be checked at any invokation
     * of specified Behaviour.
     * @return This same instance once has been initialized.
     */
    public BehavioursInvoker setValues(Behaviour behaviour, double probability) {
		this.beh = behaviour;
		this.prob = probability;
        this.random = new Random();
        return this;
	}
    
	/**
	 * This method invokes a behaviour. 
	 * @param msg The RouteMessage recently arrived.
	 * @param node The Node which holds the behaviour.
	 * @return Returns a BehaviourInvokerStatus with replies from the
	 * behaviour's invocation and a boolean signaling if the invocation
	 * was finally made.
	 */
	public boolean invoke(RouteMessage msg, Node node) {
		if (random.nextDouble() <= prob)
        {
            beh.onMessage(msg,node);
            return true;
        }
		else 
            return false;
	}
	/**
	 * @return Returns the name of a behaviour.
	 */
	public String getName() {
		return beh.getName();
	}
	/**
	 * @return Returns a string representation of the behaviour. In general, the toString
	 * method returns a string that "textually represents" this behaviour. The result should 
	 * be a concise but informative representation that is easy for a person to read. 
	 */ 
	public String toString() {
		return "Behaviour [" + this.beh + "] " +
			   "PreCondition On [" + this.prob + "]"; 
	}
}
