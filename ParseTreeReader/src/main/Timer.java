package main;

/**
 * A simple timer in milliseconds.
 * @author Antoine LAFOUASSE
 *
 */
public class Timer
{
	private long		_start;
	private long		_lastLap;
	private int			_lapCount;

	/**
	 * Starts a new timer.
	 */
	public Timer()
	{
		this._start = System.currentTimeMillis();
		this._lastLap = this._start;
		this._lapCount = 0;
	}
	
	/**
	 * Records a lap and returns the elapsed time in a String.
	 * @return A String containing the number of times this method has been
	 * called, the time elapsed since the last time this method was called, 
	 * and the time elapsed since the start of the timer (i.e. the time the
	 * timer's constructor was called).
	 */
	public String lap()
	{
		long	snap;
		long	lap;
		long	total;

		snap = System.currentTimeMillis();
		lap = snap - this._lastLap;
		total = snap - this._start;
		this._lastLap = snap;
		this._lapCount++;
		return (String.format("Lap %d: %dms (%dms)",
				this._lapCount, lap, total));
	}
}
