package io.bali.sta.core;

import io.bali.sta.core.bus.EventBus;

public interface Balista
{

    EventBus eventBus();

    void installDriver( BalistaDriver driver );

    void destroy();
}
