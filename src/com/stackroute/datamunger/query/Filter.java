package com.stackroute.datamunger.query;

//This class contains methods to evaluate expressions
public class Filter {
	
	/* 
	 * The evaluateExpression() method of this class is responsible for evaluating 
	 * the expressions mentioned in the query. It has to be noted that the process 
	 * of evaluating expressions will be different for different data types. there 
	 * are 6 operators that can exist within a query i.e. >=,<=,<,>,!=,= This method 
	 * should be able to evaluate all of them. 
	 * Note: while evaluating string expressions, please handle uppercase and lowercase 
	 * 
	 */
	public boolean evaluateExpression(String element,String value,String condition){
		boolean flag=false;
		if(condition.equals("="))
			flag=equalTo(element,value);
		if(condition.equals("!="))
			flag=notEqualTo(element,value);
		if(condition.equals("<"))
			flag=lessThan(element,value);
		if(condition.equals(">"))
			flag=greaterThan(element,value);
		if(condition.equals("<="))
			flag=lessThanOrEqual(element,value);
		if(condition.equals(">="))
			flag=greaterThanOrEqualTo(element,value);
		return flag;
	}
	
	
	
	
	
	//Method containing implementation of equalTo operator
	public boolean equalTo(String input1,String input2){
		return input1.toLowerCase().equals(input2.toLowerCase());
	}
	
	
	
	
	
	//Method containing implementation of notEqualTo operator
	public boolean notEqualTo(String input1,String input2){
		return !input1.toLowerCase().equals(input2.toLowerCase());
	}
	
	
	
	
	
	
	//Method containing implementation of greaterThan operator
	public boolean greaterThan(String input1,String input2){
		if(isInteger(input1.trim()) && isInteger(input2.trim())){
			if(Integer.parseInt(input1)>Integer.parseInt(input2))
				return true;
			else
				return false;
		}
		if(input1.compareTo(input2)>0)
			return true;
		else
			return false;
	}
	
	
	
	
	
	
	//Method containing implementation of greaterThanOrEqualTo operator
	public boolean greaterThanOrEqualTo(String input1,String input2){
		if(isInteger(input1.trim()) && isInteger(input2.trim())){
			if(Integer.parseInt(input1.trim())>=Integer.parseInt(input2.trim()))
				return true;
			else
				return false;
		}
		if(input1.compareTo(input2)>=0)
			return true;
		else
			return false;
	}
	
	
	
	
	
	//Method containing implementation of lessThan operator
	public boolean lessThan(String input1,String input2){
		if(isInteger(input1.trim()) && isInteger(input2.trim())){
			if(Integer.parseInt(input1)<Integer.parseInt(input2))
				return true;
			else
				return false;
		}
		if(input1.compareTo(input2)<0)
			return true;
		else
			return false;
	}
	
	
	
	
	//Method containing implementation of lessThanOrEqualTo operator
	public boolean lessThanOrEqual(String input1,String input2){
		if(isInteger(input1.trim()) && isInteger(input2.trim())){
			if(Integer.parseInt(input1)<=Integer.parseInt(input2))
				return true;
			else
				return false;
		}
		if(input1.compareTo(input2)<=0)
			return true;
		else
			return false;
	}
	public boolean isInteger(String field){
		try{
			Integer.parseInt(field);
			return true;
		}
		catch (NumberFormatException e){
			return false;
		}
	}
}
