package Interpreter;

public class Token {		//It will be used for making tokens by tokenizer
	public String val;
	public TokenType type;
	
	public Token(String val,TokenType type){
		this.val=val;
		this.type=type;
	}
	
	public String toString(){
		return "\nVal: "+val+" type= "+type;
	}
}
