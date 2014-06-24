
set -o nounset
set -o pipefail
set -o errexit
# //////////////////////////////////////
f_input="$1"

f_output_stab="$2"

# ----- 
## p1 = the portion of the input lines that go to the training set
p1="0.6"
## p2 = the portion of the input lines that go to the developpement set
p2="0.2"
## p3 = (1 - p1 - p2) = the portion of the input lines that go to the test set
# ------
## the seed of the random number generator
seed="123"
# ///////////////////////////////////////

cat "$f_input" |
awk -v p1="$p1" -v p2="$p2" -v f_output_stab="$f_output_stab" -v seed="$seed" '
BEGIN {
 srand(seed);
 ## get the cumulative probabilities
 cum_p[0] = p1; cum_p[1] = p1 + p2; 
 cum_p[2] = 1;
 ## choose the output file names
 # training set
 f[0] = f_output_stab ".train"
 # developpement set
 f[1] = f_output_stab ".dev"
 # test set
 f[2] = f_output_stab ".test"
}
## choose the file to which the current line is to be printed to
## (according to the proportions given in the parameters p1, p2)
function pick_set() {
 alpha = rand();
 i = 0;
 while (alpha > cum_p[i]) { i++; }
 return f[i];
}
{
 output_file = pick_set();
 print > output_file;
}
'

