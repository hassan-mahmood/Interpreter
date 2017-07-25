package Interpreter;
	//an important node, which will be used for negating both integers and booleans
	
public class NegativeNode extends Node{
	public Node node;
	
	public NegativeNode(){}
	public boolean boolval=false;
	public NegativeNode(Node node){
		this.node=node;
	}
	
	public Object getLong(){
		return (new Long(5));
	}
	
	public Long ToLong(Node node){		//convert the node to integer
		Object res=node.evaluate();		
		if(res.getClass().equals(getLong().getClass())){		//if returned res is integer
			return ((Long)res).longValue();	//then simply cast res to integer and return it
		}
		boolval=true;						//else consider that res is bool
		return (boolean)res==true? (long)0:(long)1;		//reversing for negative of boolean
	}
	
	public Object evaluate(){
		Object result;
		long val=ToLong(node);						//result from ToInt function
		if(boolval==true){							//if the result was bool then convert it to bool
			if(val==0){
				result=new Boolean(false);	
			}
			else{
				result=new Boolean(true);
			}
			
			
		}
		else{				//if the res was Integer, then simply negate the integer
		result=new Long(-ToLong(node));
		}
		if(node.gettype().equals("variable")){
			node.setVal(result);
		}
		
		return result;
	}
	
	public void setVal(Object ob){//do nothing
	}
	public String gettype(){return "negative";}
	
}
