/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlite;

import java.sql.*;

/**
 *
 * @author przem
 */
public class SqlConnection {
    /**
     * Create instance of connection to SQLite database
     * @param path absolute path to database file
     * @return connection to the SQLite database
     */
    public static Connection Connector(String path){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + path);
            return conn;
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
