package project;
/**
 * @author Edgar Morales
 *
 */
public enum TokenType {
	/** A token, for example: () " , */
	TOKEN,
	
	/**First character is a letter followed by letters/numbers */
	IDENTIFIER,
	
	/** A number*/
	INTEGER_LITERAL,
	
	/** Anything enclosed in double quotes */
	STRING_LITERAL
}