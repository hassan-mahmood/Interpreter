package Interpreter;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Test {

	String file;
	@org.junit.Test
	public void test() {			//for the first program given in assignment
		file="test";
		System.out.println("Test1:");
		PerformTest();
		assertEquals("10\ntrue\n",GlobalVar.output);
		
	}
	
	
	
	@org.junit.Test
	public void test2() {			//for variables including * in names
		file="test2";
		System.out.println("Test2:");
		PerformTest();
	
		assertEquals("36\n", GlobalVar.output);
		
	}
	
	@org.junit.Test
	public void test3() {		//using '-' operator on numbers and booleans
		file="test3";
		System.out.println("Test3:");
		PerformTest();
		
	assertEquals("-81\ntrue\n", GlobalVar.output);
	}
	
	@org.junit.Test
	public void test4() {					//not equal '><' operator
		file="test4";
		System.out.println("Test4:");
		PerformTest();
		
	assertEquals("true\n", GlobalVar.output);	
		
		
	}
	
	@org.junit.Test
	public void test5() {					//not equal '><' operator
		file="test1";
		System.out.println("Test5:");
		PerformTest();
		
	assertEquals("true\n", GlobalVar.output);	
		
		
	}
	
	
	public void PerformTest(){
		
		GlobalVar.empty();
		Interpreter interpreter = new Interpreter();		//interpret it
		interpreter.Interpret(file);

	}
}
