package ac.bali.sta.core.bus;

import io.bali.sta.core.bus.Event;
import io.bali.sta.core.bus.EventBus;
import io.bali.sta.core.bus.EventFilter;
import io.bali.sta.core.bus.Subscriber;
import io.bali.sta.core.bus.Subscription;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EventBusImpl
    implements EventBus
{
    private final ConcurrentHashMap<Class, List<EventSubscriptionImpl>> subscriptions;

    public EventBusImpl()
    {
        subscriptions = new ConcurrentHashMap<Class, List<EventSubscriptionImpl>>();
    }

    @Override
    public <T extends Event> void publish( Class<T> eventType, T event )
    {
        List<EventSubscriptionImpl> allSubscriptions = subscriptions.get( eventType );
        if( allSubscriptions == null )
        {
            return;
        }
        for( EventSubscriptionImpl subscription : allSubscriptions )
        {
            subscription.publish( event );
        }
    }

    @Override
    public <T extends Event> Subscription<T> subscribe( Class<T> eventType,
                                                        EventFilter<T> filter,
                                                        Subscriber<T> subscriber
    )
    {
        synchronized( subscriptions )
        {
            EventSubscriptionImpl<T> subscription = new EventSubscriptionImpl<T>( eventType, filter, subscriber );
            List<EventSubscriptionImpl> subscriptions = getSubscriptions( eventType );
            subscriptions.add( subscription );
            return subscription;
        }
    }

    @Override
    public <T extends Event> void unsubscribe( Subscription<T> subscription )
    {
        for( List<EventSubscriptionImpl> subs : subscriptions.values() )
        {
            subs.remove( subscription );
        }
    }

    private <T extends Event> List<EventSubscriptionImpl> getSubscriptions( Class<T> eventType )
    {
        List<EventSubscriptionImpl> subscriptions = this.subscriptions.get( eventType );
        if( subscriptions == null )
        {
            subscriptions = new ArrayList<EventSubscriptionImpl>();
            this.subscriptions.put( eventType, subscriptions );
        }
        return subscriptions;
    }
}
