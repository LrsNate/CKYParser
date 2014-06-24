
set -o nounset
set -o errexit
# /////////////////////////////////////////
## given the output of the syntactic parser
## that prints out the bare phrases first
## and the parse tree then,
## extracts the parse trees only 

# /////////////////////////////////////////

cat /dev/stdin | awk '
($1 !~ /\*\*[0-9]+\*\*/) && ($0 ~ /\(|\)/) 
'

