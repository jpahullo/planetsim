package planet.chord;
import java.math.BigInteger;
import java.util.Random;

import planet.commonapi.Id;
import planet.util.Properties;
import planet.util.Utilities;

/**
 * Spedific Chord Id implementation.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 25/02/2005
 */ 
public class ChordId extends Id implements java.io.Serializable { 

	/* ******************* CHORD SPECIFIC  CONSTANTS **************/
	/**
	 * Chord specific constant: Maximum number of bits for any Id.
	 */
	public static final int MAX_BITS = 160;
	/**
	 * Chord specific constant:  Number of bits saved at any array position.
	 */
	public static final int MIN_BITS = 32;
	/**
	 * Chord specific constant: Maximum value of internal value of Id.
	 */
	//public static final int[] MAX_CHORD = {0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff};
	/**
	 * Chord specific constant: The value of one (1) of internal value of Id.
	 */
	public static final int[] ONE_CHORD = {0x1, 0, 0, 0, 0};
	/**
	 * Chord specific constant: The value of two (2) of internal value of Id.
	 */
	public static final int[] TWO_CHORD = {0x2, 0, 0, 0, 0};
	
	/**
	 * Chord specific constant: Each array position contains a maximum 
	 * representation of MODULE bits.
	 */
	protected static final int MODULE         = MIN_BITS;
	/**
	 * Chord specific constant: Maximum size of array that contains entire identification.
	 */
	protected static final int MAX_ARRAY_SIZE = MAX_BITS/MODULE;


	/* END ******************* CHORD SPECIFIC  CONSTANTS **************/
	
	/* *********** STATIC ATTRIBUTES FOR getMaximum() METHOD ********/
	/** Sets the number of bits used in last building of MAX. */
	protected static int bitsKey;
	/** Maximum value for the ChordId implementation. */
	protected static Id MAX;
	/* END *********** STATIC ATTRIBUTES FOR getMaximum() METHOD ********/
	

  /**
   * number of elements in the array
   */ 
  private int arrayElems = ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey / 32;
  /**
   * The number of bits per key as is shown in the current configuration.
   */
  protected int bitsPerKey = ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey;

  /**
   * the id itself.
   */
  private int[] Id;
  /** Auxiliar id. */
  private ChordId offsetId = null;

  
  /**
   * Generates an Id initialized to zero.
   * It is not required for the framework.
   */
  public ChordId() {
    Id = new int[arrayElems];
    for (int i = 0; i < arrayElems; i++)
      Id[i] = 0;
  }		 
  
  /**
   * Capture the MSB (More Significative Bits) of the identification,
   * in fragments of MODULE bits (32 bits). The MSB remain the MSB 
   * at new identification and are discarded the LSB (Less Significative
   * Bits).
   * @param dataForId Array with any size, with fragments of representation
   * of MODULE bits.
   */
  private void translate(int[] dataForId) {
  	int dataLength = this.getLastIndex(dataForId) + 1;
  	if (arrayElems == dataLength) {
  		for (int i = 0; i < arrayElems; i++) {
  			Id[i] = dataForId[i];
  		}
  	} else if (arrayElems < dataLength) {
  		int length = dataLength-1;
  		int arrayLength = arrayElems-1;
  	  	for (int i = 0; i < arrayElems; i++) {
  				Id[arrayLength-i] = dataForId[length-i];
  		}
  	} else { // ArrayElems > DataLength
  		int i = 0;
  		// Capture first elements from dataForId
  		for (i=0; i<dataLength; i++) {
  			Id[i] = dataForId[i];
  		}
  		// Initialize the rest to zero
  		for (i++;i< arrayElems; i++) {
  			Id[i] = 0;
  		}
  	}
  }
  
  /**
   * Returns the last index with a value different to zero.
   * @param data Array to analyze.
   * @return The last index of array with a value different to zero.
   */
  private int getLastIndex(int[] data) {
  	int i = data.length - 1;
  	while (i > 0 && data[i] == 0) i--;
  	return i;
  }


  
  /**
   * Translate the BigInteger <b>value</b> to an Id,
   * partitioning it to fragments of 32 bits. 
   */
  protected void parseBigInteger(BigInteger value) {
	Id = new int[arrayElems];
	int[] data = new int[MAX_ARRAY_SIZE];
	
  	BigInteger two = new BigInteger("2");
	BigInteger div = two.pow(MODULE);	
	
	//build entire identification with maximum precition
	for (int i = 0; i < MAX_ARRAY_SIZE; i++) {	  	
      BigInteger[] result = value.divideAndRemainder(div);
	  data[i] = result[1].intValue();	 
	  value = result[0];	  	  	   
	}
	
	//transform the entire identification to fragment 
	//for actual presition bits.
	translate(data);
  }

  /**
   * Returns an Id corresponding to this Id plus a given offset
   *
   * @param offset the offset to add
   * @return the new Id
   */
  public Id add (Id offset) {
    ChordId newId = new ChordId();
    offsetId = (ChordId)offset;
    long x;
    long y;
    long sum;
    int carry = 0;

    for (int i = 0; i < arrayElems; i++) {
      // Little-endian to big-endian conversion
      x = Id[i] & 0x0ffffffffL;
      y = offsetId.Id[i] & 0x0ffffffffL;

      sum = x + y + carry;

      if (sum >= 0x100000000L) {
        carry = 1;
      } else {
        carry = 0;
      }

      newId.Id[i] = (int) sum;
    }

    // Overflow: have to do modulus 2^bitLength
    if (carry == 1) {
    	newId = (ChordId)newId.subtractWithCarry (getMaximum());
    }

    return newId;
  }

  public Id subtractWithCarry (Id offset) {
    ChordId newId = new ChordId();
    offsetId = (ChordId)offset;
    int carry = 1;
    long x;
    long y;
    long sub;

    for (int i = 0; i < arrayElems; i++) {
      x = Id[i] & 0x0ffffffffL;
      // Consider carry bit to be added to Id (it is in fact out of range!)
      if (i == arrayElems - 1) {
        x |= 0x100000000L;
      }
      y = offsetId.Id[i] & 0x0ffffffffL;

      sub = x - y - carry;
      if (sub < 0) {
        carry = 1;
      }
      newId.Id[i] = (int) sub;
    }

    return newId;
  }


  public Id subtract (Id offset) {
    ChordId newId = new ChordId();
    offsetId = (ChordId)offset;
    int carry = 0;
    long x;
    long y;
    long sub;

    for (int i = 0; i < arrayElems; i++) {
      x = Id[i] & 0x0ffffffffL;
      y = offsetId.Id[i] & 0x0ffffffffL;

      sub = x - y - carry;
      if (sub < 0) {
        carry = 1;
      }
      newId.Id[i] = (int) sub;
    }

    return newId;
  }

  public boolean equals (Object obj) {
    ChordId nid = (ChordId) obj;

    for (int i = 0; i < arrayElems; i++) {
      if (Id[i] != nid.Id[i]) {
        return false;
      }
    }

    return true;
  }

  public void blit (byte target[]) {
    for (int j = 0; j < bitsPerKey >> 3; j++) {
      int k = Id[j >> 2] >> ((j % 4) * 8);
      target[j] = (byte) (k & 0xff);
    }
  }

  public byte[] copy() {
    byte target[] = new byte[bitsPerKey >> 3];
    blit(target);
    return target;
  }

  /**
   * Comparison operator for Ids. The comparison that occurs is a numerical comparison.
   *
   * @param obj the Id to compare with.
   * @return negative if this < obj, 0 if they are equal and positive if this > obj.
   */
  public int compareTo (Object obj) {
    ChordId oth = (ChordId) obj;

    for (int i = arrayElems - 1; i >= 0; i--) {
      if (Id[i] != oth.Id[i]) {
        long t = Id[i] & 0x0ffffffffL;
        long o = oth.Id[i] & 0x0ffffffffL;
        if (t < o) {
          return -1;
        } else {
          return 1;
        }
      }
    }

    return 0;
  }

  /**
   * Returns the byte array representation of this Id
   *
   * @return The byte array representation of this id
   */
  public byte[] toByteArray() {
    return copy();
  }

  /**
   * Hash codes for Ids.
   *
   * @return a hash code.
   */
  public int hashCode() {
    int h = 0;

    /// Hash function is computed by XORing the bits of the Id.
    for (int i = 0; i < arrayElems; i++) {
      h ^= Id[i];
    }

    return h;
  }

  /**
   * Returns the complete represntation of this Id, in hex.
   *
   * @return The complete representation of this Id, in hexadecimal
   */
  public String toStringHexFull() {
    StringBuffer buffer = new StringBuffer();

    String tran[] = {"0", "1", "2", "3", "4", "5", "6", "7",
      "8", "9", "A", "B", "C", "D", "E", "F"};

    int n = bitsPerKey / 4;
    for (int i = n - 1; i >= 0; i--) {
      int d = getDigit(i, 4);

      buffer.append(tran[d]);
    }
    return buffer.toString();
  }

  public String toStringFull() {
    String hex = toStringHexFull();
    java.math.BigInteger dec;    // Decimal (base-10) equivalent of hexadecimal number.
    int i;                       // A position in hex, from 0 to hex.length()-1.
    dec = new java.math.BigInteger ("0");
    for ( i = 0; i < hex.length(); i++ ) {
      int digit = hexValue (hex.charAt(i));
      dec = ((dec.multiply (new java.math.BigInteger("16"))).add (new java.math.BigInteger (""+digit)));
    }
    return dec.toString();
  }

  private int hexValue (char ch) {
    // Returns the hexadecimal value of ch, or returns
    // -1 if ch is not one of the hexadecimal digits.
    switch (ch) {
      case '0':
        return 0;
      case '1':
        return 1;
      case '2':
        return 2;
      case '3':
        return 3;
      case '4':
        return 4;
      case '5':
        return 5;
      case '6':
        return 6;
      case '7':
        return 7;
      case '8':
        return 8;
      case '9':
        return 9;
      case 'a':
      case 'A':
        return 10;
      case 'b':
      case 'B':
        return 11;
      case 'c':
      case 'C':
        return 12;
      case 'd':
      case 'D':
        return 13;
      case 'e':
      case 'E':
        return 14;
      case 'f':
      case 'F':
        return 15;
      default:
        return -1;
    }
  }

  /**
   * Returns a string representation of the Id in base 16. The string is a byte string from most to
   * least significant.
   *
   * @return A String representation of this Id, abbreviated
   */
  public String toString() {
    return this.toStringFull();
  }

  /**
   * Gets the ith digit in base 2^b. i = 0 is the least significant digit.
   *
   * @param i which digit to get.
   * @param b which power of 2 is the base to get it in.
   * @return the ith digit in base 2^b.
   */
  public int getDigit(int i, int b) {
    int bitIndex = b * i + (bitsPerKey % b);
    int index = bitIndex / 32;
    int shift = bitIndex % 32;
    long val = Id[index];
    if (shift + b > 32) {
      val = (val & 0xffffffffL) | (((long) Id[index + 1]) << 32);
    }
    return ((int) (val >> shift)) & ((1 << b) - 1);
  }

  /** Shift operator. shift(-1,0) multiplies value of this by two, shift(1,0) divides by 2
   *
   * @param cnt the number of bits to shift, negative shifts left, positive shifts right
   * @param fill value of bit shifted in (0 if fill == 0, 1 otherwise)
   * @return this
   */
  public Id shift (int cnt, int fill) {
    return shift (cnt, fill, false);
  }


  /**
   * Shift operator. shift(-1,0) multiplies value of this by two, shift(1,0) divides by 2
   *
   * @param cnt the number of bits to shift, negative shifts left, positive shifts right
   * @param fill value of bit shifted in (0 if fill == 0, 1 otherwise)
   * @param roundUp if true, round up the results after right shifting
   * @return this
   */
  public Id shift (int cnt, int fill, boolean roundUp) {
    ChordId newId = new ChordId();
    newId.setValues(this.Id);
    int carry = 0;
    int bit = 0;
    int lsb = 0;

    if (cnt > 0) {
      for (int j = 0; j < cnt; j++) {
        // shift right one bit
        carry = (fill == 0) ? 0 : 0x80000000;
        for (int i = arrayElems - 1; i >= 0; i--) {
          bit = newId.Id[i] & 1;
          newId.Id[i] = (newId.Id[i] >>> 1) | carry;
          carry = (bit == 0) ? 0 : 0x80000000;
        }

        if (j == 0) {
          lsb = bit;
        }
      }

      if (roundUp && lsb > 0) {
        newId = (ChordId)inc();
      }
    } else {
      for (int j = 0; j < -cnt; j++) {
        // shift left one bit
        carry = (fill == 0) ? 0 : 1;
        for (int i = 0; i < arrayElems; i++) {
          bit = newId.Id[i] & 0x80000000;
          newId.Id[i] = (newId.Id[i] << 1) | carry;
          carry = (bit == 0) ? 0 : 1;
        }
      }
    }
    return newId;
  }

  private Id inc() {
    ChordId newId = new ChordId();
    long x;
    long sum;
    int carry = 1;

    // add one
    for (int i = 0; i < arrayElems; i++) {
      x = Id[i] & 0x0ffffffffL;
      sum = x + carry;
      if (sum >= 0x100000000L) {
        carry = 1;
      } else {
        carry = 0;
      }
      newId.Id[i] = (int) sum;
    }
    return newId;
  }

  public boolean between (Id ccw, Id cw) {
        if (ccw.equals(cw)) {
          return true;
        }
        else if (ccw.compareTo(cw) < 0) {
          return ((this.compareTo (ccw) > 0 ) && (this.compareTo (cw) < 0));
        }
        else {
          return ((this.compareTo (ccw) > 0) || (this.compareTo (cw) < 0));
        }
  }

  /**
   * Checks to see if the Id nid is clockwise or counterclockwise from this, on the ring. An Id is
   * clockwise if it is within the half circle clockwise from this on the ring. An Id is considered
   * counter-clockwise from itself.
   *
   * @param nid The Id we are comparing to
   * @return true if clockwise, false otherwise.
   */
  public boolean clockwise (Id nid) {
    ChordId nidId = (ChordId)nid;
    boolean diffMSB = ((Id[arrayElems - 1] & 0x80000000) != (nidId.Id[arrayElems - 1] & 0x80000000));
    int x;
    int y;
    int i;

    if ((x = (Id[arrayElems - 1] & 0x7fffffff)) != (y = (nidId.Id[arrayElems - 1] & 0x7fffffff))) {
      return ((y > x) ^ diffMSB);
    } else {
      for (i = arrayElems - 2; i >= 0; i--) {
        if (Id[i] != nidId.Id[i]) {
          break;
        }
      }

      if (i < 0) {
        return diffMSB;
      } else {
        long xl;
        long yl;

        xl = Id[i] & 0xffffffffL;
        yl = nidId.Id[i] & 0xffffffffL;

        return ((yl > xl) ^ diffMSB);
      }
    }
  }  
  
	/**
	 * Return the int[] with the value of this Id.
	 * @see planet.commonapi.Id#getValue()
	 * @return int[] with the value of this Id.
	 */
	public Object getValue() {
		return Id;
	}
	
	
	/**
	 * Sets the new value with the int[] that appears at <b>newValue</b>.
	 * @see planet.commonapi.Id#setValue(java.lang.Object)
	 * @param newValue New value as int[] representation.
	 */
	public void setValue(Object newValue) {
		translate((int[])newValue);
	}
	
	/**
	 * This method returns the maximum value for a ChordId that is 
     * possible to build, according the number of bits for key
     * actually in use. To return this maximum value use the
     * design pattern Singleton, reviewing the number of bits in use. 
     * @return The maximum value sets to an Id, always according
     * the number of bits actually in use.
	 * @see planet.commonapi.Id#getMaximum()
	 */
	public static Id getMaximum() {
		if (MAX == null || ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey != bitsKey) {
			bitsKey = ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey; //update values
            MAX = new ChordId();
            MAX.setValues(new BigInteger("2").pow(bitsKey)); 
		}
		return MAX;
	}
	
	/**
     * Divides the maximum domain of the node Id in <b>numberOfNodes</b>,
	 * to offers the offset between two consecutive nodes.
	 * @param numberOfNodes Number of nodes in the network. 
	 * @return The offset between two consecutive nodes, to get a network
	 * with equidistance nodes, as an object value.
     * @see planet.commonapi.Id#divide(int)
     */
    public static Id divide(int numberOfNodes) {
        BigInteger MAX = new BigInteger("2").pow(((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey);
        BigInteger div = new BigInteger(Integer.toString(numberOfNodes));
        ChordId toReturn = new ChordId();
        toReturn.setValues(MAX.divide(div));
        return toReturn;
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
        BigInteger thisId = new BigInteger(this.toString());
        BigInteger div    = new BigInteger(Integer.toString(divisor)); 
        ChordId toReturn  = new ChordId();
        toReturn.setValues(thisId.divide(div));
        return toReturn;
    }
    
    
    /**
     * Sets the internal int[] value from the BigInteger <b>newValue</b>.
     * @param newValue The new value
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(java.math.BigInteger)
     */
    public Id setValues(BigInteger newValue) {
        parseBigInteger(newValue);
        return this;
    }

    /**
     * Sets the internal int[] value from the byte[] <b>newValue</b>.
     * @param newValue The new value
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(byte[])
     */
    public Id setValues(byte[] newValue) {
        translate(Utilities.toIntArray(newValue));
        return this;
    }

    /**
     * Always throws a NoSuchMethodError.
     * @param newValue The new value
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(double)
     */
    public Id setValues(double newValue) {
        throw new NoSuchMethodError("No double value be applied to ChordId.");
    }
    /**
     * Sets the <b>newValue</b> to the first position of the internal int[],
     * and the rest of positions to zero.
     * @param newValue The new value.
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(int)
     */
    public Id setValues(int newValue){
        if (Id == null)
            Id = new int[arrayElems];
        Id[0]= newValue;
        for (int i=1; i< arrayElems; i++) Id[i] = 0;
        return this;
    }
    
    /** 
     * Sets the internal value from the int[] <b>newValue</b>
     * @param newValue The new value
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(int[])
     */
    public Id setValues(int[] newValue) {
        translate(newValue);
        return this;
    }
    
    /**
     * Uses the <b>valueGenerator</b> to radomly build a new value.
     * @param valueGenerator A Random number generator.
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(java.util.Random)
     */
    public Id setValues(Random valueGenerator) {
        return setValues(new BigInteger(bitsPerKey,valueGenerator));
    }
    
    /**
     * Copies the string representation of a number to the internal int[].
     * @param newValue The new value.
     * @return The Id itself
     * @see planet.commonapi.Id#setValues(java.lang.String)
     */
    public Id setValues(String newValue) {
        parseBigInteger(new BigInteger(newValue));
        return this;
    }
}