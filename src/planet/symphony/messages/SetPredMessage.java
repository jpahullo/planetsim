package planet.symphony.messages;

import planet.commonapi.Id;
import planet.commonapi.Message;

public class SetPredMessage implements Message {

	Id predId;
	
	public SetPredMessage(Id predId) {
		this.predId = predId;
	}

	public void setPredId(Id predId) {
		this.predId = predId;
	}
	
	public Id getPredId() {
		return predId;
	}
	
	public String toString() {
		return "<SetPredMessage predid=\"" + predId.toString() + "\" />";
    }

}
