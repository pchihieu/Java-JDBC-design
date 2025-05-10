package com.vgb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseFactory {
	

	public static Connection openConnection() {
        try {
            String username = "hmonahan2";
            String password = "liz3quijaeBi";
            String url = "jdbc:mysql://nuros.unl.edu/hmonahan2";

            return DriverManager.getConnection(url, username, password);
            
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }
	
	public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
	
	
	public static void closeStatements(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


