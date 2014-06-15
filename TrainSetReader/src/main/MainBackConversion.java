package main;

import java.io.FileNotFoundException;

public class MainBackConversion {

	public static final void main(String[] argv) {
		BackConversionArgParser arg_parser = new BackConversionArgParser(argv);
		try {
			BackConversion C = new BackConversion(arg_parser.getInputFile());
			C.convert(arg_parser.getPrefixe(), arg_parser.getModeProbabilistic());
		} catch (IllegalArgumentException e) {
			Messages.warning(e.getMessage());
		} catch (FileNotFoundException e1) {
			Messages.warning(e1.getMessage());
		}
	}

}
