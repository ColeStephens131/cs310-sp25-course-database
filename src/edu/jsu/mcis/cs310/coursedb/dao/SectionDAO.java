package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class SectionDAO {
    
    private static final String QUERY_FIND = "SELECT * FROM section WHERE termid = ? AND subjectid = ? AND num = ? ORDER BY crn";
    
    private final DAOFactory daoFactory;
    
    SectionDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public String find(int termid, String subjectid, String num) {
        
        String result = "[]";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                // INSERT YOUR CODE HERE
                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, termid);
                ps.setString(2, subjectid);
                ps.setString(3, num);
                
                boolean hasresults = ps.execute();
                        
                if (hasresults) {
                    rs = ps.getResultSet();
                    rsmd = rs.getMetaData();
                
                    StringBuilder jsonResult = new StringBuilder("[");
                       
                    while(rs.next()) {
                        jsonResult.append("{");
                        
                        for (int index = 1; index <= rsmd.getColumnCount(); index++) {
                            String columnName = rsmd.getColumnName(index);
                            String columnValue = rs.getString(index);
                            
                            jsonResult.append("\"").append(columnName).append("\":\"").append(columnValue).append("\"");
                            
                            if (index < rsmd.getColumnCount()) {
                                jsonResult.append(",");
                            }
                        }
                         
                        jsonResult.append("}, ");
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