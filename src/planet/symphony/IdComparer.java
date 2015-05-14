package planet.symphony;

import java.util.Comparator;

import planet.commonapi.Id;
import planet.commonapi.NodeHandle;
/**
 * This is class is used to compare two ids. For that purpose, it implements interface Comparer.
 * This includes a method called <b>compare</b> which returns a negative integer, zero, or a 
 * positive integer as the first argument is less than, equal to, or greater than the second.
 * 
 * @author <a href=mailto:marc.sanchez@estudiants.urv.es>Marc Sanchez</a>
 * @author <a href=mailto:jordi.pujol@estudiants.urv.es>Jordi Pujol</a> 
 */

public class IdComparer implements Comparer, Comparator, java.io.Serializable {
    
	private Id root;
    private int result1 = 0;
    private int result2 = 0;
    private Id id1 = null;
    private Id id2 = null;
    
    
	public IdComparer(NodeHandle root) {
		this.root = root.getId();
	}
	
	public int compare(Object o1, Object o2) {
        id1 = ((NodeHandle)o1).getId();
        id2 = ((NodeHandle)o2).getId();
        result1 = id1.compareTo(root);
        result2 = id2.compareTo(root);
        if (result1 > 0 && result2 > 0)
            return id1.compareTo(id2);
        else if (result1 < 0 && result2 < 0)
            return id1.compareTo(id2);
        else 
        {
            if (result1 >= 0 && result2 < 0) return -1;
            else if (result1 < 0 && result2 >= 0) return 1;
            else if (result1 == 0) return (result2==0)?0:-1;
            else return (result1==0)?0:1;  //==> result2 == 0
        }
	}
    
	public boolean equals(Object o)
	{
	    return (o instanceof IdComparer);
	}
}
