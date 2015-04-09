package io.bali.sta.core.bus;

public interface Subscription<T extends Event>
{
    Class<T> getEventType();
}