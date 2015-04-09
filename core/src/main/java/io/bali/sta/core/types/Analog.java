package io.bali.sta.core.types;

/** Data type for Analog, floating point, values.
 *
 */
public class Analog
    implements Comparable<Analog>
{
    public final double value;

    public Analog( double value )
    {
        this.value = value;
    }

    public boolean isLargerThanEqual( Analog otherValue )
    {
        return value >= otherValue.value;
    }

    public boolean isSmallerThanEqual( Analog otherValue )
    {
        return value <= otherValue.value;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o )
        {
            return true;
        }
        if( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        Analog analog = (Analog) o;
        return Double.compare( analog.value, value ) == 0;
    }

    @Override
    public int hashCode()
    {
        long temp = value != +0.0d ? Double.doubleToLongBits( value ) : 0L;
        return (int) ( temp ^ ( temp >>> 32 ) );
    }

    @Override
    public int compareTo( Analog other )
    {
        return new Double(value).compareTo( other.value );
    }

    public double getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append( "Analog(" );
        sb.append( value );
        sb.append( ')' );
        return sb.toString();
    }
}
