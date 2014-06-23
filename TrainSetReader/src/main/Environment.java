package main;

import java.io.PrintWriter;

/**
 * Abstract class defining the environment in which the TrainSetReader module is used.
 *
 */
public abstract class Environment
{	
	private static int			_precision = 10;
	private static int			_nThreads = 4;
	private static Integer		_unknownThreshold = null;
	private static String		_unknownLabel = "**UNKNOWN**";
	private static boolean		_lexical = false;
	private static	PrintWriter 	_out = null;
	private static  AnnotationStripperOption annotation_stripper_opt = null;
	
	public static void closeOutput() {
		Environment._out.close();
	}

	/**
	 * Get one of the three options for the phrase format.
	 * @return One of the three options for the annotation stripping.
	 */
	public static AnnotationStripperOption getAnnotationStripperOption() {
		return Environment.annotation_stripper_opt;
	}
	
	/**
	 * Set the output file.
	 * @param p The stream to print the output to.
	 */
	public static void setOutputStream(PrintWriter p) {
		Environment._out = p;
	}
	
	/**
	 * Check whether rare words are used for dealing with unknown words.
	 * @return True if using a threshold for determining rare words.
	 */
	public static boolean isAltMode()
	{
		return (Environment._unknownThreshold != null);
	}
	
	/**
	 * Get the output file.
	 * @return The output stream to print to.
	 */
	public static PrintWriter getOutputStream() {
		return Environment._out;
	}
	
	/**
	 * Get the precision (the number of digits to print for the probabilities).
	 * @return The precision (the number of digits after the decimal separator).
	 */
	public static int getPrecision()
	{
		return (Environment._precision);
	}
	
	/**
	 * Get the number of threads.
	 * @return The number of threads.
	 */
	public static int getNThreads()
	{
		return (Environment._nThreads);
	}
	
	/**
	 * Get the threshold for rare words.
	 * @return The threshold for counting words as unknown.
	 */
	public static int getUnknownThreshold()
	{
		if (Environment._unknownThreshold == null)
			throw new RuntimeException("Undefined variable: unknownThreshold");
		return ((int)Environment._unknownThreshold);
	}
	
	/**
	 * Get the label for unknown words.
	 * @return The unique label for all all unknown words.
	 */
	public static String getUnknownLabel()
	{
		return (Environment._unknownLabel);
	}

	/**
	 * Whether terminals are used or not.
	 * @return True if using terminals, false if using only categories.
	 */
	public static boolean hasLexical()
	{
		return (Environment._lexical);
	}
	
	/**
	 * Set the precision (number of digits in probabilities).
	 * @param p The precision value.
	 */
	public static void setPrecision(int p)
	{
		Environment._precision = p;
	}
	
	/**
	 * Set the number of threads.
	 * @param t The number of threads.
	 */
	public static void setNThreads(int t)
	{
		Environment._nThreads = t;
	}
	
	/**
	 * Set the threshold for rare words.
	 * @param u The threshold for counting words as unknown.
	 */
	public static void setUnknownThreshold(int u)
	{
		Environment._unknownThreshold = u;
	}
	
	/**
	 * Set the label for unknown words.
	 * @param l The unique label for all all unknown words.
	 */
	public static void setUnknownLabel(String l)
	{
		Environment._unknownLabel = l;
	}
	
	/**
	 * Set one of the three options for the phrase format (LEX, CAT or LEX_N_CAT).
	 * @param opt One of the three options for the annotation stripping.
	 */
	public static void setAnnotationStripperOption(AnnotationStripperOption opt) {
		Environment.annotation_stripper_opt = opt;
	}

	/**
	 * Set the environment as lexical or non-lexical.
	 * @param l True if using terminals, false if using only categories.
	 */
	public static void setLexical(boolean l)
	{
		Environment._lexical = l;
	}
}
