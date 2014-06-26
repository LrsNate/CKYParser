
set -o nounset
set -o pipefail
set -o errexit
# ///////////////////////////////////
# ------ input arguments ------------
## grammar file
f_grammar="$1"
# -----------------------------------
DIR=`dirname $0`
MELT_tagger="$DIR""/Tagger/tagger.sh"
WORDS_extract="$DIR""/Tagger/extract_words_only.sh"
CKYParser="$DIR""/CKYParser.jar"

## call MElt with the tokenization option turned on
f_tagged_phrase="$(mktemp)"
f_tokens="$(mktemp)"
cat /dev/stdin | "$MELT_tagger" -t > "$f_tagged_phrase"
## en extraire la tokenisation
cat "$f_tagged_phrase" | "$WORDS_extract" > "$f_tokens"  

echo "============== Tokenization: =============="
cat "$f_tokens"

f_parse="$(mktemp)"
## appel au parseur CKY
cat "$f_tokens" |
java -jar "$CKYParser" -k 5 --log_prob -g "$f_grammar" > "$f_parse"

echo "============== Parsing: =============="
cat "$f_parse"

## -------- reinserer les elements lexicaux -------
REINSERT="$DIR""/sh/ReinsertLexicals/reinsert_lexicals_for_k_best.sh"

echo "============== Lexical parse: =============="
"$REINSERT" "$f_parse" "$f_tagged_phrase"




