
set -o nounset
#/////////////////////////////
f_gold="$1"
f_test="$2"

evalb -p evalb_param.prm "$f_gold" "$f_test"

