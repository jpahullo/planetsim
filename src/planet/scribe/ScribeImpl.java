/*************************************************************************

"Free Pastry" Peer-to-Peer Application Development Substrate

Copyright 2002, Rice University. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

- Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.

- Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

- Neither  the name  of Rice  University (RICE) nor  the names  of its
contributors may be  used to endorse or promote  products derived from
this software without specific prior written permission.

This software is provided by RICE and the contributors on an "as is"
basis, without any representations or warranties of any kind, express
or implied including, but not limited to, representations or
warranties of non-infringement, merchantability or fitness for a
particular purpose. In no event shall RICE or contributors be liable
for any direct, indirect, incidental, special, exemplary, or
consequential damages (including, but not limited to, procurement of
substitute goods or services; loss of use, data, or profits; or
business interruption) however caused and on any theory of liability,
whether in contract, strict liability, or tort (including negligence
or otherwise) arising in any way out of the use of this software, even
if advised of the possibility of such damage.

********************************************************************************/

package planet.scribe;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import planet.commonapi.Application;
import planet.commonapi.EndPoint;
import planet.commonapi.Id;
import planet.commonapi.Message;
import planet.commonapi.NodeHandle;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;
import planet.scribe.messaging.AnycastMessage;
import planet.scribe.messaging.DropMessage;
import planet.scribe.messaging.PublishMessage;
import planet.scribe.messaging.PublishRequestMessage;
import planet.scribe.messaging.ReplicaSetMessage;
import planet.scribe.messaging.SubscribeAckMessage;
import planet.scribe.messaging.SubscribeFailedMessage;
import planet.scribe.messaging.SubscribeLostMessage;
import planet.scribe.messaging.SubscribeMessage;
import planet.scribe.messaging.UnsubscribeMessage;
import planet.simulate.Logger;

/**
 * The provided implementation of Scribe.
 *
 * @version $Id: ScribeImpl.java,v 1.14 2003/10/22 03:16:40 amislove Exp $
 * @author Alan Mislove
 */
public class ScribeImpl implements Scribe, Application {

  /**
   * the timeout for a subscribe message
   */
  public static long MESSAGE_TIMEOUT = 15000;

  /**
   * the hashtable of topic -> TopicManager
   */
  public Hashtable topics;

  /**
   * this scribe's policy
   */
  protected ScribePolicy policy;

  /**
   * this application's endpoint
   */
  protected EndPoint endpoint;

  /**
   * the logger which we will use
   */
  //protected Logger log = null;

  /**
   * the local node handle
   */
  protected NodeHandle handle;

  /**
   * the hashtable of outstanding messages
   */
  private Hashtable outstanding;

  /**
   * the next unique id
   */
  private int id;
  
  /**
   * the next unique id
   */
  private String appId;

  /**
   * Constructor for Scribe, using the default policy.
   *
   * @param appId Application name
   */
  public ScribeImpl(String appId) {  	
    this(appId,new ScribePolicy.DefaultScribePolicy());
  }

  /**
   * Constructor for Scribe
   *
   * @param appId Application name
   * @param policy The policy for this Scribe
   */
  public ScribeImpl(String appId,ScribePolicy policy) {
    //this.endpoint = node.registerApplication(this, appId);
  	this.appId = appId;
    this.topics = new Hashtable();
    this.outstanding = new Hashtable();
    this.policy = policy;
    //this.handle = endpoint.getLocalNodeHandle();
    this.id = Integer.MIN_VALUE;    
    //log = Logger.getLogger(this.getClass().getName()); 
    //System.out.println("antes -->"+log.getLevel());
    //log.addHandler(new StreamHandler(System.out,new SimpleFormatter()));
    //log.addHandler(new ConsoleHandler());
    //log.setLevel(Level.ALL);
    //System.out.println("despues-->"+log.getLevel());
  }
  
  /**
   * An upcall to inform this Application for a new step.
   * This method is invoked at the end of each simulation step. 
   * Before this, may arrive any number of application Messages,
   * depending on your own main application.
   */
  public void byStep(){}

  public void setEndPoint(EndPoint endpoint) {
  	this.endpoint=endpoint;
  	this.handle = endpoint.getLocalNodeHandle();
  	Logger.log(endpoint.getId() + ": Starting up Scribe",Logger.EVENT_LOG);
  }
  
  /**
   * Returns the current policy for this scribe object
   *
   * @return The current policy for this scribe
   */
  public ScribePolicy getPolicy() {
    return policy;
  }

  /**
   * Sets the current policy for this scribe object
   *
   * @param policy The current policy for this scribe
   */
  public void setPolicy(ScribePolicy policy) {
    this.policy = policy;
  }

  
  /**
   * Returns the Id of the local node
   *
   * @return The Id of the local node
   */
  public Id getNodeId() {
	  return endpoint.getId();
  }

  /**
   * Returns the Id of the application
   *
   * @return The Id of the application
   */	
  public String getId() {
    return appId;
  }
  
  /**
   * Sets the identification of this application.
   * @param appId Identification of this application.
   */
  public void setId(String appId) {
    this.appId = appId;
  }

  /**
   * Returns the list of clients for a given topic
   *
   * @param topic The topic to return the clients of
   * @return The clients of the topic
   */
  public ScribeClient[] getClients(Topic topic) {
    if (topics.get(topic) != null) {
      return ((TopicManager) topics.get(topic)).getClients();
    }

    return new ScribeClient[0];
  }

  /**
   * Returns the list of children for a given topic
   *
   * @param topic The topic to return the children of
   * @return The children of the topic
   */
  public NodeHandle[] getChildren(Topic topic) {
    if (topics.get(topic) != null) {
      return ((TopicManager) topics.get(topic)).getChildren();
    }

    return new NodeHandle[0];
  }

  /**
   * Returns the parent for a given topic
   *
   * @param topic The topic to return the parent of
   * @return The parent of the topic
   */
  public NodeHandle getParent(Topic topic) {
    if (topics.get(topic) != null) {
      return ((TopicManager) topics.get(topic)).getParent();
    }

    return null;
  }

  /**
   * Returns whether or not this Scribe is the root for the given topic
   *
   * @param topic The topic in question
   * @return Whether or not we are currently the root
   */
   public boolean isRoot(Topic topic) {
        Vector set = endpoint.replicaSet(topic.getId(), 1);

        if (set==null)
          return false;
        else
          return ((NodeHandle) set.get(0)).getId().equals(endpoint.getId());    
    
  }
  
  private void isRoot(Topic topic, IsRootListener irl) { 	
  	endpoint.route(topic.getId(),new ReplicaSetMessage(endpoint.getId(),1),null);
  }

  /**
   * Internal method for sending a subscribe message
   *
   * @param Topic topic
   */
  private void sendSubscribe(Topic topic, ScribeClient client, ScribeContent content) {
    sendSubscribe(topic, client, content, null);
  }

  /**
   * Internal method for sending a subscribe message
   *
   * @param Topic topic
   */
  private void sendSubscribe(Topic topic, ScribeClient client, ScribeContent content, Id previousParent) {
    id++;

    Logger.log(endpoint.getId() + ": Sending subscribe message for topic " + topic,Logger.EVENT_LOG);

    if (client != null)
      outstanding.put(new Integer(id), client);

    endpoint.route(topic.getId(), new SubscribeMessage(handle, topic, previousParent, id, content), null);
  }

  /**
   * Internal method which processes an ack message
   *
   * @param message The ackMessage
   */
  private void ackMessageReceived(SubscribeAckMessage message) {
    outstanding.remove(new Integer(message.getId()));
  }

  /**
   * Internal method which processes a subscribe failed message
   *
   * @param message THe lost message
   */
  private void failedMessageReceived(SubscribeFailedMessage message) {   
    ScribeClient client = (ScribeClient) outstanding.remove(new Integer(message.getId()));

    if (client != null)
      client.subscribeFailed(message.getTopic());
  }

  /**
   * Internal method which processes a subscribe lost message
   *
   * @param message THe lost message
   */
  private void lostMessageReceived(SubscribeLostMessage message) {
    ScribeClient client = (ScribeClient) outstanding.remove(new Integer(message.getId()));

    if (client != null)
      client.subscribeFailed(message.getTopic());
  }

  // ----- SCRIBE METHODS -----

  /**
   * Subscribes the given client to the provided topic. Any message published to the topic will be
   * delivered to the Client via the deliver() method.
   *
   * @param topic The topic to subscribe to
   * @param client The client to give messages to
   */
  public void subscribe(Topic topic, ScribeClient client) {
    subscribe(topic, client, null);
  }

  /**
   * Subscribes the given client to the provided topic. Any message published to the topic will be
   * delivered to the Client via the deliver() method.
   *
   * @param topic The topic to subscribe to
   * @param client The client to give messages to
   */
  public void subscribe(Topic topic, ScribeClient client, ScribeContent content) {
    Logger.log(endpoint.getId() + ": Subscribing client " + client + " to topic " + topic,Logger.EVENT_LOG);
  	//System.out.println(endpoint.getId() + ": Subscribing client " + client + " to topic " + topic);

    // if we don't know about this topic, subscribe
    // otherwise, we simply add the client to the list
    if (topics.get(topic) == null) {
      topics.put(topic, new TopicManager(topic, client));

      sendSubscribe(topic, client, content);
    } else {
      TopicManager manager = (TopicManager) topics.get(topic);
      manager.addClient(client);

      if ((manager.getParent() == null) && (! isRoot(topic))) {
        sendSubscribe(topic, client, content);
      }
    }
  }

  /**
   * Unsubscribes the given client from the provided topic.
   *
   * @param topic The topic to unsubscribe from
   * @param client The client to unsubscribe
   */
  public void unsubscribe(Topic topic, ScribeClient client) {
    Logger.log(endpoint.getId() + ": Unsubscribing client " + client + " from topic " + topic,Logger.EVENT_LOG);

    if (topics.get(topic) != null) {
      TopicManager manager = (TopicManager) topics.get(topic);

      // if this is the last client and there are no children,
      // then we unsubscribe from the topic
      if (manager.removeClient(client)) {
        topics.remove(topic);
        NodeHandle parent = manager.getParent();

        if (parent != null) {
          endpoint.route(parent.getId(), new UnsubscribeMessage(handle, topic), parent);
        }
      }
    } else {
    	Logger.log(endpoint.getId() + ": Attempt to unsubscribe client " + client + " from unknown topic " + topic,Logger.EVENT_LOG);
    }
  }

  /**
   * Publishes the given message to the topic.
   *
   * @param topic The topic to publish to
   * @param content The content to publish
   */
  public void publish(Topic topic, ScribeContent content) {
    Logger.log(endpoint.getId() + ": Publishing content " + content + " to topic " + topic,Logger.EVENT_LOG);

    endpoint.route(topic.getId(), new PublishRequestMessage(handle, topic, content), null);
  }

  /**
   * Anycasts the given content to a member of the given topic
   *
   * @param topic The topic to anycast to
   * @param content The content to anycast
   */
  public void anycast(Topic topic, ScribeContent content) {
    Logger.log(endpoint.getId() + ": Anycasting content " + content + " to topic " + topic,Logger.EVENT_LOG);

    endpoint.route(topic.getId(), new AnycastMessage(handle, topic, content), null);
  }

  /**
   * Adds a child to the given topic
   *
   * @param topic The topic to add the child to
   * @param child The child to add
   */
  public void addChild(Topic topic, NodeHandle child) {
    Logger.log(endpoint.getId() + ": Adding child " + child + " to topic " + topic,Logger.EVENT_LOG);
    TopicManager manager = (TopicManager) topics.get(topic);

    // if we don't know about the topic, we subscribe, otherwise,
    // we simply add the child to the list
    if (manager == null) {
      manager = new TopicManager(topic, child);
      topics.put(topic, manager);

      Logger.log(endpoint.getId() + ": Implicitly subscribing to topic " + topic,Logger.EVENT_LOG);
      sendSubscribe(topic, null, null);
    } else {
      manager.addChild(child);
    }

    // we send a confirmation back to the child
    endpoint.route(child.getId(), new SubscribeAckMessage(handle, topic, manager.getPathToRoot(), Integer.MAX_VALUE), child);

    // and lastly notify all of the clients
    ScribeClient[] clients = manager.getClients();

    for (int i = 0; i < clients.length; i++) {
      clients[i].childAdded(topic, child);
    }
  }

  /**
   * Removes a child from the given topic
   *
   * @param topic The topic to remove the child from
   * @param child The child to remove
   */
  public void removeChild(Topic topic, NodeHandle child) {
    removeChild(topic, child, true);
  }

  /**
   * Removes a child from the given topic
   *
   * @param topic The topic to remove the child from
   * @param child The child to remove
   * @param sendDrop Whether or not to send a drop message to the chil
   */
  protected void removeChild(Topic topic, NodeHandle child, boolean sendDrop) {
    Logger.log(endpoint.getId() + ": Removing child " + child + " from topic " + topic,Logger.EVENT_LOG);

    if (topics.get(topic) != null) {
      TopicManager manager = (TopicManager) topics.get(topic);

      // if this is the last child and there are no clients, then
      // we unsubscribe, if we are not the root
      if (manager.removeChild(child)) {
        topics.remove(topic);
        NodeHandle parent = manager.getParent();
        Logger.log(endpoint.getId() + ": We no longer need topic " + topic + " - unsubscribing from parent " + parent,Logger.EVENT_LOG);

        if (parent != null) {
          endpoint.route(parent.getId(), new UnsubscribeMessage(handle, topic), parent);
        }
      }

      if ((sendDrop) && (child.isAlive())) {
        Logger.log(endpoint.getId() + ": Informing child " + child + " that he has been dropped from topic " + topic,Logger.EVENT_LOG);
        
        // now, we tell the child that he has been dropped
        endpoint.route(child.getId(), new DropMessage(handle, topic), child);
      }

      // and lastly notify all of the clients
      ScribeClient[] clients = manager.getClients();

      for (int i = 0; i < clients.length; i++) {
        clients[i].childRemoved(topic, child);
      }
    } else {
      Logger.log(endpoint.getId() + ": Unexpected attempt to remove child " + child + " from unknown topic " + topic,Logger.EVENT_LOG);
    }
  }


  // ----- COMMON API METHODS -----

  /**
   * This method is invoked on applications when the underlying node is about to forward the given
   * message with the provided target to the specified next hop. Applications can change the
   * contents of the message, specify a different nextHop (through re-routing), or completely
   * terminate the message.
   *
   * @param message The message being sent, containing an internal message along with a destination
   *      key and nodeHandle next hop.
   * @return Whether or not to forward the message further
   */
  public boolean forward(final Message message) {  	
  	//System.out.println(endpoint.getId()+" forward "+message); 
    Logger.log(endpoint.getId() + ": Forward called with " + message,Logger.EVENT_LOG);
    
    
    if (message instanceof AnycastMessage) {
      AnycastMessage aMessage = (AnycastMessage) message;           
      
      // get the topic manager associated with this topic
      TopicManager manager = (TopicManager) topics.get(aMessage.getTopic());

      // if it's a subscribe message, we must handle it differently
      if (message instanceof SubscribeMessage) {
        SubscribeMessage sMessage = (SubscribeMessage) message;       
        
        
        // if this is our own subscribe message, ignore it
        if (sMessage.getSource().getId().equals(endpoint.getId())) {
          return true;
        }

        if (manager != null) {
          // first, we have to make sure that we don't create a loop, which would occur
          // if the subcribing node's previous parent is on our path to the root
          Id previousParent = sMessage.getPreviousParent();
          List path = Arrays.asList(manager.getPathToRoot());

          if (path.contains(previousParent)) {
            Logger.log(endpoint.getId() + ": Rejecting subscribe message from " +
          	//  System.out.println(endpoint.getId() + ": Rejecting subscribe message from " +
                      sMessage.getSubscriber() + " for topic " + sMessage.getTopic() +
                      " because we are on the subscriber's path to the root.",Logger.EVENT_LOG);
            return true;
          }
        }
          
        ScribeClient[] clients = new ScribeClient[0];
        NodeHandle[] handles = new NodeHandle[0];

        if (manager != null) {
          clients = manager.getClients();
          handles = manager.getChildren();
        }

        // check if child is already there
        if (Arrays.asList(handles).contains(sMessage.getSubscriber())){
          return false;
        }

        // see if the policy will allow us to take on this child
        if (policy.allowSubscribe(sMessage, clients, handles)) {
          Logger.log(endpoint.getId() + ": Hijacking subscribe message from " +
        	sMessage.getSubscriber() + " for topic " + sMessage.getTopic(),Logger.EVENT_LOG);
        //	 System.out.println(endpoint.getId() + ": Hijacking subscribe message from " +
        //    sMessage.getSubscriber().getId() + " for topic " + sMessage.getTopic());

          // if so, add the child
          addChild(sMessage.getTopic(), sMessage.getSubscriber());
          return false;
        }

        // otherwise, we are effectively rejecting the child
        Logger.log(endpoint.getId() + ": Rejecting subscribe message from " +
        //System.out.println(endpoint.getId() + ": Rejecting subscribe message from " +
        sMessage.getSubscriber() + " for topic " + sMessage.getTopic(),Logger.EVENT_LOG);

        // if we are not associated with this topic at all, we simply let the subscribe go
        // closer to the root
        if (manager == null) {
          return true;
        }
      } else {
        // if we are not associated with this topic at all, let the
        // anycast continue
        if (manager == null) {
          return true;
        }

        ScribeClient[] clients = manager.getClients();

        // see if one of our clients will accept the anycast
        for (int i = 0; i < clients.length; i++) {
          if (clients[i].anycast(aMessage.getTopic(), aMessage.getContent())) {
            Logger.log(endpoint.getId() + ": Accepting anycast message from " +
              aMessage.getSource() + " for topic " + aMessage.getTopic(),Logger.EVENT_LOG);

            return false;
          }
        }

        // if we are the orginator for this anycast and it already has a destination,
        // we let it go ahead
        if (aMessage.getSource().getId().equals(endpoint.getId())
          && ( aMessage.getNext() != null) &&  (! handle.equals(aMessage.getNext()))) { 	
          //&& ( message.getNextHopHandle() != null) &&  (! handle.equals(message.getNextHopHandle()))) 
                   
          return true;
        }

        Logger.log(endpoint.getId() + ": Rejecting anycast message from " +
          aMessage.getSource() + " for topic " + aMessage.getTopic(),Logger.EVENT_LOG);
      }

      // add the local node to the visited list
      aMessage.addVisited(endpoint.getLocalNodeHandle());

      // allow the policy to select the order in which the nodes are visited
      policy.directAnycast(aMessage, manager.getParent(), manager.getChildren());

      // reset the source of the message to be us
      aMessage.setSource(endpoint.getLocalNodeHandle());

      // get the next hop
      NodeHandle handle = aMessage.getNext();

      // make sure that the next node is alive
      while ((handle != null) && (!handle.isAlive())) {
        handle = aMessage.getNext();
      }

      Logger.log(endpoint.getId() + ": Forwarding anycast message for topic " + aMessage.getTopic() + "on to " + handle,Logger.EVENT_LOG);

      if (handle == null) {
        Logger.log(endpoint.getId() + ": Anycast " + aMessage + " failed.",Logger.EVENT_LOG);

        // if it's a subscribe message, send a subscribe failed message back
        // as a courtesy
        if (aMessage instanceof SubscribeMessage) {
          SubscribeMessage sMessage = (SubscribeMessage) aMessage;          
          Logger.log(endpoint.getId() + ": Sending SubscribeFailedMessage to " + sMessage.getSubscriber(),Logger.EVENT_LOG);

          endpoint.route(sMessage.getSubscriber().getId(),
                         new SubscribeFailedMessage(handle, sMessage.getTopic(), sMessage.getId()),
                         sMessage.getSubscriber());
        }
      } else {
        endpoint.route(handle.getId(), aMessage, handle);          
      }

      return false;
    }
    //else
      //System.out.println(endpoint.getId()+" forward "+message); 
        
    

    return true;
  }

  /**
   * This method is called on the application at the destination node for the given id.
   *
   * @param id The destination id of the message
   * @param message The message being sent
   */
  public void deliver(Id id, Message message)  {
  	//System.out.println(endpoint.getId()+" deliver "+message);
    Logger.log(endpoint.getId() + ": Deliver called with " + id + " " + message,Logger.EVENT_LOG);
    
    if (message instanceof AnycastMessage) {
      AnycastMessage aMessage = (AnycastMessage) message;
      
      // if we are the recipient to someone else's subscribe, then we should have processed
      // this message in the forward() method.
      // Otherwise, we received our own subscribe message, which means that we are
      // the root
      if (aMessage.getSource().getId().equals(endpoint.getId())) {
        if (aMessage instanceof SubscribeMessage) {
          SubscribeMessage sMessage = (SubscribeMessage) message;
         // System.out.println(endpoint.getId()+" deliver "+sMessage);
          
          outstanding.remove(new Integer(sMessage.getId()));
          Logger.log(endpoint.getId() + ": Received our own subscribe message " + aMessage + " for topic " +
            aMessage.getTopic() + " - we are the root.",Logger.EVENT_LOG);
        } else {
          Logger.log(endpoint.getId() + ": Received unexpected delivered anycast message " + aMessage + " for topic " +
            aMessage.getTopic() + " - was generated by us.",Logger.EVENT_LOG);
        }
      } else {
        // here, we have had a subscribe message delivered, which means that we are the root, but
        // our policy says that we cannot take on this child
        if (aMessage instanceof SubscribeMessage) {
          SubscribeMessage sMessage = (SubscribeMessage) aMessage;
          Logger.log(endpoint.getId() + ": Sending SubscribeFailedMessage (at root) to " + sMessage.getSubscriber(),Logger.EVENT_LOG);

          endpoint.route(sMessage.getSubscriber().getId(),
                         new SubscribeFailedMessage(handle, sMessage.getTopic(), sMessage.getId()),
                         sMessage.getSubscriber());
        } else {
          Logger.log(endpoint.getId() + ": Received unexpected delivered anycast message " + aMessage + " for topic " +
                      aMessage.getTopic() + " - not generated by us, but was expected to be.",Logger.EVENT_LOG);
        }
      }
    } else if (message instanceof SubscribeAckMessage) {
    	//System.out.println(endpoint.getId()+" deliver "+message); 
      SubscribeAckMessage saMessage = (SubscribeAckMessage) message;
      TopicManager manager = (TopicManager) topics.get(saMessage.getTopic());

      ackMessageReceived(saMessage);

      Logger.log(endpoint.getId() + ": Received subscribe ack message from " + saMessage.getSource() + " for topic " + saMessage.getTopic(),Logger.EVENT_LOG);

      if (! saMessage.getSource().isAlive()) {
        Logger.log(endpoint.getId() + ": Received subscribe ack message from " + saMessage.getSource() + " for topic " + saMessage.getTopic(),Logger.EVENT_LOG);
        (new Exception()).printStackTrace();
        System.exit(-1);
      }
        
      
      // if we're the root, reject the ack message
      if (isRoot(saMessage.getTopic())) {
        Logger.log(endpoint.getId() + ": Received unexpected subscribe ack message (we are the root) from " +
                 saMessage.getSource() + " for topic " + saMessage.getTopic(),Logger.EVENT_LOG);
        endpoint.route(saMessage.getSource().getId(), new UnsubscribeMessage(handle, saMessage.getTopic()), saMessage.getSource());
      } else {
        // if we don't know about this topic, then we unsubscribe
        // if we already have a parent, then this is either an errorous
        // subscribe ack, or our path to the root has changed.
        if (manager != null) {
          if (manager.getParent() == null) {
            manager.setParent(saMessage.getSource());
          }

          if (manager.getParent().equals(saMessage.getSource())) {
            manager.setPathToRoot(saMessage.getPathToRoot());
          } else {
            Logger.log(endpoint.getId() + ": Received unexpected subscribe ack message (already have parent " + manager.getParent() +
                        ") from " + saMessage.getSource() + " for topic " + saMessage.getTopic(),Logger.EVENT_LOG);
            endpoint.route(saMessage.getSource().getId(), new UnsubscribeMessage(handle, saMessage.getTopic()), saMessage.getSource());
          }
        } else {
          Logger.log(endpoint.getId() + ": Received unexpected subscribe ack message from " +
                      saMessage.getSource() + " for unknown topic " + saMessage.getTopic(),Logger.EVENT_LOG);
          endpoint.route(saMessage.getSource().getId(), new UnsubscribeMessage(handle, saMessage.getTopic()), saMessage.getSource());
        }
      }
    } else if (message instanceof SubscribeLostMessage) {
      SubscribeLostMessage slMessage = (SubscribeLostMessage) message;

      lostMessageReceived(slMessage);
    } else if (message instanceof SubscribeFailedMessage) {
      SubscribeFailedMessage sfMessage = (SubscribeFailedMessage) message;

      failedMessageReceived(sfMessage);
    } else if (message instanceof PublishRequestMessage) {
      PublishRequestMessage prMessage = (PublishRequestMessage) message;
      TopicManager manager = (TopicManager) topics.get(prMessage.getTopic());

      Logger.log(endpoint.getId() + ": Received publish request message with data " +
        prMessage.getContent() + " for topic " + prMessage.getTopic(),Logger.EVENT_LOG);

      // if message is for a non-existant topic, drop it on the floor (we are the root, after all)
      // otherwise, turn it into a publish message, and forward it on
      if (manager == null) {
        Logger.log(endpoint.getId() + ": Received publish request message for non-existent topic " +
          prMessage.getTopic() + " - dropping on floor.",Logger.EVENT_LOG);
      } else {
        deliver(prMessage.getTopic().getId(), new PublishMessage(prMessage.getSource(), prMessage.getTopic(), prMessage.getContent()));
      }
    } else if (message instanceof PublishMessage) {
      PublishMessage pMessage = (PublishMessage) message;
      TopicManager manager = (TopicManager) topics.get(pMessage.getTopic());

      Logger.log(endpoint.getId() + ": Received publish message with data " + pMessage.getContent() + " for topic " + pMessage.getTopic(),Logger.EVENT_LOG);

      // if we don't know about this topic, send an unsubscribe message
      // otherwise, we deliver the message to all clients and forward the
      // message to all children
      if (manager != null) {
        pMessage.setSource(handle);

        ScribeClient[] clients = manager.getClients();

        for (int i = 0; i < clients.length; i++) {
          Logger.log(endpoint.getId() + ": Delivering publish message with data " + pMessage.getContent() + " for topic " +
            pMessage.getTopic() + " to client " + clients[i],Logger.EVENT_LOG);
          clients[i].deliver(pMessage.getTopic(), pMessage.getContent());
        }

        NodeHandle[] handles = manager.getChildren();

        for (int i = 0; i < handles.length; i++) {
          Logger.log(endpoint.getId() + ": Forwarding publish message with data " + pMessage.getContent() + " for topic " +
            pMessage.getTopic() + " to child " + handles[i],Logger.EVENT_LOG);
          endpoint.route(handles[i].getId(), pMessage, handles[i]);
        }
      } else {
        Logger.log(endpoint.getId() + ": Received unexpected publish message from " +
          pMessage.getSource() + " for unknown topic " + pMessage.getTopic(),Logger.EVENT_LOG);

        endpoint.route(pMessage.getSource().getId(), new UnsubscribeMessage(handle, pMessage.getTopic()), pMessage.getSource());
      }
    } else if (message instanceof UnsubscribeMessage) {
      UnsubscribeMessage uMessage = (UnsubscribeMessage) message;
      Logger.log(endpoint.getId() + ": Received unsubscribe message from " +
        uMessage.getSource() + " for topic " + uMessage.getTopic(),Logger.EVENT_LOG);

      removeChild(uMessage.getTopic(), uMessage.getSource(), false);
    } else if (message instanceof DropMessage) {
      DropMessage dMessage = (DropMessage) message;
      Logger.log(endpoint.getId() + ": Received drop message from " + dMessage.getSource() + " for topic " + dMessage.getTopic(),Logger.EVENT_LOG);
      
      TopicManager manager = (TopicManager) topics.get(dMessage.getTopic());

      if (manager != null) {
        if ((manager.getParent() != null) && manager.getParent().equals(dMessage.getSource())) {
          // we set the parent to be null, and then send out another subscribe message
          manager.setParent(null);
          ScribeClient[] clients = manager.getClients();

          if (clients.length > 0)
            sendSubscribe(dMessage.getTopic(), clients[0], null);
          else
            sendSubscribe(dMessage.getTopic(), null, null);
        } else {
          Logger.log(endpoint.getId() + ": Received unexpected drop message from non-parent " +
                      dMessage.getSource() + " for topic " + dMessage.getTopic() + " - ignoring",Logger.EVENT_LOG);
        }
      } else {
        Logger.log(endpoint.getId() + ": Received unexpected drop message from " +
                    dMessage.getSource() + " for unknown topic " + dMessage.getTopic() + " - ignoring",Logger.EVENT_LOG);
      }
    } else if (message instanceof ReplicaSetMessage) {
    	
    	ReplicaSetMessage mrs = (ReplicaSetMessage) message;
       if (mrs.isRequest()){
       	  int max = mrs.getMaxRank();
       	  Id sourceId = mrs.getMessageKey();
       	  mrs = new ReplicaSetMessage(endpoint.getId(),endpoint.replicaSet(endpoint.getId(),max));
    	  try {
    	  		endpoint.route(endpoint.getId(),mrs,GenericFactory.buildNodeHandle(sourceId,true));
    	  } catch (InitializationException e) {
    	  		e.printStackTrace();
    	  }
       }
       else {
         mrs.getReplicaSet();
       }
    
    } else {
      Logger.log(endpoint.getId() + ": Received unknown message " + message + " - dropping on floor.",Logger.EVENT_LOG);
    }
  }

  /**
   * This method is invoked to inform the application that the given node has either joined or left
   * the neighbor set of the local node, as the set would be returned by the neighborSet call.
   *
   * @param handle The handle that has joined/left
   * @param joined Whether the node has joined or left
   */
  public void update(NodeHandle handle, boolean joined) {
    Set set = topics.keySet();
    Iterator e = set.iterator();
    TopicManager manager;
    Topic topic;

    while(e.hasNext()){
      topic = (Topic)e.next();
      manager = (TopicManager)topics.get(topic);

      if (joined){
        // check if new guy is root, we were old root, then subscribe
        if (manager.getParent() == null){
          // send subscribe message
          sendSubscribe(topic, null, null);
        }
      } else {
        if (isRoot(topic) && (manager.getParent() != null)) {
          endpoint.route(manager.getParent().getId(), new UnsubscribeMessage(handle, topic), manager.getParent());
          manager.setParent(null);
        }
      }
    }    
  }

  /**
   * Sets the name for this applicaton.
   * @param applicationName Name for this application.
   * @return The same instance, once it has been updated.
   */
  public Application setValues(String applicationName)
  {
      this.appId = applicationName;
      return this;
  }

  
  /**
   * Class which keeps track of a given topic
   *
   * @version $Id: ScribeImpl.java,v 1.14 2003/10/22 03:16:40 amislove Exp $
   * @author amislove
   */
  public class TopicManager implements Observer {

    /**
     * DESCRIBE THE FIELD
     */
    protected Topic topic;

    /**
     * The current path to the root for this node
     */
    protected Id[] pathToRoot;

    /**
     * DESCRIBE THE FIELD
     */
    protected Vector clients;

    /**
     * DESCRIBE THE FIELD
     */
    protected Vector children;

    /**
     * DESCRIBE THE FIELD
     */
    protected NodeHandle parent;

    /**
     * Constructor for TopicManager.
     *
     * @param topic DESCRIBE THE PARAMETER
     * @param client DESCRIBE THE PARAMETER
     */
    public TopicManager(Topic topic, ScribeClient client) {
      this(topic);

      addClient(client);
    }

    /**
     * Constructor for TopicManager.
     *
     * @param topic DESCRIBE THE PARAMETER
     * @param child DESCRIBE THE PARAMETER
     */
    public TopicManager(Topic topic, NodeHandle child) {
      this(topic);

      addChild(child);
    }

    /**
     * Constructor for TopicManager.
     *
     * @param topic DESCRIBE THE PARAMETER
     */
    protected TopicManager(Topic topic) {
      this.topic = topic;
      this.clients = new Vector();
      this.children = new Vector();

      setPathToRoot(new Id[0]);
    }

    /**
     * Gets the Parent attribute of the TopicManager object
     *
     * @return The Parent value
     */
    public NodeHandle getParent() {
      return parent;
    }

    /**
     * Gets the Clients attribute of the TopicManager object
     *
     * @return The Clients value
     */
    public ScribeClient[] getClients() {
      return (ScribeClient[]) clients.toArray(new ScribeClient[0]);
    }

    /**
     * Gets the Children attribute of the TopicManager object
     *
     * @return The Children value
     */
    public NodeHandle[] getChildren() {
      return (NodeHandle[]) children.toArray(new NodeHandle[0]);
    }

    /**
     * Gets the PathToRoot attribute of the TopicManager object
     *
     * @return The PathToRoot value
     */
    public Id[] getPathToRoot() {
      return pathToRoot;
    }

    /**
     * Sets the PathToRoot attribute of the TopicManager object
     *
     * @param pathToRoot The new PathToRoot value
     */
    public void setPathToRoot(Id[] pathToRoot) {
      // build the path to the root for the new node
      this.pathToRoot = new Id[pathToRoot.length + 1];
      System.arraycopy(pathToRoot, 0, this.pathToRoot, 0, pathToRoot.length);
      this.pathToRoot[pathToRoot.length] = endpoint.getId();

      // now send the information out to our children
      NodeHandle[] children = getChildren();
      for (int i=0; i<children.length; i++) {
        if (Arrays.asList(this.pathToRoot).contains(children[i].getId())) {
          endpoint.route(children[i].getId(), new DropMessage(handle, topic), children[i]);
          removeChild(children[i]);
        } else {
          endpoint.route(children[i].getId(), new SubscribeAckMessage(handle, topic, getPathToRoot(), Integer.MAX_VALUE), children[i]);
        }
      }
    }

    /**
     * Sets the Parent attribute of the TopicManager object
     *
     * @param handle The new Parent value
     */
    public void setParent(NodeHandle handle) {
      if ((handle != null) && (parent != null)) {
        Logger.log(endpoint.getId() + ": Unexpectedly changing parents for topic " + topic,Logger.EVENT_LOG);
      }

      if (parent != null) {
        parent.deleteObserver(this);
      }

      parent = handle;
      setPathToRoot(new Id[0]);

      if ((parent != null) && parent.isAlive()) {
        parent.addObserver(this);
      }
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param o DESCRIBE THE PARAMETER
     * @param arg DESCRIBE THE PARAMETER
     */
    public void update(Observable o, Object arg) {
      if (arg.equals(NodeHandle.DECLARED_DEAD)) {
        if (children.contains(o)) {
          Logger.log(endpoint.getId() + ": Child " + o + " for topic " + topic + " has died - removing.",Logger.EVENT_LOG);

          ScribeImpl.this.removeChild(topic, (NodeHandle) o);
        } else if (o.equals(parent)) {
          // if our parent has died, then we must resubscribe to the topic
          Logger.log(endpoint.getId() + ": Parent " + parent + " for topic " + topic + " has died - resubscribing.",Logger.EVENT_LOG);
          
          setParent(null);

          if (clients.size() > 0)
            sendSubscribe(topic, (ScribeClient) clients.elementAt(0), null, ((NodeHandle) o).getId());
          else
            sendSubscribe(topic, null, null, ((NodeHandle) o).getId());
        } else {
          Logger.log(endpoint.getId() + ": Received unexpected update from " + o,Logger.EVENT_LOG);
          o.deleteObserver(this);
        }
      }
    }

    /**
     * Adds a feature to the Client attribute of the TopicManager object
     *
     * @param client The feature to be added to the Client attribute
     */
    public void addClient(ScribeClient client) {
      if (!clients.contains(client)) {
        clients.add(client);
      }
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param client DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public boolean removeClient(ScribeClient client) {
      clients.remove(client);

      boolean unsub = ((clients.size() == 0) && (children.size() == 0));

      // if we're going to unsubscribe, then we remove ourself as
      // as observer
      if (unsub && (parent != null)) {
        parent.deleteObserver(this);
      }

      return unsub;
    }

    /**
     * Adds a feature to the Child attribute of the TopicManager object
     *
     * @param child The feature to be added to the Child attribute
     */
    public void addChild(NodeHandle child) {
      if ((!children.contains(child)) && child.isAlive()) {
        children.add(child);
        child.addObserver(this);
      }
    }

    /**
     * DESCRIBE THE METHOD
     *
     * @param child DESCRIBE THE PARAMETER
     * @return DESCRIBE THE RETURN VALUE
     */
    public boolean removeChild(NodeHandle child) {
      children.remove(child);
      child.deleteObserver(this);

      boolean unsub = ((clients.size() == 0) && (children.size() == 0));

      // if we're going to unsubscribe, then we remove ourself as
      // as observer
      if (unsub && (parent != null)) {
        parent.deleteObserver(this);
      }

      return unsub;
    }
  }
}
