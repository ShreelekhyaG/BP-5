package com.stackroute.datamunger.query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Implementation of DataTypeDefinitions class. This class contains a method getDataTypes() 
 * which will contain the logic for getting the datatype for a given field value. This
 * method will be called from QueryProcessors.   
 * In this assignment, we are going to use Regular Expression to find the 
 * appropriate data type of a field. 
 * Integers: should contain only digits without decimal point 
 * Double: should contain digits as well as decimal point 
 * Date: Dates can be written in many formats in the CSV file. 
 * However, in this assignment,we will test for the following date formats('dd/mm/yyyy',
 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm-dd')
 */
public class DataTypeDefinitions {
	//BufferedReader bufferedReader;
	//method stub
	public static Object getDataType(String input) throws IOException {
//		BufferedReader bufferedReader=new BufferedReader(new FileReader(input));
//		bufferedReader.readLine();
//		String secondLine=bufferedReader.readLine();
		String[] data=input.split(",",-1);
		String[] dataType=new String[data.length];
		for(int index=0;index<data.length;index++) {
			if(data[index].length()==0) {
				dataType[index]="java.lang.Object";
				continue;
			}
			// checking for Integer
			String regex="^[0-9]+$";
			Pattern pattern= Pattern.compile(regex);
			Matcher matcher=pattern.matcher(data[index]);
			if(matcher.find()) {
				dataType[index] = "java.lang.Integer";
				continue;
			}
			// checking for floating point numbers
			regex="^[0-9]+[\\.]+[0-9]+$";
			pattern=Pattern.compile(regex);
			matcher=pattern.matcher(data[index]);
			if(matcher.find()) {
				dataType[index] = "java.lang.Float";
				continue;
			}
			// checking for date format dd/mm/yyyy
			regex="^(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/(\\d{4})$";
			pattern=Pattern.compile(regex);
			matcher=pattern.matcher(data[index]);
			if(matcher.find()) {
				dataType[index] = "java.util.Date";
				continue;
			}
			// checking for date format mm/dd/yyyy
			regex="^(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/(\\d{4})$";
			pattern=Pattern.compile(regex);
			matcher=pattern.matcher(data[index]);
			if(matcher.find()) {
				dataType[index] = "java.util.Date";
				continue;
			}
			// checking for date format dd-mon-yy
			regex="^(0?[1-9]|[12][0-9]|3[01])-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-(\\d{2})$";
			pattern=Pattern.compile(regex);
			matcher=pattern.matcher(data[index]);
			if(matcher.find()) {
				dataType[index] = "java.util.Date";
				continue;
			}
			// checking for date format dd-mon-yyyy
			regex="^(0?[1-9]|[12][0-9]|3[01])-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-(\\d{4})$";
			pattern=Pattern.compile(regex);
			matcher=pattern.matcher(data[index]);
			if(matcher.find()) {
				dataType[index] = "java.util.Date";
				continue;
			}
			// checking for date format dd-month-yy
			regex="^(0?[1-9]|[12][0-9]|3[01])-(January|February|March|April|May|June|July|August|Septempber|October|November|December)-(\\d{2})$";
			pattern=Pattern.compile(regex);
			matcher=pattern.matcher(data[index]);
			if(matcher.find()) {
				dataType[index] = "java.util.Date";
				continue;
			}
			// checking for date format dd-month-yyyy
			regex="^(0?[1-9]|[12][0-9]|3[01])-(January|February|March|April|May|June|July|August|Septempber|October|November|December)-(\\d{4})$";
			pattern=Pattern.compile(regex);
			matcher=pattern.matcher(data[index]);
			if(matcher.find()) {
				dataType[index] = "java.util.Date";
				continue;
			}
			// checking for date format yyyy-mm-dd
			regex="^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$";
			pattern=Pattern.compile(regex);
			matcher=pattern.matcher(data[index]);
			if(matcher.find()) {
				dataType[index] = "java.util.Date";
				continue;
			}
			dataType[index]="java.lang.String";
		}
		// checking for floating point numbers
		
		// checking for date format dd/mm/yyyy
		
		// checking for date format mm/dd/yyyy
		
		// checking for date format dd-mon-yy
		
		// checking for date format dd-mon-yyyy
		
		// checking for date format dd-month-yy
		
		// checking for date format dd-month-yyyy
		
		// checking for date format yyyy-mm-dd
		
		return dataType;
	}
	

	
}
