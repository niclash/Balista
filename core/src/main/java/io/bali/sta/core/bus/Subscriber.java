package io.bali.sta.core.bus;

public interface Subscriber<T extends Event>
{
    void published( T event );
}
