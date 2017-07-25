package Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.LinkedList;

public class Interpreter {		//it will interpret the output of Tokenizer

	
	public void Interpret(String file)
	{
		try{
		String sourceCode=ReadFile(file);		//read source code from file 'filehassan'
		Tokenizer tokenizer = new Tokenizer();		//Concrete object of tokenizer
		tokenizer.setexpression(sourceCode);			//set source code for tokenizer
		
		List<Token>t=tokenizer.Tokenize();
		
		Parser parser = new Parser(t);				//parser to parse the tokens
		
		List<Node> script = parser.Block();			//will read each statement as block
		
		
		for (Node statement:script)				//Now for each statement, execute them one by one
			statement.evaluate();
		
		}
		catch(Exception e){
			GlobalVar.sysexit();
		}
	}
	
	private String ReadFile(String path)		//read file from path 
	{	
		
		FileInputStream stream = null;
		InputStreamReader input = null;
		try
		{
		stream = new FileInputStream(path);				//input stream from path
		input = new InputStreamReader(stream, Charset.defaultCharset());
		Reader reader = new BufferedReader(input);
		StringBuilder builder = new StringBuilder();		///string builder
		char[] buffer = new char[1024];		//stream buffer 1024 bytes
		long read;
		while ((read = reader.read(buffer, 0, buffer.length)) > 0)
		{
		builder.append(buffer, 0, (int)read);
		}
		//Keep the space in the end
		builder.append("end ");
		return builder.toString();
		
			
		}
		catch (FileNotFoundException e)
		{
		String errMsg = "FILE NOT FOUND. ";
		String sourceInfo = "Error in Interpreter.java->"
		+ "ReadFile(String path) method. ";
		Printfunc.print(sourceInfo + errMsg,-1);
		GlobalVar.sysexit();
		}
		catch (IOException e)
		{
		String errMsg = "Error while reading the script. ";
		String sourceInfo = "Error in Interpreter.java->"
		+ "ReadFile(String path) method. ";
		Printfunc.print(sourceInfo + errMsg,-1);
		GlobalVar.sysexit();
		}
		catch (Exception e)
		{
		String errMsg = "Error while reading the script. ";
		String sourceInfo = "Error in Interpreter.java->"
		+ "ReadFile(String path) method. ";
		Printfunc.print(sourceInfo + errMsg + e,-1);
		GlobalVar.sysexit();
		}
		finally
		{
		try
		{
		input.close();
		stream.close();
		}
		catch (Exception e)
		{
		String errMsg = "Error while closing a stream or a stream reader. ";
		String sourceInfo = "Error in Interpreter.java->"
		+ "ReadFile(String path) method. ";
		Printfunc.print(sourceInfo + errMsg + e,-1);
		GlobalVar.sysexit();
		}
		}
		return null;
	}
}
