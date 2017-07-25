package Interpreter;				//for Parsing the tokens and then inserting them in Abstract Tree


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.BinaryOperator;

public class Parser {		//parser class
	
	int no_of_errorstatement=1;
	List<Token> tokens;				//Will Contain tokens passed from tokenizer
	public Map symboltable=new HashMap();			//Mapping variables with their values	
	List<String>declaredvar=new ArrayList<String>();	//Will keep track of the declared variables
	long currentpos=0;									//Will keep track of which token is in use
	
	public Parser(){}			//Default Constructor
	public Parser(List<Token> tokens){	//Constructor
		this.tokens=tokens;
	}
	public void setcurrentpos(long n){	//sets the currentpos variable to n
		currentpos=n;
	}
	public void settokens(List<Token>tokens){	//set tokens of Parser to the tokens passed by TOkenizer
		this.tokens=tokens;
	}
	public List<Token> gettokens(){return tokens;}	//to get all the token list
	
	public Object setVariable(String name, Object value)	//will insert a new variable in map
	{		
		symboltable.put(name, value);
		return value;
	}
	public Object getVariable(String name)				//to get value of a variable
	{
		Object value = (Object) symboltable.get(name);
		if (value != null) return value;			//if variable exists, return its value
		return null;
	}
	
	public List Block()					//Block is just a name for scope, or for executing statements
	{									//one by one
	List<Node> statements = new LinkedList<Node>();
	while ( CurrentToken().type != TokenType.END)		//until 'END' keyword is find
	{
	statements.add(Statement());			//add each executable statement to a list for later execution
	}
	MatchandEat(TokenType.END);			//Eat the 'END' keywod
	return statements;
	}
	
	public Node Assignment(boolean let){			//Assignment function called for assigning values
		Node node=null;
		String name=MatchandEat(TokenType.KEYWORD).val;		//eat the first variable on left side
		boolean getvar=declaredvar.contains(name);		//if this name is already declared
		Node value;
			//name of variable
		if(getvar||let){	//if it has come from let or the variable already exists
			if((let&&!getvar)||(!let && getvar)){		//if it has come from let and the variable doesn't exist
			
			if	(CurrentToken().type==TokenType.TERMIN){	//if this is the end of line/statement
				value=new NumberNode();
			}
			else{
				MatchandEat(TokenType.ASSIGNMENT);					//eat = op
				value = Expression();				//compute the right side
			}
			node = new AssignmentNode(name, value, this);	//create an assignment node and insert right side of assignment as value
			declaredvar.add(name);	//now this is another declared variable
			return node;
			}
			
			else{		//if it is 'let' and then already declared variable
				Printfunc.print("Variable redeclaration for "+name+" error!",no_of_errorstatement);	
				return null;
			}
		}
		else{			//if there is no 'let' before variable
			Printfunc.print("Variable "+name+" doesn't exist",no_of_errorstatement);
			GlobalVar.sysexit();
			return null;
		}
	}
	public boolean isassignment(){			//check if current token is '='
		TokenType type=CurrentToken().type;
		return type == TokenType.KEYWORD &&
				nexttoken().type == TokenType.ASSIGNMENT;
	}
	
	public void termineat(Node node){		//this will eat the semicolon ; at the end of each statement
		if(node==null){			//if node passed is null
			Printfunc.print("Assignment operation error!",no_of_errorstatement);
			GlobalVar.sysexit();
			
		}
		if(CurrentToken().type==TokenType.TERMIN){		//if current token is semicolon
			no_of_errorstatement++;
			MatchandEat(TokenType.TERMIN);
		}
		else{
			Printfunc.print("Syntax error! semicolon or newline character missing",no_of_errorstatement);	//else semicolon is missing
			GlobalVar.sysexit();				//don't execute the program
		}
	}
	

	public Node Statement(){		//read each statement and save them for later execution
		
		Node node=null;
		TokenType type=CurrentToken().type;
		
		
		if(type==TokenType.LET){			//if token is 'let' keyword
			MatchandEat(TokenType.LET);			//eat this 'let'
			node=Assignment(true);				//now look for assignment
			termineat(node);
		}
		else if(isassignment()){			//if it is assignment without 'let'
			
			node=Assignment(false);			//pass false for let
			termineat(node);
		}
		else if (type == TokenType.PRINT)		//if it is PRINT command
		{
		MatchandEat(TokenType.PRINT);
		node = new PrintNode(Expression());		//execute the statement on rigth of Print
		termineat(node);
		}
		
		else if(type==TokenType.SUBTRACT){		//if it contains - in start
			node=Expression();				//evaluate the -ve expression
			termineat(new NumberNode((long)5));//random dummy numbernode 
		}
		else
		{
		Printfunc.print("Unknown language construct: "		//if the token is neither of above, then it is syntax error
		+ CurrentToken().val,no_of_errorstatement);
		GlobalVar.sysexit();
		}
		return node;
		
	}
	
	
	public Token nexttoken(){return GetToken(1);}		//get the nedxt token
	
	public Token MatchandEat(TokenType type){			//eat the token with type 'type'
		Token token=CurrentToken();
		if(CurrentToken().type!=type){
			Printfunc.print("Unexpected Token: "+token.val,no_of_errorstatement);
			GlobalVar.sysexit();
		}
		EatToken(1);				//Move to the next token by eating the current one
		return token;
	}
	public void EatToken(long n){		//will eat n tokens
		currentpos+=n;
	}
	public Node Add(){					//eat '+' operator
		MatchandEat(TokenType.ADD);
		return Term();
	}
	public Node Sub(){
		MatchandEat(TokenType.SUBTRACT);//eat '-' operator
		return Term();
	}
	public Node Multiply(){				//eat '*' operator
		MatchandEat(TokenType.MULTIPLY);
		return Factor();
	}
	public Node Divide(){				//eat '/' operator
		MatchandEat(TokenType.DIVIDE);
		return Factor();
	}
	
	//Following functions will check if  current type is any operator 
	public boolean IsMulOp(TokenType type)			
	{
	return type == TokenType.MULTIPLY || type == TokenType.DIVIDE;
	}
	public boolean IsAddOp(TokenType type)
	{
	return type == TokenType.ADD || type == TokenType.SUBTRACT;		//arithmetic operators
	}
	public boolean IsMultiDigitOp(TokenType type)
	{
	return type == TokenType.LESSEQUAL || type == TokenType.GREATEREQUAL;
	}
	public boolean IsRelOp(TokenType type)
	{
	boolean lgOps = type == TokenType.LESS || type == TokenType.GREATER;
	boolean eqOps = type == TokenType.EQUAL || type == TokenType.NOTEQUAL;
	return eqOps || lgOps || IsMultiDigitOp(type);
	}
	public boolean IsLogicalOp(TokenType type)
	{
	return type == TokenType.OR || type == TokenType.AND;	//logical operators
	}
	public boolean IsNumber()			//check if current token is a number
	{
	return CurrentToken().type == TokenType.NUMBER;
	}
	
	public Token GetToken(long n){			//get nth token
		if(currentpos+n<tokens.size()){
			
			return tokens.get((int)(currentpos+n));
		}
		
		return new Token("",TokenType.EOF);		//return EOF if it exceeds the limit
	}
	
	public Token CurrentToken(){			//return current token
		return GetToken(0);
	}
	
	public boolean isKeyword(){				//check if current token is a keyword
		return CurrentToken().type==TokenType.KEYWORD;
	}
	public Node variable(){				//check if current token is a variable
		Token token=MatchandEat(TokenType.KEYWORD);
		Node node=new VariableNode(token.val, this);
		
		return node;
	}
	public Node Factor(){				//Factor function is 
		Node result=null;
		if (CurrentToken().type == TokenType.LPARENT)		//if current token is left parenthesis
		{
		MatchandEat(TokenType.LPARENT);		//eat left parenthesis
		result = Expression();				//evaluate expression uptil Right parenthesis
		MatchandEat(TokenType.RPARENT);		//eat right parenthesis
		}
		
		else if (IsNumber())			//if current token is a number		
		{
		Token token = MatchandEat(TokenType.NUMBER);			//eat number
		result = new NumberNode(new Integer(token.val).longValue());	//create a node which contains that number
		}
		else if (isKeyword())			//if token is a keyword
		{
		result = variable();				//create a variable node
		}
		else{
			result=Expression();			//else just evaluate the expression
		}
		return result;
	}
	
	public Node SignedFactor(){							//Signed factor for negative values
		if(CurrentToken().type==TokenType.SUBTRACT){		//if token is '-'
			MatchandEat(TokenType.SUBTRACT);			//eat - sign
			//to make change if for println or print
			Node n=Factor();							//get the node for the term after - sign
			
			Node node=new NegativeNode(n);			//create a negative node with value n
			return node;
		}
		return Factor();					//else just manipulate the token 
	}
	
	public Node Term(){					//Terms consist of Factors, a higher level manipulation
		Node node=SignedFactor();
		
		while(IsMulOp(CurrentToken().type)){		//if it is * or /
			switch(CurrentToken().type){
			case MULTIPLY:				//create binary node with both sides and * operation
				node=new Binary(TokenType.MULTIPLY, node,Multiply());
				break;
				
			case DIVIDE:				//create divide node with both sides and / operation
				node=new Binary(TokenType.DIVIDE, node, Divide());
				break;
			}
		}
		return node;
	}
	
	
	public Node Relation()				//functoin for solving relational operation
	{
		Node node = solve();			//solve the arithmetic expressions
		if (IsRelOp(CurrentToken().type))			//if token is relational operation
		{
			switch(CurrentToken().type)
			{
			case LESS:					//for each operator, create a boolean node with that operator
				node = Less(node);
				break;
			case GREATER:
				node = Greater(node);					// for >
				break;
			case EQUAL:
				node = Equal(node);				// for ==
				break;
			case NOTEQUAL:
				node = NotEqual(node);							// for ><
				break;
			case LESSEQUAL:
				node = LessEqual(node);						// for <=
				break;
			case GREATEREQUAL:
				node = GreaterEqual(node);						// for >=
				break;
			}
		}
		return node;
	}
	
	public Node Less(Node node)					//will eat 'less' operator and create boolean node
	{
	MatchandEat(TokenType.LESS);
	return new Binary(TokenType.LESS, node, solve());
	}
	public Node Greater(Node node)
	{
	MatchandEat(TokenType.GREATER);			//>
	return new Binary(TokenType.GREATER, node, solve());
	}
	public Node Equal(Node node)
	{	
	MatchandEat(TokenType.EQUAL);		//==
	return new Binary(TokenType.EQUAL, node, solve());
	}
	public Node NotEqual(Node node)
	{
	MatchandEat(TokenType.NOTEQUAL);		//><
	return new Binary(TokenType.NOTEQUAL, node, solve());
	}
	public Node LessEqual(Node node)
	{
	MatchandEat(TokenType.LESSEQUAL);			//<=
	return new Binary(TokenType.LESSEQUAL, node, solve());
	}
	public Node GreaterEqual(Node node)
	{
	MatchandEat(TokenType.GREATEREQUAL);			//>=
	return new Binary(TokenType.GREATEREQUAL, node, solve());
	}
	
	public Node BoolFactor()
	{
	return Relation();				//bool factor will call relation function for relational operators
	}	
	
	public Node BoolTerm()			//boolean term
	{
	Node node = BoolFactor();
	while (CurrentToken().type == TokenType.AND)			//manipulate AND
	{
	MatchandEat(TokenType.AND);
	node = new Binary(TokenType.AND, node,  BoolFactor());
	}
	return node;
	}
	
	public Node BooleanExpression()				//Manipulate OR
	{
		Node node = BoolTerm();
		while (IsLogicalOp(CurrentToken().type))
		{
		switch(CurrentToken().type)
		{
		case OR:
		MatchandEat(TokenType.OR);
		node = new Binary(TokenType.OR, node, BoolTerm());
		break;
		}
		}
		return node;
	}
	
	public Node Expression()			//Expression function as a generic function for all
	{
	return BooleanExpression();
	}
	
	
	public Node solve(){				//will solve arithmatic expression
		
		Node node=Term();				
		
		while(IsAddOp(CurrentToken().type)){			//look for + and -
			switch(CurrentToken().type){
			case ADD:
				node=new Binary(TokenType.ADD,node,Add());
				break;
			case SUBTRACT:
				node= new Binary(TokenType.SUBTRACT,node, Sub());
				break;
		}
		
	}
		return node;
	
	}
}