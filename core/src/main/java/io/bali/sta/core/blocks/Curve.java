package io.bali.sta.core.blocks;

import io.bali.sta.core.NullArgumentException;
import io.bali.sta.core.functions.Func1;
import io.bali.sta.core.types.Analog;

public class Curve
    implements Func1<Analog, Analog>
{
    private final Analog[] xValues;
    private final Analog[] yValues;

    public Curve( Analog[] xValues, Analog[] yValues )
    {
        if( xValues.length != yValues.length )
        {
            throw new IllegalArgumentException( "xValues must be of same size as yValues." );
        }
        for( int i = 0; i < xValues.length - 1; i++ )
        {
            if( xValues[ i ].isLargerThanEqual( xValues[ i + 1 ] ) )
            {
                throw new IllegalArgumentException( "The xValues must be in ascending order." );
            }
            NullArgumentException.validateNotNull( "xValues[" + i + "]", xValues[i] );
            NullArgumentException.validateNotNull( "yValues[" + i + "]", yValues[i] );
        }
        this.xValues = xValues;
        this.yValues = yValues;
    }

    public Curve modify( int point, Analog xValue, Analog yValue )
    {
        Analog[] newX = new Analog[ xValues.length ];
        Analog[] newY = new Analog[ yValues.length ];
        System.arraycopy( xValues, 0, newX, 0, xValues.length );
        System.arraycopy( yValues, 0, newY, 0, yValues.length );
        newX[ point ] = xValue;
        newY[ point ] = yValue;
        return new Curve( newX, newY );
    }

    @Override
    public Analog f( Analog value )
    {
        if( value.isLargerThanEqual( xValues[ xValues.length - 1 ] ))
            return yValues[yValues.length-1];
        if( value.isSmallerThanEqual( xValues[ 0 ] ))
            return yValues[0];
        for( int i=0 ; i < xValues.length; i++)
        {
            if( value.isLargerThanEqual( xValues[ i ] ) && value.isSmallerThanEqual( xValues[ i + 1 ] ))
                return f( value, i );
        }
        throw new InternalError( "This can't happen." );
    }

    private Analog f( Analog value, int pos )
    {
        // y = k * x + m
        // y / (k*x) = m
        double x1 = xValues[pos].getValue();
        double x2 = xValues[pos+1].getValue();
        double y1 = yValues[pos].getValue();
        double y2 = yValues[pos+1].getValue();
        double k = (y1 - y2) / (x1-x2);
        double m = y1 - (x1 * k );
        return new Analog( value.getValue() * k + m);
    }
}
