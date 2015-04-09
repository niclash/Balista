package ac.bali.sta.core.bus;

import io.bali.sta.core.bus.Event;
import io.bali.sta.core.bus.EventFilter;
import io.bali.sta.core.bus.Subscriber;
import io.bali.sta.core.bus.Subscription;

public class EventSubscriptionImpl<T extends Event>
    implements Subscription<T>
{
    private final Class<T> type;
    private final EventFilter<T> filter;
    private final Subscriber subscriber;

    public EventSubscriptionImpl( Class<T> type, EventFilter<T> filter, Subscriber subscriber )
    {
        this.type = type;
        this.filter = filter;
        this.subscriber = subscriber;
    }

    @Override
    public Class<T> getEventType()
    {
        return type;
    }

    public void publish( T event )
    {
        if( filter.isSatisfied( event ))
            subscriber.published( event );
    }
}
