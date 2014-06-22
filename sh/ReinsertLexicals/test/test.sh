
../reinsert_lexicals.sh dev.head2.parse dev.head2.tagged_phrases > result.reinserted
tmp=$(mktemp)
head -n 2 ~/Paris-Diderot/2ieme_semestre/Projet/PROJET1-git/CKYParser/RESULTS/corpus_arbore/corpus_arbore.dev > "$tmp"
diff result.reinserted "$tmp"
rm "$tmp"
