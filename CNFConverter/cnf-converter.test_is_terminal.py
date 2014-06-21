#!/usr/bin/env python
# -*- coding: iso-8859-1 -*-

# import du module sys pour les entrées sorties
import sys
# import du module optparse pour la gestion des options / arguments en ligne de commande
from optparse import OptionParser
import warnings

# chaîne de caractères qui sera affichée si l'option -h est précisée, ou si les options passées ne sont pas celles déclarées
usage=u""" Ce programme lit de STDIN une grammaire hors-context (probabiliste ou non) (CFG ou PCFG) 
            et affiche sur STDOUT la grammaire transformée 
            en Forme Normale de Chomsky (CNF) (sans productions singulières)
            ou
            en Forme Normale de Chomsky Flexible (flexible CNF) (pas d'élimination des productions singulières)
            (selon l'option choisie)
            La grammaire de l'entrée est lit au format suivant
           1. Une regle de production par ligne 
           2. Chaque ligne au format suivant:
            en cas de PCFG: p A -> beta
	    (où p est la probabilité de lq règle)
            en cqs de CFG: A -> beta
            ( field separator : whitespace characters )
              Terminal symbols: start with a non-escaped slash

           %prog [options]
"""

# ==================   Les options    ===============================================
# gestion des options et des arguments
parser=OptionParser(usage=usage)
# déclaration d'une option
parser.add_option("-f", "--transformation_file",dest="f_trans",default="",help=u"File for storage of transformations", metavar="F_TRANS")
parser.add_option("-p", "--prob",  action="store_true",  dest="is_prob", help = u"The CFG is probabilistic")
parser.add_option("-s", "--sing_elimin",  action="store_true",  dest="sing_elimin", help = u"Eliminate singular productions")
parser.add_option("-t", "--term_droite",  action="store_true",  dest="term_droite", 
help = u"Indicate that there may be non-terminal symbols in productions that have more than one symbol in the right-hand side")
parser.add_option("--save_transform",action="store_true",  dest="save_transform", help = u"Save the transformations")

# lecture des arguments et des options passés en ligne de commande
(opts,args) = parser.parse_args()

sing_elimin = bool(opts.sing_elimin)
term_droite = bool(opts.term_droite)
is_prob = bool(opts.is_prob)
# =============================================================================
# --- 
## Notation used throughout the script: 
# LHS : left hand  side of production rule
# RHS: right hand side of production rule
# ---

# --- 
class CNFConvert:
    def __init__(self, is_prob, new_name = "Z"):
        self.new_name = new_name
        self.is_prob = is_prob
        # append_p:  the (conditional) probability of  each of the rewriting rules of the new nonterminal symbols 
        #               created by the transformation algorithm
        # shift_index: the number of the column of standard input at which the rewriting rule begins
        if (self.is_prob):
            self.append_p = "1.0 "
            self.shift_index = 1
        else:
            self.append_p = ""
            self.shift_index = 0
      
      
    def is_terminal(self, s):
        i=0;
        count_first_slashes=0;
        while ((i < len(s)) & (s[i] == '\\')):
            count_first_slashes += 1;
            i += 1;
        return ((count_first_slashes % 2) != 0);

    def generate_new_nonterminal(self, transform):
        # n_new_NT: number of new non-terminals created by the transformation algorithm            
        n_new_NT = len(transform) + 1                        
        return (self.new_name + str(n_new_NT))


    # make sure that terminals appear only in rules of the type (A -> a)
    # (i.e. no multiple terminals in RHS, no combinations of terminals and non-terminals in RHS)
    def process_terminals(self, rhs, transform):
        if (len(rhs) > 1):
            for i in range(len(rhs)):
                if (self.is_terminal(rhs[i])):
                    replace_symbol = transform.get(rhs[i])
                    # Comparisons to singletons like None should always be done 
                    # with 'is' or 'is not', never the equality operators.
                    if (replace_symbol is None):
                        new_nonterminal = self.generate_new_nonterminal(transform) 
                        new_rule = (rhs[i], new_nonterminal)
                        transform.update([new_rule])
                        self.print_rewriting_rule(new_rule, self.append_p)
                        replace_symbol = new_nonterminal
                    rhs[i] = replace_symbol
     
    def print_rewriting_rule(self, rhs_lhs, prob_string):
        print prob_string + rhs_lhs[1] + " -> " + rhs_lhs[0]
    
    ### NOTE: if (store_old_rules = True) then assume that STDIN is sorted by the length of the RHS in increasing order
    def convert(self, input_stream, term_droite = False, store_old_rules = False):
        # transform: the dictionary containing rewriting rules for the newly created non-terminal symbols
        #               what the dictionary stores: 
        #                       new_nonterminal  ->  rewriting rule
        #               how it stores it:
        #                       { RHS => LHS } (inverted form)
        #                       (RHS in string format)
        transform = {}
        # n_lines_in_stdin: number of lines read from standard input
        n_lines_in_stdin = 0
        p_string = ""
        line = input_stream.readline()
        while line:
            ### parse standard input
            n_lines_in_stdin += 1
            line = line.strip('\r').strip('\n')
            regle = line.split()
            # p: the (conditional) probability of the rewriting rule P(RHS | LHS)
            if (self.is_prob):
                p_string = str(regle[0]) + " " 
            lhs = regle[self.shift_index]
            # +2 instead of +1 to skip the "->" symbol
            rhs = regle[(self.shift_index+2):]
            ### if the right hand side might contain more than one terminal symbol
            ###     or a combination of  terminal(s) and non-terminals,
            ### preprocess the right-hand side 
            if (term_droite):
	    	    self.process_terminals(rhs, transform)
            ### transform the current production rule
            ###     to a set of binary production rules
            n = len(rhs) - 1
            NT = rhs[n]
            while (n > 1):
                # to_replace: the pair of non-terminals to be "replaced" by a new rule
                to_replace = rhs[n-1] + " " + NT
                new_nonterminal = transform.get(to_replace)
                if (new_nonterminal is None):
                    new_nonterminal = self.generate_new_nonterminal(transform)
                    new_rule = (to_replace, new_nonterminal)                                               
                    transform.update([new_rule])
                    self.print_rewriting_rule(new_rule, self.append_p)
                NT = new_nonterminal
                n -= 1
            if (n > 0):
                rule = (rhs[0] + " " + NT, lhs)
            else:
                rule = (rhs[0], lhs)
            self.print_rewriting_rule(rule, p_string)
            line = input_stream.readline()
        
        if (not n_lines_in_stdin):
            warnings.warn("Standard input: empty")

# =============================================
input_stream = sys.stdin
C = CNFConvert(is_prob)
line = input_stream.readline() 
while line:
    line = line.strip('\r').strip('\n');
    print line + " " + str(C.is_terminal(line));
    line = input_stream.readline()

