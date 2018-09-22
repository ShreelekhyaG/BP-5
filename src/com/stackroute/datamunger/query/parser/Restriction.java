package com.stackroute.datamunger.query.parser;

/*
 * This class is used for storing name of field, condition and value for 
 * each conditions
 * */
public class Restriction {
	private String name,condition,value;
	// Write logic for constructor

	public void setPropertyName(String name){
		this.name=name;
	}
	public void setPropertyValue(String value){
		this.value=value;
	}
	public void setCondition(String condition){
		this.condition=condition;
	}
	public String getPropertyName() {
		// TODO Auto-generated method stub
		return name;
	}

	public String getPropertyValue() {
		// TODO Auto-generated method stub
		return value;
	}

	public String getCondition() {
		// TODO Auto-generated method stub
		return condition;
	}

	@Override
	public String toString() {
		return name+" "+condition+" "+value;
	}

}
