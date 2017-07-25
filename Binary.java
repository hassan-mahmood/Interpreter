package Interpreter;

public class Binary extends Node{		//binary node for binary operations e.g. add, sub, etc.
	public TokenType op;
	public Node leftexp;
	public Node rightexp;
	
	public Binary(){}
	
	public Binary(TokenType op, Node leftexp, Node rightexp){
		this.op=op;
		this.leftexp=leftexp;
		this.rightexp=rightexp;
	}
	
	public Long ToLong(Node n){
		
		Object res=n.evaluate();
		return ((Long)res).longValue();
	}
	public boolean ToBool(Node n){
		Object res=n.evaluate();
		return ((Boolean)res).booleanValue();
	}
	public Object ToObject(Node n){
		return n.evaluate();
	}
	public Object evaluate(){
		Object result=null;
		try{
		switch(op){
		case ADD:
			result=new Long(ToLong(leftexp)+ToLong(rightexp));
			break;
		case SUBTRACT:
			result=new Long(ToLong(leftexp)-ToLong(rightexp));
			break;
		case MULTIPLY:
			result=new Long (ToLong(leftexp)*ToLong(rightexp));
			break;
		case DIVIDE:
			if(ToLong(rightexp)==0){
				Printfunc.print("Divide by zero error!",-1);
			}
			result=new Long (ToLong(leftexp)/ToLong(rightexp));
			break;
		case LESS:
			result = new Boolean(ToLong(leftexp) < ToLong(rightexp));
			break;
		case GREATER:
			result = new Boolean(ToLong(leftexp) > ToLong(rightexp));
			break;
			// != and == work as equal and !equal for strings
		case EQUAL:
			result=ToObject(leftexp).equals(ToObject(rightexp));
			break;
		case NOTEQUAL:
			result = new Boolean(!ToObject(leftexp).equals(ToObject(rightexp)));
			break;
		case LESSEQUAL:
			result = new Boolean(ToLong(leftexp) <= ToLong(rightexp));
			break;
		case GREATEREQUAL:
			result = new Boolean(ToLong(leftexp) >= ToLong(rightexp));
			break;
		case OR:
			result = new Boolean(ToBool(leftexp) || ToBool(rightexp));
			break;
		case AND:
			result = new Boolean(ToBool(leftexp) && ToBool(rightexp));
			break;
		}
		}
		catch(Exception e){
			Printfunc.print("Error in Types of variables!",-1);
			GlobalVar.sysexit();
		}
		return result;
	}
	public String gettype(){return "binary";}
	public void setVal(Object ob){//do nothing
	
	}
	

}
