package project;
import java.util.regex.Pattern;
/**
 * @author Edgar Morales
 *
 */
public class TokenData {
	private Pattern pattern;
	private TokenType type;
	
	public TokenData(Pattern pattern, TokenType type){
		this.pattern = pattern;
		this.type = type;
	}
	
	public Pattern getPattern(){
		return pattern;
	}
	
	public TokenType getType(){
		return type;
	}
}
