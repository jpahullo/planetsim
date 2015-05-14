package planet.symphony.messages;

import java.util.*;

public class SetInfoMessage implements planet.commonapi.Message {

	Collection succSet;
	Collection predSet;
	boolean hasSuccSet, hasPredSet;
	
	public SetInfoMessage() {
		this.succSet = null;
		this.predSet = null;
		this.hasSuccSet = false;
		this.hasPredSet = false;
	}
	
	public void setSuccSet(Collection succSet) {
		this.succSet = succSet;
		this.hasSuccSet = true;
	}
	
	public boolean hasSuccSet() {
		return hasSuccSet;
	}
	
	public Collection getSuccSet() {
		return succSet;
	}
	
	public void setPredSet(Collection predSet) {
		this.predSet = predSet;
		this.hasPredSet = true;
	}
	
	public boolean hasPredSet() {
		return hasPredSet;
	}
	
	public Collection getPredSet() {
		return predSet;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<setInfoMessage ");
		
		sb.append("succSet= \"");
		if (succSet != null)
			sb.append(succSet.toString());
		else
			sb.append("null");
		sb.append(" \" ");
		
		sb.append("predSet= \"");
		if (predSet != null)
			sb.append(predSet.toString());
		else
			sb.append("null");
		sb.append(" \" ");
		
		sb.append("/>");
		
		return sb.toString();
    }

}
