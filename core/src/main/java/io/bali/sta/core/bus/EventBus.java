package io.bali.sta.core.bus;

public interface EventBus
{
    <T extends Event> void publish( Class<T> eventType, T event );

    <T extends Event> Subscription<T> subscribe(Class<T> eventType, EventFilter<T> filter, Subscriber<T> subscriber );

    <T extends Event> void unsubscribe(Subscription<T> subscription );
}
