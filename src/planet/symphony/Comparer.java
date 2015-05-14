package planet.symphony;

/**
* Comparer interface imposes a total ordering on some collection of objects. The  data  structure calls
* <b>compare</b> method  and gets the order  of the elements on the collection. The ordering imposed by
* Comparer c must be consistent and implies an specific definition of a comparer for each object's type. 
*
* @author Marc Sanchez (<a href=mailto:marc.sanchez@estudiants.urv.es>marc.sanchez@estudiants.urv.es</a>) 
*/
public interface Comparer {
	/**
	 * A comparison function, which imposes a total ordering  on some collection of objects. Comparer 
	 * can be passed to a sort method to allow precise control over the sort order. Comparer can also
	 * be used to control the order of certain data structures. The ordering imposed by a Comparer c 
	 * on a set of elements S is said to be consistent with equals if and only if (compare((Object)e1, 
	 * (Object)e2)==0) has the same boolean value as e1.equals((Object)e2) for every e1 and e2 in S.
	 * @param o1 The first object to be compared.
	 * @param o2 The second object to be compared.
	 * @return A negative integer, zero, or a positive integer as the first argument is less than, 
	 * equal to, or greater than the second.
	 * @throws ClassCastException - if the arguments' types prevent them from being compared by this 
	 * Comparer.
	 */
	public int compare(Object o1, Object o2);
}
