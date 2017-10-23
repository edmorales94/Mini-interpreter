package project;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author Edgar Morales
 *
 */
public class Tokenizer {
	private ArrayList<TokenData> tokenData;
	private String str;
	private Token lastToken;
	private boolean pushBack;
	
	public Tokenizer(String str){
		this.str = str;
		this.tokenData = new ArrayList<TokenData>();
		
		tokenData.add(new TokenData(Pattern.compile("[a-zA-Z][a-zA-Z0-9]*"),TokenType.IDENTIFIER));
		
	}
	
	
}
