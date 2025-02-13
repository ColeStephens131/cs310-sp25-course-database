package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class SectionDAO {
    
    //Query to find the sections based on the termid, subjectid, and num.
    private static final String QUERY_FIND = "SELECT * FROM section WHERE termid = ? AND subjectid = ? AND num = ? ORDER BY crn";
    
    //Used to manage the database connection
    private final DAOFactory daoFactory;
    
    //Constructor
    SectionDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    //Method for searching for schecdule
    public String find(int termid, String subjectid, String num) {
        
        //Default result
        String result = "[]";
        
        //Variables
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        //Try-Catch block
        try {
            
            //Used to establish a connection to the DAOFactory
            Connection conn = daoFactory.getConnection();
            
            //Checks to see if the connection is valid. 
            if (conn.isValid(0)) {
                
                // INSERT YOUR CODE HERE
                //Prepares a SQL Query for finding the students schedule
                ps = conn.prepareStatement(QUERY_FIND);
                
                //Setting the values for the placeholders in the query.
                ps.setInt(1, termid);
                ps.setString(2, subjectid);
                ps.setString(3, num);
                
                //To execute the query and check if there are results
                boolean hasResults = ps.execute();
                        
                if (hasResults) {
                    //To get the results from the query and retrieve the metadata about the columns in the result.
                    rs = ps.getResultSet();
                    rsmd = rs.getMetaData();
                
                    //StringBuilder to construct the JSON result array
                    StringBuilder jsonResult = new StringBuilder("[");
                    
                    //To check if it is the first record in the results
                    boolean first = true;
                    while(rs.next()) {
                        //Adds a comma before appending the next JSON object if it is not the first result. 
                        if (!first) {
                            jsonResult.append(",");
                        }
                        //Becomes false after the first result.
                        first = false;
                        
                        //To being a new JSON object
                        jsonResult.append("{");
                        
                        //Loop to go through all the columns in the current row and append them to the JSON object.
                        for (int index = 1; index <= rsmd.getColumnCount(); index++) {
                            String columnName = rsmd.getColumnName(index);
                            String columnValue = rs.getString(index);
                            
                            //If the column is null, set it to an empty string. 
                            if (columnValue == null) {
                                columnValue = "";
                            }
                            
                            //Appends the column to the JSON object
                            jsonResult.append("\"").append(columnName).append("\":\"").append(columnValue).append("\"");
                            
                            //Add a comma if it's not the last column
                            if (index < rsmd.getColumnCount()) {
                                jsonResult.append(",");
                            }
                        }
                        
                        //Ends the current JSON object
                        jsonResult.append("}");
                    }
                    //Ends the JSON array after the program has gone through all the rows. 
                    jsonResult.append("]");
                    
                    result = jsonResult.toString();
                }
            }
            
        }
        
        //Prints any exception that occurs and ensures that resources are closed. 
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        //Returns the result.
        return result;
        
    }
    
}