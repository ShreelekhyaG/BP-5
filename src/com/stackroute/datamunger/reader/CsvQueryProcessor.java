package com.stackroute.datamunger.reader;

import com.stackroute.datamunger.query.*;
import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.Restriction;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CsvQueryProcessor implements QueryProcessingEngine {
	/*
	 * This method will take QueryParameter object as a parameter which contains the
	 * parsed query and will process and populate the ResultSet
	 */
	String fileName;
	BufferedReader bufferedReader;
	public DataSet getResultSet(QueryParameter queryParameter) throws IOException {

		/*
		 * initialize BufferedReader to read from the file which is mentioned in
		 * QueryParameter. Consider Handling Exception related to file reading.
		 */
		fileName=queryParameter.getFileName();
		bufferedReader=new BufferedReader(new FileReader(fileName));

		/*
		 * read the first line which contains the header. Please note that the headers
		 * can contain spaces in between them. For eg: city, winner
		 */
		String[] headerElements=getHeader();

		/*
		 * read the next line which contains the first row of data. We are reading this
		 * line so that we can determine the data types of all the fields. Please note
		 * that ipl.csv file contains null value in the last column. If you do not
		 * consider this while splitting, this might cause exceptions later
		 */
		bufferedReader.readLine();
		String secondLine=bufferedReader.readLine();
		DataTypeDefinitions dataTypeDefinitions=new DataTypeDefinitions();
		String[] dataTypes= (String[]) dataTypeDefinitions.getDataType(secondLine);
		/*
		 * populate the header Map object from the header array. header map is having
		 * data type <String,Integer> to contain the header and it's index.
		 */
		Header header=new Header();
		for(int index=0;index<headerElements.length;index++)
			header.put(headerElements[index],index);

		/*
		 * We have read the first line of text already and kept it in an array. Now, we
		 * can populate the RowDataTypeDefinition Map object. RowDataTypeDefinition map
		 * is having data type <Integer,String> to contain the index of the field and
		 * it's data type. To find the dataType by the field value, we will use
		 * getDataType() method of DataTypeDefinitions class
		 */
		RowDataTypeDefinitions rowDataTypeDefinitions=new RowDataTypeDefinitions();
		for(int index=0;index<dataTypes.length;index++)
			rowDataTypeDefinitions.put(index,dataTypes[index]);

		/*
		 * once we have the header and dataTypeDefinitions maps populated, we can start
		 * reading from the first line. We will read one line at a time, then check
		 * whether the field values satisfy the conditions mentioned in the query,if
		 * yes, then we will add it to the resultSet. Otherwise, we will continue to
		 * read the next line. We will continue this till we have read till the last
		 * line of the CSV file.
		 */

		/* reset the buffered reader so that it can start reading from the first line */
		//bufferedReader.reset();
		bufferedReader=new BufferedReader(new FileReader(fileName));

		/*
		 * skip the first line as it is already read earlier which contained the header
		 */
		String line=bufferedReader.readLine();
		line=bufferedReader.readLine();
		String[] trimmedFields=null;
		Row row=new Row();
		DataSet dataSet=new DataSet();
		Long key=new Long(1L);
		Filter filter=new Filter();
		/* read one line at a time from the CSV file till we have any lines left */
		while(line!=null){

			String[] lineFields=line.split(",",-1);
			trimmedFields= new String[lineFields.length];
			for (int i = 0; i < lineFields.length; i++)
				trimmedFields[i] = lineFields[i].trim();
			List<String> logicalOps=queryParameter.getLogicalOperators();
			int logicaIndex=0,restrictionSize=0,count=0;
			String operator="";
			boolean lineSatisfies=false;
			Boolean[] conditionSatisfies=new Boolean[restrictionSize];
			List<Boolean> conditionList=new ArrayList<>();
			List<Restriction> restrictionList;
			if(queryParameter.getRestrictions()==null)
				lineSatisfies=true;
			if(queryParameter.getRestrictions()!=null){
				restrictionList=queryParameter.getRestrictions();
				restrictionSize=restrictionList.size();
				for(Restriction restriction1:restrictionList){

					String name=restriction1.getPropertyName();
					String value=restriction1.getPropertyValue();
					String condition=restriction1.getCondition();
					conditionSatisfies=new Boolean[restrictionSize];
					Integer index=header.get(name);
					if(filter.evaluateExpression(trimmedFields[index],value,condition)){
						conditionList.add(true);
						conditionSatisfies[count]=true;}
					else{
						conditionList.add(false);
						conditionSatisfies[count]=false;}
					count++;
				}

				Boolean first=null,second=null;
				for(int index=0;index<logicalOps.size();index++){
					if(conditionList.size()==3){
						if(logicalOps.get(index).equals("and") && logicalOps.get(index).equals("or")){
							if( conditionList.get(index) && conditionList.get(index+1) || conditionList.get(index+2))
								first=true;
							else
								first=false;
							break;
						}
						if(logicalOps.get(index).equals("or") && logicalOps.get(index).equals("and")){
							if( conditionList.get(index) || conditionList.get(index+1) && conditionList.get(index+2))
								first=true;
							else
								first=false;
							break;
						}

					}
					if(logicalOps.get(index).equals("and") ){
						if( conditionList.get(index) && conditionList.get(index+1))
							first=true;
						else
							first=false;
						break;
					}
					if(logicalOps.get(index).equals("or") ){
						if(conditionList.get(index) || conditionList.get(index+1))
							first=true;
						else
							first=false;
						break;
					}

				}
				if(restrictionList.size()==1 && conditionList.get(0)==true)
					lineSatisfies=true;
				else
					lineSatisfies=false;
				if(first!=null && first==true)
					lineSatisfies=true;
			}
			row=new Row();
			List<String> fields=queryParameter.getFields();
			if(fields.size()==1 && fields.get(0).equals("*"))
			{
				for(int index=0;index<trimmedFields.length;index++){
					row.put(headerElements[index],trimmedFields[index]);
				}
			}
			else{
				for(String field:fields){
					for(int index=0;index<trimmedFields.length;index++){
						if(headerElements[index].equals(field) && lineSatisfies==true)
							row.put(headerElements[index],trimmedFields[index]);
					}
				}
			}
			if(row.size()!=0) {
				dataSet.put(key,row);
				key++;
			}
			line=bufferedReader.readLine();
		}
		/*
		 * once we have read one line, we will split it into a String Array. This array
		 * will continue all the fields of the row. Please note that fields might
		 * contain spaces in between. Also, few fields might be empty.
		 */
		/*
		 * if there are where condition(s) in the query, test the row fields against
		 * those conditions to check whether the selected row satifies the conditions
		 */

		/*
		 * from QueryParameter object, read one condition at a time and evaluate the
		 * same. For evaluating the conditions, we will use evaluateExpressions() method
		 * of Filter class. Please note that evaluation of expression will be done
		 * differently based on the data type of the field. In case the query is having
		 * multiple conditions, you need to evaluate the overall expression i.e. if we
		 * have OR operator between two conditions, then the row will be selected if any
		 * of the condition is satisfied. However, in case of AND operator, the row will
		 * be selected only if both of them are satisfied.
		 */

		/*
		 * check for multiple conditions in where clause for eg: where salary>20000 and
		 * city=Bangalore for eg: where salary>20000 or city=Bangalore and dept!=Sales
		 */

		/*
		 * if the overall condition expression evaluates to true, then we need to check
		 * if all columns are to be selected(select *) or few columns are to be
		 * selected(select col1,col2). In either of the cases, we will have to populate
		 * the row map object. Row Map object is having type <String,String> to contain
		 * field Index and field value for the selected fields. Once the row object is
		 * populated, add it to DataSet Map Object. DataSet Map object is having type
		 * <Long,Row> to hold the rowId (to be manually generated by incrementing a Long
		 * variable) and it's corresponding Row Object.
		 */

		/* return dataset object */
		return dataSet;
	}
	public String[] getHeader() throws IOException {
		// TODO Auto-generated method stub
		bufferedReader=new BufferedReader(new FileReader(fileName));
		// read the first line
		String firstLine=bufferedReader.readLine();
		Header header=new Header();
		String[] trimmedArray = new String[firstLine.split(",").length];
		for (int i = 0; i < firstLine.split(",").length; i++)
			trimmedArray[i] = firstLine.split(",")[i].trim();

		header.setHeaders(trimmedArray);
		// populate the header object with the String array containing the header names
		return trimmedArray;
	}

}
