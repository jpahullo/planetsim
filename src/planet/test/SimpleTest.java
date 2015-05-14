package planet.test;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import planet.chord.ChordId;
import planet.chord.ChordProperties;
import planet.commonapi.Id;
import planet.commonapi.Network; 
import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.NodeImpl;
import planet.generic.commonapi.factory.GenericFactory; 
import planet.generic.commonapi.factory.Topology;
import planet.simulate.EventParser;
import planet.simulate.NetworkSimulator;
import planet.simulate.Results;
import planet.simulate.Scheduler;
import planet.util.Properties;

/**
 * Test differents simulation cases, with joining, failing and leaving nodes.
 * 
 * @author <a href="mailto:Ruben.Mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto:jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 */
public class SimpleTest extends TestCase {
    
	private NetworkSimulator sim;
	private Scheduler timer;
	private Vector events;
	private Network network;
	
	public SimpleTest(String name) throws InitializationException {
		super(name);
	}
	
	protected void setUp() {
		try {
            //arguments: properties file, application level, events, results, serialization
            GenericApp.start("../conf/master.properties",TestNames.SIMPLETEST,false,true,false,false);

            //Topology == Random because the size of network is zero.
			Properties.factoriesNetworkTopology = Topology.RANDOM;
            Properties.factoriesNetworkSize = 0;
			Properties.simulatorQueueSize = 250;
			((ChordProperties)Properties.overlayPropertiesInstance).stabilizeSteps = 5;
			((ChordProperties)Properties.overlayPropertiesInstance).fixFingerSteps = 10;
            
            GenericApp.restart(false,true,false,false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static junit.framework.Test suite() {
		return new TestSuite(SimpleTest.class);
	}
	
	/**
	 * Join 4 nodes, with events at file "data/test_join.txt". 
	 * @throws Exception
	 */
	public void testJoin() throws Exception {
		System.out.println("//// TEST JOIN //// (using 'data/test_join.txt' events file)");
		events = EventParser.parseEvents("data/test_join.txt");
		timer = new Scheduler();
		timer.addEvents(events);
		network = GenericFactory.buildNetwork();
		sim = new NetworkSimulator(timer, network);
		sim.stabilize();
		System.out.println("Nodes joined with events at [" + network.getSimulatedSteps() + "] steps");
		sim.printNodes();
		sim.getInternalNetwork().prettyPrintNodes();
		assertEquals(4, network.size());
	}

	/**
	 * Joins 4 nodes, and one leave, based with events file "data/test_leave.txt". 
	 * @throws Exception
	 */
	public void testLeave() throws Exception {
		System.out.println("//// TEST LEAVE //// (using 'data/test_leave.txt' events file)");
		events = EventParser.parseEvents("data/test_leave.txt");
		timer = new Scheduler();
		timer.addEvents(events);
		network = GenericFactory.buildNetwork();
		sim = new NetworkSimulator(timer, network);
		sim.stabilize();
		System.out.println("Nodes joined with events at [" + network.getSimulatedSteps() + "] steps");
		sim.printNodes();
		sim.getInternalNetwork().prettyPrintNodes();
		assertEquals(3, network.size());
	}

	/**
	 * Joins 4 nodes and one fail, based with events file "data/test_fail.txt".
	 * @throws Exception
	 */
	public void testFail() throws Exception {
		System.out.println("//// TEST FAIL //// (using 'data/test_fail.txt' events file)");
		events = EventParser.parseEvents("data/test_fail.txt");
		timer = new Scheduler();
		timer.addEvents(events);
		network = GenericFactory.buildNetwork();
		sim = new NetworkSimulator(timer, network);
		sim.stabilize();
		System.out.println("Nodes joined with events at [" + network.getSimulatedSteps() + "] steps");
		sim.printNodes();
		sim.getInternalNetwork().prettyPrintNodes();
		assertEquals(2, network.size());
	}

	/**
	 * Tests the stabilization process of 4 nodes,
	 * based with events file "data/test_join.txt".
	 * @throws Exception
	 */
	public void testStabilize() throws Exception {
		System.out.println("//// TEST STABILIZE //// (using 'data/test_join.txt' events file)");
		events = EventParser.parseEvents("data/test_join.txt");
		Id id0 = new ChordId().setValues(new int[]{0});
		Id id1 = new ChordId().setValues(new int[]{1});
		Id id3 = new ChordId().setValues(new int[]{3});
		Id id6 = new ChordId().setValues(new int[]{6});
		Hashtable succs = new Hashtable();
		succs.put(id0, id1);
		succs.put(id1, id3);
		succs.put(id3, id6);
		succs.put(id6, id0);
		Hashtable preds = new Hashtable();
		preds.put(id0, id6);
		preds.put(id1, id0);
		preds.put(id3, id1);
		preds.put(id6, id3);
		timer = new Scheduler();
		timer.addEvents(events);
		
		network = GenericFactory.buildNetwork();
		sim = new NetworkSimulator(timer, network);
		sim.stabilize();
		System.out.println("Nodes joined with events at [" + network.getSimulatedSteps() + "] steps");
		sim.printNodes();
		sim.getInternalNetwork().prettyPrintNodes();
		
		Iterator it = network.iterator();
		while (it.hasNext()) {
			NodeImpl n = (NodeImpl) it.next();
			Hashtable info = n.getInfo();
			NodeHandle[] finger = (NodeHandle[]) info.get("finger");
			NodeHandle predecessor = (NodeHandle) info.get("predecessor");
			assertEquals(succs.get(n.getId()).toString(), finger[0].getId().toString());
			assertEquals(preds.get(n.getId()).toString(), predecessor.getId().toString());
		}
	}

	/**
	 * Tests if all finger tables contains correct values,
	 * based with events file "data/test_join.txt".
	 * @throws Exception
	 */
	public void testFixFingers() throws Exception {
		System.out.println("//// TEST FIX FINGERS //// (using 'data/test_join.txt' events file)");
		events = EventParser.parseEvents("data/test_join.txt");
		Id ids[] = new Id[4];
		ids[0] = new ChordId().setValues(new int[]{0});
		ids[1] = new ChordId().setValues(new int[]{1});
		ids[2] = new ChordId().setValues(new int[]{3});
		ids[3] = new ChordId().setValues(new int[]{6});
		timer = new Scheduler();
		timer.addEvents(events);

		network = GenericFactory.buildNetwork();
		sim = new NetworkSimulator(timer, network);
		sim.stabilize();
		System.out.println("Nodes joined with events at [" + network.getSimulatedSteps() + "] steps");
		sim.printNodes();
		sim.getInternalNetwork().prettyPrintNodes();
		
		Iterator it = network.iterator();
		while (it.hasNext()) {
			NodeImpl n = (NodeImpl) it.next();
			Hashtable info = n.getInfo();
			NodeHandle[] finger = (NodeHandle[]) info.get("finger");
			Id[] start = (Id[]) info.get("start");
			int j = 0;
			boolean find = false;
			while (!find) {
				if (ids[j].toString().equals(n.getId().toString()))
					find = true;
				j = (j + 1) % 4;
			}
			int i = 0;
			boolean end = false;
			while (i < ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey) {
				if (start[i].toString().compareTo(ids[j].toString()) <= 0
						&& !end) {
					assertEquals(finger[i].getId(), ids[j]);
					i++;
				} else if (end
						|| start[i].toString().compareTo(ids[3].toString()) > 0) {
					assertEquals(finger[i].getId(), ids[0]);
					end = true;
					i++;
				} else {
					j = (j + 1) % 4;
				}
			}
		}
	}
	
	/**
	 * Test to build dynamically 100 nodes with the predefined node class
	 * at properties file. It shows for each iteration the node just builded
	 * using GenericFactory.buildNode().
	 * @throws Exception
	 */
	public void testBuild100() throws Exception {
		System.out.println("//// TEST 100 JOINS ////");
		HashMap hashMap = new HashMap();
		for (int i = 0; i < 100; i++) {
			Node node = GenericFactory.buildNode();
			System.out.println("i [" + i + "]: Id [" + node.getId() + "]");
			hashMap.put(node.getId(), node);
		}
		assertEquals(100, hashMap.size());
	}
	
	/**
	 * Builds a network with 1000 nodes, with the predefined node class
	 * at properties file, using network.joinNodes(1000).
	 * @throws Exception
	 */
	public void test1000NetworkJoin() throws Exception {
		System.out.println("//// TEST NETWORK 1000 JOINS ////");
		network = GenericFactory.buildNetwork();
		int steps = network.joinNodes(1000);
		System.out.println("Nodes joined at [" + steps + "] steps");
		steps = network.stabilize();
		System.out.println("Stabilizatoin at [" + steps + "] steps");
		assertEquals(1000, network.size());
	}
	
	/**
	 * Builds a network with 1000 nodes, based with
	 * events file "data/test_join1000r.txt".
	 * @throws Exception
	 */
	public void test1000Join() throws Exception {
		System.out.println("//// TEST 1000 JOINS //// (using data/test_join1000r.txt)");
		Id first = new ChordId().setValues(new int[]{1});
		Id last = new ChordId().setValues(new int[]{1000});
		events = EventParser.parseEvents("data/test_join1000r.txt");
		timer = new Scheduler();
		timer.addEvents(events);
		network = GenericFactory.buildNetwork();
		sim = new NetworkSimulator(timer, network);
		sim.stabilize();
		System.out.println("Nodes joined with events at [" + network.getSimulatedSteps() + "] steps");
		assertEquals(0, Results.getStabRate());
	}
}