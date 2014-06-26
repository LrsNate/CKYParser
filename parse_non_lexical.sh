
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
TAGS_extract="$DIR""/Tagger/extract_tags_only.sh"
CKYParser="$DIR""/CKYParser.jar"

## fichier temporarire pour la phrase tagge
f_tagged_phrase="$(mktemp)"
## call MElt with the tokenization option turned on
cat /dev/stdin | "$MELT_tagger" -t > "$f_tagged_phrase"

echo "============== Tagging: =============="
cat "$f_tagged_phrase"

f_non_lexical_parse="$(mktemp)"
## extraire les tags et les passer au parseur CKY
cat "$f_tagged_phrase" | "$TAGS_extract" | 
java -jar "$CKYParser" -k 5 --log_prob -g "$f_grammar" > "$f_non_lexical_parse"

echo "============== Non-lexical parse: =============="
cat "$f_non_lexical_parse"

## -------- reinserer les elements lexicaux -------
REINSERT="$DIR""/sh/ReinsertLexicals/reinsert_lexicals_for_k_best.sh"

echo "============== Lexical parse: =============="
"$REINSERT" "$f_non_lexical_parse" "$f_tagged_phrase"




