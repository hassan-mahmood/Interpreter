package Interpreter;

public class VariableNode extends Node{			//this will store variable name and its value.
	public String varName;
	public Parser parser;
	public VariableNode() {}
	public VariableNode(String varName, Parser parser)
	{
	this.varName = varName;
	this.parser = parser;
	}
	public Object evaluate()
	{
	Object varValue = parser.getVariable(varName);		//will get the variable value
	
	if (varValue == null)
	{
		Printfunc.print("Undefined Variable...Var Name: " + varName,-1);
		GlobalVar.sysexit();
	
	}
	return varValue;
	}
	public void setVal(Object ob){parser.setVariable(varName, (Object)ob);}
	public String gettype(){return "variable";}
}
