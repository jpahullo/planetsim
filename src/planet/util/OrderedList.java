
package planet.util;

/**
 * This class offers the ability of mantain a collection of objects ordered
 * using an int value as order key.
 * 
 * @author <a href="mailto: pedro.garcia@urv.net">Pedro Garcia</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 14-jul-2005
 */
public class OrderedList implements java.io.Serializable {
    
    /**
     * The next item into the list. If is null, this instance is the last one.
     */
	private OrderedList next = null;
    /**
     * The previous item into the list. If is null, this instance is the first one.
     */
	private OrderedList prev = null;
    /**
     * The saved data into this item.
     */
	private Object elem; 
    /**
     * The order key.
     */
	private int value;
	
    /**
     * Builds an ordered list item with specified values.
     * @param elem Data to save.
     * @param value Ordenation key.
     */
	public OrderedList(Object elem, int value){
		this.elem = elem;
		this.value = value;
		this.next = null;
		this.prev = null;
	}
	
	/**
     * Inserts into the list the specified <b>elem</b>.
	 * @param elem OrderedList item to be inserted.
	 * @return The same instance once the <b>elem</b> has been inserted.
	 */
	public OrderedList insert (OrderedList elem){
		OrderedList tmp;
		
		tmp = this;
		while (tmp.next!=null && tmp.value<elem.value){
			tmp = tmp.next;
		}
		if (tmp.next==null&& tmp.value<elem.value) {
			tmp.next = elem;
			elem.prev = tmp;
			
		}else if (tmp.value>elem.value){
			elem.prev = tmp.prev;
			elem.next = tmp;
			tmp.prev.next = elem;
			tmp.prev = elem;
			
		}
	
		return this;
	}
	
    /**
     * Gets the next element into the ordered list, or null if the current
     * item is the last one.
     * @return The next element into the ordered list, or null if the 
     * current item is the last one.
     */
	public OrderedList pop(){
		if (next==null){
			return null;
		}else{
			this.next.prev = null;
			return this.next;
		}	
	}
	
    /**
     * Gets the saved data maped to the specified <b>value</b>.
     * @param value The found order key.
     * @return The saved data maped to the <b>value</b> or null if not exists
     * the <b>value</b> into the whole ordered list.
     */
	public Object get(int value){
		OrderedList tmp;
		tmp = this;
		
		while (tmp!=null&&tmp.value!=value){
			tmp = tmp.next;
		}
		if (tmp!=null)
			return tmp.elem;
		else return null;
	}
	
    /**
     * Requests if exists the key <b>value</B> into the ordered value.
     * @param value The key to be found.
     * @return true if this key is found into the whole list.
     */
	public boolean contains(int value){
		OrderedList tmp;
		tmp = this;
	
		while (tmp!=null&&tmp.value!=value){
			tmp = tmp.next;
		}
		if (tmp!=null)
			return true;
		else return false;
	}
	
	/**
     * Returns the number of elements into the list.
	 * @return The number of elements into the list.
	 */
	public int size (){
		OrderedList tmp;
		int sz = 0;
		tmp = this;
		while (tmp!=null){
			tmp = tmp.next;
			sz++;
		}
		return sz;
	}
	
    /**
     * Shows the following list item.
     * @return The following list item.
     */
	public OrderedList next(){
		return next;
	}
    
    /**
     * Shows the previous list item.
     * @return The previous list item.
     */
	public OrderedList prev() {
		return prev;
	}
	
    /**
     * Shows the key for the current list item.
     * @return The key for the current list item.
     */
	public int value() {
		return value;
	}
	
    /**
     * Shows the saved data into this list item.
     * @return The saved data into this list item.
     */
	public Object elem() {
		return elem;
	}
    
    /**
     * Returns recursively all values saved into the current list.
     * @return String representation of this list.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return " key="+value+" has the value="+elem+
            ((next==null)?"":""+next);
    }
}
