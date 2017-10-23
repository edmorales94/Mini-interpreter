package project;
/**
 * @author Edgar Morales
 *
 */
public class TokenizerTest {
	public static void main(String[] args){
		String code = "class HelloWorld\n"+
					  "method main requires()\n"+
					  "print \"hello\"";
		Tokenizer tokenizer = new Tokenizer(code);
		while(tokenizer.hasNextToken()){
			System.out.println(tokenizer.nextToken().getToken());
		}
		
		String code2 = "1994";
		Tokenizer tokenizer2 = new Tokenizer(code2);
		while(tokenizer2.hasNextToken()){
			System.out.println(tokenizer2.nextToken().getToken());
		}
	
	}
}
