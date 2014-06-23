package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Class for parsing the command-line arguments of the CKY parser module.
 * 
 */
public class CKYArgumentParser extends ArgumentParser0 {
	
	/**
	 * Set to true to use log-probabilities in the probabilistic parsing algorithm.
	 */
	private boolean _mode_log = false;
	/**
	 * If set to true, then the k best parses are returned, each with a probability.
	 * If set to false, then only the best parse is returned for every sentence, 
	 * probabilities are not included in the result.
	 */
	private boolean _get_k_best = false;
	
	/**
	 * The number of best parses to be returned for every phrase.
	 */
	private int 	_k = 1;
	
	/**
	 * The method of dealing with unknown words. Is set to IGNORE by default (the probability of producing an unknown word is taken to be zero).
	 */
	private DealWithUnknown deal_with_unknown = DealWithUnknown.IGNORE; 
	
	/**
	 * The apriori probability of producing an unknown word.
	 */
	private double 	_apriori_unknown_prob;
	
	/**
	 * The unique terminal symbol that represents all the "unknown" words (token "UNKNOWN").
	 */
	private String 	_unknown_label;
	
	/**
	 * Set to true if each input phrase consists of terminal symbols only.
	 * Set to false if each input phrase consists of non-terminal symbols only.
	 */
	private boolean 	_input_is_lexical = true;
	
	/**
	 * The file containing the input PCFG to use for parsing. Obligatory argument.
	 */
	private BufferedReader 	_input_grammar = null;
	
	/**
	 * The output file for writing the results (parse trees).
	 */
	private PrintWriter 	_output_stream = null;
	
	public void closeOutput() {
		this._output_stream.close();
	}
	
	/**
	 * Getter for _input_is_lexical.
	 * @return True if each input phrase consists of terminal symbols only,
	 * false if each input phrase consists of non-terminal symbols only.
	 */
	public boolean getInputIsLexical() {
		return this._input_is_lexical;
	}
	
	/**
	 * Getter for _output_stream.
	 * @return The PrintWriter to the output file.
	 */
	public PrintWriter getOutputFile() {
		return this._output_stream;
	}
	
	/**
	 * Set the output stream to standard output.
	 */
	private void setDefaultOutputStream() {
		this._output_stream = new PrintWriter(System.out, true);
	}
	
	/**
	 * Getter for _k. Returns 1 best tree if _get_k_best is false.
	 * @return The number of best parse trees that the parser must provide.
	 */
	public int getKBest() {
		if (this._get_k_best) {
			return this._k;
		}
		return 1;
	}
	
	/**
	 * Getter for _mode_log.
	 * @return True if using log-probabilities in the probabilistic parsing algorithm.
	 */
	public boolean getLogMode() {
		return this._mode_log;
	}
	
	/**
	 * Getter for _apriori_unknown_prob.
	 * @return The apriori probability of producing an unknown word.
	 */
	public double getAprioriUnknownProb() {
		return _apriori_unknown_prob;
	}
	
	/**
	 * Getter for _unknown_label.
	 * @return The unique terminal symbol that represents all the "unknown" words.
	 */
	public String getUnknownLabel() {
		return this._unknown_label;
	}
	
	/**
	 * Getter for _input_grammar.
	 * @return A BufferedReader from the file with the grammar rules.
	 */
	public BufferedReader getGrammarBufferedReader() {
		return this._input_grammar;
	}
	
	/**
	 * Getter for deal_with_unknown.
	 * @return The mode of dealing with unknown words chosen for the parser.
	 */
	public DealWithUnknown getModeTreatUnknown() {
		return this.deal_with_unknown;
	}
	
	/**
	 * Check if the grammar input file is present.
	 * @throws MissingObligatoryArgException
	 */
	private void checkPresenceOfObligatoryArgs() throws MissingObligatoryArgException {
		if (this._input_grammar == null) {
			throw new MissingObligatoryArgException("Missing the grammar file in the arguments.");		
		}
		return;
	}

	/**
	 * Build a new ArgumentParser and parse the word list given in argument.
	 * @param argv Originally the program's command-line arguments.
	 */
	public CKYArgumentParser(String[] argv) throws MissingArgumentValueException, NumberFormatException, FileNotFoundException, MissingObligatoryArgException {
		int i = 0;
		while (i < argv.length)
		{
			if (argv[i].equals("-l") || argv[i].equals("--log_prob"))
			{
				this._mode_log = true;
			} else if (argv[i].equals("-k") || argv[i].equals("--k_best"))
			{
				this._get_k_best = true;
				parseKBest(argv, i);
				i++;
			} else if (argv[i].equals("-a") || argv[i].equals("--apriori_unknown_prob"))
			{
				this.deal_with_unknown = DealWithUnknown.APRIORI_PROB; 
				parseAprioriProb(argv, i);
				i++;
			} else if (argv[i].equals("-u") || argv[i].equals("--unknown_label"))
			{
				this.deal_with_unknown = DealWithUnknown.RARE; 
				checkArgumentPresence(argv, i);
				this._unknown_label = argv[i+1];
				i++;
			}  else if (argv[i].equals("-g") || argv[i].equals("--grammar_file"))
			{
				checkArgumentPresence(argv, i);
				this._input_grammar = ArgumentParser0.openFile(argv[i+1]);
				i++;
			} else if (argv[i].equals("-o") || argv[i].equals("--output_file"))
			{
				checkArgumentPresence(argv, i);
				this._output_stream = ArgumentParser0.openOutputFile(argv[i+1]);
				i++;
			} else if (argv[i].equals("-n") || argv[i].equals("--non_lexical_input"))
			{
				this._input_is_lexical = false;
			}
			i++;
		}
		checkPresenceOfObligatoryArgs();
		if (this._output_stream == null) {
			setDefaultOutputStream();
		}
	}
	
	/**
	 * Get the number of best parses that the parser must give.
	 * @param argv Command-line arguments.
	 * @param idx The position of the argument in the list.
	 * @throws NumberFormatException
	 * @throws MissingArgumentValueException
	 */
	private void parseKBest(String argv[], int idx)
			throws NumberFormatException, MissingArgumentValueException
	{
		this._k = ArgumentParser0.parsePositiveInt(argv, idx);
		Messages.info(String.format("Number of best parses to be returned for every phrase = %d",
				this._k));
	}
	
	/**
	 * Get the apriori probability of unknown words.
	 * @param argv Command-line arguments.
	 * @param idx The position of the argument in the list.
	 * @throws NumberFormatException
	 * @throws MissingArgumentValueException
	 */
	private void parseAprioriProb(String argv[], int idx)
			throws NumberFormatException, MissingArgumentValueException
	{
		this._apriori_unknown_prob = parsePositiveProb(argv, idx);
		Messages.info(String.format("For every morpho-syntactic category the apriori probability of getting an unknown word is set to = %f",
				this._apriori_unknown_prob));
	}
	
	/**
	 * Get a probability passed as a command-line argument.
	 * @param argv Command-line arguments.
	 * @param idx The position of the argument in the list.
	 * @return A probability.
	 * @throws MissingArgumentValueException
	 * @throws NumberFormatException
	 */
	protected static double parsePositiveProb(String[] argv, int idx) throws MissingArgumentValueException, NumberFormatException {
		checkArgumentPresence(argv,idx);
		double res = Double.parseDouble(argv[idx + 1]);
		if ((!(res > 0)) || (res > 1))
			throw new NumberFormatException(String.format(
					"The value of the argument %s must be positive (non-zero) and not greater than 1",
					argv[idx]));
		return res;
	}

}
