package io.bali.sta.core.types;

import java.io.UnsupportedEncodingException;

public class Bytes
{
    public static final byte[] EMPTY = new byte[0];

    public static int convertByte( byte b )
    {
        if( b >= 0 )
        {
            return b;
        }
        return 256 + b;
    }

    public static byte[] convertFromLogic( boolean value )
    {
        byte[] result = new byte[ 1 ];
        result[ 0 ] = (byte) ( value ? 1 : 0 );
        return result;
    }

    public static byte[] convertFromIndex( int indexValue )
    {
        byte[] result = new byte[ 1 ];
        result[ 0 ] = (byte) ( indexValue & 255 );
        return result;
    }

    public static byte[] convertFromInteger( int integerValue )
    {
        byte[] result = new byte[ 2 ];
        result[ 0 ] = (byte) ( integerValue & 0xFF );
        result[ 1 ] = (byte) ( integerValue >> 8 & 0xFF );
        return result;
    }

    public static void convertFromReal( float realValue, byte[] destination, int offset )
    {
        int bits = Float.floatToIntBits( realValue );
        destination[ offset + 0 ] = (byte) ( bits & 0xFF );
        destination[ offset + 1 ] = (byte) ( ( bits >>> 8 ) & 0xFF );
        destination[ offset + 2 ] = (byte) ( ( bits >>> 16 ) & 0xFF );
        destination[ offset + 3 ] = (byte) ( ( bits >>> 24 ) & 0xFF );
    }

    public static byte[] convertFromReal( float realValue )
    {
        byte[] result = new byte[ 4 ];
        convertFromReal( realValue, result, 0 );
        return result;
    }

    public static Boolean convertToLogic( byte[] bytes, int offset )
    {
        return convertByte( bytes[ offset ] ) != 0;
    }

    public static int convertToIndex( byte[] bytes, int offset )
    {
        return convertByte( bytes[ offset ] );
    }

    public static int convertToInteger( byte[] bytes, int offset )
    {
        int value;
        int hi = convertByte( bytes[ offset + 1 ] );
        int lo = convertByte( bytes[ offset ] );
        value = lo + hi * 256;
        if( hi >= 128 )
        {
            value |= 0xFFFF0000;
        }
        return value;
    }

    public static int convertToExolInteger( int lsb, int msb )
    {
        int value = convertToJavaInt( lsb, msb );
        if( msb >= 128 )
        {
            // negative number
            return value - 65536;
        }
        else
        {
            //positive number
            return value;
        }
    }

    public static int convertToJavaInt( int lsb, int msb )
    {
        return ( lsb + msb * 256 );
    }

    public static long convertToJavaLong( byte[] bytes, int offset )
    {
        return convertByte( bytes[ offset ] ) +
               convertByte( bytes[ offset + 1 ] ) * 256 +
               convertByte( bytes[ offset + 2 ] ) * 65536 +
               convertByte( bytes[ offset + 3 ] ) * 256 * 65536;
    }

    public static float convertToReal( byte[] bytes, int offset )
    {
        return convertToReal( convertByte( bytes[ offset ] ),
                              convertByte( bytes[ offset + 1 ] ),
                              convertByte( bytes[ offset + 2 ] ),
                              convertByte( bytes[ offset + 3 ] ) );
    }

    public static float convertToReal( int bits1, int bits2, int bits3, int bits4 )
    {
        return Float.intBitsToFloat( (int) bits1 | bits2 << 8 | bits3 << 16 | bits4 << 24 );
    }

    public static byte[] extract( byte[] data, int fromBeginning, int fromEnd )
    {
        byte[] result = new byte[ data.length - fromBeginning - fromEnd ];
        System.arraycopy( data, fromBeginning, result, 0, result.length );
        return result;
    }

    public static String formatHexByte( int value, int size )
    {
        String hex = "000" + Integer.toHexString( value );
        int start = hex.length() - size;
        int end = hex.length();
        return hex.substring( start, end ).toUpperCase();
    }

    public static byte[] convertFromString( String value )
    {
        try
        {
            return value.getBytes( "ISO-8859-1" );
        }
        catch( UnsupportedEncodingException e )
        {
            // can not happen!!?!??!!
            return value.getBytes();
        }
    }

    public static String convertToString( byte[] data, int offset, int length )
    {
        try
        {
            return new String( data, offset, length, "ISO-8859-1" );
        }
        catch( UnsupportedEncodingException e )
        {
            // Can not happen??!?!!?!!??!!
            return new String( data, offset, length );
        }
    }
}
