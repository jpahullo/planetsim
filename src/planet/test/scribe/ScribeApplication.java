package planet.test.scribe;

import planet.commonapi.*;
import planet.scribe.*;

/**
 * @author Ruben Mondejar  <Ruben.Mondejar@estudiants.urv.es>
 */

public class ScribeApplication implements ScribeClient {     

	protected String id = null;
    /**
     * Whether or not this client should accept anycasts
     */
    protected boolean acceptAnycast = false;

    /**
     * Whether this client has had a subscribe fail
     */
    protected boolean subscribeFailed = false;

    /**
     * Constructor.
     */
    public ScribeApplication(String id) {
      this.id = id;
    }
    
    public void acceptAnycast(boolean value) {
      this.acceptAnycast = value;
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param topic DESCRIBE THE PARAMETER
     * @param content DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public boolean anycast(Topic topic, ScribeContent content) {
      if (acceptAnycast)
      	System.out.println(content);

      return acceptAnycast;
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param topic DESCRIBE THE PARAMETER
     * @param content DESCRIBE THE PARAMETER
     */
    public void deliver(Topic topic, ScribeContent content) {
      System.out.println(id+" deliver -->"+content);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param topic DESCRIBE THE PARAMETER
     * @param child DESCRIBE THE PARAMETER
     */
    public void childAdded(Topic topic, NodeHandle child) {
      System.out.println("CHILD ADDED AT " +child.getId() +" --> "+id);
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param topic DESCRIBE THE PARAMETER
     * @param child DESCRIBE THE PARAMETER
     */
    public void childRemoved(Topic topic, NodeHandle child) {
      System.out.println("CHILD REMOVED AT " + id);
    }

    public void subscribeFailed(Topic topic) {
      System.out.println(id+" subscribe failed of " +topic);
      subscribeFailed = true;
    }

    public boolean getSubscribeFailed() {
      return subscribeFailed;
    }
  }