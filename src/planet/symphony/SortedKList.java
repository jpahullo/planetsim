package planet.symphony;

import java.util.Iterator;
import java.util.Vector;

/**
 * A SortedKList is a list of objects kept in sorted order. A Comparer object is needed to compare 
 * objects in the list, as there is no standard compare operation for objects.  Comparer class for
 * standard types are found in the package planet.symphony. Moreover, the SortedKList needs from a
 * referee object and bound k. These two modifiers are used to filter elements on the SortedKList.
 * The others are removed. This data structure is useful to maintain neighbour's set of nodes. The
 * nodes befores the referee are its predecessors, the nodes after the referee are its successors.
 * @see planet.symphony.SymphonyNode Also the SortedKList deletes duplicates.
 *
 * @author Marc Sanchez (<a href=mailto:marc.sanchez@estudiants.urv.es>marc.sanchez@estudiants.urv.es</a>) 
 **/
public class SortedKList implements java.io.Serializable {
	/*
	 * SortedKList property: Comparer used to sort all the elements on the list.
	 */
	private Comparer listOrder; 
	/*
	 * SortedKList property: K elements must remain on the set.
	 */
	private int k;
	/*
	 * SortedKList property: sorted list.
	 */
	private java.util.Vector list = new java.util.Vector();
	/*
	 * SortedKList property: duplicate event. 
	 */
	private static final int DUPLICATE = -1;
	/*
	 * SortedKList property: referee object.
	 */
	private Object referee;
    private Vector predecessors = null;
    private boolean remakePredecessors = true;
    private Vector successors = null;
    private boolean remakeSuccessors = true;
    private Vector neighbours = null;
    private boolean remakeNeighbours = true;
    private Vector toRemove = null;
    private boolean remakeFarthestNeighbours = true;
    private Vector farthestNeighbours = null;
    /** For internal uses in the addAll() method. */
    private Vector addAllVector = null;
    
	/**
     * Create a new SortedKList using the Comparer interface to
     * define the order on elements.
     * @see planet.symphony.Comparer
     * @param listOrder Comparer used to sort the list.
     * @param referee The referee used to filter objects.
     * @param k The bound used to filter objects.
     */
    public SortedKList(Comparer listOrder, Object referee, int k) {
    	this.listOrder     = listOrder;
    	this.k             = k;
    	add(referee);
        this.referee       = referee;
        predecessors       = new Vector(k/2,1);
        successors         = new Vector(k/2,1);
        neighbours         = new Vector(k,1);
        toRemove           = new Vector(1,1);
        farthestNeighbours = new Vector(2,1);
    }
    /**
     * Returns the comparer object used to order list.
     */
    public Comparer getComparer(){
    	return listOrder;
    }
    
    /**
     * Adds a object to the sortedKList.
     * @param o Object to add.
     * @return Returns true if Object o has added to SortedKList.
     */
    public boolean add(Object o) {
    	if (list.isEmpty()) { 
    		list.add(o);
    		return true;
    	}
    	else { 
    		int insertAt = nearBinarySearch(0, list.size() - 1, o);
    		// Found
			if (insertAt == DUPLICATE) return false;
            remakePredecessors = true;
            remakeSuccessors = true;
            remakeNeighbours = true;
            remakeFarthestNeighbours = true;
			list.insertElementAt(o, insertAt);
    		while (list.size() > k + 1) 
            {
                if (!o.equals(list.get(k/2+1)))
                    toRemove.add(list.get(k/2+1));
                list.remove(k/2 + 1);
            }
		    return list.contains(o); //can be removed
    	}
    }
    /**
     * Returns the referee's object. Normally, if enough objects are on the list, i.e, k + 1
     * objects then k/2 objects may be before referee and k/2 objectes may be after referee. 
     * @return The referee's object on the list.
     */public Object getReferee() {
    	return this.referee;
    }
    /**
     * Adds a collection of elements inside the SortedKList.
     * @param c Collection of elements to add.
     * @return Returns true if some object has added to SortedKList.
     */ 
    public boolean addAll(java.util.Collection c) {
    	addAllVector = (Vector)c;
    	boolean inserted = false;
        int length = addAllVector.size();
        toRemove.clear();
        for (int i =0; i < length; i++){
    		inserted |= this.add(addAllVector.get(i));
    	}
    	return inserted;
    }
    /**
     * @return Returns the neighbourSet.
     */
    public java.util.Collection getNeighbourSet() {
        if (remakeNeighbours)
        {
            remakeNeighbours = false;
            neighbours.clear();
            for(int i = 1; i < list.size(); i++)
                neighbours.add(list.get(i));
        }
        return neighbours;
    }
    
    /**
     * Gets the removed neighbours in the last addition.
     * @return The removed neighbours in the last addition.
     */
    public Vector getRemovedNeighbours()
    {
        return toRemove;
    }
    
    /**
     * Gets the farthest neighbours in this sorted list, or the entire list
     * if it is not full.
     * @return The farthest neighbours or the entire list if is not full.
     */
    public java.util.Collection getFarthestNeighbours()
    {
        if (remakeFarthestNeighbours)
        {
            farthestNeighbours.clear();
            if (list.size() == k + 1)
            {
                farthestNeighbours.add(list.get(k/2));
                farthestNeighbours.add(list.get(k/2+1));
            } 
        }
        return (list.size() == k+1) ? farthestNeighbours:list;
    }
    
    /**
     * @return Returns an iterator over the elements in this list in proper sequence.
     */
    public Iterator iterator() {
    	return list.iterator();
    }
    
    /**
     * @return Returns the whole sorted set including referee.
     */
    public java.util.Collection getSortedSet() {
    	return list;
    }
    /**
     * Tests if the specified object is a component in this sortedList. 
     * @param o An object. 
     * @return Returns true if and only if the specified object is the same as a component on this SortedKList, 
     * as determined by the compare method; false otherwise.
     */
    public boolean contains(Object o) {
    	return binarySearchIter(0, list.size() - 1, o) > -1;
    }
    /**
     * @return Returns true when sorted list has immediate neighbours; false otherwise.
     */
    public boolean hasNeighbour() {
    	return list.size() > 1;
    }
    /**
     * Removes an object from neighbour's Set.
     * @param o The object to remove.
     * @return Returns true if the object has been removed; false otherwise.
     */
    public boolean remove(Object o) {
    	int pred = binarySearchIter(0, list.size()- 1, o);
    	if (pred < 0) return false;
        remakeSuccessors = true;
        remakePredecessors = true;
        remakeNeighbours = true;
        remakeFarthestNeighbours = true;
    	list.remove(pred);
    	return true; 
    }
    /**
     * Use binary search to get near toFind on sortedKList data. Only
     * search between indexes startIndex and endIndex of data.
     */
    private int nearBinarySearch(int startOffset, int endOffset, Object toFind) {
    	int comparer = 0;
    	int midPoint = 0;

        do {
    		midPoint = (startOffset + endOffset) / 2;
    		comparer = listOrder.compare(toFind, list.get(midPoint));
            
    		if (comparer < 0) endOffset = midPoint - 1;
    		else if (comparer > 0) 	startOffset = midPoint + 1;
    		// Found
    		else return DUPLICATE;
    	} while (endOffset >= startOffset);
        
    	if (comparer > 0) 
            return midPoint + 1;
    	else
            return midPoint;
    }
    /**
     * Use binary search to get toFind item on SortedKList. 
     * @param toFind The object toFind.
     */
    private int binarySearchIter(int startOffset, int endOffset, Object toFind) {
    	   while (startOffset <= endOffset) {
           		int midPoint = (startOffset + endOffset) / 2;
           		int comparer = listOrder.compare(toFind, list.get(midPoint));
           		if (comparer < 0) endOffset = midPoint - 1;
           		else if (comparer > 0) 	startOffset = midPoint + 1;
           		else return midPoint;
            }
            return (-1);
    }
    
    /**
     * Returns the number of NodeHandles in this ordered list, including the
     * referee (the local node).
     * @return The number of elements in the list.
     */
    public int size()
    {
        return list.size();
    }
    
    /**
     * Returns the number of neighbours in this ordered list.
     * @return the number of neighbours in this ordered list.
     */
    public int neighboursNumber()
    {
        return list.size()-1;
    }
    
    /**
     * Tests if this vector has no components. 
     * @return True if and only if this vector has no components, that is, its size is zero; false otherwise.
     */
    public boolean isEmpty() {
    	return list.isEmpty();
    }
    /**
     * Returns the element at the specified position in this sorted list.
     * @param index Index of element to return.
     * @return Returns object at the specified index.
     * @throws ArrayIndexOutOfBoundsException - index is out of range (index < 0 || index >= size()).
     */
    public Object get(int index) {
    	return list.get(index);
    }
    /**
     * Returns the first successor.
     * @return Returns the first successor or null if no such successor is available.
     */
    public Object getFirstSucc() {
    	if (hasNeighbour())	return list.get(1);
    	return null;
    }
    /**
     * Returns the first predecessor.
     * @return Returns the first predecessor or null if no such predecessor is available.
     */
    public Object getFirstPred() {
    	if (hasNeighbour())	return list.lastElement();
    	return null;    	
    }
    
    /**
     * Returns the second predecessor.
     * @return The second predecessor or null if it not exist.
     */
    public Object getSecondPred()
    {
        if (list.size()>3) return list.get(list.size()-2);
        return null;
    }
    /**
     * Returns the successor list.
     * @param max Maximum number of successors.
     * @return Successor list
     */
    public Vector getSuccList(int max)
    {
        if (remakeSuccessors)
        {
            remakeSuccessors = false;
            successors.clear();
            int top = k/2-1;
            for (int i=1; i<=max && i<top; i++)
                successors.add(list.get(i));
        }
        return successors;
    }

    /**
     * Returns the predecessor list.
     * @param max Maximum number of predecessor.
     * @return Predecessor list
     */
    public Vector getPredList(int max)
    {
        if (remakePredecessors)
        {
            remakePredecessors = false;
            predecessors.clear();
            int j=0;
            for (int i=k/2+1; j<max && i<list.size(); i++)
            {
                predecessors.add(list.get(i));
                j++;
            }
        }
        return predecessors;
    }
    
    public String toString() {
    	return "<" + list + ">";
    }
}