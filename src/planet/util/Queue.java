// An implementation of queues, based on arrays.
// (c) 1998, 2001 duane a. bailey

package planet.util;


/**
 * An implementation of queues based on arrays.
 * The head of the queue starts out at the head of the array, allowing the queue to 
 * grow and shrink in constant time. 
 * This queue implementation is ideal for 
 * applications that require a queue with a known maximum size that expands 
 * in constant time.
 * <P>
 * Example usage:
 * <P>
 * To compute the sum of the unicode value of every character in the standard input
 * we could use the following:
 * <P>
 * <pre>
 * public static void main(String[] arguments)
 * {
 *     int charsInInput = QueueExample.countChars(argument);
 *     {@link Queue} q = new {@link #Queue(int) Queue(charsInInput)};
 *     int unicodeSum = 0;
 *
 *     if(arguments.length > 0){
 *         for(int i=0; i < arguments.length; i++){
 *	       for(int j=0; j < arguments[i].length(); j++){
 *		   q.{@link #add(Object) #remove(new Character(arguments[i].charAt(j)))};
 *	       }
 *	   }
 *     }
 *
 *     while(!q.{@link #isEmpty()}){
 *	  char c = ((Character)q.{@link #remove()}).charValue();
 *	  unicodeSum+=Character.getNumericValue(c);
 *     }
 *
 *     System.out.println("Total Value: " + unicodeSum);
 * }
 * </pre>
 * @version $Id: Queue.java,v 1.1 2004/03/11 11:22:31 jordi Exp $
 * @author 2001 duane a. bailey
 */
public class Queue implements java.io.Serializable
{
    /**
     * The references to values stored within the queue.
     */
    protected Object data[]; // an array of data
    /**
     * index of the head of queue.
     */
    protected int head; // next dequeue-able value
    /**
     * current size of queue
     */
    protected int count; // current size of queue

    /**
     * Construct a queue holding at most size elements.
     *
     * Postconditions: create a queue capable of holding at most size values
     * 
     * @param size The maximum size of the queue.
     */
    public Queue(int size)
    {
        data = new Object[size];
        head = 0;
		count = 0;
    }

    /**
     * Add a value to the tail of the queue.
     * Preconditions:  the queue is not full
     * Postconditions: the value is added to the tail of the structure
     * 
     * @param value The value added.
     */
    public void add(Object value) throws QueueFull
    {
		if (isFull()) {
			throw new QueueFull();
			
		}	
		int tail = (head + count) % data.length;
		data[tail] = value;
		count++;
    }

    /**
     * Remove a value from the head of the queue.
     *
     * Preconditions:  the queue is not empty
     * Postconditions: the head of the queue is removed and returned
     * 
     * @return The value actually removed.
     */
    public Object remove()
    {
		Object value = data[head];
		data[head] = null; //free reference
		head = (head + 1) % data.length;
		count--;
		return value;
    }

    /**
     * Fetch the value at the head of the queue.
     *
     * Preconditions:  the queue is not empty
     * Postconditions: the element at the head of the queue is returned
     * 
     * @return Reference to the first value of the queue.
     */
    public Object get()
    {
		return data[head];
    }

    /**
     * Determine the number of elements within the queue
     *
     * Postconditions: returns the number of elements in the queue
     * 
     * @return The number of elements within the queue.
     */
    public int size()
    {
		return count;
    }

    /**
     * Remove all the values from the queue.
     *
     * Postconditions: removes all elements from the queue
     */
    public void clear()
    {
		// we could remove all the elements from the queue
		count = 0;
		head = 0;
    }
    
    /**
     * Determines if the queue is not able to accept any new values.
     *
     * Postconditions: returns true if the queue is at its capacity
     * 
     * @return True iff the queue is full.
     */
    public boolean isFull()
    {
		return count == data.length;
    }

    /**
     * Determine if the queue is empty.
     *
     * Postconditions: returns true iff the queue is empty
     * 
     * @return True iff the queue is empty.
     */
    public boolean isEmpty()
    {
		return count == 0;
    }

    
    /**
     * Construct a string representation of the queue.
     *
     * Postconditions: returns string representation of queue
     * 
     * @return String representing the queue.
     */
    public String toString()
    {
		StringBuffer s = new StringBuffer();
		int i,l;
	
		s.append("<QueueArray:");
		for (i = 0, l = head; i < count; i++, l = (l+1)%data.length)
		{
		    s.append("[" + l + "]"+data[l]);
		}
		s.append(">");
		return s.toString();
    }
}
