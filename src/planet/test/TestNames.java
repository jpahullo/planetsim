package planet.test;

/**
 * This class shows all distributed test names. They will be used to identify
 * the requested configuration file into the main properties file, as the
 * <b>mainPropertyName</b> parameter on the simulator initialization by
 * GenericApp.start(...) method or its constructors.
 * <br><br>
 * The name structure has been designed following the test name, deleting the common 
 * starting <b>planet.test</b> and using an underscore to separate each subpackage
 * name. For example, the main test class <b>planet.test.SimTest</b> has its 
 * name as <B>SIMTEST</b>, and for <B>planet.test.GML.GMLTopology.GMLTopologyTest</b>
 * has <B>GML_GMLTOPOLOGY_GMLTOPOLOGYTEST</b>. In this way, any test has an
 * unique property key.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 13-jul-2005
 */
public class TestNames {
    public static final String IDTEST                          = "IDTEST";
    public static final String SIMNETTEST                      = "SIMNETTEST";
    public static final String SIMPLETEST                      = "SIMPLETEST";
    public static final String SIMTEST                         = "SIMTEST";
    public static final String TESTPOOL                        = "TESTPOOL";
    public static final String BAD_SIMNETTEST                  = "BAD_SIMNETTEST";
    public static final String BROADCAST_BROADCASTTEST         = "BROADCAST_BROADCASTTEST";
    public static final String DHT_DHTTEST                     = "DHT_DHTTEST";
    public static final String DHT2_DHTTEST                    = "DHT2_DHTTEST";
    public static final String FACTORY_TESTAPPFACTORY          = "FACTORY_TESTAPPFACTORY";
    public static final String FACTORY_TESTENDPOINTFACTORY     = "FACTORY_TESTENDPOINTFACTORY";
    public static final String FACTORY_TESTIDFACTORY           = "FACTORY_TESTIDFACTORY";
    public static final String FACTORY_TESTNETFACTORY          = "FACTORY_TESTNETFACTORY";
    public static final String FACTORY_TESTNODEFACTORY         = "FACTORY_TESTNODEFACTORY";
    public static final String FACTORY_TESTNODEHANDLEFACTORY   = "FACTORY_TESTNODEHANDLEFACTORY";
    public static final String GML_GMLTOPOLOGY_GMLTOPOLOGYTEST = "GML_GMLTOPOLOGY_GMLTOPOLOGYTEST";
    public static final String HELLOWORLD_DHTPEERTEST          = "HELLOWORLD_DHTPEERTEST";
    public static final String SCRIBE_SCRIBEPEERTEST           = "SCRIBE_SCRIBEPEERTEST";
    public static final String SCRIBE_SCRIBETEST               = "SCRIBE_SCRIBETEST";
    public static final String SERIALIZE_GENSERIALIZEDFILE     = "SERIALIZE_GENSERIALIZEDFILE";
    public static final String SERIALIZE_LOADSERIALIZEDFILE    = "SERIALIZE_LOADSERIALIZEDFILE";
    public static final String SERIALIZE_SERIALIZENETWORK      = "SERIALIZE_SERIALIZENETWORK";
    public static final String TRIVIALP2PTEST_TRIVIALTEST      = "TRIVIALP2PTEST_TRIVIALTEST";
}
