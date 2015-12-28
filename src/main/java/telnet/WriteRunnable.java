package telnet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;

public class WriteRunnable implements Runnable {

	private String stringToWrite;
	private IWriteCallback callback;
	private TelnetClientWrapper telnetClient;

	public WriteRunnable(String stringToWrite, IWriteCallback callback, TelnetClientWrapper telnetClient) {
		this.stringToWrite = stringToWrite;
		this.callback = callback;
		this.telnetClient = telnetClient;
	}

	@Override
	public void run() {
		try {
			String actualStringToWrite = stringToWrite + "\n";
			WritableByteChannel channel = getTelnetClient().getOutputChannel();
			channel.write(ByteBuffer.wrap(actualStringToWrite.getBytes()));
			getTelnetClient().getOutputStream().flush();
			callback.call(Optional.empty());
		} catch (NonWritableChannelException e) {
			callback.call(Optional.of(e));
		} catch (IOException e) {
			callback.call(Optional.of(e));
		}
	}

	public String getStringToWrite() {
		return stringToWrite;
	}

	public IWriteCallback getCallback() {
		return callback;
	}

	public TelnetClientWrapper getTelnetClient() {
		return telnetClient;
	}

}
