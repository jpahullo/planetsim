package planet.symphony.messages;


import java.util.Set;

import planet.commonapi.Message;

public class QueryJoinSetMessage implements Message {

	private Set pendingJoinMsg;
	
	public QueryJoinSetMessage(Set pendingJoinMsg) {
		this.pendingJoinMsg = pendingJoinMsg;
	}
	
	public void setPendingJoinMsg(Set pendingJoinMsg) {
		this.pendingJoinMsg = pendingJoinMsg;
	}
	
	public Set getPendingJoinMsg() {
		return pendingJoinMsg;
	}
	
	public String toString() {
		return "<pendingJoinMsg "
			 + "/>";
    }

}
