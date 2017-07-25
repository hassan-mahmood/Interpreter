package Interpreter;

public class BooleanNode extends Node
{//boolean node for holding boolean values and expression
Boolean value;
public BooleanNode() {}
public BooleanNode(Boolean value)
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
public String gettype(){return "boolean";}
public void setVal(Object ob)
{
	//do nothing
	}

}