
cat /dev/stdin | awk '{gsub(",", ".", $1); print $0;}'

