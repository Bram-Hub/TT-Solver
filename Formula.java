import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Formual manipulation and container class
 *
 */

public class Formula{ 
  String value;  //formula
  
	/*
	* Constructor
	* 
	* @param f String name of formula (prefix notation)
	* @return a Formula object
	*/
 
	public Formula(String v){
	   value = v;
	}

	/**
	* Parses the string 
	*
	* @param s String to be parsed
	* @return an int object
	*/

	public int parse(String s){
		int sum = 0;
		char ch;

		for (int i =0; i < s.length(); i++){
			ch = s.charAt(i);
			if (ch =='^'|ch == 'v'|ch == '>') sum++;
			else if (ch !='~') sum--; 
		}
		return sum;
	}
  
	/**
	* Returns first variable of a formula
	*
	* @return a Formula object
	*/

	public  Formula first(){
		String s = this.value;
		char ch = s.charAt(0);
		int i = 1;
		
		if (ch == 'v'|ch =='^'|ch == '>'){
			while (parse(s.substring(1,i))>-1) i++;
			return (new Formula(s.substring(1,i)));
		}
		else return (new Formula("incorrect formula"));
	}
  
	/**
	* Returns second variable of a formula
	*
	* @return a Formula object
	*/

	public Formula second(){
		String s = this.value;
		char ch = s.charAt(0);
		int i = 1, l = s.length();

		if (ch =='v'|ch =='^'|ch == '>'){
			while (parse(s.substring(1,i))>-1) i++;
			return (new Formula(s.substring(i,l)));
		}
		else return (new Formula("incorrect formula"));
	}
 
	/**
	* Returns a string minus the first character (to fix negations)
	*
	* @return a Formula object
	*/

	public Formula tail(){
		if (value.equals("")) return (new Formula("error"));
		else return (new Formula(value.substring(1,this.value.length())));
	}
  
	/**
	* Adds a negation to a formula
	*
	* @return a Formula object
	*/

	public Formula neg(){
		return (new Formula("~"+value));
	}
  
	/**
	* Returns negation of a literal
	*
	* @return a Formula object
	*/

	public  Formula negation(){
		String lit = this.value;

		if (value.charAt(0) == '~') return (tail());
		else return (neg());
	}

	/**
	* Returns first formula to add
	*
	* @return a Formula object
	*/

	public Formula first_expansion(){
		String s = this.value,t = this.type();

		if (t.equals("literal")) return (new Formula("none"));
		else if (t.equals("a_1")) return (this.tail().tail()); //deletes first two chars if double negation
		else if (t.equals("a")|t.equals("b_1")) return (this.first());
		else if (t.equals("b_3")) return (this.first().neg()); //implication
		else if (t.equals("a_3")) return (this.tail().first()); //negated implication
		else return (this.tail().first().neg()); // negated or/and
	}
  
	/**
	* Returns second formula to add
	*
	* @return a Formula object
	*/

	public Formula second_expansion(){
		String s = this.value, t = this.type();

		if (t.equals("literal")|t.equals("a_1")) return (new Formula("none"));
		else if (t.equals("a")|t.equals("b_1")|t.equals("b_3")) return (this.second());
		else if (t.equals("a_3")) return (this.tail().second().neg()); // negated implication
		else return (this.tail().second().neg()); // negated or/and
	}

	/**
	* Returns the type of a string 
	*
	* @return a String object
	*/

	public  String type(){
		String s = value;
		char first = s.charAt(0), second = 'x';
	
		if (s.length() > 1) second = s.charAt(1);
		if (first == '~'){
			if (second == '~') return ("a_1");  //double negation
			else if (second == 'v') return ("a_2"); //negated or
			else if (second == '^') return ("b_2");  //negated and
			else if (second == '>') return ("a_3");  // negated implication
			else return ("literal");
		}
		else if (first == 'v') return ("b_1"); // or
		else if (first == '^') return ("a"); //and
		else if (first == '>') return ("b_3");  // implication
		else return "literal";
	}
}  











