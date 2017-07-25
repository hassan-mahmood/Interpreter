package Interpreter;

public abstract class Node {		//Parent class for holding each token
	public abstract Object evaluate();
	public abstract void setVal(Object ob);
	public abstract String gettype();
}