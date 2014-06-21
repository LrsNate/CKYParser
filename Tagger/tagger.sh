
set -o nounset
# /////////////////////////
input_already_tokenised="${1:-""}"

# TODO: change it to a local path to the MElt tagger
TAGGER="/home/yuliya/Paris-Diderot/2ieme_semestre/Projet/Tokenisation/melt-2.0b4/bin/MElt"

cat /dev/stdin | "$TAGGER" "$input_already_tokenised"

