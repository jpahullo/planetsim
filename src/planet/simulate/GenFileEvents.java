package planet.simulate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import planet.commonapi.Id;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;

/**
 * Build file events in random or distributed manner.
 * The random events shows a network with its Ids built randomly.
 * The distributed events shows a network with equidistant Ids.  
 * @author Ruben Mondejar
 * @author Jordi Pujol
 */
public class GenFileEvents {

    /**
	 * Generate a set of random ids for nodes to insert to the overlay.
	 * The generated values are node's id and nodes' bootstrap. All this
	 * information is saved into file called <b>name</b>.
	 * @param name Filename where to save all events.
	 * @param num Number of nodes to generate.
	 * @param time Number of steps between any two events.
	 */
	public static void genRandomEvents(String name,int num, int time) 
			throws IOException,FileNotFoundException, InitializationException {
		printEvents(name,GenEvents.genRandomIds(num),time);
	}
	
	/**
	 * Build <b>num</b> equidistant Ids. It means that the increment between any two
	 * consecutive Ids are the same. The generated values are node's id and nodes' bootstrap. All this
	 * information is saved into file called <b>name</b>.
	 * @param name Filename where to save all events.
	 * @param num Number of nodes to generate.
	 * @param time Number of steps between any two events.
	 */
	public static void genDistribEvents(String name,int num, int time) throws IOException,FileNotFoundException, InitializationException {
		
	    printEvents(name,GenEvents.genDistribEvents(num),time);
	}

	/**
	 * Prints into the file called <b>name</b> all events appeared in <b>ids</b>.
	 * @param name Filename where to save all events.
	 * @param ids Object[] with two positions: the first one with the Id[] of the bootstraps;
	 * the second one with the Id[] of the node Ids itself.
	 * @param time Number of steps between any two events.
	 * @throws IOException when any error has ocurred during the printing.
	 * @throws FileNotFoundException if cannot make the new file or cannot overwrite it.
	 */
	private static void printEvents(String name, Object[] ids, int time) throws IOException,FileNotFoundException
	{
	    Id[] boot = (Id[])ids[0];
	    Id[] node = (Id[])ids[1];
				
		PrintStream pr = new PrintStream(new FileOutputStream(name));
		
		for(int i=0;i<boot.length;i++) {
		  pr.println("at "+i*time+" JOIN "+node[i]+" "+boot[i]+" 1");
		}		
		
		pr.close();
	}

	/**
	 * This main application wait three arguments:
	 * <ol>
	 * <li><b>name</b>: Filename to save all events.</li>
	 * <li><b>type</b>: A character: 'R' for Random events; 'D' for distributed events.</li>
	 * <li><b>num</b>: Number of events to build.</li>
	 * </ol>
	 * @param args Command line arguments.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
	    if (args.length!=4)
	    {
	        System.err.println("Required arguments: {name} {type} {numEvents} {steps}");
	        System.err.println("    {name}     : Filename where to save all events.");
	        System.err.println("    {type}     : R for random events or D for distributed events ");
	        System.err.println("    {numEvents}: Total number of events to build.");
	        System.err.println("    {steps}    : Number of steps between any two events.");
	        System.exit(0);
	    }
	    
		String name = args[0];
		String type = args[1];
		
		String number = args[2];
		int num = Integer.parseInt(number);	
		
		String step_time = args[3];
		int time = Integer.parseInt(step_time);	
        GenericApp gap = new GenericApp("../conf/master.properties",false,true,false,false);
		
		if (type.charAt(0)=='R') {
		  genRandomEvents(name,num,time);
		}
		else {
		  genDistribEvents(name,num,time);
		} 		
		System.out.println("The generated events file is ["+name+"]");
	}
}

