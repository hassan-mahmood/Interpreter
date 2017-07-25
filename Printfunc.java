package Interpreter;

public class Printfunc {
	public static void print(String st, int line){
		if(line!=-1){
			st=st+" at line number "+line;
		}
		GlobalVar.output=GlobalVar.output+st+"\n";
		//System.out.println(st);
	}
}
