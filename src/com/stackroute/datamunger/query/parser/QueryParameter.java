package com.stackroute.datamunger.query.parser;

import java.util.List;
/* 
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */
public class QueryParameter {
	private String queryString,file,baseQuery,QUERY_TYPE;
	private List<String> fields,orderByFields,groupByFields,logicalOperators;
	private List<Restriction> restrictions;
	private List<AggregateFunction> aggregateFunctions;

	public void setFileName(String filename) {
		file=filename;
	}
	public String getFileName() { return file; }

	public void setBaseQuery(String basequery) {
		baseQuery=basequery;
	}
	public String getBaseQuery() {
		return baseQuery;
	}

	public void setRestrictions(List<Restriction> restrictionList) {
		restrictions=restrictionList;
	}
	public List<Restriction> getRestrictions() {
		return restrictions;
	}

	public void setLogicalOperators(List<String> logicalOp) {
		logicalOperators=logicalOp;
	}
	public List<String> getLogicalOperators() {
		return logicalOperators;
	}

	public void setFields(List<String> fields1) {
		fields=fields1;
	}
	public List<String> getFields() {
		return fields;
	}

	public void setAggregateFunctions(List<AggregateFunction> aggregateFunctionList) {
		aggregateFunctions=aggregateFunctionList;
	}
	public List<AggregateFunction> getAggregateFunctions() {
		return aggregateFunctions;
	}

	public void setGroupByFields(List<String> group) {
		groupByFields=group;
	}
	public List<String> getGroupByFields() {
		return groupByFields;
	}

	public void setOrderByFields(List<String> order) {
		orderByFields=order;
	}
	public List<String> getOrderByFields() {
		return orderByFields;
	}

	public String getQUERY_TYPE() {
		// TODO Auto-generated method stub
		return null;
	}

		

	
}
