
cat /dev/stdin | awk '{ 
 gsub(",",".",$1);
 s = $1; 
 for (i=2; i < NF+1; i++) { s = s " " $i;} print s; 
}'

