package main;

/**
 * three modes of dealing with unknown words
 * @author yuliya
 * IGNORE: if an unknown terminal occurs in the corpus, then the probability of producing it is taken to be zero
 * APRIORI_PROB: use an apriori probability of getting an unknown word in a category, which is set to be the same for every category
 * RARE: The probabilities of getting an unknown word are supposed to be already present in the grammar
 *
 */
public enum DealWithUnknown { IGNORE, APRIORI_PROB, RARE }
