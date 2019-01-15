import java.lang.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A Truth Tree GUI program using swing
 *
 * The program is used simply by inputing one formula with prefix notation and hitting compute.
 * Also, there is a clear button which clears the screen.
 *
 * @author James Bell
 * @version 1.0
 */

public class TruthTreeGUI extends JFrame implements ActionListener {
  TruthTreeGUI left,right, parent;  	// the two possible branches, one parent
  Formula label;		// the formula at this node
  JTextField jt;  //field of GUI

	/*
	* 
	* default constructor. 
	*/

	public TruthTreeGUI(){
 	
		left = null;
		right = null;
		parent = null;
		label = null;

		Container c = getContentPane();  //creates a container and sets up layout
		c.setLayout(new BorderLayout(10,10));

		jt = new JTextField();  //creates text field and sets attributes
		jt.setSize(100,20);
		jt.setFont(new Font("Arial",Font.BOLD,20));
		jt.setBackground(Color.WHITE);
		jt.setForeground(Color.BLACK);
		c.add(jt,BorderLayout.NORTH);
		c.add( createButtonPanel(), BorderLayout.CENTER );  //adds text bar - a JPanel

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,300);

		setVisible(true);
	}

	/*
	* Constructor
	* 
	* @param f the formula of the root
	* @param P the parent TruthTreeGUI
	*/
	public TruthTreeGUI(Formula f, TruthTreeGUI P) {
		left = null;
		right = null;
		parent = P;
		label = f;
	}

	/**
	* creates a JButton with title s
	*
	* @param s the title of the button to be created
	* @return a JButton object
	*/

	JButton createButton(String s){  
		  JButton j = new JButton(s);
		  j.setFont(new Font("Arial",Font.BOLD,14));
		  j.setBackground(new Color(140,140,255));
		  j.setForeground(Color.BLACK);

		  j.addActionListener(this);  //to activate button
		  return j;
	}

	/**
	* creates a JPanel for all the JButtons and adds the buttons to it
	*
	* @return a JPanel object
	*/

  JPanel createButtonPanel() {
	JPanel jp = new JPanel();

	jp.setLayout( new GridLayout(2,5,0,0));  //sets layout size and type

	JButton j;

	j = createButton("~"); //not
	jp.add(j);

	j = createButton("v"); //or
	jp.add(j);

	j = createButton("^");  //and
	jp.add(j);

	j = createButton("->"); //implication
	jp.add(j);

	j = createButton("COMPUTE");
	jp.add(j);

	j = createButton("p");
	jp.add(j);

	j = createButton("q");
	jp.add(j);

	j = createButton("r");
	jp.add(j);

	j = createButton("s");
	jp.add(j);

	j = createButton("CLEAR");
	jp.add(j);

	return jp;  
  }

    /**
	*  Adds a formula under all subTruthTreeGUI leaves
	*
	* @param form The formula to be added
	*/

	public  void add_one(Formula form){ 
		if (left == null) left = new TruthTreeGUI(form, this);
		else left.add_one(form);
		if (right !=null)  right.add_one(form);
	}

    /**
	*  Adds two new nodes on separate branches at each leaf.
	*
	* @param form1 The formula to be added as a left leaf
	* @param form2 The formula to be added as a right leaf
	*/

	public  void add_two(Formula form1, Formula form2){
		if (left == null){									// then both subTruthTreeGUIs null
			left = new TruthTreeGUI(form1, this);
			right = new TruthTreeGUI(form2, this);
		}
		else {
			left.add_two(form1, form2);
			if (right != null) right.add_two(form1, form2);
		}
	}

    /**
	* Expands TruthTreeGUI by adding expansion formulas depending on a (1 step) or b (2 steps)
	*
	*/

	public  void expand(){
		String t =label.type();
		Formula f = label.first_expansion();
		Formula s = label.second_expansion();
		if (t.equals("a_1")) add_one(f); //double negations
		if (t.equals("a")|t.equals("a_2")|t.equals("a_3")){
			add_one(f); 
			add_one(s);
		}
	   if (t.equals("b_1")|t.equals("b_2")|t.equals("b_3"))  add_two(f, s);
	}

    /**
	* Completes the TruthTreeGUI recursively
	*
	*/

	public void complete(int tabs){
		expand();					//expand root.
		for(int i=0; i<tabs; i++) System.out.print("\t");
		if(tabs>0) System.out.print("{" + lits() + "}\n");
		if (left != null) left.complete(tabs+1); 
		if (right != null) right.complete(tabs+1);
	}

    /**
	* Returns all the literals from the current node up to the root.
	*
	* @return a String object
	*/

	public  String lits(){
		String s = "";
		if (this==null) return "";
		else{
			if  (label.type().equals("literal")){
				if(s.compareTo("")==0) s = label.value+" "+s;
				else s = label.value + s;
			}
			if (parent == null) return s; 
			else return (s+parent.lits());
		}
	}

  	/**
	* Checks for complements (p and not p) in the TruthTreeGUI
	*
	* @return a boolean object
	*/

	public  boolean closed_branch(){
		if (this==null) return false;
		else{
			Formula f = label;
			if (parent == null) return false ;
			else if (f.type().equals("literal")& find(f.negation().value)) return true;
			else return (parent.closed_branch());
		}
	}
  
  	/**
	* Searches the TruthTreeGUI for a string
	*
	* @param x the String search key 
	* @return a boolean object
	*/

	public  boolean find(String x){
		if (this==null) return false;
		else if (label.value.equals(x)) return true;
		else if (parent == null) return false;
		else return (parent.find(x));
	}

  	/**
	* Checks recursively if either subTruthTreeGUI is closed
	*
	* @return a boolean object
	*/

	public  boolean closed_tab(){
		if (this==null) return false;
		else if (left == null) return (closed_branch());
		else if (left != null & right == null) return (left.closed_tab());
		else if (left != null & right != null) return (left.closed_tab() & right.closed_tab());
		else System.out.println("Error"); return true;
	}

	/**
	* Creates a Vector of literals along first open branch
	*
	* @return a Vector object
	*/

	public Vector oneval(){
		Vector answer = new Vector();
		if (closed_tab()) System.out.println("Unsatisfiable");
		else{
			if (label.type().equals("literal")) answer.addElement(label);
			if (left != null){
				if (!left.closed_tab()) answer.addAll(left.oneval());
				else answer.addAll(right.oneval()); //right must be open branch
			}
		}
		return answer;
	} 
  
  	/**
	* Finds the value of a formula
	*
	* @return a String object
	*/

	public  String literals(){
		String lits = "";
		if (closed_tab()) System.out.println("Unsatisfiable");
		if (label.type().equals("literal")) lits = lits + ", " + label.value;
		if (left ==null) return lits;
		else{
			if (!left.closed_tab()) lits = lits + left.literals();
			else lits = lits + right.literals();
			return lits;
		}
	}

	/**
	* Main function which creates interface for Satisfiability-solver
	*
	* @param args the command line arguments
	*/
	public static void main(String [] args){
		TruthTreeGUI tr = new TruthTreeGUI();		//creates interface
	}

	/**
	* Reads the input from the buttons and performs calculations based on them
	*
	* @param e the action being performed
	*/

	public void actionPerformed(ActionEvent e) {
		String act = e.getActionCommand(),str,calc;
		int temp=0;
		char ch;
		TruthTreeGUI T;
		Formula f;

	try{
		if(act.compareTo("->")==0) jt.setText(jt.getText() + ">" );  //used to keep operators a single character
		else if(act.compareTo("COMPUTE")==0) {  //if compute
			f = new Formula(jt.getText());  //creates formula
			System.out.println("\n\n*********************\n\n{" + f.first().value + "} {" + f.second().value + "}");      
			T = new TruthTreeGUI(f,null);  //creates a TruthTreeGUI
			T.complete(-4);  //expands TruthTreeGUI
			if (T.closed_tab())   //if a contradiction occurs - unsatisfiable
			{
				jt.setText("Unsatisfiable");
				System.out.print("_______________________________\nUnsatisfiable");
			}	
			else     //else - satisfiable
			{
				Vector v = T.oneval();
				jt.setText("Satisfiable as: " + T.literals().substring(1,T.literals().length()));
				System.out.print("_______________________________\n{" + T.literals().substring(1,T.literals().length()) + " }");
			}
		}
		else if(act.compareTo("CLEAR")==0) {  //else if clear, clears all variables and operators
			jt.setText("");
		}
		else	jt.setText(jt.getText() + act );  //else append variable or operator
	}catch(Exception e2){ //if error occurs, clear
		jt.setText(""); 
		}
  }
}









