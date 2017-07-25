package Interpreter;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {		//To tokenize given source code
	String exp;					//will hold all the source code
	
	char look;					//current character in buffer
	long current=0;				//current character position in source code string
	
	boolean let=false;			//will be used for variables containing * in name
	
	public Tokenizer(){}	//Default Constructor
	
	public Tokenizer(String exp){	
		this.exp=exp+" ";	//Add space at the end to make things easy for recognizing end
	}
	
	public void setexpression(String exp){
		this.exp=exp+" ";
		
	}
	
	public TokenType FindOpType(char ch, char next){	//to find the type of character ch
		TokenType type=TokenType.NULL;
		switch(ch){
		case '+':
			type=TokenType.ADD;
			break;
		case '-':
			type=TokenType.SUBTRACT;
			break;
		case '*':
			type=TokenType.MULTIPLY;
			break;
		case '/':
			type=TokenType.DIVIDE;
			break;
		case '<':
			type = TokenType.LESS;
			if (next == '=') type = TokenType.LESSEQUAL;		//if it is <=
			break;
		case '>':
			type=TokenType.GREATER;
			if (next == '='){ 
				//type = TokenType.GREATER;
				type = TokenType.GREATEREQUAL;		//if it is >=
			}
			else if(next== '<'){
				//type = TokenType.NOT;
				type = TokenType.NOTEQUAL;			//if it is !=
			}
			break;
		case '=':
			type = TokenType.ASSIGNMENT;
			if (next == '=') 
				{type = TokenType.EQUAL;					//if it is ==
				
				}
			break;
		case '|':
			type = TokenType.OR;
			break;
		case '&':
			type = TokenType.AND;
			break;
		case '(':
			type = TokenType.LPARENT;
			break;
		case ')':
			type = TokenType.RPARENT;
			break;
		}
		return type;
	}

	public boolean IsParen(char ch)			//Will detect if ch is a parenthesis
	{
	boolean Op = ch == '(' || ch == ')';
	return Op;
	}
	
	public TokenType FindParenType(char ch){		//return the type of parenthesis
		TokenType type = TokenType.NULL;
		switch(ch)
		{
		case '(':
		type = TokenType.LPARENT;			//left parenthesis
		break;
		case ')':
		type = TokenType.RPARENT;			//right parenthesis
		break;
		}
		return type;
	}
	
	public boolean IsOp(char ch){			//Check if ch is an operator
		return ((ch=='-')||(ch=='+')||(ch=='*')||(ch=='/')||(ch=='<')
				||(ch=='>')||(ch=='=')||(ch=='!')||(ch=='|')||(ch=='&'));
	}

	public TokenType FindStatementType(String str)		//Find the statement represented by String str
	{
		TokenType type = TokenType.NULL;			//if it is null
		switch(str)
		{
		case ";":
			type=TokenType.TERMIN;			//if it is ;
			break;
		case "Let":
			type=TokenType.LET;							//if it is LET keyword
			break;
		case "end":
			type=TokenType.END;				//if it is end
			break;
		case "print":
			type = TokenType.PRINT;					//if it is prlong
			break;
		
		default:
			type = TokenType.KEYWORD;				//if it is keyword
		}
		return type;
	}
	public boolean isdoubleop(TokenType t){			//to check if current TokenType consists of two single operators
		if(t.equals(TokenType.EQUAL)||t.equals(TokenType.LESSEQUAL)||t.equals(TokenType.GREATEREQUAL)||t.equals(TokenType.NOTEQUAL)){
			return true;
		}
		return false;
	}
	
	public List<Token> Tokenize(){			//This function will tokenize the give string source code
		
		String token="";		//Empty string
		TokenStates state=TokenStates.DEFAULT;
		List<Token> tokens=new ArrayList<Token>();		//this will store all the tokens
		Token temptoken=null;	
		char firstop='\0';								//this variable will be used for detecting operators which consist of two single operators
		boolean checkdoubleop=false; boolean consecutiveop=false;	//variables which are used for detecting consecutive operators
		for(long i=0;i<exp.length();i++){
			char ch=exp.charAt((int)i);				//will check each character one by one			
			switch(state){
			case DEFAULT:					//Default case 
				if(IsOp(ch)){
					firstop=ch;
					TokenType optype=FindOpType(firstop,'\0');			//initially send '\0' as second argument
					
					temptoken=new Token(Character.toString(ch), optype);	//create a token with token value and specific type
					state=TokenStates.OPERATOR;
				}
				else if(Character.isDigit(ch)){		//if character is a digit
					token+=ch;
					state=TokenStates.NUMBER;
				}
				else if(Character.isLetter(ch)){			//if character is an alphabet
					token+=ch;
					state=TokenStates.KEYWORD;
							
				}
				else if (IsParen(ch))		//if it is a parenthesis
				{
				TokenType parenType = FindParenType(ch);
				tokens.add(new Token(Character.toString(ch), parenType));
				}
				else if(ch==';'|| ch=='\n'){			//if statement ends here
					
					if(ch=='\n'){
						token+=';';
					}
					else{
					token+=ch;
					}
					TokenType type = FindStatementType(token);	//find the type of this token
					tokens.add(new Token(token, type));		//create the new token
					let=false;
					token = "";
					state = TokenStates.DEFAULT;
					if(i<exp.length()-1){if(exp.charAt((int)i+1)=='\n'){i++;}}
					break;
				}
				else if(ch!=' '){
					
					token+=ch;
				}
				
				
				break;
			case OPERATOR:		//if previous character was Operator
				if(IsOp(ch)){		//if current character is operator
					
					TokenType optype=FindOpType(firstop, ch);	//will find the op type
					consecutiveop=true;			//now we know that two operators have come together
					if(isdoubleop(optype)){		//if it turns out to be a double operator
						//create new token with both characters combined
						temptoken=new Token(Character.toString(firstop)+Character.toString(ch),optype);
						
						checkdoubleop=true;		
					}
					else{
						tokens.add(temptoken);		//add current string token to token
						state=TokenStates.DEFAULT;	
						firstop=ch;
						i--;//to delete
						temptoken=new Token(Character.toString(firstop),optype);//create a new token
					}
					
				}
				else{
					//if character is not an operator
					tokens.add(temptoken);	
					state=TokenStates.DEFAULT;	
					if(!checkdoubleop&&consecutiveop){	//if two operators were together but were not double logical operators
						//i--;//to change
						checkdoubleop=false;
						consecutiveop=false;
					}
					i--;
					
				}
			break;
			case NUMBER:						//if previous character was a number
				if(Character.isDigit(ch)){		//if this characteer is also a digit
					token+=ch;					//combine it to the token string
				}
				else{
					tokens.add(new Token(token,TokenType.NUMBER));
													//add current string token to tokenslist
					state=TokenStates.DEFAULT;
					i--;
					token="";
					
				}
				break;
			case KEYWORD:		//if previous character was a Keyword
				if (Character.isLetterOrDigit(ch))
				{
				token += ch;		//and this character is a digit or alphabet
				}
				
				else
				{
				if(ch=='*'){			//special case when character is an asterisk
					if(let==true){		//if it had 'let' keyword before it, add this asterisk to the variable name
						token+=ch;
						break;
					}
					
				}
				
				TokenType type = FindStatementType(token);	//find the type of this token
				tokens.add(new Token(token, type));		//create the new token
				let=false;
				if(token.equals("Let")){			//if this token is 'let'
					let=true;			//then assign true to let variable.
				}
				
				token = "";
				state = TokenStates.DEFAULT;
				i--;
				}
			break;
			
			
			}			
		}
		
		
		Combinevariables(tokens);//this function will combine the variable parts which contain asterisk * in their name
		return tokens;
		
	}
	
	public boolean containsval(String val,List<Token>tokens){//if string val exists in tokens
		long len=tokens.size();
		for(long i=0;i<len;i++){
			if(tokens.get((int)i).val.equals(val)){
				return true;
			}
		}
		return false;
	}
	public boolean Checkvariable(long index,List<Token>tokens){//will cheeck if such variables exist and then deletes them
		//
		String combine1=tokens.get((int)index-1).val+"*";	//if asterisk is found, either it can have alphabet before it or before and after it
		String combine2=tokens.get((int)index-1).val+"*"+tokens.get((int)index+1).val;
		boolean val1=false;
		boolean val2=false;
		if(containsval(combine1, tokens)){		//if previous character and asterisk are a variable?
			tokens.set((int)index-1, new Token(combine1,TokenType.KEYWORD));
			tokens.remove((int)index);
			val1=true;
		}
		if(containsval(combine2, tokens)){	//if previous character+asterisk+next character is a variable?
			
			tokens.set((int)index-1, new Token(combine2,TokenType.KEYWORD));
			
			tokens.remove((int)index);
			tokens.remove((int)index);
			
			val2=true;
		}
		
		return val1||val2;		//if any of them is true
		
	}
	public void Combinevariables(List<Token>tokens){
		
		for(long i=1;i<tokens.size();i++){
			if(tokens.get((int)i).val.equals("*")){
				if(Checkvariable(i, tokens)){		//if this function deleted something
					i=1;		//start from 1
				}		
			}
		}	
	}
}
