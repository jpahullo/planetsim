package planet.simulate;
import planet.commonapi.Id;
import planet.commonapi.RouteMessage;
/**
 * @author Pedro García
 * @author Ruben Mondejar
 * @author Jordi Pujol
 */
public class Logger {

	/* *************** CONSTANTS FOR LOGGER LEVELS ***************/
	
	/**
	 * Logger level: Only permits to show error logs.
	 */
    public final static int ERROR_LOG  	= 0;
    /**
     * Logger level: Permits to show event logs.
     */	
    public final static int EVENT_LOG  	= 1;
    /**
     * Logger level: Permits to show a bigger number of logs.
     */
	public final static int PRINT_LOG  	= 2;
	/**
	 * Logger level: The less restrictive level of logs. 
	 * It is anticipated that they appear logs of sending and reception 
	 * of the different messages.
	 */
	public final static int MSG_LOG		= 3;	
	
	/* END *************** CONSTANTS FOR LOGGER LEVELS ***************/
	
	/**
	 * Level of logs.
	 */
	private static int level;
	/**
	 * Number of step actually in run.
	 */
	private static int step;
	/**
	 * Prebuilded string for concrete logs.
	 */
	private static String[] msgsR = {"Node id ", " receive message from ",
			" to ", " type ", " mode ", " key ", " content "};
	/**
	 * Prebuilded string array for concrete logs.
	 */
	private static String[] msgsS = {"Node id ", " send message to ", " from ",
			" type ", " mode ", " key ", " content "};
	
	/**
	 * Sets the level of logs to be applied.
	 * 
	 * @param lvl Level of logs to apply.
	 */
	public static void setLevel(int lvl) {
		level = lvl;
	}
	
	/**
	 * Sets the actual step in run.
	 * 
	 * @param time Actual step in run.
	 */
	public static void setStep(int time) {
		step = time;
	}
	
	/**
	 * Gets the actual step in run.
	 * 
	 * @return Actual step in run
	 */
	public static int getStep() {
		return step;
	}
	
	/**
	 * Logs a message <b>msg </b> with level <b>lvl </b>.
	 * 
	 * @param msg Message to be log.
	 * @param lvl Priority of the log.
	 */
	public static void log(String msg, int lvl) {
		if (level >= lvl) {
			System.out.println("LOGGER at " + step + " : " + msg);
		}
	}
	
	/**
	 * Logs a string combining arguments as following:
	 * <pre>
	 *    msg + p1 + msg1
	 * </pre>
	 * @param msg First fragment of the message.
	 * @param p1 int value to be log in second term.
	 * @param msg1 Last fragment of the message.
	 * @param lvl Priority of the message.
	 */
	public static void log(String msg, int p1, String msg1, int lvl) {
		if (level >= lvl) {
			System.out.println("LOGGER at " + step + " : " + msg + p1 + msg1);
		}
	}
	
	/**
	 * Logs a sent RouteMessage.
	 * @param id Related Id for the RouteMessage.
	 * @param msg RouteMessage to be log.
	 * @param lvl Priority of the message.
	 */
	public static void logSend(Id id, RouteMessage msg, int lvl) {
		if (level >= lvl) {
			Object[] objs = {id, msg.getDestination().getId(), msg.getSource().getId(),
					Globals.typeToString(msg.getType()), Globals.typeToString(msg.getMode()),
					msg.getKey(), msg.getMessage()};
			Logger.log(msgsS, objs, lvl);
		}
	}
	
	/**
	 * Logs a received RouteMessage
	 * @param id Related Id of the RouteMessage.
	 * @param msg RouteMessage to be log.
	 * @param lvl Priority of the log.
	 */
	public static void logReceive(Id id, RouteMessage msg, int lvl) {
		if (level >= lvl) {
			Object[] objs = {id, msg.getSource().getId(), msg.getDestination().getId(),
					Globals.typeToString(msg.getType()), Globals.typeToString(msg.getMode()),
					msg.getKey(), msg.getMessage()};
			Logger.log(msgsR, objs, MSG_LOG);
		}
	}
	
	/**
	 * Logs any object. 
	 * @param msg String to be logged first.
	 * @param obj Object to be logged at last (using obj.toString()).
	 * @param lvl Priority of the log.
	 */
	public static void log(String msg, Object obj, int lvl) {
		if (level >= lvl) {
			System.out.println("LOGGER at " + step + " : " + msg + obj);
		}
	}
	
	/**
	 * Logs the following string:
	 * <pre>
	 *    obj + msg + obj2
	 * </pre>
	 * @param obj First object to be logged.
	 * @param msg Message to be logged at second term.
	 * @param obj2 Last object to be logged.
	 * @param lvl Priority of the log.
	 */
	public static void log(Object obj, String msg, Object obj2, int lvl) {
		if (level >= lvl) {
			System.out.println("LOGGER at " + step + " : " + obj + msg + obj2);
		}
	}
	
	/**
	 * Logs the following string:
	 * <pre>
	 *    msg + obj + msg2
	 * </pre>
	 * @param msg Message to be logged at first.
	 * @param obj Object to be logged at second term.
	 * @param msg2 Message to be logged at last.
	 * @param lvl Priority of the log.
	 */
	public static void log(String msg, Object obj, String msg2, int lvl) {
		if (level >= lvl) {
			System.out.println("LOGGER at " + step + " : " + msg + obj + msg2);
		}
	}
	
	/**
	 * Logs a combination of the arguments. That is:
	 * <pre>
	 * for (int i = 0; i < msgs.length; i++) {
	 *     result = result + msgs[i] + objs[i];
	 * }
	 * </pre>
	 * and logs the <b>result</b> string.
	 * @param msgs String to be used in combination.
	 * @param objs Objects to be used in combination.
	 * @param lvl Priority of the log.
	 */
	public static void log(String[] msgs, Object[] objs, int lvl) {
		if (level >= lvl) {
			String result = "";
			for (int i = 0; i < msgs.length; i++) {
				result = result + msgs[i] + objs[i];
			}
			System.out.println("LOGGER at " + step + " : " + result);
		}
	}
}
