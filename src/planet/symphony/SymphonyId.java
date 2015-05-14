package planet.symphony;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import planet.commonapi.Id;
import planet.util.Utilities;


/**
 * This Id is backed up a double value. This value always must to be
 * between 0.0 and 1.0, as Symphony establish.
 * 
 * @author <a href="mailto: heliodoro.tejedor@estudiants.urv.es">Helio Tejedor</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @author <a href="mailto: marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
 */
public class SymphonyId extends Id implements java.io.Serializable {

	/* ********************** SYMPHONY SPECIFIC CONSTANTS  ***************/
	/**
	 * Symphony specific constant: Maximum value for the internal value of Id.
	 */
	public static final double MAX_SYMPHONY = 1.0;
	/* ********************** SYMPHONY SPECIFIC CONSTANTS  ***************/
	
	/* *************** STATIC ATTRIBUTES ****************************/
	/** Inform the maximum value for this SymphonyId (1.0). */
	protected static Id MAX;
	/* END *********** STATIC ATTRIBUTES ****************************/
    
	
	/**
	 * Internal value for this Id.
	 */
	private double id;
	
	/**
	 * Builds the double Id from a byte[].
	 * @param data Byte array get from a hashing function, like SHA-1 or MD5.
	 * @return The double value once the 'data' has been converted.
	 */
	static public double buildIdFrom(byte[] data)
	{
	    BigInteger Mask = BigInteger.ONE;
	    Mask = Mask.shiftLeft(data.length * Utilities.BYTE_SIZE);
		
		//always these are integer values ==> show them as double values to get double result
	    //it has 17 decimals, equals the maximum shown in a string representation of a double
		BigDecimal wrapperDigest = new BigDecimal(new BigInteger(data).toString()+".00000000000000000");
		wrapperDigest = wrapperDigest.divide(new BigDecimal(Mask.toString()+".00000000000000000"),BigDecimal.ROUND_CEILING);
		  
		double id = wrapperDigest.doubleValue();
		return getDecimals(id);
	}
	
	/**
	 * Gives back the value between 0.0 and 1.0 of the <b>value</b>,
	 * using only the decimals of the argument.
	 * @param value Double which gives back its decimals.
	 * @return The decimals that appear in the argument.
	 */
  	protected static double getDecimals(double value) {
		int i = (int) value;
		double ret = value - (double) i;
		if (ret < 0.0)
			ret += 1.0;
		return ret;
	}
	
    /* **************** END  STATICS METHODS *********************************/
    
    /**
     * Must be initialized to initial value, in this case to [0.00].
     */
    public SymphonyId() {
        id = 0.0;
    }
    
	public Object getValue() {
		return new Double(id);
	}
	
	public double getDoubleValue() {
		return id;
	}
	
	public void setValue(double id) {
		this.id = id;
	}
	
// Id

	public boolean between(Id ccw, Id cw) {
		int comp = ccw.compareTo(cw);
		
		if (comp == 0)
			return true;
		else if (comp < 0)
			return ( (compareTo(ccw) > 0) && (compareTo(cw) < 0) );
		else
			return ( (compareTo(ccw) > 0) || (compareTo(cw) < 0) );
	}

	public boolean clockwise(Id nid) {
		/* Difference between your id and mine must be less than or equal 0.5 */
		double other = getDecimals( ((SymphonyId) nid).id - id );
		return (other <= 0.5);
	}
	
	public Id add(Id offset) {
		return new SymphonyId().setValues(id + ((SymphonyId) offset).id);
	}
	
	public Id subtract(Id offset) {
		return new SymphonyId().setValues(id - ((SymphonyId) offset).id);
	}
	
	/* fill not used! */
	public Id shift(int cnt, int fill) {
		double m = (double) Math.pow(2, cnt);
		double nid = this.id * m;
		return new SymphonyId().setValues(nid);
	}

	public byte[] toByteArray() {
		long ba = Double.doubleToRawLongBits(id);
		byte[] r = new byte[8];
		for(int i = 0; i < r.length; i++) {
			r[i] = (byte) (ba & 0xFF);
			ba >>= 8;
		}
		return r;
	}

	public String toStringFull() {
		return Double.toString(id);
	}
	
	// Comparable
	public boolean equals(Object o) {
		return ((SymphonyId)o).id == id;
	}
	
	public int compareTo(Object o) {
        double other = ((SymphonyId)o).id;
        if (id<other)
            return -1;
        else if (id> other)
            return 1;
        else return 0;
	}

	public int hashCode() {
		long lb = Double.doubleToRawLongBits(id);
		return (int) (lb & 0xFFFF) ^ (int) (lb >> 8);
	}
	
	public String toString() {
		return Double.toString(id);
	}
	
	/**
	 * Sets the new value for this SymphonyId. This method
	 * waits an Double object as parameter.
	 * @see planet.commonapi.Id#setValue(java.lang.Object)
	 * @param newValue New double value for this Id, wrapped
	 * by a Double.
	 */
	public void setValue(Object newValue) {
		this.id = ((Double)newValue).doubleValue();
	}
	
	/**
	 * This method returns the maximum value for a SymphonyId that is 
     * possible to build. To return this maximum value use the
     * design pattern Singleton. 
     * @return The maximum value sets to an Id (1.0).
	 * @see planet.commonapi.Id#getMaximum()
	 */
	public static Id getMaximum() {
		if (MAX == null ) {
			MAX = new SymphonyId().setValues(MAX_SYMPHONY); 
		}
		return MAX;
	}
	
	/**
	 * Overwrite the static method to return the required Id.
	 * @param num Total number of nodes in the network.
	 * @return A new instance of this Id, with the increment between
	 * two consecutive Ids.
	 * @see planet.commonapi.Id#divide(int)
	 */
	public static Id divide(int num)
	{
	    return new SymphonyId().setValues((MAX_SYMPHONY/(double)num));
	}
	
    /**
     * This method returns the arithmetical result of this division:
     * <br>
     * <center>thisId/<b>divisor</b></center>
     * <br>
     * @param divisor The number of parts to divide this Id.
     * @return The result of division.
     */
    public Id divideOn(int divisor)
    {
        return new SymphonyId().setValues(id/(double)divisor);
    }
      
    /**
     * Always throws a NoSuchMethodError error.
     * @param newValue The new value
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(java.math.BigInteger)
     */
    public Id setValues(BigInteger newValue) {
        throw new NoSuchMethodError("ERROR: This constructor is non applicable");
    }
    
    /**
     * Sets the internal double value from the byte[] <b>newValue</b>.
     * @param newValue The new value
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(byte[])
     */
    public Id setValues(byte[] newValue) {
        id = buildIdFrom(newValue);
        return this;
    }
    
    /**
     * Sets the internal double value from the <b>newValue</b>
     * @param newValue The new value
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(double)
     */
    public Id setValues(double newValue) {
        id = getDecimals(newValue);
        return this;
    }
    /**
     * Always throws a NoSuchMethodError error.
     * @param newValue The new value.
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(int)
     */
    public Id setValues(int newValue){
        throw new NoSuchMethodError("ERROR: This constructor is non applicable");
    }
    
    /** 
     * Always throws a NoSuchMethodError error.
     * @param newValue The new value
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(int[])
     */
    public Id setValues(int[] newValue) {
        throw new NoSuchMethodError("ERROR: This constructor is non applicable");
    }
    
    /**
     * Uses the <b>valueGenerator</b> to radomly build a new value.
     * @param valueGenerator A Random number generator.
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(java.util.Random)
     */
    public Id setValues(Random valueGenerator) {
        return setValues(valueGenerator.nextDouble());
    }
    
    /**
     * Copies the string representation of a number to the internal double.
     * @param newValue The new value.
     * @return The Id itself
     * @throws An Error if the <b>newValue</b> is not a double value.
     * @see planet.commonapi.Id#setValues(java.lang.String)
     */
    public Id setValues(String newValue){
        try {
            id = Double.parseDouble(newValue);
            id = getDecimals(id);
        } catch(NumberFormatException nfe) {
            throw new Error("SymphonyId.setValues(java.lang.String) receives a non double value.");
        }
        return this;
    }
}
