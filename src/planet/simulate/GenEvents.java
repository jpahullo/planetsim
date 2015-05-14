package planet.simulate;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import planet.commonapi.Id;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;

/**
 * This class permits to build events in a programming way, without any event file.
 * @author <a href="mailto: ruben.mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 08-jul-2005
 */
public class GenEvents {
	
	/**
	 * Generate a set of random ids for nodes to insert to the overlay.
	 * The generated values are node's id and nodes' bootstrap.
	 * @param num Number of nodes to generate.
	 * @return An array of two positions with: in first position the
	 * id node bootstrap of the node 'i'th; in second position the id of
	 * node 'i'th. 
	 */
	public static Object[] genRandomIds(int num) {
		
		Hashtable values = new Hashtable();
		HashSet control = new HashSet();
		
		Id value = null;

		//build all node Ids randomly
		int j=0;
		while(values.size()<num) {
		  do {
		    try {
		        value = GenericFactory.buildRandomId();
		    } catch (InitializationException e){
		        e.printStackTrace();
		    }
		  } while (control.contains(value));	         		      	 
		  control.add(value);
		  values.put(new Integer(j),value);
		  j++;	      
		}	     			
		
		//select all bootstrap Id
		Id[] node = new Id[num];
		Id[] boot = new Id[num];
		int pos;
		
		for(int i=0;i<num;i++) {	 
		  node[i] = (Id) values.get(new Integer(i));
		  pos = (int) (Math.random()*(double)i);
		  boot[i] = node[pos];
		}		
		
		//build the returned value.
		Object[] ids = new Object[2];
		ids[0]=boot;
		ids[1]=node;
		return ids;
	}
	
	/**
	 * Returns an array with two positions. The first one contains an array of Id of 
	 * the bootstraps. The second one contains an array of Id with the node Id itself.
	 * The Ids are built equidistant. It means that the increment between any two
	 * consecutive Ids are the same.
	 * @param num Number of nodes to build.
	 * @return An array of two positions of Object, with an Id[] in any position. The first
	 * position contains the bootstrap Ids for any node, that appear in the second position.
	 */
	public static Object[] genDistribEvents(int num)  {
	    try {
	        Iterator it   = GenericFactory.buildDistributedIds(num);
	        Object[] toReturn = new Object[2];
	        Id[] node     = new Id[num];
	        Id[] boot     = new Id[num];
	        Id value      = (Id)it.next();
	        int index     = 0;
	        int bootIndex = 0;
	        
	        node[0]      = value;
	        boot[0]      = value;
	        while (it.hasNext())
	        {
	            node[bootIndex+1] = (Id)it.next();
	            index = (int)(Math.random()*(double)bootIndex);
	            bootIndex++;
	            boot[bootIndex] = node[index];
	        }
	        
	        toReturn[0]=boot;
	        toReturn[1]=node;
	        return toReturn;
	        
	    } catch (Exception e)
	    {
	        e.printStackTrace();
	        return null;
	    }
	}
}
