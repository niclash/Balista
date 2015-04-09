package io.bali.sta.core.blocks;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

public class PidTest
{
    @Test
    public void givenOffManualStateWhenExecutingExpectNoChangeInOutput()
    {
        Pid pid = new Pid( 10 );
        pid.setOn( false );
        pid.setAuto( false );
        pid.setPConst( 2 );
        pid.setITime( 100 );
        for( int i = 0; i < 100; i++ )
        {
            pid.setInput( i );
            pid.setManual( i / 2.0 );
            pid.run();
            assertThat( pid.getOutput(), equalTo( 0.0 ) );
        }
    }

    @Test
    public void givenOffAutoStateWhenExecutingExpectNoChangeInOutput()
    {
        Pid pid = new Pid( 10 );
        pid.setOn( false );
        pid.setAuto( true );
        pid.setPConst( 2 );
        pid.setITime( 100 );
        for( int i = 0; i < 100; i++ )
        {
            pid.setInput( i );
            pid.setManual( i / 2.0 );
            pid.run();
            assertThat( pid.getOutput(), equalTo( 0.0 ) );
        }
    }

    @Test
    public void givenOnManualStateWhenExecutingExpectOutputFollowManualWithoutLimits()
    {
        Pid pid = new Pid( 10 );
        pid.setAuto( false );
        pid.setOn( true );
        pid.setPConst( 2 );
        pid.setITime( 100 );
        for( int i = 0; i < 100; i++ )
        {
            pid.setInput( i );
            pid.setManual( i / 2.0 );
            pid.run();
            assertThat( pid.getOutput(), equalTo( pid.getManual() ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenExecutingInPmodeExpectOutputToStaySame()
    {
        Pid pid = new Pid( 10 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 2 );
        pid.setITime( 0 );
        pid.setLoLimit( 0 );
        pid.setHiLimit( 100 );
        double[] expected = { 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0, 40.0 };

        for( int loopVar = 0; loopVar < 10; loopVar++ )
        {
            pid.setManual( loopVar / 2.0 );
            pid.run();
            assertThat( pid.getOutput(), equalTo( expected[ loopVar ] ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenExecutingInPmodeBeyondLimitsExpectOutputToStayWithinHiLimit()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 15f );
        pid.setITime( 0 );
        pid.setLoLimit( 0 );
        pid.setHiLimit( 100 );
        double[] expected = { 0, 15, 30, 45, 60, 75, 90, 100, 100, 100 };

        for( int loopVar = 0; loopVar < 10; loopVar++ )
        {
            pid.setInput( 20 - loopVar );
            pid.run();
            assertThat( pid.getOutput(), equalTo( expected[ loopVar ] ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenExecutingInPmodeBeyondLimitsExpectOutputToStayWithinLoLimit()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 15f );
        pid.setITime( 0 );
        pid.setLoLimit( 0 );
        pid.setHiLimit( 100 );
        double[] expected = { 75, 60, 45, 30, 15, 0, 0, 0, 0, 0 };

        for( int loopVar = 0; loopVar < 10; loopVar++ )
        {
            pid.setInput( 15 + loopVar );
            pid.run();
            assertThat( pid.getOutput(), equalTo( expected[ loopVar ] ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenExecutingInImodeExpectOutputToIncrease()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 0 );
        pid.setITime( 1 );
        pid.setLoLimit( 0 );
        pid.setHiLimit( 100 );
        double[] expected = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 };

        for( int loopVar = 0; loopVar < 10; loopVar++ )
        {
            pid.run();
            assertThat( pid.getOutput(), equalTo( expected[ loopVar ] ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenExecutingInImodeExpectAntiWindupLimitAtLow()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setInput( 40 );
        pid.setSetpoint( 20 );
        pid.setPConst( 0 );
        pid.setITime( 1 );
        pid.setLoLimit( -100 );
        pid.setHiLimit( 100 );

        for( int loopVar = 0; loopVar < 1000; loopVar++ )
        {
            pid.run();
        }
        assertThat( pid.getITerm(), equalTo( pid.getLoLimit() ) );
    }

    @Test
    public void givenOnAutoStateWhenExecutingInImodeExpectAntiWindupLimitAtHigh()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setInput( 0 );
        pid.setSetpoint( 20 );
        pid.setPConst( 0 );
        pid.setITime( 1 );
        pid.setLoLimit( -100 );
        pid.setHiLimit( 100 );

        for( int loopVar = 0; loopVar < 1000; loopVar++ )
        {
            pid.run();
        }
        assertThat( pid.getITerm(), equalTo( pid.getHiLimit() ) );
    }

    @Test
    public void givenOnAutoStateWhenExecutingInDmodeAndNoChangeInErrorExpectOutputToRemainUnchanged()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 0 );
        pid.setITime( 0 );
        pid.setDTime( 1 );
        pid.setLoLimit( -100 );
        pid.setHiLimit( 100 );
        double[] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

        for( int loopVar = 0; loopVar < 10; loopVar++ )
        {
            pid.run();
            assertThat( pid.getOutput(), equalTo( expected[ loopVar ] ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenExecutingInDmodeExpectOutputToShowSmallNegativeExceptFirstCycle()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 0 );
        pid.setITime( 0 );
        pid.setDTime( 1 );
        pid.setLoLimit( -100 );
        pid.setHiLimit( 100 );
        double[] expected = { 0.0, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1, -0.1 };

        for( int loopVar = 0; loopVar < 10; loopVar++ )
        {
            pid.setInput( loopVar );
            pid.run();
            assertThat( pid.getOutput(), closeTo( expected[ loopVar ], 0.005 ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenExecutingInPImodeExpectOutputToIncrease()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 2 );
        pid.setITime( 100 );
        pid.setLoLimit( 0 );
        pid.setHiLimit( 100 );
        double[] expected = { 40.04, 40.08, 40.12, 40.16, 40.20, 40.24, 40.28, 40.32, 40.36, 40.40 };

        for( int loopVar = 0; loopVar < 10; loopVar++ )
        {
            pid.run();
            assertThat( pid.getOutput(), closeTo( expected[ loopVar ], 0.005 ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenExecutingInPDmodeWithInputChangeExpectOutputToShowPCharacteristicsMinusDcompensation()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 1 );
        pid.setITime( 0 );
        pid.setDTime( 1 );
        pid.setLoLimit( -100 );
        pid.setHiLimit( 100 );
        double[] expected = { 20.0, 18.9, 17.9, 16.9, 15.9, 14.9, 13.9, 12.9, 11.9, 10.9 };

        for( int loopVar = 0; loopVar < 10; loopVar++ )
        {
            pid.setInput( loopVar );
            pid.run();
            assertThat( pid.getOutput(), closeTo( expected[ loopVar ], 0.005 ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenExecutingInPIDmodeWithInputChangeExpectOutputToShowPIDcurve()
    {
        Pid pid = new Pid( 100 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 1 );
        pid.setITime( 1 );
        pid.setDTime( 2 );
        pid.setLoLimit( -100 );
        pid.setHiLimit( 100 );
        double[] expected = { 22.0, 22.85, 23.650002, 24.350002, 24.95, 25.45, 25.850002, 26.150002, 26.350002, 26.45 };

        for( int loopVar = 0; loopVar < 10; loopVar++ )
        {
            pid.setInput( loopVar );
            pid.run();
            assertThat( pid.getOutput(), closeTo( expected[ loopVar ], 0.005 ) );
        }
    }

    @Test
    public void givenOnAutoStateWhenStartingPmodeWithLargeOutputExpectOutputToBeWithinHiLimit()
    {
        Pid pid = new Pid( 100 );
        pid.setOutput( 200 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setInput( 10 );
        pid.setSetpoint( 20 );
        pid.setPConst( 1 );
        pid.setITime( 0 );
        pid.setLoLimit( 0 );
        pid.setHiLimit( 100 );
        pid.run();
        assertThat( pid.getOutput(), equalTo( 100.0 ) );
    }

    @Test
    public void givenOnAutoStateWhenStartingPmodeWithLargeNegativeOutputExpectOutputToBeWithinLoLimit()
    {
        Pid pid = new Pid( 100 );
        pid.setOutput( -200 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 0 );
        pid.setITime( -5 );
        pid.setLoLimit( -100 );
        pid.setHiLimit( 100 );
        pid.run();
        assertThat( pid.getOutput(), equalTo( -100.0 ) );
    }

    @Test
    public void givenOnAutoStateWhenStartingPmodeWithLargeNegativeOutputExpectOutputToBeWithinLoLimit2()
    {
        Pid pid = new Pid( 100 );
        pid.setOutput( -200 );
        pid.setAuto( true );
        pid.setOn( true );
        pid.setSetpoint( 20 );
        pid.setPConst( 0 );
        pid.setITime( 0 );
        pid.setDTime( -5 );
        pid.setLoLimit( 0 );
        pid.setHiLimit( 100 );
        pid.run();
        assertThat( pid.getOutput(), equalTo( 0.0 ) );
    }

    @Test
    public void testGetOn()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getOn(), equalTo( true ) );
        pid.setOn( false );
        assertThat( pid.getOn(), equalTo( false ) );
        pid.setOn( true );
        assertThat( pid.getOn(), equalTo( true ) );
    }

    @Test
    public void testGetAuto()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getAuto(), equalTo( true ) );
        pid.setAuto( false );
        assertThat( pid.getAuto(), equalTo( false ) );
        pid.setAuto( true );
        assertThat( pid.getAuto(), equalTo( true ) );
    }

    @Test
    public void testGetInput()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getInput(), equalTo( 0.0 ) );
        pid.setInput( 10 );
        assertThat( pid.getInput(), equalTo( 10.0 ) );
        pid.setInput( 50 );
        assertThat( pid.getInput(), equalTo( 50.0 ) );
    }

    @Test
    public void testGetSetpoint()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getSetpoint(), equalTo( 0.0 ) );
        pid.setSetpoint( 10 );
        assertThat( pid.getSetpoint(), equalTo( 10.0 ) );
        pid.setSetpoint( 50 );
        assertThat( pid.getSetpoint(), equalTo( 50.0 ) );
    }

    @Test
    public void testGetManual()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getManual(), equalTo( 0.0 ) );
        pid.setManual( 10 );
        assertThat( pid.getManual(), equalTo( 10.0 ) );
        pid.setManual( 50 );
        assertThat( pid.getManual(), equalTo( 50.0 ) );
    }

    @Test
    public void testGetLoLimit()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getLoLimit(), equalTo( 0.0 ) );
        pid.setLoLimit( 10 );
        assertThat( pid.getLoLimit(), equalTo( 10.0 ) );
        pid.setLoLimit( 50 );
        assertThat( pid.getLoLimit(), equalTo( 50.0 ) );
    }

    @Test
    public void testGetHiLimit()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getHiLimit(), equalTo( 0.0 ) );
        pid.setHiLimit( 10 );
        assertThat( pid.getHiLimit(), equalTo( 10.0 ) );
        pid.setHiLimit( 50 );
        assertThat( pid.getHiLimit(), equalTo( 50.0 ) );
    }

    @Test
    public void testGetPConst()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getPConst(), equalTo( 0.0 ) );
        pid.setPConst( 10 );
        assertThat( pid.getPConst(), equalTo( 10.0 ) );
        pid.setPConst( 50 );
        assertThat( pid.getPConst(), equalTo( 50.0 ) );
    }

    @Test
    public void testGetITime()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getITime(), equalTo( 0.0 ) );
        pid.setITime( 10 );
        assertThat( pid.getITime(), equalTo( 10.0 ) );
        pid.setITime( 50 );
        assertThat( pid.getITime(), equalTo( 50.0 ) );
    }

    @Test
    public void testGetDTime()
    {
        Pid pid = new Pid( 10 );
        assertThat( pid.getDTime(), equalTo( 0.0 ) );
        pid.setDTime( 10 );
        assertThat( pid.getDTime(), equalTo( 10.0 ) );
        pid.setDTime( 50 );
        assertThat( pid.getDTime(), equalTo( 50.0 ) );
    }
}
