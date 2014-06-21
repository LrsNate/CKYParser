package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class CKYArgumentParser extends ArgumentParser0 {
	
	/**
	 * whether to use log-probabilities in the probabilistic parsing algorithm
	 */
	private boolean _mode_log = false;
	/**
	 * if set to true, then the k best parses are returned, each with a probability
	 * if set to false, then only the best parse is returned for every sentence, 
	 * probabilities are not included in the result
	 */
	private boolean _get_k_best = false;
	/**
	 * The number of best parses to be returned for every phrase
	 */
	private int 	_k = 1;
	
	/**
	 * the method of deailing with unknown words
	 */
	private DealWithUnknown deal_with_unknown = DealWithUnknown.IGNORE; 
	
	private double 	_apriori_unknown_prob;
	private String 	_unknown_label;
	
	private BufferedReader 	_input_grammar = null;
	
	public int getKBest() {
		return this._k;
	}
	
	public boolean getLogMode() {
		return this._mode_log;
	}
	
	public double getAprioriUnknownProb() {
		return _apriori_unknown_prob;
	}
	
	public String getUnknownLabel() {
		return this._unknown_label;
	}
	
	public BufferedReader getGrammarBufferedReader() {
		return this._input_grammar;
	}
	
	public DealWithUnknown getModeTreatUnknown() {
		return this.deal_with_unknown;
	}
	
	private void checkPresenceOfObligatoryArgs() throws MissingObligatoryArgException {
		if (this._input_grammar == null) {
			throw new MissingObligatoryArgException("Missing the grammar file in the arguments.");		
		}
		return;
	}

	/**
	 * Builds a new ArgumentParser and parses the wordtab given in argument.
	 * @param argv Originally the program's command line arguments.
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
			}
			i++;
		}
		checkPresenceOfObligatoryArgs();
	}
	
	private void parseKBest(String argv[], int idx)
			throws NumberFormatException, MissingArgumentValueException
	{
		this._k = ArgumentParser0.parsePositiveInt(argv, idx);
		Messages.info(String.format("Number of best parses to be returned for every phrase = %d",
				this._k));
	}
	
	private void parseAprioriProb(String argv[], int idx)
			throws NumberFormatException, MissingArgumentValueException
	{
		this._apriori_unknown_prob = parsePositiveProb(argv, idx);
		Messages.info(String.format("For every morpho-syntactic category the apriori probability of getting an unknown word is set to = %f",
				this._apriori_unknown_prob));
	}
	
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
