package planet.test;
import java.util.Hashtable;
import java.util.Vector;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import planet.chord.ChordProperties;
import planet.commonapi.Id;
import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.generic.commonapi.factory.Topology;
import planet.simulate.NetworkSimulator; 
import planet.simulate.Scheduler;
import planet.util.Properties; 
/**
 * Make different tests to build Ids with 32, 64, 96, 128 and 160 bits. The
 * Id's are generated randomly and circular manner. At last, the number of
 * generated Ids are 100 and 1000.
 * 
 * @author <a href="mailto:Ruben.Mondejar@estudiants.urv.es">Ruben Mondejar
 *         </a>
 * @author <a href="mailto:jordi.pujol@estudiants.urv.es">Jordi Pujol </a>
 */
public class IdTest extends TestCase {
	private NetworkSimulator sim;
	private Scheduler timer;
	private Vector events;
	private Network network;
	
	public IdTest(String name) throws InitializationException {
		super(name);
	}

	protected void setUp() {
		try {
            //arguments: properties file, application level, events, results, serialization
            GenericApp.start("../conf/master.properties",TestNames.IDTEST,false,false,false,false);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        if (! (Properties.overlayPropertiesInstance instanceof ChordProperties))
            throw new Error("This test only runs under Chord implementation.");
	}
	
	public static junit.framework.Test suite() {
		return new TestSuite(IdTest.class);
	}
	
	/**
	 * Make a test building Id's with 32 bits and network topology Random.
	 * Network size = 100 nodes.
	 * @throws Exception
	 */
	public void test100_32_random() throws Exception {
		System.out.println("//// TEST 100 WITH 32 BITS RANDOM////");
		Properties.factoriesNetworkTopology = Topology.RANDOM;
		Properties.factoriesNetworkSize = 100;
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 32;
        GenericApp.restart(false,false,false,false);

        Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}
	
	/**
	 * Make a test building Id's with 64 bits and network topology Random.
	 * Network size = 100 nodes.
	 * @throws Exception
	 */
	public void test100_64_random() throws Exception {
		System.out.println("//// TEST 100 WITH 64 BITS RANDOM////");
		Properties.factoriesNetworkTopology = Topology.RANDOM;
		Properties.factoriesNetworkSize = 100;
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 64;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}
	
	/**
	 * Make a test building Id's with 96 bits and network topology Random.
	 * Network size = 100 nodes.
	 * @throws Exception
	 */
	public void test100_96_random() throws Exception {
		System.out.println("//// TEST 100 WITH 96 BITS RANDOM////");
		Properties.factoriesNetworkTopology = Topology.RANDOM;
		Properties.factoriesNetworkSize = 100;
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 96;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}

	/**
	 * Make a test building Id's with 128 bits and network topology Random.
	 * Network size = 100 nodes.
	 * @throws Exception
	 */
	public void test100_128_random() throws Exception {
		System.out.println("//// TEST 100 WITH 128 BITS RANDOM////");
		Properties.factoriesNetworkTopology = Topology.RANDOM;
		Properties.factoriesNetworkSize = 100;
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 128;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}
	/**
	 * Make a test building Id's with 160 bits and network topology Random.
	 * Network size = 100 nodes.
	 * @throws Exception
	 */	
	public void test100_160_random() throws Exception {
		System.out.println("//// TEST 100 WITH 160 BITS RANDOM////");
		Properties.factoriesNetworkTopology = Topology.RANDOM;
		Properties.factoriesNetworkSize = 100;
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 160;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}
	
	/* ************* CIRCULAR NETWORK ********************* */
	/**
	 * Make a test building Id's with 32 bits and network topology Circular.
	 * Network size = 100 nodes.
	 * @throws Exception 
	 */
	public void test100_32_circular() throws Exception {
		System.out.println("//// TEST 100 WITH 32 BITS CIRCULAR////");
		Properties.factoriesNetworkTopology = Topology.CIRCULAR;
		Properties.factoriesNetworkSize = 100;
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 32;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}
	
	/**
	 * Make a test building Id's with 64 bits and network topology Circular.
	 * Network size = 100 nodes.
	 * @throws Exception
	 */
	public void test100_64_circular() throws Exception {
		System.out.println("//// TEST 100 WITH 64 BITS CIRCULAR////");
		Properties.factoriesNetworkTopology = Topology.CIRCULAR;
		Properties.factoriesNetworkSize = 100;
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 64;
        GenericApp.restart(false,false,false,false);
        
		Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}
	
	/**
	 * Make a test building Id's with 96 bits and network topology Circular.
	 * Network size = 100 nodes.
	 * @throws Exception
	 */
	public void test100_96_circular() throws Exception {
		System.out.println("//// TEST 100 WITH 96 BITS CIRCULAR////");
		Properties.factoriesNetworkTopology = Topology.CIRCULAR;
		Properties.factoriesNetworkSize = 100;
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 96;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}

	/**
	 * Make a test building Id's with 128 bits and network topology Circular.
	 * Network size = 100 nodes.
	 * @throws Exception
	 */
	public void test100_128_circular() throws Exception {
		System.out.println("//// TEST 100 WITH 128 BITS CIRCULAR////");
		Properties.factoriesNetworkTopology = Topology.CIRCULAR;
		Properties.factoriesNetworkSize = 100;
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 128;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}

	/**
	 * Make a test building Id's with 160 bits and network topology Circular.
	 * Network size = 100 nodes.
	 * @throws Exception
	 */
	public void test100_160_circular() throws Exception {
		System.out.println("//// TEST 100 WITH 160 BITS CIRCULAR////");
        Properties.factoriesNetworkTopology = Topology.CIRCULAR;
        Properties.factoriesNetworkSize = 100;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 160;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 100; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(100, ids.size());
	}
	
	/* ******************* NETWORK WITH 1000 NODES ************* */
	/**
	 * Make a test building Id's with 32 bits and network topology Random.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_32_random() throws Exception {
		System.out.println("//// TEST 1000 WITH 32 BITS RANDOM////");
        Properties.factoriesNetworkTopology = Topology.RANDOM;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 32;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}
	
	/**
	 * Make a test building Id's with 64 bits and network topology Random.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_64_random() throws Exception {
		System.out.println("//// TEST 1000 WITH 64 BITS RANDOM////");
        Properties.factoriesNetworkTopology = Topology.RANDOM;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 64;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}
	
	/**
	 * Make a test building Id's with 96 bits and network topology Random.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_96_random() throws Exception {
		System.out.println("//// TEST 1000 WITH 96 BITS RANDOM////");
        Properties.factoriesNetworkTopology = Topology.RANDOM;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 96;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}
	
	/**
	 * Make a test building Id's with 128 bits and network topology Random.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_128_random() throws Exception {
		System.out.println("//// TEST 1000 WITH 128 BITS RANDOM////");
        Properties.factoriesNetworkTopology = Topology.RANDOM;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 128;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}

	/**
	 * Make a test building Id's with 160 bits and network topology Random.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_160_random() throws Exception {
		System.out.println("//// TEST 1000 WITH 160 BITS RANDOM////");
        Properties.factoriesNetworkTopology = Topology.RANDOM;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 160;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}
	/* ************* CIRCULAR NETWORK ********************* */
	
	/**
	 * Make a test building Id's with 32 bits and network topology Circular.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_32_circular() throws Exception {
		System.out.println("//// TEST 1000 WITH 32 BITS CIRCULAR////");
        Properties.factoriesNetworkTopology = Topology.CIRCULAR;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 32;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}
	
	/**
	 * Make a test building Id's with 64 bits and network topology Circular.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_64_circular() throws Exception {
		System.out.println("//// TEST 1000 WITH 64 BITS CIRCULAR////");
        Properties.factoriesNetworkTopology = Topology.CIRCULAR;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 64;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}
	
	/**
	 * Make a test building Id's with 96 bits and network topology Circular.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_96_circular() throws Exception {
		System.out.println("//// TEST 1000 WITH 96 BITS CIRCULAR////");
        Properties.factoriesNetworkTopology = Topology.CIRCULAR;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 96;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}
	
	/**
	 * Make a test building Id's with 128 bits and network topology Circular.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_128_circular() throws Exception {
		System.out.println("//// TEST 1000 WITH 128 BITS CIRCULAR////");
        Properties.factoriesNetworkTopology = Topology.CIRCULAR;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 128;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}

	/**
	 * Make a test building Id's with 160 bits and network topology Circular.
	 * Network size = 1000 nodes.
	 * @throws Exception
	 */
	public void test1000_160_circular() throws Exception {
		System.out.println("//// TEST 1000 WITH 160 BITS CIRCULAR////");
        Properties.factoriesNetworkTopology = Topology.CIRCULAR;
        Properties.factoriesNetworkSize = 1000;
        ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey = 160;
        GenericApp.restart(false,false,false,false);

		Hashtable ids = new Hashtable();
		for (int i = 0; i < 1000; i++) {
			Id id = GenericFactory.buildId();
			System.out.println("[" + i + "]: " + id);
			ids.put(id, id);
		}
		assertEquals(1000, ids.size());
	}
}