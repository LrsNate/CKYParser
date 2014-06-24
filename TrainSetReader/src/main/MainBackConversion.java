package main;

import java.io.FileNotFoundException;
/**
 * Convert all phrases given by a grammar converted to CNF back by eliminating all the auxiliary non-terminals 
 * added during the conversion to CNF.
 *
 */
public class MainBackConversion {

	public static final void main(String[] argv) {
		BackConversionArgParser arg_parser = new BackConversionArgParser(argv);
		try {
			BackConversion C = new BackConversion(arg_parser.getInputFile());
			Environment.setLexical(true);
			C.convert(arg_parser.getPrefixe(), arg_parser.getModeProbabilistic());
		} catch (IllegalArgumentException e) {
			Messages.warning(e.getMessage());
		} catch (FileNotFoundException e1) {
			Messages.warning(e1.getMessage());
		}
	}

}
