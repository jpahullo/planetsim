package planet.symphony.messages;

import planet.commonapi.Message;
import planet.commonapi.NodeHandle;

public class JoinMessage implements Message {

	NodeHandle newNode;
	NodeHandle bootStrap;
	int retries;
	
	public JoinMessage(NodeHandle newNode, NodeHandle bootStrap) {
		this.newNode = newNode;
		this.bootStrap = bootStrap;
		this.retries = 1;
	}

	public void setNewNode(NodeHandle newNode) {
		this.newNode = newNode;
	}
	
	public NodeHandle getNewNode() {
		return newNode;
	}

	public void setBootStrap(NodeHandle bootStrap) {
		this.bootStrap = bootStrap;
	}
	
	public NodeHandle getBootStrap() {
		return bootStrap;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}
	
	public int getRetries() {
		return retries;
	}

	public String toString() {
		return "<joinMessage "
		     + "newnode=\"" + newNode.toString() + "\" "
			 + "retries=\"" + retries + "\" "
			 + "/>";
    }

}
