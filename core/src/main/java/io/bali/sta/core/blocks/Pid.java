package io.bali.sta.core.blocks;

public class Pid extends CyclicBlock
{
    private boolean on;
    private boolean auto;
    private double manual;
    private double input;
    private double setpoint;
    private double output;
    private double pConst;
    private double iTime;
    private double dTime;
    private double loLimit;
    private double hiLimit;

    private double lastInput;
    private boolean initialized;
    private double kI;
    private double kD;
    private double iTerm;

    public Pid( int interval )
    {
        super( interval );
        on = true;
        auto = true;

    }

    @Override
    public void run()
    {
        boolean inAuto = getAuto();
        boolean on = getOn();
        if( !on )
        {
            initialized=false;
            return;
        }
        if( !inAuto )
        {
            // Let the initialize method keep running until we enter Auto mode.
            setOutput( getManual() );
            initialized=false;
            return;
        }
        initializeIfRequired();

        double input = getInput();
        double outMax = getHiLimit();
        double outMin = getLoLimit();

        // Compute all the working error variables
        double error = getSetpoint() - input;
        double iTerm = this.iTerm;
        iTerm += ( kI * error );
        if( iTerm > outMax )
        {
            iTerm = outMax;
        }
        else if( iTerm < outMin )
        {
            iTerm = outMin;
        }
        double dInput = ( input - lastInput );

        // Compute PID Output
        double output = (float) ( pConst * error + iTerm - kD * dInput );
        if( output > outMax )
        {
            output = outMax;
        }
        else if( output < outMin )
        {
            output = outMin;
        }

        // Remember some variables for next time
        lastInput = input;
        setOutput( output );
        this.iTerm = iTerm;
    }

    private void initializeIfRequired()
    {
        boolean flag = initialized;
        if( !flag )
        {
            initialize();
            initialized = true;
        }
    }

    private void initialize()
    {
        lastInput = getInput();
        double max = getHiLimit();
        double min = getLoLimit();
        double output = getOutput();
        if( output > max )
        {
            output = max;
        }
        else if( output < min )
        {
            output = min;
        }
        iTerm = output;
        setOutput( output );
        double iTime = getITime();
        double dTime = getDTime();

        if( pConst < 0 || iTime < 0 || dTime < 0 )
        {
            return;
        }
        double sampleTimeInSec = ( getInterval() ) / 1000.0;

        kI = computeConstant( pConst, iTime, sampleTimeInSec );
        kD = computeConstant( pConst, dTime, sampleTimeInSec );
    }

    private double computeConstant( double pConst, double timeFactor, double sampleTimeInSec )
    {
        double constant;
        if( timeFactor == 0 )
        {
            constant = 0;
        }
        else
        {
            if( pConst == 0 )
            {
                constant = sampleTimeInSec / timeFactor;
            }
            else
            {
                constant = pConst * sampleTimeInSec / timeFactor;
            }
        }
        return constant;
    }

    boolean getOn()
    {
        return on;
    }

    public void setOn( boolean on )
    {
        this.on = on;
    }

    boolean getAuto()
    {
        return auto;
    }

    public void setAuto( boolean auto )
    {
        this.auto = auto;
    }

    public double getInput()
    {
        return input;
    }

    public void setInput( double input )
    {
        this.input = input;
    }

    double getSetpoint()
    {
        return setpoint;
    }

    public void setSetpoint( double setpoint )
    {
        this.setpoint = setpoint;
    }

    double getOutput()
    {
        return output;
    }

    void setOutput( double value )
    {
        output = value;
    }

    double getManual()
    {
        return manual;
    }

    public void setManual( double manual )
    {
        this.manual = manual;
    }

    public double getPConst()
    {
        return pConst;
    }

    public void setPConst( double pConst )
    {
        this.pConst = pConst;
    }

    public double getITime()
    {
        return iTime;
    }

    public void setITime( double iTime )
    {
        this.iTime = iTime;
    }

    public double getDTime()
    {
        return dTime;
    }

    public void setDTime( double dTime )
    {
        this.dTime = dTime;
    }

    public double getLoLimit()
    {
        return loLimit;
    }

    public void setLoLimit( double loLimit )
    {
        this.loLimit = loLimit;
    }

    public double getHiLimit()
    {
        return hiLimit;
    }

    public void setHiLimit( double hiLimit )
    {
        this.hiLimit = hiLimit;
    }

    double getITerm()
    {
        return iTerm;
    }
}
