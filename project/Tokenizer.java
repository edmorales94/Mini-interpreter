package project;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Edgar Morales
 *
 */
public class Tokenizer {
	private ArrayList<TokenData> tokenData;//storing the datatypes
	private String str;//variable used to store the input
	private Token lastToken;//current token we are working with 
	private boolean pushBack;//helper for pushBack method
	
/*****************************************************************************************************************************************************************************/
	public Tokenizer(String str){
		this.str = str;
		this.tokenData = new ArrayList<TokenData>();//initializing the arraylist
		
		//storing a regular expresion for indentifiers
		tokenData.add(new TokenData(Pattern.compile("^([a-zA-Z][a-zA-Z0-9]*)"),TokenType.IDENTIFIER));
		
		//storing a regular expresion for integers
		tokenData.add(new TokenData(Pattern.compile("^((-)?[0-9]+)"),TokenType.INTEGER_LITERAL));
		
		//storing a regular expresion for strings
		tokenData.add(new TokenData(Pattern.compile("^(\".*\")"), TokenType.STRING_LITERAL));
		
		//storing some simple tokens
		for(String t : new String[]{"=", "\\(", "\\)","\\.", "\\,"}){
			tokenData.add(new TokenData(Pattern.compile("^(" + t + ")"), TokenType.TOKEN));
		}
	}
	
/******************************************************************************************************************************************************************************
 * Returns the next token(sequence of characters)/word that follows
 * @return Token
 */
	public Token nextToken(){
		str = str.trim();//leading or trailing white space is removed
		if(pushBack){//
			pushBack = false;
			return lastToken;
		}
		//----------------------------------------------------------------------------------------
		//if the string is empty, we just return an empty token
		if(str.isEmpty()){
			return (lastToken = new Token("",TokenType.EMPTY));
		}
		
		//----------------------------------------------------------------------------------------
		//we iterate through each regular expression in the arrayList
		for(TokenData data: tokenData){
			Matcher matcher = data.getPattern().matcher(str);//each item in the array list is a regular expression, we create a pattern from it to see a match for the input
			if(matcher.find()){//if str can be created from a stored regular expression, then
				String token = matcher.group().trim();//groups the words and removes spaces at beginning and ending
				str = matcher.replaceFirst("");//removes the current token from input and updates it
				
				//we now try to see if the found match for input is a string
				if(data.getType()== TokenType.STRING_LITERAL){
					//we return the token from its beginning to its ending, and we declare its type
					return(lastToken = new Token(token.substring(1,token.length()-1), TokenType.STRING_LITERAL));
				}
				else{
					//we just return the token and get its type
					return(lastToken = new Token(token, data.getType()));
				}
			}
		}
		throw new IllegalStateException("Could not parse " + str);
	}
	
/******************************************************************************************************************************************************************************
 * Returns the next token from the given input
 * @return true or false
 */
	public boolean hasNextToken(){
		return !str.isEmpty();// if the string is not empty, then it has a next token
	}
	
/******************************************************************************************************************************************************************************
 * This method allows us to stop the tokenizer at the current token, and then we can resume parsing
 */
	public void pushBack(){
		if(lastToken != null){
			this.pushBack = true;
		}
	}
	
}
