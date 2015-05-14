package planet.symphony.messages;

import planet.commonapi.Message;

public class SetSectionSizeSuccMessage implements Message {
	double sectionSizeSucc;
	
	public SetSectionSizeSuccMessage(double sectionSizeSucc) {
		this.sectionSizeSucc = sectionSizeSucc;
	}

	public void setSectionSizeSucc(double sectionSizeSucc) {
		this.sectionSizeSucc = sectionSizeSucc;
	}
	
	public double getSectionSizeSucc() {
		return sectionSizeSucc;
	}
	
	public String toString() {
		return "<setSectionSizeSucc sectionsizesucc=\"" + sectionSizeSucc + "\" />";
    }

}
