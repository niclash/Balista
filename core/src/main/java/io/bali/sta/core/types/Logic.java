package io.bali.sta.core.types;

import io.bali.sta.core.NullArgumentException;

/** Data type for Logical (tri-state) values.
 *
 */
public class Logic
{

    private final TriState state;

    public Logic(TriState state)
    {
        NullArgumentException.validateNotNull( "state", state );
        this.state = state;
    }

    public TriState getState()
    {
        return state;
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

        Logic logic = (Logic) o;

        if( state != logic.state )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return state.hashCode();
    }
}
