package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.*;
import com.github.cliftonlabs.json_simple.*;
import java.util.ArrayList;

public class DAOUtility {
    
    //Constant representing the term ID for FA24.
    public static final int TERMID_FA24 = 1;
    
    //Method to convert ResultSet to a JSON string.
    public static String getResultSetAsJson(ResultSet rs) {
        
        //Creates a JsonArray.
        JsonArray records = new JsonArray();
        
        //Try-Catch Block.
        try {
        
            //Checks to see if the Result set is not null.
            if (rs != null) {

                // INSERT YOUR CODE HERE
                
                //Gets the meta data about the ResultSet and gets the number of columns in the result set. 
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int columnCount = rsMetaData.getColumnCount();
                
                // Iterates through each row
                while (rs.next()) {
                    
                    //Creates a new JsonObject
                    JsonObject record = new JsonObject();
                    
                    //Iterates through each column
                    for (int index = 1; index <= columnCount; index++) {
                        String columnName = rsMetaData.getColumnName(index);
                        Object columnValue = rs.getObject(index);
                        
                        //Adds the column name and its value to the current JsonObject
                        record.put(columnName, columnValue);
                    }
                    
                    //Adds the current JsonObject to the JsonArray
                    records.add(record);
                }
            }
            
        }
        //Prints any exception that occurs.
        catch (Exception e) {
            e.printStackTrace();
        }
        
        //Serialize the JsonArray into a JSON string and returns it. 
        return Jsoner.serialize(records);
        
    }
    
}
