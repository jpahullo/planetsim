package planet.symphony.messages;

import planet.commonapi.Id;
import planet.commonapi.Message;

public class SetSuccMessage implements Message {

	Id succId;
	
	public SetSuccMessage(Id succId) {
		this.succId = succId;
	}

	public void setSuccId(Id succId) {
		this.succId = succId;
	}
	
	public Id getSuccId() {
		return succId;
	}
	
	public String toString() {
		return "<SetSuccMessage succid=\"" + succId.toString() + "\" />";
    }

}
