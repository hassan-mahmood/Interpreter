package Interpreter;

public class AssignmentNode extends Node{		//this will hold name of variable and value
	public String name;
	public Node value;
	public Parser parser;
	public String scope;
	public AssignmentNode() {}
	public AssignmentNode(String name, Node value, Parser parser)
	{
	this.name = name;
	this.value = value;
	this.parser = parser;
	}
	public Object evaluate()
	{
	return parser.setVariable(name, value.evaluate());
	}
	public void setVal(Object ob){value= (Node)ob;}
	public String gettype(){return "assignment";}		//get the type of this node
}
