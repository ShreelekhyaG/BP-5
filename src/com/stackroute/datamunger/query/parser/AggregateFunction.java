package com.stackroute.datamunger.query.parser;

/* This class is used for storing name of field, aggregate function for 
 * each aggregate function
 * */
public class AggregateFunction {
	private String fields,functions;
	// Write logic for constructor
	public AggregateFunction(String field, String function) {
		fields=field;
		functions=function;
	}

	public String getFunction() {
		// TODO Auto-generated method stub
		return functions;
	}

	public String getField() {
		// TODO Auto-generated method stub
		return fields;
	}

	@Override
	public String toString(){
		return fields+" "+functions;
	}


}
