package Interpreter;

public class NumberNode extends Node			//this node will hold the 
{
Long value;
public NumberNode() {}
public NumberNode(Long value)
{
this.value = value;
}
public Object evaluate()
{
return value;
}
public String toString()
{
return value+"";
}
public void setVal(Object ob){//do nothing
	
}
public String gettype(){return "number";}
}