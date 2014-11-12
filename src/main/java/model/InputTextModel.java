package model;

import java.io.IOException;
import java.io.OutputStream;

import com.google.common.eventbus.Subscribe;

import events.EnterPressedEvent;

public class InputTextModel {

	private TelnetClientWrapper telnetClient;
	private OutputStream outputStream;

	public InputTextModel() {
		this(new TelnetClientFactory());
	}

	public InputTextModel(TelnetClientFactory telnetClientFactory) {
		telnetClient = telnetClientFactory.getInstance();
		outputStream = telnetClient.getOutputStream();
	}

	@Subscribe
	public void onEnterPressed(EnterPressedEvent e) {
		try {
			String text = e.getText() + "\n";
			outputStream.write(text.getBytes());
			outputStream.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
