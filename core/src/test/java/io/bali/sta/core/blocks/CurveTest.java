package io.bali.sta.core.blocks;

import io.bali.sta.core.types.Analog;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class CurveTest
{

    private Curve underTest;

    @Before
    public void initializeTest()
    {
        Analog[] xValues = new Analog[]{
            new Analog( 10 ),
            new Analog( 20 ),
            new Analog( 30 ),
            new Analog( 40 ),
            new Analog( 50 )
        };
        Analog[] yValues = new Analog[]{
            new Analog( 20 ),
            new Analog( 25 ),
            new Analog( 35 ),
            new Analog( 50 ),
            new Analog( 70 )
        };
        underTest = new Curve(xValues, yValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsorted()
    {
        Analog[] xValues = new Analog[]{
            new Analog( 10 ),
            new Analog( 30 ),
            new Analog( 20 ),
            new Analog( 40 ),
            new Analog( 50 )
        };
        Analog[] yValues = new Analog[]{
            new Analog( 20 ),
            new Analog( 25 ),
            new Analog( 35 ),
            new Analog( 50 ),
            new Analog( 70 )
        };
        new Curve(xValues, yValues);
    }

    @Test
    public void testTooLow()
        throws Exception
    {
        Analog x = new Analog( 9.9 );
        assertThat(underTest.f( x ), equalTo(new Analog(20)));
    }

    @Test
    public void testTooHigh()
        throws Exception
    {
        Analog x = new Analog( 50.1 );
        assertThat(underTest.f( x ), equalTo(new Analog(70)));
    }

    @Test
    public void testSegment1()
    {
        Analog x = new Analog( 15 );
        assertThat(underTest.f( x ), equalTo(new Analog(22.5)));
    }
    @Test
    public void testSegment2()
    {
        Analog x = new Analog( 25 );
        assertThat(underTest.f( x ), equalTo(new Analog(30)));
    }
    @Test
    public void testSegment3()
    {
        Analog x = new Analog( 35 );
        assertThat(underTest.f( x ), equalTo(new Analog(42.5)));
    }
    @Test
    public void testSegment4()
    {
        Analog x = new Analog( 45 );
        assertThat(underTest.f( x ), equalTo(new Analog(60)));
    }
}
