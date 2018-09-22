package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();
	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		queryParameter.setFileName(getFileName(queryString));
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setOrderByFields(getOrderBy(queryString));
		queryParameter.setGroupByFields(getGroupBy(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
		queryParameter.setRestrictions(getRestrictions(queryString));
		queryParameter.setLogicalOperators(getLogicalOp(queryString));
		return queryParameter;
	}
		/*
		 * extract the name of the file from the query. File name can be found after the
		 * "from" clause.
		 */
		private String getFileName(String queryString){
			String[] splitStrings=queryString.toLowerCase().trim().split("\\s");
			int indexFrom=0;
			for(int part=0;part<splitStrings.length;part++)
			{
				if(splitStrings[part].equals("from"))
				{
					indexFrom=part;
				}
			}
			return splitStrings[indexFrom+1];
		}
		private String getBaseQuery(String queryString) {
			String[] splitStrings=queryString.toLowerCase().trim().split("\\s");
			int indexFrom=0;
			for(int part=0;part<splitStrings.length;part++)
			{
				if(splitStrings[part].equals("from"))
				{
					indexFrom=part;
				}
			}
			String result=String.join(" ", Arrays.copyOfRange(splitStrings, 0, indexFrom+2));
			return result;
		}
		
		
		/*
		 * extract the order by fields from the query string. Please note that we will
		 * need to extract the field(s) after "order by" clause in the query, if at all
		 * the order by clause exists. For eg: select city,winner,team1,team2 from
		 * data/ipl.csv order by city from the query mentioned above, we need to extract
		 * "city". Please note that we can have more than one order by fields.
		 */
		private List<String> getOrderBy(String queryString){
			String[] splitStrings=queryString.toLowerCase().trim().split("\\s");
			int indexOrder=0;
			if(!queryString.toLowerCase().contains("order by") )
				queryParameter.setOrderByFields(null);
			for(int part=0;part<splitStrings.length;part++)
			{
				if(splitStrings[part].equals("order"))
				{
					indexOrder=part;
				}
			}
			String result=splitStrings[indexOrder+2];
			String orderByFields[]=result.split(",");
			List<String> orderByField =
					new ArrayList<>(Arrays.asList(orderByFields));
			return orderByField;
		}
		
		
		/*
		 * extract the group by fields from the query string. Please note that we will
		 * need to extract the field(s) after "group by" clause in the query, if at all
		 * the group by clause exists. For eg: select city,max(win_by_runs) from
		 * data/ipl.csv group by city from the query mentioned above, we need to extract
		 * "city". Please note that we can have more than one group by fields.
		 */
		private List<String>  getGroupBy(String queryString){
			String[] splitStrings=queryString.toLowerCase().trim().split("\\s");
			int indexGroup=0;
			if(!queryString.toLowerCase().contains("group by") )
				queryParameter.setGroupByFields(null);
			for(int part=0;part<splitStrings.length;part++)
			{
				if(splitStrings[part].equals("group"))
				{
					indexGroup=part;
				}
			}
			String result=splitStrings[indexGroup+2];
			String groupByByFields[]=result.split(",");
			List<String> groupByField =
					new ArrayList<>(Arrays.asList(groupByByFields));
			return groupByField;
		}
		
		
		/*
		 * extract the selected fields from the query string. Please note that we will
		 * need to extract the field(s) after "select" clause followed by a space from
		 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
		 * query mentioned above, we need to extract "city" and "win_by_runs". Please
		 * note that we might have a field containing name "from_date" or "from_hrs".
		 * Hence, consider this while parsing.
		 */
		private List<String> getFields(String queryString){
			String[] splitStrings=queryString.toLowerCase().trim().split("\\s");
			int indexFrom=0,indexTo=queryString.length();
			for(String part:splitStrings)
			{
				if(part.equals("select"))
				{
					indexFrom=queryString.indexOf(part)+7;
				}
				if(part.equals("from"))
				{
					indexTo=queryString.indexOf(part)-1;
				}

			}
			String result=queryString.toLowerCase().substring(indexFrom,indexTo);
			String fields[]=new String[result.split(",").length];
			for (int i = 0; i < result.split(",").length; i++)
				fields[i] = result.split(",")[i].trim();
			List<String> field =
					new ArrayList<>(Arrays.asList(fields));
			return field;
		}
		
		
		
		
		/*
		 * extract the conditions from the query string(if exists). for each condition,
		 * we need to capture the following: 
		 * 1. Name of field 
		 * 2. condition 
		 * 3. value
		 * 
		 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
		 * where season >= 2008 or toss_decision != bat
		 * 
		 * here, for the first condition, "season>=2008" we need to capture: 
		 * 1. Name of field: season 
		 * 2. condition: >= 
		 * 3. value: 2008
		 * 
		 * the query might contain multiple conditions separated by OR/AND operators.
		 * Please consider this while parsing the conditions.
		 * 
		 */
		private List<Restriction> getRestrictions(String queryString){
			String[] splitStrings=queryString.toLowerCase().trim().split("\\s");
			int indexFrom=0;int indexTo=queryString.length(),flagWhere=0;
			List<Restriction> restrictionList = new ArrayList<>();
			for(String part:splitStrings)
			{
				if(part.equals("where"))
				{
					indexFrom=queryString.indexOf(part)+6;
					flagWhere=1;
				}
				if(part.equals("group") || part.equals("having") || part.equals("order"))
				{
					if(flagWhere==1)
						indexTo=queryString.indexOf(part)-1;
					break;
				}
			}
			String result=queryString.substring(indexFrom,indexTo);
			String conditions[]=result.split(" and | or ");
			String[] relationalOperators=new String[]{"=","!=","<","<=",">",">="};
			for(String eachCondition:conditions)
			{
				String operator = "";
				for(int operatorIndex =0; operatorIndex < relationalOperators.length; operatorIndex++)
					if(eachCondition.contains(relationalOperators[operatorIndex]))
						operator=relationalOperators[operatorIndex];
				String[] eachCond=eachCondition.trim().split(operator);
				String val=eachCond[1];
				if(eachCond[1].contains("'"))
					val=eachCond[1].substring(1,eachCond[1].length()-1);
				Restriction restriction=new Restriction();
				restriction.setPropertyName(eachCond[0].trim());
				restriction.setPropertyValue(val.trim());
				restriction.setCondition(operator);
				restrictionList.add(restriction);
			}
			if(!queryString.toLowerCase().contains("where") )
				return null;
			return restrictionList;
		}
		
		
		/*
		 * extract the logical operators(AND/OR) from the query, if at all it is
		 * present. For eg: select city,winner,team1,team2,player_of_match from
		 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
		 * bangalore
		 * 
		 * the query mentioned above in the example should return a List of Strings
		 * containing [or,and]
		 */
		private List<String> getLogicalOp(String queryString){
			if(!queryString.toLowerCase().contains("where") )
				return null;
			String[] splitStrings=queryString.toLowerCase().trim().split("\\s");
			List<String> logicalOperators=new ArrayList<>();
			for(String part:splitStrings)
			{
				if(part.equals("and") || part.equals("or"))
				{
					logicalOperators.add(part);
				}
			}
			return logicalOperators;
		}



	/*
		 * extract the aggregate functions from the query. The presence of the aggregate
		 * functions can determined if we have either "min" or "max" or "sum" or "count"
		 * or "avg" followed by opening braces"(" after "select" clause in the query
		 * string. in case it is present, then we will have to extract the same. For
		 * each aggregate functions, we need to know the following: 
		 * 1. type of aggregate function(min/max/count/sum/avg) 
		 * 2. field on which the aggregate function is being applied
		 * 
		 * Please note that more than one aggregate function can be present in a query
		 * 
		 * 
		 */
		private List<AggregateFunction> getAggregateFunctions(String queryString){
			String[] splitStrings=queryString.toLowerCase().trim().split("\\s");
			String result="";
			List<AggregateFunction> aggFunc = new ArrayList<>();
			for(String part:splitStrings)
			{
				if(part.contains("sum(") || part.contains("count(") || part.contains("min(") || part.contains("max(") || part.contains("avg("))
				{
					String[] aggPart=part.split(",");
					for(String eachAggPart:aggPart)
					{
						if(eachAggPart.contains("sum(") || eachAggPart.contains("count(") || eachAggPart.contains("min(") || eachAggPart.contains("max(") || eachAggPart.contains("avg("))
						{
							int indexOfOpen=eachAggPart.indexOf("(");
							int indexOfClose=eachAggPart.indexOf(")");
							String func=eachAggPart.substring(0,indexOfOpen);
							String fiel=eachAggPart.substring(indexOfOpen+1,indexOfClose);
							AggregateFunction aggF=new AggregateFunction(fiel,func);
							aggFunc.add(aggF);
						}
					}
				}
			}
			return aggFunc;
		}

	}

