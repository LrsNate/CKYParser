
dir=$(dirname $0)

cat /dev/stdin | awk '($0 != "")' | python "$dir""/cnf-converter.py" -t -p

