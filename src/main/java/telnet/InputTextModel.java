package telnet;

import com.google.common.eventbus.Subscribe;

import events.EnterPressedEvent;

public class InputTextModel {

	private TelnetLineWriter telnetLineWriter;

	public InputTextModel() {
		this(new TelnetLineWriterSingleton().getInstance());
	}

	InputTextModel(TelnetLineWriter telnetLineWriter) {
		this.telnetLineWriter = telnetLineWriter;
	}

	@Subscribe
	public void onEnterPressed(EnterPressedEvent e) {
		telnetLineWriter.write(e.getText(), (maybeException) -> {
		});
	}

}
