/*************************************************************************

"FreePastry" Peer-to-Peer Application Development Substrate

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

import planet.commonapi.*;
import planet.scribe.*;

/**
 * This class the abstraction of a message used internally by Scribe.
 *
 * @version $Id: ScribeMessage.java,v 1.1 2003/09/24 02:20:08 amislove Exp $
 *
 * @author Alan Mislove
 */
public abstract class ScribeMessage implements Message {

  // the source of this message
  protected NodeHandle source;

  // the topic of this message
  protected Topic topic;

  /**
   * @param source The source address
   * @param topic The topic
   */
  protected ScribeMessage(NodeHandle source, Topic topic) {
    this.source = source;
    this.topic = topic;
  }

  /**
   * Method which returns this messages' source address
   *
   * @return The source of this message
   */
  public NodeHandle getSource() {
    return source;
  }
  
  /**
   * Method which set this messages' source address
   *
   * @param source The source of this message
   */
  public void setSource(NodeHandle source) {
    this.source = source;
  }

  /**
   * Method which returns this messages' topic
   *
   * @return The topic of this message
   */
  public Topic getTopic() {
    return topic;
  }
  
}

