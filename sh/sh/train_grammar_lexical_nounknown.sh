
set -o nounset
set -o pipefail
set -o errexit
# //////////////////////////////////////
JAVA_TRAIN_PCFG="../TrainSetReader/Train"
# /////// Input arguments //////////////
f_sequoia=~/Paris-Diderot/2ieme_semestre/Projet/Sequoia/sequoia-corpus-v4.0/frwiki_50.1000+fct.mrg_strict
precision="7"
nthreads="1"

cat $f_sequoia | 
java $JAVA_TRAIN_PCFG --precision "$precision" --nthreads "$nthreads" --lexical

