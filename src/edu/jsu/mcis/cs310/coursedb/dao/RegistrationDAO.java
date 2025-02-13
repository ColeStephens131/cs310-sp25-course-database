package edu.jsu.mcis.cs310.coursedb.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class RegistrationDAO {
    
    //SQL Queries for Registration
    private static final String QUERY_CREATE = "INSERT INTO registration (studentid, termid, crn) values (?, ?, ?)";
    private static final String QUERY_DROP = "DELETE FROM registration WHERE studentid = ? AND termid = ? and crn = ?";
    private static final String QUERY_WITHDRAW = "DELETE FROM registration WHERE studentid = ? and termid = ?";
    private static final String QUERY_LIST = "SELECT studentid, termid, crn FROM registration WHERE studentid = ? AND termid = ? ORDER BY crn";
    
    //Used to manage the database connection
    private final DAOFactory daoFactory;
    
    //Constructor
    RegistrationDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    //Method for registering for a course
    public boolean create(int studentid, int termid, int crn) {
        
        //Variables
        boolean result = false;
        
        //
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        //Try-Catch block
        try {
            //Used to establish a connection to the DAOFactory
            Connection conn = daoFactory.getConnection();
            
            //Checks to see if the connection is valid.
            if (conn.isValid(0)) {
                
                // INSERT YOUR CODE HERE
               
                //Prepares the SQL Query for registration.
                ps = conn.prepareStatement(QUERY_CREATE);
                
                //Setting the values for the placeholders in the query
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                ps.setInt(3, crn);
                
                //To execute the update and get the number of affected rows. 
                int updateCount = ps.executeUpdate();
                
                //If one row was affected, the registration was successful.
                if (updateCount > 0) {
                    result = true;
                }
                
            }
            
        }
        
        //Catches any exceptions that occur and enusres that resources are closed.
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        //Returns the result of the registration
        return result;
        
    }

    //Method to drop a previous registration for a single course. 
    public boolean delete(int studentid, int termid, int crn) {
        
        //Variables
        boolean result = false;
        
        PreparedStatement ps = null;
        
        //Try-Catch Block
        try {
            
            //Used to establish a connection to the DAOFactory
            Connection conn = daoFactory.getConnection();
            
            //Checks to see if the connection is valid.
            if (conn.isValid(0)) {
                
                // INSERT YOUR CODE HERE
                //Prepares the SQL Query for dropping.
                ps = conn.prepareStatement(QUERY_DROP);
                
                //Setting the values for the placeholders in the query
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                ps.setInt(3, crn);
                
                //To execute the update and get the number of affected rows.
                int updateCount = ps.executeUpdate();
                
                //If one row was affected, the drop was successful.
                if (updateCount > 0) {
                    result = true;
                }
                
            }
            
        }
        
        //Catches any exceptions that occur and enusres that resources are closed.
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        //Returns the result of the drop
        return result;
        
    }
    
    //Method to Withdraw from all registerd courses. 
    public boolean delete(int studentid, int termid) {
        
        //Variables
        boolean result = false;
        
        PreparedStatement ps = null;
        
        //Try-Catch Block
        try {
            
            //Used to establish a connection to the DAOFactory
            Connection conn = daoFactory.getConnection();
            
            //Checks to see if the connection is valid.
            if (conn.isValid(0)) {
                
                // INSERT YOUR CODE HERE
                //Prepares the SQL Query for Withdrawing.
                ps = conn.prepareStatement(QUERY_WITHDRAW);
                
                //Setting the values for the placeholders in the query
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                
                //To execute the update and get the number of affected rows.
                int updateCount = ps.executeUpdate();
                
                //If one row was affected, the withdraw was successful.
                if (updateCount > 0) {
                    result = true;
                }
                
            }
            
        }
        
        //Catches any exceptions that occur and enusres that resources are closed.
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        //Returns the result of the withdraw
        return result;
        
    }

    //Method to list all registered courses.
    public String list(int studentid, int termid) {
        
        //Variables
        String result = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        //Try-Catch Block
        try {
            
            //Used to establish a connection to the DAOFactory
            Connection conn = daoFactory.getConnection();
            
            //Checks to see if the connection is valid.
            if (conn.isValid(0)) {
                
                // INSERT YOUR CODE HERE
                //Prepares the SQL Query for listing.
                ps = conn.prepareStatement(QUERY_LIST);
                
                //Setting the values for the placeholders in the query
                ps.setInt(1, studentid);
                ps.setInt(2, termid);
                
                
                
                //To execute the query and check if there are results
                boolean hasresults = ps.execute();
                        
                if (hasresults) {
                    rs = ps.getResultSet();
                    rsmd = rs.getMetaData();
                
                    StringBuilder jsonResult = new StringBuilder("[");
                    
                    boolean first = true;
                    while(rs.next()) {
                        if (!first) {
                            jsonResult.append(",");
                        }
                        first = false;
                        jsonResult.append("{");
                        
                        for (int index = 1; index <= rsmd.getColumnCount(); index++) {
                            String columnName = rsmd.getColumnName(index);
                            String columnValue = rs.getString(index);
                            
                            if (columnValue == null) {
                                columnValue = "";
                            }
                            
                            jsonResult.append("\"").append(columnName).append("\":\"").append(columnValue).append("\"");
                            
                            if (index < rsmd.getColumnCount()) {
                                jsonResult.append(",");
                            }
                        }
                         
                        jsonResult.append("}");
                    }
                    jsonResult.append("]");
                    
                    result = jsonResult.toString();
                }
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
}
