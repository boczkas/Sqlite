/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sqlite;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author przem
 */
public class SqlModel {
    
    Connection connection;
    ResultSet resultSet;
    /**
     * Constructor of the SqlModel class
     * @param path Path to the SQLite database file
     */
    public SqlModel(String path){
        this.resultSet = null;
        connection = SqlConnection.Connector(path);
        
        if(connection == null){
            System.out.println("Przemek Connection not successful");
            System.exit(1);
        }
    }
    
    public boolean isDbConnected(){
        try {
            return !connection.isClosed();
        } catch (SQLException ex) {
            Logger.getLogger(SqlModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean isLogin() throws SQLException{
        PreparedStatement preparedStatement = null;
        
        //String query = "SELECT * FROM Connections WHERE SourceDevice = ? AND DestDevice = ?";
        String query = "SELECT * FROM Connections";
        
        try{
            preparedStatement = connection.prepareStatement(query);
            
            resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()){
                return true;
            }
            else{   
                return false;
            }
        }
        catch (SQLException e){
            return false;
        }
        finally{
            preparedStatement.close();
            resultSet.close();
        }
    }
    
    public ResultSet getResultSet() throws SQLException{
        
        String query = "SELECT * FROM Connections";
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        
        return resultSet;
        /*
        for(int i = 0; i < resultSet.getMetaData().getColumnCount(); i++){
            final int j = i;
            TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            System.out.println("Column ["+i+"] ");
        }
        //return new TableColumn<String, String>("Name");
        */
    }
    
    
}
