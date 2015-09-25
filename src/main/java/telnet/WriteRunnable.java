package telnet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;

public class WriteRunnable implements Runnable {

	private String stringToWrite;
	private IWriteCallback callback;
	private WritableByteChannel writeableByteChannel;

	public WriteRunnable(String stringToWrite, IWriteCallback callback, WritableByteChannel writeableByteChannel) {
		this.stringToWrite = stringToWrite;
		this.callback = callback;
		this.writeableByteChannel = writeableByteChannel;
	}

	@Override
	public void run() {
		try {
			String actualStringToWrite = stringToWrite + "\n";
			writeableByteChannel.write(ByteBuffer.wrap(actualStringToWrite.getBytes()));
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

	public WritableByteChannel getWriteableByteChannel() {
		return writeableByteChannel;
	}

}
