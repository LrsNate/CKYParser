package main;
/**
 * Choose the format of the phrases.
 * LEX: Only words without their tags (bare phrase).
 * CAT: Only categories without lexical information.
 * LEX_N_CAT: Phrase with tags.
 */
public enum AnnotationStripperOption {
	LEX, CAT, LEX_N_CAT
}
