package planet.symphony.messages;

import planet.commonapi.Message;
import planet.commonapi.NodeHandle;

public class NewSuccInListMessage implements Message {

	NodeHandle newNode;
	int curF;
	
	public NewSuccInListMessage(NodeHandle newNode, int F) {
		this.newNode = newNode;
		this.curF = F;
	}
	
	public void setNewNodeId(NodeHandle newNode) {
		this.newNode = newNode;
	}
	
	public NodeHandle getNewNode() {
		return newNode;
	}
	
	public int decF() {
		curF--;
		return curF;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<NewSuccInListMessage ");
		sb.append("newNodeId=\"");
		if (newNode != null)
			sb.append(newNode.getId().toString());
		else
			sb.append("null");
		sb.append("\" ");
		sb.append("/>");
		
		return sb.toString();
	}
	
}
