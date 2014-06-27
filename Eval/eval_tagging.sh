## === Evaluation of the tagger ===
## The script 
## takes two files each containing sequences of tags;
## the files are supposed to be aligned by lines
## in a way that the i-th line of the first file 
## contains the sequence of tags for the same phrase
## as does the i-th line of the second file
# /////////////////////////////////////////
set -o nounset
set -o pipefail
set -o errexit
# ////////Input arguments /////////////////
## the gold tagging
f_tags_gold="$1"
## the tagging to be evaluated
f_tags="$2"
# /////////////////////////////////////////

cat "$f_tags" | sed -e 's/^ *//g' | awk -v f_tags_gold="$f_tags_gold" '
BEGIN {
 correct = 0; ntokens_total = 0;
}
{
  str_tags = $0; nr = NR;
  if (!((getline str_gold < f_tags_gold) > 0)) {
    print nr;
    print "ERROR: Not enough lines in the file with gold tags.";
  	exit 1;
  }
  ngold = split(str_gold, gold, " "); 
  ntags = split(str_tags, tags, " ");
  if (ngold != ntags) {
    if (str_tags ~ "NULL") {
      ntokens_total += ntags;
      next;
    }
      print nr;
      print "ERROR: length mismatch during tagger evaluation";
      exit 1;
  }
  ntokens_total += ntags;
  for (i = 1; i < ntags+1; i++) {
      if (gold[i] == tags[i]) {
          correct++;
      }
  }
}
END {
 ## print out the precision
 print "Precision = " correct / ntokens_total;
}
'

