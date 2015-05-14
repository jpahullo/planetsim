package planet.badchord;
import planet.chord.ChordNode;
import planet.chord.message.BroadcastMessage;
import planet.chord.message.IdMessage;
import planet.chord.message.NodeMessage;
import planet.chord.message.SuccListMessage;
import planet.commonapi.EndPoint;
import planet.commonapi.Id;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;
import planet.simulate.Globals;
import planet.simulate.Logger;
import planet.simulate.MessageListener;
import planet.simulate.Results;

/**
 * 
 * A Chord node is single entity in the chord network. It extends of the class
 * Node and specializes following the lookup Chord protocol. Moreover, the
 * stabilization implementation, producing the periodic stabilition events.
 * 
 * @author <a href="mailto:Ruben.Mondejar@estudiants.urv.es">Ruben Mondejar
 *         </a>
 * @author <a href="mailto:cpairot@etse.urv.es">Carles Pairot </a>
 * @author <a href="mailto:jordi.pujol@estudiants.urv.es">Jordi Pujol </a>
 *  
 */ 
public class BadChordNode extends ChordNode {
	
	/**
	 * Constructor, create a new BadChordNode instance with this node Id
	 */
	public BadChordNode() throws InitializationException {
		super();
	}
	
	/**
	 * Treats the messages and according to the case, executes the generic
	 * listeners or listeners specialized, forward the messages or send
	 * responses messages
	 * 
	 * @param msg
	 *            IMessage to treat
	 *  
	 */
	public void dispatcher(RouteMessage msg) {
		//Response message but not successor list type (key == null)
		if (msg.getMode() == REPLY && msg.getKey() != null) {
			String key = msg.getKey();
			try {
				((MessageListener) listeners.get(key)).onMessage(msg);
			} catch (NullPointerException e) {
				Logger.log("I'm [" + id + "]; not exist listener for key ["
						+ msg.getKey() + "]", Logger.ERROR_LOG);
			}
			removeMessageListener(key);
		} else if (msg.getMode() == Globals.ERROR) {
			if (msg.getKey() != null) {
				String key_fp = msg.getKey();
				Logger.log("Node " + this.id + " destroy message key " + key_fp
						+ " type " + Globals.typeToString(msg.getType()) + " content "
						+ msg.getMessage(), Logger.MSG_LOG);
				MessageListener lst = (MessageListener) listeners.get(key_fp);
				if (lst != null) {
					removeMessageListener(key_fp);
				}
			}
			//Successor lost
			if (finger[0] != null && msg.getSource().equals(finger[0])
					&& succList.size() > 0) {
				//if not exists, succ_list is unchanged
				succList.remove(finger[0]);
				if (succList.size() > 0) {
					finger[0] = (NodeHandle) succList.firstElement();
					//send notify
					msg.setSource(this.nodeHandle);
					msg.setDestination(finger[0]);
					msg.setNextHopHandle(finger[0]);
					msg.setType(SET_PRE);
					msg.setMode(REFRESH);
					msg.setMessage(new NodeMessage(nodeHandle));
					sendMessage(msg);
				}
			} else
				//if source not exists, succ_list is unchanged
				succList.remove(msg.getSource());
		} else {
			switch (msg.getType()) {
				//DATA
				case DATA :
					String id_ap = msg.getApplicationId();
					EndPoint endpoint = (EndPoint) endpoints.get(id_ap);

					if (msg.getMode() == REQUEST) {
						boolean forward = endpoint.forward(msg);

						if (forward) {
							if (finger[0] != null
									&& msg.getDestination().getId().betweenE(this.id,
											finger[0].getId())) {
								//found successor --> send data
								msg.setNextHopHandle(finger[0]);
								msg.setType(DATA);
								msg.setMode(REFRESH);
								sendMessage(msg);
								Results.updateHopsMsg(msg.getSource().getId(),
										msg.getKey());
							} else {
								//next node [ROUTING]
								msg.setNextHopHandle(closestPrecedingFinger(
										msg.getDestination().getId()));
								msg.setType(DATA);
								msg.setMode(REQUEST);
								sendMessage(msg);
								Results.updateHopsMsg(msg.getSource().getId(),
										msg.getKey());
							}
						} else {
							Results.decTraffic();
						}
					} else if (msg.getMode() == REFRESH) {
						//	    	Deliver message to End Point
						boolean forward = endpoint.forward(msg);
						if (forward) {
							id_ap = msg.getApplicationId();
							endpoint = (EndPoint) endpoints.get(id_ap);
							endpoint.scheduleMessage(msg, 0);
							Results.decTraffic();
						} else {
							Results.decTraffic();
						}
					} else
						Results.decTraffic();
					break;
				//CONTROL
				//REFRESH
				case SET_SUCC :
					NodeHandle succ = ((NodeMessage) msg.getMessage()).getNode();
					setSucc(succ);
					succList.remove(succ);
					break;
				case SET_PRE :
					setPred(((NodeMessage) msg.getMessage()).getNode());
					break;
				case NOTIFY :
					notify(msg.getSource());
					break;
				case BROADCAST :
					NodeHandle r, new_limit;
				BroadcastMessage bm = (BroadcastMessage) msg.getMessage();
					NodeHandle limit = bm.getLimit();
					Id limitId = limit.getId();
					planet.commonapi.Message info = bm.getInfo();
					
					for (int i = 0; i < bitsPerKey - 1; i++) {
						//Skip a redundant finger
						if (!finger[i].equals(finger[i + 1])) {
							//Forward while within "Limit"
							if (finger[i].getId().between(this.id, limitId)) {
								r = finger[i];
								//New Limit must not exceed Limit
								if (finger[i + 1].getId().between(this.id, limitId)) {
									new_limit = finger[i + 1];
								} else {
									new_limit = limit;
								}
								//no reuse of RouteMessage msg ==> send one
								// message to different nodes ==> requires
								// different messages
								planet.commonapi.RouteMessage aMsg = null;
								try {
									// String appId,Id from, Id to, Id nextHop
									aMsg = getBroadcastMessage(msg
											.getApplicationId(), this.nodeHandle, r, r,
											new BroadcastMessage(info,new_limit));
									sendMessage(aMsg);
								} catch (InitializationException e) {
									Logger.log(
											"ERROR: Cannot get a RouteMessage of MessagePool\n"
											+ e.getMessage(), Logger.ERROR_LOG);
								}
							}
						}
					}
					Logger.log("Broadcast : Node " + this.id + " info : "
							+ info, Logger.EVENT_LOG);
					msg.setMessage(info);
					((EndPoint) endpoints.get(msg.getApplicationId())).scheduleMessage(msg, 0);
					break;
				//REQUEST
				case FIND_SUCC :
					
					/* ***************** CHANGES FOR BAD NODE *************/
					//source --> join();
					/*NodeHandle fSucc = find_successor(msg.getSource());
					if (fSucc != null) {
						msg.setMessage(new NodeMessage(fSucc));
						msg.setDestination(msg.getSource());
						msg.setNextHopHandle(msg.getSource());
						msg.setSource(nodeHandle);
						msg.setMode(REPLY);
						try {
							send(msg);
						} catch (QueueFull e) {
							Logger.log("Outgoing Queue of Node " + this.id
									+ " is Full", Logger.ERROR_LOG);
							MessagePool.freeMessage(msg);
						}
					} else {*/
					
						String key_fp = GenericFactory.generateKey(); //keyGen.generateKey();
						//order of invokes is important!!
						msg.setMessage(new IdMessage(msg.getSource().getId()));
						msg.setDestination(closestPrecedingFinger(msg.getSource().getId()));
						msg.setNextHopHandle(msg.getDestination());
						msg.setSource(this.nodeHandle);
						String oldKey = msg.getKey();
						msg.setKey(key_fp);
						msg.setType(FIND_PRE);
						msg.setMode(REQUEST);
						addMessageListener(key_fp, new FindPredListener(this, oldKey));
						sendMessage(msg);
					//}
					/* END ***************** CHANGES FOR BAD NODE *************/
					break;
				case FIND_PRE :
					if (finger[0] != null
							&& ((IdMessage) msg.getMessage()).getNode().betweenE(
									this.id, finger[0].getId())) {
						//return successor
						Id msgId = ((IdMessage) msg.getMessage()).getNode();
						NodeHandle originalSource = msg.getSource();
						msg.setMessage(new NodeMessage(getSucc()));
						try {
							msg.setSource(GenericFactory.buildNodeHandle(msgId,true));
						} catch (InitializationException e1) {
							e1.printStackTrace();
                            System.exit(-1);
						}
						msg.setDestination(originalSource);
						msg.setNextHopHandle(originalSource);
						msg.setType(FIND_PRE);
						msg.setMode(REPLY);
						sendMessage(msg);
					} else {
						//next node
						Id aux = ((IdMessage) msg.getMessage()).getNode();
						msg.setDestination(closestPrecedingFinger(aux));
						msg.setNextHopHandle(msg.getDestination());
						sendMessage(msg);
					}
					break;
				case GET_PRE :
					//origen --> stabilize();
					msg.setDestination(msg.getSource());
					msg.setNextHopHandle(msg.getSource());
					msg.setMessage(new NodeMessage(predecessor));
					msg.setSource(this.nodeHandle);
					msg.setType(GET_PRE);
					msg.setMode(REPLY);
					sendMessage(msg);
					break;
				case SUCC_LIST :
					if (msg.getMode() == REQUEST) {
						SuccListMessage succs = 
							(SuccListMessage) msg.getMessage();
                        this.sendMessage(msg,msg.getKey(),nodeHandle,msg.getSource(),msg.getSource(),SUCC_LIST,REPLY,new SuccListMessage(succList));
                        /*
						Vector v = succs.getSuccs();
						if (this.nodeHandle.equals(msg.getSource())) {
							if (v.size() > 0) {
								succList = v;
								succList.remove(finger[0]);
							}
						} else {
							v.add(this.nodeHandle);
							if (v.size() > ((ChordProperties)Properties.overlayPropertiesInstance).succListMax
									|| finger[0] == null) {
								msg.setDestination(msg.getSource());
								msg.setSource(this.nodeHandle);
								msg.setMode(REPLY);
							} else {
								msg.setDestination(finger[0]);
								msg.setMode(REQUEST);
							}
							msg.setNextHopHandle(msg.getDestination());
							msg.setType(SUCC_LIST);
							succs.setSuccs(v);
							msg.setMessage(succs);
							sendMessage(msg);
						}*/
					} else if (msg.getMode() == REPLY) {
						SuccListMessage succs = 
							(SuccListMessage) msg.getMessage();
                        succList.clear();
                        succList.add(msg.getSource());
                        succList.addAll(succs.getSuccs());
                        cleanSuccList();
                            
    /*                      hasReceivedSuccList = true;
                            Vector v = succs.getSuccs();
                            if (v.size() > 0)
                            {
                                succList = v;
                            }*/
                            GenericFactory.freeMessage(msg);
                        /*
						Vector v = succs.getSuccs();
						if (v.size() > 0)
							succList = v;*/
					}
					break;
			}
		}
	}
}
