package main;

import com.google.common.eventbus.EventBus;

public class GuavaEventBus implements IEventBus {

	private EventBus eventBus = new EventBus();

	public void register(Object object) {
		eventBus.register(object);
	}

	@Override
	public void unregister(Object object) {
		eventBus.unregister(object);
	}

	public void post(Object event) {
		eventBus.post(event);
	}

}
