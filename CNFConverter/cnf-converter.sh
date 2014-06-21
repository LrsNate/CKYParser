
cat /dev/stdin | awk '($0 != "")' | python ./cnf-converter.py -t -p

