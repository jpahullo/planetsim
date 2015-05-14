package planet.commonapi;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import planet.commonapi.exception.InitializationException;

/**
 * This interface is an abstraction of an Id (or key) from the CommonAPI paper.
 * <br><br>
 * Any class that extends this <b>Id</b> for a specific overlay, must offer
 * as least the no argument constructor.
 * <br><br>
 * The value for any built Id will be made with the related <b>setValues()</b>
 * method. If any of them is nonapplicable to any Id implementation,
 * an InitializationException must be thrown.
 *
 * @author <a href="mailto: cpairot@etse.urv.es">Carles Pairot</a>
 * @author <a href="mailto: ruben.mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 
 * @see planet.commonapi.exception.InitializationException
 */
public abstract class Id implements Comparable, Serializable {

  /**
   * Checks if this Id is between two given ids ccw (inclusive) and cw (exclusive) on the circle
   *
   * @param ccw the counterclockwise id
   * @param cw the clockwise id
   * @return true if this is between ccw (inclusive) and cw (exclusive), false otherwise
   */
  public abstract boolean between (Id ccw, Id cw);
  
  /**
   * Checks if this Id is in interval [init,end)
   *
   * @param init Id that open the interval
   * @return end Id that close the interval
   */
  public boolean Ebetween (Id init, Id end) {
  	if (this.equals (init)) {
        return true;
    }
    else {
      return this.between (init, end);
    }
  }
  
  /**
   * Checks if this Id is in interval (init,end]
   *
   * @param init Id that open the interval
   * @return end Id that close the interval
   */
  public boolean betweenE (Id init, Id end) {
  	if (this.equals (end)) {
  		return true;
  	}
  	else {
  		return this.between (init, end);
  	}
  }

  /**
   * Checks to see if the Id nid is clockwise or counterclockwise from this, on the ring. An Id is
   * clockwise if it is within the half circle clockwise from this on the ring. An Id is considered
   * counter-clockwise from itself.
   *
   * @param nid The id to compare to
   * @return true if clockwise, false otherwise.
   */
  public abstract boolean clockwise(Id nid);

  /**
   * Returns an Id corresponding to this Id plus a given distance
   *
   * @param offset the distance to add
   * @return the new Id
   */
  public abstract Id add (Id offset);

  /**
   * Returns an Id corresponding to this Id minus a given distance
   *
   * @param offset the distance to subtract
   * @return the new Id
   */
  public abstract Id subtract (Id offset);

  /** Shift operator. shift(-1,0) multiplies value of this by two, shift(1,0) divides by 2
   *
   * @param cnt the number of bits to shift, negative shifts left, positive shifts right
   * @param fill value of bit shifted in (0 if fill == 0, 1 otherwise)
   * @return this
   */
  public abstract Id shift (int cnt, int fill);

  /**
   * Returns a (mutable) byte array representing this Id
   *
   * @return A byte[] representing this Id
   */
  public abstract byte[] toByteArray();

  /**
   * Returns a string representing the full length of this Id.
   *
   * @return A string with all of this Id
   */
  public abstract String toStringFull();
  
  /**
   * Sets the new value with Id <b>newValue</b>.
   * <br><br>
   * This invokes:<br>
   * <center><b>setValue(newValue.getValue());</b></center>
   * @see planet.commonapi.Id#setValue(planet.commonapi.Id)
   * @param newValue Id with the new value.
   */
  public void setValue(Id newValue) {
  	setValue(newValue.getValue());
  }
  
  /**
   * Sets the new value to this Id with <b>newValue</b>
   * @param newValue Internal representation with the new value 
   * to set to this Id.
   */
  public abstract void setValue(Object newValue);
  
  /**
   * Returns the internal representation value of this Id.
   * @return The internal representatio value of this Id.
   */
  public abstract Object getValue();
  
  /**
   * This method returns the maximum value for an Id that is 
   * possible to build. Must to be reimplemented for any 
   * specific overlay Id. We recommend to use the singleton design pattern.
   * @return The maximum value sets to an Id. In this case,
   * always null. 
   */ 
  public static Id getMaximum() 
  {return null; }
  
  /**
   * Divides the maximum domain of the node Id in <b>numberOfNodes</b>,
   * to offers the offset between two consecutive nodes.
   * <br><br>
   * If the domain is [MIN..MAX], the aritmethical operation will be as follows:
   * <br>
   * <center>(MAX-MIN)/<b>numberOfNodes</b></center>
   * <br>
   * @param numberOfNodes Number of nodes in the network. 
   * @return The offset between two consecutive nodes, to get a network
   * with equidistance nodes. This static implementation always return null.
   * The expected value must be the same type as on the Id.setValue(Object) method.
   */
  public static Id divide(int numberOfNodes)
  { return null;}
  
  /**
   * This method returns the arithmetical result of this division:
   * <br>
   * <center>thisId/<b>divisor</b></center>
   * <br>
   * @param divisor The number of parts to divide this Id.
   * @return The result of division.
   */
  public abstract Id divideOn(int divisor);

  /**
   * Sets the new value for this Id, based on an integer value.
   * If this type of internal value is non applicable for the related
   * implementation, we recommend throws a NoSuchMethodError error.
   * @param newValue The new value.
   * @return The Id itself
   */
  public abstract Id setValues(int newValue);
  
  /**
   * Sets the new value for this Id, based on a double value.
   * If this type of internal value is non applicable for the related
   * implementation, we recommend throws a NoSuchMethodError error.
   * @param newValue The new value.
   * @return The Id itself
   */
  public abstract Id setValues(double newValue);
  
  /** 
   * Sets the new value for this Id, based on a byte[] value. It is also
   * used by the setValues(String, String) method.
   * If this type of internal value is non applicable for the related
   * implementation, we recommend throws a NoSuchMethodError error.
   * @param newValue The new value.
   * @return The Id itself
   * @see #setValues(String, String)
   */
  public abstract Id setValues(byte[] newValue);
  
  /** 
   * Sets the new value for this Id, based on an int[] value.
   * If this type of internal value is non applicable for the related
   * implementation, we recommend throws a NoSuchMethodError error.
   * @param newValue The new value.
   * @return The Id itself
   */
  public abstract Id setValues(int[] newValue);
  
  /** 
   * Sets the new value for this Id, based on a String value. Is a string
   * representation of any required number.
   * If this type of internal value is non applicable for the related
   * implementation, we recommend throws a NoSuchMethodError error.
   * @param newValue The new value.
   * @return The Id itself
   */
  public abstract Id setValues(String newValue);
  
  /** 
   * Sets the new value for this Id, based on a BigInteger value.
   * If this type of internal value is non applicable for the related
   * implementation, we recommend throws a NoSuchMethodError error.
   * @param newValue The new value.
   * @return The Id itself
   */
  public abstract Id setValues(BigInteger newValue);

  /** 
   * Sets the new value for this Id, based on a randomly generated value.
   * If this type of internal value is non applicable for the related
   * implementation, we recommend throws a NoSuchMethodError error.
   * @param valueGenerator The new value.
   * @return The Id itself
   */
  public abstract Id setValues(Random valueGenerator);

  /** 
   * Sets the new value for this Id, based on a hashed value. Finally,
   * it uses the setValues(byte[]) to set the correct value.
   * If this type of internal value is non applicable for the related
   * implementation, we recommend throws a NoSuchMethodError error.
   * @param material The input for the algorithm.
   * @param algorithm One-way hashing algorithm such as "SHA" or "MD5".
   * @throws InitializationException if the <b>material</b> is null or
   * the <B>algorithm</b> is not found. 
   * @return The Id itself
   * @see #setValues(byte[])
   */
  public Id setValues(String material, String algorithm) throws InitializationException
  {
      	byte[] digest = null;
		if (material == null) throw new InitializationException("The arbitrary string is set to null"); 
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(material.getBytes());
			digest = md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new InitializationException("Not algorithm support!", e);
		}
		return setValues(digest);
  }
}

