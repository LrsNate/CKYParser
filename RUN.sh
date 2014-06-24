
set -o nounset
set -o pipefail
set -o errexit

## //////////// Input parameters ///////////////////

## mode_non_lexical=1 si les categories morpho-syntaxiques \
##   sont  obtenues par un outil exterieur ou issues du corpus gold
##  mode_non_lexical=0 sinon
mode_non_lexical=1

## tagging_gold=0 si le tagger MElt est utilise pour le tagging en mode non-lexical
## tagging_gold=1 pour utiliser le tagging gold
tagging_gold=1

## /////////////////////////////////////////////////////
if [ "$mode_non_lexical" -gt "0" ]
then
mode_lexical=0
else
mode_lexical=1
fi

## le répositoire où se trouvent tous les scripts, programmes aussi bien que les données
## et où les résultats sont éventuellement stockés
DIR=`dirname $0`

## le corpus arboré Sequoia Tree Bank
f_sequoia="$DIR""/data/frwiki_50.1000+fct.mrg_strict"

## le dossier où les résultats vont être stockés
DIR_RESULTS="$DIR""/RESULTS--1/"
mkdir -p "$DIR_RESULTS"

## le sous-dossier où les arbres syntaxiques vont être stockés
DIR_TREES="$DIR_RESULTS""/ParseTrees/"
mkdir -p "$DIR_TREES"

# ----------------------------------------------------------------------
echo "## --- découpage du corpus ---"
## --- découpage du corpus ---
DIR_CORPUS="$DIR_RESULTS""/corpus_arbore/"
mkdir -p "$DIR_CORPUS"
f_stem="$DIR_CORPUS""/corpus_arbore"

#### ===== CorpusSplitter =====
CMD_SPLIT="$DIR""/sh/CorpusSplitter/corpus_splitter.sh"

## les fichiers du découpage du corpus 
## en trois parties : 
# corpus d'entrainementet 
f_train_corpus_arbore="$f_stem"".train"
# de developpement 
f_dev_corpus_arbore="$f_stem"".dev"
# de test
f_test_corpus_arbore="$f_stem"".test"

## ---- appel à l'outil -----
"$CMD_SPLIT" "$f_sequoia" "$f_stem"

# ----------------------------------------------------------------------
## --- apprentissage de la grammaire ---
## le dossier où la grammaire va être stockée
GRAMMAR_DIR="$DIR_RESULTS""/PCFG/"
mkdir -p "$GRAMMAR_DIR"

## le fichier pour la grammaire depourvues des règles lexicales
## (si l'option correspondante est choisie)
f_grammar_non_lex="$GRAMMAR_DIR""/pcfg_non_lex.txt"
## le fichier pour la grammaire pourvues des règles lexicales
## (si l'option correspondante est choisie)
f_grammar_lex="$GRAMMAR_DIR""/pcfg_lex.txt"

## ===== TrainSetReader =====
TRAIN_GRAMMAR="$DIR""/TrainSetReader.jar"
CMD_grammar_format_issues="$DIR""/sh/grammar_format_issues.sh"

unknown_label="**UNKNOWN**"

#--- apprentissage de la grammaire depourvue de règles lexicales ---
## (si l'option correspondante est choisie)
if [ "$mode_non_lexical" -gt "0" ]
then
echo "--- apprentissage de la grammaire non-lexicale ---"
cat $f_train_corpus_arbore | java -jar "$TRAIN_GRAMMAR" -p 7 -t 1 | "$CMD_grammar_format_issues" > $f_grammar_non_lex
fi

# --- apprentissage de la grammaire pourvue de règles lexicales ---
## (si l'option correspondante est choisie)
if [ "$mode_lexical" -gt "0" ]
then
echo "--- apprentissage de la grammaire lexicale ---"
## l'option -l pour "lexical" est passée en parametres
cat $f_train_corpus_arbore | java -jar "$TRAIN_GRAMMAR" -p 7 -t 1 -l | "$CMD_grammar_format_issues" > $f_grammar_lex
fi

# ----------------------------------------------------------------------
## --- apprentissage de la grammaire ---
## le dossier où la grammaire va être stockée

## =====  CNFConverter =====
CNFConverter="$DIR""/CNFConverter/cnf-converter.sh"

# --- conversion de la grammaire en forme normale de Chomsky ---
if [ "$mode_non_lexical" -gt "0" ]
then
echo "--- parsing non-lexical ---"
cat $f_grammar_non_lex | "$CNFConverter" > $f_grammar_non_lex".CNF"
f_grammar_non_lex=$f_grammar_non_lex".CNF"
fi

if [ "$mode_lexical" -gt "0" ]
then
echo "--- parsing lexical ---"
cat $f_grammar_lex | "$CNFConverter" > $f_grammar_lex".CNF"
f_grammar_lex=$f_grammar_lex".CNF"
fi

# ----------------------------------------------------------------------
## --- la préparation des corpus gold ---
## --- pour des différentes étapes de l'évaluation ---

## des phrases non-annotées (mais tokénisées)
f_dev_bare_phrases="$f_dev_corpus_arbore"".bare_phrases"
## des séquences de catégories morpho-syntaxiques gold
f_dev_tags_only="$f_dev_corpus_arbore"".tagginng_only"
## des phrases taggées gold
f_dev_tagged_phrases="$f_dev_corpus_arbore"".tagged_phrases"

## ===== AnnotationStripper =====
ANNOTATION_STRIPPER="$DIR""/AnnotationStripper.jar"

echo "--- préparation des corpus gold (AnnotationStripper) ---"

cat "$f_dev_corpus_arbore" | java -jar "$ANNOTATION_STRIPPER" --get-bare-phrase > "$f_dev_bare_phrases"
cat "$f_dev_corpus_arbore" | java -jar "$ANNOTATION_STRIPPER" --get-categories > "$f_dev_tags_only"
cat "$f_dev_corpus_arbore" | java -jar "$ANNOTATION_STRIPPER" --get-tagged-phrase > "$f_dev_tagged_phrases"

## ==== CKYParser ====
CKYParser="$DIR""/CKYParser.jar"

## ------------------ Mode Non-lexical: 
##          les catégories morpho-syntaxiques 
##           sont obtenues par des outils externes 
##              ou issu du corpus gold
##              (  selon l'option gold_tagging de ce script  )
if [ "$mode_non_lexical" -gt "0" ]
then

## ------------ obtenir des séquences des catégories 
##                              morpho-syntaxiques --------------------
if [ "$tagging_gold" -gt "0" ]
then
 echo "--- utiliser le tagging gold comme données d'entrée du parseur ---"
 ## le corpus de séquences de tags gold
 f_dev_non_lex="$f_dev_tags_only"
else
 ## produire le corpus de séquences de tags 
 ## issues du tagger MElt
 MELT_get_tags_only="$DIR""/Tagger/get_tags_only.sh"
 f_dev_non_lex="$f_dev_bare_phrases"".melt-sequences-de-tags"
 cat $f_dev_bare_phrases | $MELT_get_tags_only > $f_dev_non_lex 
fi # fi tagging_gold

## ----- appeler le parseur -----
echo "--- PARSING ---"
f_parse="$DIR_TREES""/dev_parse_non_lexical.txt"
cat $f_dev_non_lex | java -jar "$CKYParser"  -k 1 -g "$f_grammar_non_lex" > $f_parse

fi # fi mode_non_lexical

## ------------------ Mode Lexical: 
##          les catégories morpho-syntaxiques 
##              aussi bien que l'arbre syntaxique
##           sont trouvés par le parseur

if [ "$mode_lexical" -gt "0" ]
then
## le corpus de phrases non-annotées
 f_dev_lex="$f_dev_bare_phrases"

echo "--- PARSING ---"
f_parse="$DIR_TREES""/dev_parse_lexical.txt"
cat $f_dev_lex | java -jar "$CKYParser"  -k 1 -g "$f_grammar_lex" > $f_parse

fi # mode_lexical

## ------------------ Reinsert  the lexical elements 
##                            into the parse trees ---------------

echo "--- Reinsert  the lexical elements into the parse trees ---"
## ====== ReinsertLexicals ======
CMD_reinsert="$DIR""/sh/ReinsertLexicals/reinsert_lexicals.sh"

f_tagged_phrases="$f_dev_tagged_phrases"

## le résultat de réinsertion d'éléments léxicaux
f_reinserted=$f_parse".reinsert"

## --- l'appel à l'outil de réinsertion des lexicaux
$CMD_reinsert $f_parse $f_tagged_phrases > $f_reinserted

## ------------------ Transformation des arbres syntaxiques
##                            de la forme normale de Chomsky
##                              à la forme de la grammaire d'origine ---------------

echo "= BackConverter ="
## ===== BackConverter =====
BACKConverter="$DIR""/BackConverter.jar"

f_back_converted=$f_reinserted".back_converted"
java -jar "$BACKConverter" --input_file "$f_reinserted" --prefixe "Z" > $f_back_converted

## ------------------ Evaluation ----------------

echo "--- Evaluation ---"
## ==== EVALB ====
EVALB="$DIR""/Eval/EVALB/run_evalb.sh"

f_gold="$f_dev_corpus_arbore"
$EVALB $f_gold  $f_back_converted >  $f_back_converted".EVALB"

