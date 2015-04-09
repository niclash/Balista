package io.bali.sta.core;

public class NullArgumentException extends IllegalArgumentException
{
    public NullArgumentException( String message )
    {
        super(message);
    }

    public static void validateNotEmpty( String name, String value )
    {
        if( value == null )
            throw new NullArgumentException(name + " is not allowed to be null." );
        if( value.length() == 0 )
            throw new NullArgumentException(name + " is not allowed to be empty String." );
    }

    public static void validateNotNull( String name, Object value )
    {
        if( value == null )
            throw new NullArgumentException(name + " is not allowed to be null." );
    }
}
