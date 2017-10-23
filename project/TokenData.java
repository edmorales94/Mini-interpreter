package project;
import java.util.regex.Pattern;
/**
 * @author Edgar Morales
 *
 */
public class TokenData {
	private Pattern pattern;
	private TokenType type;
	
/*******************************************************************************************************
 * This constructor will allow the instance to have access to the Pattern class 
 * which can work with regular expressions
 * @param Pattern
 * @param TokenType
 */
	public TokenData(Pattern pattern, TokenType type){
		this.pattern = pattern;
		this.type = type;
	}
	
/*******************************************************************************************************
 * Returns a regular expression pattern
 * @return Pattern
 */
	public Pattern getPattern(){
		return pattern;
	}
	
/*******************************************************************************************************
 * Returns the TokenType
 * @return TokenType
 */
	public TokenType getType(){
		return type;
	}
}
