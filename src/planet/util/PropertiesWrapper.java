package planet.util;

import java.util.Properties;

import planet.commonapi.exception.InitializationException;

/**
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 05-jul-2005
 */
public class PropertiesWrapper extends Properties {
    
    /**
     * Gets the related value for the key <b>key</b> as a boolean value.
     * @param key Key to be found in this properties.
     * @return The associated value for the <b>key</b> as a boolean value.
     */
    public boolean getPropertyAsBoolean(String key) 
    {
        return getValueAsBoolean(getProperty(key),key);
    }
    
    /**
     * Gets the related value for the key <b>key</b> as a Class reference.
     * @param key Key to be found in this properies.
     * @return The associated value for the <b>key</b> as a Class reference.
     * @throws InitializationException if occurs any error during the loading
     * process.
     */
    public Class getPropertyAsClass(String key) throws InitializationException 
    {
        return getValueAsClass(getProperty(key),key);
    }
    
    /**
     * Gets the related value for the key <b>key</b> as a double value.
     * @param key Key to be found in this properies.
     * @return The associated value for the <b>key</b> as a double value.
     * @throws InitializationException if occurs any error during the loading
     * process.
     */
    public double getPropertyAsDouble(String key) throws InitializationException
    {
        return getValueAsDouble(getProperty(key),key);
    }
    
    /**
     * Gets the related value for the key <b>key</b> as an int value.
     * @param key Key to be found in this properties.
     * @return The associated value for the <b>key</b> as an int value.
     * @throws InitializationException if occurs any error during the loading
     * process.
     */
    public int getPropertyAsInt(String key) throws InitializationException 
    {
        return getValueAsInt(getProperty(key),key);
    }
    
    /**
     * Gets the <b>value</b> as a boolean value.
     * It uses the planet.util.Utilities.parseBoolean() method.
     * @param value Value to be converted as a boolean.
     * @param propertyName Name of the related property (for logging purposes).
     * @return The boolean value for the <b>value</b>
     * @see planet.util.Utilities#parseBoolean(java.lang.String)
     */
    public static boolean getValueAsBoolean(String value, String propertyName)
    {
            return Utilities.parseBoolean(value.trim());
    }

    /**
     * Obtain the Class reference from the related <b>value</b>.
     * @param value Name of the class to get its reference.
     * @param propertyName Name of the related property (for logging purposes).
     * @return The Class reference for the <B>value</b>
     * @throws InitializationException if any error has ocurred during the
     * loading process.
     */
    public static Class getValueAsClass(String value, String propertyName) throws InitializationException
    {
        try {
            return Class.forName(value.trim());
        } catch (ClassNotFoundException e)
        {
            throw new InitializationException("The value '" + value + "' has a non valid class name. Found in the property '"+propertyName+"'.",e);
        }
    }
    
    /**
     * Obtain the double value from the related <b>value</b>.
     * @param value A double value in string format.
     * @param propertyName Name of the related property (for loggin purposes).
     * @return The double value for the value.
     * @throws InitializationException if any error occurs during the 
     * loading process.
     */
    public static double getValueAsDouble(String value, String propertyName) throws InitializationException
    {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e)
        {
            throw new InitializationException("The value '" + value + "' is not a valid double value. Found in the property '"+propertyName+"'.",e);
        }
    }
    
    /**
     * Loads the value for the <b>propertyName</b> as an int value.
     * It uses the java.lang.Integer.parseInt() method.
     * @param value An int value in decimal format.
     * @param propertyName Name of the related property (for logging purposes).
     * @return The related int value.
     * @throws InitializationException if appears a no numerical expression.
     * @see java.lang.Integer#parseInt(java.lang.String)
     */
    public static int getValueAsInt(String value, String propertyName) throws InitializationException
    {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e)
        {
            throw new InitializationException("The value '" + value + "' is not a valid number. Found in the property '"+propertyName+"'.",e);
        }
    }
}
