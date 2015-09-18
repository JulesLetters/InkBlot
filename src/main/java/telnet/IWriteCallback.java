package telnet;

import java.util.Optional;

public interface IWriteCallback {

	void call(Optional<Exception> maybeException);

}
