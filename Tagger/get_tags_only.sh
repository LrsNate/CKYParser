
set -o pipefail
set -o errexit
# /////////////////////
dir=`dirname $0`
TAG="$dir""/tagger.sh"
EXTRACT="$dir""/extract_tags_only.sh"

cat /dev/stdin | "$TAG" | "$EXTRACT"

