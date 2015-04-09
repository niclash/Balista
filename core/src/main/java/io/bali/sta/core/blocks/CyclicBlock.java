package io.bali.sta.core.blocks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class CyclicBlock
    implements Runnable
{
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    private final int interval;
    private ScheduledFuture<?> future;

    protected CyclicBlock( int interval )
    {
        this.interval = interval;
    }

    public void start()
    {
        future = executor.scheduleAtFixedRate( this, interval, interval, TimeUnit.MILLISECONDS );
    }

    public void stop()
    {
        future.cancel( true );
    }

    public long getInterval()
    {
        return interval;
    }

    public static void shutdown()
    {
        executor.shutdown();
    }
}
