package main;

public abstract class Environment
{	
	private static int			_precision = 10;
	private static int			_nThreads = 4;
	private static Integer		_unknownThreshold = null;
	private static String		_unknownLabel = "UNK";
	private static boolean		_lexical = false;
	
	public static boolean isAltMode()
	{
		return (Environment._unknownThreshold != null);
	}
	
	public static int getPrecision()
	{
		return (Environment._precision);
	}
	
	public static int getNThreads()
	{
		return (Environment._nThreads);
	}
	
	public static int getUnknownThreshold()
	{
		if (Environment._unknownThreshold == null)
			throw new RuntimeException("Undefined variable: unknownThreshold");
		return ((int)Environment._unknownThreshold);
	}
	
	public static String getUnknownLabel()
	{
		return (Environment._unknownLabel);
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
	
	public static void setUnknownThreshold(int u)
	{
		Environment._unknownThreshold = u;
	}
	
	public static void setUnknownLabel(String l)
	{
		Environment._unknownLabel = l;
	}

	public static void setLexical(boolean l)
	{
		Environment._lexical = l;
	}
}
