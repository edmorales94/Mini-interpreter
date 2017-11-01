package project;

import java.util.ArrayList;

/**
 * @author Edgar Morales
 * Represents a block of code
 */
public abstract class Block {
	/**
	 * There are blocks of code, a method is a block that might contain inner blocks of code,
	 * so the method would be the superblock and the rest will be subBlocks
	 * for example a method contains a for loop so we have 
	 *  method -> for loop
	 */
	private Block superBlock;//if we have a class, there is no super block before a class
	private ArrayList<Block> subBlocks;//blocks inside a block
	
/*************************************************************************************************
 * Each super block will contain an arrayList that will store its sub blocks
 * @param superBlock
 */
	public Block(Block superBlock){
		this.superBlock = superBlock;
		this.subBlocks = new ArrayList<Block>();
	}
	
/*************************************************************************************************
 * @return superBlock
 */
	public Block getSuperBlock(){
		return superBlock;
	}
	
/*************************************************************************************************
 * Adds a subBlock for a superblock 
 * @param block
 */
	public void addBlock(Block block){
		subBlocks.add(block);
	}
	
/*************************************************************************************************
 * This will need to be defined in other classes to see if you can run the block(main method)
 */
	public abstract void run();
}
