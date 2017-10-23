package project;
/**
 * @author Edgar Morales
 *
 */
public class Token {
	private String token;
	private TokenType type;

/*****************************************************************************************************************************************
 * @param String
 * @param TokenType
 */
	public Token(String token, TokenType type){
		this.token = token;
		this.type = type;
	}
	
/*****************************************************************************************************************************************
 * Returns a Token
 * @return token
 */
	public String getToken(){
		return token;
	}
	
/*****************************************************************************************************************************************
 * Returns the TokenType: token, identifier, string, integer, etc
 * @return TokenType
 */
	public TokenType getType(){
		return type;
	}
}
