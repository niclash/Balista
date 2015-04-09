package io.bali.sta.core;

/** All exceptions in Balista must extend from this exception.
 *
 */
public class BalistaException extends RuntimeException
{
    public BalistaException( String message )
    {
        super( message );
    }

    public BalistaException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
