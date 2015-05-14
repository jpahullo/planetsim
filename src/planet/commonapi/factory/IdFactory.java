package planet.commonapi.factory;

import planet.commonapi.Id;
import planet.commonapi.exception.InitializationException;

/**
 * This interface provides methods with a way of generating 
 * Ids, using the factory method design pattern.  
 * <br><br>
 * Any future implementation must contain the no argument constructor.
 * <br><br>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public interface IdFactory extends java.io.Serializable  {
	
    /**
     * Sets the specified initial values.
     * @param idClass Class reference for the current Id implementation.
     * @param topology Desired network topology.
     * @param networkSize Desired network size.
     * @return The same instance once it has been updated.
     * @throws InitializationException if any error occurs during the
     * initialization process.
     */
    public IdFactory setValues(Class idClass,String topology,int networkSize) throws InitializationException;
    
	/**
	 * Builds an Id with the actual configuration of network topology and size.
	 * @return A new Id generated with the actual configuration.
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable.
	 */
	public Id buildId() throws InitializationException;
	
	/**
	 * Builds a protocol-specific Id given the source data.
	 * @param material The material to use as double value.
	 * @return A new Id with the double value as internal one.
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable.
	 */
	public Id buildId(int material) throws InitializationException;

	
	/**
	 * Builds a protocol-specific Id given the source data.
	 * @param material The material to use as double value.
	 * @return A new Id with the double value as internal one.
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable.
	 */
	public Id buildId(double material) throws InitializationException;

	
	/**
	 * Builds a protocol-specific Id given the source data.
	 *
	 * @param material The material to use to build the new Id.
	 * @return A new Id with the byte[] value.
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable.
	 */
	public Id buildId(byte[] material) throws InitializationException;

	/**
	 * Builds a protocol-specific Id given the source data as int[].
	 * @param material The internal representation value of the new Id
	 * as int[].
	 * @return A new Id with the int[] value.
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable.
	 */
	public Id buildId(int[] material) throws InitializationException;

	/**
	 * Builds a protocol-specific Id by using the given 
	 * string as source data for a hash function (like SHA-1). The 
	 * returned hash value will be the internal value for the new Id.
	 *
	 * @param materialToHash The string to use as source data for a hash function.
	 * @return The built Id as result to apply a hash function
	 * to the given String.
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable.
	 */
	public Id buildKey(String materialToHash) throws InitializationException; 
	
	/**
	 * Generate an Id from the String as its internal value.
	 * @param material With the String representation of the
	 * internal value.
	 * @return A new Id with the value included in the String.  
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable. 
	 */
	public Id buildId(String material) throws InitializationException;
	
	/**
	 * Generate an Id from a BigInteger, that includes its internal value.
	 * @param material BigInteger with the internal value for the new Id.
	 * @return A new Id with the value included in the BigInteger.
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable. 
	 */
	public Id buildId(java.math.BigInteger material) throws InitializationException;
	
	/**
     * Builds a new Id from an arbitray string applying a one-way hashing algorithm,
	 * such as SHA-1 or MD5.
	 * @param material An arbitray string. 
	 * @param algorithm One-way hashing algorithm such as "SHA" or "MD5".	 
	 * @throws InitializationException when an error occurs during the
	 * initialization of the Id or when this factory method is
	 * nonapplicable. 
	 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/guide/security/CryptoSpec.html#AppA">  
	 * Java Cryptography Architecture API Specification & Reference </a>
	 */
	public Id buildId(String material, String algorithm) throws InitializationException;
	
	/**
	 * Builds an uniformly distributed random Id.
	 * @return A new Id built randomly
	 * @throws InitializationException if an error occurs during the
	 * initialization of the Id.
	 */
	public Id buildRandomId() throws InitializationException;
	
	/**
	 * The Iterator instance returned permits to build as maximum <b>desiredNetworkSize</b>.
	 * This method goal is to obtain <b>desiredNetworkSize</b> equidistant Ids for a network.   
	 * @param desiredNetworkSize Number of nodes Id to obtain.
	 * @return An Iterator instance for getting all <b>desiredNetworkSize</b>.
	 * null if <b>desiredNetworkSize</b> is zero.
	 * @throws InitializationException if any error has ocurred during the initialization.
	 */
	public java.util.Iterator buildDistributedIds(int desiredNetworkSize) throws InitializationException;
}
