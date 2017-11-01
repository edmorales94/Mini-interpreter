package project;
/**
 * @author Edgar Morales
 *
 */
public abstract class Parser <T extends Block>{
	
/*************************************************************************************************
 * Takes a line and checks to see if it is for this parser by using regex
 * @param line
 * @return boolean
 */
	public abstract boolean shouldParse(String line);
	
/**
 * Take the super block and the tokenizer for the line, and return a block of this parser's type
 * @param superBlock
 * @param tokenizer
 */
	public abstract T parse(Block superBlock, Tokenizer tokenizer);
	
}
