package planet.generic.commonapi.results;

import planet.commonapi.exception.InitializationException;
import planet.util.PropertiesInitializer;
import planet.util.PropertiesWrapper;

/**
 * Default graphical properties for GML graphs.
 * @author <a href="mailto: marc.sanchez@estudiants.urv.es">Marc Sanchez </a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol </a>
 */
public class ResultsGMLProperties implements PropertiesInitializer {
    
    /* *********** GML GRAPH PROPERTIES **********/
    /** Width Of  The Virtual Bounding Box  */
    public static final String RESULTS_PROPERTIES_GML_WIDTH                     = "RESULTS_PROPERTIES_GML_WIDTH";
    /** Height Of The Virtual Bounding Box*/
    public static final String RESULTS_PROPERTIES_GML_HEIGHT                    = "RESULTS_PROPERTIES_GML_HEIGHT";
    /**  Shape Of The Node */  		
    public static final String RESULTS_PROPERTIES_GML_SHAPE                     = "RESULTS_PROPERTIES_GML_SHAPE";
    /** Fill Color For The Shape Of the Node  (in #'RRGGBB' format) */
    public static final String RESULTS_PROPERTIES_GML_FILL                      = "RESULTS_PROPERTIES_GML_FILL";
    /** Alternative Fill Color For the Shape Of The Node  (in #'RRGGBB' format) */
    public static final String RESULTS_PROPERTIES_GML_ALTERNATIVE_FILL          = "RESULTS_PROPERTIES_GML_ALTERNATIVE_FILL";
    /** Color Of The Border Line (in #'RRGGBB' format) */
    public static final String RESULTS_PROPERTIES_GML_OUTLINE                   = "RESULTS_PROPERTIES_GML_OUTLINE"; 
    /** Font Size Of the Node Id Label */
    public static final String RESULTS_PROPERTIES_GML_FONT_SIZE                 = "RESULTS_PROPERTIES_GML_FONT_SIZE";
    /** Font Name Of the Node Id Label */
    public static final String RESULTS_PROPERTIES_GML_FONT_NAME                 = "RESULTS_PROPERTIES_GML_FONT_NAME";
    /** Minimal Node Distance arranged on a circle */
    public static final String RESULTS_PROPERTIES_GML_MINIMAL_NODE_DISTANCE     = "RESULTS_PROPERTIES_GML_MINIMAL_NODE_DISTANCE";
    
    /* ********************** DEFAULT ATTRIBUTE VALUES ******************/
    /** Default value: Width Of  The Virtual Bounding Box. */
  	public static final float WIDTH = 20.0f;
  	
  	/** Default value:  Height Of The Virtual Bounding Box */
  	public static final float HEIGHT = 20.0f;
  	
  	/** Default value:  Shape Of The Node */  		
  	public static final String SHAPE = "ellipse";
  	
  	/** Default value:  Fill Color For The Shape Of the Node */
  	public static final String FILL = "#CCCCFF";
  	
  	/** Default value:  Alternative Fill Color For the Shape Of The Node */
  	public static final String ALTERNATIVE_FILL=  "#00FF66";

  	/** Default value:  Color Of The Border Line */
  	public static final String OUTLINE ="#000000"; 
  	
  	/** Default value:  Font Size Of the Node Id Label */
  	public static final int FONT_SIZE = 12;
  	
  	/** Default value:  Font Name Of the Node Id Label */
  	public static final String FONT_NAME = "dialog";
  	
  	/** Default value:  Minimal Node Distance arranged on a circle */
  	public static final int MINIMAL_NODE_DISTANCE = 50;
    
    
  	/* *********** ACTUAL GML GRAPH ATTRIBUTES **********/
	/** Width Of  The Virtual Bounding Box. */
  	public float width = 20.0f;
  	
  	/** Height Of The Virtual Bounding Box */
  	public float height = 20.0f;
  	
  	/** Shape Of The Node */  		
  	public String shape = "ellipse";
  	
  	/** Fill Color For The Shape Of the Node */
  	public String fill = "#CCCCFF";
  	
  	/** Alternative Fill Color For the Shape Of The Node */
  	public String alternativeFill =  "#00FF66";

  	/** Color Of The Border Line */
  	public String outline ="#000000"; 
  	
  	/** Font Size Of the Node Id Label */
  	public int fontSize = 12;
  	
  	/** Font Name Of the Node Id Label */
  	public String fontName = "dialog";
  	
  	/** Minimal Node Distance arranged on a circle */
  	public int minimalNodeDistance = 50;
  	
  	
  	/**
	 * Loads the default values specified in the <b>properties</b>
	 * for GML purposes. If don't appear any of required attribute, it is
     * set the default value.
	 * @throws InitializationException if an error occurs during  the initialization
	 * of the different properties.
	 * @param properties Properties with the current configuration.
	 */
	public void init(PropertiesWrapper properties) throws InitializationException {
        //loading current configuration
        if (properties.containsKey(RESULTS_PROPERTIES_GML_WIDTH))
            width = (float)properties.getPropertyAsDouble(RESULTS_PROPERTIES_GML_WIDTH);
        else 
            width = WIDTH; //the default value
        
        if (properties.containsKey(RESULTS_PROPERTIES_GML_HEIGHT))
            height = (float)properties.getPropertyAsDouble(RESULTS_PROPERTIES_GML_HEIGHT);
        else 
            height = HEIGHT; //the default value

        if (properties.containsKey(RESULTS_PROPERTIES_GML_SHAPE))
            shape = properties.getProperty(RESULTS_PROPERTIES_GML_SHAPE);
        else 
            shape = SHAPE; //the default value

        if (properties.containsKey(RESULTS_PROPERTIES_GML_FILL))
            fill = "#"+properties.getProperty(RESULTS_PROPERTIES_GML_FILL);
        else 
            fill = FILL; //the default value

        if (properties.containsKey(RESULTS_PROPERTIES_GML_OUTLINE))
            outline = "#"+properties.getProperty(RESULTS_PROPERTIES_GML_OUTLINE);
        else 
            outline = OUTLINE; //the default value
		
        if (properties.containsKey(RESULTS_PROPERTIES_GML_FONT_SIZE))
            fontSize = properties.getPropertyAsInt(RESULTS_PROPERTIES_GML_FONT_SIZE);
        else 
            fontSize = FONT_SIZE; //the default value

        if (properties.containsKey(RESULTS_PROPERTIES_GML_FONT_NAME))
            fontName = properties.getProperty(RESULTS_PROPERTIES_GML_FONT_NAME);
        else 
            fontName = FONT_NAME; //the default value
        
        if (properties.containsKey(RESULTS_PROPERTIES_GML_MINIMAL_NODE_DISTANCE))
            minimalNodeDistance = properties.getPropertyAsInt(RESULTS_PROPERTIES_GML_MINIMAL_NODE_DISTANCE);
        else 
            minimalNodeDistance = MINIMAL_NODE_DISTANCE; //the default value
	}
    
    /**
     * Makes the postinitialization process. Does nothing.
     * @throws InitializationException if an error occurs during
     * the initialization of the different properties.
     * @param properties A Properties instance with all required configuration properties.
     * @see planet.util.PropertiesInitializer#postinit(planet.util.PropertiesWrapper)
     */
    public void postinit(PropertiesWrapper properties) throws InitializationException
    {
        //does nothing
    }
}
