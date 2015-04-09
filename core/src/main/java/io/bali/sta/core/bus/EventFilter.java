package io.bali.sta.core.bus;

public interface EventFilter<T extends Event>
{
    boolean isSatisfied( T event );
}
