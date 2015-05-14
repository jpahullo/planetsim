package planet.generic.commonapi.behaviours;

import planet.commonapi.behaviours.BehavioursPattern;

/**
 *	This class stores a pattern for incoming messages on a node. This pattern is used to
 *  dispatch a behaviour <b>classOf</b> whenever the pattern matches the type and mode of 
 *  the incoming message. To allow behaviours to execute randomly on time, every pattern
 *  has a probability.
 *  @author <a href="mailto: marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
 *  Date: 10/10/2004   
 */
public class BehavioursPatternImpl implements BehavioursPattern {
    
    /* **************************** GLOBAL PATTERN ATTRIBUTES *****************/
	/**
	 * Complementary wildcard: given a set of patterns P, this wildcard is used 
	 * to attach a behaviour to the patterns on the whole pattern space S not
	 * included in P set. 
	 */
	public static final String COMPLEMENTARY_WILDCARD = "?";
	/**
	 * Universal wildcard: given a set of patterns P, this wildcard is used 
	 * to attach a behaviour to all the patterns on the whole pattern space 
	 * S included P set, i.e. P C S. 
	 */
	public static final String UNIVERSAL_WILDCARD = "*";
	/*
	 * The number of possible tokens: Complementary wildcard "?", Universal
	 * wildcard "*" and tag token.
	 */
	private static final int MAX_TOKENS = 3;
	/**
	 * Overlay property: Local refers to a behaviour which must run only
	 * when RouteMessage's pattern is for him.
	 */
	public static final String RUN_LOCAL   = "LOCAL";
	/**
	 * Overlay property: Remote refers to a behaviour which must run only
	 * when RouteMessage's pattern is for another peer rather than him.
	 */
	public static final String RUN_REMOTE  = "REMOTE";
	/**
	 * Overlay property: Always refers to a behvaiour which must run always
	 * despite RouteMessage's destination. 
	 */
	public static final String RUN_ALWAYS  = "ALWAYS";
	/**
	 * Overlay property: Good refers to a behaviour which must run when node
	 * behaves as underlying kbr protocol stipulates.
	 */
	public static final String ROLE_GOOD    = "GOOD";
	/**
	 * Overlay property: Bad refers to a behvaiour which must run when node
	 * misbehaves, including routing and overlay invariant's maintenance.
	 */
	public static final String ROLE_BAD     = "BAD";
	/**
	 * Overlay property: Neutral refers to a behvaiour which must run always
	 * despite node's role is bad or good.
	 */
	public static final String ROLE_NEUTRAL = "NEUTRAL";
    
    
    /* **************************** ATTRIBUTES PER PATTERN INSTANCE ***********/
    /**
     * Maps a pattern to an integer.
     */
	private int map;
	/**
	 * Chance for behaviour <b>classOf</b> to get scheduled.  
	 */
	private double pdf;
	/**
	 * Pattern Matching: Type's field of incoming messages.
	 */
	private String typeOf;
	/**
	 * Pattern Matching: Mode's field of incoming messages.
	 */
	private String modeOf;
	/**
	 * Instances of the class Class represent classes and interfaces in a running Java application.
	 */
	private Class  classOf;
	/**
	 * Specifies when to execute some behaviour.
	 */
	private String whenTo;
	/**
	 * Specifies the role of a behaviour.
	 */
	private String roleOf;
	 
	/**
     * Builds a non initialized BehavioursPattern. Requires the setValues(...)
     * method invokation.
	 */
	public BehavioursPatternImpl(){}

    /**
     * Sets the initial values for this pattern. 
     * @param classOf Behaviours class to be invoked when this pattern is matched.
     * @param typeOf RouteMessage type.
     * @param modeOf RouteMessage mode.
     * @param pdf The probability for a successfully invokation of the related
     * behaviour.
     * @param whenTo Determines the locality of the behaviour (local, remote or
     * both situations).
     * @param roleOf Determines the active role to invoke the related behaviour (good, bad or both)
     * @return The same instance once it has been updated.
     */
    public BehavioursPattern setValues (Class classOf, String typeOf, String modeOf, double pdf, String whenTo, String roleOf) {
		this.classOf = classOf;
		this.typeOf = typeOf;
		this.modeOf = modeOf;
		this.pdf = pdf;
		this.whenTo = whenTo;
		this.roleOf = roleOf;
		this.map = map(typeOf)* MAX_TOKENS + map(modeOf);
        return this;
	}
	/**
	 * @return Returns the class of behaviour.
	 */
	public Class getClassOf() {
		return classOf;
	}
	/**
	 * @return Returns the mode of pattern.
	 */
	public String getModeOf() {
		return modeOf;
	}
	/**
	 * @return Returns the probability binding.
	 */
	public double getPdf() {
		return pdf;
	}
	/**
	 * @return Returns the type of the pattern.
	 */
	public String getTypeOf() {
		return typeOf;
	}
	/**
	 * @return Returns when to exec this behaviour.
	 */
	public String getWhenTo() {
		return this.whenTo;
	}
	/**
	 * @return Returns the role of this behaviour.
	 */
	public String getRoleOf() {
		return this.roleOf;
	}
	/**
	 * @return Returns an integer mapping for this pattern.
	 */
	public int getMap() {
		return map;
	}
	/**
	 * Maps a property to specific quantifier. So Complementary wildcard <b>"?"</b> is more 
	 * specific than universal wildcard <b>"*"</b>. 
	 * @param property The property to quantify.
	 * @return The quantifier of the property. 
	 */
	public int map(String  property) {
		int map = -MAX_TOKENS; 
		if (property.equals(COMPLEMENTARY_WILDCARD)) map = 1;
		else if (property.equals(UNIVERSAL_WILDCARD)) map = 2;
		return map;  
	}
	/**
	 * Compares two patterns based on specific criteria.
     * <PRE>
	 *   Specific criteria:
	 * 		   Type  Mode	
	 * 		1) Tag,  Tag
	 * 		2) Tag,   ?
	 * 		3) Tag,   *
	 * 		4)  ? ,  Tag
	 *      5)  * ,  Tag
	 *      6)  ? ,   *
	 *      7)  * ,   ?
	 *      8)  * ,   *
     * </PRE>
	 * @param pattern The pattern to compare.
	 * @return the value 0 if the argument pattern is equal to this pattern; 
	 * 	a value less than 0 if this pattern is less specific than the pattern argument; 
	 *  and a value greater than 0 if the pattern is more specific than the pattern argument.
	 */
	public int compareTo(BehavioursPatternImpl pattern) {
		if (pattern.getMap() == this.map) return 0;
		else if (pattern.getMap() < this.map) return -1;
		return 1;
	}
	
    /**
     * Shows the string representation of this pattern.
     * @return The string representation of this pattern.
     * @see java.lang.Object#toString()
     */
	public String toString() {
		return "Behaviour_________________________\n\n\t" + classOf.getName() 
		+ "\n\tPattern [[ " + typeOf + " AND " + modeOf + "]]" 
		+ "\n\tPdf     " + pdf
		+ "\n\tWhen	   " + whenTo
		+ "\n\tRole	   " + roleOf
		+ "\n\tMap     " + map
		+ "\n__________________________________";
	}
}
