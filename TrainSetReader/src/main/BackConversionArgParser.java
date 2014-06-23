package main;

/**
 * A class for parsing arguments for BackConversion.
 */
public class BackConversionArgParser {
	
	/**
	 * The file containing the parse trees in CNF form (one tree per line).
	 */
	private String input_file = ""; 
	
	/**
	 * The unique prefix of the labels of the nodes created 
	 * during the conversion to CNF.
	 */
	private String prefix_nodes_to_be_deleted = "";
	
	/**
	 * Set to true if each tree in the input file is preceded by a probability value.
	 */
	private boolean mode_probabilistic = false;
	
	/**
	 * Parse the command line arguments to be passed to the BackConversion class.
	 * @param argv The command line arguments.
	 */
	public BackConversionArgParser(String argv[]) {
		int i = 0;
		while (i < argv.length)
		{
			if (argv[i].equals("-f") || argv[i].equals("--input_file"))
			{
				i++;
				this.input_file = argv[i];
			}
			else if (argv[i].equals("--prefixe"))
			{
				i++;
				this.prefix_nodes_to_be_deleted = argv[i];
			}
			else if (argv[i].equals("--prob"))
			{
				this.mode_probabilistic = true;
			}
			i++;
		}
	}
	
	/**
	 * Check if all the necessary parameters have been passed via the command line arguments.
	 * @throws IllegalArgumentException
	 */
	public void check() {
		if (input_file.equals("")) {
			throw new IllegalArgumentException("The argument --input_file not set.");
		}
		if (prefix_nodes_to_be_deleted.equals("")) {
			throw new IllegalArgumentException("The argument --prefixe not set.");
		}
	}
	
	/**
	 * Getter for mode_probabilistic.
	 * @return True if each tree in the input file is preceded by a probability value.
	 */
	public boolean getModeProbabilistic() {
		return this.mode_probabilistic;
	}
	
	/**
	 * Getter for input_file.
	 * @return The file containing the parse trees in CNF form.
	 */
	public String getInputFile() {
		return this.input_file;
	}
	
	/**
	 * Getter for prefix_nodes_to_be_deleted.
	 * @return The unique prefix of the labels of the nodes created during the conversion to CNF.
	 */
	public String getPrefixe() {
		return this.prefix_nodes_to_be_deleted;
	}

}
