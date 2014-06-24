
set -o nounset
#/////////////////////////////
f_gold="$1"
f_test="$2"
# ////////////////////////////
dir=`dirname $0`
EVALB="$dir""/evalb"
f_evalb_parameters="$dir""/evalb_param.prm"

"$EVALB" -p "$f_evalb_parameters" "$f_gold" "$f_test"

