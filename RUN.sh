
DIR=~/Paris-Diderot/2ieme_semestre/Projet/PROJET2-git/CKYParser/

f_train="$DIR""/RESULTS/corpus_arbore/corpus_arbore.train"
f_grammar_non_lex="$DIR""/RESULTS/PCFG/lowercase/pcfg_non_lex.txt--1"
f_grammar_lex="$DIR""/RESULTS/PCFG/lowercase/pcfg_lex.txt--1"

TRAIN_GRAMMAR="$DIR""/TrainSetReader.jar"

CMD_grammar_format_issues="$DIR""/sh/grammar_format_issues.sh"

cat $f_train | java -jar "$TRAIN_GRAMMAR" -p 7 -t 1 | "$CMD_grammar_format_issues" > $f_grammar_non_lex
cat $f_train | java -jar "$TRAIN_GRAMMAR" -p 7 -t 1 -l | "$CMD_grammar_format_issues" > $f_grammar_lex

CNFConverter="$DIR""CNFConverter/cnf-converter.sh"

cat $f_grammar_non_lex | "$CNFConverter" > $f_grammar_non_lex".CNF"
f_grammar_non_lex=$f_grammar_non_lex".CNF"
cat $f_grammar_lex | "$CNFConverter" > $f_grammar_lex".CNF"
f_grammar_lex=$f_grammar_lex".CNF"

f_corpus_arbore_dev="$DIR""/RESULTS/corpus_arbore/corpus_arbore.dev"
f_dev_bare_phrases="$DIR""/RESULTS/corpus_dev_bare/corpus.bare_phrases.txt--1"
f_dev_tags_only="$DIR""/RESULTS/corpus_dev_bare/corpus.tagging_only.txt--1"
f_dev_tagged_phrases="$DIR""/RESULTS/corpus_dev_bare/corpus.tagged_phrases.txt--1"

ANNOTATION_STRIPPER="$DIR""/AnnotationStripper.jar"

cat "$f_corpus_arbore_dev" | java -jar "$ANNOTATION_STRIPPER" --get-categories > "$f_dev_tags_only"
cat "$f_corpus_arbore_dev" | java -jar "$ANNOTATION_STRIPPER" --get-bare-phrase > "$f_dev_bare_phrases"
cat "$f_corpus_arbore_dev" | java -jar "$ANNOTATION_STRIPPER" --get-tagged-phrase > "$f_dev_tagged_phrases"

f_dev_non_lex="$f_dev_tags_only"

f_parse_non_lex="$DIR""/RESULTS/ParseTrees/dev/dev_parse_non_lex.txt--1"
cat $f_dev_non_lex | java -jar CKYParser.jar  -k 1  -g "$f_gram_non_lex" > \
$f_parse_non_lex

CMD_reinsert="$DIR""/sh/ReinsertLexicals/reinsert_lexicals.sh"
f_tagged_phrases="$f_dev_tagged_phrases"
f_reinserted=$f_parse_non_lex".reinsert"

$CMD_reinsert $f_parse_non_lex $f_tagged_phrases > $f_reinserted

BACKConverter="$DIR""/BackConverter.jar"

f_back_converted=$f_reinserted".back_converted"
java -jar "$BACKConverter" --input_file "$f_reinserted" --prefixe "Z" > $f_back_converted

f_gold="$f_corpus_arbore_dev"
EVALB="$DIR""/Eval/EVALB/run_evalb.sh"

$EVALB $f_gold  $f_back_converted >  $f_back_converted".EVALB"

