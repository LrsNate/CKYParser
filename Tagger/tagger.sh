
set -o nounset
# /////////////////////////
## ===== Call the MElt tagger =====
# standard input : a text that is to be tagged 
# (and tokenised if necessary)

## the first argument is to be set to the string "-t"
## if the input is to be tokenised
input_to_be_tokenised="${1:-""}"
# /////////////////////////
dir=`dirname $0`
# TODO: change it to a local path to the MElt tagger
TAGGER="$dir""/melt-2.0b4/bin/MElt"

cat /dev/stdin | "$TAGGER" "$input_to_be_tokenised"

