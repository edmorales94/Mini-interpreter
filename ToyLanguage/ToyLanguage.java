package ToyLanguage;

import java.util.ArrayList;
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
	public static ArrayList<String> tokens = new ArrayList<String>();
	public static Hashtable<String, String> symbolTable = new Hashtable<String, String>();
	
	public static ArrayList<String> list(String data) {
		ArrayList<String> characters = new ArrayList<String>();
		for(int i = 0; i < data.length(); i++) {
			char character = data.charAt(i);
			characters.add(String.valueOf(character));
		}
		return characters;
	}
	
	public static ArrayList<String> tokenizer(String assignment){
		String token = "";
		int state = 0; //found a keyword, variable, etc
		String string = "";
		ArrayList<String> data = list(assignment);
		String numericalExpression = "";
		boolean isAnExpression = false;
		boolean possibleVariable = false;
		String variableName = "";
		
		for (int i = 0; i < data.size(); i++) {
			token += data.get(i);
			//System.out.print(token); // + " " 
			if(token.equalsIgnoreCase(" ")) {
				if(state == 0) { token = "";}
				else { token = " ";}
			}
			
			else if(token.equalsIgnoreCase("\n") || token.equalsIgnoreCase(";")) {
				if(isAnExpression && numericalExpression.equalsIgnoreCase("") == false) {
					//System.out.print("Numerical Expression: " + numericalExpression);
					tokens.add("Express: " + numericalExpression);
					tokens.add("Semico: " + token);
					numericalExpression = "";
				}
				else if(numericalExpression.equals("") == false && !(isAnExpression)) {
					//System.out.print("Number: " + numericalExpression);
					tokens.add("Number: " + numericalExpression);
					tokens.add("Semico: " + token);
					numericalExpression = "";
				}
				else if(variableName.equalsIgnoreCase("") == false) {
					//System.out.print("Variab: " + variableName);
					tokens.add("Variab: " + variableName);
					variableName = "";
					possibleVariable = false;
				}
				token = "";	
			}
			
			else if(token.equalsIgnoreCase("=") && state == 0) {
				if(variableName.equalsIgnoreCase("") == false){
					tokens.add("Variab: " + variableName);
					variableName = "";
				}
				tokens.add("EQUALS");
				token = "";
				possibleVariable = false;
			}
			
			else if(token.equalsIgnoreCase("$") && state == 0) {
				possibleVariable = true;
				variableName += token;
				token = "";
			}
			
			else if(possibleVariable) {
				Pattern pattern = Pattern.compile("^([a-zA-Z]|[0-9])");
				Matcher matcher = pattern.matcher(token);
				if(matcher.find()) {
					variableName += token;
					token = "";
				}
				else {
					//token = token;
					if(token.equalsIgnoreCase(" ")) {
						token = "";
					}
					possibleVariable = false;
				}
			}
			
			else if(token.equalsIgnoreCase("0")||token.equalsIgnoreCase("1")||token.equalsIgnoreCase("2")||
					token.equalsIgnoreCase("3")||token.equalsIgnoreCase("4")||token.equalsIgnoreCase("5")||
					token.equalsIgnoreCase("6")||token.equalsIgnoreCase("7")||token.equalsIgnoreCase("8")||
					token.equalsIgnoreCase("9")) {
				numericalExpression += token;
				token = "";
			}
			
			else if(token.equalsIgnoreCase("+")||token.equalsIgnoreCase("-")|token.equalsIgnoreCase("*")||
					token.equalsIgnoreCase("(")||token.equalsIgnoreCase(")")) {
				isAnExpression = true;
				numericalExpression += token;
				token = "";
			}
			
			else if(token.equalsIgnoreCase("\"")) {
				if(state == 0) {//found a keyword, variable, etc
					state = 1;
				}
				else if(state == 1) {
					tokens.add("String: " + string + "\"");
					state = 0;
					string = "";
					token = "";
				}
			}
			
			else if(state == 1) {
				string += token;
				token = "";
			}
		}
		
		System.out.print("[");
		for(int i = 0; i < tokens.size(); i++) {
			if(i == tokens.size()-1) {
				System.out.print(tokens.get(i));
			}
			else {
				System.out.print(tokens.get(i) + ", ");
			}
		}
		System.out.println("]");
		return tokens;
	}

	public static String evaluateExpression(String expression){
		String finalExpression = "";
		for(int i = 0; i < expression.length()-1; i ++) {
			String currentElement = String.valueOf(expression.charAt(i));
			String nextElement = String.valueOf(expression.charAt(i+1));
			if(currentElement.equalsIgnoreCase("-") && nextElement.equalsIgnoreCase("-")) {
				finalExpression += currentElement +"1*";
			}
			else if(currentElement.equalsIgnoreCase("+") && nextElement.equalsIgnoreCase("+")) {
				finalExpression += currentElement +"1*";
			}
			else {
				finalExpression += String.valueOf(currentElement);
			}
		}
		finalExpression += expression.substring(expression.length()-1);
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
		String data = "$myVariable = 345"
				+ "$myVarible2 = (10+2)*4;";
		tokenizer(data);
		System.out.println(evaluateExpression("---(1+2)*(1-+2)"));
	}

}
