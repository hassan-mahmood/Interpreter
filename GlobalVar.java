package Interpreter;

public class GlobalVar {
	public static String output="";
	public static void empty(){
		output="";
	}
	public static void sysexit(){
		
		System.out.println(GlobalVar.output);
		System.exit(0);
	}
	
}
