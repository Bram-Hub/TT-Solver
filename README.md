# TT ATP
## Authors
2003:
James Bell

## About
I implemented a simple Java Truth Tree GUI for solving satisfiability problems.
You will need to download the SDK to run the .java files.  To run, use commands:

javac Formula.java TruthTreeGUI.java
java TruthTreeGUI


TruthTreeGUI.java is the main program which accepts the input from the calculator-like
GUI, creates a tree, and searches for open/closed branches.  This is done using the
input as the root (such as A & B & C), and one at a time takes individual clauses (as the 
input should be in Clausal Normal Form) as each subroot( so C is the right subtree). 
The program will combine C with A and with B to form the subroots of C.  This is done
for efficiency.  Instead of checking all possible combinations for one answer, since
C must be satisfiable and is known to be the last clause without an O(N) count of clauses,
C is checked with each other clause.  Thus, the tree is expanded by checking if A & C
are satisfiable and then if B & C are satisfiable.  Then a quick O(logN) check is done
to see if there are any open branches, if so returns the satisfied equation, else the
input is unsatisfiable.

Formula.java is the class for all the individual formulas within the tree.  This
class allows for all manipulation, taking care of double negations and implications etc.,
when the tree is expanded simply by manipulating the strings.

When the program is run, a tree is output which shows the tree expansion exactly 
as it occurred for better understanding.  Also, I have heavily commented the code so 
it can be understood, and I have JavaDoc-style commented the two files so you 
can create HTML from them and understand the program hierarchy.

If you have any further questions, email me.
