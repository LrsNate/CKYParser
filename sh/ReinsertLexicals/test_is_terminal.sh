
cat /dev/stdin |
awk '
function is_terminal(s) {                                                                    
  print s;
  n = split(s, a, "");                                                                        
  i = 1;
  while ((i < n+1) && (a[i] == "\\")) { i++; }
  i = i -1;
  print i;
  print "is_terminal = " ((i % 2) != 0);
}
{
 t = is_terminal($1);

}'
