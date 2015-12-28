package events;

public class EnterPressedEvent {

	private String text;

	public EnterPressedEvent(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
