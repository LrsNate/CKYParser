package main;

public abstract class Environment
{	
	private static int			_precision = 10;
	private static int			_nThreads = 4;
	private static boolean		_lexical = false;
	
	public static int getPrecision()
	{
		return (Environment._precision);
	}
	
	public static int getNThreads()
	{
		return (Environment._nThreads);
	}

	public static boolean hasLexical()
	{
		return (Environment._lexical);
	}
	
	public static void setPrecision(int p)
	{
		Environment._precision = p;
	}
	
	public static void setNThreads(int t)
	{
		Environment._nThreads = t;
	}

	public static void setLexical(boolean l)
	{
		Environment._lexical = l;
	}
}
