package ac.bali.sta.core;

import io.bali.sta.core.Balista;
import io.bali.sta.core.BalistaDriver;
import io.bali.sta.core.bus.EventBus;

public class BalistaImpl
    implements Balista
{
    private final EventBus bus;

    public BalistaImpl( EventBus bus )
    {
        this.bus = bus;
    }

    @Override
    public EventBus eventBus()
    {
        return bus;
    }

    @Override
    public void installDriver( BalistaDriver driver )
    {

    }

    @Override
    public void destroy()
    {
    }
}
