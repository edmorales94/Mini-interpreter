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
		//System.out.println("characters: " + Arrays.toString(characters) + "\n");
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
		boolean possibleVariable = true;//This will let us know if we are storing a variable name or not
		String variableName = "";//each character belonging to a variable name will be store here
		String variableAssingedtoVariable = "";//name of the variable given as a value for another variable
		boolean AssigningVariableToVariable = false;
		Pattern numberPattern = Pattern.compile("[0-9]");//regular expression for numbers
		
		for (int i = 0; i < data.length; i++) {//loop through each character
			token += data[i];//update token by appending each character stored in data[]
			Matcher numberMatcher = numberPattern.matcher(token);
			
			if(token.equalsIgnoreCase(" ")) {//if we get a space
				if(state == 0) { token = "";}//if it's not a space from a string, get rid of spaces
				else { token = " ";}//if it belongs to a string value, we keep it
			}
			
			else if(token.equalsIgnoreCase("\n") || token.equalsIgnoreCase(";")) {//in case we find a new line or semicolon
				if(isAnExpression && numericalExpression.equalsIgnoreCase("") == false) {//if we have a mathematical expression and numericalExpression is not empty
					tokens.add("Expres: " + numericalExpression);//add the exact mathematical expression
					tokens.add("Semico: " + token);//since token is a ; we added it after the expression
					numericalExpression = "";//we clear the expression for a possible next new expression
				}
				else if(numericalExpression.equals("") == false && !(isAnExpression)) {//if we didn't detect an expression, then it's just a single number
					tokens.add("Number: " + numericalExpression);
					tokens.add("Semico: " + token);//since token is a ; we added it after the number
					numericalExpression = "";//we clear the expression for a possible next new expression
				}
				else if(variableName.equals("") == false) {//if we don't have an empty variableName
					tokens.add("Variab: " + variableName);//add variableName to the tokens list
					tokens.add("Semico: " + token);//since token is a ; we added it after the variableName(not initialized variable)
					variableName = "";//restart the variableName
				}
				else if(variableAssingedtoVariable.equals("") == false) {//if variable given as value is not empty
					tokens.add("Variab: " + variableAssingedtoVariable);//add variable given as value to the token list
					tokens.add("Semico: " + token);//add semicolon
					variableAssingedtoVariable = "";//restar the variable value holder
				}
				
				token = "";	//restart the token so we can get a different sequence of characters
				possibleVariable = true;//we restart everything again
				AssigningVariableToVariable = false;//we could get another variable as a value again
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
			
			
			else if(possibleVariable) {//if we reeived the signal of an upcoming variable
				Pattern pattern = Pattern.compile("^([a-zA-Z]|[0-9]|_)");//variable name needs to contain letters and numbers(optional) or a _(optional)
				Matcher matcher = pattern.matcher(token);//Try to find matches of any of the variableName conditons: letter,number, underscore
				if(matcher.find()) {//if the token matches any of the requirements
					variableName += token;//add that token to the variableName holder
					token = "";//restart token for new upcoming characters
				}
				else {
					System.out.println("Improper character for a variable name: " + token);
					System.out.println("This is an invalid variable name syntax: " + variableName + token);
					possibleVariable = false;
				}
			}
			
						
			else if(numberMatcher.find()) {//if the token is a number
				if(!AssigningVariableToVariable) {//if the number doesn't belong to a variable name
					numericalExpression += token;//add that number to the expression holder
					token = "";//restar the token
				}
				else {
					variableAssingedtoVariable += token;//if belongs to a variable, keep it wih the variable name
					token = "";//restart the token
				}
			}
			
			else if(token.equals("+")||token.equals("-")|token.equals("*")||token.equals("(")||token.equals(")")) { //This'll signal we have a mathematical expres.
				isAnExpression = true;//update the flag
				if(AssigningVariableToVariable) {//if we got a variable in the expression
					if(variableAssingedtoVariable.length() != 0) {
						numericalExpression += variableAssingedtoVariable;//keep the variable in the expression
					}
				}
				numericalExpression += token;//add number to the numerical expression
				token = "";//restart token
				variableAssingedtoVariable = "";
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
			else {
				AssigningVariableToVariable = true;//since its not a number, then we got a variable as a value
				variableAssingedtoVariable += token;
				token = "";	
			}
		}
				
		//System.out.println("tokens: " + tokens + "\n");
		return tokens;
	}

/*******************************************************************************************************************************************************************
 * This method will evalute the expression, do the mathematical operations, and return the result
 * @param String expression
 * @return String resultObtainedFromExpression
 */
	public static String evaluateExpression(String expression){
		//System.out.println("Initial Expression: " + expression);
		String finalExpression = "";//holder used to place the corrected sintax of the expression
		
		//for loop to find sintax errors
		for(int i = 0; i < expression.length()-1; i ++) {//loop through the first element to the second to last so we dont get out of range
			String currentElement = String.valueOf(expression.charAt(i));//get current element in the expression
			String nextElement = String.valueOf(expression.charAt(i+1));//get the next element 
			if(currentElement.equals("-") && nextElement.equals("-")) {//if we get two continuous --, then we have a multiplication of two -1's
				finalExpression += currentElement +"1*";//updating with the correct syntax
			}
			else if(currentElement.equals("+") && nextElement.equals("+")) {//if we get two continuous ++, then we have a multiplication of two +1's
				finalExpression += currentElement +"1*";//updating with the correct syntax
			}
			else if(currentElement.equals("*") && nextElement.equals("*")) {//if we get two continuous ++, then we have a multiplication of two +1's
				finalExpression += currentElement +"1";//updating with the correct syntax
			}
			else {//sintax is correct 
				finalExpression += String.valueOf(currentElement);//update the finalExpression holder
			}
		}
		finalExpression += expression.substring(expression.length()-1);//add the last element we left out in order to avoid an out of range situation
		
		char[] expressionCharacters = finalExpression.toCharArray();//get each character in the expression
		String variableName = "";
		boolean weGotVariable = false;
		finalExpression = "";
		
		Pattern pattern = Pattern.compile("[0-9]");
		//for loop to find variables in the expression
		for(int i = 0; i < expressionCharacters.length; i++) {
			String character = String.valueOf(expressionCharacters[i]);
			Matcher match = pattern.matcher(character);
			if(match.find() && !weGotVariable) {//if we got a number not belonging to a variable name
				finalExpression += character;//keep the number
			}
			else if(character.equals("+") || character.equals("-") || character.equals("*") ||
					character.equals("(") || character.equals(")")){
				if(variableName.length() != 0) {
					if(isVariableInTheTable(variableName)) {//if the variable is in symbols table
						finalExpression += getVariableValueOf(variableName);//retrieve its value
					}
					else {
						System.out.println("Variable was not found: " + variableName);
						break;
					}
				}
				variableName = "";//we dont need to hold the variable anymore
				weGotVariable = false;
				finalExpression += character;//add the mathematical symbols
			}
			
			else {
				// if not a number or symbol, then we got a variable
				variableName += character;
				weGotVariable = true;
			}
		}
		if(isVariableInTheTable(variableName)) {//in case a variable was the last element
			finalExpression += getVariableValueOf(variableName);
		}
		
		//built-in fuctions needed to evaluate the expression
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Object result = null;
		try {
			result = engine.eval(finalExpression);
		} 
		catch (ScriptException e) {
			System.out.println("Expression Sintax error");
		}
		return result.toString();
	}

/*******************************************************************************************************************************************************************
 * This method will place the variable with its value in the symbolTable Hashtable
 * @param variableName
 * @param variableValue
 */
	public static void assignVariable(String variableName, String variableValue) {
		symbolTable.put(variableName.substring(8), variableValue);
		//we save varibles in a hashtable in case a variable is set equal to another variable
	}
	

/*******************************************************************************************************************************************************************
 * This method tries to see if we a given variable is stored in the symbols table
 * @param variableName
 * @return
 */
	public static boolean isVariableInTheTable(String variableName) {
		for(String key: symbolTable.keySet()) {
			if(variableName.equals(key)) {
				return true;
			}
		}
		return false;
	}
/*******************************************************************************************************************************************************************
 * This method will return the value of a variable stored in the symbolTable if it was saved there.
 * @param varibleName
 * @return valueOfVariableRequested
 */
	public static String getVariableValueOf(String variableName) {
		String value = "";//value will be stored here
		for(String key: symbolTable.keySet()) {//get the keys(variableNames) stored in the hashtable
			if(variableName.equals(key)) {//if the variableName requested is in the hashatable
				value = symbolTable.get(key);//save its value in the value holder
				break;
			}
			else {
				value = "Variable Error";//variable requested was not found 
			}
		}
		return value; 
	}
	
/*******************************************************************************************************************************************************************
 * This method will go through the list of tokens and save the variables with their values depending on their value type
 * @param listOfTokens
 */
	public static void parse(ArrayList<String> listOfTokens) {
		int i = 0;
		Pattern pattern = Pattern.compile("^([a-zA-Z_])[a-zA-Z_0-9]*$");//variable name needs to contain letters and numbers(optional) or a _(optional)
		while(i < listOfTokens.size()-1) {//loop through the list of tokens
			//get specific keywords in the list of tokens so we can find out how to assign variables to the hashtable 
			String keyWords = listOfTokens.get(i).substring(0, 6) + " " + listOfTokens.get(i+1) + " " + listOfTokens.get(i+2).substring(0, 6) + " " + listOfTokens.get(i+3).substring(0,6);
			
			//There are the specific keywords that can be used to help determine how to proceed
			if(keyWords.equalsIgnoreCase("variab equals number semico") || keyWords.equalsIgnoreCase("variab equals string semico") || 
					keyWords.equalsIgnoreCase("variab equals expres semico") || keyWords.equalsIgnoreCase("variab equals variab semico")) {
				Matcher matcher = pattern.matcher(listOfTokens.get(i).substring(8));
				
				if(matcher.matches()) {
				
					if(keyWords.substring(14).equalsIgnoreCase("string")) {
						assignVariable(listOfTokens.get(i), listOfTokens.get(i+2).substring(8));
					}
					
					else if(keyWords.substring(14,20).equalsIgnoreCase("number")) {
						String number  = listOfTokens.get(i+2).substring(8);
						if(number.length() >= 2) {
							if(number.startsWith("0")) {
								System.out.println("Error. This is not an acceptable number syntax: " + number);
								System.out.println("Program won't parse anymore");
								break;//stop parsing
							}
						}
						System.out.println(listOfTokens.get(i).substring(8) + " = " + number);
						assignVariable(listOfTokens.get(i), listOfTokens.get(i+2).substring(8));
					}
					
					if(keyWords.substring(14,20).equalsIgnoreCase("expres")) {
						String solvedExpression = evaluateExpression(listOfTokens.get(i+2).substring(8));
						System.out.println(listOfTokens.get(i).substring(8) + " = " + solvedExpression);
						assignVariable(listOfTokens.get(i),solvedExpression);
					}
					
					if(keyWords.substring(14,20).equalsIgnoreCase("variab")) {
						String valueOfVariable = getVariableValueOf((listOfTokens.get(i+2).substring(8)));
						System.out.println(listOfTokens.get(i).substring(8) + " = " + valueOfVariable);
						assignVariable(listOfTokens.get(i), valueOfVariable);
					}
					i += 3;
				}
				
				else {
					System.out.println("Incorrect variable name syntax: " + listOfTokens.get(i).substring(8));
					System.out.println("Program won't parse anymore");
					break;
				}
			}
			
			else if(keyWords.substring(0,13).equalsIgnoreCase("variab semico")) {
				System.out.println("Variable: " + listOfTokens.get(i).substring(8) + " was not initialized");
				System.out.println("Program won't parse anymore");
				break;			
			}
			
			i++;
		}
		//System.out.println("\nsymbolTable: " + symbolTable);
	}
	
//---main-----------------------------------------------------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) throws ScriptException {
		String data = "variable2 = 0;"
				+     "var = 345;"
				+     "x = 12;"
				+     "y = (10+2) * 4;"
				+     "variable_2 = var;"
				+     "variable = x;"
				+     "Hello4 = variable_2;"
				+     "lastVariable = variable2;"
				+     "x = 1;"
				+     "y = 2;"
				+     "z = ---(x + y)*(x-+y);";
		parse(tokenizer(data));
	}

}
