package planet.generic.commonapi.factory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Random;

import planet.commonapi.Id;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.IdFactory;
import planet.util.Utilities;

/**
 * This Factory generate uniques Id from a material.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public class IdFactoryImpl implements IdFactory {
	/**
	 * Properties file name whose read properties.
	 */
	protected String propertiesFile = "";
	
	
	/* ********* PROPERTIES INTO THE CONFIGURATION FILE *************/
	
	/**
	 * The default Class for Ids specified in the properties file.
	 */
	protected static final String defaultIdClass = "DEFAULT_IDCLASS";
	/**
	 * The default topology of the target network specified in the properties file.
	 */
	protected static final String defaultTopology = "DEFAULT_TOPOLOGY";
	/**
	 * The default size key for the target network specified in the properties file.
	 */
	protected static final String defaultNetworkSize = "DEFAULT_SIZE";
	
	/* END ********* PROPERTIES INTO THE CONFIGURATION FILE *************/
	
	/* ************************* VALUES OF PROPERTIES ************************/
	/**
	 * Actual instance of Class for new Ids.
	 */
	protected Class idClass = null;
	/**
	 * Id.divide(long) method for the actual Id implementation.
	 */
	protected transient Method idDivideMethod = null;
	/**
	 * Topology of the actual target network. It is necessary to know how
	 * generate the new Ids of Nodes.
	 */
	protected String topology = null;
	/**
	 * Network size for the target network.
	 */
	protected int networkSize;
	
	/* END ************************* VALUES OF PROPERTIES ********************/
	
	/* *********************** ATTRIBUTES TO BUILD DISTRIBUTED ID ************/
	
	/**
	 * Id just generated. 
	 */
	protected Id actualValue = null;
	/**
	 * Actual value to add to the <b>actualValue</b> to obtain the next Id.
	 */
	protected Id chunkValue = null;
	/**
	 * Number of Ids generated actually. Its value is important for the
	 * Circular networks.
	 */
	protected int actualBuildsIds;
	
	/* END ******************* ATTRIBUTES TO BUILD DISTRIBUTED ID ************/
	
	/**
	 * Random generator for random Id's. 
	 */
	protected Random random = null;
	
	
	/**
	 * Builds the IdFactory. Does nothing. Requires the <b>setValues(...)</b> 
     * method invokation.
	 * @throws InitializationException if occurs some error during initialization.
	 */
	public IdFactoryImpl() throws InitializationException {}
    
    /**
     * Sets the specified initial values.
     * @param idClass Class reference for the current Id implementation.
     * @param topology Desired network topology.
     * @param networkSize Desired network size.
     * @return The same instance once it has been updated.
     * @throws InitializationException if any error occurs during the
     * initialization process.
     * @see planet.commonapi.factory.IdFactory#setValues(java.lang.Class, java.lang.String, int)
     */
    public IdFactory setValues(Class idClass,String topology,int networkSize) throws InitializationException
    {
		//get constructors for idClass
		this.idClass = idClass;
		
		try {
			Class types[]  = {int.class};
			idDivideMethod = idClass.getMethod("divide",types);
		} catch (Exception e) {
			throw new InitializationException("Cannot obtain divide(int) method for '"+ idClass.getName() +"'.",e);
		}
		
		//read defaultTopology of network
		this.topology = topology;
		if (!Topology.isValid(topology))
			throw new InitializationException("The topology '"+topology+"' is not a valid topology to build new Ids.");
		
		//initialize Random generator
		this.random = new Random();
		
		//read the defaultSize of network.
		this.networkSize = networkSize;
		if (this.networkSize < 0) {
			throw new InitializationException("The network size '"+networkSize+"' are invalid.");
		}
		
		initDistributedAttr();
        return this;
	}
	
	/**
	 * Initializes the protected attributes to permits the uniform
	 * distribution of Ids. 
	 * @throws InitializationException if an error occurs during
	 * their initialization.
	 */
	protected void initDistributedAttr() throws InitializationException {
		if (this.networkSize != 0) {
			//initialize actualValue and chunkValue to generate distributed Ids (for CIRCULARs networks)
		    
			//generate the chunkValue
			try {
			    Object params[]  = {new Integer(this.networkSize)};
				chunkValue = (Id)idDivideMethod.invoke(null,params); //is a statical method
			} catch (Exception e) {
				throw new InitializationException("The chunk value for Ids cannot be initialized.",e);
			}

            //verifying a correct value for the chunk
            if (chunkValue == null)
            {
                throw new InitializationException("The chunk value for Ids cannot be initialize (divide method returns null)");
            }
            
			//generate the actualValue (-chunkValue)
			try{
				actualValue = (Id) idClass.newInstance();
				actualValue = actualValue.subtract(chunkValue);
			} catch (Exception e) {
				throw new InitializationException("The initial value for the first Id cannot be initialized.",e);
			}
		}
		//sets the actual number of Ids generated (0)
		actualBuildsIds = 0;
	}
	
	
	/**
	 * Builds an Id with the actual configuration of network topology and size.
	 * Use the protected method buildRandomId() to build the Id if the
	 * specified topology is random.
	 * @return A new Id generated with the actual configuration.
	 */
	public Id buildId() throws InitializationException {
		Id toReturn = null;
		if (this.topology.equalsIgnoreCase(Topology.RANDOM)) {
			toReturn = buildRandomId();
		} else if (this.topology.equalsIgnoreCase(Topology.CIRCULAR)) {
			if (this.actualBuildsIds >= this.networkSize ) {
				throw new InitializationException("Cannot build a new instance of ["+ idClass.getName()
						+"]. The topology network is ["+ Topology.CIRCULAR +"] and just generated all possible Ids ["+
						this.networkSize+"].");
			}
			this.actualValue = this.actualValue.add(this.chunkValue);
			this.actualBuildsIds++;
			toReturn = actualValue;
		} else {
			throw new InitializationException("Cannot build a new Id below the actual network topology ["+
					this.topology +"].");
		}
		return toReturn;
	}
	
	/**
	 * Builds a random Id using the Random constructor of the target Id.
	 * @return A new Id built randomly.
	 * @throws InitializationException if an error occurs during the
	 * initialization of the Id.
	 */
	public Id buildRandomId() throws InitializationException {
	    try {
            return ((Id)idClass.newInstance()).setValues(random);
        } catch (Exception e) {
            throw new InitializationException("IdFactoryImpl.buildRandomId: cannot build a new random Id");
        }
	}
	
	/**
	 * Builds an Id with the double parameter as its internal value.
	 * @see planet.commonapi.factory.IdFactory#buildId(double)
	 * @param material double with the internal value for the new Id.
	 * @return New Id with the double internal value.
	 * @throws InitializationException if an error occurs during the
	 * initialization of the Id or if it is nonapplicable to the 
	 * target Id.
	 */
	public Id buildId(double material) throws InitializationException {
        try {
            return ((Id)idClass.newInstance()).setValues(material);
        } catch (Exception e) {
            throw new InitializationException("IdFactoryImpl: cannot build a new Id");
        }
	}
	
	/**
	 * Builds an Id with the int parameter as its internal value.
	 * @see planet.commonapi.factory.IdFactory#buildId(int)
	 * @param material Int with the internal value for the new Id.
	 * @return New Id with the int internal value.
	 * @throws InitializationException if an error occurs during the
	 * initialization of the Id or if it is nonapplicable to the 
	 * target Id.
	 */
	public Id buildId(int material) throws InitializationException {
        try {
            return ((Id)idClass.newInstance()).setValues(material);
        } catch (Exception e) {
            throw new InitializationException("IdFactoryImpl: cannot build a new Id");
        }
	}
	
	/**
	 * Generate an Id from material in byte[] format as its internal value.
	 * @param material Hash code previously generated.
	 * @see planet.commonapi.factory.IdFactory#buildId(byte[])
	 * @throws InitializationException if an error occurs during the
	 * initialization of the Id or if it is nonapplicable to the 
	 * target Id.
	 */
	public Id buildId(byte[] material) throws InitializationException {
        try {
            return ((Id)idClass.newInstance()).setValues(material);
        } catch (Exception e) {
            throw new InitializationException("IdFactoryImpl: cannot build a new Id");
        }
	}

	/**
	 * Generate an Id from material in int[] format, as its internal value.
	 * @param material Internal value of the new Id in int[] format.
	 * @see planet.commonapi.factory.IdFactory#buildId(int[])
	 * @throws InitializationException if an error occurs during the
	 * initialization of the Id or if it is nonapplicable to the 
	 * target Id.
	 */
	public Id buildId(int[] material) throws InitializationException {
        try {
            return ((Id)idClass.newInstance()).setValues(material);
        } catch (Exception e) {
            throw new InitializationException("IdFactoryImpl: cannot build a new Id");
        }
	}

	/**
	 * Generate an Id from a String with SHA-1 hash function.
	 * This method use the constructor of the implemented Id with 
	 * byte[] argument.
	 * <br><br>
	 * The implementation makes the following operation:
	 * <br>
	 * <center><b>Utilities.generateByteHash(string)</b></center>
	 * <br>
	 * to build a byte[] with its hash value.
	 * @param string String to apply default hash function (SHA-1) to 
	 * generate Id. 
	 * @see planet.commonapi.factory.IdFactory#buildKey(java.lang.String)
	 * @see planet.util.Utilities#generateByteHash(java.lang.String)
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable. 
	 */
	public Id buildKey(String string) throws InitializationException {
        try {
            return ((Id)idClass.newInstance()).setValues(Utilities.generateByteHash(string));
        } catch (Exception e) {
            throw new InitializationException("IdFactoryImpl: cannot build a new Id");
        }
	}
	
	/**
	 * Generate an Id from a String that contains its internal value.
	 * @param material Contains the internal value of Id in String format. 
	 * @see planet.commonapi.factory.IdFactory#buildId(java.lang.String)
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable. 
	 */
	public Id buildId(String material) throws InitializationException {
        try {
            return ((Id)idClass.newInstance()).setValues(material);
        } catch (Exception e) {
            throw new InitializationException("IdFactoryImpl: cannot build a new Id");
        }
    }
    
	/**
     * Builds a new Id from an arbitray string applying a one-way hashing algorithm,
	 * such as SHA-1 or MD5.
	 * @param material An arbitray string. 
	 * @param algorithm One-way hashing algorithm such as "SHA" or "MD5".	 
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable. 
	 */
	public Id buildId(String material, String algorithm) throws InitializationException {
        try {
            return ((Id)idClass.newInstance()).setValues(material,algorithm);
        } catch (Exception e) {
            throw new InitializationException("IdFactoryImpl: cannot build a new Id");
        }

	}
	
	/**
	 * Generate an Id from the BigInteger as its internal value.
	 * @param bigNumber BigInteger with the internal value of the new Id. 
	 * @see planet.commonapi.factory.IdFactory#buildId(java.math.BigInteger)
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable. 
	 */
	public Id buildId(BigInteger bigNumber) throws InitializationException {
        try {
            return ((Id)idClass.newInstance()).setValues(bigNumber);
        } catch (Exception e) {
            throw new InitializationException("IdFactoryImpl: cannot build a new Id");
        }
	}
	
	/**
	 * The Iterator instance returned permits to build as maximum <b>desiredNetworkSize</b>.
	 * This method goal is to obtain <b>desiredNetworkSize</b> equidistant Ids for a network.   
	 * @param desiredNetworkSize Number of nodes Id to obtain.
	 * @return An Iterator instance for getting all <b>desiredNetworkSize</b>. The 
	 * Iterator.remove() method of this instance is not implemented and always throws
	 * a NoSuchMethodError error. null if <b>desiredNetworkSize</b> is zero.
	 * @throws InitializationException if any error has ocurred during the initialization.
	 */
	public java.util.Iterator buildDistributedIds(int desiredNetworkSize) throws InitializationException
	{
	    if (desiredNetworkSize==0) return null;
	    
	    Id chunkValue = null;
	    Id initialValue = null;
	    // generate the chunkValue
		try {
		    Object params[]  = {new Integer(desiredNetworkSize)};
			chunkValue = (Id)idDivideMethod.invoke(null,params); //is a statical method
		} catch (Exception e) {
			throw new InitializationException("The chunk value for Ids cannot be initialized.",e);
		}
		//generate the actualValue (-chunkValue)
		try{
			initialValue = (Id) idClass.newInstance();
			initialValue = initialValue.subtract(chunkValue);
		} catch (Exception e) {
			throw new InitializationException("The initial value for the first Id cannot be initialized.",e);
		}
		//generate the Iterator
	    return new DistributedIdIterator(desiredNetworkSize, initialValue,chunkValue);
	}

	/**
	 * Is an Id iterator, for building up to <b>networkSize</b> Id. All Id are
	 * equidistant between two consecutive Id.
	 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
	 * 28/02/2005
	 */
	public class DistributedIdIterator implements java.util.Iterator, java.io.Serializable
	{
	    /** Number of Id to generate. */
	    private int networkSize;
	    /** Number of built Ids. */
	    private int actualBuiltIds;
	    /** Increment value between two consecutive Ids. */
	    private Id chunkValue;
	    /** Actual value of the Id. */
	    private Id actualValue;

	    /**
	     * Initialize this instance with the specified values. The first returned Id
	     * for the <b>next()</b> method will be:<br><br>
	     * <center><b>initialValue+chunkValue</b></center>
	     * @param networkSize Number of Id to build.
	     * @param initialValue Initial Id value.
	     * @param chunkValue Increment value between two consecutive Ids.
	     */
	    public DistributedIdIterator(int networkSize, Id initialValue, Id chunkValue)
	    {
	        this.networkSize = networkSize;
			this.actualValue = initialValue;
			this.chunkValue = chunkValue;
	    }
	    
        /**
         * Test if all Id have been built.
         * @return true if the number of built Ids is less than the initial <b>networkSize</b>.
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
           return actualBuiltIds < networkSize;
        }

        /**
         * The next Id or null if all <b>networkSize</b> Id has been generated.
         * @return The next Id or null if all ones has been generated.
         * @see java.util.Iterator#next()
         */
        public Object next() {
            Id toReturn = actualValue;
            if (actualBuiltIds >= networkSize ) {
				return null;
			}
			toReturn = actualValue.add(chunkValue); //builds a new instance of Id
			actualValue.setValue(toReturn);         //update local instance
			actualBuiltIds++;
			return toReturn;
        }

        /**
         * This method is not implemented and throws a NoSuchMethodError error always. 
         * @see java.util.Iterator#remove()
         * @throws NoSuchMethodError always.
         */
        public void remove() {
            throw new NoSuchMethodError("Method is not implemented");
        }
	    
	}
	
	/* ************************ METHODS FOR SERIALIZATION ****************************/
	/**
	 * Makes nothing special, only invokes to <b>stream.defaultWriteObject()</b>.
	 * @param stream Stream to save the actual instance.
	 * @throws IOException if any error has ocurred.
	 */
	private void writeObject(java.io.ObjectOutputStream stream)
    throws IOException {
		stream.defaultWriteObject();
	}
	
	/**
	 * Makes a first <b>stream.defaultReadObject()</b> invocation to uses
	 * the default read method. And then, builds the constructor for
	 * the loaded Node class.
	 * @param stream Stream to read the actual instance.
	 * @throws IOException if occur any error during 
	 * default read object, or if there are any error during
	 * building of Node constructor.  
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream stream)
    throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        try {
            Class types[] = {int.class};
            idDivideMethod                 = idClass.getMethod("divide",types);
        } catch (Exception e) {
            throw new IOException("Cannot obtain constructors for '"+ idClass.getName() +"'.");
        }
	}
}
