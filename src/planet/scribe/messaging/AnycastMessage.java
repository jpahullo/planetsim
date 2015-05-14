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

package planet.scribe.messaging;

import java.util.*;

import planet.commonapi.*;
import planet.scribe.*;

/**
 * @version $Id: AnycastMessage.java,v 1.6 2003/10/22 03:16:42 amislove Exp $
 * @author Alan Mislove
 */
public class AnycastMessage extends ScribeMessage {

  /**
   * the content of this message
   */
  protected ScribeContent content;

  /**
   * the list of nodes which we have visited
   */
  protected Vector visited;

  /**
   * the list of nodes which we are going to visit
   */
  protected LinkedList toVisit;

  /**
   * @param source The source address
   * @param topic The topic
   * @param content The content
   */
  public AnycastMessage(NodeHandle source, Topic topic, ScribeContent content) {
    super(source, topic);

    this.content = content;
    this.visited = new Vector();
    this.toVisit = new LinkedList();

    addVisited(source);
  }

  /**
   * Returns the content
   *
   * @return The content
   */
  public ScribeContent getContent() {
    return content;
  }

  /**
   * Sets the content
   *
   * @param content The content
   */
  public void setContent(ScribeContent content) {
    this.content = content;
  }

  /**
   * Returns the next handle to visit
   *
   * @return The next handle to visit
   */
  public NodeHandle peekNext() {
    if (toVisit.size() == 0) {
      return null;
    }

    return (NodeHandle) toVisit.getFirst();
  }

  /**
   * Returns the next handle to visit and removes the
   * node from the list.
   *
   * @return The next handle to visit
   */
  public NodeHandle getNext() {
    if (toVisit.size() == 0) {
      return null;
    }
    
    return (NodeHandle) toVisit.removeFirst();
  }

  /**
   * Adds a node to the visited list
   *
   * @param handle The node to add
   */
  public void addVisited(NodeHandle handle) {
    if (handle == null) {
      return;
    }

    if (!visited.contains(handle)) {
      visited.add(handle);
    }

    while (toVisit.remove(handle)) {
      toVisit.remove(handle);
    }
  }

  /**
   * Adds a node the the front of the to-visit list
   *
   * @param handle The handle to add
   */
  public void addFirst(NodeHandle handle) {
    if (handle == null) {
      return;
    }

    if ((!toVisit.contains(handle)) && (!visited.contains(handle))) {
      toVisit.addFirst(handle);
    }
  }

  /**
   * Adds a node the the end of the to-visit list
   *
   * @param handle The handle to add
   */
  public void addLast(NodeHandle handle) {
    if (handle == null) {
      return;
    }

    if ((!toVisit.contains(handle)) && (!visited.contains(handle))) {
      toVisit.addLast(handle);
    }
  }

  /**
   * Removes the node handle from the to visit and visited lists
   *
   * @param handle The handle to remove
   */
  public void remove(NodeHandle handle) {
    if (handle == null) {
      return;
    }

    toVisit.remove(handle);
    visited.remove(handle);
  }
}

