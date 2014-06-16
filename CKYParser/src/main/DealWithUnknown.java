package main;

/**
 * three modes of dealing with unknown words
 * @author yuliya
 * IGNORE: if an unknown terminal occurs in the corpus, then the probability of producing it is taken to be zero
 * APRIORI_PROB: 
 *
 */
public enum DealWithUnknown { IGNORE, APRIORI_PROB, RARE }
