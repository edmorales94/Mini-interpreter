package ToyLanguage;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
/**
 * @author Edgar Morales
 *
 */
public class ToyLanguage {
	public static ArrayList<String> tokens = new ArrayList<String>();//Every character will get store here
	public static Hashtable<String, String> symbolTable = new Hashtable<String, String>();//Variables and their values will be stored here
	
/*******************************************************************************************************************************************************************
 * This method will read every single character from the input and store them in an ArrayList
 * @param String data
 * @return ArrayList characters
 */
	public static String[] list(String data) {
		String[] characters = new String[data.length()];//Array needed for storing each character
		for(int i = 0; i < data.length(); i++) { //Loop through the whole input
			characters[i] = String.valueOf(data.charAt(i));//get the character at the specific location and cast it to string. Then add it to the array
		}
		//System.out.println(Arrays.toString(characters));
		return characters;
	}
	
/*******************************************************************************************************************************************************************
 * This method converts a sequence of characters into a sequence of tokens (strings with an assigned and thus identified meaning).
 * @param String assignment
 * @return ArrayList of tokens
 */
	public static ArrayList<String> tokenizer(String assignment){
		int state = 0; //found a keyword, variable, etc
		String string = "";
		String[] data = list(assignment);//get each character from assignament and store them in data[]
		String token = "";//This will hold each character from the assignment
		String numericalExpression = "";//Used to store mathetical expressions such as (347+[-12*3]-10)
		boolean isAnExpression = false;//This will let us know if we have an expression or just a single number
		boolean possibleVariable = false;//This will let us know if we are storing a variable name or not
		String variableName = "";//each character belonging to a variable name will be store here
		
		for (int i = 0; i < data.length; i++) {//loop through each character
			token += data[i];//update token by appending each character stored in data[]
			//System.out.print(token);
			if(token.equalsIgnoreCase(" ")) {//if we get a space
				if(state == 0) { token = "";}//if it's not a space from a string, get rid of spaces
				else { token = " ";}//if it belongs to a string value, we keep it
			}
			
			else if(token.equalsIgnoreCase("\n") || token.equalsIgnoreCase(";")) {//in case we find a new line or semicolon
				if(isAnExpression && numericalExpression.equalsIgnoreCase("") == false) {//if we have a mathematical expression and numericalExpression is not empty
					//System.out.print("Numerical Expression: " + numericalExpression);
					tokens.add("Express: " + numericalExpression);//add the exact mathematical expression
					tokens.add("Semico: " + token);//since token is a ; we added it after the expression
					numericalExpression = "";//we clear the expression for a possible next new expression
				}
				else if(numericalExpression.equals("") == false && !(isAnExpression)) {//if we didn't detect an expression, then it's just a single number
					//System.out.print("Number: " + numericalExpression);
					tokens.add("Number: " + numericalExpression);
					tokens.add("Semico: " + token);//since token is a ; we added it after the number
					numericalExpression = "";//we clear the expression for a possible next new expression
				}
				else if(variableName.equalsIgnoreCase("") == false) {//if we don't have an empty variableName
					//System.out.print("Variab: " + variableName);
					tokens.add("Variab: " + variableName);//add variableName to the tokens list
					tokens.add("Semico: " + token);//since token is a ; we added it after the variableName(not initialized variable)
					variableName = "";//restart the variableName
					possibleVariable = false;//restart the condition
				}
				token = "";	//restart the token so we can get a different sequence of characters
			}
			
			else if(token.equalsIgnoreCase("=") && state == 0) {//if the token is =, and we haven't found a variable value yet
				if(variableName.equalsIgnoreCase("") == false){// if the variable name is not empty
					tokens.add("Variab: " + variableName);//add the variable name since token is a =, this variable is getting initialized
					variableName = "";//restart the variable 
				}
				tokens.add("EQUALS");//add the = token to the list
				token = "";//restart token for a new character sequence
				possibleVariable = false;//restart the condition for a new upcoming variable
			}
			
			else if(token.equalsIgnoreCase("$") && state == 0) {//if token is a $, and no string value for a variable is found
				possibleVariable = true;//this is to signal we have found an  upcoming variable
				variableName += token;//append the $ that belong from the incoming variable to the variableName holder 
				token = "";//update the token
			}
			
			else if(possibleVariable) {//if we reeived the signal of an upcoming variable
				Pattern pattern = Pattern.compile("^([a-zA-Z]|[0-9]|_)");//variable name needs to contain letters and numbers(optional) or a _(optional)
				Matcher matcher = pattern.matcher(token);//Try to find matches of any of the variableName conditons: letter,number, underscore
				if(matcher.find()) {//if the token matches any of the requirements
					variableName += token;//add that token to the variableName holder
					token = "";//restart token for new upcoming characters
				}
				/*else {
					token = token;
					if(token.equalsIgnoreCase(" ")) {
						token = "";
					}
					possibleVariable = false;
				}*/
			}
			
			//if the token is a number
			else if(token.equals("0")||token.equals("1")||token.equals("2")||token.equals("3")||token.equals("4")||token.equals("5")||
					token.equals("6")||token.equals("7")||token.equals("8")||token.equals("9")) {
				numericalExpression += token;//add that number to the expression holder
				token = "";//restar the token
			}
			
			else if(token.equals("+")||token.equals("-")|token.equals("*")||token.equals("(")||token.equals(")")) { //This'll signal we have a mathematical expres.
				isAnExpression = true;//update the flag
				numericalExpression += token;//add token to the numerical expression
				token = "";//restart token
			}
			
			else if(token.equalsIgnoreCase("\"")) {
				if(state == 0) {//if we're still on a keyword, variable, etc but found a " mark
					state = 1;//update state, we got an upcoming string
				}
				else if(state == 1) {//if we are already in the string found state and found another ", then string is finished
					tokens.add("String: " + string + "\"");//add the string plus the last " mark
					state = 0;//restart the state
					string = "";//restart the string holder
					token = "";//restart the token
				}
			}
			
			else if(state == 1) {//if we get the signal for an upcoming string
				string += token;//add its characters to the string holder
				token = "";//update the token
			}
		}
				
		System.out.println(tokens);
		return tokens;
	}

/*******************************************************************************************************************************************************************
 * This method will evalute the expression, do the mathematical operations, and return the result
 * @param String expression
 * @return String resultObtainedFromExpression
 */
	public static String evaluateExpression(String expression){
		String finalExpression = "";//holder used to place the corrected sintax of the expression
		for(int i = 0; i < expression.length()-1; i ++) {//loop through the first element to the second to last so we dont get out of range
			String currentElement = String.valueOf(expression.charAt(i));//get current element in the expression
			String nextElement = String.valueOf(expression.charAt(i+1));//get the next element 
			if(currentElement.equals("-") && nextElement.equals("-")) {//if we get two continuous --, then we have a multiplication of two -1's
				finalExpression += currentElement +"1*";//updating with the correct syntax
			}
			else if(currentElement.equals("+") && nextElement.equals("+")) {//if we get two continuous ++, then we have a multiplication of two +1's
				finalExpression += currentElement +"1*";//updating with the correct syntax
			}
			else {//sintax is correct 
				finalExpression += String.valueOf(currentElement);//update the finalExpression holder
			}
		}
		finalExpression += expression.substring(expression.length()-1);//add the last element we left out in order to avoid an out of range situation
		//System.out.println("This is final Exp: " + finalExpression);
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Object result = null;
		try {
			result = engine.eval(finalExpression);
		} catch (ScriptException e) {
			System.out.println("Expression Sintax error");;
		}
		return result.toString();
	}

	public void assignVariable(String variableName, String variableValue) {
		System.out.println("Variable name: " + variableName + ", Variable value: " + variableValue);
		symbolTable.put(variableName, variableValue);
	}
	
	public String getVariableValue(String varibleName) {
		String value = "";
		for(String key: symbolTable.keySet()) {
			if(varibleName.equals(key)) {
				value = symbolTable.get(varibleName);
				break;
			}
			else {
				value = "Variable Error";
			}
		}
		return value;
	}
	
	
	public static void main(String[] args) throws ScriptException {
		String data = "$myVariable = 345;"
				+ "$myVarible2 = (10+2)*4;"
				+ "$myVariable3 = 5 + 10(-3+7);";
		tokenizer(data);
		System.out.println(evaluateExpression("---(1+2)*(1-+2)"));
	}

}
