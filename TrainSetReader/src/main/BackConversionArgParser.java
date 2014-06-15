package main;

import java.io.FileNotFoundException;

public class BackConversionArgParser {
	
	/**
	 * the file containing the parse trees in CNF form
	 * (one tree per line)
	 */
	private String input_file = ""; 
	/**
	 * the unique prefixe of the lables of the nodes created 
	 * during the conversion to CNF
	 */
	private String prefixe_nodes_to_be_deleted = "";
	
	/**
	 * set to true if each tree in the input file is preceded by a probability value
	 */
	private boolean mode_probabilistic = false;
	
	/**
	 * Parses the command line arguments to be passed to the BackConversion class 
	 * @param argv: 
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
				this.prefixe_nodes_to_be_deleted = argv[i];
			}
			else if (argv[i].equals("--prob"))
			{
				this.mode_probabilistic = true;
			}
			i++;
		}
	}
	
	/**
	 * checks if all the necessary parameters have been passed via the command line arguments
	 * @throws IllegalArgumentException if there are missing command line arguments
	 */
	public void check() {
		if (input_file.equals("")) {
			throw new IllegalArgumentException("The argument --input_file not set.");
		}
		if (prefixe_nodes_to_be_deleted.equals("")) {
			throw new IllegalArgumentException("The argument --prefixe not set.");
		}
	}
	
	public boolean getModeProbabilistic() {
		return this.mode_probabilistic;
	}
	
	public String getInputFile() {
		return this.input_file;
	}
	
	public String getPrefixe() {
		return this.prefixe_nodes_to_be_deleted;
	}

}
