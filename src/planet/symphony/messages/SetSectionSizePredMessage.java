package planet.symphony.messages;

import planet.commonapi.Message;

public class SetSectionSizePredMessage implements Message {
	double sectionSizePred;
	
	public SetSectionSizePredMessage(double sectionSizePred) {
		this.sectionSizePred = sectionSizePred;
	}

	public void setSectionSizePred(double sectionSizePred) {
		this.sectionSizePred = sectionSizePred;
	}
	
	public double getSectionSizePred() {
		return sectionSizePred;
	}
	
	public String toString() {
		return "<setSectionSizePred sectionsizepred=\"" + sectionSizePred + "\" />";
    }

}
