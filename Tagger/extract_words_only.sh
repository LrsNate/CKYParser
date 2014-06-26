
set - o nounset
## ///////////////////////////////////
## extract the sequence of morphosyntactic categories from
## each tagged phrase
## ///////////////////////////////////

cat /dev/stdin | awk '
($0 != "") {
 s = "";
 for (i = 1; i < NF+1; i++) {
  n = split($i, a, "/");
  ## the tag is always the last one after the last slash 
  word = a[1];
  for (j = 2; j < n; j++) {
    word = word + "/" + a[j];
  }
  s = s " " word;
 }
 print s;
}
'

