
set -o nounset
set -o errexit
# /////////////////////////////////////////
f_non_lexical_parse="$1"
f_tagged_phrases="$2"

cat "$f_non_lexical_parse" | sed 's/)/ )/g' | awk -v f_tagged_phrases="$f_tagged_phrases" '
function is_terminal(s) {
 n = split(s, a, "");
 i = 1;
 while ((i < n+1) && (a[i] == "\\")) { i++; }
 i = i - 1;
 return ((i % 2) != 0);
}
function get_lexical_terminal() {
 print "( " word_number, nwords " )";
 if (word_number > nwords) {
   print "ERROR: length mismatch between the non-lexical parse and the tagged phrase";
   exit 1;
 }
 tagged_item = tagged_phrase[word_number];
 ni = split(tagged_item, tagged_item_split, "/");
 lex = tagged_item_split[1];
 ## concatenate back all but the last element of the split,
 ## which is the morphosyntactic category
 for (ii = 2; ii < ni; ii++) {
   lex = lex "/" tagged_item_split[ii];
 }
 return lex;
}
{
  delete tagged_phrase; delete parse;
  numero_de_la_phrase = $1; print "field 1 = " $1;
  gsub("*", "", numero_de_la_phrase);
  print "numero = " numero_de_la_phrase;
  ## get the line with the parse
  getline; str_parse = $0; print "str_parse = " $0;
  if (!((getline str_tagged_phrase < f_tagged_phrases) > 0)) {
        print "ERROR: Not enough lines in the file with tagged phrases.";
  	exit 1;
  }
  if (str_parse == "(NULL null)") { print str_parse; next; }
  nwords = split(str_tagged_phrase, tagged_phrase, " "); 
  n_items_in_parse = split(str_parse, parse, " ");
  res = ""; k = 1; word_number = 0;
  while (k < n_items_in_parse+1) {
      parse_item = parse[k];
      if (!(is_terminal(parse_item))) {
        res = res " " parse_item;
      } else {
        print "is_terminal = " parse_item; 
        word_number++;
        res = res " " get_lexical_terminal();
      }
    k++;
  }
  print res;
}
' | sed 's/ )/)/g' | sed -e 's/^ *//'

