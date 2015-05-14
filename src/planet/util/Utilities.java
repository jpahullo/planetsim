package planet.util;

import java.security.*;

/**
 * Offers different utilities.
 * @author Jordi Pujol
 * @author Pedro Garcia
 */
public class Utilities {
    
    /** Size in bits of the <b>int</b> */
    public final static int INT_SIZE  = 32;
    /** Size in bits of the <b>long</b> */
    public final static int LONG_SIZE = 64; 
    /** Size in bits of the <b>byte</b> */
    public final static int BYTE_SIZE = 8;
    /** Number of bytes for any int value. */
    public final static int INT_AS_BYTES = INT_SIZE >> 3;

    /**
     * Copy all bytes as int values.
     * @param dst int[] where to copy all bytes.
     * @param src source byte[] 
     */
    public static void toIntArray(int[] dst, byte[] src)
    {
        for (int clusterI = 0; clusterI < dst.length; clusterI++) {
            for (int byteI = 0; byteI < INT_AS_BYTES; byteI++) {
                dst[clusterI] <<= 8;
                dst[clusterI] = dst[clusterI] | (src[byteI + clusterI * INT_AS_BYTES] & 0xff);
            }
        }
    }
    
	/**
	 * Change a byte array to int array
	 * @param data byte array to change to int array
	 * @return int array from data
	 */
	public static int[] toIntArray(byte[] data) {
		int[] newData = new int[data.length/INT_AS_BYTES+((data.length%INT_AS_BYTES>0)?1:0)];
        toIntArray(newData,data);
		return newData;
	}
	
	/**
	 * Change a byte array to int array
	 * @param data byte array to change to int array
	 * @return int array from data
	 */
	public static int[] toIntArray(String data) {
		byte[] dataByte = data.getBytes();
		int[] newData = new int[dataByte.length];
		for (int i=0; i<dataByte.length;i++) {
			newData[i] = (int) dataByte[i];
		}
		return newData;
	}
	
	/**
	 * Generate a hash code from specified data. Hash function is SHA-1. 
	 * @param data String to obtain its hash code.
	 * @return null if no SHA could be obtained, or result of hash function in 
	 * int array format.
	 */
	public static int[] generateIntHash (String data) {
	  MessageDigest md = null;
	  byte[] digest = null;

	  try {
		md = MessageDigest.getInstance("SHA");
		md.update(data.getBytes());
		digest = md.digest();
	  }
	  catch (NoSuchAlgorithmException e) {
		System.err.println("No SHA support!");
	  }
	  
	  return toIntArray(digest);
	}

	/**
	 * Generate a hash code from specified data. Hash function is SHA-1. 
	 * @param data String to obtain its hash code.
	 * @return null if no SHA could be obtained, or result of hash function 
	 * in byte array format.
	 */
	public static byte[] generateByteHash (String data) {
	  MessageDigest md = null;
	  byte[] digest = null;

	  try {
		md = MessageDigest.getInstance("SHA");
		md.update(data.getBytes());
		digest = md.digest();
	  }
	  catch (NoSuchAlgorithmException e) {
		System.err.println("No SHA support!");
	  }
	  return digest;
	}

	/**
	 * Parses the string argument as a boolean. The boolean returned represents the value true if the string argument is not null and is equal, ignoring case, to the string "true".
	 * Implemented in JDK1.4 as a replacement of jdk1.5 Boolean.parseBoolean.
	 * @param txt The String containing the boolean representation to be parsed
	 * @return the boolean represented by the string argument
	 */
	public static boolean parseBoolean(String txt){
		String true_v = txt.toLowerCase();
		return (true_v!=null && true_v.equals("true"));
	}
}
