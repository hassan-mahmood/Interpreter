package Interpreter;

public class PrintNode extends Node{		//will store the expression or the parameter of print funtion

	public Node expr;
	public String type;
	
	public PrintNode(){
		
	}
	
	public PrintNode(Node expr){
		this.expr=expr; 
		this.type=type;
	}
	public Object evaluate()		//evaluate means evaluate the expression and print the result
	{
	Object writee = expr.evaluate();
	

		Printfunc.print(writee.toString(),-1);
		//Printfunc.print(writee);
	
	return writee;
	}
	public void setVal(Object ob){//do nothing
		
	}
	public String gettype(){return "print";}
}
